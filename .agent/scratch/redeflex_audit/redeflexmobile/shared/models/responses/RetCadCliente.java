package com.axys.redeflexmobile.shared.models.responses;

import com.axys.redeflexmobile.shared.models.RetCadClienteEnd;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by joao.viana on 20/09/2016.
 */
public class RetCadCliente {

    @SerializedName("id") private String id;
    @SerializedName("idAppMobile") private String idAppMobile;
    @SerializedName("cpfCnpj") private String cpfCnpj;
    @SerializedName("situacao") private String situacao;
    @SerializedName("retorno") private String retorno;
    @SerializedName("codigoSGV") private String codigoSGV;
    @SerializedName("data") private Date data;
    @SerializedName("retCadClienteEnd") private List<RetCadClienteEnd> retCadClienteEnd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAppMobile() {
        return idAppMobile;
    }

    public void setIdAppMobile(String idAppMobile) {
        this.idAppMobile = idAppMobile;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public String getCodigoSGV() {
        return codigoSGV;
    }

    public void setCodigoSGV(String codigoSGV) {
        this.codigoSGV = codigoSGV;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public List<RetCadClienteEnd> getRetCadClienteEnd() {
        return retCadClienteEnd;
    }

    public void setRetCadClienteEnd(List<RetCadClienteEnd> retCadClienteEnd) {
        this.retCadClienteEnd = retCadClienteEnd;
    }
}