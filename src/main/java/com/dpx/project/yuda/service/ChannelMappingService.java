package com.dpx.project.yuda.service;

/**
 * Service which performs actions related to Channel Mapping
 */
public interface ChannelMappingService {

    /**
     * Method fetches the Channel ID based on Channel Handle
     * @param channelHandle
     * @return
     */
    String fetchChannelId(String channelHandle);
}
