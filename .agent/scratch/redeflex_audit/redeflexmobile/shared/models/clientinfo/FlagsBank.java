package com.axys.redeflexmobile.shared.models.clientinfo;

import com.google.gson.annotations.SerializedName;

import java.nio.charset.Charset;

/**
 * @author lucasmarciano on 01/07/20
 */
public class FlagsBank {
    @SerializedName("idBandeira") private Integer id;
    @SerializedName("nomeBandeira") private String name;
    @SerializedName("imagem") private String image;
    @SerializedName("ativo") private boolean active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image.getBytes(Charset.forName("UTF-8"));
    }

    public String getBase64Image(){
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
