package com.axys.redeflexmobile.ui.cliente.lista;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class ClienteListaPresenterImpl extends BasePresenterImpl<ClienteListaView>
        implements ClienteListaPresenter {

    private final ClienteManager clienteManager;

    ClienteListaPresenterImpl(ClienteListaView view, SchedulerProvider schedulerProvider,
                              ExceptionUtils exceptionUtils, ClienteManager clienteManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.clienteManager = clienteManager;
    }

    @Override
    public void carregarClientes() {
        // Tipo zero carregar Todos os Clientes ----> Tipo 1 Carregar Somente Clientes com Consignação Ativa
        Disposable disposable = clienteManager.obterClientes()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(clientes -> {
                    getView().validarMensagemListaVazia(clientes.isEmpty());
                    getView().popularAdapter(clientes);
                }, error -> {
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void carregarClientesConsignado() {
        Disposable disposable = clienteManager.obterClientes()
                .distinctUntilChanged()
                .debounce(600, TimeUnit.MILLISECONDS)
                .flatMap(clientes -> {
                    List<Cliente> temp = Stream.of(clientes)
                            .filter(value -> {
                                Boolean ehConsignacaoAtiva = value.isConsignacaoAtiva();
                                return ehConsignacaoAtiva;
                            })
                            .toList();

                    return Observable.just(temp);
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(clientes -> {
                    getView().validarMensagemListaVazia(clientes.isEmpty());
                    getView().popularAdapter(clientes);
                }, error -> {
                });

        compositeDisposable.add(disposable);
    }



    @Override
    public void pesquisarClientes(String texto) {
        Disposable disposable = clienteManager.obterClientes()
                .distinctUntilChanged()
                .debounce(600, TimeUnit.MILLISECONDS)
                .flatMap(clientes -> {
                    List<Cliente> temp = Stream.of(clientes)
                            .filter(value -> {
                                String pesquisa = Util_IO.trataString(texto).toLowerCase();
                                String nome = value.getNomeFantasia() != null
                                        ? Util_IO.trataString(value.getNomeFantasia().toLowerCase())
                                        : "";
                                String codigo = value.getCodigoSGV() != null
                                        ? Util_IO.trataString(value.getCodigoSGV().toLowerCase())
                                        : "";
                                return nome.contains(pesquisa)
                                        || codigo.contains(pesquisa);
                            })
                            .toList();

                    return Observable.just(temp);
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(clientes -> {
                    getView().validarMensagemListaVazia(clientes.isEmpty());
                    getView().popularAdapter(clientes);
                }, error -> {
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void abrirInformacoes(Cliente cliente) {
        Disposable disposable = clienteManager.obterColaborador()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(colaborador -> {
                    String latitude = String.valueOf(cliente.getLatitude());
                    String longitude = String.valueOf(cliente.getLongitude());
                    getView().abrirInformacoes(cliente.getId(), latitude, longitude);
                }, error -> {
                });

        compositeDisposable.add(disposable);
    }
}
