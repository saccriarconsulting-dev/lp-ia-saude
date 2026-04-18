package com.axys.redeflexmobile.shared.enums.register;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.Arrays;
import java.util.List;

public enum EnumRegisterHorarioFunc implements ICustomSpinnerDialogModel {

    SEMPREABERTA(1, "Sempre Aberta"),
    HORARIOSELECIONADO(2, "Aberto no horário Selecionado");

    private int id;
    private Character value;
    private String description;

    EnumRegisterHorarioFunc(int id, String description) {
        this.id = id;
        this.value = value;
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

    public static EnumRegisterHorarioFunc getEnumByValue(int value) {
        return values()[value];
    }

    public static List<EnumRegisterHorarioFunc> getEnumList() {
        return Arrays.asList(SEMPREABERTA, HORARIOSELECIONADO);
    }
}

