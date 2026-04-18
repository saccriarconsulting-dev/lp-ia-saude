package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.dao.ColaboradorDao;
import com.axys.redeflexmobile.shared.dao.PosDao;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.models.PosRequest;
import com.axys.redeflexmobile.shared.models.PosResponse;
import com.axys.redeflexmobile.shared.services.network.PosNetwork;

import java.util.List;

import io.reactivex.Single;

public class PosManagerImpl implements PosManager {

    private PosDao posDao;
    private ClienteDao clienteDao;
    private PosNetwork posNetwork;
    private ColaboradorDao colaboradorDao;

    public PosManagerImpl(PosDao posDao, ClienteDao clienteDao, PosNetwork posNetwork, ColaboradorDao colaboradorDao) {
        this.posDao = posDao;
        this.clienteDao = clienteDao;
        this.posNetwork = posNetwork;
        this.colaboradorDao = colaboradorDao;
    }

    @Override
    public Single<List<InformacaoGeralPOS>> obterInformacoesPosPorCliente(String clienteId) {
        return posDao.obterInformacoesPosPorCliente(clienteId);
    }

    @Override
    public void removerPos(int id) {
        posDao.removerPos(id);
    }

    @Override
    public Cliente obterClientePorId(String id) {
        return clienteDao.obterPorId(id);
    }

    @Override
    public Single<PosResponse> trocarPos(PosRequest posRequest) {
        return posNetwork.baixaPos(posRequest);
    }

    @Override
    public Single<PosResponse> removerPos(PosRequest posRequest) {
        return posNetwork.baixaPos(posRequest);
    }

    @Override
    public Colaborador obterColaborador() {
        return colaboradorDao.get();
    }
}
