package com.axys.redeflexmobile.shared.models.adquirencia;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.services.network.util.JsonExclude;
import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 03/01/19.
 */

public class RouteClientProspect {

    public static final int TYPE_PROSPECT = 0;
    public static final int TYPE_CUSTOMER = 1;

    @SerializedName("id") private Integer id;
    @SerializedName("razaoSocial") private String nameFull;
    @SerializedName("nomeFatansia") private String nameFantasy;
    @SerializedName("cpf_cnpj") private String cpfCnpj;
    @SerializedName("cep") private String postalCode;
    @SerializedName("bairro") private String neighborhood;
    @SerializedName("nomeLogradouro") private String addressName;
    @SerializedName("numeroLogradouro") private String addressNumber;
    @SerializedName("complementoLogradouro") private String addressComplement;
    @SerializedName("tipoLogradouro") private String addressType;
    @SerializedName("cidade") private String city;
    @SerializedName("estado") private String federalState;
    @SerializedName("dddTelefone") private String dddTelephone;
    @SerializedName("telefone") private String telephone;
    @SerializedName("dddCelular") private String dddCellphone;
    @SerializedName("celular") private String cellphone;
    @SerializedName("contato") private String contact;
    @SerializedName("latitude") private Double latitude;
    @SerializedName("longitude") private Double longitude;
    @SerializedName("email") private String email;
    @JsonExclude private String sgvCode;
    @JsonExclude private int type;
    @JsonExclude private Integer cerca;
    @JsonExclude private boolean contactUpdate;

    public RouteClientProspect() {
    }

    public RouteClientProspect(Integer id,
                               String nameFull,
                               String nameFantasy,
                               String cpfCnpj,
                               String postalCode,
                               String neighborhood,
                               String addressName,
                               String addressNumber,
                               String addressComplement,
                               String addressType,
                               String city,
                               String federalState,
                               String dddTelephone,
                               String telephone,
                               String dddCellphone,
                               String cellphone,
                               String contact,
                               Double latitude,
                               Double longitude,
                               String email) {
        this.id = id;
        this.nameFull = nameFull;
        this.nameFantasy = nameFantasy;
        this.cpfCnpj = cpfCnpj;
        this.postalCode = postalCode;
        this.neighborhood = neighborhood;
        this.addressName = addressName;
        this.addressNumber = addressNumber;
        this.addressComplement = addressComplement;
        this.addressType = addressType;
        this.city = city;
        this.federalState = federalState;
        this.dddTelephone = dddTelephone;
        this.telephone = telephone;
        this.dddCellphone = dddCellphone;
        this.cellphone = cellphone;
        this.contact = contact;
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
        this.type = TYPE_PROSPECT;
    }

    public RouteClientProspect(Cliente customer) {
        this.id = Integer.valueOf(customer.getId());
        this.nameFull = customer.getRazaoSocial();
        this.nameFantasy = customer.getNomeFantasia();
        this.cpfCnpj = customer.getCpf_cnpj();
        this.postalCode = customer.getCep();
        this.neighborhood = customer.getBairro();
        this.addressName = customer.getNomeLogradouro();
        this.addressNumber = customer.getNumeroLogradouro();
        this.addressComplement = customer.getComplementoLogradouro();
        this.addressType = customer.getTipoLogradouro();
        this.city = customer.getCidade();
        this.federalState = customer.getEstado();
        this.dddTelephone = customer.getDddTelefone();
        this.telephone = customer.getTelefone();
        this.dddCellphone = customer.getDddCelular();
        this.cellphone = customer.getCelular();
        this.latitude = customer.getLatitude();
        this.longitude = customer.getLongitude();
        this.email = customer.getEmailCliente();
        this.cerca = customer.getCerca();
        this.sgvCode = customer.getCodigoSGV();
        this.contactUpdate = customer.getAtualizaContato() != null && customer.getAtualizaContato().equals("S");
        this.type = TYPE_CUSTOMER;
    }

    //region GET/SET
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameFull() {
        return nameFull;
    }

    public void setNameFull(String nameFull) {
        this.nameFull = nameFull;
    }

