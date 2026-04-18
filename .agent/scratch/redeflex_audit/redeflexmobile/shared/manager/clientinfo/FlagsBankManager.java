package com.axys.redeflexmobile.shared.manager.clientinfo;

import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 01/07/20
 */
public interface FlagsBankManager {

    Single<Boolean> insert(@NotNull FlagsBank flagsBank);

    Single<FlagsBank> getById(int id);

    Single<List<FlagsBank>> getAll();

    Single<Boolean> deleteAll();

    Single<Boolean> deleteById(int id);

}
