package com.chatapp.cloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/userServiceFallback")
    public String userServiceFallback() {
        return "There is a problem with USER-SERVICE. Please try again later";
    }

    @GetMapping("/directMessageServiceFallback")
    public String directMessageServiceFallback() {
        return "There is a problem with DIRECT-MESSAGE-SERVICE. Please try again later";
    }
}