    public String getNameFantasy() {
        return nameFantasy;
    }

    public void setNameFantasy(String nameFantasy) {
        this.nameFantasy = nameFantasy;
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

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
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

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFederalState() {
        return federalState;
    }

    public void setFederalState(String federalState) {
        this.federalState = federalState;
    }

    public String getDddTelephone() {
        return dddTelephone;
    }

    public void setDddTelephone(String dddTelephone) {
        this.dddTelephone = dddTelephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDddCellphone() {
        return dddCellphone;
    }

    public void setDddCellphone(String dddCellphone) {
        this.dddCellphone = dddCellphone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSgvCode() {
        return sgvCode;
    }

    public void setSgvCode(String sgvCode) {
        this.sgvCode = sgvCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getCerca() {
        return cerca;
    }

    public void setCerca(Integer cerca) {
        this.cerca = cerca;
    }

    public boolean isContactUpdate() {
        return contactUpdate;
    }

    public void setContactUpdate(boolean contactUpdate) {
        this.contactUpdate = contactUpdate;
    }

    //endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteClientProspect that = (RouteClientProspect) o;

        if (type != that.type) return false;
        if (contactUpdate != that.contactUpdate) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (nameFull != null ? !nameFull.equals(that.nameFull) : that.nameFull != null)
            return false;
        if (nameFantasy != null ? !nameFantasy.equals(that.nameFantasy) : that.nameFantasy != null)
            return false;
        if (cpfCnpj != null ? !cpfCnpj.equals(that.cpfCnpj) : that.cpfCnpj != null) return false;
        if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null)
            return false;
        if (neighborhood != null ? !neighborhood.equals(that.neighborhood) : that.neighborhood != null)
            return false;
        if (addressName != null ? !addressName.equals(that.addressName) : that.addressName != null)
            return false;
        if (addressNumber != null ? !addressNumber.equals(that.addressNumber) : that.addressNumber != null)
            return false;
        if (addressComplement != null ? !addressComplement.equals(that.addressComplement) : that.addressComplement != null)
            return false;
        if (addressType != null ? !addressType.equals(that.addressType) : that.addressType != null)
            return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (federalState != null ? !federalState.equals(that.federalState) : that.federalState != null)
            return false;
        if (dddTelephone != null ? !dddTelephone.equals(that.dddTelephone) : that.dddTelephone != null)
            return false;
        if (telephone != null ? !telephone.equals(that.telephone) : that.telephone != null)
            return false;
        if (dddCellphone != null ? !dddCellphone.equals(that.dddCellphone) : that.dddCellphone != null)
            return false;
        if (cellphone != null ? !cellphone.equals(that.cellphone) : that.cellphone != null)
            return false;
        if (contact != null ? !contact.equals(that.contact) : that.contact != null) return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null)
            return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (sgvCode != null ? !sgvCode.equals(that.sgvCode) : that.sgvCode != null) return false;
        return cerca != null ? cerca.equals(that.cerca) : that.cerca == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nameFull != null ? nameFull.hashCode() : 0);
        result = 31 * result + (nameFantasy != null ? nameFantasy.hashCode() : 0);
        result = 31 * result + (cpfCnpj != null ? cpfCnpj.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (neighborhood != null ? neighborhood.hashCode() : 0);
        result = 31 * result + (addressName != null ? addressName.hashCode() : 0);
        result = 31 * result + (addressNumber != null ? addressNumber.hashCode() : 0);
        result = 31 * result + (addressComplement != null ? addressComplement.hashCode() : 0);
        result = 31 * result + (addressType != null ? addressType.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (federalState != null ? federalState.hashCode() : 0);
        result = 31 * result + (dddTelephone != null ? dddTelephone.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (dddCellphone != null ? dddCellphone.hashCode() : 0);
        result = 31 * result + (cellphone != null ? cellphone.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (sgvCode != null ? sgvCode.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + (cerca != null ? cerca.hashCode() : 0);
        result = 31 * result + (contactUpdate ? 1 : 0);
        return result;
    }
}
