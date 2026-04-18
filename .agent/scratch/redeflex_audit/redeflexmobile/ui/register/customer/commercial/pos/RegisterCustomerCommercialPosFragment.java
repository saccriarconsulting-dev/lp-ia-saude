package com.axys.redeflexmobile.ui.register.customer.commercial.pos;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;
import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

import android.view.View;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.shared.models.ModeloPOSConexao;
import com.axys.redeflexmobile.shared.models.customerregister.MachineType;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity.RegisterCustomerActivityListener;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Rogério Massa on 18/02/19.
 */

public class RegisterCustomerCommercialPosFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerCommercialPosView,
        RegisterCustomerActivityListener {

    @Inject RegisterCustomerCommercialPosPresenter presenter;

    @BindView(R.id.customer_register_commercial_pos_spn_model) CustomSpinner cptSpinnerModel;
    @BindView(R.id.customer_register_commercial_pos_spn_connection) CustomSpinner cptSpinnerConnection;
    @BindView(R.id.customer_register_commercial_pos_spn_carrier) CustomSpinner cptSpinnerCarrier;
    @BindView(R.id.customer_register_commercial_pos_cet_cable_length) CustomEditText cetCableLength;
    @BindView(R.id.customer_register_commercial_pos_cet_rent_value) CustomEditText cetRentValue;
    @BindView(R.id.customer_register_commercial_pos_bt_cancel) Button btCancel;
    @BindView(R.id.customer_register_commercial_pos_bt_confirm) Button btConfirm;

    private MachineType machineType;
    private Integer positionInList;

    public static RegisterCustomerCommercialPosFragment newInstance(MachineType machineType,
                                                                    Integer position) {
        RegisterCustomerCommercialPosFragment fragment = new RegisterCustomerCommercialPosFragment();
        fragment.setMachineType(machineType);
        fragment.setPositionInList(position);
        return fragment;
    }

    private void setMachineType(MachineType machineType) {
        this.machineType = machineType;
    }

    private void setPositionInList(Integer positionInList) {
        this.positionInList = positionInList;
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_commercial_pos;
    }

    @Override
    public void initialize() {
        parentActivity.setKeyboardListenerActivated(false);

        cptSpinnerModel.setCallback(item -> presenter.getPosById(item.getIdValue()));
        cptSpinnerConnection.setCallback(item -> {
            if (item instanceof ModeloPOSConexao) {
                presenter.getPosConnection((ModeloPOSConexao) item);
            }
        });
        cetRentValue.setText(String.valueOf(EMPTY_DOUBLE));
        btCancel.setOnClickListener(v -> onBackPressed());
        btConfirm.setOnClickListener(v -> savePos());

        presenter.setPositionInList(positionInList);
        presenter.initializeData();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        parentActivity.setKeyboardListenerActivated(true);
        parentActivity.closeFragmentWithoutBottomBarFromCommercial();
    }

    @Override
    public void fillPosModel(final List<ICustomSpinnerDialogModel> list) {
        cptSpinnerModel.setList(list);
        initializeValues();
    }

    @Override
    public void setPosFields(final ModeloPOS machineType) {
        cptSpinnerConnection.removeSelection();
        cptSpinnerConnection.setList(new ArrayList<>(machineType.getConnections()));
        cetRentValue.setText(StringUtils.maskCurrencyDouble(machineType.getValorAluguelPadrao()));
    }

    @Override
    public void showCarriersField(final List<ICustomSpinnerDialogModel> carriers) {
        cptSpinnerCarrier.setVisibility(View.VISIBLE);
        cptSpinnerCarrier.setList(carriers);
    }

    @Override
    public void hideCarriersField() {
        cptSpinnerCarrier.hideVisibilityAndClearValue();
    }

    @Override
    public void showCableLengthField() {
        cetCableLength.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCableLengthField() {
        cetCableLength.setVisibility(View.GONE);
        cetCableLength.clearValue();
    }

    private void initializeValues() {
        if (machineType == null) return;
        cptSpinnerModel.doSelectWithCallback(machineType.getDescription());
        cptSpinnerConnection.doSelectWithCallback(machineType.getIdConnectionType());
        cptSpinnerCarrier.doSelectWithCallback(machineType.getCarrierId());
        final Integer cableLength = machineType.getCableLength();
        cetCableLength.setText(cableLength == null ? EMPTY_STRING : String.valueOf(cableLength));
        cetRentValue.setText(StringUtils.maskCurrencyDouble(machineType.getSelectedRentValue()));
    }

    private void savePos() {
        final MachineType machineType = new MachineType();
        machineType.setModelId(cptSpinnerModel.getSelectedItemId());
        machineType.setIdConnectionType(cptSpinnerConnection.getSelectedItemId());
        machineType.setSelectedRentValue(cetRentValue.getCurrencyDouble());
        machineType.setCarrierId(cptSpinnerCarrier.getSelectedItemId());
        final String cableLengthText = cetCableLength.getText();
        machineType.setCableLength(cableLengthText.isEmpty() ? null : Integer.parseInt(cableLengthText));
        presenter.savePos(machineType);
    }
}
