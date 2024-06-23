package com.dpx.project.yuda.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component
public class AppConfigs {

    @Value("${yt.data.api.v3.connection.timeout}")
    private int youtubeDataApiV3ConnectionTimeout;

    @Value("${yt.data.api.v3.read.timeout}")
    private int youtubeDataApiV3ReadTimeout;

    @Value("${yt.data.api.v3.write.timeout}")
    private int youtubeDataApiV3WriteTimeout;

    @Value("${yt.data.api.v3.base.url}")
    private String youtubeDataApiV3BaseUrl;

    @Value("${yt.data.api.v3.key}")
    private String youtubeDataApiV3Key;

    @Value("${yt.data.api.v3.channels.endpoint}")
    private String youtubeDataApiV3ChannelsEndpoint;

    @Value("${yt.data.api.v3.search.endpoint}")
    private String youtubeDataApiV3SearchEndpoint;

    @Value("${yt.data.api.v3.videos.endpoint}")
    private String youtubeDataApiV3VideosEndpoint;

    @Value("${yt.data.api.v3.max.batch.size:10}")
    private Integer youtubeDataApiV3MaxBatchSize;

}
