package com.axys.redeflexmobile.shared.models;

import java.io.Serializable;

public class SolicitacaoPidStatus implements Serializable {
    private int idSolicitacaoPid;
    private String status;

    public SolicitacaoPidStatus() {
    }

    public int getIdSolicitacaoPid() {
        return idSolicitacaoPid;
    }

    public void setIdSolicitacaoPid(int idSolicitacaoPid) {
        this.idSolicitacaoPid = idSolicitacaoPid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
