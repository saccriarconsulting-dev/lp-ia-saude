package com.axys.redeflexmobile.shared.enums.adquirencia;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;

/**
 * @author Rogério Massa on 30/10/18.
 */

public enum EnumRoutesProspectStatus {

    NEXT_VISIT(1, "PRÓXIMA VISITA", R.drawable.status_label_yellow, null),
    COMPLETED(2, "CONCLUÍDA", R.drawable.status_label_green, R.drawable.ic_check_green_wraped);

    private int value;
    private String title;
    private int background;
    private Integer flag;

    EnumRoutesProspectStatus(int value,
                             String title,
                             int background,
                             Integer flag) {
        this.value = value;
        this.title = title;
        this.background = background;
        this.flag = flag;
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

    public Integer getFlag() {
        return flag;
    }

    public static EnumRoutesProspectStatus getEnumByValue(int value) {
        return Stream.of(values())
                .filter(item -> item.getValue() == value)
                .findFirst()
                .orElse(null);
    }
}
