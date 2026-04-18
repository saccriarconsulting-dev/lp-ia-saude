package com.axys.redeflexmobile.shared.enums;

public enum EnumSituacaoPrecoDiferenciado {

    ANALISE_GERENTE(1, "Aguardando Análise - Gerente"),
    ANALISE_DIRETOR(2, "Aguardando Análise - Diretor"),
    RECUSADA_GERENTE(3, "Recusada - Gerente"),
    RECUSADA_DIRETOR(4, "Recusada - Diretor"),
    APLICADA(5, "Aplicada"),
    APLICADA_PARCIALMENTE(6, "Aplicada Parcialmente"),
    EXPIRADA(7, "Expirada"),
    INATIVA(8, "Inativa");


    private final int id;
    private final String situacao;

    EnumSituacaoPrecoDiferenciado(int id, String situacao) {
        this.id = id;
        this.situacao = situacao;
    }

    public int getId() {
        return id;
    }

    public String getSituacao() {
        return situacao;
    }
}

