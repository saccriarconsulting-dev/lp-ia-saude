package com.axys.redeflexmobile.shared.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.util.UUID;

public class DeviceUniqueProviderImpl implements DeviceUniqueProvider {

    private final Context context;

    public DeviceUniqueProviderImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getUniqueId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return getImei();
        return generateUuid();
    }

    private String getImei() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            return tm.getDeviceId();
        }

        return generateUuid();
    }

    private String generateUuid() {
        UUID temp = UUID.randomUUID();

        return Long.toHexString(temp.getMostSignificantBits())
                + Long.toHexString(temp.getLeastSignificantBits());
    }
}
