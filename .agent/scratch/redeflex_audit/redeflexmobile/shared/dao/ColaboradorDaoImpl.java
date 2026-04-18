package com.axys.redeflexmobile.shared.dao;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ColaboradorDaoImpl implements ColaboradorDao {

    private final Context context;

    public ColaboradorDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public Colaborador get() {
        return Tabela.TabelaColaborador.get(context);
    }

    @Override
    public Single<Colaborador> getCurrent() {
        return new DBColaborador(context).getCurrent();
    }
}
