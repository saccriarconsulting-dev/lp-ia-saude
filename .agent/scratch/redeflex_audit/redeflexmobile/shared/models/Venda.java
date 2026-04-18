package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.shared.util.Util_IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Desenvolvimento on 01/07/2016.
 */
public class Venda implements Serializable {

    private int id;
    private String idVendedor;
    private String idClienteSGV;
    private int idVisita;
    private Date data;
    private int idFormaPagamento;
    private int idServer;
    private String idCliente;
    private Date dataCobranca;
    private double valorTotal;
    private String identificadorAutidagem;
    private int IdConsignadoRefer;
    private String ChaveCobranca;
    private int Pago;
    private String QrCodeLink;
    private Date DataExpiracaoPix;
    private String ComprovantePagto;

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getDataCobranca() {
        return dataCobranca;
    }

    public void setDataCobranca(Date dataCobranca) {
        this.dataCobranca = dataCobranca;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
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

    public String getIdClienteSGV() {
        return idClienteSGV;
    }

    public void setIdClienteSGV(String idClienteSGV) {
        this.idClienteSGV = idClienteSGV;
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

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public String getIdentificadorAutidagem() {
        return identificadorAutidagem;
    }

    public void setIdentificadorAutidagem(String identificadorAutidagem) {
        this.identificadorAutidagem = identificadorAutidagem;
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

    public Date getDataExpiracaoPix() {
        return DataExpiracaoPix;
    }

    public void setDataExpiracaoPix(Date dataExpiracaoPix) {
        DataExpiracaoPix = dataExpiracaoPix;
    }

    public String getComprovantePagto() {
        return ComprovantePagto;
    }

    public void setComprovantePagto(String comprovantePagto) {
        ComprovantePagto = comprovantePagto;
    }

    @Override
    public String toString() {
        JSONObject main = new JSONObject();
        try {
            main.put("id", getIdServer());
            main.put("idAppMobile", getId());
            main.put("idVendedor", String.valueOf(getIdVendedor()));
            main.put("idVisita", getIdVisita());
            main.put("idFormaPagamento", getIdFormaPagamento());
            main.put("idCliente", getIdCliente());
            main.put("data", Util_IO.dateTimeToString(getData(), "yyyy-MM-dd HH:mm:ss"));
            return main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
