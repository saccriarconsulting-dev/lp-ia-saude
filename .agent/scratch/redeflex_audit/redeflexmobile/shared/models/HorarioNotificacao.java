package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Denis Gasparoto on 07/05/2019.
 */

public class HorarioNotificacao {

    private static final String PUSH_NUMBER_ONE = "1";
    private static final String PUSH_NUMBER_TWO = "2";
    private static final String PUSH_NUMBER_THREE = "3";
    private static final String PUSH_NUMBER_FOUR = "4";

    @SerializedName("Id") private int idServer;
    @SerializedName("DiaSemana") private int diaSemana;
    @SerializedName("Hora1") private String horaUm;
    @SerializedName("Hora2") private String horaDois;
    @SerializedName("Hora3") private String horaTres;
    @SerializedName("Hora4") private String horaQuatro;
    @SerializedName("Push") private String push;
    @SerializedName("TempoLeitura") private int tempoLeitura;

    public HorarioNotificacao() {

    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraUm() {
        return horaUm;
    }

    public void setHoraUm(String horaUm) {
        this.horaUm = horaUm;
    }

    public String getHoraDois() {
        return horaDois;
    }

    public void setHoraDois(String horaDois) {
        this.horaDois = horaDois;
    }

    public String getHoraTres() {
        return horaTres;
    }

    public void setHoraTres(String horaTres) {
        this.horaTres = horaTres;
    }

    public String getHoraQuatro() {
        return horaQuatro;
    }

    public void setHoraQuatro(String horaQuatro) {
        this.horaQuatro = horaQuatro;
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }

    public int getTempoLeitura() {
        return tempoLeitura;
    }

    public void setTempoLeitura(int tempoLeitura) {
        this.tempoLeitura = tempoLeitura;
    }

    public String getHourFromType(String pushType) {
        if (PUSH_NUMBER_ONE.equals(pushType)) {
            return horaUm;
        } else if (PUSH_NUMBER_TWO.equals(pushType)) {
            return horaDois;
        } else if (PUSH_NUMBER_THREE.equals(pushType)) {
            return horaTres;
        } else if (PUSH_NUMBER_FOUR.equals(pushType)) {
            return horaQuatro;
        }
        return null;
    }
}
