package com.axys.redeflexmobile.shared.models.customerregister;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 2019-09-12.
 */

public class PrazoNegociacao implements ICustomSpinnerDialogModel {

    @SerializedName(value = "Id", alternate = {"id"}) private int id;
    @SerializedName(value = "Descricao", alternate = {"descricao"}) private String descricao;
    @SerializedName(value = "Ativo", alternate = {"ativo"}) private boolean ativo;
    @SerializedName(value = "ConfiguraAntecipacao", alternate = {"configuraAntecipacao"}) private boolean configuraAntecipacao;

    public PrazoNegociacao() {
    }

    public PrazoNegociacao(int id, String descricao, boolean ativo, boolean configuraAntecipacao) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo;
        this.configuraAntecipacao = configuraAntecipacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isConfiguraAntecipacao() {
        return configuraAntecipacao;
    }

    public void setConfiguraAntecipacao(boolean configuraAntecipacao) {
        this.configuraAntecipacao = configuraAntecipacao;
    }

    @Override
    public Integer getIdValue() {
        return id;
    }

    @Override
    public String getDescriptionValue() {
        return descricao;
    }
}
