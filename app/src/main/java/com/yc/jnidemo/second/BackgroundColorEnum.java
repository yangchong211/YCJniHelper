package com.yc.jnidemo.second;

public enum BackgroundColorEnum {

    RED("#F44336"),
    LIGHT_BLUE("#03A9F4"),
    GREEN("#4CAF50"),
    YELLOW("#FFEB3B"),
    DEEP_ORANGE("#FF5722"),
    BLUE_GREY("#607D8B"),
    WHITE("#FFFFFF");

    String color;

    BackgroundColorEnum(String color) {
        this.color = color;
    }

}
