package com.axys.redeflexmobile.shared.models.migracao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Lucas Marciano on 24/03/2020
 */
public class RegisterMigrationSubTax {
    @SerializedName("idTaxa") private int id;
    @Expose(serialize = false) private int idCliente;
    private int idCadastroMigracaoSub;
    @SerializedName("bandeiraId") private int bandeiraTipoId;
    private double taxaDebito;
    private double taxaCredito;
    private double taxaCredito6x;
    private double taxaCredito12x;
    private double antecipacaoAutomatica;
    @Expose(serialize = false) private boolean ativo;

    public RegisterMigrationSubTax() { }

    public RegisterMigrationSubTax(int bandeiraTipoId) {
        this.bandeiraTipoId = bandeiraTipoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdCadastroMigracaoSub() {
        return idCadastroMigracaoSub;
    }

    public void setIdCadastroMigracaoSub(int idCadastroMigracaoSub) {
        this.idCadastroMigracaoSub = idCadastroMigracaoSub;
    }

    public int getBandeiraTipoId() {
        return bandeiraTipoId;
    }

    public void setBandeiraTipoId(int bandeiraTipoId) {
        this.bandeiraTipoId = bandeiraTipoId;
    }

    public double getDebito() {
        return taxaDebito;
    }

    public void setDebito(double taxaDebito) {
        this.taxaDebito = taxaDebito;
    }

    public double getCreditoAVista() {
        return taxaCredito;
    }

    public void setCreditoAVista(double taxaCredito) {
        this.taxaCredito = taxaCredito;
    }

    public double getCreditoAte6() {
        return taxaCredito6x;
    }

    public void setCreditoAte6(double taxaCredito6x) {
        this.taxaCredito6x = taxaCredito6x;
    }

    public double getCreditoMaior6() {
        return taxaCredito12x;
    }

    public void setCreditoMaior6(double taxaCredito12x) {
        this.taxaCredito12x = taxaCredito12x;
    }

    public double getAntecipacaoAutomatica() {
        return antecipacaoAutomatica;
    }

    public void setAntecipacaoAutomatica(double antecipacaoAutomatica) {
        this.antecipacaoAutomatica = antecipacaoAutomatica;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
