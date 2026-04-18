package com.axys.redeflexmobile.shared.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Desenvolvimento on 08/02/2016.
 */
public class Colaborador {
    private boolean ok;
    private int id;
    private String nome;
    private boolean RegistrarGcm;
    private String acao;
    private int enviarBase;
    private Date dataServer;
    private String desbloqueiaVenda;
    private Date dataDesbloqueiaVenda;
    private String bloqueiaHorario;
    private String tipoColaborador;
    private String versaoApp;
    private Double distancia;
    private boolean validaICCID;
    private boolean validaOrdemRota;
    private boolean obrigaAuditagem;
    private int semanaRota;
    private boolean validaDataGps;
    private boolean validaCercaFinalAtend;
    private boolean novoAtend;
    private String idCliente;
    private String pis;
    private String cnpjFilial;
    private boolean cartaoPonto;
    private boolean rfma;
    private boolean verificaClientePendencia;
    private String email;
    private int CicloRoteirizacao;

    public Colaborador() {
    }

    public boolean isValidaCercaFinalAtend() {
        return validaCercaFinalAtend;
    }

    public void setValidaCercaFinalAtend(boolean validaCercaFinalAtend) {
        this.validaCercaFinalAtend = validaCercaFinalAtend;
    }

    public boolean isNovoAtend() {
        return novoAtend;
    }

    public void setNovoAtend(boolean novoAtend) {
        this.novoAtend = novoAtend;
    }

    public boolean isValidaDataGps() {
        return validaDataGps;
    }

    public void setValidaDataGps(boolean validaDataGps) {
        this.validaDataGps = validaDataGps;
    }

    public int getSemanaRota() {
        return semanaRota;
    }

    public void setSemanaRota(int semanaRota) {
        this.semanaRota = semanaRota;
    }

    public boolean isObrigaAuditagem() {
        return obrigaAuditagem;
    }

    public void setObrigaAuditagem(boolean obrigaAuditagem) {
        this.obrigaAuditagem = obrigaAuditagem;
    }

    public boolean isValidaOrdemRota() {
        return validaOrdemRota;
    }

    public void setValidaOrdemRota(boolean validaOrdemRota) {
        this.validaOrdemRota = validaOrdemRota;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public String getVersaoApp() {
        return versaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        this.versaoApp = versaoApp;
    }

    public String getTipoColaborador() {
        return tipoColaborador;
    }

    public void setTipoColaborador(String tipoColaborador) {
        this.tipoColaborador = tipoColaborador;
    }

    public boolean isOk() {
        return ok;
    }

    public String getBloqueiaHorario() {
        return bloqueiaHorario;
    }

    public void setBloqueiaHorario(String bloqueiaHorario) {
        this.bloqueiaHorario = bloqueiaHorario;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isRegistrarGcm() {
        return RegistrarGcm;
    }

    public void setRegistrarGcm(boolean registrarGcm) {
        RegistrarGcm = registrarGcm;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public int getEnviarBase() {
        return enviarBase;
    }

    public void setEnviarBase(int enviarBase) {
        this.enviarBase = enviarBase;
    }

    public Date getDataServer() {
        return dataServer;
    }

    public void setDataServer(Date dataServer) {
        dataServer = dataServer;
    }

    public String getDesbloqueiaVenda() {
        return desbloqueiaVenda;
    }

    public void setDesbloqueiaVenda(String desbloqueiaVenda) {
        this.desbloqueiaVenda = desbloqueiaVenda;
    }

    public Date getDataDesbloqueiaVenda() {
        return dataDesbloqueiaVenda;
    }

    public void setDataDesbloqueiaVenda(Date dataDesbloqueiaVenda) {
        this.dataDesbloqueiaVenda = dataDesbloqueiaVenda;
    }

    public boolean isValidaICCID() {
        return validaICCID;
    }

    public void setValidaICCID(boolean validaICCID) {
        this.validaICCID = validaICCID;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getCnpjFilial() {
        return cnpjFilial;
    }

    public void setCnpjFilial(String cnpjFilial) {
        this.cnpjFilial = cnpjFilial;
    }

    public boolean isCartaoPonto() {
        return cartaoPonto;
    }

    public void setCartaoPonto(boolean cartaoPonto) {
        this.cartaoPonto = cartaoPonto;
    }

    public boolean isRfma() {
        return rfma;
    }

    public void setRfma(boolean rfma) {
        this.rfma = rfma;
    }

    public boolean isVerificaClientePendencia() {
        return verificaClientePendencia;
    }

    public void setVerificaClientePendencia(boolean verificaClientePendencia) {
        this.verificaClientePendencia = verificaClientePendencia;
    }

    public boolean checkIfBloqueiaHorario() {
        return "S".equals(getBloqueiaHorario());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuarioChatbot()
    {
        String[] partes = email.split("@"); // Dividir o e-mail em duas partes usando o símbolo "@"
        String nomeUsuario = partes[0];
        String[] lista = nomeUsuario.split("\\.");

        nomeUsuario = "Usuario Anonimo";
        if (lista.length > 0) {
            nomeUsuario = id + " - " + lista[0].substring(0, 1).toUpperCase() + lista[0].substring(1);

            if (lista.length > 1)
                nomeUsuario = nomeUsuario + " " + lista[1].substring(0, 1).toUpperCase() + lista[1].substring(1);
        }

        return nomeUsuario.trim();
    }

    public int getCicloRoteirizacao() {
        return CicloRoteirizacao;
    }

    public void setCicloRoteirizacao(int cicloRoteirizacao) {
        CicloRoteirizacao = cicloRoteirizacao;
    }
}
