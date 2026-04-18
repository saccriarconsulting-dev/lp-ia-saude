package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 07/03/2017.
 */

public class Interacoes {
    private int Id;
    private int InteracaoID;
    private int ChamadoID;
    private String Descricao;
    private Date DataCadastro;
    private Integer IdUsuario;
    private String Usuario;
    private Integer IdAppMobile;
    private Date DataAppMobile;
    private Integer idCallReason;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getInteracaoID() {
        return InteracaoID;
    }

    public void setInteracaoID(int interacaoID) {
        InteracaoID = interacaoID;
    }

    public int getChamadoID() {
        return ChamadoID;
    }

    public void setChamadoID(int chamadoID) {
        ChamadoID = chamadoID;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public Date getDataCadastro() {
        return DataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        DataCadastro = dataCadastro;
    }

    public Integer getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
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

    public Integer getIdCallReason() {
        return idCallReason;
    }

    public void setIdCallReason(Integer idCallReason) {
        this.idCallReason = idCallReason;
    }
}
