package com.axys.redeflexmobile.shared.models;

/**
 * @author Lucas Marciano on 02/03/2020
 */
public class PendenciaResposta {
    private Pendencia pendencia;
    private PendenciaCliente clientePendencia;
    private PendenciaMotivo respostaPendencia;

    public Pendencia getPendencia() {
        return pendencia;
    }

    public void setPendencia(Pendencia pendencia) {
        this.pendencia = pendencia;
    }

    public PendenciaCliente getPendenciaCliente() {
        return clientePendencia;
    }

    public void setPendenciaCliente(PendenciaCliente clientePendencia) {
        this.clientePendencia = clientePendencia;
    }

    public PendenciaMotivo getPendenciaMotivo() {
        return respostaPendencia;
    }

    public void setPendenciaMotivo(PendenciaMotivo respostaPendencia) {
        this.respostaPendencia = respostaPendencia;
    }
}
