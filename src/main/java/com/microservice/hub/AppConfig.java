package com.microservice.hub;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfig is a configuration class used to configure and bootstrap the spring application
 *
 * */
@Configuration
public class AppConfig {

    /**
     * The restTemplate() bean method creates and configures an instance of RestTemplate.
     *  ProtobufHttpMessageConverter, allows the RestTemplate to handle HTTP requests
     *  and responses containing Protobuf (Protocol Buffers) data.
     *
     * */
    @Bean
    public RestTemplate restTemplate(ProtobufHttpMessageConverter protobufHttpMessageConverter) {
        return new RestTemplateBuilder()
                .additionalMessageConverters(protobufHttpMessageConverter)
                .build();
    }

    @Bean
    public ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

}