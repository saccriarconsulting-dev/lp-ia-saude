package com.axys.redeflexmobile.shared.util;

import java.util.Date;

import javax.annotation.Nullable;

import io.reactivex.annotations.NonNull;
import timber.log.Timber;

public final class DataSyncValidator {

    private static final String TAG = "DataSyncValidator";

    private DataSyncValidator() {
        // no-op
    }

    public static boolean isValid(@Nullable String value) {
        if (value == null) return false;
        final String v = value.trim();
        if (v.isEmpty()) return false;
        return DataSyncManager.parse(v) != null;
    }

    @Nullable
    public static String toDbIfValid(@Nullable Date date) {
        if (date == null) return null;
        final String ds = DataSyncManager.toDbString(date);
        return isValid(ds) ? ds : null;
    }

    @Nullable
    public static String normalizeOrNull(@Nullable String raw) {
        final Date parsed = DataSyncManager.parse(raw);
        return (parsed != null) ? DataSyncManager.toDbString(parsed) : null;
    }

    public static void warnInvalid(@Nullable String value,
                                   @NonNull String table,
                                   @NonNull String keyField,
                                   @NonNull String keyValue) {
        if (!isValid(value)) {
            Timber.w("[%s] Invalid DataSync detected: table=%s key=%s:%s raw='%s'",
                    TAG, table, keyField, keyValue, value);
        }
    }
}