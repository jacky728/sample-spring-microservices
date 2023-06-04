package com.nomura.sample.dataservice.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import static com.nomura.sample.dataservice.utils.InetUtils.hostIp;
import static com.nomura.sample.dataservice.utils.InetUtils.hostName;

/**
 * FallbackFactory to provide fallback process for SampleFeignClient
 */
@Slf4j
@Component
public class SampleFeignFallbackFactory implements FallbackFactory<SampleFeignClient> {

    @Override
    public SampleFeignClient create(Throwable cause) {
        return name -> {
            log.error("Failed to retrieve sample data thru FeignClient, using fallback data instead! " +
                            "Client host name={}, ip={}",  name, hostName, hostIp, cause);
            SampleData fallbackData = new SampleData();
            fallbackData.setName("Fallback Name");
            return fallbackData;
        };
    }
}
