package com.dpx.project.yuda.controller;


import com.dpx.project.yuda.client.YoutubeDataApiV3Client;
import com.dpx.project.yuda.facade.ChannelVideoDetailsFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rest")
public class ChannelDetailsController {

    @Autowired
    private YoutubeDataApiV3Client youtubeDataApiV3Client;

    @Autowired
    private ChannelVideoDetailsFacade channelVideoDetailsFacade;


    @GetMapping("/channel-details/update")
    public ResponseEntity<?> updateChannelStats(@RequestParam String channelHandle){
        try{
            channelVideoDetailsFacade.upsertChannelDetails(channelHandle);
        }
        catch (Exception ex){
            log.error("[ChannelDetailsController][updateChannelStats]:: Exception occurred for Channel Handle : {}, and " +
                    "ex: {}",
                    channelHandle,
                    ex.getMessage()
                    ,ex);
        }
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
