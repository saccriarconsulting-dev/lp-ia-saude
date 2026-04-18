package com.axys.redeflexmobile.shared.dao;

import android.content.Context;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.Segmento;

public class SegmentoDaoImpl implements SegmentoDao {

    private Context context;

    public SegmentoDaoImpl(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Segmento obterSegmentPorId(String id) {
        return Tabela.TabelaSegmento.obterPorId(context, String.valueOf(id));
    }
}
