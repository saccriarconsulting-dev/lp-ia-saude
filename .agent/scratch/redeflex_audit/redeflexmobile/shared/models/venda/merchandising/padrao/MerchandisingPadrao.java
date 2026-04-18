package com.axys.redeflexmobile.shared.models.venda.merchandising.padrao;

import java.util.List;

public class MerchandisingPadrao {
    private int id;
    private int idMerchandising;
    private String merchanInterno;
    private String merchanExterno;
    private String caminhoFotoInterno;
    private String caminhoFotoExterno;
    private List<MerchandisingProdutoPadrao> produtoPadraoList;

    public MerchandisingPadrao() {
    }

    public MerchandisingPadrao(int idMerchandising, String merchanInterno, String merchanExterno, String caminhoFotoInterno, String caminhoFotoExterno, List<MerchandisingProdutoPadrao> produtoPadraoList) {
        this.idMerchandising = idMerchandising;
        this.merchanInterno = merchanInterno;
        this.merchanExterno = merchanExterno;
        this.caminhoFotoInterno = caminhoFotoInterno;
        this.caminhoFotoExterno = caminhoFotoExterno;
        this.produtoPadraoList = produtoPadraoList;
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

    public String getMerchanInterno() {
        return merchanInterno;
    }

    public void setMerchanInterno(String merchanInterno) {
        this.merchanInterno = merchanInterno;
    }

    public String getMerchanExterno() {
        return merchanExterno;
    }

    public void setMerchanExterno(String merchanExterno) {
        this.merchanExterno = merchanExterno;
    }

    public String getCaminhoFotoInterno() {
        return caminhoFotoInterno;
    }

    public void setCaminhoFotoInterno(String caminhoFotoInterno) {
        this.caminhoFotoInterno = caminhoFotoInterno;
    }

    public String getCaminhoFotoExterno() {
        return caminhoFotoExterno;
    }

    public void setCaminhoFotoExterno(String caminhoFotoExterno) {
        this.caminhoFotoExterno = caminhoFotoExterno;
    }

    public List<MerchandisingProdutoPadrao> getProdutoPadraoList() {
        return produtoPadraoList;
    }

    public void setProdutoPadraoList(List<MerchandisingProdutoPadrao> produtoPadraoList) {
        this.produtoPadraoList = produtoPadraoList;
    }
}
