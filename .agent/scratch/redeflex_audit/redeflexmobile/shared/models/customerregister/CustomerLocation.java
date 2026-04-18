package com.axys.redeflexmobile.shared.models.customerregister;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 19/11/18.
 */

public class CustomerLocation {

    @SerializedName("id") private int id;
    @SerializedName("latitude") private Double latitude;
    @SerializedName("longitude") private Double longitude;
    @SerializedName("precisao") private Double precision;
    @SerializedName("idCliente") private String idClient;
    @SerializedName("datagps") private String dateGps;
    @SerializedName("anexo") private String attachment;
    @SerializedName("localArquivo") private String fileLocation;
    private int sync;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getDateGps() {
        return dateGps;
    }

    public void setDateGps(String dateGps) {
        this.dateGps = dateGps;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }
}
