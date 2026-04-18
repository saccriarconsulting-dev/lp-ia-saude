package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * @author Vitor Otero on 04/05/18.
 */

public class DataHoraServidor {

    private Date date;

    public DataHoraServidor() {
    }

    public DataHoraServidor(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
