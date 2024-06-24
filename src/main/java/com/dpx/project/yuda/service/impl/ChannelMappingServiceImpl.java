package com.dpx.project.yuda.service.impl;

import com.dpx.project.yuda.model.YTApiGenericResponse;
import com.dpx.project.yuda.model.dao.ChannelMappingDao;
import com.dpx.project.yuda.respository.ChannelMappingRepository;
import com.dpx.project.yuda.service.ChannelMappingService;
import com.dpx.project.yuda.service.YoutubeDataAPIService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.dpx.project.yuda.configuration.AppConstants.ID;

@Slf4j
@Service
public class ChannelMappingServiceImpl implements ChannelMappingService {

    @Autowired
    private ChannelMappingRepository channelMappingRepository;

    @Autowired
    private YoutubeDataAPIService youtubeDataAPIService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String fetchChannelId(String channelHandle){
        // Checking if the Channel Handle - ID Mapping already exists in system.
        ChannelMappingDao channelMappingDao = channelMappingRepository.findByChannelHandle(channelHandle);

        if(Objects.nonNull(channelMappingDao)){
            log.info("[ChannelDetailsServiceImpl][fetchChannelId]:: Found the channelId : {} in DB for channel Handle:{}",
                    channelMappingDao.getChannelId(),
                    channelHandle
            );
            return channelMappingDao.getChannelId();
        }

        //Call YOUTUBE Data API to fetch the channel ID.
        String channelId = callYTDataApiForChannelId(channelHandle);
        log.info("[ChannelDetailsServiceImpl][fetchChannelId]:: Found the channelId : {} from YT Data API for channel Handle:{}",
                channelId,
                channelHandle
        );

        // Saving the Mapping into Channel Mapping Repository.
        channelMappingRepository.save(
                new ChannelMappingDao()
                        .setChannelId(channelId)
                        .setChannelHandle(channelHandle)
                        .setIsActive(1)
                        .setCreatedDatetime(LocalDateTime.now())
                        .setModifiedDateTime(LocalDateTime.now())
        );

        return  channelId;
    }

    @SneakyThrows
    private String callYTDataApiForChannelId(String channelHandle){

        YTApiGenericResponse response = youtubeDataAPIService.getChannelDetails(channelHandle);

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
}
