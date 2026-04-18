package com.axys.redeflexmobile.shared.enums;

/**
 * @author Rogério Massa on 2019-08-07.
 */

public enum EnumStatusAuditagem {

    ABERTA("N"), PROCESSANDO("P"), CONCLUIDA("S");

    private final String value;

    EnumStatusAuditagem(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
