package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.shared.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Desenvolvimento on 01/07/2016.
 */
public class ItemVenda extends ArrayList<ItemVenda> {

    public static final String LER_CODIGO_BARRA = "S";

    private int id;
    private String idVendedor;
    private String idProduto;
    private boolean combo;
    private String nomeProduto;
    private int idVenda;
    private Date dataVenda;
    private int qtde;
    private double valorUN;
    private int idServer;
    private List<CodBarra> codigosList;
    private int idPreco;
    private String bipagem;
    private int quantidadeSerial;
    private String situacaosugestaovenda;

    public ItemVenda() {
        codigosList = new ArrayList<>();
    }

    public int getIdPreco() {
        return idPreco;
    }

    public void setIdPreco(int idPreco) {
        this.idPreco = idPreco;
    }

    public void setIdPreco(String idPreco) {
        if (StringUtils.isEmpty(idPreco)) {
            return;
        }

        try {
            this.idPreco = Integer.parseInt(idPreco);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public boolean isCombo() {
        return combo;
    }

    public void setCombo(boolean combo) {
        this.combo = combo;
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

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
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

    public List<CodBarra> getCodigosList() {
        return codigosList;
    }

    public void setCodigosList(List<CodBarra> codigosList) {
        this.codigosList = codigosList;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public String getBipagem() {
        return bipagem;
    }

    public void setBipagem(String bipagem) {
        this.bipagem = bipagem;
    }

    public int getQuantidadeSerial() {
        return quantidadeSerial;
    }

    public void setQuantidadeSerial(int quantidadeSerial) {
        this.quantidadeSerial = quantidadeSerial;
    }

    public String getSituacaosugestaovenda() {
        return situacaosugestaovenda;
    }

    public void setSituacaosugestaovenda(String situacaosugestaovenda) {
        this.situacaosugestaovenda = situacaosugestaovenda;
    }
}