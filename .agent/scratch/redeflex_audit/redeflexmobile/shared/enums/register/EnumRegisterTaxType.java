package com.axys.redeflexmobile.shared.enums.register;

import com.annimon.stream.Stream;

import java.util.List;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.TAX_CREDIT_2X_6X;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.TAX_CREDIT_30;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.TAX_CREDIT_7X_12X;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.TAX_DEBIT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ANTICIPATION_TAX;

/**
 * @author Rogério Massa on 18/02/19.
 */

public enum EnumRegisterTaxType {

    DEBIT(1, "Débito"),
    CREDIT_IN_CASH(2, "Crédito à vista"),
    CREDIT_UNTIL_SIX(3, "Crédito Parcelado (2x até 6x)"),
    CREDIT_BIGGER_SIX(4, "Crédito Parcelado (7x até 12x)"),
    ANTICIPATION(5, "Antecipação");

    private int id;
    private String description;

    EnumRegisterTaxType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static EnumRegisterTaxType getEnumById(int id) {
        return Stream.of(values())
                .filter(value -> value.id == id)
                .findFirst()
                .orElse(null);
    }

    public static EnumRegisterTaxType getEnumByRegisterField(EnumRegisterFields fieldType) {
        if (fieldType == TAX_DEBIT) {
            return DEBIT;
        } else if (fieldType == TAX_CREDIT_30) {
            return CREDIT_IN_CASH;
        } else if (fieldType == TAX_CREDIT_2X_6X) {
            return CREDIT_UNTIL_SIX;
        } else if (fieldType == TAX_CREDIT_7X_12X) {
            return CREDIT_BIGGER_SIX;
        }else if (fieldType == ANTICIPATION_TAX) {
            return ANTICIPATION;
        }
        return null;
    }

    public static List<EnumRegisterTaxType> getEnumList() {
        return Stream.ofNullable(values()).toList();
    }
}
