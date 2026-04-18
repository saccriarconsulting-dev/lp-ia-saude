package com.axys.redeflexmobile.shared.models.clientinfo;

import com.google.gson.annotations.SerializedName;

/**
 * @author lucasmarciano on 01/07/20
 */
public class LigationTableFlagsHomeBanking {
    private Integer id;
    @SerializedName("idBandeira") private Integer idFlag;
    @SerializedName("idDomicilioBancario") private Integer idHomeBank;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdFlag() {
        return idFlag;
    }

    public void setIdFlag(Integer idFlag) {
        this.idFlag = idFlag;
    }

    public Integer getIdHomeBank() {
        return idHomeBank;
    }

    public void setIdHomeBank(Integer idHomeBank) {
        this.idHomeBank = idHomeBank;
    }
}
