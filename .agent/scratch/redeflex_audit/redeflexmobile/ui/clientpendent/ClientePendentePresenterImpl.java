package com.axys.redeflexmobile.ui.clientpendent;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.manager.PendenciaClienteManager;
import com.axys.redeflexmobile.shared.manager.PendenciaManager;
import com.axys.redeflexmobile.shared.manager.PendenciaMotivoManager;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.List;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public class ClientePendentePresenterImpl extends BasePresenterImpl<ClientePendenteView>
        implements ClientePendentePresenter {

    private final ClienteManager clienteManager;
    private final PendenciaManager pendenciaManager;
    private final PendenciaClienteManager pendenciaClienteManager;
    private final PendenciaMotivoManager pendenciaMotivoManager;

    public ClientePendentePresenterImpl(ClientePendenteView view,
                                        SchedulerProvider schedulerProvider,
                                        ExceptionUtils exceptionUtils,
                                        ClienteManager clienteManager,
                                        PendenciaManager pendenciaManager,
                                        PendenciaClienteManager pendenciaClienteManager,
                                        PendenciaMotivoManager pendenciaMotivoManager) {
        super(view, schedulerProvider, exceptionUtils);

        this.clienteManager = clienteManager;
        this.pendenciaManager = pendenciaManager;
        this.pendenciaClienteManager = pendenciaClienteManager;
        this.pendenciaMotivoManager = pendenciaMotivoManager;
    }

    @Override
    public void carregarPendenciasCliente() {
        Disposable disposable = pendenciaClienteManager.obterTodos()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(itens -> getView().carregarPendenciasClientes(itens), Timber::e);

        compositeDisposable.add(disposable);
    }

    @Override
    public void carregarClientesPendentes() {
        Disposable disposable = clienteManager.obterClientesComPendencia()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showMainLoading())
                .subscribe(this::carregarPendencia, e -> {
                    Timber.e(e);
                    getView().hideMainLoading();
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void carregarClientesPendentesNaoRespondido() {
        Disposable disposable = clienteManager.obterClientesComPendenciaNaoRespondido()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showMainLoading())
                .subscribe(this::carregarPendencia, e -> {
                    Timber.e(e);
                    getView().hideMainLoading();
                });

        compositeDisposable.add(disposable);
    }

    public void carregarClientePendenteNaoRespondidoPorClienteId(String clienteId) {
        Disposable disposable = clienteManager.obterClienteComPendenciaNaoRespondidoPorClienteId(clienteId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showMainLoading())
                .subscribe(this::carregarPendencia, e -> {
                    Timber.e(e);
                    getView().hideMainLoading();
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void carregarPendencia(List<Cliente> clientes) {
        Disposable disposable = pendenciaManager.obterTodosComCliente(clientes)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::carregarPendenciaMotivos, e -> {
                    Timber.e(e);
                    getView().hideMainLoading();
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void carregarPendenciaMotivos(List<Cliente> clientes) {
        Disposable disposable = pendenciaMotivoManager.obterTodosComCliente(clientes)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doFinally(() -> getView().hideMainLoading())
                .subscribe(itens -> {
                    getView().popularAdapter(itens);
                    getView().showModalTodosClientesRespondidos(itens.size() == 0);
                }, Timber::e);

        compositeDisposable.add(disposable);
    }

    @Override
    public void abrirClienteActivity(Cliente cliente) {
        getView().abrirClienteActivity(cliente.getId(), String.valueOf(cliente.getLatitude()),
                String.valueOf(cliente.getLongitude()));
    }

    @Override
    public void showModalMotivos(List<PendenciaMotivo> motivos, PendenciaCliente pendenciaCliente) {
        getView().showModalMotivos(motivos, pendenciaCliente);
    }

    @Override
    public void salvarResposta(PendenciaCliente pendenciaCliente) {
        Disposable disposable = pendenciaClienteManager.updateMotivo(pendenciaCliente)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showMainLoading())
                .doFinally(() -> getView().hideMainLoading())
                .subscribe(getView()::returnUpdatedNumberRows,
                        Timber::e);
        compositeDisposable.add(disposable);
    }
}
