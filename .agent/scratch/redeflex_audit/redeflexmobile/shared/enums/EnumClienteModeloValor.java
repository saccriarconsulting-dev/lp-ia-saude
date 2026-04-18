package com.axys.redeflexmobile.shared.enums;

/**
 * @author Rogério Massa on 07/12/18.
 */

public enum EnumClienteModeloValor {

    NEGATIVO(0, "Não", false),
    POSITIVO(1, "Sim", true);

    private int valueInt;
    private String valueString;
    private boolean valueBool;

    EnumClienteModeloValor(int valueInt, String valueString, boolean valueBool) {
        this.valueInt = valueInt;
        this.valueString = valueString;
        this.valueBool = valueBool;
    }

    public int getIntValue() {
        return valueInt;
    }

    public String getStringValue() {
        return valueString;
    }

    public static EnumClienteModeloValor getEnumByValue(int value) {
        for (EnumClienteModeloValor item : EnumClienteModeloValor.values()) {
            if (item.valueInt == value) {
                return item;
            }
        }
        return NEGATIVO;
    }

    public static EnumClienteModeloValor getEnumByValue(String value) {
        if (value == null) return NEGATIVO;
        for (EnumClienteModeloValor item : EnumClienteModeloValor.values()) {
            if (item.valueString.equals(value)) {
                return item;
            }
        }
        return NEGATIVO;
    }

    public static EnumClienteModeloValor getEnumByValue(boolean value) {
        for (EnumClienteModeloValor item : EnumClienteModeloValor.values()) {
            if (item.valueBool == value) {
                return item;
            }
        }
        return NEGATIVO;
    }
}
