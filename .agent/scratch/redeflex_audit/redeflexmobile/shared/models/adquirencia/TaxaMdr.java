package com.axys.redeflexmobile.shared.models.adquirencia;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

/**
 * @author Rogério Massa on 25/03/19.
 */

public class TaxaMdr implements ICustomSpinnerDialogModel {

    @SerializedName("id") private Integer id;
    @SerializedName("mcc") private int mcc;
    @SerializedName("descricaoMcc") private String description;
    @SerializedName("idPrazoNegociacao") private Integer negotiationTermId;
    @SerializedName("tipoPessoa") private int personType;
    @SerializedName("tipoBandeira") private Integer flagType;
    @SerializedName("faturamentoInicial") private double billingInitial;
    @SerializedName("faturamentoFinal") private double billingFinal;
    @SerializedName("taxaDebito") private double taxDebit;
    @SerializedName("taxaCredito") private double taxCredit;
    @SerializedName("taxaCredito6x") private double taxUntilSix;
    @SerializedName("taxaCredito12x") private double taxBiggerThanSix;
    @SerializedName("antecipacaoAutomatica") private double anticipation;
    @SerializedName("ativo") private boolean activated;

    @SerializedName("TipoClassificacao") private String TipoClassificacao;
    @SerializedName("TipoNegociacao") private Integer TipoNegociacao; // 1 - Negociação 2 - Renegociacao
    @SerializedName("RAV") private boolean RAV;

    public TaxaMdr() {
    }

    public TaxaMdr(Integer flagType) {
        this.flagType = flagType;
    }

    public TaxaMdr(Integer id,
                   int mcc,
                   String description,
                   Integer negotiationTermId,
                   int personType,
                   Integer flagType,
                   double billingInitial,
                   double billingFinal,
                   double taxDebit,
                   double taxCredit,
                   double taxUntilSix,
                   double taxBiggerThanSix,
                   double anticipation,
                   boolean activated,
                   String TipoClassificacao,
                   Integer TipoNegociacao,
                   boolean RAV) {
        this.id = id;
        this.mcc = mcc;
        this.description = description;
        this.negotiationTermId = negotiationTermId;
        this.personType = personType;
        this.flagType = flagType;
        this.billingInitial = billingInitial;
        this.billingFinal = billingFinal;
        this.taxDebit = taxDebit;
        this.taxCredit = taxCredit;
        this.taxUntilSix = taxUntilSix;
        this.taxBiggerThanSix = taxBiggerThanSix;
        this.anticipation = anticipation;
        this.activated = activated;
        this.TipoClassificacao = TipoClassificacao;
        this.TipoNegociacao = TipoNegociacao;
        this.RAV = RAV;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNegotiationTermId() {
        return negotiationTermId;
    }

    public void setNegotiationTermId(Integer negotiationTermId) {
        this.negotiationTermId = negotiationTermId;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public Integer getFlagType() {
        return flagType;
    }

    public void setFlagType(Integer flagType) {
        this.flagType = flagType;
    }

    public double getBillingInitial() {
        return billingInitial;
    }

    public void setBillingInitial(double billingInitial) {
        this.billingInitial = billingInitial;
    }

    public double getBillingFinal() {
        return billingFinal;
    }

    public void setBillingFinal(double billingFinal) {
        this.billingFinal = billingFinal;
    }

    public double getTaxDebit() {
        return taxDebit;
    }

    public void setTaxDebit(double taxDebit) {
        this.taxDebit = taxDebit;
    }

    public double getTaxCredit() {
        return taxCredit;
    }

    public void setTaxCredit(double taxCredit) {
        this.taxCredit = taxCredit;
    }

    public double getTaxUntilSix() {
        return taxUntilSix;
    }

    public void setTaxUntilSix(double taxUntilSix) {
        this.taxUntilSix = taxUntilSix;
    }

    public double getTaxBiggerSix() {
        return taxBiggerThanSix;
    }

    public void setTaxBiggerThanSix(double taxBiggerThanSix) {
        this.taxBiggerThanSix = taxBiggerThanSix;
    }

    public double getAnticipation() {
        return anticipation;
    }

    public void setAnticipation(double anticipation) {
        this.anticipation = anticipation;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public Integer getIdValue() {
        return mcc;
    }

    @Override
    public String getDescriptionValue() {
        return String.format("%s - %s", mcc, description);
    }

    public String getTipoClassificacao() {
        return TipoClassificacao;
    }

    public void setTipoClassificacao(String tipoClassificacao) {
        TipoClassificacao = tipoClassificacao;
    }

    public Integer getTipoNegociacao() {
        return TipoNegociacao;
    }

    public void setTipoNegociacao(Integer tipoNegociacao) {
        TipoNegociacao = tipoNegociacao;
    }

    public boolean isRAV() {
        return RAV;
    }

    public void setRAV(boolean RAV) {
        this.RAV = RAV;
    }

    public double getTaxBiggerThanSix() {
        return taxBiggerThanSix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaxaMdr taxaMdr = (TaxaMdr) o;

        if (mcc != taxaMdr.mcc) return false;
        if (personType != taxaMdr.personType) return false;
        if (Double.compare(taxaMdr.billingInitial, billingInitial) != 0) return false;
        if (Double.compare(taxaMdr.billingFinal, billingFinal) != 0) return false;
        if (Double.compare(taxaMdr.taxDebit, taxDebit) != 0) return false;
        if (Double.compare(taxaMdr.taxCredit, taxCredit) != 0) return false;
        if (Double.compare(taxaMdr.taxUntilSix, taxUntilSix) != 0) return false;
        if (Double.compare(taxaMdr.taxBiggerThanSix, taxBiggerThanSix) != 0) return false;
        if (Double.compare(taxaMdr.anticipation, anticipation) != 0) return false;
        if (activated != taxaMdr.activated) return false;
        if (id != null ? !id.equals(taxaMdr.id) : taxaMdr.id != null) return false;
        if (description != null ? !description.equals(taxaMdr.description) : taxaMdr.description != null)
            return false;
        if (negotiationTermId != null ? !negotiationTermId.equals(taxaMdr.negotiationTermId) : taxaMdr.negotiationTermId != null)
            return false;
        return flagType != null ? flagType.equals(taxaMdr.flagType) : taxaMdr.flagType == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + mcc;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (negotiationTermId != null ? negotiationTermId.hashCode() : 0);
        result = 31 * result + personType;
        result = 31 * result + (flagType != null ? flagType.hashCode() : 0);
        temp = Double.doubleToLongBits(billingInitial);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(billingFinal);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(taxDebit);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(taxCredit);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(taxUntilSix);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(taxBiggerThanSix);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(anticipation);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (activated ? 1 : 0);
        return result;
    }
}
