package com.axys.redeflexmobile.shared.models.customerregister;

import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.services.network.util.JsonExclude;
import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 06/02/19.
 */

public class CustomerRegisterAddress {

    @SerializedName("IdAppMobile") private Integer id;
    @SerializedName("id") private Integer idServer;
    @SerializedName("IdTipoEndereco") private Integer idAddressType;
    @SerializedName("Cep") private String postalCode;
    @SerializedName("TipoLogradouro") private String addressType;
    @SerializedName("Logradouro") private String addressName;
    @SerializedName("Bairro") private String neighborhood;
    @SerializedName("Numero") private String addressNumber;
    @SerializedName("Complemento") private String addressComplement;
    @SerializedName("UF") private String federalState;
    @SerializedName("Cidade") private String city;
    @SerializedName("NomeContato") private String contactName;
    @SerializedName("TelResidencial") private String phoneNumber;
    @SerializedName("TelCelular") private String cellphone;
    @SerializedName("Email") private String email;
    @SerializedName("Ativo") private boolean activate;
    @JsonExclude private Integer idSalesman;
    @JsonExclude private Integer idCustomer;
    @JsonExclude private String cpfCnpj;
    @JsonExclude private int sync;
    @JsonExclude private boolean validated;

    public CustomerRegisterAddress() {
        this.activate = true;
    }

    public CustomerRegisterAddress(ConsultaReceita consultaReceita) {
        this.cpfCnpj = consultaReceita.getCnpj();
        this.postalCode = consultaReceita.getCep();
        this.federalState = consultaReceita.getUf();
        this.addressName = consultaReceita.getLogradouro();
        this.neighborhood = consultaReceita.getBairro();
        this.addressNumber = consultaReceita.getNumero();
        this.addressComplement = consultaReceita.getComplemento();
        this.city = consultaReceita.getCidade();
        this.phoneNumber = consultaReceita.getTelefone();
        this.cellphone = consultaReceita.getCelular();
        this.email = consultaReceita.getEmail();
    }

    public CustomerRegisterAddress(CustomerRegisterAddress customerRegisterAddress) {
        this.id = customerRegisterAddress.getId();
        this.idServer = customerRegisterAddress.getIdServer();
        this.idCustomer = customerRegisterAddress.getIdCustomer();
        this.idSalesman = customerRegisterAddress.getIdSalesman();
        this.idAddressType = customerRegisterAddress.getIdAddressType();
        this.cpfCnpj = customerRegisterAddress.getCpfCnpj();
        this.postalCode = customerRegisterAddress.getPostalCode();
        this.addressType = customerRegisterAddress.getAddressType();
        this.addressName = customerRegisterAddress.getAddressName();
        this.neighborhood = customerRegisterAddress.getNeighborhood();
        this.addressNumber = customerRegisterAddress.getAddressNumber();
        this.addressComplement = customerRegisterAddress.getAddressComplement();
        this.federalState = customerRegisterAddress.getFederalState();
        this.city = customerRegisterAddress.getCity();
        this.contactName = customerRegisterAddress.getContactName();
        this.phoneNumber = customerRegisterAddress.getPhoneNumber();
        this.cellphone = customerRegisterAddress.getCellphone();
        this.email = customerRegisterAddress.getEmail();
        this.activate = customerRegisterAddress.isActivate();
        this.sync = customerRegisterAddress.getSync();
        this.validated = customerRegisterAddress.isValidated();
    }

