package com.axys.redeflexmobile.shared.models;

import java.util.Date;

public class QrCodeGerarResponse {
    private String QrCodeLink;
    private Date ExpiraEm;
    private String TxtId;

    public QrCodeGerarResponse() {
    }

    public String getQrCodeLink() {
        return QrCodeLink;
    }

    public void setQrCodeLink(String qrCodeLink) {
        QrCodeLink = qrCodeLink;
    }

    public Date getExpiraEm() {
        return ExpiraEm;
    }

    public void setExpiraEm(Date expiraEm) {
        ExpiraEm = expiraEm;
    }

    public String getTxtId() {
        return TxtId;
    }

    public void setTxtId(String txtId) {
        TxtId = txtId;
    }
}
