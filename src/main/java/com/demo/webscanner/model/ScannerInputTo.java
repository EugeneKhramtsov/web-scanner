package com.demo.webscanner.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ScannerInputTo {
    private String url;
    private String lookingFor;
    private Integer maxNumberOfPagesToScan;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public Integer getMaxNumberOfPagesToScan() {
        return maxNumberOfPagesToScan;
    }

    public void setMaxNumberOfPagesToScan(final Integer maxNumberOfPagesToScan) {
        this.maxNumberOfPagesToScan = maxNumberOfPagesToScan;
    }
}
