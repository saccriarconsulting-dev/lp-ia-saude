package com.axys.redeflexmobile.shared.enums.register;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rogério Massa on 16/01/19.
 */

public enum EnumRegisterDebitAutomatic implements ICustomSpinnerDialogModel {

    NO(0, "Não"), YES(1, "Sim");

    private int id;
    private String description;

    EnumRegisterDebitAutomatic(int id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public Integer getIdValue() {
        return id;
    }

    @Override
    public String getDescriptionValue() {
        return description;
    }

    public static EnumRegisterDebitAutomatic getEnumByValue(int value) {
        return values()[value];
    }

    public static List<EnumRegisterDebitAutomatic> getEnumList() {
        return Arrays.asList(YES, NO);
    }
}
