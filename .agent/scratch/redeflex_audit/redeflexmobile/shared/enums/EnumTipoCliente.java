package com.axys.redeflexmobile.shared.enums;

import static com.axys.redeflexmobile.shared.util.Util_IO.trataString;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.Arrays;
import java.util.List;

public enum EnumTipoCliente implements ICustomSpinnerDialogModel {
    ISO(0, "ISO"),
    SUB(1, "SUB"),
    ADQ(2, "ADQ");

    private int id;
    private String description;

    EnumTipoCliente(int id, String description) {
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

    public static EnumTipoCliente getEnumByValue(int value) {
        return Stream.of(values())
                .filter(value1 -> value1.getIdValue() == value)
                .findFirst()
                .orElse(null);
    }

    public static EnumTipoCliente getEnumByDescription(String value) {
        return Stream.of(values())
                .filter(value1 -> trataString(value1.description).equals(trataString(value)))
                .findFirst()
                .orElse(null);
    }

    public static List<EnumTipoCliente> getList() {
        return Arrays.asList(ISO, SUB, ADQ);
    }
}
