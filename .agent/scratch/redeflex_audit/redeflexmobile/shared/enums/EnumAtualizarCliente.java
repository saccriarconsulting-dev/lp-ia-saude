package com.axys.redeflexmobile.shared.enums;

public enum EnumAtualizarCliente {

    CONCLUIDO(0), ANDAMENTO(1);

    private final int value;

    EnumAtualizarCliente(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
