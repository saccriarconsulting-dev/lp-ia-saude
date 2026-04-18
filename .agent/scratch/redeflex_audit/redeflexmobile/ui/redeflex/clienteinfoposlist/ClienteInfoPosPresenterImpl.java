package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist;

import android.location.Location;
import androidx.annotation.Nullable;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.PosManager;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.models.PosRequest;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.shared.util.exception.MessageException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ClienteInfoPosPresenterImpl
        extends BasePresenterImpl<ClienteInfoPosListView>
        implements ClienteInfoPosPresenter {


    private final PosManager posManager;

    @Nullable private String idCliente;
    private List<InformacaoGeralPOS> pos;
    private Cliente cliente;

    ClienteInfoPosPresenterImpl(ClienteInfoPosListView view,
                                SchedulerProvider schedulerProvider,
                                ExceptionUtils exceptionUtils,
                                PosManager posManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.posManager = posManager;
    }

    @Override
    public void setClienteId(String idCliente) {
        this.idCliente = idCliente;
    }


    @Override
    public void carregarDados() {
        if (idCliente == null) {
            getView().apresentarClienteNaoEncontrado();

            return;
        }

        cliente = posManager.obterClientePorId(idCliente);
        if (cliente == null) {
            getView().apresentarClienteNaoEncontrado();

            return;
        }

        Disposable disposable = posManager.obterInformacoesPosPorCliente(idCliente)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(posLista -> {
                    pos = posLista;
                    final List<ClienteInfoPosListAdapter.ClienteInfoPosListItem> lista = new ArrayList<>();
                    lista.add(new ClienteInfoPosListAdapter.ClienteInfoPosListItem(
                            getString(R.string.cliente_info_pos_dias_list_item_descricao_label),
                            getString(R.string.cliente_info_pos_dias_list_item_movimentacao_label)
                    ));
                    lista.addAll(Stream.ofNullable(pos)
                            .map(informacaoGeralPOS -> new ClienteInfoPosListAdapter.ClienteInfoPosListItem(
                                    informacaoGeralPOS.getId(),
                                    informacaoGeralPOS.getDescricao(),
                                    informacaoGeralPOS.getModelo(),
                                    Utilidades.daybetween(informacaoGeralPOS.getDataUltimaTransacao(), new Date())
                            ))
                            .toList());

                    getView().popularAdapter(lista);
                }, error -> getView().fechar());
        compositeDisposable.add(disposable);
    }

    @Override
    public InformacaoGeralPOS pegarPos(ClienteInfoPosListAdapter.ClienteInfoPosListItem item) {
        return Stream.of(pos)
                .filter(v -> v.getId() == item.posId)
                .single();
    }

    @Override
    public void removerPos(int id) {
        posManager.removerPos(id);
    }

    @Override
    public void trocarPos(InformacaoGeralPOS pos) {
        Colaborador colaborador = posManager.obterColaborador();
        PosRequest posRequest = new PosRequest(colaborador.getId(), pos.getIdCliente(), pos.getNumeroSerie());
        posRequest.troca();

        Disposable disposable = posManager.trocarPos(posRequest)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(value -> getView().showLoadingDialog())
                .doAfterTerminate(() -> getView().hideLoadingDialog())
                .subscribe(value -> {
                    if (value.isSucesso()) {
                        posManager.removerPos(pos.getId());
                        getView().trocaEfetuada();
                        return;
                    }

                    getView().apresentarErroTroca(value.getTituloErro(), value.getDescricaoErro());
                }, error -> {
                    if (error instanceof IOException) {
                        getView().apresentarErroInternet(false);
                        return;
                    }

                    MessageException message = exceptionUtils.getExceptionsMessage(error);
                    getView().apresentarErroTroca(message.getTitle(), message.getMessage());
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void removerPos(InformacaoGeralPOS pos) {
        Colaborador colaborador = posManager.obterColaborador();
        PosRequest posRequest = new PosRequest(colaborador.getId(), pos.getIdCliente(), pos.getNumeroSerie());
        posRequest.remocao();

        Disposable disposable = posManager.trocarPos(posRequest)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(value -> getView().showLoadingDialog())
                .doAfterTerminate(() -> getView().hideLoadingDialog())
                .subscribe(value -> {
                    if (value.isSucesso()) {
                        posManager.removerPos(pos.getId());
                        getView().remocaoEfetuada();
                        return;
                    }

                    getView().apresentarErroRemocao(value.getTituloErro(), value.getDescricaoErro());
                }, error -> {
                    if (error instanceof IOException) {
                        getView().apresentarErroInternet(true);
                        return;
                    }

                    MessageException message = exceptionUtils.getExceptionsMessage(error);
                    getView().apresentarErroRemocao(message.getTitle(), message.getMessage());
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public boolean estaProximo() {
        Location location = new Location("cliente");
        location.setLatitude(cliente.getLatitude());
        location.setLongitude(cliente.getLongitude());

        Colaborador colaborador = posManager.obterColaborador();
        return getView().estaProximo(
                colaborador.getDistancia(),
                cliente.getCerca(),
                location
        );

    }

}
