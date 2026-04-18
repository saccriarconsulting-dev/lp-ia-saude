package com.axys.redeflexmobile.shared.models.clientinfo;

import com.axys.redeflexmobile.shared.services.network.util.JsonExclude;
import com.google.gson.annotations.SerializedName;

/**
 * @author lucasmarciano on 30/06/20
 */
public class ClientTaxMdr {
    @SerializedName("idTaxa") private Integer id;
    @SerializedName("idCliente") private Integer idClient;
    @SerializedName("bandeiraId") private Integer flagId;
    @SerializedName("taxaDebito") private Double debitTax;
    @SerializedName("taxaCredito") private Double creditTax;
    @SerializedName("taxaCredito6x") private Double creditTaxSixTimes;
    @SerializedName("taxaCredito12x") private Double creditTaxTwelveTimes;
    @SerializedName("taxaAntecipacao") private Double taxAnticipation;
    @SerializedName("ativo") private boolean active;
    @JsonExclude private FlagsBank flag;

    public ClientTaxMdr() {
    }

    public ClientTaxMdr(Integer id, Integer idClient, Integer flagId, Double debitTax, Double creditTax, Double creditTaxSixTimes, Double creditTaxTwelveTimes, Double taxAnticipation, boolean active) {
        this.id = id;
        this.idClient = idClient;
        this.flagId = flagId;
        this.debitTax = debitTax;
        this.creditTax = creditTax;
        this.creditTaxSixTimes = creditTaxSixTimes;
        this.creditTaxTwelveTimes = creditTaxTwelveTimes;
        this.taxAnticipation = taxAnticipation;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public Integer getFlagId() {
        return flagId;
    }

    public void setFlagId(Integer flagId) {
        this.flagId = flagId;
    }

    public Double getDebitTax() {
        return debitTax;
    }

    public void setDebitTax(Double debitTax) {
        this.debitTax = debitTax;
    }

    public Double getCreditTax() {
        return creditTax;
    }

    public void setCreditTax(Double creditTax) {
        this.creditTax = creditTax;
    }

    public Double getCreditTaxSixTimes() {
        return creditTaxSixTimes;
    }

    public void setCreditTaxSixTimes(Double creditTaxSixTimes) {
        this.creditTaxSixTimes = creditTaxSixTimes;
    }

    public Double getCreditTaxTwelveTimes() {
        return creditTaxTwelveTimes;
    }

    public void setCreditTaxTwelveTimes(Double creditTaxTwelveTimes) {
        this.creditTaxTwelveTimes = creditTaxTwelveTimes;
    }

    public Double getTaxAnticipation() {
        return taxAnticipation;
    }

    public void setTaxAnticipation(Double taxAnticipation) {
        this.taxAnticipation = taxAnticipation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public FlagsBank getFlag() {
        return flag;
    }

    public void setFlag(FlagsBank flag) {
        this.flag = flag;
    }
}
