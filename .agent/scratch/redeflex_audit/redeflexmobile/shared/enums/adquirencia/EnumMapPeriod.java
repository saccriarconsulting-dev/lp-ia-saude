package com.axys.redeflexmobile.shared.enums.adquirencia;

/**
 * @author Rogério Massa on 27/11/18.
 */

public enum EnumMapPeriod {

    CURRENT(""),
    THIRTY("30"),
    SIXTY("60"),
    NINETY("90");

    private String value;

    EnumMapPeriod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
