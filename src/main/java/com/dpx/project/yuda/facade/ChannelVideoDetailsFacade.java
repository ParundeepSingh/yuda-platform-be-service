package com.dpx.project.yuda.facade;

import com.dpx.project.yuda.client.YoutubeDataApiV3Client;
import com.dpx.project.yuda.configuration.AppConfigs;
import com.dpx.project.yuda.model.VideoDetails;
import com.dpx.project.yuda.service.ChannelMappingService;
import com.dpx.project.yuda.service.VideoDetailsService;
import com.dpx.project.yuda.service.YoutubeDataAPIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.concurrent.ConcurrentHashMap;

/**
 * Facade Layer responsible for Fetching the Channel Details, Updating it in our system and finding/storing trends.
 */
@Slf4j
@Service
public class ChannelVideoDetailsFacade {

    @Autowired
    private YoutubeDataApiV3Client youtubeDataApiV3Client;

    @Autowired
    private ChannelMappingService channelMappingService;

    @Autowired
    private VideoDetailsService videoDetailsService;

    @Autowired
    private YoutubeDataAPIService youtubeDataAPIService;

    @Autowired
    private AppConfigs appConfigs;

    @Async("threadPoolTaskExecutor")
    public void upsertChannelDetails(String channelHandle){
        try{
            String channelId = channelMappingService.fetchChannelId(channelHandle);

            log.info("[ChannelDetailsServiceImpl][upsertChannelDetails]:: ChannelId is: {} for Channel Handle: {}",
                    channelId,
                    channelHandle);

            ConcurrentHashMap<String,VideoDetails> videos = youtubeDataAPIService.fetchVideoDetails(channelId);

            log.info("[ChannelDetailsServiceImpl][upsertChannelDetails]:: Videos for Channel Handle: {} is : {}",
                    channelHandle,
                    videos);

            youtubeDataAPIService.fetchVideoStats(channelId, videos);

            log.info("[ChannelDetailsServiceImpl][upsertChannelDetails]:: After Fetching Stats Videos for Channel Handle: {} is : {}",
                    channelHandle,
                    videos);

            //Update the video details in DB
            videoDetailsService.upsertVideoDetails(channelId,videos);

        }catch (Exception ex){
            log.info("[ChannelDetailsServiceImpl][upsertChannelDetails]:: Exception occurred for channelHandle: {} with exception-message: {}",
                    channelHandle,
                    ex.getMessage(),
                    ex);
        }
    }

}
