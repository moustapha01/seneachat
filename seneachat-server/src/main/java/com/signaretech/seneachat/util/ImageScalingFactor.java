package com.signaretech.seneachat.util;

public enum ImageScalingFactor {

    SMALL(40, 80),
    MEDIUM(100, 175),
    LARGE(200, 350),
    XLARGE(400, 600),
    FIXED(96, 96);

    ImageScalingFactor(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private final int width;
    private final int height;

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

}
