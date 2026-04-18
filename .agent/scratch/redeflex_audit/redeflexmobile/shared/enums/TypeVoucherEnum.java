package com.axys.redeflexmobile.shared.enums;
import java.util.Arrays;
import java.util.List;

public enum TypeVoucherEnum {
    MERCHAN_SKY(1, "Merchan Sky"),
    TA(2, "Comprovante T. A."),
    MERCHAN_CLARO(3, "Merchan Claro");

    public final int value;
    public final String displayText;

    TypeVoucherEnum(int value, String displayText) {
        this.value = value;
        this.displayText = displayText;
    }

    public static TypeVoucherEnum intToTypeVoucherEnum(int value) {
        for (TypeVoucherEnum tp : TypeVoucherEnum.values()) {
            if (tp.value == value) {
                return tp;
            }
        }
        return null;
    }

    public static TypeVoucherEnum displayToTypeVoucherEnum(String displayText) {
        for (TypeVoucherEnum tp : TypeVoucherEnum.values()) {
            if (tp.displayText == displayText) {
                return tp;
            }
        }
        return null;
    }

    public static List<TypeVoucherEnum> getEnumList() {
        return Arrays.asList(MERCHAN_SKY, MERCHAN_CLARO);
    }

    public static List<String> getEnumDisplayList() {
        return Arrays.asList(TA.displayText, MERCHAN_CLARO.displayText, MERCHAN_SKY.displayText);
    }
}