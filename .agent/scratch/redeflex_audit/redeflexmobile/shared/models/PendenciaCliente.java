package com.axys.redeflexmobile.shared.models;


/**
 * @author Lucas Marciano on 27/02/2020
 */
public class PendenciaCliente {
    private String pendenciaClienteId;
    private int clienteId;
    private int pendenciaId;
    private int pendenciaMotivoId;
    private String observacao;
    private Double latitude;
    private Double longitude;
    private Double precisao;
    private String dataVisualizacao;
    private String dataResposta;
    private boolean ativo;
    private int ordem;
    private int idVendedor;
    private boolean exibeExplicacao;
    private String explicacao;

    public PendenciaCliente(String pendenciaClienteId,
                            int clienteId,
                            int pendenciaId,
                            int pendenciaMotivoId,
                            String observacao,
                            Double latitude,
                            Double longitude,
                            Double precisao,
                            String dataVisualizacao,
                            String dataResposta,
                            int idVendedor,
                            boolean exibeExplicacao,
                            String explicacao,
                            int ordem
    ) {
        this.pendenciaClienteId = pendenciaClienteId;
        this.clienteId = clienteId;
        this.pendenciaId = pendenciaId;
        this.pendenciaMotivoId = pendenciaMotivoId;
        this.observacao = observacao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.precisao = precisao;
        this.dataVisualizacao = dataVisualizacao;
        this.dataResposta = dataResposta;
        this.idVendedor = idVendedor;
        this.exibeExplicacao = exibeExplicacao;
        this.explicacao = explicacao;
        this.ordem = ordem;
    }

    public PendenciaCliente() {
    }

    public String getId() {
        return pendenciaClienteId;
    }

    public void setId(String pendenciaClienteId) {
        this.pendenciaClienteId = pendenciaClienteId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getPendenciaId() {
        return pendenciaId;
    }

    public void setPendenciaId(int pendenciaId) {
        this.pendenciaId = pendenciaId;
    }

    public int getPendenciaMotivoId() {
        return pendenciaMotivoId;
    }

    public void setPendenciaMotivoId(int pendenciaMotivoId) {
        this.pendenciaMotivoId = pendenciaMotivoId;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String motivoPendencia) {
        this.observacao = motivoPendencia;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getPrecision() {
        return precisao;
    }

    public void setPrecision(Double precisao) {
        this.precisao = precisao;
    }

    public String getDataVisualizacao() {
        return dataVisualizacao;
    }

    public void setDataVisualizacao(String dataVisualizacao) {
        this.dataVisualizacao = dataVisualizacao;
    }

    public String getDataResposta() {
        return dataResposta;
    }

    public void setDataResposta(String dataResposta) {
        this.dataResposta = dataResposta;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public boolean isExibeExplicacao() {
        return exibeExplicacao;
    }

    public void setExibeExplicacao(boolean exibeExplicacao) {
        this.exibeExplicacao = exibeExplicacao;
    }

    public String getExplicacao() {
        return explicacao;
    }

    public void setExplicacao(String explicacao) {
        this.explicacao = explicacao;
    }

    public boolean isEmptyExplication() {
        return isExibeExplicacao() && getExplicacao() != null && getExplicacao().length() > 0;
    }
}
