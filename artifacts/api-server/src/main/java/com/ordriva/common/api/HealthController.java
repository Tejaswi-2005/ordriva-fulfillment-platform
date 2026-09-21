package com.ordriva.common.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {
    @GetMapping("/healthz")
    public ApiResponse<Map<String, String>> health() {
        return ApiResponse.of(Map.of("status", "ok"));
    }
}