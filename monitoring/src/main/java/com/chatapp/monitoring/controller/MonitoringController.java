package com.chatapp.monitoring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringController {

    @GetMapping(path = "/monitor")
    public String monitor() {
        return "Monitoring";
    }
}
