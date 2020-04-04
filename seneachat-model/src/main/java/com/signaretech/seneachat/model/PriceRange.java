package com.signaretech.seneachat.model;

public class PriceRange {

    private Long minValue;
    private Long maxValue;

    public PriceRange() {
    }

    public PriceRange(Long minValue, Long maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
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
