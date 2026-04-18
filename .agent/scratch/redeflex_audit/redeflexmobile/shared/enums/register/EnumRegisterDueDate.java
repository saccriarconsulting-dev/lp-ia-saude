package com.axys.redeflexmobile.shared.enums.register;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rogério Massa on 16/01/19.
 */

public enum EnumRegisterDueDate implements ICustomSpinnerDialogModel {

    FIVE(0, "5"), TEN(1, "10");

    private int id;
    private String description;

    EnumRegisterDueDate(int id, String description) {
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

    public static EnumRegisterDueDate getEnumByValue(int value) {
        return Stream.of(values())
                .filter(value1 -> value1.id == value)
                .findFirst()
                .orElse(null);
    }

    public static EnumRegisterDueDate getEnumByDescription(String value) {
        return Stream.of(values())
                .filter(value1 -> value1.description.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static List<EnumRegisterDueDate> getDueDateList() {
        return Arrays.asList(EnumRegisterDueDate.FIVE, EnumRegisterDueDate.TEN);
    }
}
