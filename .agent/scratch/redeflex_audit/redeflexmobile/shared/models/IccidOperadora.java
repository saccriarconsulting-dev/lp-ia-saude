package com.axys.redeflexmobile.shared.models;

public class IccidOperadora {
    private int Id;
    private int IccidOperadoraId;
    private int OperadoraId;
    private String InicioCodBarra;
    private int Ativo;

    public IccidOperadora() {
    }

    public IccidOperadora(int id, int iccidOperadoraId, int operadoraId, String inicioCodBarra, int ativo) {
        Id = id;
        IccidOperadoraId = iccidOperadoraId;
        OperadoraId = operadoraId;
        InicioCodBarra = inicioCodBarra;
        Ativo = ativo;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIccidOperadoraId() {
        return IccidOperadoraId;
    }

    public void setIccidOperadoraId(int iccidOperadoraId) {
        IccidOperadoraId = iccidOperadoraId;
    }

    public int getOperadoraId() {
        return OperadoraId;
    }

    public void setOperadoraId(int operadoraId) {
        OperadoraId = operadoraId;
    }

    public String getInicioCodBarra() {
        return InicioCodBarra;
    }

    public void setInicioCodBarra(String inicioCodBarra) {
        InicioCodBarra = inicioCodBarra;
    }

    public int getAtivo() {
        return Ativo;
    }

    public void setAtivo(int ativo) {
        Ativo = ativo;
    }
}