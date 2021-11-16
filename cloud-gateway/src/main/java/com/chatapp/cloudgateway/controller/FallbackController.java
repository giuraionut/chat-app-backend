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

    @GetMapping("/directMessageServiceFallback")
    public Mono<String> directMessageServiceFallback() {
        return Mono.just("There is a problem with DIRECT-MESSAGE-SERVICE. Please try again later");
    }

    @GetMapping("/webSocketServiceFallback")
    public Mono<String> webSocketServiceFallback() {
        return Mono.just("There is a problem with the WEBSOCKET. Please try again later");
    }
}
