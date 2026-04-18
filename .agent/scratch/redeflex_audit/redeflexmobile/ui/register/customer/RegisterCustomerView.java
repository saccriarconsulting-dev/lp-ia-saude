package com.axys.redeflexmobile.ui.register.customer;

import androidx.fragment.app.Fragment;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;

/**
 * @author Bruno Pimentel on 20/11/18.
 */

public interface RegisterCustomerView extends BaseActivityView {

    void onInitializeCustomerSuccess();

    CustomerRegister getCustomerRegister();

    CustomerRegister getCustomerRegisterClone();

    ConsultaReceita getConsultaReceita();

    void setConsultaReceita(ConsultaReceita consultaReceita);

    CustomerRegister getCustomerRegisterCommercialCache();

    void setCustomerRegisterCommercialCache(CustomerRegister customerRegisterCommercialCache);

    List<EnumRegisterFields> getCustomerRegisterCommercialErrorsCache();

    void setCustomerRegisterCommercialErrorsCache(List<EnumRegisterFields> customerRegisterCommercialErrorsCache);

    void inicializaAdquirenciaPJ();

    void initializeNonAcquisitionFlow();

    void inicializaAdquirenciaPF();

    ProspectingClientAcquisition getProspectObject();

    void stepValidatedBack();

    void cancelFlow();

    void stepValidated();

    void onSaveSuccess();

    void openFragmentWithoutBottomBar(Fragment fragment);

    void closeFragmentWithoutBottomBarFromAddress();

    void closeFragmentWithoutBottomBarFromCommercial();

    void closeFragmentWithoutBottomBarFromHorarioFunc();

    void setFinancialAlert();

    boolean getFinancialAlert();

    boolean isBlockRegisterFields();

    int getProspectingClientAcquisitionId();

    void setProspectObject(ProspectingClientAcquisition prospectingClientAcquisition);

    void setProspectModalIsShowed(boolean isShowed);

    boolean getProspectModalIsShowed();
}
