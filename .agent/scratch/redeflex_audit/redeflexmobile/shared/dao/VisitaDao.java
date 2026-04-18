package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.Visita;

public interface VisitaDao {

    Visita obterVisitaPorId(int id);

    Visita retornaUltimaVisitaByClienteId(String pIdCliente);
}
