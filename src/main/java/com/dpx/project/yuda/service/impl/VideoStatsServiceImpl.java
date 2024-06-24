package com.dpx.project.yuda.service.impl;

import com.dpx.project.yuda.configuration.AppConfigs;
import com.dpx.project.yuda.model.dao.VideoStatsDao;
import com.dpx.project.yuda.model.trends.ViewCountRecord;
import com.dpx.project.yuda.model.trends.ViewCountTrendResponse;
import com.dpx.project.yuda.respository.VideoStatsRepository;
import com.dpx.project.yuda.service.VideoStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VideoStatsServiceImpl implements VideoStatsService {

    @Autowired
    private VideoStatsRepository videoStatsRepository;

    @Autowired
    private AppConfigs appConfigs;

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewCountTrendResponse getVideoViewCountTrend(String videoId) {
        List<VideoStatsDao> videoStatsDaos =
                videoStatsRepository.getLatestRecordsByVideoId(videoId,appConfigs.getVideoViewCountTrendRecordLimit());

        return mapVideoStatsDaosToViewCountTrendResponse(videoId,videoStatsDaos);
    }


    private ViewCountTrendResponse mapVideoStatsDaosToViewCountTrendResponse(String videoId, List<VideoStatsDao> videoStatsDaos){

        List<ViewCountRecord> viewCountRecords = new ArrayList<>();
        for(VideoStatsDao videoStatsDao: videoStatsDaos){
            ViewCountRecord viewCountRecord = ViewCountRecord.builder()
                    .viewCount(videoStatsDao.getViewCount())
                    .timestamp(videoStatsDao.getCreatedDatetime())
                    .build();
            viewCountRecords.add(viewCountRecord);
        }

        return ViewCountTrendResponse.builder()
                .videoId(videoId)
                .viewCount(viewCountRecords)
                .build();
    }
}
