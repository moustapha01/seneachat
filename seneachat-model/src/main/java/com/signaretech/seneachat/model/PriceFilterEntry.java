package com.signaretech.seneachat.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PriceFilterEntry {

    private String label;
    private Long minValue;
    private Long maxValue;


    public PriceFilterEntry() {
    }

    public PriceFilterEntry(String label, Long minValue, Long maxValue) {
        this.label = label;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }
}
