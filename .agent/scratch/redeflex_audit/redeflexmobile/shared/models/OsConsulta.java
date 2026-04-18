package com.axys.redeflexmobile.shared.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by joao.viana on 21/09/2016.
 */

public class OsConsulta {
    private Date dataOs;
    private ArrayList<OS> list;

    public ArrayList<OS> getList() {
        return list;
    }

    public void setList(ArrayList<OS> list) {
        this.list = list;
    }

    public Date getDataOs() {
        return dataOs;
    }

    public void setDataOs(Date dataOs) {
        this.dataOs = dataOs;
    }
}