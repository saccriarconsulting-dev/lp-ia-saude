package com.axys.redeflexmobile.shared.models.customerregister;

import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 16/11/18.
 */

public class CustomerRegisterTax {

    @SerializedName("Id") private Integer id;
    @SerializedName("IdCadastroCliente") private Integer idCustomerRegister;
    @SerializedName("BandeiraTipoId") private Integer flag;
    @SerializedName("Debito") private Double debit;
    @SerializedName("CreditoAVista") private Double credit;
    @SerializedName("CreditoAte6") private Double untilSix;
    @SerializedName("CreditoMaior6") private Double biggerSix;
    @SerializedName("AntecipacaoAutomatica") private Double anticipation;

    public CustomerRegisterTax() {

    }

    public CustomerRegisterTax(Integer id,
                               Integer idCustomerRegister,
                               Integer flag,
                               Double debit,
                               Double credit,
                               Double untilSix,
                               Double biggerSix,
                               Double anticipation) {
        this.id = id;
        this.idCustomerRegister = idCustomerRegister;
        this.flag = flag;
        this.debit = debit;
        this.credit = credit;
        this.untilSix = untilSix;
        this.biggerSix = biggerSix;
        this.anticipation = anticipation;
    }

    public CustomerRegisterTax(CustomerRegisterTax tax) {
        this.id = tax.getId();
        this.idCustomerRegister = tax.getIdCustomerRegister();
        this.flag = tax.getFlag();
        this.debit = tax.getDebit();
        this.credit = tax.getCredit();
        this.untilSix = tax.getUntilSix();
        this.biggerSix = tax.getBiggerSix();
        this.anticipation = tax.getAnticipation();
    }

    public CustomerRegisterTax(TaxaMdr taxaMdr) {
        this.flag = taxaMdr.getFlagType();
        this.debit = taxaMdr.getTaxDebit();
        this.credit = taxaMdr.getTaxCredit();
        this.untilSix = taxaMdr.getTaxUntilSix();
        this.biggerSix = taxaMdr.getTaxBiggerSix();
        this.anticipation = taxaMdr.getAnticipation();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCustomerRegister() {
        return idCustomerRegister;
    }

    public void setIdCustomerRegister(Integer idCustomerRegister) {
        this.idCustomerRegister = idCustomerRegister;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getUntilSix() {
        return untilSix;
    }

    public void setUntilSix(Double untilSix) {
        this.untilSix = untilSix;
    }

    public Double getBiggerSix() {
        return biggerSix;
    }

    public void setBiggerSix(Double biggerSix) {
        this.biggerSix = biggerSix;
    }

    public Double getAnticipation() {
        return anticipation;
    }

    public void setAnticipation(Double anticipation) {
        this.anticipation = anticipation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerRegisterTax that = (CustomerRegisterTax) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idCustomerRegister != null ? !idCustomerRegister.equals(that.idCustomerRegister) : that.idCustomerRegister != null)
            return false;
        if (flag != null ? !flag.equals(that.flag) : that.flag != null) return false;
        if (debit != null ? !debit.equals(that.debit) : that.debit != null) return false;
        if (credit != null ? !credit.equals(that.credit) : that.credit != null) return false;
        if (untilSix != null ? !untilSix.equals(that.untilSix) : that.untilSix != null)
            return false;
        if (biggerSix != null ? !biggerSix.equals(that.biggerSix) : that.biggerSix != null)
            return false;
        return anticipation != null ? anticipation.equals(that.anticipation) : that.anticipation == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idCustomerRegister != null ? idCustomerRegister.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        result = 31 * result + (debit != null ? debit.hashCode() : 0);
        result = 31 * result + (credit != null ? credit.hashCode() : 0);
        result = 31 * result + (untilSix != null ? untilSix.hashCode() : 0);
        result = 31 * result + (biggerSix != null ? biggerSix.hashCode() : 0);
        result = 31 * result + (anticipation != null ? anticipation.hashCode() : 0);
        return result;
    }
}
