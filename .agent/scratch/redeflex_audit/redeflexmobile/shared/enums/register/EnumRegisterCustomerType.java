package com.axys.redeflexmobile.shared.enums.register;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

import static com.axys.redeflexmobile.shared.util.Util_IO.trataString;

/**
 * @author Rogério Massa on 11/01/19.
 */
public enum EnumRegisterCustomerType implements ICustomSpinnerDialogModel {

    @SerializedName("Fisico") PHYSICAL(0, "Físico"),
    @SerializedName("Eletronico") ELECTRONIC(1, "Eletrônico"),
    @SerializedName("Solver") ACQUISITION(2, "Adquirência"),
    @SerializedName("Adquirencia") SUBADQUIRENCIA(4, "Adquirência Sub"),
    @SerializedName("Migração") MIGRATION(3, "Migração");

    private int id;
    private String description;

    EnumRegisterCustomerType(int id, String description) {
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

    public String getDescriptionWhitOutAccents() {
        return Util_IO.trataString(description);
    }

    public static EnumRegisterCustomerType getEnumByValue(int value) {
        return Stream.of(values())
                .filter(value1 -> value1.getIdValue() == value)
                .findFirst()
                .orElse(null);
    }

    public static EnumRegisterCustomerType getEnumByDescription(String value) {
        return Stream.of(values())
                .filter(value1 -> trataString(value1.description).equals(trataString(value)))
                .findFirst()
                .orElse(null);
    }

    public static List<EnumRegisterCustomerType> getList() {
        return Arrays.asList(PHYSICAL, ELECTRONIC, SUBADQUIRENCIA, ACQUISITION);
    }
}
