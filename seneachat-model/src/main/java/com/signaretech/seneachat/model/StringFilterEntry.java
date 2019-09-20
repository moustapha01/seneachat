package com.signaretech.seneachat.model;

public class StringFilterEntry {

    private String label;

    private String value;

    public StringFilterEntry() {
    }

    public StringFilterEntry(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
