package com.axys.redeflexmobile.shared.dao;

import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.bd.DBClienteCadastroEndereco;
import com.axys.redeflexmobile.shared.bd.DBSugestaoVenda;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.SugestaoVenda;

import java.util.ArrayList;

import timber.log.Timber;

public class SugestaoVendaDaoImpl implements SugestaoVendaDao {

    private Context context;

    public SugestaoVendaDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public void salvar(SugestaoVenda sugestaoVenda) {
        Tabela.TabelaSugestaoVenda.salvar(context, sugestaoVenda);
    }

    @Override
    public SugestaoVenda obterPorCliente(String idCliente,
                                         String idOperador,
                                         String grupo) {
        return Tabela.TabelaSugestaoVenda.obterPorCliente(context, idCliente, idOperador, grupo);
    }

    @Override
    public SugestaoVenda obterPorId(String id) {
        return Tabela.TabelaSugestaoVenda.obterPorId(context, id);
    }
}