    public CustomerRegisterAddress(Integer id,
                                   Integer idServer,
                                   Integer idCustomer,
                                   Integer idSalesman,
                                   Integer idAddressType,
                                   String cpfCnpj,
                                   String postalCode,
                                   String addressType,
                                   String addressName,
                                   String neighborhood,
                                   String addressNumber,
                                   String addressComplement,
                                   String federalState,
                                   String city,
                                   String contactName,
                                   String phoneNumber,
                                   String cellphone,
                                   String email,
                                   boolean activate,
                                   int sync) {
        this.id = id;
        this.idServer = idServer;
        this.idCustomer = idCustomer;
        this.idSalesman = idSalesman;
        this.idAddressType = idAddressType;
        this.cpfCnpj = cpfCnpj;
        this.postalCode = postalCode;
        this.addressType = addressType;
        this.addressName = addressName;
        this.neighborhood = neighborhood;
        this.addressNumber = addressNumber;
        this.addressComplement = addressComplement;
        this.federalState = federalState;
        this.city = city;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.cellphone = cellphone;
        this.email = email;
        this.activate = activate;
        this.sync = sync;
        this.validated = true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdServer() {
        return idServer;
    }

    public void setIdServer(Integer idServer) {
        this.idServer = idServer;
    }

    public Integer getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Integer idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Integer getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(Integer idSalesman) {
        this.idSalesman = idSalesman;
    }

    public Integer getIdAddressType() {
        return idAddressType;
    }

    public void setIdAddressType(Integer idAddressType) {
        this.idAddressType = idAddressType;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getAddressComplement() {
        return addressComplement;
    }

    public void setAddressComplement(String addressComplement) {
        this.addressComplement = addressComplement;
    }

    public String getFederalState() {
        return federalState;
    }

    public void setFederalState(String federalState) {
        this.federalState = federalState;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerRegisterAddress address = (CustomerRegisterAddress) o;

        if (activate != address.activate) return false;
        if (sync != address.sync) return false;
        if (validated != address.validated) return false;
        if (id != null ? !id.equals(address.id) : address.id != null) return false;
        if (idCustomer != null ? !idCustomer.equals(address.idCustomer) : address.idCustomer != null)
            return false;
        if (idSalesman != null ? !idSalesman.equals(address.idSalesman) : address.idSalesman != null)
            return false;
        if (idAddressType != null ? !idAddressType.equals(address.idAddressType) : address.idAddressType != null)
            return false;
        if (cpfCnpj != null ? !cpfCnpj.equals(address.cpfCnpj) : address.cpfCnpj != null)
            return false;
        if (postalCode != null ? !postalCode.equals(address.postalCode) : address.postalCode != null)
            return false;
        if (addressType != null ? !addressType.equals(address.addressType) : address.addressType != null)
            return false;
        if (addressName != null ? !addressName.equals(address.addressName) : address.addressName != null)
            return false;
        if (neighborhood != null ? !neighborhood.equals(address.neighborhood) : address.neighborhood != null)
            return false;
        if (addressNumber != null ? !addressNumber.equals(address.addressNumber) : address.addressNumber != null)
            return false;
        if (addressComplement != null ? !addressComplement.equals(address.addressComplement) : address.addressComplement != null)
            return false;
        if (federalState != null ? !federalState.equals(address.federalState) : address.federalState != null)
            return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (contactName != null ? !contactName.equals(address.contactName) : address.contactName != null)
            return false;
        if (phoneNumber != null ? !phoneNumber.equals(address.phoneNumber) : address.phoneNumber != null)
            return false;
        if (cellphone != null ? !cellphone.equals(address.cellphone) : address.cellphone != null)
            return false;
        return email != null ? email.equals(address.email) : address.email == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idCustomer != null ? idCustomer.hashCode() : 0);
        result = 31 * result + (idSalesman != null ? idSalesman.hashCode() : 0);
        result = 31 * result + (idAddressType != null ? idAddressType.hashCode() : 0);
        result = 31 * result + (cpfCnpj != null ? cpfCnpj.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (addressType != null ? addressType.hashCode() : 0);
        result = 31 * result + (addressName != null ? addressName.hashCode() : 0);
        result = 31 * result + (neighborhood != null ? neighborhood.hashCode() : 0);
        result = 31 * result + (addressNumber != null ? addressNumber.hashCode() : 0);
        result = 31 * result + (addressComplement != null ? addressComplement.hashCode() : 0);
        result = 31 * result + (federalState != null ? federalState.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (contactName != null ? contactName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (cellphone != null ? cellphone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (activate ? 1 : 0);
        result = 31 * result + sync;
        result = 31 * result + (validated ? 1 : 0);
        return result;
    }
}
