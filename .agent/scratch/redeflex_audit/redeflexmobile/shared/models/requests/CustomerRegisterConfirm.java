package com.axys.redeflexmobile.shared.models.requests;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 2019-11-05.
 */

public class CustomerRegisterConfirm {

    @SerializedName("IdVendedor") private int idSalesman;
    @SerializedName("TipoCliente") private String customerType;
    @SerializedName("CpfCnpj") private String cpfCnpj;
    @SerializedName("IdAppMobile") private int idAppMobile;

    public CustomerRegisterConfirm(int idSalesman, String customerType, String cpfCnpj, int idAppMobile) {
        this.idSalesman = idSalesman;
        this.customerType = customerType;
        this.cpfCnpj = cpfCnpj;
        this.idAppMobile = idAppMobile;
    }

    public int getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(int idSalesman) {
        this.idSalesman = idSalesman;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public int getIdAppMobile() {
        return idAppMobile;
    }

    public void setIdAppMobile(int idAppMobile) {
        this.idAppMobile = idAppMobile;
    }
}
