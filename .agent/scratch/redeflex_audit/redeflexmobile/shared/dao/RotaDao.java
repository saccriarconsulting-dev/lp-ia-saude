package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.Rota;

import java.util.ArrayList;

public interface RotaDao {

    String getProximaVisita(String pIdCliente);

    ArrayList<Rota> getRotasByIdCliente(String pIdCliente);
}
