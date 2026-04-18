package com.axys.redeflexmobile.shared.enums;

/**
 * Created by Desenvolvimento on 06/04/2016.
 */
public enum EnumStatusSolicitacao {
    Pendente(0),
    Enviada(1),
    Aprovada(2),
    Regeitada(3),
    RemessaGerada(4),
    RemessaAceita(5);

    private final int value;

    EnumStatusSolicitacao(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}