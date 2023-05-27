package com.nomura.sample.dataservice.domain;

import lombok.Data;

import java.util.Date;

/**
 * Sample data for data-service
 */
@Data
public class SampleData implements Cloneable {
    private String author;
    private String email;
    private String department;
    private String location;
    private String webex;

    private int retrievalCost;
    private Date retrievalTime;
    @Override
    public SampleData clone() throws CloneNotSupportedException {
        return (SampleData) super.clone();
    }
}
