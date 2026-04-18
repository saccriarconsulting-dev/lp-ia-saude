package com.axys.redeflexmobile.shared.models.customerregister;

import com.axys.redeflexmobile.shared.models.ClienteCadastroPOS;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Rogério Massa on 16/11/18.
 */

public class MachineType implements ICustomSpinnerDialogModel {

    @SerializedName("IdAppMobile") private Integer id;
    @SerializedName("IdClienteCadastro") private int idCustomerRegister;
    @SerializedName("TipoMaquinaId") private Integer idMachineType;
    @SerializedName("IdTerminal") private String idTerminal;
    @SerializedName("ValorAluguel") private Double rentValue;
    @SerializedName("DataSync") private Date dateSync;
    @SerializedName("CpfCnpjCliente") private String customerCpfCnpj;
    @SerializedName("TipoConexaoId") private Integer idConnectionType;

    /* 0: deletado, 1: ativo */
    @SerializedName("Situacao") private int situation;
    @SerializedName("Descricao") private String description;
    @SerializedName("IdModelo") private Integer modelId;
    @SerializedName("Modelo") private String model;
    @SerializedName("ValorAluguelSelecionado") private double selectedRentValue;
    @SerializedName("IdOperadora") private Integer carrierId;
    @SerializedName("MetragemCabo") private Integer cableLength;

    public MachineType() {
    }

    public MachineType(MachineType item) {
        this.id = item.getId();
        this.idCustomerRegister = item.getIdCustomerRegister();
        this.idMachineType = item.getIdMachineType();
        this.idTerminal = item.getIdTerminal();
        this.rentValue = item.getRentValue();
        this.dateSync = item.getDateSync();
        this.customerCpfCnpj = item.getCustomerCpfCnpj();
        this.situation = item.getSituation();
        this.description = item.getDescription();
        this.model = item.getModel();
        this.selectedRentValue = item.getSelectedRentValue();
        this.idConnectionType = item.getIdConnectionType();
    }

    public MachineType(ClienteCadastroPOS item) {
        this.id = item.getIdAppMobile();
        this.idCustomerRegister = item.getIdClienteCadastro();
        this.idMachineType = item.getIdTipoMaquina();
        this.idTerminal = item.getIdTerminal();
        this.rentValue = item.getValorAluguel();
        this.dateSync = item.getDataSync();
        this.customerCpfCnpj = item.getCpfCnpjCliente();
        this.situation = item.getSituacao();
        this.description = item.getPosDescricao();
        this.model = item.getPosModelo();
        this.selectedRentValue = item.getValorAluguel();
        this.idConnectionType = item.getTipoConexao();
        this.cableLength = item.getMetragemCabo();
        this.carrierId = item.getIdOperadora();
    }

    public MachineType(ModeloPOS item) {
        this.idMachineType = item.getIdTipoMaquina();
        this.description = item.getDescricao();
        this.model = item.getModelo();
        this.rentValue = item.getValorAluguelPadrao();
        this.selectedRentValue = item.getValorAluguelPadrao();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdCustomerRegister() {
        return idCustomerRegister;
    }

    public void setIdCustomerRegister(int idCustomerRegister) {
        this.idCustomerRegister = idCustomerRegister;
    }

    public Integer getIdMachineType() {
        return idMachineType;
    }

    public void setIdMachineType(Integer idMachineType) {
        this.idMachineType = idMachineType;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(String idTerminal) {
        this.idTerminal = idTerminal;
    }

    public Double getRentValue() {
        return rentValue;
    }

    public void setRentValue(Double rentValue) {
        this.rentValue = rentValue;
    }

    public Date getDateSync() {
        return dateSync;
    }

    public void setDateSync(Date dateSync) {
        this.dateSync = dateSync;
    }

    public String getCustomerCpfCnpj() {
        return customerCpfCnpj;
    }

    public void setCustomerCpfCnpj(String customerCpfCnpj) {
        this.customerCpfCnpj = customerCpfCnpj;
    }

    public Integer getIdConnectionType() {
        return idConnectionType;
    }

    public void setIdConnectionType(Integer idConnectionType) {
        this.idConnectionType = idConnectionType;
    }

    public int getSituation() {
        return situation;
    }

    public void setSituation(int situation) {
        this.situation = situation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getSelectedRentValue() {
        return selectedRentValue;
    }

    public void setSelectedRentValue(double selectedRentValue) {
        this.selectedRentValue = selectedRentValue;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }

    public Integer getCableLength() {
        return cableLength;
    }

    public void setCableLength(Integer cableLength) {
        this.cableLength = cableLength;
    }

    @Override
    public Integer getIdValue() {
        return id;
    }

    @Override
    public String getDescriptionValue() {
        return description + ' ' + model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MachineType that = (MachineType) o;

        if (idCustomerRegister != that.idCustomerRegister) return false;
        if (situation != that.situation) return false;
        if (Double.compare(that.selectedRentValue, selectedRentValue) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idMachineType != null ? !idMachineType.equals(that.idMachineType) : that.idMachineType != null)
            return false;
        if (idTerminal != null ? !idTerminal.equals(that.idTerminal) : that.idTerminal != null)
            return false;
        if (rentValue != null ? !rentValue.equals(that.rentValue) : that.rentValue != null)
            return false;
        if (dateSync != null ? !dateSync.equals(that.dateSync) : that.dateSync != null)
            return false;
        if (customerCpfCnpj != null ? !customerCpfCnpj.equals(that.customerCpfCnpj) : that.customerCpfCnpj != null)
            return false;
        if (idConnectionType != null ? !idConnectionType.equals(that.idConnectionType) : that.idConnectionType != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (modelId != null ? !modelId.equals(that.modelId) : that.modelId != null) return false;
        return model != null ? model.equals(that.model) : that.model == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + idCustomerRegister;
        result = 31 * result + (idMachineType != null ? idMachineType.hashCode() : 0);
        result = 31 * result + (idTerminal != null ? idTerminal.hashCode() : 0);
        result = 31 * result + (rentValue != null ? rentValue.hashCode() : 0);
        result = 31 * result + (dateSync != null ? dateSync.hashCode() : 0);
        result = 31 * result + (customerCpfCnpj != null ? customerCpfCnpj.hashCode() : 0);
        result = 31 * result + (idConnectionType != null ? idConnectionType.hashCode() : 0);
        result = 31 * result + situation;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (modelId != null ? modelId.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        temp = Double.doubleToLongBits(selectedRentValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}