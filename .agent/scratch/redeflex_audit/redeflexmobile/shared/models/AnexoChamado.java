package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 14/03/2017.
 */

public class AnexoChamado {
    private Integer Id;
    private Integer AnexoID;
    private Integer InteracaoID;
    private Integer ChamadoID;
    private String Arquivo;
    private String Nome;
    private String Tipo;
    private Date DataCadastro;
    private Integer IdAppMobile;
    private Date DataAppMobile;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getAnexoID() {
        return AnexoID;
    }

    public void setAnexoID(Integer anexoID) {
        AnexoID = anexoID;
    }

    public Integer getInteracaoID() {
        return InteracaoID;
    }

    public void setInteracaoID(Integer interacaoID) {
        InteracaoID = interacaoID;
    }

    public Integer getChamadoID() {
        return ChamadoID;
    }

    public void setChamadoID(Integer chamadoID) {
        ChamadoID = chamadoID;
    }

    public String getArquivo() {
        return Arquivo;
    }

    public void setArquivo(String arquivo) {
        Arquivo = arquivo;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public Date getDataCadastro() {
        return DataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        DataCadastro = dataCadastro;
    }

    public Integer getIdAppMobile() {
        return IdAppMobile;
    }

    public void setIdAppMobile(Integer idAppMobile) {
        IdAppMobile = idAppMobile;
    }

    public Date getDataAppMobile() {
        return DataAppMobile;
    }

    public void setDataAppMobile(Date dataAppMobile) {
        DataAppMobile = dataAppMobile;
    }
}