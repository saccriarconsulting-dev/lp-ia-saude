package com.axys.redeflexmobile.shared.enums.adquirencia;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;

/**
 * @author Rogério Massa on 30/10/18.
 */

public enum EnumRoutesProspectType {

    ROUTE(0, "ROTA", R.drawable.status_label_red),
    SCHEDULED(1, "AGENDADA", R.drawable.status_label_purple);

    private int value;
    private String title;
    private int background;

    EnumRoutesProspectType(int value,
                           String title,
                           int background) {
        this.value = value;
        this.title = title;
        this.background = background;
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public int getBackground() {
        return background;
    }

    public static EnumRoutesProspectType getEnumByValue(int value) {
        return Stream.of(values())
                .filter(item -> item.getValue() == value)
                .findFirst()
                .orElse(ROUTE);
    }
}
