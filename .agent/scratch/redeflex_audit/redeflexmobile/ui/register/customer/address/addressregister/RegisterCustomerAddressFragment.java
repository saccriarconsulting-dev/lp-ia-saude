package com.axys.redeflexmobile.ui.register.customer.address.addressregister;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_ADDRESS;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_ADDRESS_COMPLEMENT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_ADDRESS_NUMBER;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_ADDRESS_TYPE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_NEIGHBORHOOD;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_POSTAL_CODE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_CITY;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_STATE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.StringUtils.CEP_LENGTH;
import static com.axys.redeflexmobile.ui.register.customer.address.addressregister.RegisterCustomerAddressPresenterImpl.OPERATION_REGISTER;
import static com.axys.redeflexmobile.ui.register.customer.address.addressregister.RegisterCustomerAddressPresenterImpl.OPERATION_UPDATE;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddress;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.dialog.register.CustomerRegisterAddressRemoveDialog;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity.RegisterCustomerActivityListener;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Bruno Pimentel on 21/11/18.
 */

public class RegisterCustomerAddressFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerAddressView,
        RegisterCustomerActivityListener {

    @Inject RegisterCustomerAddressPresenter presenter;

    @BindView(R.id.second_register_scroll_view) ScrollView scrollView;
    @BindView(R.id.second_register_et_postal_code) CustomEditText etPostalCode;
    //@BindView(R.id.second_register_et_address_type) CustomEditText etAddressType;
    @BindView(R.id.second_register_et_address) CustomEditText etAddress;
    @BindView(R.id.second_register_et_address_number) CustomEditText etNumber;
    @BindView(R.id.second_register_et_neighborhood) CustomEditText etNeighborhood;
    @BindView(R.id.second_register_et_address_complement) CustomEditText etComplement;
    @BindView(R.id.second_register_spn_state) CustomSpinner spnState;
    @BindView(R.id.second_register_et_city) CustomEditText etCity;
    @BindView(R.id.second_register_et_contact_name) CustomEditText etContactName;
    @BindView(R.id.second_register_et_first_phone) CustomEditText etFirstPhone;
    @BindView(R.id.second_register_et_second_phone) CustomEditText etSecondPhone;
    @BindView(R.id.second_register_et_email) CustomEditText etEmail;
    @BindView(R.id.second_register_bt_remove) Button btRemove;
    @BindView(R.id.second_register_bt_confirm) Button btConfirm;

    @BindView(R.id.second_register_sp_tipologradouro) CustomSpinner spnTipoLogradouro;
    private CustomerRegisterAddress address;
    private int operationType;

    public static RegisterCustomerAddressFragment newInstance(CustomerRegisterAddressType addressType) {
        RegisterCustomerAddressFragment fragment = new RegisterCustomerAddressFragment();
        CustomerRegisterAddress address = new CustomerRegisterAddress();
        address.setIdAddressType(addressType.getType().getValue());
        fragment.setAddress(address);
        fragment.setOperationType(OPERATION_REGISTER);
        return fragment;
    }

    public static RegisterCustomerAddressFragment newInstance(CustomerRegisterAddress address) {
        RegisterCustomerAddressFragment fragment = new RegisterCustomerAddressFragment();
        fragment.setAddress(new CustomerRegisterAddress(address));
        fragment.setOperationType(OPERATION_UPDATE);
        return fragment;
    }

    public void setAddress(CustomerRegisterAddress address) {
        this.address = address;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_address;
    }

    @Override
    public void initialize() {
        presenter.attachView(this);
        parentActivity.setKeyboardListenerActivated(false);
        setCancelButton();
        etPostalCode.setAfterTextListener(this::postalCodeListener);
        btRemove.setOnClickListener(view -> onRemoveClick());
        btConfirm.setOnClickListener(view -> onConfirmClick());
        presenter.initializeData(address, operationType);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.clearDispose();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void fillStates(List<ICustomSpinnerDialogModel> list) {
        spnState.setList(list);
    }

    public void fillTipoLogradouro(List<ICustomSpinnerDialogModel> list) {
        spnTipoLogradouro.setList(list);
    }

    @Override
    public void initializeFieldValues(CustomerRegisterAddress address) {
        etPostalCode.setText(address.getPostalCode());
        //etAddressType.setText(address.getAddressType());
        Log.d("Roni", "initializeFieldValues: " + address.getAddressType());
        spnTipoLogradouro.doSelectWithCallback(address.getAddressType());
        etAddress.setText(address.getAddressName());
        etNumber.setText(address.getAddressNumber());
        etNeighborhood.setText(address.getNeighborhood());
        etComplement.setText(address.getAddressComplement());
        spnState.doSelectWithCallback(address.getFederalState());
        etCity.setText(address.getCity());

        etContactName.setText(address.getContactName());
        if (StringUtils.isNotEmpty(address.getCellphone())
                && (StringUtils.isPhone(address.getCellphone())
                || StringUtils.isCellphone(address.getCellphone()))) {
            etFirstPhone.setText(address.getCellphone());
        }
        if (StringUtils.isNotEmpty(address.getPhoneNumber())
                && (StringUtils.isPhone(address.getPhoneNumber())
                || StringUtils.isCellphone(address.getPhoneNumber()))) {
            etSecondPhone.setText(address.getPhoneNumber());
        }
        if (address.getEmail() != null && address.getEmail().length() > 0) {
            etEmail.setText(address.getEmail());
        }
    }

    @Override
    public void blockFieldFromSearchData(List<EnumRegisterFields> fields) {
        for (EnumRegisterFields field : fields) {
            if (field.equals(ET_POSTAL_CODE)) {
                etPostalCode.setEnabled(false);
            } else if (field.equals(ET_ADDRESS)) {
                etAddress.setEnabled(false);
            } else if (field.equals(ET_ADDRESS_NUMBER)) {
                etNumber.setEnabled(false);
            } else if (field.equals(ET_NEIGHBORHOOD)) {
                etNeighborhood.setEnabled(false);
            } else if (field.equals(ET_ADDRESS_COMPLEMENT)) {
                etComplement.setEnabled(false);
            } else if (field.equals(SPN_STATE)) {
                spnState.setEnabled(false);
            } else if (field.equals(SPN_CITY)) {
                etCity.setEnabled(false);
            }
        }
    }

    @Override
    public void fillAddress(Cep postalCode) {
        clearErrors();
        //etAddressType.setText(postalCode.getTipoLogradouro());
        spnTipoLogradouro.doSelectWithCallback(postalCode.getTipoLogradouro());
        etAddress.setText(postalCode.getNomeLogradouro());
        etNeighborhood.setText(postalCode.getNomeBairro());
        etComplement.setText(postalCode.getComplementoLogradouro());
        spnState.doSelectWithCallback(postalCode.getUf());
        etCity.setText(postalCode.getNomeMunicipio());
        moveToFirstEmpty();
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) return;
        if (errors.contains(ET_POSTAL_CODE)) etPostalCode.showError();
        //if (errors.contains(ET_ADDRESS_TYPE)) etAddressType.showError();
        if (errors.contains(ET_ADDRESS_TYPE)) spnTipoLogradouro.showError();
        if (errors.contains(ET_ADDRESS)) etAddress.showError();
        if (errors.contains(ET_ADDRESS_NUMBER)) etNumber.showError();
        if (errors.contains(ET_NEIGHBORHOOD)) etNeighborhood.showError();
        if (errors.contains(SPN_STATE)) spnState.showError();
        if (errors.contains(SPN_CITY)) etCity.showError();
        moveFocusToFirstError(errors.get(EMPTY_INT));
    }

    @Override
    public void showAddressError() {
        new Alerta(
                getContext(),
                getString(R.string.app_titulo),
                getString(R.string.customer_register_address_alert_error_address)
        ).show((dialog, which) -> onBackPressed());
    }

    @Override
    public void initializeFieldByProspectInfo(ProspectingClientAcquisition prospect) {
        etFirstPhone.setText(prospect.getPhone());
        etEmail.setText(prospect.getEmail());
    }

    @Override
    public void onBackPressed() {
        parentActivity.setKeyboardListenerActivated(true);
        parentActivity.closeFragmentWithoutBottomBarFromAddress();
    }

    @Override
    public void prepareNonMainAddressLayout() {
        etContactName.setVisibility(View.GONE);
        etFirstPhone.setVisibility(View.GONE);
        etSecondPhone.setVisibility(View.GONE);
        etEmail.setVisibility(View.GONE);
    }

    private void setCancelButton() {
        btRemove.setText(operationType == OPERATION_REGISTER
                ? getString(R.string.customer_register_address_bt_cancel)
                : getString(R.string.customer_register_address_bt_remove));
    }

    private void postalCodeListener(String text) {
        if (etPostalCode.isNotFocused()) return;
        text = StringUtils.returnOnlyNumbers(text);
        if (text.length() == CEP_LENGTH) presenter.getAddress(text);
    }

    private void moveToFirstEmpty() {
        //if (etAddressType.isEmpty()) {
        //    etAddressType.requestFieldFocus();
        if (etAddress.isEmpty()) {
            etAddress.requestFieldFocus();
        } else if (etNumber.isEmpty()) {
            etNumber.requestFieldFocus();
        } else if (etNeighborhood.isEmpty()) {
            etNeighborhood.requestFieldFocus();
        } else if (etComplement.isEmpty()) {
            etComplement.requestFieldFocus();
        } else if (etCity.isEmpty()) {
            etCity.requestFieldFocus();
        }
    }

    private void moveFocusToFirstError(EnumRegisterFields error) {
        int margin = (int) getResources().getDimension(R.dimen.spacing_normal);
        switch (error) {
            case ET_POSTAL_CODE:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etPostalCode.getY() - margin);
                break;
            case ET_ADDRESS_TYPE:
                //scrollView.smoothScrollTo(EMPTY_INT, (int) etAddressType.getY() - margin);
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnTipoLogradouro.getY() - margin);
                break;
            case ET_ADDRESS:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etAddress.getY() - margin);
                break;
            case ET_ADDRESS_NUMBER:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etNumber.getY() - margin);
                break;
            case ET_NEIGHBORHOOD:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etNeighborhood.getY() - margin);
                break;
            case SPN_STATE:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnState.getY() - margin);
                break;
            case SPN_CITY:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etCity.getY() - margin);
                break;
            default:
                break;
        }
    }

    private void clearErrors() {
        etPostalCode.hideError();
        //etAddressType.hideError();
        spnTipoLogradouro.hideError();
        etAddress.hideError();
        etNumber.hideError();
        etNeighborhood.hideError();
        etComplement.hideError();
        spnState.hideError();
        etCity.hideError();
        etContactName.hideError();
        etFirstPhone.hideError();
        etSecondPhone.hideError();
        etEmail.hideError();
    }

    private void onRemoveClick() {
        if (operationType == OPERATION_REGISTER) {
            onBackPressed();
            return;
        }

        CustomerRegisterAddressRemoveDialog dialog = CustomerRegisterAddressRemoveDialog
                .newInstance(() -> presenter.doRemove());
        dialog.show(getChildFragmentManager(), dialog.getClass().getSimpleName());
    }

    private void onConfirmClick() {
        clearErrors();
        CustomerRegisterAddress address = new CustomerRegisterAddress();
        address.setPostalCode(etPostalCode.getText());
        //address.setAddressType(etAddressType.getText());
        address.setAddressType(spnTipoLogradouro.getSelectedItemDescription());
        address.setAddressName(etAddress.getText());
        address.setAddressNumber(etNumber.getText());
        address.setNeighborhood(etNeighborhood.getText());
        address.setAddressComplement(etComplement.getText());
        address.setFederalState(spnState.getSelectedItemDescription());
        address.setCity(etCity.getText());
        address.setContactName(etContactName.getText());
        address.setCellphone(etFirstPhone.getText());
        address.setPhoneNumber(etSecondPhone.getText());
        address.setEmail(etEmail.getText());
        address.setIdAddressType(this.address.getIdAddressType());
        presenter.doSave(address);
    }
}
