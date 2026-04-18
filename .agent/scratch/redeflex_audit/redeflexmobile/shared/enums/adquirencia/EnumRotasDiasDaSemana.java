package com.axys.redeflexmobile.shared.enums.adquirencia;

import com.annimon.stream.Stream;

/**
 * @author Rogério Massa on 26/11/18.
 */

public enum EnumRotasDiasDaSemana {

    SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(7);

    private int value;

    EnumRotasDiasDaSemana(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EnumRotasDiasDaSemana getEnumByValue(int value) {
        return Stream.of(values())
                .filter(item -> item.getValue() == value)
                .findFirst()
                .get();
    }
}
