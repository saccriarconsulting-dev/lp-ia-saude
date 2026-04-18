package com.axys.redeflexmobile.shared.models;

public class QrCodeConsultaRequest {
    private int ColaboradorId;
    private String TxtId;

    public QrCodeConsultaRequest() {
    }

    public int getColaboradorId() {
        return ColaboradorId;
    }

    public void setColaboradorId(int colaboradorId) {
        ColaboradorId = colaboradorId;
    }

    public String getTxtId() {
        return TxtId;
    }

    public void setTxtId(String txtId) {
        TxtId = txtId;
    }
}
