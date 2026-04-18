package com.axys.redeflexmobile.shared.di.module;

import com.axys.redeflexmobile.shared.bd.DBAtualizarCliente;
import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.dao.ConsignadoLimiteProdutoDao;
import com.axys.redeflexmobile.shared.dao.EstoqueDao;
import com.axys.redeflexmobile.shared.dao.PrecoDao;
import com.axys.redeflexmobile.shared.dao.SugestaoVendaDao;
import com.axys.redeflexmobile.shared.dao.VendaDao;
import com.axys.redeflexmobile.shared.dao.VisitaDao;
import com.axys.redeflexmobile.shared.repository.AtualizarClienteRepository;
import com.axys.redeflexmobile.shared.repository.AtualizarClienteRepositoryImpl;
import com.axys.redeflexmobile.shared.repository.PedidoVendaRepository;
import com.axys.redeflexmobile.shared.repository.PedidoVendaRepositoryImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    AtualizarClienteRepository provideAtualizarCliente(DBAtualizarCliente dbAtualizarCliente,
                                                       ClienteDao clienteDao) {
        return new AtualizarClienteRepositoryImpl(dbAtualizarCliente, clienteDao);
    }

    @Provides
    PedidoVendaRepository providePedidoVendaRepository(ClienteDao clienteDao,
                                                       VisitaDao visitaDao,
                                                       VendaDao vendaDao,
                                                       EstoqueDao estoqueDao,
                                                       PrecoDao precoDao,
                                                       SugestaoVendaDao sugestaoVendaDao) {
        return new PedidoVendaRepositoryImpl(
                clienteDao,
                visitaDao,
                vendaDao,
                estoqueDao,
                precoDao,
                sugestaoVendaDao
        );
    }
}
