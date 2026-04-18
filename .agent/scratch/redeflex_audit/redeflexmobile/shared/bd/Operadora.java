package com.axys.redeflexmobile.shared.bd;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

/**
 * @author Bruno Pimentel on 10/09/2019
 */
public class Operadora implements ICustomSpinnerDialogModel {

    private int id;
    private int idOperadora;
    private String descricao;
    private String imagem;

    public Operadora(final int id,
                     final int idOperadora,
                     final String descricao,
                     final String imagem) {
        this.id = id;
        this.idOperadora = idOperadora;
        this.descricao = descricao;
        this.imagem = imagem;
    }

    public int getIdOperadora() {
        return idOperadora;
    }

    public void setIdOperadora(int idOperadora) {
        this.idOperadora = idOperadora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Integer getIdValue() {
        return getIdOperadora();
    }

    @Override
    public String getDescriptionValue() {
        return getDescricao();
    }
}
