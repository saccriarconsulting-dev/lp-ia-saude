package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.models.PosRequest;
import com.axys.redeflexmobile.shared.models.PosResponse;

import java.util.List;

import io.reactivex.Single;

public interface PosManager {

    Single<List<InformacaoGeralPOS>> obterInformacoesPosPorCliente(String clienteId);

    void removerPos(int id);

    Cliente obterClientePorId(String id);

    Single<PosResponse> trocarPos(PosRequest posRequest);

    Single<PosResponse> removerPos(PosRequest posRequest);

    Colaborador obterColaborador();
}
