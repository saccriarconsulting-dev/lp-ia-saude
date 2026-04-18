package com.axys.redeflexmobile.shared.models.venda.merchandising.padrao;

import java.util.Date;

public class MerchandisingProdutoPadrao {
    private int id;
    private int idProduto;
    private int idPadrao;
    private int tipoMerchandising;
    private Date data;

    public MerchandisingProdutoPadrao() {
    }

    public MerchandisingProdutoPadrao(int idProduto, Date data, int tipoMerchandising) {
        this.idProduto = idProduto;
        this.data = data;
        this.tipoMerchandising = tipoMerchandising;
    }

    public MerchandisingProdutoPadrao(int idProduto, int idPadrao, int tipoMerchandising, Date data) {
        this.idProduto = idProduto;
        this.idPadrao = idPadrao;
        this.tipoMerchandising = tipoMerchandising;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdPadrao() {
        return idPadrao;
    }

    public void setIdPadrao(int idPadrao) {
        this.idPadrao = idPadrao;
    }

    public int getTipoMerchandising() {
        return tipoMerchandising;
    }

    public void setTipoMerchandising(int tipoMerchandising) {
        this.tipoMerchandising = tipoMerchandising;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
