package com.dpx.project.yuda.service.impl;


import com.dpx.project.yuda.client.YoutubeDataApiV3Client;
import com.dpx.project.yuda.configuration.AppConfigs;
import com.dpx.project.yuda.model.VideoDetails;
import com.dpx.project.yuda.model.YTApiGenericResponse;
import com.dpx.project.yuda.service.YoutubeDataAPIService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.dpx.project.yuda.configuration.AppConstants.*;
import static com.dpx.project.yuda.configuration.AppConstants.PUBLISHED_AT;

@Slf4j
@Service
public class YoutubeDataAPIServiceImpl implements YoutubeDataAPIService {

    @Autowired
    private YoutubeDataApiV3Client youtubeDataApiV3Client;

    @Autowired
    private AppConfigs appConfigs;

    /**
     * {@inheritDoc}
     */
    @Override
    public ConcurrentHashMap<String, VideoDetails> fetchVideoDetails(String channelId){
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
            videoDetails.setId(videoId);

            videos.put(videoId,videoDetails);
        }

        return videos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fetchVideoStats(String channelId, ConcurrentHashMap<String,VideoDetails> videos) {
        List<String> videoIds = videos.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
        int batchSize = appConfigs.getYoutubeDataApiV3MaxBatchSize();


        IntStream.range(0, (videoIds.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> videoIds.subList(i * batchSize, Math.min(batchSize * (i + 1), videoIds.size())))
                .forEach(
                        //TODO: Check if parallel stream can be applied to send multiple requests at the same time.
                        x -> fetchVideoStatsForBatch(channelId, videos, x));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public YTApiGenericResponse getChannelDetails(String channelHandle) {
        return youtubeDataApiV3Client.getChannelDetails(channelHandle);
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
