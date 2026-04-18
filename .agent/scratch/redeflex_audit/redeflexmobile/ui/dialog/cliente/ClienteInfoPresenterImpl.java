package com.axys.redeflexmobile.ui.dialog.cliente;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.ReenviaSenhaCliente;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.shared.util.exception.MessageException;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ClienteInfoPresenterImpl extends BasePresenterImpl<ClienteInfoView>
        implements ClienteInfoPresenter {

    private final ClienteManager clienteManager;
    private Cliente cliente;
    private ReenviaSenhaCliente senhaCliente;

    public ClienteInfoPresenterImpl(ClienteInfoView view,
                                    SchedulerProvider schedulerProvider,
                                    ExceptionUtils exceptionUtils,
                                    ClienteManager clienteManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.clienteManager = clienteManager;
    }

    @Override
    public void iniciar(final String clienteJson, final String senhaJson) {
        try {
            cliente = Utilidades.getGsonInstance()
                    .fromJson(clienteJson, Cliente.class);
            senhaCliente = Utilidades.getGsonInstance()
                    .fromJson(senhaJson, ReenviaSenhaCliente.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return;
        }

        if (StringUtils.isEmpty(cliente.getDddTelefone()) &&
                StringUtils.isEmpty(cliente.getTelefone()) &&
                StringUtils.isEmpty(cliente.getDddTelefone2()) &&
                StringUtils.isEmpty(cliente.getTelefone2()) &&
                StringUtils.isEmpty(cliente.getDddCelular()) &&
                StringUtils.isEmpty(cliente.getCelular()) &&
                StringUtils.isEmpty(cliente.getDddCelular2()) &&
                StringUtils.isEmpty(cliente.getCelular2()) &&
                StringUtils.isEmpty(cliente.getEmailCliente())) {
            getView().apresentarMensagemSemDados();

            return;
        }

        getView().apresentarTelefonePrincipal(formatarTelefone(
                cliente.getDddTelefone(),
                cliente.getTelefone()
        ));

        getView().apresentarTelefone(formatarTelefone(
                cliente.getDddTelefone2(),
                cliente.getTelefone2())
        );

        getView().apresentarCelular(formatarTelefone(
                cliente.getDddCelular(),
                cliente.getCelular()
        ));

        getView().apresentarCelularOpcional(formatarTelefone(
                cliente.getDddCelular2(),
                cliente.getCelular2()
        ));

        getView().apresentarEmail(cliente.getEmailCliente());
    }

    @Override
    public void solicitarSenhaSgv(final boolean telefonePrincipalSelecionado,
                                  final boolean telefoneSelecionado,
                                  final boolean celularSelecionado,
                                  final boolean celularOpcionalSelecionado,
                                  final boolean emailSelecionado) {
        List<String> contatos = criarListaContatos(
                telefonePrincipalSelecionado,
                telefoneSelecionado,
                celularSelecionado,
                celularOpcionalSelecionado,
                emailSelecionado
        );

        senhaCliente.setContatos(contatos);

        Disposable disposable = clienteManager.obterSenhaSgv(senhaCliente)
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe(value -> getView().showLoading())
                .observeOn(schedulerProvider.ui())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe((genericResponse, throwable) -> {
                    if (throwable != null) {
                        MessageException message = exceptionUtils.getExceptionsMessage(throwable);
                        String texto = message.getMessage() == null
                                ? getView().getStringByResId(message.getMessageResId())
                                : message.getMessage();
                        getView().mostrarErro(texto);
                        return;
                    }

                    if (!genericResponse.isSuccess()) {
                        getView().mostrarErro(genericResponse.getTitle(), genericResponse.getDescription());
                        return;
                    }

                    getView().mostrarSucesso(genericResponse.getTitle(), genericResponse.getDescription());
                });

        compositeDisposable.add(disposable);
    }

    @NotNull
    private List<String> criarListaContatos(final boolean telefonePrincipalSelecionado,
                                            final boolean telefoneSelecionado,
                                            final boolean celularSelecionado,
                                            final boolean celularOpcionalSelecionado,
                                            final boolean emailSelecionado) {
        List<String> contatos = new ArrayList<>();
        if (telefonePrincipalSelecionado) {
            contatos.add(formatarTelefone(cliente.getDddTelefone(), cliente.getTelefone()));
        }
        if (telefoneSelecionado) {
            contatos.add(formatarTelefone(cliente.getDddTelefone2(), cliente.getTelefone2()));
        }
        if (celularSelecionado) {
            contatos.add(formatarTelefone(cliente.getDddCelular(), cliente.getCelular()));
        }
        if (celularOpcionalSelecionado) {
            contatos.add(formatarTelefone(cliente.getDddCelular2(), cliente.getCelular2()));
        }
        if (emailSelecionado) {
            contatos.add(cliente.getEmailCliente());
        }
        return contatos;
    }

    private String formatarTelefone(String ddd, String telefone) {
        if (StringUtils.isEmpty(ddd) || StringUtils.isEmpty(telefone)) {
            return null;
        }
        return "(" + ddd + ") " + telefone;
    }
}
