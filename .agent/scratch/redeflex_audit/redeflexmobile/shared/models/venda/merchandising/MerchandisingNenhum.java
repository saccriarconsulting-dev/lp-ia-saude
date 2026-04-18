package com.axys.redeflexmobile.shared.models.venda.merchandising;

public class MerchandisingNenhum {
    private int id;
    private int idMerchandising;
    private String permiteMerchandising;
    private String caminhoFoto;

    public MerchandisingNenhum() {
    }

    public MerchandisingNenhum(int idMerchandising, String permiteMerchandising, String caminhoFoto) {
        this.idMerchandising = idMerchandising;
        this.permiteMerchandising = permiteMerchandising;
        this.caminhoFoto = caminhoFoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMerchandising() {
        return idMerchandising;
    }

    public void setIdMerchandising(int idMerchandising) {
        this.idMerchandising = idMerchandising;
    }

    public String permiteMerchandising() {
        return permiteMerchandising;
    }

    public void setPermiteMerchandising(String permiteMerchandising) {
        this.permiteMerchandising = permiteMerchandising;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
