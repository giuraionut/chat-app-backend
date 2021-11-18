package com.chatapp.cloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/userServiceFallback")
    public Mono<String> userServiceFallback() {
        return Mono.just("There is a problem with USER-SERVICE. Please try again later");
    }

    @GetMapping("/messageServiceFallback")
    public Mono<String> messageServiceFallback() {
        return Mono.just("There is a problem with MESSAGE-SERVICE. Please try again later");
    }

    @GetMapping("/channelServiceFallback")
    public Mono<String> channelServiceFallback() {
        return Mono.just("There is a problem with the CHANNEL-SERVICE. Please try again later");
    }
}
