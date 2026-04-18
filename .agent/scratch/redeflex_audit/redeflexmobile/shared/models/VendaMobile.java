package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 05/08/2016.
 */
public class VendaMobile {
    private int id;
    private String idVendedor;
    private String idCliente;
    private int idVisita;
    private Date data;
    private int idFormaPagamento;
    private double valorTotal;
    private double latitude;
    private double longitude;
    private double precisao;
    private String versaoApp;

    private int idItem;
    private String idProduto;
    private int qtde;
    private double valorUN;
    private Date dataCobranca;
    private String idProjeto;
    private boolean combo;
    private String IdPrecoDif;

    private int IdConsignadoRefer;
    private String ChaveCobranca;
    private int Pago;
    private String QrCodeLink;
    private String ComprovantePagto;
    private String NomeArquivoComprovante;

    public String getIdPrecoDif() {
        return IdPrecoDif;
    }

    public void setIdPrecoDif(String idPrecoDif) {
        IdPrecoDif = idPrecoDif;
    }

    public boolean isCombo() {
        return combo;
    }

    public void setCombo(boolean combo) {
        this.combo = combo;
    }

    //    private ItemVendaCombo itemVendaCombo;
//
//    public ItemVendaCombo getItemVendaCombo() {
//        return itemVendaCombo;
//    }
//
//    public void setItemVendaCombo(ItemVendaCombo itemVendaCombo) {
//        this.itemVendaCombo = itemVendaCombo;
//    }

    public String getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(String idProjeto) {
        this.idProjeto = idProjeto;
    }

    public Date getDataCobranca() {
        return dataCobranca;
    }

    public void setDataCobranca(Date dataCobranca) {
        this.dataCobranca = dataCobranca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getIdFormaPagamento() {
        return idFormaPagamento;
    }

    public void setIdFormaPagamento(int idFormaPagamento) {
        this.idFormaPagamento = idFormaPagamento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
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

    public double getPrecisao() {
        return precisao;
    }

    public void setPrecisao(double precisao) {
        this.precisao = precisao;
    }

    public String getVersaoApp() {
        return versaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        this.versaoApp = versaoApp;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public double getValorUN() {
        return valorUN;
    }

    public void setValorUN(double valorUN) {
        this.valorUN = valorUN;
    }

    public int getIdConsignadoRefer() {
        return IdConsignadoRefer;
    }

    public void setIdConsignadoRefer(int idConsignadoRefer) {
        this.IdConsignadoRefer = idConsignadoRefer;
    }

    public String getChaveCobranca() {
        return ChaveCobranca;
    }

    public void setChaveCobranca(String chaveCobranca) {
        ChaveCobranca = chaveCobranca;
    }

    public int getPago() {
        return Pago;
    }

    public void setPago(int pago) {
        Pago = pago;
    }

    public String getQrCodeLink() {
        return QrCodeLink;
    }

    public void setQrCodeLink(String qrCodeLink) {
        QrCodeLink = qrCodeLink;
    }

    public String getComprovantePagto() {
        return ComprovantePagto;
    }

    public void setComprovantePagto(String comprovantePagto) {
        ComprovantePagto = comprovantePagto;
    }

    public String getNomeArquivoComprovante() {
        return NomeArquivoComprovante;
    }

    public void setNomeArquivoComprovante(String nomeArquivoComprovante) {
        NomeArquivoComprovante = nomeArquivoComprovante;
    }
}