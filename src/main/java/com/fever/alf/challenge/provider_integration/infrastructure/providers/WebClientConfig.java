package com.fever.alf.challenge.provider_integration.infrastructure.providers;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration

public class WebClientConfig {
    private final ProviderProperties props;

    public WebClientConfig(ProviderProperties props) {
        this.props = props;
    }

    @Bean
    public WebClient providerWebClient() {
        HttpClient client = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        (int) props.getRequestTimeout().toMillis())
                .doOnConnected(c -> c
                        .addHandlerLast(
                                new ReadTimeoutHandler(props.getRequestTimeout().getSeconds(), TimeUnit.SECONDS))
                        .addHandlerLast(
                                new WriteTimeoutHandler(props.getRequestTimeout().getSeconds(), TimeUnit.SECONDS)));

        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }
}
