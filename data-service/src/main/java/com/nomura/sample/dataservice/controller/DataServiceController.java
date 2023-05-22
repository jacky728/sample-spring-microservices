package com.nomura.sample.dataservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DataServiceController {
    @GetMapping("/sleep")
    public String sleep(@RequestParam(name = "millis", defaultValue = "1000") int millis) throws InterruptedException {
        log.info("About to sleep {} millis...", millis);
        Thread.sleep(millis);
        return String.format("Awake after sleeping %d millis!", millis);
    }
}
