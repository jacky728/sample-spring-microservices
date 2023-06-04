package com.nomura.sample.dataservice.domain;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OpenFeign client interface to retrieve data from /sample/data endpoint
 */
@FeignClient(name="${sample.feign.client.name}", url="${sample.feign.client.url}",
        fallbackFactory = SampleFeignFallbackFactory.class)
public interface SampleFeignClient {
    @GetMapping("/sample/data")
    SampleData getSampleData(@RequestParam("name") String name);

}
