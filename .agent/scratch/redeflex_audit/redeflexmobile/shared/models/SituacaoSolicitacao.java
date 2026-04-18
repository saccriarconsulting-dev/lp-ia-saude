package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by Desenvolvimento on 24/04/2016.
 */
public class SituacaoSolicitacao {
    private int id;
    private int idAppMobile;
    private int idStatus;
    private Date data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAppMobile() {
        return idAppMobile;
    }

    public void setIdAppMobile(int idAppMobile) {
        this.idAppMobile = idAppMobile;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}