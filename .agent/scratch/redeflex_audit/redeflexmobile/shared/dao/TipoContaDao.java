package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.TipoConta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.Single;

public interface TipoContaDao {

    Single<Integer> add(@NotNull TipoConta tipoConta);

    Single<List<TipoConta>> getAllActive();

    Single<List<TipoConta>> getAll(@Nullable String where, @Nullable String[] fields);

    Single<Boolean> deleteAll();
}
