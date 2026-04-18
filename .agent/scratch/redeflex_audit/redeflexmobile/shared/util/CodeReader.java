package com.axys.redeflexmobile.shared.util;

import android.app.Activity;
import android.content.Intent;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CodeReader {
    public static void openCodeReader(Activity activity, String... desiredBarcodeFormats){
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(desiredBarcodeFormats);
        integrator.initiateScan();
    }

    public static IntentResult parseActivityResult(int requestCode, int resultCode, Intent intent) {
        return IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
    }
}
