package com.dpx.project.yuda.service.impl;

import com.dpx.project.yuda.client.YoutubeDataApiV3Client;
import com.dpx.project.yuda.configuration.AppConfigs;
import com.dpx.project.yuda.model.VideoDetails;
import com.dpx.project.yuda.model.YTApiGenericResponse;
import com.dpx.project.yuda.service.ChannelDetailsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.dpx.project.yuda.configuration.AppConstants.*;

@Slf4j
@Service
public class ChannelDetailsServiceImpl implements ChannelDetailsService {

    @Autowired
    private YoutubeDataApiV3Client youtubeDataApiV3Client;

    @Autowired
    @Qualifier("yudaObjectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    private AppConfigs appConfigs;

    public void upsertChannelDetails(String channelHandle){
        try{
            String channelId = fetchChannelId(channelHandle);

            log.info("[ChannelDetailsServiceImpl][upsertChannelDetails]:: ChannelId is: {} for Channel Handle: {}",
                    channelId,
                    channelHandle);

            ConcurrentHashMap<String,VideoDetails> videos = fetchVideoDetails(channelId);

            log.info("[ChannelDetailsServiceImpl][upsertChannelDetails]:: Videos for Channel Handle: {} is : {}",
                    channelHandle,
                    videos);

            fetchVideoStats(channelId, videos);

            log.info("[ChannelDetailsServiceImpl][upsertChannelDetails]:: After Fetchinh Stats Videos for Channel Handle: {} is : {}",
                    channelHandle,
                    videos);

        }catch (Exception ex){
            log.info("[ChannelDetailsServiceImpl][upsertChannelDetails]:: Exception occurred for channelHandle: {} with exception-message: {}",
                    channelHandle,
                    ex.getMessage(),
                    ex);
        }
    }

    @SneakyThrows
    private String fetchChannelId(String channelHandle){
        // TODO: To check if channel id already exists in our system. No need to fetch it from Youtube DATA API. As it incurs cost.
        YTApiGenericResponse response = youtubeDataApiV3Client.getChannelDetails(channelHandle);

        List<JsonNode> channels = response.getItems();
        if(!channels.isEmpty()
                && Objects.nonNull(channels.get(0))
                &&  channels.get(0).has(ID)){
            return channels.get(0).get(ID).textValue();
        }
        else{
            throw new Exception("Invalid Channel Name !");
        }
    }

    private ConcurrentHashMap<String,VideoDetails> fetchVideoDetails(String channelId){
        YTApiGenericResponse response = youtubeDataApiV3Client.searchVideos(channelId);

        ConcurrentHashMap<String,VideoDetails> videos = new ConcurrentHashMap<>();

        List<JsonNode> channels = response.getItems();
        for(JsonNode jsonNode: channels){
            JsonNode snippet = jsonNode.get(SNIPPET);

            JsonNode idNode = jsonNode.get(ID);
            JsonNode videoIdNode = idNode.get(VIDEO_ID);
            String videoId = videoIdNode.asText();

            VideoDetails videoDetails =  new VideoDetails();
            JsonNode title = snippet.get(TITLE);
            if(Objects.nonNull(title)) videoDetails.setTitle(title.textValue());

            JsonNode description = snippet.get(DESCRIPTION);
            if(Objects.nonNull(description)) videoDetails.setDescription(description.textValue());

            JsonNode publicationDate =  snippet.get(PUBLISHED_AT);
            String publicationDateStr = publicationDate.textValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            LocalDateTime dateTime = LocalDateTime.parse(publicationDateStr, formatter);
            videoDetails.setPublicationDate(dateTime);

            videos.put(videoId,videoDetails);
        }

        return videos;
    }

    private void fetchVideoStats(String channelId, ConcurrentHashMap<String,VideoDetails> videos) {
        List<String> videoIds = videos.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
        int batchSize = appConfigs.getYoutubeDataApiV3MaxBatchSize();


        IntStream.range(0, (videoIds.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> videoIds.subList(i * batchSize, Math.min(batchSize * (i + 1), videoIds.size())))
                .forEach(
                        //TODO: Check if parallel stream can be applied to send multiple requests at the same time.
                        x -> fetchVideoStatsForBatch(channelId, videos, x)
                );
    }


    private void fetchVideoStatsForBatch(String channelId, ConcurrentHashMap<String,VideoDetails> videoDetails, List<String> videoIds){
        YTApiGenericResponse response = youtubeDataApiV3Client.fetchVideoStats(channelId, videoIds);

        List<JsonNode> items = response.getItems();

        for(JsonNode item: items){
            JsonNode idNode = item.get(ID);
            String videoId = idNode.asText();

            JsonNode viewCount = item.at("/" + STATISTICS + "/" + VIEW_COUNT);
            long viewCountVal  = viewCount.asLong();

            JsonNode likeCount = item.at("/" + STATISTICS + "/" + LIKE_COUNT);
            long likeCountVal  = likeCount.asLong();

            JsonNode commentCount = item.at("/" + STATISTICS + "/" + COMMENT_COUNT);
            long commentCountVal  = commentCount.asLong();

            VideoDetails videoDetailCurrent = videoDetails.get(videoId);
            videoDetailCurrent.setLikeCount(likeCountVal);
            videoDetailCurrent.setViewCount(viewCountVal);
            videoDetailCurrent.setCommentCount(commentCountVal);

            videoDetails.put(videoId, videoDetailCurrent);
        }
    }
}
