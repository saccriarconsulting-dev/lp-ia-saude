package com.axys.redeflexmobile.shared.models.responses;

import com.axys.redeflexmobile.shared.models.ConsignadoLimiteProduto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConsignadoLimiteCliente {
    private int Id;
    private int IdCliente;
    private String CodigoClienteSGV;
    private String Ativo;
    @SerializedName("ConsignadoLimiteProduto") private List<ConsignadoLimiteProduto> consignadoLimiteProdutoList;

    public ConsignadoLimiteCliente() {
    }

    public ConsignadoLimiteCliente(int id, int idCLiente, String codigoClienteSGV, String ativo, List<ConsignadoLimiteProduto> consignadoLimiteProdutoList) {
        this.Id = id;
        this.IdCliente = idCLiente;
        this.CodigoClienteSGV = codigoClienteSGV;
        this.Ativo = ativo;
        this.consignadoLimiteProdutoList = consignadoLimiteProdutoList;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getIdCLiente() {
        return IdCliente;
    }

    public void setIdCLiente(int idCLiente) {
        this.IdCliente = idCLiente;
    }

    public String getCodigoClienteSGV() {
        return CodigoClienteSGV;
    }

    public void setCodigoClienteSGV(String codigoClienteSGV) {
        this.CodigoClienteSGV = codigoClienteSGV;
    }

    public String getAtivo() {
        return Ativo;
    }

    public void setAtivo(String ativo) {
        this.Ativo = ativo;
    }

    public List<ConsignadoLimiteProduto> getConsignadoLimiteProdutoList() {
        return consignadoLimiteProdutoList;
    }

    public void setConsignadoLimiteProdutoList(List<ConsignadoLimiteProduto> consignadoLimiteProdutoList) {
        this.consignadoLimiteProdutoList = consignadoLimiteProdutoList;
    }
}
