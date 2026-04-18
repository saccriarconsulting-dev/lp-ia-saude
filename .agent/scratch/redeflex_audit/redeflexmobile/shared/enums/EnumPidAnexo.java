package com.axys.redeflexmobile.shared.enums;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.Arrays;
import java.util.List;

public enum EnumPidAnexo implements ICustomSpinnerDialogModel {
    FACHADA(0, "FotoFachadaPDV", "Fachada"),
    TAXA(1, "Taxa", "Taxas de Concorrência"),
    OUTROS(15, "Outros", "Outros");

    private int id;
    private String tipo;
    private String descricao;
    private String identificacao;

    EnumPidAnexo(int id, String tipo, String descricao) {
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    EnumPidAnexo(int id, String tipo, String descricao, String identificacao) {
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.identificacao = identificacao;
    }

    public static EnumPidAnexo getEnumTipo(String tipo) {
        return Stream.of(values())
                .filter(value1 -> value1.tipo.equals(tipo))
                .findFirst()
                .get();
    }

    public static List<EnumPidAnexo> getPidAnexos() {
        return Arrays.asList(FACHADA, TAXA, OUTROS);
    }

    @Override
    public Integer getIdValue() {
        return id;
    }

    @Override
    public String getDescriptionValue() {
        return descricao;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public String getTipo() {
        return tipo;
    }

}
