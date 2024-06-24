package com.dpx.project.yuda.controller;

import com.dpx.project.yuda.model.trends.ViewCountTrendResponse;
import com.dpx.project.yuda.service.VideoStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that exposes endpoints for fetching View Count Trend
 */

@RestController
@RequestMapping("/rest")
public class ViewCountTrendController {

    @Autowired
    private VideoStatsService videoStatsService;

    @GetMapping("/video/view-count-trend")
    public ResponseEntity<?> getViewCountTrend(@RequestParam String videoId){
        ViewCountTrendResponse response = videoStatsService.getVideoViewCountTrend(videoId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
