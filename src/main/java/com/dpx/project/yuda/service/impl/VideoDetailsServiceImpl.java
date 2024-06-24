package com.dpx.project.yuda.service.impl;

import com.dpx.project.yuda.model.VideoDetails;
import com.dpx.project.yuda.model.dao.VideoDetailsDao;
import com.dpx.project.yuda.model.dao.VideoStatsDao;
import com.dpx.project.yuda.respository.VideoDetailsRepository;
import com.dpx.project.yuda.respository.VideoStatsRepository;
import com.dpx.project.yuda.service.VideoDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class VideoDetailsServiceImpl implements VideoDetailsService {

    @Autowired
    private VideoDetailsRepository videoDetailsRepository;

    @Autowired
    private VideoStatsRepository videoStatsRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void upsertVideoDetails(String channelId, Map<String, VideoDetails> videoDetailsMap) {
        // TODO: Check if batch upsert is possible or find any optimized way.
        for(Map.Entry<String, VideoDetails> entry: videoDetailsMap.entrySet()){
            VideoDetails videoDetails = entry.getValue();

            VideoDetailsDao videoDetailsDao =
                    videoDetailsRepository.findByVideoId(videoDetails.getId());

            if(Objects.isNull(videoDetailsDao)){
                videoDetailsDao = new VideoDetailsDao()
                        .setVideoId(videoDetails.getId())
                        .setTitle(videoDetails.getTitle())
                        .setDescription(videoDetails.getDescription())
                        .setModifiedDateTime(LocalDateTime.now())
                        .setPublicationDatetime(videoDetails.getPublicationDate())
                        .setIsDeleted(0)
                        .setChannelId(channelId);
                videoDetailsRepository.save(videoDetailsDao);
            }
            else if (
                    !videoDetails.getTitle().equals(videoDetailsDao.getTitle())
                    || !videoDetails.getDescription().equals(videoDetailsDao.getDescription())
            ){
                videoDetailsDao = videoDetailsDao
                        .setTitle(videoDetails.getTitle())
                        .setDescription(videoDetails.getDescription())
                        .setModifiedDateTime(LocalDateTime.now());
                videoDetailsRepository.save(videoDetailsDao);
            }


            //Saving the stats every time when job executes as a new record.
            videoStatsRepository.save(
                    new VideoStatsDao()
                            .setVideoId(videoDetails.getId())
                            .setCommentCount(videoDetails.getCommentCount())
                            .setViewCount(videoDetails.getViewCount())
                            .setLikeCount(videoDetails.getLikeCount())
            );

        }
    }
}
