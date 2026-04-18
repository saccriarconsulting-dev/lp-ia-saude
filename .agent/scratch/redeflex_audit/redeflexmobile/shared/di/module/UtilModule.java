package com.axys.redeflexmobile.shared.di.module;

import com.axys.redeflexmobile.shared.dao.PrecoDao;
import com.axys.redeflexmobile.shared.dao.VendaDao;
import com.axys.redeflexmobile.shared.util.PrecoDiferenciadoValidador;
import com.axys.redeflexmobile.shared.util.PrecoDiferenciadoValidadorImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilModule {

    @Provides
    public PrecoDiferenciadoValidador providePrecoDiferenciadoValidador(PrecoDao precoDao,
                                                                        VendaDao vendaDao) {
        return new PrecoDiferenciadoValidadorImpl(precoDao, vendaDao);
    }
}
