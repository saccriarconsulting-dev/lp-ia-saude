package com.axys.redeflexmobile.shared.models.migracao;

import com.google.gson.annotations.SerializedName;

public class CadastroMigracaoSubHorario {
    private int Id;
    private int IdCadastroMigracao;
    private int DiaAtendimentoId;
    private int Aberto;
    @SerializedName("HorarioInicio") private String HoraInicio;
    @SerializedName("HorarioFim") private String HoraFim;

    public CadastroMigracaoSubHorario() {
    }

    public CadastroMigracaoSubHorario(CadastroMigracaoSubHorario cadastroMigracaoSubHorario) {
        this.Id = cadastroMigracaoSubHorario.getId();
        this.IdCadastroMigracao = cadastroMigracaoSubHorario.getIdCadastroMigracao();
        this.DiaAtendimentoId = cadastroMigracaoSubHorario.getDiaAtendimentoId();
        this.Aberto = cadastroMigracaoSubHorario.getAberto();
        this.HoraInicio = cadastroMigracaoSubHorario.getHoraInicio();
        this.HoraFim = cadastroMigracaoSubHorario.getHoraFim();
    }

    public CadastroMigracaoSubHorario(int id, int idCadastroMigracao, int diaAtendimentoId, int aberto, String horaInicio, String horaFim) {
        this.Id = id;
        this.IdCadastroMigracao = idCadastroMigracao;
        this.DiaAtendimentoId = diaAtendimentoId;
        this.Aberto = aberto;
        this.HoraInicio = horaInicio;
        this.HoraFim = horaFim;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdCadastroMigracao() {
        return IdCadastroMigracao;
    }

    public void setIdCadastroMigracao(int idCadastroMigracao) {
        IdCadastroMigracao = idCadastroMigracao;
    }

    public int getDiaAtendimentoId() {
        return DiaAtendimentoId;
    }

    public void setDiaAtendimentoId(int diaAtendimentoId) {
        DiaAtendimentoId = diaAtendimentoId;
    }

    public int getAberto() {
        return Aberto;
    }

    public void setAberto(int aberto) {
        Aberto = aberto;
    }

    public String getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraInicio(String horaInicio) {
        HoraInicio = horaInicio;
    }

    public String getHoraFim() {
        return HoraFim;
    }

    public void setHoraFim(String horaFim) {
        HoraFim = horaFim;
    }
}
