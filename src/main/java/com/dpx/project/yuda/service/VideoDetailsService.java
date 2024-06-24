package com.dpx.project.yuda.service;

import com.dpx.project.yuda.model.VideoDetails;

/**
 * Service which performs actions related to Upserting Video Details
 */
import java.util.Map;

public interface VideoDetailsService {

    /**
     * Methods upserts the Video Details in our system.
     * @param channelId
     * @param videoDetailsMap
     */
    void upsertVideoDetails(String channelId, Map<String, VideoDetails> videoDetailsMap);
}
