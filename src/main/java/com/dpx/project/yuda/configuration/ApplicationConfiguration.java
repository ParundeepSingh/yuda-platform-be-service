package com.dpx.project.yuda.configuration;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


@EnableAsync
@EnableRetry
@Configuration
public class ApplicationConfiguration {

    @Autowired
    private AppConfigs appConfigs;

    @Bean("yudaObjectMapper")
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean(name = "threadPoolTaskExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(appConfigs.getAsyncExecutorCorePoolSize());
        executor.setMaxPoolSize(appConfigs.getAsyncExecutorMaxPoolSize());
        executor.setQueueCapacity(appConfigs.getAsyncExecutorQueueCapacity());
        executor.setThreadNamePrefix("AsyncThread::");
        executor.initialize();
        return executor;
    }

    @Bean("youtubeDataApiV3WebClient")
    public WebClient youtubeDataApiV3WebClient(){
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(clientCodecConfigurer ->
                                clientCodecConfigurer.defaultCodecs().maxInMemorySize(1000000))
                        .build())
                .clientConnector(reactorClientHttpConnector(
                        appConfigs.getYoutubeDataApiV3ConnectionTimeout(),
                        appConfigs.getYoutubeDataApiV3ReadTimeout(),
                        appConfigs.getYoutubeDataApiV3WriteTimeout()
                ))
                .baseUrl(appConfigs.getYoutubeDataApiV3BaseUrl())
                .build();
    }

    private ReactorClientHttpConnector reactorClientHttpConnector(int connectionTimeout, int readTimeout, int writeTimeout){
        HttpClient httpClient =
                HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                        .doOnConnected(
                                connection -> {
                                    connection.addHandlerFirst(
                                            new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS)
                                    );
                                    connection.addHandlerFirst(
                                            new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS)
                                    );
                                }
                        );
        return new ReactorClientHttpConnector(httpClient);
    }
}
