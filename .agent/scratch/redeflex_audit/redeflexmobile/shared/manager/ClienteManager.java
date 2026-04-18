package com.axys.redeflexmobile.shared.manager;


import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.GenericResponse;
import com.axys.redeflexmobile.shared.models.ReenviaSenhaCliente;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface ClienteManager {

    Single<Cep[]> obterInformacoesCep(String cep);

    Observable<List<Cliente>> obterClientes();

    Observable<List<Cliente>> obterClientesComPendencia();

    Single<Cliente> obterClientePorId(int id);

    Observable<List<Cliente>> obterClientesComPendenciaNaoRespondido();

    Observable<List<Cliente>> obterClienteComPendenciaNaoRespondidoPorClienteId(String idCliente);

    Single<Colaborador> obterColaborador();

    Observable<List<Cliente>> obterClientesMigracao();

    Observable<List<Cliente>> obterCadastroMigracaoSub();

    Single<GenericResponse> obterSenhaSgv(ReenviaSenhaCliente reenviaSenhaCliente);

    Single<Boolean> updateNegotiationMigrateSub(int clintId);

    Observable<List<Cliente>> obterClientesPendentesMigracao();
}
