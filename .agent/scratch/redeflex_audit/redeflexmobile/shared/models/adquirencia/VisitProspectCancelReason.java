package com.axys.redeflexmobile.shared.models.adquirencia;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 07/01/19.
 */

public class VisitProspectCancelReason {

    @SerializedName("id") private int id;
    @SerializedName("tipo") private int type;
    @SerializedName("descricao") private String reason;
    @SerializedName("ativo") private String activated;
    @SerializedName("agendar") private boolean schedule;

    public VisitProspectCancelReason() {
    }

    public VisitProspectCancelReason(int id,
                                     int type,
                                     String reason,
                                     String activated,
                                     boolean schedule) {
        this.id = id;
        this.type = type;
        this.reason = reason;
        this.activated = activated;
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public boolean isSchedule() {
        return schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VisitProspectCancelReason that = (VisitProspectCancelReason) o;

        if (id != that.id) return false;
        if (type != that.type) return false;
        if (schedule != that.schedule) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;
        return activated != null ? activated.equals(that.activated) : that.activated == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + type;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (activated != null ? activated.hashCode() : 0);
        result = 31 * result + (schedule ? 1 : 0);
        return result;
    }
}
