package com.axys.redeflexmobile.shared.models;

import android.content.Intent;
import android.os.Handler;

import com.axys.redeflexmobile.shared.enums.EnumStatusAuditagem;

/**
 * @author Rogério Massa on 2019-08-08.
 */

public class AuditagemEstoqueResponse {

    private EnumStatusAuditagem status;
    private Intent serviceIntent;
    private Handler handler;
    private Runnable runnable;

    public AuditagemEstoqueResponse(EnumStatusAuditagem status,
                                    Intent serviceIntent,
                                    Handler handler,
                                    Runnable runnable) {
        this.status = status;
        this.serviceIntent = serviceIntent;
        this.handler = handler;
        this.runnable = runnable;
    }

    public EnumStatusAuditagem getStatus() {
        return status;
    }

    public Intent getServiceIntent() {
        return serviceIntent;
    }

    public Handler getHandler() {
        return handler;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
