package com.axys.redeflexmobile.shared.dao;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.BDConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.models.ConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.models.SugestaoVenda;

import java.util.List;

public class ConsignadoLimiteProdutoDaoImpl implements ConsignadoLimiteProdutoDao {

    private Context context;

    public ConsignadoLimiteProdutoDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public ConsignadoLimiteProduto obterPorClienteProduto(String idCliente, String idProduto) {
        BDConsignadoLimiteProduto bdConsignadoLimiteProduto = new BDConsignadoLimiteProduto(context);
        return bdConsignadoLimiteProduto.getByIdConsignadoLimiteProduto(idCliente, idProduto);
    }
}
