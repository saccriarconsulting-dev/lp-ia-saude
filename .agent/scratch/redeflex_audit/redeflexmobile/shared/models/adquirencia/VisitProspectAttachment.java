package com.axys.redeflexmobile.shared.models.adquirencia;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Rogério Massa on 25/01/19.
 */

public class VisitProspectAttachment {

    @SerializedName("id") private Integer id;
    @SerializedName("idVendedor") private Integer idSalesman;
    @SerializedName("idVisita") private Long idVisit;
    @SerializedName("idCliente") private Integer idCustomer;
    @SerializedName("idProspect") private Integer idProspect;
    @SerializedName("foto") private String image;
    @SerializedName("DataGps") private Date date;
    @SerializedName("latitude") private double latitude;
    @SerializedName("longitude") private double longitude;
    @SerializedName("precisao") private double precision;
    private String imageSize;

    public VisitProspectAttachment() {
    }

    public VisitProspectAttachment(Integer id,
                                   Integer idSalesman,
                                   Long idVisit,
                                   Integer idCustomer,
                                   Integer idProspect,
                                   String image,
                                   Date date,
                                   double latitude,
                                   double longitude,
                                   double precision) {
        this.id = id;
        this.idSalesman = idSalesman;
        this.idVisit = idVisit;
        this.idCustomer = idCustomer;
        this.idProspect = idProspect;
        this.image = image;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.precision = precision;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(Integer idSalesman) {
        this.idSalesman = idSalesman;
    }

    public Long getIdVisit() {
        return idVisit;
    }

    public void setIdVisit(Long idVisit) {
        this.idVisit = idVisit;
    }

    public Integer getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Integer idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Integer getIdProspect() {
        return idProspect;
    }

    public void setIdProspect(Integer idProspect) {
        this.idProspect = idProspect;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VisitProspectAttachment that = (VisitProspectAttachment) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (Double.compare(that.precision, precision) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idSalesman != null ? !idSalesman.equals(that.idSalesman) : that.idSalesman != null)
            return false;
        if (idVisit != null ? !idVisit.equals(that.idVisit) : that.idVisit != null) return false;
        if (idCustomer != null ? !idCustomer.equals(that.idCustomer) : that.idCustomer != null)
            return false;
        if (idProspect != null ? !idProspect.equals(that.idProspect) : that.idProspect != null)
            return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return imageSize != null ? imageSize.equals(that.imageSize) : that.imageSize == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idSalesman != null ? idSalesman.hashCode() : 0);
        result = 31 * result + (idVisit != null ? idVisit.hashCode() : 0);
        result = 31 * result + (idCustomer != null ? idCustomer.hashCode() : 0);
        result = 31 * result + (idProspect != null ? idProspect.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(precision);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (imageSize != null ? imageSize.hashCode() : 0);
        return result;
    }
}
