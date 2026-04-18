package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.dao.CallReasonDao;
import com.axys.redeflexmobile.shared.models.CallReason;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 16/07/20
 */
public class CallReasonManagerImpl implements CallReasonManager {

    private final CallReasonDao dao;

    public CallReasonManagerImpl(CallReasonDao dao) {
        this.dao = dao;
    }

    @Override
    public Single<Integer> add(@NotNull CallReason callReason) {
        return dao.add(callReason);
    }

    @Override
    public Single<List<CallReason>> getAllActive() {
        return dao.getAllActive();
    }

    @Override
    public Single<List<CallReason>> getAll(@Nullable String where, @Nullable String[] fields) {
        return dao.getAll(where, fields);
    }

    @Override
    public Single<Boolean> deleteAll() {
        return dao.deleteAll();
    }
}
