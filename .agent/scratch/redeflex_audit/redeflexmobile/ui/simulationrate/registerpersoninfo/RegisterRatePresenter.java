package com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo;

import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface RegisterRatePresenter extends BasePresenter<RegisterRateView> {

    void initializer(int prospectId, boolean completeRegister);

    void loadViewData();

    void save(ProspectingClientAcquisition prospect);

    void loadDataPersonType();

    void loadDataTradingTerms();

    void loadDataMCC(int personType);

    void loadDataAnticipation();

    void queryDocumentPersonJuridic(String text);

    void rollbackProspect();

    void loadMccValueByIdMcc(ConsultaReceita consultaReceita);

    void validateProspect();
}
