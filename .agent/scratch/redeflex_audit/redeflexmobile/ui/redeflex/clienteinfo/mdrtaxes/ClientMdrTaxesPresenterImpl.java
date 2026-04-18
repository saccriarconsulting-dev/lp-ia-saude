package com.axys.redeflexmobile.ui.redeflex.clienteinfo.mdrtaxes;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.clientinfo.ClientTaxMdrManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.FlagsBankManager;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientTaxMdr;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_LIST;

/**
 * @author lucasmarciano on 30/06/20
 */
public class ClientMdrTaxesPresenterImpl extends BasePresenterImpl<ClientMdrTaxesView>
        implements ClientMdrTaxesPresenter {

    private final ClientTaxMdrManager clientTaxMdrManager;
    private final FlagsBankManager flagsBankManager;
    private List<ClientTaxMdr> taxes = new ArrayList<>();
    private List<FlagsBank> flags = new ArrayList<>();

    public ClientMdrTaxesPresenterImpl(ClientMdrTaxesView view,
                                       SchedulerProvider schedulerProvider,
                                       ExceptionUtils exceptionUtils,
                                       ClientTaxMdrManager clientTaxMdrManager,
                                       FlagsBankManager flagsBankManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.clientTaxMdrManager = clientTaxMdrManager;
        this.flagsBankManager = flagsBankManager;
    }

    @Override
    public void loadFlags() {
        compositeDisposable.add(flagsBankManager.getAll()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(response -> {
                            flags = response;
                            getView().fillAdapterFlags(response);
                        },
                        Timber::e
                )
        );
    }

    @Override
    public void loadData(String clientId) {
        compositeDisposable.add(clientTaxMdrManager.getByClientId(Integer.parseInt(clientId))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showMainLoading())
                .doFinally(getView()::hideMainLoading)
                .subscribe(response -> {
                    taxes = response;
                    getView().fillViewData(
                            taxes.get(EMPTY_LIST),
                            loadFlag(taxes.get(EMPTY_LIST).getFlagId())
                    );
                }, e -> {
                    getView().fillViewData(
                            null,
                            loadFlag(flags.get(EMPTY_LIST).getId())
                    );
                    Timber.e(e);
                }));
    }

    @Override
    public void loadTaxesByFlagId(int flagId) {
        ClientTaxMdr clientTaxMdr = Stream.ofNullable(taxes)
                .filter(tax -> tax.getFlagId() == flagId)
                .findFirst()
                .orElse(null);

        getView().fillViewData(clientTaxMdr, loadFlag(flagId));
    }

    private FlagsBank loadFlag(int flagId) {
        return Stream.ofNullable(flags)
                .filter(tax -> tax.getId() == flagId)
                .findFirst()
                .orElse(null);
    }
}
