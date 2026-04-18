package com.axys.redeflexmobile.shared.models;

import java.util.ArrayList;

/**
 * Created by joao.viana on 15/03/2017.
 */

public class InteracaoAnexos extends Interacoes {
    private ArrayList<AnexoChamado> listaAnexos;

    public ArrayList<AnexoChamado> getListaAnexos() {
        return listaAnexos;
    }

    public void setListaAnexos(ArrayList<AnexoChamado> listaAnexos) {
        this.listaAnexos = listaAnexos;
    }
}