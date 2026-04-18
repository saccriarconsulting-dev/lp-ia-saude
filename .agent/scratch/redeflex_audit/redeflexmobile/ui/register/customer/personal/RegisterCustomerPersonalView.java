package com.axys.redeflexmobile.ui.register.customer.personal;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.ConsultaPessoaFisica;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

import java.util.List;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public interface RegisterCustomerPersonalView extends RegisterCustomerCommon {

    void fillClientType(List<ICustomSpinnerDialogModel> list);

    void fillPersonType(List<ICustomSpinnerDialogModel> list);

    void fillMccList(List<ICustomSpinnerDialogModel> list);

    void fillSegmentList(List<ICustomSpinnerDialogModel> list);

    void fillMccFromConsultaReceita(ICustomSpinnerDialogModel mcc);

    void initializeSpinnersSelection(CustomerRegister customerRegister);

    void makeAcquisitionPhysicalLayout();

    void makeAcquisitionJuridicalLayout();

    void makeNonAcquisitionPhysicalLayout();

    void makeNonAcquisitionJuridicalLayout();

    void setErrors(List<EnumRegisterFields> errors);

    void initializeFieldValues(CustomerRegister customerRegister);

    void onValidationSuccess();

    void onValidationSuccessBack();

    void onQueryCnpjSuccess(ConsultaReceita consultaReceita);

    void onQueryCnpjResponseNoDialog(ConsultaReceita consultaReceita);

    void onQueryCpfSuccess(ConsultaPessoaFisica consultaPessoaFisica);

    void fillInterfaceWithProspectData(ProspectingClientAcquisition prospecting,
                                       ICustomSpinnerDialogModel personType,
                                       boolean isPhysical,
                                       @Nullable ICustomSpinnerDialogModel mcc);

    void showDialogToFillTheJuridicalInformation(ProspectingClientAcquisition prospecting);

    void fillProfissoes(List<ICustomSpinnerDialogModel> profissoes);

    void fillRenda(List<ICustomSpinnerDialogModel> renda);

    void fillPatrimonio(List<ICustomSpinnerDialogModel> patrimonio);

    void fillSexo(List<ICustomSpinnerDialogModel> list);
}
