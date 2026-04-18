package com.axys.redeflexmobile.shared.enums.register;

import static com.axys.redeflexmobile.shared.util.Util_IO.trataString;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public enum EnumRegisterSexo implements ICustomSpinnerDialogModel {
    @SerializedName("M") MASCULINO(1, 'M', "Masculino"),
    @SerializedName("F") FEMININO(2, 'F', "Feminino");

    private int id;
    private Character value;
    private String description;

    EnumRegisterSexo(int id, Character value, String description) {
        this.id = id;
        this.value = value;
        this.description = description;
    }

    public static EnumRegisterSexo getEnumByIdValue(int idValue) {
        return Stream.of(values())
                .filter(value1 -> value1.id == idValue)
                .findFirst()
                .orElse(null);
    }

    public static EnumRegisterSexo getEnumByCharValue(String value) {
        return getEnumByCharValue(value.charAt(0));
    }

    public static EnumRegisterSexo getEnumByCharValue(Character value) {
        return Stream.of(values())
                .filter(value1 -> value1.value.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static EnumRegisterSexo getEnumByDescription(String value) {
        return Stream.of(values())
                .filter(value1 -> trataString(value1.description).equals(trataString(value)))
                .findFirst()
                .orElse(null);
    }

    public static List<EnumRegisterSexo> getList() {
        return Arrays.asList(MASCULINO, FEMININO);
    }

    @Override
    public Integer getIdValue() {
        return id;
    }

    @Override
    public String getDescriptionValue() {
        return description;
    }

    public String getCharValue() {
        return value.toString();
    }
}
