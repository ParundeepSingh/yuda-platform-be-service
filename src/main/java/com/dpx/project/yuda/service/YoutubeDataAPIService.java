package com.dpx.project.yuda.service;

import com.dpx.project.yuda.model.VideoDetails;
import com.dpx.project.yuda.model.YTApiGenericResponse;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Service which performs actions related to Youtube Data API
 */
public interface YoutubeDataAPIService {

    /**
     * Method fetches the Channel Details based on Channel Handle.
     * @param channelHandle
     * @return
     */
    YTApiGenericResponse getChannelDetails(String channelHandle);

    /**
     * Method fetches the list of Videos corresponding to the channelId. <br>
     * Along, with that fetches the video details like (title, description).
     * @param channelId
     * @return
     */
    ConcurrentHashMap<String, VideoDetails> fetchVideoDetails(String channelId);

    /**
     * Method fetches the Video Statistics (like Video Like, View & Comment Count) based on Channel Handle.
     * @param channelId
     * @param videos
     */
    void fetchVideoStats(String channelId, ConcurrentHashMap<String,VideoDetails> videos);

}
