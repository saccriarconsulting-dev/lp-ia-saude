package com.axys.redeflexmobile.shared.models.customerregister;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;

/**
 * @author Rogério Massa on 30/10/18.
 */

public class RegisterListItem {

    private int id;
    private int status;
    private int clientId; /* ID na tabela ClienteCadastro */
    private Integer customerId; /* ID na tabela Cliente */
    private String clientName;
    private String clientDateRegister;
    private String clientDateChange;
    private String clientCpf;
    private String reason;
    private EnumRegisterCustomerType customerType;
    private Double latitude; // Latitude da tabela ClienteCadastroAnexo
    private Double longitude; // Longitude da tabela ClienteCadastroAnexo
    private String sgvCode;
    private String token;
    private boolean confirmed;

    public RegisterListItem() {
    }

    public RegisterListItem(final CustomerRegister customer) {
        this.id = Integer.parseInt(customer.getId());
        this.status = customer.getSync();
        this.clientId = this.id;
        this.clientName = StringUtils.isNotEmpty(customer.getFullName())
                ? customer.getFullName()
                : customer.getFantasyName();
        this.clientDateRegister = Util_IO.dateToStringBr(customer.getDateRegister());
        this.clientDateChange = Util_IO.dateToStringBr(customer.getDateUpdate());
        this.clientCpf = customer.getCpfCnpj();
        this.customerType = customer.getCustomerType();
        this.customerId = customer.getIdServer();
        this.reason = customer.getReturnValue();
        this.sgvCode = customer.getSgvCode();
        this.token = customer.getToken();
        this.confirmed = customer.isConfirmed();

        getLocationFromAttach(customer);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientDateRegister() {
        return clientDateRegister;
    }

    public void setClientDateRegister(String clientDateRegister) {
        this.clientDateRegister = clientDateRegister;
    }

    public String getClientDateChange() {
        return clientDateChange;
    }

    public void setClientDateChange(String clientDateChange) {
        this.clientDateChange = clientDateChange;
    }

    public String getClientCpf() {
        return clientCpf;
    }

    public void setClientCpf(String clientCpf) {
        this.clientCpf = clientCpf;
    }

    public EnumRegisterCustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(EnumRegisterCustomerType customerType) {
        this.customerType = customerType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getSgvCode() {
        return sgvCode;
    }

    public void setSgvCode(String sgvCode) {
        this.sgvCode = sgvCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    private void getLocationFromAttach(CustomerRegister customer) {
        Optional<CustomerRegisterAttachment> attachmentOptional = Stream.of(customer.getAttachments())
                .filter(value -> EnumRegisterAttachmentType.FACADE.getType().equals(value.getType()))
                .findFirst();

        if (!attachmentOptional.isEmpty()) {
            CustomerRegisterAttachment attachment = attachmentOptional.get();
            latitude = attachment.getLatitude();
            longitude = attachment.getLongitude();
        }
    }
}
