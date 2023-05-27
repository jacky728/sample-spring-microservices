package com.nomura.sample.dataservice.controller;

import com.nomura.sample.dataservice.config.SampleDataConfig;
import com.nomura.sample.dataservice.domain.SampleData;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DataServiceController {
    /**
     * Exposed sample timer metrics to endpoint: /actuator/metrics/sample.timer
     */
    private Timer timer = Metrics.timer("sample.timer", "timer", "sample");

    /**
     * Sample data config with defaultCostMillis and randomCostMillisLimit
     */
    private final SampleDataConfig config;

    /**
     * Get sample data from configuration and mock the time cost by request param, or random or default value by configuration
     * @param cost The time cost if specified
     * @param random Random time cost if cost not specified, upper limit is configured by ${random.random-cost-millis-limit}
     * @return Configured sample data with retrieval cost and time
     */
    @GetMapping("/sample")
    public ResponseEntity<SampleData> getSampleData(
            @RequestParam(name = "cost", required = false) Integer cost,
            @RequestParam(name = "random", defaultValue = "false") boolean random) {
        Date time = new Date();
        final int costMillis = (cost != null) ? cost :  // request param specified cost, else random or default cost by config
                (random ? new Random(time.getTime()).nextInt(config.getRandomCostMillisLimit()) : config.getDefaultCostMillis());
        log.info("To get sample data may cost {} millis", costMillis);

        timer.record(() -> timeCost(costMillis));       // cost time and record timer metrics

        SampleData data;
        try {
            data = config.getData().clone();
            data.setRetrievalTime(time);
            data.setRetrievalCost(costMillis);
        } catch (CloneNotSupportedException e) {
            log.error("Retrieving data error!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        log.info("Got sample data after {} millis: {}", costMillis, config.getData());
        return ResponseEntity.ok(data);
    }

    private void timeCost(int costMillis) {
        try {
            Thread.sleep(costMillis);
        } catch (InterruptedException e) {
            log.error("Interrupted while costs time={} millis", costMillis);
        }
    }
}
