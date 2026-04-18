package com.axys.redeflexmobile.shared.enums;

import java.util.Arrays;
import java.util.List;

public enum EnumSituacaoPrecoDif {
    AGUARDA_ANALISE_GER(1, "Aguardando Análise - Gerente"),
    AGUARDA_ANALISE_DIR(2, "Aguardando Análise - Diretor"),
    RECUSADA_GER(3, "Recusada - Gerente"),
    RECUSADA_DIR(4, "Recusada - Diretor"),
    APLICADA(5, "Aplicada"),
    APLICADA_PARCIAL(6, "Aplicada Parcialmente"),
    EXPIRADA(7, "Expirada"),
    INATIVADA(8, "Inativada"),
    AGUARDA_ANALISE_SUP(9, "Aguardando Análise - Supervisor"),
    RECUSADA_SUP(10, "Recusada - Supervisor"),
    AGUARDA_ANALISE_COR(11, "Aguardando Análise - Coordenador"),
    RECUSADA_COR(12, "Recusada - Coordenador"),
    AGUARDA_ANALISE_ACI(13, "Aguardando Análise - Acionista"),
    RECUSADA_ACI(14, "Recusada - Acionista");

    public final int valor;
    public final String descricao;

    EnumSituacaoPrecoDif(int valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static EnumSituacaoPrecoDif getPorId(int value) {
        for (EnumSituacaoPrecoDif tp : EnumSituacaoPrecoDif.values()) {
            if (tp.valor == value) {
                return tp;
            }
        }
        return null;
    }

    public static EnumSituacaoPrecoDif getPorDescricao(String displayText) {
        for (EnumSituacaoPrecoDif tp : EnumSituacaoPrecoDif.values()) {
            if (tp.descricao == displayText) {
                return tp;
            }
        }
        return null;
    }

    public static List<EnumSituacaoPrecoDif> getEnumList() {
        return Arrays.asList(AGUARDA_ANALISE_GER, AGUARDA_ANALISE_DIR, RECUSADA_GER, RECUSADA_DIR, APLICADA, APLICADA_PARCIAL, EXPIRADA, INATIVADA, AGUARDA_ANALISE_SUP, RECUSADA_SUP, AGUARDA_ANALISE_COR, RECUSADA_COR, AGUARDA_ANALISE_ACI, RECUSADA_ACI);
    }

    public static List<String> getEnumDisplayList() {
        return Arrays.asList(AGUARDA_ANALISE_GER.descricao, AGUARDA_ANALISE_DIR.descricao, RECUSADA_GER.descricao, RECUSADA_DIR.descricao, APLICADA.descricao, APLICADA_PARCIAL.descricao, EXPIRADA.descricao, INATIVADA.descricao, AGUARDA_ANALISE_SUP.descricao, RECUSADA_SUP.descricao, AGUARDA_ANALISE_COR.descricao, RECUSADA_COR.descricao, AGUARDA_ANALISE_ACI.descricao, RECUSADA_ACI.descricao);
    }
}
