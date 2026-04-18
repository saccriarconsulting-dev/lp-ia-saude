package com.axys.redeflexmobile.shared.models;

import androidx.annotation.NonNull;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.dialog.GenericaDialog;

/**
 * Created by joao.viana on 31/08/2016.
 */
public class Estado implements ICustomSpinnerDialogModel, GenericaDialog.GenericaItem {
    private String id;
    private String sigla;
    private String descricao;
    private String situacao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Override
    public Integer getIdValue() {
        return Integer.parseInt(getId());
    }

    @Override
    public String getDescriptionValue() {
        return getSigla();
    }

    @NonNull
    @Override
    public String toString() {
        return getDescricao();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Estado estado = (Estado) o;

        if (id != null ? !id.equals(estado.id) : estado.id != null) return false;
        if (sigla != null ? !sigla.equals(estado.sigla) : estado.sigla != null) return false;
        if (descricao != null ? !descricao.equals(estado.descricao) : estado.descricao != null)
            return false;
        return situacao != null ? situacao.equals(estado.situacao) : estado.situacao == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sigla != null ? sigla.hashCode() : 0);
        result = 31 * result + (descricao != null ? descricao.hashCode() : 0);
        result = 31 * result + (situacao != null ? situacao.hashCode() : 0);
        return result;
    }
}