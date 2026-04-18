package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;

import java.util.List;

import io.reactivex.Single;

public interface PosDao {

    Single<List<InformacaoGeralPOS>> obterInformacoesPosPorCliente(String clienteId);

    void removerPos(int id);
}
