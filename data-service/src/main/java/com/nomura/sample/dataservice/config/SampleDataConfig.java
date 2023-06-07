package com.nomura.sample.dataservice.config;

import com.nomura.sample.dataservice.domain.SampleData;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Sample data config with defaultCostMillis and randomCostMillisLimit
 */
@Data
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "sample")
public class SampleDataConfig {

    private int defaultCostMillis = 1000;

    private SampleData[] data;

}
