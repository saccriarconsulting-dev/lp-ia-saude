package com.axys.redeflexmobile.shared.models.customerregister;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 06/02/19.
 */

public class CustomerRegisterExemption implements ICustomSpinnerDialogModel {

    @SerializedName("id") private Integer id;
    @SerializedName("descricao") private String monthCount;
    @SerializedName("ativo") private boolean activate;

    public CustomerRegisterExemption() {
    }

    public CustomerRegisterExemption(Integer id, String monthCount) {
        this.id = id;
        this.monthCount = monthCount;
    }

    public CustomerRegisterExemption(Integer id, String monthCount, boolean activate) {
        this.id = id;
        this.monthCount = monthCount;
        this.activate = activate;
    }

    @Override
    public Integer getIdValue() {
        return id;
    }

    @Override
    public String getDescriptionValue() {
        return monthCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(String monthCount) {
        this.monthCount = monthCount;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }
}
