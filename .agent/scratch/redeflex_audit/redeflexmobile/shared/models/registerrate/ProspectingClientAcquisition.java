package com.axys.redeflexmobile.shared.models.registerrate;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.services.network.util.JsonExclude;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public class ProspectingClientAcquisition {
    @SerializedName("idAppMobile") private Integer id;
    @SerializedName("idVendedor") private Integer idSeller;
    @SerializedName("tipoPessoa") private String personType;
    @SerializedName("cpfCnpj") private String cpfCnpjNumber;
    @SerializedName("nomeCompleto") private String completeName;
    @SerializedName("nomeFantasia") private String fantasyName;
    @SerializedName("mcc") private Integer idMcc;
    @SerializedName("faturamentoMedioPrevisto") private double estimatedAverageBilling;
    @SerializedName("idPrazoNegociacao") private Integer idTradingTerm;
    @SerializedName("antecipacao") private Boolean anticipation;
    @SerializedName("email") private String email;
    @SerializedName("telefone") private String phone;
    @SerializedName("latitude") private double latitude;
    @SerializedName("longitude") private double longitude;
    @SerializedName("precisao") private double precision;
    @SerializedName("dataCadastro") private Date registerDate;
    @SerializedName("taxas") private List<RegistrationSimulationFee> taxes;

    @SerializedName("taxa_rav") private double taxaRav;

    // Only in local data base
    @JsonExclude
    private boolean sync;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(Integer idSeller) {
        this.idSeller = idSeller;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getCpfCnpjNumber() {
        return cpfCnpjNumber;
    }

    public void setCpfCnpjNumber(String cpfCnpj) {
        this.cpfCnpjNumber = cpfCnpj;
    }

    public Integer getIdMcc() {
        return idMcc;
    }

    public void setIdMcc(Integer mcc) {
        this.idMcc = mcc;
    }

    public double getEstimatedAverageBilling() {
        return estimatedAverageBilling;
    }

    public void setEstimatedAverageBilling(double estimatedAverageBilling) {
        this.estimatedAverageBilling = estimatedAverageBilling;
    }

    public Integer getIdTradingTerm() {
        return idTradingTerm;
    }

    public void setIdTradingTerm(Integer idTradingTerm) {
        this.idTradingTerm = idTradingTerm;
    }

    public Boolean isAnticipation() {
        return anticipation;
    }

    public void setAnticipation(Boolean anticipation) {
        this.anticipation = anticipation;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public List<RegistrationSimulationFee> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<RegistrationSimulationFee> taxes) {
        this.taxes = taxes;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public boolean isPhysical() {
        if (getPersonType().length() >= 1) {
            EnumRegisterPersonType enumPersonType = EnumRegisterPersonType
                    .getEnumByCharValue(getPersonType());

            return enumPersonType.getCharValue().equals(EnumRegisterPersonType.PHYSICAL.getCharValue());
        } else {
            return true;
        }
    }

    public double getTaxaRav() {
        return taxaRav;
    }

    public void setTaxaRav(double taxaRav) {
        this.taxaRav = taxaRav;
    }
}
