package com.axys.redeflexmobile.shared.models.requests;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterSexo;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddress;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContato;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContatoPrincipal;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterHorarioFunc;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterTax;
import com.axys.redeflexmobile.shared.models.customerregister.MachineType;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Request body for client registration via API.
 */
public class ClienteCadastroRequest {
    private String id;
    private String idSalesman;
    private String dateRegister;
    private String dateUpdate;
    private boolean reRegister;
    private EnumRegisterCustomerType customerType;
    private EnumRegisterPersonType personType;
    private Integer mcc;
    private String nomeRazaoSocial;
    private String nomeFantasia;
    private String cpfCnpj;
    private String partnerCpf;
    private String partnerName;
    private String partnerBirthday;
    private String accountType;
    private Integer bank;
    private String bankAgency;
    private String bankAgencyDigit;
    private String bankAccount;
    private String bankAccountDigit;
    private Double foreseenRevenue;
    private Integer negotiationTermId;
    private boolean anticipationAutomatic;
    private Double anticipationValue;
    private List<CustomerRegisterTax> taxList;
    private List<MachineType> posList;
    private String rentalMachineDue;
    private Integer exemption;
    private String observation;
    private List<CustomerRegisterContato> contacts;
    private List<CustomerRegisterAddress> addresses;
    private List<String> attachments;
    private Integer professionId;
    private Integer incomeId;
    private Integer assetId;
    private EnumRegisterSexo gender;
    private List<CustomerRegisterHorarioFunc> horarioFunc;
    private CustomerRegisterContatoPrincipal mainContact;
    private Double latitude;
    private Double longitude;
    private Double precision;
    private String appVersion;

    public ClienteCadastroRequest(
            @NonNull String id,
            @NonNull String idSalesman,
            @NonNull String dateRegister,
            @NonNull String dateUpdate,
            boolean reRegister,
            @NonNull EnumRegisterCustomerType customerType,
            @NonNull EnumRegisterPersonType personType,
            Integer mcc,
            String nomeRazaoSocial,
            String nomeFantasia,
            String cpfCnpj,
            String partnerCpf,
            String partnerName,
            String partnerBirthday,
            String accountType,
            Integer bank,
            String bankAgency,
            String bankAgencyDigit,
            String bankAccount,
            String bankAccountDigit,
            Double foreseenRevenue,
            Integer negotiationTermId,
            boolean anticipationAutomatic,
            Double anticipationValue,
            List<CustomerRegisterTax> taxList,
            List<MachineType> posList,
            String rentalMachineDue,
            Integer exemption,
            String observation,
            List<CustomerRegisterContato> contacts,
            List<CustomerRegisterAddress> addresses,
            List<String> attachments,
            Integer professionId,
            Integer incomeId,
            Integer assetId,
            @NonNull EnumRegisterSexo gender,
            List<CustomerRegisterHorarioFunc> horarioFunc,
            CustomerRegisterContatoPrincipal mainContact,
            Double latitude,
            Double longitude,
            Double precision,
            @NonNull String appVersion
    ) {
        this.id                    = id;
        this.idSalesman            = idSalesman;
        this.dateRegister          = dateRegister;
        this.dateUpdate            = dateUpdate;
        this.reRegister            = reRegister;
        this.customerType          = customerType;
        this.personType            = personType;
        this.mcc                   = mcc;
        this.nomeRazaoSocial       = nomeRazaoSocial;
        this.nomeFantasia          = nomeFantasia;
        this.cpfCnpj               = cpfCnpj;
        this.partnerCpf            = partnerCpf;
        this.partnerName           = partnerName;
        this.partnerBirthday       = partnerBirthday;
        this.accountType           = accountType;
        this.bank                  = bank;
        this.bankAgency            = bankAgency;
        this.bankAgencyDigit       = bankAgencyDigit;
        this.bankAccount           = bankAccount;
        this.bankAccountDigit      = bankAccountDigit;
        this.foreseenRevenue       = foreseenRevenue;
        this.negotiationTermId     = negotiationTermId;
        this.anticipationAutomatic = anticipationAutomatic;
        this.anticipationValue     = anticipationValue;
        this.taxList               = taxList;
        this.posList               = posList;
        this.rentalMachineDue      = rentalMachineDue;
        this.exemption             = exemption;
        this.observation           = observation;
        this.contacts              = contacts;
        this.addresses             = addresses;
        this.attachments           = attachments;
        this.professionId          = professionId;
        this.incomeId              = incomeId;
        this.assetId               = assetId;
        this.gender                = gender;
        this.horarioFunc           = horarioFunc;
        this.mainContact           = mainContact;
        this.latitude              = latitude;
        this.longitude             = longitude;
        this.precision             = precision;
        this.appVersion            = appVersion;
    }

    public String getId() {
        return id;
    }

    public String getIdSalesman() {
        return idSalesman;
    }

    public String getDateRegister() {
        return dateRegister;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public boolean isReRegister() {
        return reRegister;
    }

    public EnumRegisterCustomerType getCustomerType() {
        return customerType;
    }

    public EnumRegisterPersonType getPersonType() {
        return personType;
    }

    public Integer getMcc() {
        return mcc;
    }

    public String getNomeRazaoSocial() {
        return nomeRazaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public String getPartnerCpf() {
        return partnerCpf;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getPartnerBirthday() {
        return partnerBirthday;
    }

    public String getAccountType() {
        return accountType;
    }

    public Integer getBank() {
        return bank;
    }

    public String getBankAgency() {
        return bankAgency;
    }

    public String getBankAgencyDigit() {
        return bankAgencyDigit;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getBankAccountDigit() {
        return bankAccountDigit;
    }

    public Double getForeseenRevenue() {
        return foreseenRevenue;
    }

    public Integer getNegotiationTermId() {
        return negotiationTermId;
    }

    public boolean isAnticipationAutomatic() {
        return anticipationAutomatic;
    }

    public Double getAnticipationValue() {
        return anticipationValue;
    }

    public List<CustomerRegisterTax> getTaxList() {
        return taxList;
    }

    public List<MachineType> getPosList() {
        return posList;
    }

    public String getRentalMachineDue() {
        return rentalMachineDue;
    }

    public Integer getExemption() {
        return exemption;
    }

    public String getObservation() {
        return observation;
    }

    public List<CustomerRegisterContato> getContacts() {
        return contacts;
    }

    public List<CustomerRegisterAddress> getAddresses() {
        return addresses;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public Integer getProfessionId() {
        return professionId;
    }

    public Integer getIncomeId() {
        return incomeId;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public EnumRegisterSexo getGender() {
        return gender;
    }

    public List<CustomerRegisterHorarioFunc> getHorarioFunc() {
        return horarioFunc;
    }

    public CustomerRegisterContatoPrincipal getMainContact() {
        return mainContact;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getPrecision() {
        return precision;
    }

    public String getAppVersion() {
        return appVersion;
    }
}