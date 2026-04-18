package com.axys.redeflexmobile.shared.models.adquirencia;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Rogério Massa on 03/01/19.
 */

public class VisitProspect {

    @SerializedName("id") private Integer id;
    @SerializedName("idAppMobile") private Integer idServer;
    @SerializedName("idVendedor") private Integer idSalesman;
    @SerializedName("dataInicio") private Date dateStart;
    @SerializedName("dataFim") private Date dateFinish;
    @SerializedName("idMotivo") private Integer cancelIdReason;
    @SerializedName("idClienteSGV") private String idCustomerSgv;
    @SerializedName("idCliente") private Integer idCustomer;
    @SerializedName("idProspect") private Integer idProspect;
    @SerializedName("latitude") private double latitude;
    @SerializedName("longitude") private double longitude;
    @SerializedName("precisao") private double precision;
    @SerializedName("versaoApp") private String versionApp;
    @SerializedName("distancia") private double distance;
    @SerializedName("observacao") private String observation;
    @SerializedName("observacaoCancelamento") private String cancelObservation;
    private Integer idRoute;
    private Integer idRouteScheduled;

    public VisitProspect() {
    }

    public VisitProspect(Integer id,
                         Integer idServer,
                         int idSalesman,
                         Date dateStart,
                         Date dateFinish,
                         Integer cancelIdReason,
                         String idCustomerSgv,
                         int idCustomer,
                         int idProspect,
                         int latitude,
                         int longitude,
                         int precision,
                         String versionApp,
                         int distance,
                         String observation,
                         String cancelObservation,
                         Integer idRoute,
                         Integer idRouteScheduled) {
        this.id = id;
        this.idServer = idServer;
        this.idSalesman = idSalesman;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.cancelIdReason = cancelIdReason;
        this.idCustomerSgv = idCustomerSgv;
        this.idCustomer = idCustomer;
        this.idProspect = idProspect;
        this.latitude = latitude;
        this.longitude = longitude;
        this.precision = precision;
        this.versionApp = versionApp;
        this.distance = distance;
        this.observation = observation;
        this.cancelObservation = cancelObservation;
        this.idRoute = idRoute;
        this.idRouteScheduled = idRouteScheduled;
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

    public Integer getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(Integer idSalesman) {
        this.idSalesman = idSalesman;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public Integer getCancelIdReason() {
        return cancelIdReason;
    }

    public void setCancelIdReason(Integer cancelIdReason) {
        this.cancelIdReason = cancelIdReason;
    }

    public String getIdCustomerSgv() {
        return idCustomerSgv;
    }

    public void setIdCustomerSgv(String idCustomerSgv) {
        this.idCustomerSgv = idCustomerSgv;
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

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getCancelObservation() {
        return cancelObservation;
    }

    public void setCancelObservation(String cancelObservation) {
        this.cancelObservation = cancelObservation;
    }

    public Integer getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(Integer idRoute) {
        this.idRoute = idRoute;
    }

    public Integer getIdRouteScheduled() {
        return idRouteScheduled;
    }

    public void setIdRouteScheduled(Integer idRouteScheduled) {
        this.idRouteScheduled = idRouteScheduled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VisitProspect that = (VisitProspect) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (Double.compare(that.precision, precision) != 0) return false;
        if (Double.compare(that.distance, distance) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idServer != null ? !idServer.equals(that.idServer) : that.idServer != null)
            return false;
        if (idSalesman != null ? !idSalesman.equals(that.idSalesman) : that.idSalesman != null)
            return false;
        if (dateStart != null ? !dateStart.equals(that.dateStart) : that.dateStart != null)
            return false;
        if (dateFinish != null ? !dateFinish.equals(that.dateFinish) : that.dateFinish != null)
            return false;
        if (cancelIdReason != null ? !cancelIdReason.equals(that.cancelIdReason) : that.cancelIdReason != null)
            return false;
        if (idCustomerSgv != null ? !idCustomerSgv.equals(that.idCustomerSgv) : that.idCustomerSgv != null)
            return false;
        if (idCustomer != null ? !idCustomer.equals(that.idCustomer) : that.idCustomer != null)
            return false;
        if (idProspect != null ? !idProspect.equals(that.idProspect) : that.idProspect != null)
            return false;
        if (versionApp != null ? !versionApp.equals(that.versionApp) : that.versionApp != null)
            return false;
        if (observation != null ? !observation.equals(that.observation) : that.observation != null)
            return false;
        if (cancelObservation != null ? !cancelObservation.equals(that.cancelObservation) : that.cancelObservation != null)
            return false;
        if (idRoute != null ? !idRoute.equals(that.idRoute) : that.idRoute != null) return false;
        return idRouteScheduled != null ? idRouteScheduled.equals(that.idRouteScheduled) : that.idRouteScheduled == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idServer != null ? idServer.hashCode() : 0);
        result = 31 * result + (idSalesman != null ? idSalesman.hashCode() : 0);
        result = 31 * result + (dateStart != null ? dateStart.hashCode() : 0);
        result = 31 * result + (dateFinish != null ? dateFinish.hashCode() : 0);
        result = 31 * result + (cancelIdReason != null ? cancelIdReason.hashCode() : 0);
        result = 31 * result + (idCustomerSgv != null ? idCustomerSgv.hashCode() : 0);
        result = 31 * result + (idCustomer != null ? idCustomer.hashCode() : 0);
        result = 31 * result + (idProspect != null ? idProspect.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(precision);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (versionApp != null ? versionApp.hashCode() : 0);
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (observation != null ? observation.hashCode() : 0);
        result = 31 * result + (cancelObservation != null ? cancelObservation.hashCode() : 0);
        result = 31 * result + (idRoute != null ? idRoute.hashCode() : 0);
        result = 31 * result + (idRouteScheduled != null ? idRouteScheduled.hashCode() : 0);
        return result;
    }
}
