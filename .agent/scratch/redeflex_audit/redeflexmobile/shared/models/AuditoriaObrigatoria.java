package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 28/10/2016.
 */

public class AuditoriaObrigatoria {
    private String id;
    private String diaSemana;
    private String obrigatorio;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(String obrigatorio) {
        this.obrigatorio = obrigatorio;
    }
}