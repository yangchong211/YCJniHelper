package com.yc.jnidemo.second;

public enum ForegroundColorEnum {

    RED("#f44336"),
    LIGHT_BLUE("#03a9f4"),
    GREEN("#4caf50"),
    YELLOW("#ffeb3b"),
    DEEP_ORANGE("#ff5722"),
    BLUE_GREY("#607d8b"),
    DEFAULT_DARK("#1f000000"),
    DEFAULT_LIGHT("#33ffffff");

    String color;

    ForegroundColorEnum(String color) {
        this.color = color;
    }

}
