package com.axys.redeflexmobile.shared.models;

import androidx.annotation.NonNull;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.dialog.GenericaDialog;

/**
 * Created by Desenvolvimento on 01/04/2016.
 */
public class Produto implements ICustomSpinnerDialogModel, GenericaDialog.GenericaItem {

    public static final String NAO_DEVE_AUDITAR_ESTOQUE = "N";
    public static final String DEVE_AUDITAR_ESTOQUE = "S";
    public static final String PERMITE_VENDA_SEM_VALOR = "N";

    private String id;
    private String codSgv;
    private String nome;
    private double valor;
    private int estoqueMax;
    private double mediaDiariaVnd;
    private int diasEstoque;
    private int estoqueAtual;
    private int estoqueSugerido;
    private int qtde;
    private String ativo;
    private String bipagem;
    private double precovenda;
    private String bipagemAuditoria;
    private int qtdCodBarra;
    private String iniciaCodBarra;
    private String bipagemCliente;
    private int qtdCombo;
    private String permiteVendaSemValor;
    private int grupo;
    private int operadora;
    private String permiteVendaPrazo;
    private double precoMedio;
    private String SituacaoSugestaoVenda;
    private int quantidadeConsignada;
    private double precovendaminimo;
    private String VendaAvista;

    private ValorPorFormaPagto valorPorFormaPagto;

    public ValorPorFormaPagto getValorPorFormaPagto() {
        return valorPorFormaPagto;
    }

    public void setValorPorFormaPagto(ValorPorFormaPagto valorPorFormaPagto) {
        this.valorPorFormaPagto = valorPorFormaPagto;
    }

    public String getPermiteVendaPrazo() {
        return permiteVendaPrazo;
    }

    public void setPermiteVendaPrazo(String permiteVendaPrazo) {
        this.permiteVendaPrazo = permiteVendaPrazo;
    }

    public int getOperadora() {
        return operadora;
    }

    public void setOperadora(int operadora) {
        this.operadora = operadora;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public String getPermiteVendaSemValor() {
        return permiteVendaSemValor;
    }

    public void setPermiteVendaSemValor(String permiteVendaSemValor) {
        this.permiteVendaSemValor = permiteVendaSemValor;
    }

    public int getQtdCombo() {
        return qtdCombo;
    }

    public void setQtdCombo(int qtdCombo) {
        this.qtdCombo = qtdCombo;
    }

    public String getBipagemCliente() {
        return bipagemCliente;
    }

    public void setBipagemCliente(String bipagemCliente) {
        this.bipagemCliente = bipagemCliente;
    }

    public int getQtdCodBarra() {
        return qtdCodBarra;
    }

    public void setQtdCodBarra(int qtdCodBarra) {
        this.qtdCodBarra = qtdCodBarra;
    }

    public String getIniciaCodBarra() {
        return iniciaCodBarra;
    }

    public void setIniciaCodBarra(String iniciaCodBarra) {
        this.iniciaCodBarra = iniciaCodBarra;
    }

    public String getBipagemAuditoria() {
        return bipagemAuditoria;
    }

    public void setBipagemAuditoria(String bipagemAuditoria) {
        this.bipagemAuditoria = bipagemAuditoria;
    }

    public double getPrecovenda() {
        return precovenda;
    }

    public void setPrecovenda(double precovenda) {
        this.precovenda = precovenda;
    }

    public String getBipagem() {
        return bipagem;
    }

    public void setBipagem(String bipagem) {
        this.bipagem = bipagem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDescricao() {
        return getNome();
    }

    public String getCodSgv() {
        return codSgv;
    }

    public void setCodSgv(String codSgv) {
        this.codSgv = codSgv;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getEstoqueMax() {
        return estoqueMax;
    }

    public void setEstoqueMax(int estoqueMax) {
        this.estoqueMax = estoqueMax;
    }

    public double getMediaDiariaVnd() {
        return mediaDiariaVnd;
    }

    public void setMediaDiariaVnd(double mediaDiariaVnd) {
        this.mediaDiariaVnd = mediaDiariaVnd;
    }

    public int getDiasEstoque() {
        return diasEstoque;
    }

    public void setDiasEstoque(int diasEstoque) {
        this.diasEstoque = diasEstoque;
    }

    public int getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(int estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public int getEstoqueSugerido() {
        return estoqueSugerido;
    }

    public void setEstoqueSugerido(int estoqueSugerido) {
        this.estoqueSugerido = estoqueSugerido;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public double getPrecoMedio() {
        return precoMedio;
    }

    public void setPrecoMedio(double precovenda) {
        this.precoMedio = precovenda;
    }

    public boolean permiteVendaSemValor() {
        return !permiteVendaSemValor.equalsIgnoreCase(PERMITE_VENDA_SEM_VALOR);
    }

    @NonNull
    @Override
    public String toString() {
        return getNome();
    }

    public String getSituacaoSugestaoVenda() {
        return SituacaoSugestaoVenda;
    }

    public void setSituacaoSugestaoVenda(String situacaoSugestaoVenda) {
        SituacaoSugestaoVenda = situacaoSugestaoVenda;
    }

    public int getQuantidadeConsignada() {
        return quantidadeConsignada;
    }

    public void setQuantidadeConsignada(int quantidadeConsignada) {
        this.quantidadeConsignada = quantidadeConsignada;
    }

    @Override
    public Integer getIdValue() {
        return Integer.parseInt(getId());
    }

    @Override
    public String getDescriptionValue() {
        return getDescricao();
    }

    public double getPrecovendaminimo() {
        return precovendaminimo;
    }

    public void setPrecovendaminimo(double precovendaminimo) {
        this.precovendaminimo = precovendaminimo;
    }

    public String getVendaAvista() {
        return VendaAvista;
    }

    public void setVendaAvista(String vendaAvista) {
        VendaAvista = vendaAvista;
    }
}