package com.axys.redeflexmobile.ui.clientemigracao.register.proposal;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.manager.PeriodNegotiationManager;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FORESEEN_REVENUE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCCARTAOPRESENTE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCECOMMERCE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCENTREGAIMEDIATA;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCENTREGAPOSTERIOR;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCFATURAMENTO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_DEBIT_AUTOMATIC;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_NEGOTIATION;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;

/**
 * @author lucasmarciano on 06/04/20
 */
public class RegisterMigrationProposalPresenterImpl
        extends BasePresenterImpl<RegisterMigrationProposalView>
        implements RegisterMigrationProposalPresenter {
    private final PeriodNegotiationManager periodNegotiationManager;
    private RegisterMigrationSub registerMigrationSub;

    public RegisterMigrationProposalPresenterImpl(RegisterMigrationProposalFragment view,
                                                  SchedulerProvider schedulerProvider,
                                                  ExceptionUtils exceptionUtils,
                                                  PeriodNegotiationManager periodNegotiationManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.periodNegotiationManager = periodNegotiationManager;
    }

    @Override
    public void initializeData(RegisterMigrationSub registerMigrationSub) {
        this.registerMigrationSub = registerMigrationSub;
        EnumRegisterAnticipation anticipation = configureEnumAnticipation(registerMigrationSub.isAntecipacao());
        if (registerMigrationSub.getIdPrazoNegociacao() != null) {
            compositeDisposable.add(periodNegotiationManager.getById(registerMigrationSub.getIdPrazoNegociacao())
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(item -> getView().initializeInterface(registerMigrationSub, item, anticipation
                    ), Timber::e));
        } else {
            getView().initializeInterface(registerMigrationSub, null, anticipation);
        }
    }

    @Override
    public void loadNegotiationPeriod() {
        compositeDisposable.add(periodNegotiationManager.getAll()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(list -> getView().fillSpinnerNegotiationPeriod(
                        new ArrayList<>(list)), Timber::e));
    }

    @Override
    public void loadDataAutomaticAnticipation() {
        getView().fillSpinnerAutomaticAnticipation(new ArrayList<>(EnumRegisterAnticipation.getEnumList()));
    }

    @Override
    public void doSave(RegisterMigrationSub request) {
        List<EnumRegisterFields> errors = new ArrayList<>(validateFields(request));
        if (!errors.isEmpty()) {
            getView().setErrors(errors);
        } else {
            finalizeFlow(request, false);
        }
    }

    @Override
    public void finalizeFlow(RegisterMigrationSub request, boolean isBack) {
        try {
            registerMigrationSub.setIdPrazoNegociacao(request.getIdPrazoNegociacao());
            registerMigrationSub.setFaturamentoMedioPrevisto(request.getFaturamentoMedioPrevisto());
            registerMigrationSub.setAntecipacao(request.isAntecipacao());
            registerMigrationSub.setFaturamentoBruto(request.getFaturamentoBruto());
            registerMigrationSub.setPercFaturamento(request.getPercFaturamento());
            registerMigrationSub.setPercVendaCartao(request.getPercVendaCartao());
            registerMigrationSub.setPercFaturamentoEcommerce(request.getPercFaturamentoEcommerce());
            registerMigrationSub.setEntregaPosCompra(request.getEntregaPosCompra());
            registerMigrationSub.setPrazoEntrega(request.getPrazoEntrega());
            registerMigrationSub.setPercEntregaImediata(request.getPercEntregaImediata());
            registerMigrationSub.setPercEntregaPosterior(request.getPercEntregaPosterior());

        } catch (Exception ex) {
        }

        if (isBack) {
            getView().onValidationSuccessBack();
        } else {
            getView().onValidationSuccess(registerMigrationSub);
        }
    }

    private EnumRegisterAnticipation configureEnumAnticipation(Boolean isAnticipation) {
        if (isAnticipation != null)
            return EnumRegisterAnticipation.getEnumByValue(isAnticipation ? 1 : 0);
        else
            return EnumRegisterAnticipation.getEnumByValue(1);
    }

    private List<EnumRegisterFields> validateFields(RegisterMigrationSub request) {
        List<EnumRegisterFields> errors = new ArrayList<>();

        if (request.getFaturamentoMedioPrevisto() == 0.0)
            errors.add(ET_FORESEEN_REVENUE);

        if (!this.registerMigrationSub.getTipoMigracao().equals("ADQ")) {
            if (request.getIdPrazoNegociacao() == null) {
                errors.add(SPN_NEGOTIATION);
            }
            if (request.isAntecipacao() == null) {
                errors.add(SPN_DEBIT_AUTOMATIC);
            }
        }

        if (request.getPercFaturamento() != 0) {
            if (request.getPercFaturamento() > 100)
                errors.add(ET_PERCFATURAMENTO);
        } else
            errors.add(ET_PERCFATURAMENTO);

        if (request.getPercVendaCartao() == 0 && request.getPercFaturamentoEcommerce() == 0) {
            errors.add(ET_PERCCARTAOPRESENTE);
            errors.add(ET_PERCECOMMERCE);
        }
        else {
            if (request.getPercVendaCartao() != 0) {
                if (request.getPercVendaCartao() > 100)
                    errors.add(ET_PERCCARTAOPRESENTE);
            }

            if (request.getPercFaturamentoEcommerce() != 0) {
                if (request.getPercFaturamentoEcommerce() > 100)
                    errors.add(ET_PERCECOMMERCE);
            }
        }

        if (request.getEntregaPosCompra() != null) {
            if (request.getEntregaPosCompra().equals("S")) {
                if (request.getPercEntregaImediata() == 0 && request.getPercEntregaPosterior() == 0) {
                    errors.add(ET_PERCENTREGAIMEDIATA);
                    errors.add(ET_PERCENTREGAPOSTERIOR);
                } else {
                    if (request.getPercEntregaImediata() != 0) {
                        if (request.getPercEntregaImediata() > 100)
                            errors.add(ET_PERCENTREGAIMEDIATA);
                    }

                    if (request.getPercEntregaPosterior() != 0) {
                        if (request.getPercEntregaPosterior() > 100)
                            errors.add(ET_PERCENTREGAPOSTERIOR);
                    }
                }
            }
        }

        return errors;
    }
}
