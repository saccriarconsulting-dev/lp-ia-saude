package com.axys.redeflexmobile.shared.enums;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;

/**
 * @author lucasmarciano on 30/06/20
 */
public enum EnumCreditCardFlags {
    MASTERCARD(1, "Mastercard", R.drawable.ic_tax_master_wrapped),
    VISA(2, "Visa", R.drawable.ic_tax_visa_wrapped),
    HIPERCARD(3, "Hipercard", R.drawable.ic_tax_hipercard_wrapped),
    ELO(4, "Elo", R.drawable.ic_tax_elo_wrapped),
    AMEX(5, "Amex", R.drawable.ic_tax_amex_wrapped);

    private final int id;
    private final String name;
    private final int imageResource;

    EnumCreditCardFlags(int id, String name, int imageResource) {
        this.id = id;
        this.name = name;
        this.imageResource = imageResource;
    }

    public static EnumCreditCardFlags getCreditFlagById(int id) {
        return Stream.ofNullable(values())
                .filter(item -> item.id == id)
                .findFirst()
                .orElse(null);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }
}
