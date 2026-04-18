package com.axys.redeflexmobile.shared.manager;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.dao.SolicitacaoPDDao;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.services.SolicitacaoPDService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SolicitacaoPDManagerImpl implements SolicitacaoPDManager{

    private SolicitacaoPDService service;
    private SolicitacaoPDDao solicitacaoPDDao;
    public SolicitacaoPDManagerImpl(SolicitacaoPDService service, SolicitacaoPDDao dao) {
        this.service = service;
        this.solicitacaoPDDao = dao;
    }

    @Nullable
    @Override
    public Observable<List<SolicitacaoPrecoDiferenciado>> obterTodos() {
        return solicitacaoPDDao.obterTodos();
    }

    public Single<List<SolicitacaoPrecoDiferenciado>> serverGetAll(String vendedorId, String carga) {
        return service.getAll(vendedorId, carga);
    }
}
