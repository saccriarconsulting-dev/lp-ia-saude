package com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.customerregister.PrazoNegociacao;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity;
import com.axys.redeflexmobile.ui.simulationrate.dialogs.CancelRegisterRateDialog;
import com.axys.redeflexmobile.ui.simulationrate.dialogs.ChooseImportDataRegisterDialog;
import com.axys.redeflexmobile.ui.simulationrate.registerlist.RegisterProspectListActivity;
import com.axys.redeflexmobile.ui.simulationrate.registertaxes.RegisterTaxesActivity;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CNPJ;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CPF;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CPF_INSERTED;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_EMAIL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FANTASY_NAME;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FIRST_PHONE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FORESEEN_REVENUE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FULL_NAME;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_ANTICIPATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_MCC;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_NEGOTIATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_PERSON_TYPE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CNPJ;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CPF;
import static com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity.FLAG_PROSPECT_MDR;
import static com.axys.redeflexmobile.ui.simulationrate.registerlist.RegisterProspectListActivity.KEY_ITEM_LISTED;

/**
 * @author Lucas Marciano on 30/04/2020
 */
@SuppressLint("NonConstantResourceId")
public class RegisterRateActivity extends BaseActivity implements RegisterRateView {

    private static final int BUTTON_WAIT_DURATION = 1200;
    public static final String KEY_PROSPECT_ID = "KEY_PROSPECT_ID";
    public static final String KEY_PROSPECT_IS_NEW = "KEY_PROSPECT_IS_NEW";
    public static final String KEY_PROSPECT_NO_TAXES = "KEY_PROSPECT_NO_TAXES";
    public static final String KEY_PROSPECT_COMPLETE_REGISTER = "KEY_PROSPECT_COMPLETE_REGISTER";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cs_person_type)
    CustomSpinner csPersonType;
    @BindView(R.id.cs_trading_term)
    CustomSpinner csTradingTerm;
    @BindView(R.id.cs_mcc)
    CustomSpinner csMcc;
    @BindView(R.id.cs_anticipation)
    CustomSpinner csAnticipation;
    @BindView(R.id.cet_document)
    CustomEditText cetDocument;
    @BindView(R.id.cet_estimated_billing)
    CustomEditText cetEstimatedBilling;
    @BindView(R.id.cet_name)
    CustomEditText cetName;
    @BindView(R.id.cet_fantasy_name)
    CustomEditText cetFantasyName;
    @BindView(R.id.cet_phone)
    CustomEditText cetPhone;
    @BindView(R.id.cet_email)
    CustomEditText cetEmail;
    @BindView(R.id.bt_generate_taxes)
    Button btGenerateTaxes;
    @BindView(R.id.bt_show_taxes)
    Button btShowTaxes;
    @BindView(R.id.bt_go_to_register)
    Button btGoToRegister;
    @BindView(R.id.loading_view)
    View viewLoading;
    @BindView(R.id.sv_content)
    ScrollView svContent;
    @BindView(R.id.ll_buttons_container)
    LinearLayout llButtonContainer;
    @BindView(R.id.cet_taxa_rav)
    CustomEditText cetTaxaRav;

    @Inject
    RegisterRatePresenter presenter;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int prospectId = 0;
    private boolean isNewProspect = true;
    private boolean completeRegister = false;
    private boolean noTaxesFound = false;
    private boolean itemAreListed = false;

    private ProspectingClientAcquisition prospect = new ProspectingClientAcquisition();
    private GPSTracker gpsTracker;

    @Override
    protected int getContentView() {
        return R.layout.activity_register_rate;
    }

    @Override
    protected void initialize() {
        getBundleVariable();
        gpsTracker = new GPSTracker(this);
        presenter.initializer(prospectId, completeRegister);
        presenter.loadDataPersonType();

        if (prospectId > 0) {
            presenter.loadViewData();
        } else {
            presenter.loadDataTradingTerms();
        }

        presenter.loadDataAnticipation();

        setupToolbar();
        setupCallbackAndActions();

        if (itemAreListed) {
            disableFields();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void fillViews(ProspectingClientAcquisition prospect) {
        clearErrors();
        this.prospect = prospect;

        EnumRegisterPersonType personType
                = EnumRegisterPersonType.getEnumByCharValue(prospect.getPersonType());
        EnumRegisterAnticipation anticipation
                = EnumRegisterAnticipation.getEnumByValue(prospect.isAnticipation() ? 1 : 0);
        presenter.loadDataMCC(personType.getIdValue());
        presenter.loadDataTradingTerms();
        setupDocumentField(personType);

        csPersonType.doSelect(personType);
        csAnticipation.doSelect(anticipation);
        cetDocument.setText(prospect.getCpfCnpjNumber());
        cetEstimatedBilling.setText(Util_IO.formatDoubleToDecimalPercent(
                prospect.getEstimatedAverageBilling()
        ));
        cetName.setText(prospect.getCompleteName());
        cetFantasyName.setText(prospect.getFantasyName());
        cetPhone.setText(prospect.getPhone());
        cetEmail.setText(prospect.getEmail());
        cetTaxaRav.setText(Util_IO.formatDoubleToDecimalPercent(
                prospect.getTaxaRav()
        ));

        if (completeRegister) {
            if (prospect.getCompleteName().length() <= 0) cetName.showError();
            if (prospect.getFantasyName().length() <= 0) cetFantasyName.showError();
            if (prospect.getPhone().length() <= 0) cetPhone.showError();
            if (prospect.getEmail().length() <= 0) cetEmail.showError();
            svContent.smoothScrollTo((int) cetEmail.getX(), EMPTY_INT);
            cetEmail.setFocusable(true);
            cetEmail.setClickable(true);
        }
    }

    @Override
    public void fillSpinnerPersonType(List<ICustomSpinnerDialogModel> list) {
        csPersonType.setList(list);
    }

    @Override
    public void fillSpinnerTradingTerms(List<ICustomSpinnerDialogModel> list) {
        csTradingTerm.setList(list);
        if (!isNewProspect) {
            ICustomSpinnerDialogModel item = Stream.ofNullable(list)
                    .filter(aux -> aux.getIdValue().equals(prospect.getIdTradingTerm()))
                    .findFirst()
                    .orElse(null);
            if (item != null) {
                csTradingTerm.doSelect(item);
                setupAnticipationField(item);
            }
        }
    }

    @Override
    public void fillSpinnerMCC(List<ICustomSpinnerDialogModel> list) {
        csMcc.setList(list);
        if (!isNewProspect) {
            ICustomSpinnerDialogModel item = Stream.ofNullable(list)
                    .filter(aux -> aux.getIdValue().equals(prospect.getIdMcc()))
                    .findFirst()
                    .orElse(null);
            if (item != null) {
                csMcc.doSelect(item);
            }
        }
    }

    @Override
    public void fillSpinnerAnticipation(List<ICustomSpinnerDialogModel> list) {
        csAnticipation.setList(list);
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        clearErrors();
        if (errors == null || errors.isEmpty()) return;
        if (errors.contains(SPN_PERSON_TYPE)) csPersonType.showError();
        if (errors.contains(SPN_NEGOTIATION)) csTradingTerm.showError();
        if (errors.contains(SPN_MCC)) csMcc.showError();
        if (errors.contains(SPN_ANTICIPATION)) csAnticipation.showError();
        if (errors.contains(ET_CPF)) cetDocument.showError();
        if (errors.contains(ET_CNPJ))
            cetDocument.showError(getString(R.string.message_error_format_cnpj));
        if (errors.contains(ET_CPF_INSERTED))
            cetDocument.showError(getString(R.string.message_error_format_cpf));
        if (errors.contains(ET_FORESEEN_REVENUE)) cetEstimatedBilling.showError();
        if (errors.contains(ET_EMAIL)) cetEmail.showError();

        if (completeRegister) {
            if (errors.contains(ET_FULL_NAME)) cetName.showError();
            if (errors.contains(ET_FANTASY_NAME)) cetFantasyName.showError();
            if (errors.contains(ET_FIRST_PHONE)) cetPhone.showError();
        }
    }

    @Override
    public void disableAnticipation() {
        csAnticipation.setEnabled(false);
    }

    @Override
    public void goToNextScreen(int prospectId) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PROSPECT_ID, prospectId);
        if (completeRegister) {
            bundle.putBoolean(KEY_PROSPECT_IS_NEW, true);
        } else {
            if (noTaxesFound)
                bundle.putBoolean(KEY_PROSPECT_IS_NEW, true);
            else
                bundle.putBoolean(KEY_PROSPECT_IS_NEW, isNewProspect);
        }
        Utilidades.openNewActivity(
                this,
                RegisterTaxesActivity.class,
                bundle,
                true
        );
    }

    @Override
    public void setPersonJuridicInformation(@Nullable ConsultaReceita consultReceipt) {
        if (consultReceipt != null) {
            if (isNewProspect) {
                cetFantasyName.setText(consultReceipt.getFantasia());
                cetEmail.setText(consultReceipt.getEmail());
                cetPhone.setText(consultReceipt.getTelefone());
            }
            presenter.loadMccValueByIdMcc(consultReceipt);
        }
    }

    @Override
    public void fillMccSpinnerValue(@Nullable ICustomSpinnerDialogModel item) {
        if (item != null) {
            csMcc.doSelect(item);
            csMcc.setEnabled(false);
        } else {
            csMcc.setEnabled(true);
        }
    }

    @Override
    public void showLoading() {
        viewLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        viewLoading.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (completeRegister || noTaxesFound) {
            CancelRegisterRateDialog dialog = CancelRegisterRateDialog.newInstance(presenter::rollbackProspect);
            dialog.show(getSupportFragmentManager(), CancelRegisterRateDialog.class.getName());
        } else {
            CancelRegisterRateDialog dialog = CancelRegisterRateDialog.newInstance(this::openListActivity);
            dialog.show(getSupportFragmentManager(), CancelRegisterRateDialog.class.getName());
        }
    }

    @Override
    public void openListActivity() {
        Utilidades.openNewActivity(
                this,
                RegisterProspectListActivity.class,
                null,
                true
        );
    }

    @Override
    public String getDocumentValue() {
        return cetDocument.getText();
    }

    @Override
    public void checkTaxes() {
        ChooseImportDataRegisterDialog dialog = ChooseImportDataRegisterDialog.newInstance(this::openRegisterActivity, false);
        dialog.show(getSupportFragmentManager(), ChooseImportDataRegisterDialog.class.getName());
    }

    private void clearErrors() {
        csPersonType.hideError();
        csTradingTerm.hideError();
        csMcc.hideError();
        csAnticipation.hideError();
        cetDocument.hideError();
        cetEstimatedBilling.hideError();
        cetName.hideError();
        cetFantasyName.hideError();
        cetPhone.hideError();
        cetEmail.hideError();
        cetTaxaRav.hideError();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        setTitle(getString(itemAreListed ? R.string.register_rate_title_prospect : R.string.register_rate_title));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        compositeDisposable.add(
                RxToolbar.navigationClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> onBackPressed(), Timber::e)
        );
    }

    private void setupCallbackAndActions() {
        csPersonType.setCallback(item -> {
            presenter.loadDataMCC(item.getIdValue());
            cetDocument.clearValue();
            setupDocumentField(item);
        });

        csTradingTerm.setCallback(this::setupAnticipationField);

        compositeDisposable.add(
                RxView.clicks(btGenerateTaxes)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> {
                            final Integer personTypeId = csPersonType.getSelectedItemId();
                            final Integer termId       = csTradingTerm.getSelectedItemId();
                            final Integer mccId        = csMcc.getSelectedItemId();
                            final Double  revenue      = cetEstimatedBilling.getCurrencyDouble();

                            boolean ok = true;
                            if (personTypeId == null) { csPersonType.showError(); ok = false; }
                            if (termId == null)       { csTradingTerm.showError(); ok = false; }
                            if (mccId == null)        { csMcc.showError(); ok = false; }
                            if (revenue == null || revenue <= 0d) { cetEstimatedBilling.showError(); ok = false; }

                            if (!ok) {
                                Timber.w("[MDR][VALIDATE][NOK] personType=%s termId=%s mcc=%s revenue=%s",
                                        String.valueOf(personTypeId),
                                        String.valueOf(termId),
                                        String.valueOf(mccId),
                                        String.valueOf(revenue));
                                return;
                            }

                            if (gpsTracker.isGPSEnabled) {
                                presenter.save(returnValueInInterface());
                            } else {
                                gpsTracker.showSettingsAlert();
                            }
                        }, Timber::e)
        );

        cetDocument.setAfterTextListener(value -> {
            if (StringUtils.isCnpjValid(value)) {
                presenter.queryDocumentPersonJuridic(StringUtils.returnOnlyNumbers(value));
            }
        });

        if (isNewProspect || noTaxesFound || completeRegister) {
            btGenerateTaxes.setVisibility(View.VISIBLE);
            llButtonContainer.setVisibility(View.GONE);
        } else {
            btGenerateTaxes.setVisibility(View.GONE);
            llButtonContainer.setVisibility(View.VISIBLE);

            compositeDisposable.add(
                    RxView.clicks(btShowTaxes)
                            .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(v -> this.goToNextScreen(prospectId), Timber::e)
            );

            compositeDisposable.add(
                    RxView.clicks(btGoToRegister)
                            .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(v -> this.openDialogCheckTaxes(), Timber::e)
            );
        }

        csAnticipation.setCallback(this::setupTaxaRavField);
    }

    private void openDialogCheckTaxes() {
        presenter.validateProspect();
    }

    private void setupDocumentField(ICustomSpinnerDialogModel item) {
        if (item.getIdValue().equals(EnumRegisterPersonType.PHYSICAL.getIdValue())) {
            cetDocument.setLabel(getString(R.string.register_rate_et_cpf));
            cetDocument.setMask(CPF);
        } else if (item.getIdValue().equals(EnumRegisterPersonType.JURIDICAL.getIdValue())) {
            cetDocument.setLabel(getString(R.string.register_rate_et_cnpj));
            cetDocument.setMask(CNPJ);
        } else {
            cetDocument.setLabel(getString(R.string.register_rate_et_document));
        }
    }

    private void setupAnticipationField(ICustomSpinnerDialogModel item) {
        PrazoNegociacao prazoNegociacao = (PrazoNegociacao) item;
        if (prazoNegociacao == null) return;

        if (prazoNegociacao.isConfiguraAntecipacao()) {
            if (!itemAreListed) {
                csAnticipation.setEnabled(true);
                cetTaxaRav.setText(null);
            }
        } else {
            csAnticipation.setEnabled(false);
            csAnticipation.doSelect(EnumRegisterAnticipation.YES);
        }
    }

    private void setupTaxaRavField(@androidx.annotation.Nullable ICustomSpinnerDialogModel item) {
        if (item == null) return;

        if (item.getIdValue() != EnumRegisterAnticipation.YES.getIdValue()) {
            cetTaxaRav.setVisibility(View.GONE);
            cetTaxaRav.setText(null);
            return;
        }

        cetTaxaRav.setVisibility(View.VISIBLE);

        final Integer personTypeId = csPersonType.getSelectedItemId();
        final Integer termId       = csTradingTerm.getSelectedItemId();
        final Integer mccId        = csMcc.getSelectedItemId();
        final Double  revenueRaw   = cetEstimatedBilling.getCurrencyDouble();

        if (personTypeId == null || termId == null || mccId == null || revenueRaw == null || revenueRaw <= 0d) {
            Timber.w("[MDR][RAV][NOK] personType=%s termId=%s mcc=%s revenue=%s",
                    String.valueOf(personTypeId),
                    String.valueOf(termId),
                    String.valueOf(mccId),
                    String.valueOf(revenueRaw));
            cetTaxaRav.setText(null);
            return;
        }

        final double revenue = new java.math.BigDecimal(String.valueOf(revenueRaw))
                .setScale(2, java.math.RoundingMode.HALF_UP)
                .doubleValue();

        java.util.List<TaxaMdr> taxaMdr = new DBTaxaMdr(RegisterRateActivity.this)
                .getAllTaxFlagTypes(personTypeId, revenue, termId, mccId);

        if (taxaMdr != null && !taxaMdr.isEmpty()) {
            cetTaxaRav.setText(String.valueOf(taxaMdr.get(0).getAnticipation()));
            Timber.d("[MDR][RAV] personType=%d revenue=%.2f termId=%d mcc=%d -> ok (rows=%d)",
                    personTypeId, revenue, termId, mccId, taxaMdr.size());
        } else {
            Timber.w("[MDR][RAV][EMPTY] personType=%d revenue=%.2f termId=%d mcc=%d",
                    personTypeId, revenue, termId, mccId);
            cetTaxaRav.setText(null);
        }
    }

    @androidx.annotation.NonNull
    private ProspectingClientAcquisition returnValueInInterface() {
        final ProspectingClientAcquisition prospect = new ProspectingClientAcquisition();

        final Integer personTypeId = csPersonType.getSelectedItemId();
        final EnumRegisterPersonType personType = (personTypeId == null)
                ? null
                : EnumRegisterPersonType.getEnumByIdValue(personTypeId);

        if (prospectId > 0) {
            prospect.setId(prospectId);
        }
        if (personType != null) {
            prospect.setPersonType(personType.getCharValue());
        }

        prospect.setIdTradingTerm(csTradingTerm.getSelectedItemId());
        prospect.setIdMcc(csMcc.getSelectedItemId());

        prospect.setAnticipation(
                csAnticipation.getSelectedItemId() != null
                        ? csAnticipation.getSelectedItemId().equals(EnumRegisterAnticipation.YES.getIdValue())
                        : null
        );

        // Documento somente dígitos
        prospect.setCpfCnpjNumber(com.axys.redeflexmobile.shared.util.StringUtils
                .returnOnlyNumbers(cetDocument.getText()));

        // Faturamento previsto
        final Double revenueRaw = cetEstimatedBilling.getCurrencyDouble();
        if (revenueRaw != null) {
            final double revenue = new java.math.BigDecimal(String.valueOf(revenueRaw))
                    .setScale(2, java.math.RoundingMode.HALF_UP)
                    .doubleValue();
            prospect.setEstimatedAverageBilling(revenue);
        } else {
            prospect.setEstimatedAverageBilling(EMPTY_DOUBLE);
        }

        prospect.setCompleteName(cetName.getText() != null ? cetName.getText().trim() : null);
        prospect.setFantasyName(cetFantasyName.getText() != null ? cetFantasyName.getText().trim() : null);
        prospect.setPhone(cetPhone.getText() != null ? cetPhone.getText().trim() : null);
        prospect.setEmail(cetEmail.getText() != null ? cetEmail.getText().trim() : null);
        // Geolocalização
        prospect.setLatitude(gpsTracker.getLatitude());
        prospect.setLongitude(gpsTracker.getLongitude());
        prospect.setPrecision(gpsTracker.getPrecisao());
        final Double ravRaw = cetTaxaRav.getCurrencyDouble();
        if (ravRaw != null) {
            final double rav = new java.math.BigDecimal(String.valueOf(ravRaw))
                    .setScale(2, java.math.RoundingMode.HALF_UP)
                    .doubleValue();
            prospect.setTaxaRav(rav);
        } else {
            prospect.setTaxaRav(EMPTY_DOUBLE);
        }

        timber.log.Timber.d("[MDR][SNAPSHOT] personType=%s termId=%s mcc=%s revenue=%s rav=%s",
                String.valueOf(personTypeId),
                String.valueOf(csTradingTerm.getSelectedItemId()),
                String.valueOf(csMcc.getSelectedItemId()),
                String.valueOf(prospect.getEstimatedAverageBilling()),
                String.valueOf(prospect.getTaxaRav()));

        return prospect;
    }

    private void getBundleVariable() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            prospectId = bundle.getInt(KEY_PROSPECT_ID, -1);
            isNewProspect = bundle.getBoolean(KEY_PROSPECT_IS_NEW, true);
            completeRegister = bundle.getBoolean(KEY_PROSPECT_COMPLETE_REGISTER, false);
            noTaxesFound = bundle.getBoolean(KEY_PROSPECT_NO_TAXES, false);
            itemAreListed = bundle.getBoolean(KEY_ITEM_LISTED, false);
        }
    }

    private void openRegisterActivity() {
        Bundle bundle = new Bundle();
        bundle.putInt(FLAG_PROSPECT_MDR, prospectId);

        Utilidades.openNewActivity(
                this,
                RegisterCustomerActivity.class,
                bundle,
                true
        );
    }

    private void disableFields() {
        csPersonType.setEnabled(false);
        csTradingTerm.setEnabled(false);
        csMcc.setEnabled(false);
        csAnticipation.setEnabled(false);
        cetDocument.setEnabled(false);
        cetEstimatedBilling.setEnabled(false);
        cetName.setEnabled(false);
        cetFantasyName.setEnabled(false);
        cetPhone.setEnabled(false);
        cetEmail.setEnabled(false);
        cetTaxaRav.setEnabled(false);

        hideKeyboardFrom(csPersonType);
        hideKeyboardFrom(csTradingTerm);
        hideKeyboardFrom(csMcc);
        hideKeyboardFrom(csAnticipation);
        hideKeyboardFrom(cetDocument);
        hideKeyboardFrom(cetEstimatedBilling);
        hideKeyboardFrom(cetName);
        hideKeyboardFrom(cetFantasyName);
        hideKeyboardFrom(cetPhone);
        hideKeyboardFrom(cetEmail);
        hideKeyboardFrom(cetTaxaRav);

        cetDocument.removeFieldFocus();
        cetEstimatedBilling.removeFieldFocus();
        cetName.removeFieldFocus();
        cetFantasyName.removeFieldFocus();
        cetPhone.removeFieldFocus();
        cetEmail.removeFieldFocus();
        cetTaxaRav.removeFieldFocus();
    }

    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}