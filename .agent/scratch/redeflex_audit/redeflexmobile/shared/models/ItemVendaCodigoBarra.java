package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.shared.util.Util_IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Desenvolvimento on 01/07/2016.
 */
public class ItemVendaCodigoBarra {
    private int id;
    private String idVendedor;
    private int idVenda;
    private Date dataVenda;
    private int idItemVenda;
    private String idProduto;
    private String codigoBarra;
    private int idServer;
    private int quantidade;
    private String codigoBarraFinal;

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getCodigoBarraFinal() {
        return codigoBarraFinal;
    }

    public void setCodigoBarraFinal(String codigoBarraFinal) {
        this.codigoBarraFinal = codigoBarraFinal;
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

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public int getIdItemVenda() {
        return idItemVenda;
    }

    public void setIdItemVenda(int idItemVenda) {
        this.idItemVenda = idItemVenda;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    @Override
    public String toString() {
        JSONObject main = new JSONObject();
        try {
            main.put("id", getIdServer());
            main.put("idAppMobile", getId());
            main.put("idVendedor", String.valueOf(getIdVendedor()));
            main.put("idProduto", getIdProduto());
            main.put("idVenda", getIdVenda());
            main.put("dataVenda", Util_IO.dateTimeToString(getDataVenda(), "yyyy-MM-dd HH:mm:ss"));
            main.put("idItemVenda", getIdItemVenda());
            main.put("codigoBarra", getCodigoBarra());
            main.put("codigoBarraFinal", getCodigoBarraFinal());
            main.put("quantidade", getQuantidade());
            return main.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
}