package com.axys.redeflexmobile.shared.models.customerregister;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType;
import com.axys.redeflexmobile.shared.models.ClienteCadastroAnexo;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectAttachment;
import com.axys.redeflexmobile.shared.services.network.util.JsonExclude;
import com.google.gson.annotations.SerializedName;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

/**
 * @author Rogério Massa on 16/11/18.
 */

public class CustomerRegisterAttachment {

    @SerializedName("id") private Integer id;
    @SerializedName("idCadastro") private Integer idRegister;
    @SerializedName("tipo") private String type;
    @SerializedName("anexo") private String file;
    @JsonExclude private String fileSize;
    @SerializedName("situacao") private Integer situation;
    @SerializedName("cpfcnpj") private String cpfCnpj;
    @SerializedName("idVendedor") private Integer idSalesman;
    @SerializedName("latitude") private Double latitude;
    @SerializedName("longitude") private Double longitude;
    @SerializedName("precisao") private Double precision;
    @SerializedName("retorno") private String returnValue;
    @JsonExclude private int sync;
    @JsonExclude private boolean activated;

    public CustomerRegisterAttachment() {
    }

    public CustomerRegisterAttachment(String type) {
        this.type = type;
    }

    public CustomerRegisterAttachment(ClienteCadastroAnexo attachment) {
        this.id = attachment.getId() != null
                ? Integer.parseInt(attachment.getId())
                : null;
        this.idRegister = attachment.getIdCadastro() != null
                ? Integer.parseInt(attachment.getIdCadastro())
                : null;
        this.type = attachment.getTipo();
        this.file = attachment.getImagem();
        this.fileSize = attachment.getTamanhoArquivo();
        this.situation = attachment.getSituacao() != null
                ? Integer.parseInt(attachment.getSituacao())
                : null;
        this.cpfCnpj = attachment.getCpfcnpj();
        this.idSalesman = attachment.getIdVendedor() != null
                ? Integer.parseInt(attachment.getIdVendedor())
                : null;
        this.latitude = attachment.getLatitude();
        this.longitude = attachment.getLongitude();
        this.precision = attachment.getPrecisao();
        this.returnValue = attachment.getMensagem();
        this.activated = true;
    }

    public CustomerRegisterAttachment(CustomerRegisterAttachment attachment) {
        this.id = attachment.getId();
        this.idRegister = attachment.getIdRegister();
        this.type = attachment.getType();
        this.file = attachment.getFile();
        this.fileSize = attachment.getFileSize();
        this.situation = attachment.getSituation();
        this.cpfCnpj = attachment.getCpfCnpj();
        this.idSalesman = attachment.getIdSalesman();
        this.latitude = attachment.getLatitude();
        this.longitude = attachment.getLongitude();
        this.precision = attachment.getPrecision();
        this.returnValue = attachment.getReturnValue();
        this.sync = attachment.getSync();
        this.activated = attachment.isActivated();
    }

    public CustomerRegisterAttachment(VisitProspectAttachment visitProspectAttachment) {
        this.type = EnumRegisterAttachmentType.FACADE.getType();
        this.file = visitProspectAttachment.getImage();
        this.fileSize = visitProspectAttachment.getImageSize();
        this.situation = EMPTY_INT;
        this.idSalesman = visitProspectAttachment.getIdSalesman();
        this.latitude = visitProspectAttachment.getLatitude();
        this.longitude = visitProspectAttachment.getLongitude();
        this.precision = visitProspectAttachment.getPrecision();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdRegister() {
        return idRegister;
    }

    public void setIdRegister(Integer idRegister) {
        this.idRegister = idRegister;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getSituation() {
        return situation;
    }

    public void setSituation(Integer situation) {
        this.situation = situation;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public Integer getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(Integer idSalesman) {
        this.idSalesman = idSalesman;
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

    public Double getPrecision() {
        return precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerRegisterAttachment that = (CustomerRegisterAttachment) o;

        if (sync != that.sync) return false;
        if (activated != that.activated) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idRegister != null ? !idRegister.equals(that.idRegister) : that.idRegister != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (file != null ? !file.equals(that.file) : that.file != null) return false;
        if (fileSize != null ? !fileSize.equals(that.fileSize) : that.fileSize != null)
            return false;
        if (situation != null ? !situation.equals(that.situation) : that.situation != null)
            return false;
        if (cpfCnpj != null ? !cpfCnpj.equals(that.cpfCnpj) : that.cpfCnpj != null) return false;
        if (idSalesman != null ? !idSalesman.equals(that.idSalesman) : that.idSalesman != null)
            return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null)
            return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null)
            return false;
        if (precision != null ? !precision.equals(that.precision) : that.precision != null)
            return false;
        return returnValue != null ? returnValue.equals(that.returnValue) : that.returnValue == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idRegister != null ? idRegister.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (fileSize != null ? fileSize.hashCode() : 0);
        result = 31 * result + (situation != null ? situation.hashCode() : 0);
        result = 31 * result + (cpfCnpj != null ? cpfCnpj.hashCode() : 0);
        result = 31 * result + (idSalesman != null ? idSalesman.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (precision != null ? precision.hashCode() : 0);
        result = 31 * result + (returnValue != null ? returnValue.hashCode() : 0);
        result = 31 * result + sync;
        result = 31 * result + (activated ? 1 : 0);
        return result;
    }
}
