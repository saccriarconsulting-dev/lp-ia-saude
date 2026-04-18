package com.axys.redeflexmobile.shared.util;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.annotations.NonNull;

public final class DataSyncManager {
    public static final String ISO_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DataSyncManager() { /* no-op */ }

    @NonNull
    public static String toDbString(@NonNull Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_PATTERN, Locale.US);
        sdf.setLenient(false);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    @Nullable
    public static Date parse(@Nullable String value) {
        if (value == null) return null;
        String v = value.trim();
        if (v.isEmpty()) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(ISO_PATTERN, Locale.US);
            sdf.setLenient(false);
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.parse(v);
        } catch (ParseException e) {
            return null;
        }
    }
}
