package com.axys.redeflexmobile.shared.models.adquirencia;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Rogério Massa on 28/01/19.
 */

public class VisitProspectCatalog {

    @SerializedName("catalogoRFMAId") private Integer id;
    @SerializedName("imagem") private String image;
    @SerializedName("dataHora") private Date date;
    @SerializedName("ativo") private boolean activate;

    public VisitProspectCatalog(Integer id, String image, Date date, boolean activate) {
        this.id = id;
        this.image = image;
        this.date = date;
        this.activate = activate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public boolean getActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }
}
