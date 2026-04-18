package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloPOS implements ICustomSpinnerDialogModel {

    @SerializedName(value = "Id", alternate = {"id"}) private Integer idAppMobile;
    @SerializedName(value = "IdTipoMaquina", alternate = {"idTipoMaquina"}) private int idTipoMaquina;
    @SerializedName("Descricao") private String descricao;
    @SerializedName("Modelo") private String modelo;
    @SerializedName("ValorAluguelPadrao") private double valorAluguelPadrao;
    @SerializedName("TipoConexao") private List<ModeloPOSConexao> connections;

    @Override
    public Integer getIdValue() {
        return getIdAppMobile();
    }

    @Override
    public String getDescriptionValue() {
        return getDescricao();
    }

    public Integer getIdAppMobile() {
        return idAppMobile;
    }

    public void setIdAppMobile(Integer idAppMobile) {
        this.idAppMobile = idAppMobile;
    }

    public int getIdTipoMaquina() {
        return idTipoMaquina;
    }

    public void setIdTipoMaquina(int idTipoMaquina) {
        this.idTipoMaquina = idTipoMaquina;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getValorAluguelPadrao() {
        return valorAluguelPadrao;
    }

    public void setValorAluguelPadrao(double valorAluguelPadrao) {
        this.valorAluguelPadrao = valorAluguelPadrao;
    }

    public List<ModeloPOSConexao> getConnections() {
        return connections;
    }

    public void setConnections(List<ModeloPOSConexao> connections) {
        this.connections = connections;
    }
}
