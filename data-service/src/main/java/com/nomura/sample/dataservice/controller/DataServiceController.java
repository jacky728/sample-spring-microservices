package com.nomura.sample.dataservice.controller;

import com.nomura.sample.dataservice.config.SampleDataConfig;
import com.nomura.sample.dataservice.domain.SampleData;
import com.nomura.sample.dataservice.domain.SampleDataResponse;
import com.nomura.sample.dataservice.domain.SampleFeignClient;
import feign.FeignException;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import static com.nomura.sample.dataservice.utils.InetUtils.hostIp;
import static com.nomura.sample.dataservice.utils.InetUtils.hostName;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DataServiceController {
    /**
     * Exposed sample timer metrics to endpoint: /actuator/metrics/sample.timer
     */
    private Timer timer = Metrics.timer("sample.timer", "timer", "sample");

    /**
     * Sample data configuration with defaultCostMillis
     */
    private final SampleDataConfig config;

    /**
     * Autowired OpenFeign client instance
     */
    private final SampleFeignClient feignClient;

    /**
     * Get sample data through OpenFeign and mock the time cost by request param or default value by configuration
     * @param name The name to retrieve sample data through Feign client interface with default value of author name
     * @param cost The time cost if specified, using positive number for fix cost, and negative number for random cost with up bound.
     * @return Configured sample data with retrieval cost and time
     */
    @GetMapping("/sample")
    public ResponseEntity<SampleDataResponse> getSampleWithCost(
            @RequestParam(name = "name", defaultValue = "Fujun") String name,
            @RequestParam(name = "cost", required = false) Integer cost) {

        Date time = new Date();
        final int costMillis = (cost == null) ? config.getDefaultCostMillis() : // default cost by config if not specified
                (cost > 0) ? cost : new Random(time.getTime()).nextInt(-cost);  // using negative number for random cost with up bound
        log.info("To get sample data may cost {} millis on host={}, ip={}", costMillis, hostName, hostIp);

        timer.record(() -> timeCost(costMillis));       // cost time and record timer metrics

        try {
            SampleData data = feignClient.getSampleData(name);      // retrieve sample data thru Feign client

            SampleDataResponse response = new SampleDataResponse(data);
            response.setRetrievalTime(time);
            response.setRetrievalCost(costMillis);
            response.setRetrievalHostName(hostName);
            response.setRetrievalHostIp(hostIp);

            log.info("Got sample data after {} millis: {}", costMillis, data);
            return ResponseEntity.ok(response);
        } catch (FeignException e) {
            log.error("Retrieving data through OpenFeign error [{}]!", e.status(), e);
            return ResponseEntity.status(e.status()).build();
        }
    }

    /**
     * Retrieve the sample data from configuration
     * @param name The name to retrieve sample data in configured list
     * @return Configured sample data containing the name
     */
    @GetMapping("/sample/data")
    public ResponseEntity<SampleData> getSampleData(@RequestParam("name") String name) {
        log.info("Retrieve sample data by name={} on host={}, ip={}", name, hostName, hostIp);

        return Arrays.stream(config.getData())
                .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                .findAny()
                .map(s -> ResponseEntity.ok(s))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private void timeCost(int costMillis) {
        try {
            Thread.sleep(costMillis);
        } catch (InterruptedException e) {
            log.error("Interrupted while costs time={} millis", costMillis);
        }
    }
}
