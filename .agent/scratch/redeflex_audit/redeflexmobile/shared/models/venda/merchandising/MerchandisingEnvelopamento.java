package com.axys.redeflexmobile.shared.models.venda.merchandising;

public class MerchandisingEnvelopamento {
    private int id;
    private int idMerchandising;
    private int idOperadora;
    private String caminhoFoto;

    public MerchandisingEnvelopamento() {
    }

    public MerchandisingEnvelopamento(int idMerchandising, int idOperadora, String caminhoFoto) {
        this.idMerchandising = idMerchandising;
        this.idOperadora = idOperadora;
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

    public int getIdOperadora() {
        return idOperadora;
    }

    public void setIdOperadora(int idOperadora) {
        this.idOperadora = idOperadora;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
