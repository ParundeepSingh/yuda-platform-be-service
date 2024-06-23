package com.dpx.project.yuda.client;

import static com.dpx.project.yuda.client.YoutubeDataApiV3Constants.*;

import com.dpx.project.yuda.configuration.AppConfigs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class YoutubeDataApiV3Client {


    @Autowired
    @Qualifier("youtubeDataApiV3WebClient")
    private WebClient youtubeDataApiV3WebClient;

    @Autowired
    private AppConfigs appConfigs;


    public void getChannelDetails(String channelHandle){

        String res = youtubeDataApiV3WebClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(appConfigs.getYoutubeDataApiV3ChannelsEndpoint())
                                .queryParam(KEY, appConfigs.getYoutubeDataApiV3Key())
                                .queryParam(PART, CONTENT_DETAILS)
                                .queryParam(FOR_HANDLE, channelHandle)
                                .build()
                ).retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> {
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(
                                            error -> {
                                                log.error("[YoutubeDataApiV3Client][getChannelDetails]:: 4XX Client Error for channel-handle: {} with error: {}",
                                                        channelHandle,
                                                        error
                                                        );
                                            // TODO: Change it to Client error.
                                            return Mono.error(new Exception(error));
                                            }
                                    );
                        }
                        )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> {
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(
                                            error -> {
                                                log.error("[YoutubeDataApiV3Client][getChannelDetails]:: 5XX Client Error for channel-handle: {} with error: {}",
                                                        channelHandle,
                                                        error
                                                );
                                                // TODO: Change it to Server ERROR.
                                                return Mono.error(new Exception(error));
                                            }
                                    );
                        }
                ).bodyToMono(String.class)
                .block();

        log.info("[YoutubeDataApiV3Client][getChannelDetails]:: Response : {}", res);
    }

}

class YoutubeDataApiV3Constants {
    public static final String KEY = "key";

    public static final String PART = "part";

    public static final String FOR_HANDLE = "forHandle";

    public static final String CONTENT_DETAILS = "contentDetails";
}
