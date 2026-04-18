package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ComprovanteSkyTa;

import java.util.List;

import io.reactivex.Single;

public interface ComprovanteSkyTaManager {

    void salvar(ComprovanteSkyTa comprovanteSkyTa);

    void sincronizar(ComprovanteSkyTa comprovanteSkyTa);

    Single<List<Cliente>> obterClientes();

    Colaborador obterColaborador();
}
