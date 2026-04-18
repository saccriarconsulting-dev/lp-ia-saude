package com.axys.redeflexmobile.shared.models.adquirencia;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

/**
 * @author Bruno Pimentel on 15/01/19.
 */
public class City implements ICustomSpinnerDialogModel {

    private int id;
    private String name;

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getIdValue() {
        return getId();
    }

    @Override
    public String getDescriptionValue() {
        return getName();
    }
}
