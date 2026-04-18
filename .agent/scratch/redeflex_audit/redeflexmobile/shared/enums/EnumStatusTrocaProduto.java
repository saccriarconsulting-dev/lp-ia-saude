package com.axys.redeflexmobile.shared.enums;

public enum EnumStatusTrocaProduto {

    ANALISE(1),
    REPROVADO(2),
    APROVADO(3),
    CANCELADO(4),
    CONCLUIDO(5);

    public final int valor;

    EnumStatusTrocaProduto(int valor) {
        this.valor = valor;
    }

    public static EnumStatusTrocaProduto obterEnum(int valor) {
        for (EnumStatusTrocaProduto trainingStatusEnum : EnumStatusTrocaProduto.values()) {
            if (trainingStatusEnum.valor == valor) {
                return trainingStatusEnum;
            }
        }
        return null;
    }
}