package com.chatapp.cloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/messageServiceFallback")
    public Mono<String> messageServiceFallback() {
        return Mono.just("There is a problem with MESSAGE-SERVICE. Please try again later");
    }

    @GetMapping("/groupServiceFallback")
    public Mono<String> groupServiceFallback() {
        return Mono.just("There is a problem with GROUP-SERVICE. Please try again later");
    }

    @GetMapping("/socketServiceFallback")
    public Mono<String> socketServiceFallback() {
        return Mono.just("There is a problem with SOCKET-SERVICE. Please try again later");
    }
}
