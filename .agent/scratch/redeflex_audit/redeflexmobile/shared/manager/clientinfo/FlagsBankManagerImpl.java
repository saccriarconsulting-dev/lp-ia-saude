package com.axys.redeflexmobile.shared.manager.clientinfo;

import com.axys.redeflexmobile.shared.dao.clientinfo.FlagsBankDao;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 01/07/20
 */
public class FlagsBankManagerImpl implements FlagsBankManager {

    private final FlagsBankDao dao;

    public FlagsBankManagerImpl(FlagsBankDao dao) {
        this.dao = dao;
    }

    @Override
    public Single<Boolean> insert(@NotNull FlagsBank flagsBank) {
        return dao.insert(flagsBank);
    }

    @Override
    public Single<FlagsBank> getById(int id) {
        return dao.getById(id);
    }

    @Override
    public Single<List<FlagsBank>> getAll() {
        return dao.getAll();
    }

    @Override
    public Single<Boolean> deleteAll() {
        return dao.deleteAll();
    }

    @Override
    public Single<Boolean> deleteById(int id) {
        return dao.deleteById(id);
    }
}
