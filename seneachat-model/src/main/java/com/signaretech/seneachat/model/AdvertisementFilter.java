package com.signaretech.seneachat.model;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementFilter {

    private PriceRange priceRange;
    private Long selectedPrice;
    private List<String> selectedCategories = new ArrayList<>(10);
    private List<ItemCondition> selectedConditions;

    public AdvertisementFilter() {
    }

    public AdvertisementFilter(Long selectedPrice, List<String> selectedCategories, List<ItemCondition> selectedConditions) {
        this.selectedPrice = selectedPrice;
        this.selectedCategories = selectedCategories;
        this.selectedConditions = selectedConditions;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    public Long getSelectedPrice() {
        return selectedPrice;
    }

    public void setSelectedPrice(Long selectedPrice) {
        this.selectedPrice = selectedPrice;
    }

    public List<String> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<String> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public List<ItemCondition> getSelectedConditions() {
        return selectedConditions;
    }

    public void setSelectedConditions(List<ItemCondition> selectedConditions) {
        this.selectedConditions = selectedConditions;
    }
}
