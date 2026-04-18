package com.axys.redeflexmobile.shared.models.adquirencia;

/**
 * @author Rogério Massa on 04/12/18.
 */

public class Release {

    private int id;
    private String machine;
    private String masterCode;
    private String muxxCode;
    private String idCliente;

    public Release() {
    }

    public Release(int id, String machine, String masterCode, String idCliente) {
        this.id = id;
        this.machine = machine;
        this.masterCode = masterCode;
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getMasterCode() {
        return masterCode;
    }

    public void setMasterCode(String masterCode) {
        this.masterCode = masterCode;
    }

    public String getMuxxCode() {
        return muxxCode;
    }

    public void setMuxxCode(String muxxCode) {
        this.muxxCode = muxxCode;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
}
