package com.axys.redeflexmobile.shared.dao;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.Segmento;

public interface SegmentoDao {

    @Nullable
    Segmento obterSegmentPorId(String id);
}
