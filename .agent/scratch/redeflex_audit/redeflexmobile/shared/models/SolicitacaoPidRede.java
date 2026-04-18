package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Optional;

public class SolicitacaoPidRede implements Serializable {
    @SerializedName("IdAppMobile") private Integer Id;
    @SerializedName("IdAppMobileSol") private Integer IdSolicitacaoPid;
    @SerializedName("CpfCnpjRede") private String CpfCnpj;
    @SerializedName("MccRede") private String Mcc;
    @SerializedName("ValorRede") private Double TpvTotal;
    @SerializedName("PorcentagemTpvRede") private Double TpvPorcentagem;

    public SolicitacaoPidRede() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getIdSolicitacaoPid() {
        return IdSolicitacaoPid;
    }

    public void setIdSolicitacaoPid(Integer idSolicitacaoPid) {
        IdSolicitacaoPid = idSolicitacaoPid;
    }

    public String getCpfCnpj() {
        return CpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        CpfCnpj = cpfCnpj;
    }

    public String getMcc() {
        return Mcc;
    }

    public void setMcc(String mcc) {
        Mcc = mcc;
    }

    public Double getTpvTotal() {
        return TpvTotal;
    }

    public void setTpvTotal(Double tpvTotal) {
        TpvTotal = tpvTotal;
    }

    public Double getTpvPorcentagem() {
        return TpvPorcentagem;
    }

    public void setTpvPorcentagem(Double tpvPorcentagem) {
        TpvPorcentagem = tpvPorcentagem;
    }
}