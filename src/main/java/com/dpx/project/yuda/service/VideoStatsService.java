package com.dpx.project.yuda.service;

import com.dpx.project.yuda.model.trends.ViewCountTrendResponse;


/**
 * Service which performs actions related to Video Statistics Details.
 */
public interface VideoStatsService {

    /**
     * Method fetches the Video View Count Trend by Video ID.
     * @param videoId
     * @return
     */
    ViewCountTrendResponse getVideoViewCountTrend(String videoId);
}
