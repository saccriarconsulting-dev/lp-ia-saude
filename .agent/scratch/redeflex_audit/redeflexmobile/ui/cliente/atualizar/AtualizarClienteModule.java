package com.axys.redeflexmobile.ui.cliente.atualizar;

import com.axys.redeflexmobile.shared.bd.DBLocalizacaoCliente;
import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.dao.SegmentoDao;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.repository.AtualizarClienteRepository;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class AtualizarClienteModule {

    @Provides
    AtualizarClientePresenter providePresenter(AtualizarClienteActivity activity,
                                               SchedulerProvider schedulerProvider,
                                               ExceptionUtils exceptionUtils,
                                               AtualizarClienteRepository repository,
                                               ClienteDao clienteDao,
                                               ClienteManager clienteManager,
                                               SegmentoDao segmentoDao,
                                               DBLocalizacaoCliente localizacaoClienteDAO) {
        return new AtualizarClientePresenterImpl(
                activity,
                schedulerProvider,
                exceptionUtils,
                repository,
                clienteDao,
                clienteManager,
                segmentoDao,
                localizacaoClienteDAO
        );
    }
}
