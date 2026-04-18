package com.axys.redeflexmobile.shared.enums;

public enum EnumSituacaoCliente {
    Ruptura("Ruptura"), PreRuptura("Pré-Ruptura"), Abastecimento("Abastecimento");

    private final String value;

    EnumSituacaoCliente(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
