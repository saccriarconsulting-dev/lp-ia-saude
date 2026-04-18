package com.axys.redeflexmobile.shared.models.registerrate;

import com.axys.redeflexmobile.shared.services.network.util.JsonExclude;
import com.google.gson.annotations.SerializedName;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public class RegistrationSimulationFee {
    @SerializedName("idAppMobile") private Integer id;
    @JsonExclude private Integer idProspecting;
    @SerializedName("bandeiraTipoId") private Integer idFlag;
    @SerializedName("debito") private double debitValue;
    @SerializedName("creditoAVista") private double creditValue;
    @SerializedName("creditoAte6") private double creditSixValue;
    @SerializedName("creditoMaior6") private double creditMoreThanSixValue;
    @SerializedName("antecipacaoAutomatica") private double automaticAnticipation;

    public RegistrationSimulationFee() {

    }

    public RegistrationSimulationFee(int idFlag) {
        this.idFlag = idFlag;
        this.debitValue = 0;
        this.creditValue = 0;
        this.creditSixValue = 0;
        this.creditMoreThanSixValue = 0;
        this.automaticAnticipation = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProspecting() {
        return idProspecting;
    }

    public void setIdProspecting(Integer idProspecting) {
        this.idProspecting = idProspecting;
    }

    public Integer getIdFlag() {
        return idFlag;
    }

    public void setIdFlag(Integer idFlag) {
        this.idFlag = idFlag;
    }

    public double getDebitValue() {
        return debitValue;
    }

    public void setDebitValue(double debitValue) {
        this.debitValue = debitValue;
    }

    public double getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(double creditValue) {
        this.creditValue = creditValue;
    }

    public double getCreditSixValue() {
        return creditSixValue;
    }

    public void setCreditSixValue(double creditSixValue) {
        this.creditSixValue = creditSixValue;
    }

    public double getCreditMoreThanSixValue() {
        return creditMoreThanSixValue;
    }

    public void setCreditMoreThanSixValue(double creditMoreThanSixValue) {
        this.creditMoreThanSixValue = creditMoreThanSixValue;
    }

    public double getAutomaticAnticipation() {
        return automaticAnticipation;
    }

    public void setAutomaticAnticipation(double automaticAnticipation) {
        this.automaticAnticipation = automaticAnticipation;
    }
}
