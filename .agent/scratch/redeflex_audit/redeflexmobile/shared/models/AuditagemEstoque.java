package com.axys.redeflexmobile.shared.models;

import java.util.Date;
import java.util.List;

/**
 * Created by Desenvolvimento on 20/06/2016.
 */
public class AuditagemEstoque {

    private int id;
    private String idVendedor;
    private String idProduto;
    private String nomeProduto;
    private int qtdeInformada;
    private int qtdeReal;
    private Date data;
    private List<CodBarra> codigosList;
    private String codigoBarra;
    private String codigoBarraFinal;
    private int idCodigoBarra;
    private double valorUnitario;
    private String identificadorVenda;

    public int getIdCodigoBarra() {
        return idCodigoBarra;
    }

    public void setIdCodigoBarra(int idCodigoBarra) {
        this.idCodigoBarra = idCodigoBarra;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getCodigoBarraFinal() {
        return codigoBarraFinal;
    }

    public void setCodigoBarraFinal(String codigoBarraFinal) {
        this.codigoBarraFinal = codigoBarraFinal;
    }

    public List<CodBarra> getCodigosList() {
        return codigosList;
    }

    public void setCodigosList(List<CodBarra> codigosList) {
        this.codigosList = codigosList;
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

    public int getQtdeInformada() {
        return qtdeInformada;
    }

    public void setQtdeInformada(int qtdeInformada) {
        this.qtdeInformada = qtdeInformada;
    }

    public int getQtdeReal() {
        return qtdeReal;
    }

    public void setQtdeReal(int qtdeReal) {
        this.qtdeReal = qtdeReal;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getQuantidadeDivergente() {
        return qtdeReal - qtdeInformada;
    }
}