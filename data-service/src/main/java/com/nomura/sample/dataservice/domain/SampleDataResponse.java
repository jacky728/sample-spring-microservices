package com.nomura.sample.dataservice.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * Sample data with retrieval details
 */
@Data
@RequiredArgsConstructor
public class SampleDataResponse {
    private final SampleData data;

    private int retrievalCost;
    private Date retrievalTime;
    private String retrievalHostName;
    private String retrievalHostIp;
}
