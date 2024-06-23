package com.dpx.project.yuda.client;

import static com.dpx.project.yuda.client.YoutubeDataApiV3Constants.*;

import com.dpx.project.yuda.configuration.AppConfigs;
import com.dpx.project.yuda.model.YTApiGenericResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Configuration
public class YoutubeDataApiV3Client {


    @Autowired
    @Qualifier("youtubeDataApiV3WebClient")
    private WebClient youtubeDataApiV3WebClient;

    @Autowired
    private AppConfigs appConfigs;

    @Autowired
    @Qualifier("yudaObjectMapper")
    private ObjectMapper objectMapper;

    @Retryable(exclude = ValidationException.class, maxAttemptsExpression = "${yt.data.api.retry.config.maxAttempts}",
            backoff = @Backoff(delayExpression = "${yt.data.api.retry.config.maxDelay}"))
    public YTApiGenericResponse getChannelDetails(String channelHandle){
        String response = youtubeDataApiV3WebClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(appConfigs.getYoutubeDataApiV3ChannelsEndpoint())
                                .queryParam(KEY, appConfigs.getYoutubeDataApiV3Key())
                                .queryParam(PART, CONTENT_DETAILS)
                                .queryParam(FOR_HANDLE, channelHandle)
                                .build()
                )
                .retrieve()
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
                                                return Mono.error(new ValidationException(error));
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
                                                return Mono.error(new RetryException(error));
                                            }
                                    );
                        }
                )
                .bodyToMono(String.class)
                .block();


        log.info("[YoutubeDataApiV3Client][getChannelDetails]:: Response : {}", response);
        return this.processResponse(response, YTApiGenericResponse.class);
    }

    @Retryable(exclude = ValidationException.class, maxAttemptsExpression = "${yt.data.api.retry.config.maxAttempts}",
            backoff = @Backoff(delayExpression = "${yt.data.api.retry.config.maxDelay}"))
    public YTApiGenericResponse searchVideos(String channelId){
        String response = youtubeDataApiV3WebClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(appConfigs.getYoutubeDataApiV3SearchEndpoint())
                                .queryParam(KEY, appConfigs.getYoutubeDataApiV3Key())
                                .queryParam(PART, SNIPPET)
                                .queryParam(CHANNEL_ID, channelId)
                                .queryParam(TYPE, VIDEO)
                                .queryParam(MAX_RESULTS, appConfigs.getYoutubeDataApiV3MaxBatchSize())
                                // TODO: Need to include the pagination
                                //queryParam(PAGE_TOKEN, "page_token)
                                .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> {
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(
                                            error -> {
                                                log.error("[YoutubeDataApiV3Client][searchVideos]:: 4XX Client Error for channel-id: {} with error: {}",
                                                        channelId,
                                                        error
                                                );
                                                return Mono.error(new ValidationException(error));
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
                                                log.error("[YoutubeDataApiV3Client][searchVideos]:: 5XX Client Error for channel-id: {} with error: {}",
                                                        channelId,
                                                        error
                                                );
                                                return Mono.error(new RetryException(error));
                                            }
                                    );
                        }
                )
                .bodyToMono(String.class)
                .block();


        log.info("[YoutubeDataApiV3Client][searchVideos]:: Response : {}", response);
        return this.processResponse(response, YTApiGenericResponse.class);
    }


    @Retryable(exclude = ValidationException.class, maxAttemptsExpression = "${yt.data.api.retry.config.maxAttempts}",
            backoff = @Backoff(delayExpression = "${yt.data.api.retry.config.maxDelay}"))
    public YTApiGenericResponse fetchVideoStats(String channelId, List<String> videoIds){
        String response = youtubeDataApiV3WebClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(appConfigs.getYoutubeDataApiV3VideosEndpoint())
                                .queryParam(KEY, appConfigs.getYoutubeDataApiV3Key())
                                .queryParam(PART, STATISTICS)
                                .queryParam(ID, videoIds)
                                .queryParam(MAX_RESULTS, appConfigs.getYoutubeDataApiV3MaxBatchSize())
                                // TODO: Need to include the pagination
                                //queryParam(PAGE_TOKEN, "page_token)
                                .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> {
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(
                                            error -> {
                                                log.error("[YoutubeDataApiV3Client][fetchVideoStats]:: 4XX Client Error for channel-id: {} with error: {}",
                                                        channelId,
                                                        error
                                                );
                                                return Mono.error(new ValidationException(error));
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
                                                log.error("[YoutubeDataApiV3Client][fetchVideoStats]:: 5XX Client Error for channel-id: {} with error: {}",
                                                        channelId,
                                                        error
                                                );
                                                return Mono.error(new RetryException(error));
                                            }
                                    );
                        }
                )
                .bodyToMono(String.class)
                .block();


        log.info("[YoutubeDataApiV3Client][fetchVideoStats]:: Response : {}", response);
        return this.processResponse(response, YTApiGenericResponse.class);
    }




    private <T> T processResponse(String response, Class<T> clazz){
        try{
            return objectMapper.readValue(response, clazz);
        }
        catch (Exception ex){
            throw new ValidationException(ex.getMessage());
        }
    }

}

class YoutubeDataApiV3Constants {
    public static final String KEY = "key";

    public static final String PART = "part";

    public static final String FOR_HANDLE = "forHandle";

    public static final String CONTENT_DETAILS = "contentDetails";

    public static final String SNIPPET = "snippet";

    public static final String STATISTICS = "statistics";

    public static final String CHANNEL_ID = "channelId";

    public static final String ID = "id";

    public static final String TYPE = "type";

    public static final String VIDEO = "video";

    public static final String MAX_RESULTS = "maxResults";

    public static final String PAGE_TOKEN = "pageToken";

}
