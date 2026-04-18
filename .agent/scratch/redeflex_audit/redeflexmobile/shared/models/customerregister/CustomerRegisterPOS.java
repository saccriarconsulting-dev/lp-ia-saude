package com.axys.redeflexmobile.shared.models.customerregister;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 16/11/18.
 */

public class CustomerRegisterPOS {

    @SerializedName("id") private int id;
    @SerializedName("IdCadastroCliente") private Integer idRegister;
    @SerializedName("IdTipoMaquina") private Integer idMachineType;
    @SerializedName("DataSync") private String dateSync;
    @SerializedName("ValorAluguel") private Double rentValue;
    @SerializedName("IdTerminal") private String idTerminal;
    @SerializedName("Situacao") private boolean situation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdRegister() {
        return idRegister;
    }

    public void setIdRegister(Integer idRegister) {
        this.idRegister = idRegister;
    }

    public Integer getIdMachineType() {
        return idMachineType;
    }

    public void setIdMachineType(Integer idMachineType) {
        this.idMachineType = idMachineType;
    }

    public String getDateSync() {
        return dateSync;
    }

    public void setDateSync(String dateSync) {
        this.dateSync = dateSync;
    }

    public Double getRentValue() {
        return rentValue;
    }

    public void setRentValue(Double rentValue) {
        this.rentValue = rentValue;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(String idTerminal) {
        this.idTerminal = idTerminal;
    }

    public boolean isSituation() {
        return situation;
    }

    public void setSituation(boolean situation) {
        this.situation = situation;
    }
}
