package com.axys.redeflexmobile.shared.enums.adquirencia;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;

/**
 * @author Rogério Massa on 22/11/18.
 */

public enum EnumRankingStatus {

    UNDER_THAN_50(0, 49, R.drawable.status_label_black, R.drawable.ic_map_marker_black_wraped),
    MORE_THAN_50(50, 89, R.drawable.status_label_red, R.drawable.ic_map_marker_red_wraped),
    MORE_THAN_90(90, 99, R.drawable.status_label_yellow, R.drawable.ic_map_marker_yellow_wraped),
    MORE_THAN_100(100, R.drawable.status_label_green, R.drawable.ic_map_marker_green_wraped);

    private int startValue;
    private int endValue;
    private int background;
    private int mapMarker;

    EnumRankingStatus(int startValue, int endValue, int background, int mapMarker) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.background = background;
        this.mapMarker = mapMarker;
    }

    EnumRankingStatus(int startValue, int background, int mapMarker) {
        this.startValue = startValue;
        this.background = background;
        this.mapMarker = mapMarker;
    }

    public static EnumRankingStatus getEnumByValue(int value) {
        return Stream.of(values())
                .sortBy(enumRankingStatus -> -enumRankingStatus.startValue)
                .filter(item -> value >= MORE_THAN_100.startValue
                        || item.getStartValue() <= value && item.getEndValue() >= value)
                .findFirst()
                .orElse(null);
    }

    public int getStartValue() {
        return startValue;
    }

    public int getEndValue() {
        return endValue;
    }

    public int getBackground() {
        return background;
    }

    public int getMapMarker() {
        return mapMarker;
    }
}
