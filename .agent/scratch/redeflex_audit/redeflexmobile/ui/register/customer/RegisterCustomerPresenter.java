package com.axys.redeflexmobile.ui.register.customer;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.List;

/**
 * @author Bruno Pimentel on 30/11/18.
 */
public interface RegisterCustomerPresenter extends BasePresenter<RegisterCustomerView> {

    CustomerRegister getCustomerRegister();

    CustomerRegister getCustomerRegisterClone();

    ConsultaReceita getConsultaReceita();

    void setConsultaReceita(ConsultaReceita consultaReceita);

    CustomerRegister getCustomerRegisterCommercialCache();

    void setCustomerRegisterCommercialCache(CustomerRegister customerRegisterCommercialCache);

    List<EnumRegisterFields> getCustomerRegisterCommercialErrorsCache();

    void setCustomerRegisterCommercialErrorsCache(List<EnumRegisterFields> customerRegisterCommercialErrorsCache);

    void getCustomerByCustomer(String idCustomer);

    void getProspectValue(int prospectMdcIdExtra);

    void getCustomerByCustomerRegister(int idProspect);

    void getCustomerByProspect(int idProspect);

    void saveCustomerRegister();

    void getCustomerBySolicitacaoPid(int idSolicitacaoPidServer);
}
