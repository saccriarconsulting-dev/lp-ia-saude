package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.CallReason;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 16/07/20
 */
public interface CallReasonDao {

    Single<Integer> add(@NotNull CallReason callReason);

    Single<List<CallReason>> getAllActive();

    Single<List<CallReason>> getAll(@Nullable String where, @Nullable String[] fields);

    Single<Boolean> deleteAll();
}
