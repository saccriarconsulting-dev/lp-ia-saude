package com.axys.redeflexmobile.shared.models.clientinfo;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lucasmarciano on 30/06/20
 */
public class ClientHomeBanking {
    private Integer id;
    @SerializedName("idCliente")
    private Integer idClient;
    @SerializedName("nomeBanco")
    private String bankName;
    @SerializedName("tipoConta")
    private String countType;
    @SerializedName("agencia")
    private String agency;
    @SerializedName("agenciaDigito")
    private String digitAgency;
    @SerializedName("conta")
    private String count;
    @SerializedName("contaDigito")
    private String digitCount;
    @SerializedName("idBandeira")
    private Integer idFlag;
    @SerializedName("ativo")
    private boolean active;

    private List<FlagsBank> flags;

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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCountType() {
        return countType;
    }

    public void setCountType(String countType) {
        this.countType = countType;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<FlagsBank> getFlags() {
        return flags;
    }

    public void setFlags(List<FlagsBank> flags) {
        this.flags = flags;
    }

    public String getDigitAgency() {
        if (digitAgency == null)
            return "";
        else
            return digitAgency;
    }

    public void setDigitAgency(String digitAgency) {
        this.digitAgency = digitAgency;
    }

    public String getDigitCount() {
        if (digitCount == null)
            return "";
        else
            return digitCount;
    }

    public void setDigitCount(String digitCount) {
        this.digitCount = digitCount;
    }

    public Integer getIdFlag() {
        return idFlag;
    }

    public void setIdFlag(Integer idFlag) {
        this.idFlag = idFlag;
    }

    public String getAgencyInfo() {
        if (digitAgency != null)
            return String.format("%s-%s", agency, digitAgency);
        else
            return agency;
    }

    public String getCountInfo() {
        if (digitCount != null)
            return String.format("%s-%s", count, digitCount);
        else
            return count;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;

        ClientHomeBanking clientHomeBanking = (ClientHomeBanking) obj;

        return clientHomeBanking.getBankName().equals(bankName)
                && clientHomeBanking.getCountType().equals(countType)
                && clientHomeBanking.getAgency().equals(agency)
                && clientHomeBanking.getDigitAgency().equals(digitAgency)
                && clientHomeBanking.getCount().equals(count)
                && clientHomeBanking.getDigitCount().equals(digitCount);
    }
}
