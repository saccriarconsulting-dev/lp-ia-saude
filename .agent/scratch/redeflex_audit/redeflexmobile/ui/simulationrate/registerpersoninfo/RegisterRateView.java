package com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.mvp.BaseView;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface RegisterRateView extends BaseView {

    /**
     * Fill the spinner of the Person Type with data that did going of the data base.
     *
     * @param list List<ICustomSpinnerDialogModel>
     */
    void fillSpinnerPersonType(List<ICustomSpinnerDialogModel> list);

    /**
     * Fill the spinner of the Trading Terms with data that did going of the data base.
     *
     * @param list List<ICustomSpinnerDialogModel>
     */
    void fillSpinnerTradingTerms(List<ICustomSpinnerDialogModel> list);

    /**
     * Fill the spinner of the MCC with data that did going of the data base.
     *
     * @param list List<ICustomSpinnerDialogModel>
     */
    void fillSpinnerMCC(List<ICustomSpinnerDialogModel> list);

    /**
     * Fill the spinner of the Anticipation with data that did going of the data base.
     *
     * @param list List<ICustomSpinnerDialogModel>
     */
    void fillSpinnerAnticipation(List<ICustomSpinnerDialogModel> list);

    /**
     * Show errors in the view.
     *
     * @param errors List<EnumRegisterFields>
     */
    void setErrors(List<EnumRegisterFields> errors);

    /**
     * Disable the Anticipation spinner.
     */
    void disableAnticipation();

    /**
     * Go to next activity.
     */
    void goToNextScreen(int prospectId);

    /**
     * Show data of the client that did sent by the server.
     *
     * @param consultReceipt ConsultaReceita
     */
    void setPersonJuridicInformation(@Nullable ConsultaReceita consultReceipt);

    /**
     * Fill spinner MCC with value sent by the server.
     *
     * @param item ICustomSpinnerDialogModel
     */
    void fillMccSpinnerValue(@Nullable ICustomSpinnerDialogModel item);

    /**
     * Fill views when the item will be edited.
     *
     * @param prospectingClientAcquisition ProspectingClientAcquisition
     */
    void fillViews(ProspectingClientAcquisition prospectingClientAcquisition);

    void openListActivity();

    String getDocumentValue();

    void checkTaxes();
}
