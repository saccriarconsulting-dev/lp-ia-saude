package com.axys.redeflexmobile.shared.models.adquirencia;

import com.google.gson.annotations.SerializedName;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

/**
 * @author Rogério Massa on 30/10/18.
 */

public class RoutesProspect {

    public static final int TYPE_PROSPECT = 0;
    public static final int TYPE_QUALITY = 1;

    @SerializedName("id") private Integer id;
    @SerializedName("idCliente") private Integer customerId;
    @SerializedName("idProspect") private Integer prospectId;
    @SerializedName("diaSemana") private int dayOfWeek;
    @SerializedName("ordem") private int order;
    @SerializedName("incluir") private boolean insert;
    @SerializedName("idTipo") private int typeId;
    @SerializedName("semana") private int week;
    private String customerName;
    private String customerAddress;
    private int status;
    private int typeAttendance;
    private Integer idScheduled;
    private int visitCount;

    public RoutesProspect() {
    }

    public RoutesProspect(int id,
                          Integer customerId,
                          Integer prospectId,
                          int dayOfWeek,
                          int order,
                          boolean insert,
                          int typeId,
                          int week,
                          int visitCount) {
        this.id = id;
        this.customerId = customerId;
        this.prospectId = prospectId;
        this.dayOfWeek = dayOfWeek;
        this.order = order;
        this.insert = insert;
        this.typeId = typeId;
        this.week = week;
        this.visitCount = visitCount;
    }

    public RoutesProspect(int id,
                          Integer customerId,
                          Integer prospectId,
                          int dayOfWeek,
                          int order,
                          boolean insert,
                          int typeId,
                          int week,
                          Integer idScheduled,
                          int visitCount) {
        this.id = id;
        this.customerId = customerId;
        this.prospectId = prospectId;
        this.dayOfWeek = dayOfWeek;
        this.order = order;
        this.insert = insert;
        this.typeId = typeId;
        this.week = week;
        this.idScheduled = idScheduled;
        this.visitCount = visitCount;
    }

    public RoutesProspect(RoutesProspect routesProspect) {
        this.id = null;
        this.customerId = routesProspect.getCustomerId();
        this.prospectId = routesProspect.getProspectId();
        this.dayOfWeek = routesProspect.getDayOfWeek();
        this.order = routesProspect.getOrder();
        this.insert = routesProspect.isInsert();
        this.typeId = routesProspect.getTypeId();
        this.week = routesProspect.getWeek();
        this.customerName = routesProspect.getCustomerName();
        this.customerAddress = routesProspect.getCustomerAddress();
        this.status = EMPTY_INT;
        this.typeAttendance = routesProspect.getTypeAttendance();
        this.idScheduled = routesProspect.getIdScheduled();
        this.visitCount = routesProspect.getVisitCount();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getProspectId() {
        return prospectId;
    }

    public void setProspectId(Integer prospectId) {
        this.prospectId = prospectId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTypeAttendance() {
        return typeAttendance;
    }

    public void setTypeAttendance(int typeAttendance) {
        this.typeAttendance = typeAttendance;
    }

    public Integer getIdScheduled() {
        return idScheduled;
    }

    public void setIdScheduled(Integer idScheduled) {
        this.idScheduled = idScheduled;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }
}
