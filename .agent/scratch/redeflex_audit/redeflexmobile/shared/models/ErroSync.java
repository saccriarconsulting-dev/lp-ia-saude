package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 17/11/2017.
 */

public class ErroSync {
    private int SyncPendenciasId;
    private int IdColaborador;
    private String Entidade;
    private Date DataHora;
    private String VersaoApp;
    private boolean Ajustado;
    private int IdAppMobile;

    public int getSyncPendenciasId() {
        return SyncPendenciasId;
    }

    public void setSyncPendenciasId(int syncPendenciasId) {
        SyncPendenciasId = syncPendenciasId;
    }

    public int getIdColaborador() {
        return IdColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        IdColaborador = idColaborador;
    }

    public String getEntidade() {
        return Entidade;
    }

    public void setEntidade(String entidade) {
        Entidade = entidade;
    }

    public Date getDataHora() {
        return DataHora;
    }

    public void setDataHora(Date dataHora) {
        DataHora = dataHora;
    }

    public String getVersaoApp() {
        return VersaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        VersaoApp = versaoApp;
    }

    public boolean isAjustado() {
        return Ajustado;
    }

    public void setAjustado(boolean ajustado) {
        Ajustado = ajustado;
    }

    public int getIdAppMobile() {
        return IdAppMobile;
    }

    public void setIdAppMobile(int idAppMobile) {
        IdAppMobile = idAppMobile;
    }
}