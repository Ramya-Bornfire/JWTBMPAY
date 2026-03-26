package com.bornfire.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class BMPAYTestController {

    @GetMapping("/get")
    public ResponseEntity<Object> getResponse() {

        String url = "https://bipsbtwtddb301.bankofbaroda.co.in:8443/Bipsportal/api/get";
        RestTemplate restTemplate = new RestTemplate();

        // The response might be an array or an object, so use Object.class
        Object response = restTemplate.getForObject(url, Object.class);

        return ResponseEntity.ok(response);
    }
}
