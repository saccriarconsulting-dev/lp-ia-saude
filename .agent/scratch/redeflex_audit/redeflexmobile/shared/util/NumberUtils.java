package com.axys.redeflexmobile.shared.util;

import timber.log.Timber;

/**
 * @author vitor on 09/10/17.
 */

public class NumberUtils {

    public static final int NEGATIVE_SINGLE_INT = -1;
    public static final int EMPTY_INT = 0;
    public static final int SINGLE_INT = 1;
    public static final int TWO_INT = 2;
    public static final int THREE_INT = 3;
    public static final int FOUR_INT = 4;
    public static final int FIVE_INT = 5;
    public static final int SIX_INT = 6;
    public static final int SEVEN_INT = 7;
    public static final int TEN_INT = 10;
    public static final int FROZEN_DURATION = 1200;
    public static final float EMPTY_FLOAT = 0;
    public static final double EMPTY_DOUBLE = 0d;
    public static final double SINGLE_DOUBLE = 1d;


    private NumberUtils() {
    }

    public static boolean isEmptyDouble(Double value) {
        return value == null || value == EMPTY_DOUBLE;
    }

    public static boolean isNotEmptyDouble(Double value) {
        return !isEmptyDouble(value);
    }

    public static double parseStringDateToDouble(final String stringDate) {
        if (StringUtils.isEmpty(stringDate)) {
            return EMPTY_DOUBLE;
        }

        try {
            String[] hourMinutes = stringDate.split(":");
            int hour = Integer.parseInt(hourMinutes[0]);
            int minutes = (Integer.parseInt(hourMinutes[1]) * 100) / 60;
            return Double.parseDouble(String.format("%s.%s", hour, minutes));
        } catch (RuntimeException e) {
            Timber.e(e);
            return EMPTY_DOUBLE;
        }
    }
}
