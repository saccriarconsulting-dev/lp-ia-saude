package com.axys.redeflexmobile.shared.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Desenvolvimento on 06/04/2016.
 */
public class SolicitacaoMercadoria {
    private int id;
    private int idServer;
    private int idVendedor;
    private Date dataCriacao;
    private int status;
    private Date dataUltimaAtualizacao;
    private ArrayList<Produto> Itens;

    public SolicitacaoMercadoria() {
        this.Itens = new ArrayList<>();
    }

    public static String getDescricaoStatus(int status) {
        switch (status) {
            case 0:
                return "Pendente";
            case 1:
                return "Enviada";
            case 2:
                return "Aprovada";
            case 3:
                return "Rejeitada";
            case 4:
                return "Remessa Gerada";
            default:
                return "Não identificado";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Produto> getItens() {
        return Itens;
    }

    public void setItens(ArrayList<Produto> itens) {
        this.Itens = itens;
    }

    public Date getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }
}