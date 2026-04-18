package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 11/01/2018.
 */

public class InformacoesChip {
    private String ProdutoSAPId;
    private String Descricao;
    private String ICCID;
    private Date DataTransacao;
    private String Colaborador;
    private int CodigoVendedor;

    public String getProdutoSAPId() {
        return this.ProdutoSAPId;
    }

    public void setProdutoSAPId(String ProdutoSAPId) {
        this.ProdutoSAPId = ProdutoSAPId;
    }

    public String getDescricao() {
        return this.Descricao;
    }

    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }

    public String getICCID() {
        return this.ICCID;
    }

    public void setICCID(String ICCID) {
        this.ICCID = ICCID;
    }

    public Date getDataTransacao() {
        return this.DataTransacao;
    }

    public void setDataTransacao(Date DataTransacao) {
        this.DataTransacao = DataTransacao;
    }

    public String getColaborador() {
        return this.Colaborador;
    }

    public void setColaborador(String Colaborador) {
        this.Colaborador = Colaborador;
    }

    public int getCodigoVendedor() {
        return this.CodigoVendedor;
    }

    public void setCodigoVendedor(int CodigoVendedor) {
        this.CodigoVendedor = CodigoVendedor;
    }
}