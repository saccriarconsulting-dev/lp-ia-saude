package com.axys.redeflexmobile.shared.enums;

/**
 * Created by Rogério Massa on 17/09/2018.
 */

public enum EnumStatusVenda {
    NOVO(0), ANDAMENTO(1), CANCELADO(2), CONCLUIDO(3);

    private final int value;

    EnumStatusVenda(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}