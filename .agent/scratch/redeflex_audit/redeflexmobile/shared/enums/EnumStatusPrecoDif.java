package com.axys.redeflexmobile.shared.enums;

import java.util.Arrays;
import java.util.List;

public enum EnumStatusPrecoDif {
    APROVADA_GER(1, "APROVADA - GERENTE"),
    APROVADA_DIR(2, "APROVADA - DIRETOR"),
    RECUSADA_GER(3, "RECUSADA - GERENTE"),
    RECUSADA_DIR(4, "RECUSADA - DIRETOR"),
    INATIVADA(5, "INATIVADA"),
    APROVADA_SUP(6, "APROVADA - SUPERVISOR"),
    RECUSADA_SUP(7, "RECUSADA - SUPERVISOR"),
    APROVADA_ACI(8, "APROVADA - ACIONISTA"),
    RECUSADA_ACI(9, "RECUSADA - ACIONISTA"),
    APROVADA_COR(10, "APROVADA - COORDENADOR"),
    RECUSADA_COR(11, "RECUSADA - COORDENADOR");

    public final int valor;
    public final String descricao;

    EnumStatusPrecoDif(int valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static EnumStatusPrecoDif getPorId(int value) {
        for (EnumStatusPrecoDif tp : EnumStatusPrecoDif.values()) {
            if (tp.valor == value) {
                return tp;
            }
        }
        return null;
    }

    public static EnumStatusPrecoDif getPorDescricao(String displayText) {
        for (EnumStatusPrecoDif tp : EnumStatusPrecoDif.values()) {
            if (tp.descricao == displayText) {
                return tp;
            }
        }
        return null;
    }

    public static List<EnumStatusPrecoDif> getEnumList() {
        return Arrays.asList(APROVADA_GER, APROVADA_DIR, RECUSADA_GER, RECUSADA_DIR, INATIVADA, APROVADA_SUP, RECUSADA_SUP, APROVADA_ACI, RECUSADA_ACI, APROVADA_COR, RECUSADA_COR);
    }

    public static List<String> getEnumDisplayList() {
        return Arrays.asList(APROVADA_GER.descricao, APROVADA_DIR.descricao, RECUSADA_GER.descricao, RECUSADA_DIR.descricao, INATIVADA.descricao, APROVADA_SUP.descricao, RECUSADA_SUP.descricao, APROVADA_ACI.descricao, RECUSADA_ACI.descricao, APROVADA_COR.descricao, RECUSADA_COR.descricao);
    }
}
