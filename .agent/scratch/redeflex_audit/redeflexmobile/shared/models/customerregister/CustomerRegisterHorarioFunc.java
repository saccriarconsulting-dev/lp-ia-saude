package com.axys.redeflexmobile.shared.models.customerregister;

import com.google.gson.annotations.SerializedName;

public class CustomerRegisterHorarioFunc {
    @SerializedName("IdAppMobileId") private Integer id;
    @SerializedName("idCadastro") private Integer idCadastro;
    @SerializedName("DiaAtendimento") private Integer diaAtendimentoId;
    @SerializedName("aberto") private Integer aberto;
    @SerializedName("horarioInicio") private String horarioInicio;
    @SerializedName("horarioFim") private String horarioFim;

    public CustomerRegisterHorarioFunc() {

    }

    public CustomerRegisterHorarioFunc(CustomerRegisterHorarioFunc customerRegisterHorarioFunc) {
        this.id = customerRegisterHorarioFunc.getId();
        this.idCadastro = customerRegisterHorarioFunc.getIdCadastro();
        this.diaAtendimentoId = customerRegisterHorarioFunc.getDiaAtendimentoId();
        this.aberto = customerRegisterHorarioFunc.getAberto();
        this.horarioInicio = customerRegisterHorarioFunc.getHorarioInicio();
        this.horarioFim = customerRegisterHorarioFunc.getHorarioFim();
    }

    public CustomerRegisterHorarioFunc(Integer id, Integer idCadastro, Integer diaAtendimentoId, Integer aberto, String horarioInicio, String horarioFim) {
        this.id = id;
        this.idCadastro = idCadastro;
        this.diaAtendimentoId = diaAtendimentoId;
        this.aberto = aberto;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCadastro() {
        return idCadastro;
    }

    public void setIdCadastro(Integer idCadastro) {
        this.idCadastro = idCadastro;
    }

    public Integer getDiaAtendimentoId() {
        return diaAtendimentoId;
    }

    public void setDiaAtendimentoId(Integer diaAtendimentoId) {
        this.diaAtendimentoId = diaAtendimentoId;
    }

    public Integer getAberto() {
        return aberto;
    }

    public void setAberto(Integer aberto) {
        this.aberto = aberto;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(String horarioFim) {
        this.horarioFim = horarioFim;
    }
}
