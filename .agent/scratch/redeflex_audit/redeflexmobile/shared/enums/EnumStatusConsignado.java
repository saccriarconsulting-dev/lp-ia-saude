package com.axys.redeflexmobile.shared.enums;

public enum EnumStatusConsignado {
    ATIVA(0), ANDAMENTO(1), CANCELADO(2), CONCLUIDO(3);

    private final int value;

    EnumStatusConsignado(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
