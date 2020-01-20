package com.demo.webscanner.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ScannerResultTo {
    private int matchesNumber;


    public int getMatchesNumber() {
        return this.matchesNumber;
    }

    public void setMatchesNumber(int matchesNumber) {
        this.matchesNumber = matchesNumber;
    }
}
