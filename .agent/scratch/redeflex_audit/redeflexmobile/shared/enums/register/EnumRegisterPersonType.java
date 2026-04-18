package com.axys.redeflexmobile.shared.enums.register;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

import static com.axys.redeflexmobile.shared.util.Util_IO.trataString;

/**
 * @author Rogério Massa on 11/01/19.
 */

public enum EnumRegisterPersonType implements ICustomSpinnerDialogModel {

    @SerializedName("F") PHYSICAL(1, 'F', "Pessoa Física"),
    @SerializedName("J") JURIDICAL(2, 'J', "Pessoa Jurídica");

    private int id;
    private Character value;
    private String description;

    EnumRegisterPersonType(int id, Character value, String description) {
        this.id = id;
        this.value = value;
        this.description = description;
    }

    public static EnumRegisterPersonType getEnumByIdValue(int idValue) {
        return Stream.of(values())
                .filter(value1 -> value1.id == idValue)
                .findFirst()
                .orElse(null);
    }

    public static EnumRegisterPersonType getEnumByCharValue(String value) {
        return getEnumByCharValue(value.charAt(0));
    }

    public static EnumRegisterPersonType getEnumByCharValue(Character value) {
        return Stream.of(values())
                .filter(value1 -> value1.value.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static EnumRegisterPersonType getEnumByDescription(String value) {
        return Stream.of(values())
                .filter(value1 -> trataString(value1.description).equals(trataString(value)))
                .findFirst()
                .orElse(null);
    }

    public static List<EnumRegisterPersonType> getList() {
        return Arrays.asList(PHYSICAL, JURIDICAL);
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
