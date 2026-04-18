package com.axys.redeflexmobile.shared.models.solicitacaotroca;

import com.google.gson.annotations.SerializedName;

public class SolicitacaoTrocaCodBarras {

    @SerializedName(value = "IdServer", alternate = {"SolicitacaoTrocaCodBarrasId"}) private int idServer;
    @SerializedName("IdAppMobile") private int idApp;
    @SerializedName("IccidDe") private String iccidDe;
    @SerializedName("IccidPara") private String iccidPara;
    private String codigoItem;
    private int solicitacaoTrocaId;
    private boolean bipadoAntigo;
    private boolean bipadoNovo;

    public int getIdApp() {
        return idApp;
    }

    public void setIdApp(int idApp) {
        this.idApp = idApp;
    }

    public int getSolicitacaoTrocaId() {
        return solicitacaoTrocaId;
    }

    public void setSolicitacaoTrocaId(int solicitacaoTrocaId) {
        this.solicitacaoTrocaId = solicitacaoTrocaId;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public String getIccidDe() {
        return iccidDe;
    }

    public void setIccidDe(String iccidDe) {
        this.iccidDe = iccidDe;
    }

    public String getIccidPara() {
        return iccidPara;
    }

    public void setIccidPara(String iccidPara) {
        this.iccidPara = iccidPara;
    }

    public boolean isBipadoAntigo() {
        return bipadoAntigo;
    }

    public void setBipadoAntigo(boolean bipadoAntigo) {
        this.bipadoAntigo = bipadoAntigo;
    }

    public boolean isBipadoNovo() {
        return bipadoNovo;
    }

    public void setBipadoNovo(boolean bipadoNovo) {
        this.bipadoNovo = bipadoNovo;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }
}
