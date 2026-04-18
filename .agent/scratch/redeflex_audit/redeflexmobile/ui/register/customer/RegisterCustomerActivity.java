package com.axys.redeflexmobile.ui.register.customer;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType.ACQUISITION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType.PHYSICAL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.ALREADY_REGISTERED;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.DISAPPROVED;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.INVALID_DOCUMENT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.SCHEDULED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAttachment;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.NumberUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.ComponentProgressLoading;
import com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigation;
import com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigation.CustomArrowNavigationListener;
import com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigationItem;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.register.customer.address.RegisterCustomerAddressListFragment;
import com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentFragment;
import com.axys.redeflexmobile.ui.register.customer.commercial.RegisterCustomerCommercialFragment;
import com.axys.redeflexmobile.ui.register.customer.contato.RegisterCustomerDadosContatoFragment;
import com.axys.redeflexmobile.ui.register.customer.dadosec.RegisterCustomerDadosECFragment;
import com.axys.redeflexmobile.ui.register.customer.financial.RegisterCustomerFinancialFragment;
import com.axys.redeflexmobile.ui.register.customer.partners.RegisterCustomerPartnersFragment;
import com.axys.redeflexmobile.ui.register.customer.personal.RegisterCustomerPersonalFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * @author Bruno Pimentel on 20/11/18.
 */
@SuppressLint("NonConstantResourceId")
public class RegisterCustomerActivity extends BaseActivity implements RegisterCustomerView,
        CustomArrowNavigationListener {

    public static final String FLAG_CUSTOMER = "customerUpdate";
    public static final String FLAG_PROSPECT = "prospectRegister";
    public static final String FLAG_FACADE_ATTACHMENT = "facadeAttachment";
    public static final String FLAG_PROSPECT_MDR = "FLAG_PROSPECT_MDR";

    public static final int REGISTER_FLOW_FLAG = 6;
    public static final int NEGATIVE_SINGLE_INT = -1;
    public static final int SINGLE_INT = 1;

    @Inject RegisterCustomerPresenter presenter;

    @BindView(R.id.register_arrow_navigation) CustomArrowNavigation cptBottomBar;
    @BindView(R.id.register_cpl_loading) ComponentProgressLoading cptLoading;

    private RegisterCustomerPersonalFragment registerCustomerPersonalFragment;
    private RegisterCustomerAddressListFragment registerCustomerAddressListFragment;
    private RegisterCustomerFinancialFragment registerCustomerFinancialFragment;
    private RegisterCustomerCommercialFragment registerCustomerCommercialFragment;
    private RegisterCustomerAttachmentFragment registerCustomerAttachmentFragment;
    private RegisterCustomerPartnersFragment registerCustomerPartnersFragment;
    private RegisterCustomerDadosECFragment registerCustomerDadosECFragment;
    private RegisterCustomerDadosContatoFragment registerCustomerDadosContatoFragment;

    private final PublishSubject<String> backPublish = PublishSubject.create();
    private final CompositeDisposable disposables = new CompositeDisposable();

    private boolean financialAlert;
    private int prospectMdcIdExtra = -1;
    private ProspectingClientAcquisition prospectingClientAcquisition = null;
    private boolean prospectModalIsShowed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, RegisterCustomerActivity.class);
    }

    public static Intent getIntent(Context context, String clientId) {
        Intent intent = new Intent(context, RegisterCustomerActivity.class);
        intent.putExtra(FLAG_CUSTOMER, Integer.parseInt(clientId));
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_customer_register;
    }

    @Override
    protected ComponentProgressLoading getComponentProgressLoading() {
        return cptLoading;
    }

    @Override
    protected Integer getFrameLayoutId() {
        return R.id.register_fl_content_principal;
    }

    @Override
    protected void initialize() {
        setTitle(R.string.register_toolbar_title);
        showBackButtonToolbar();

        initializeFragments();
        inicializaAdquirenciaPF();
        cptBottomBar.setCallback(this);
        initializeFlow();
        createEvents();
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        presenter.detach();
        cptBottomBar.detach();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backPublish.onNext(StringUtils.EMPTY_STRING);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentManager.BackStackEntry backEntry = fragmentManager
                    .getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - SINGLE_INT);
            Fragment fragment = fragmentManager.findFragmentByTag(backEntry.getName());
            if (fragment instanceof RegisterCustomerActivityListener) {
                ((RegisterCustomerActivityListener) fragment).onBackPressed();
                return;
            }
        } catch (RuntimeException e) {
            Timber.d(e);
        }
        this.cptBottomBar.moveToPreviousFragment();
    }

    @Override
    public void onInitializeCustomerSuccess() {
        CustomerRegister customerRegister = presenter.getCustomerRegister();
        if (customerRegister != null && customerRegister.getCustomerType().equals(ACQUISITION)) {
            // Inicializa sequencia de telas que deverão ser seguidas no cadastro
            if (customerRegister.getPersonType().equals(PHYSICAL))
                this.inicializaAdquirenciaPF();
            else
                this.inicializaAdquirenciaPJ();

        } else {
            this.initializeNonAcquisitionFlow();
        }
        cptBottomBar.initializeFirstFragment();
    }

    @Override
    public CustomerRegister getCustomerRegister() {
        return presenter.getCustomerRegister();
    }

    @Override
    public CustomerRegister getCustomerRegisterClone() {
        return presenter.getCustomerRegisterClone();
    }

    @Override
    public ConsultaReceita getConsultaReceita() {
        return presenter.getConsultaReceita();
    }

    @Override
    public void setConsultaReceita(ConsultaReceita consultaReceita) {
        presenter.setConsultaReceita(consultaReceita);
    }

    @Override
    public CustomerRegister getCustomerRegisterCommercialCache() {
        return presenter.getCustomerRegisterCommercialCache();
    }

    @Override
    public void setCustomerRegisterCommercialCache(CustomerRegister customerCache) {
        presenter.setCustomerRegisterCommercialCache(customerCache);
    }

    @Override
    public List<EnumRegisterFields> getCustomerRegisterCommercialErrorsCache() {
        return presenter.getCustomerRegisterCommercialErrorsCache();
    }

    @Override
    public void setCustomerRegisterCommercialErrorsCache(List<EnumRegisterFields> errors) {
        presenter.setCustomerRegisterCommercialErrorsCache(errors);
    }

    @Override
    public void onKeyboardOpen() {
        cptBottomBar.setVisibility(View.GONE);
    }

    @Override
    public void onKeyboardClose() {
        cptBottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void inicializaAdquirenciaPJ() {
        List<CustomArrowNavigationItem> fragments = new ArrayList<>();
        fragments.add(new CustomArrowNavigationItem(registerCustomerPersonalFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerPartnersFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerDadosECFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerDadosContatoFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerAddressListFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerFinancialFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerCommercialFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerAttachmentFragment));
        cptBottomBar.setListItems(fragments);
    }

    @Override
    public void inicializaAdquirenciaPF() {
        List<CustomArrowNavigationItem> fragments = new ArrayList<>();
        fragments.add(new CustomArrowNavigationItem(registerCustomerPersonalFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerDadosECFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerDadosContatoFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerAddressListFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerFinancialFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerCommercialFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerAttachmentFragment));
        cptBottomBar.setListItems(fragments);
    }

    @Override
    public void initializeNonAcquisitionFlow() {
        List<CustomArrowNavigationItem> fragments = new ArrayList<>();
        fragments.add(new CustomArrowNavigationItem(registerCustomerPersonalFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerDadosContatoFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerAddressListFragment));
        fragments.add(new CustomArrowNavigationItem(registerCustomerAttachmentFragment));
        cptBottomBar.setListItems(fragments);
    }

    @Override
    public void onSaveSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void openFragmentWithoutBottomBar(Fragment fragment) {
        onKeyboardOpen();
        openNextFragmentWithAnimation(fragment);
    }

    @Override
    public void closeFragmentWithoutBottomBarFromAddress() {
        openPreviousFragmentWithAnimation(registerCustomerAddressListFragment);
        new Handler().postDelayed(this::onKeyboardClose, 300);
    }

    @Override
    public void closeFragmentWithoutBottomBarFromCommercial() {
        openPreviousFragmentWithAnimation(registerCustomerCommercialFragment);
        new Handler().postDelayed(this::onKeyboardClose, 300);
    }

    @Override
    public void closeFragmentWithoutBottomBarFromHorarioFunc() {
        openPreviousFragmentWithAnimation(registerCustomerDadosECFragment);
        new Handler().postDelayed(this::onKeyboardClose, 300);
    }

    @Override
    public void setFinancialAlert() {
        this.financialAlert = true;
    }

    @Override
    public boolean getFinancialAlert() {
        return financialAlert;
    }

    @Override
    public boolean isBlockRegisterFields() {
        CustomerRegister customerRegister = presenter.getCustomerRegister();
        EnumRegisterStatus status = EnumRegisterStatus.getEnumByValue(customerRegister.getSync());
        return SCHEDULED.equals(status)
                || ALREADY_REGISTERED.equals(status)
                || INVALID_DOCUMENT.equals(status)
                || DISAPPROVED.equals(status);
    }

    @Override
    public int getProspectingClientAcquisitionId() {
        return prospectMdcIdExtra;
    }

    @Override
    public void setProspectObject(ProspectingClientAcquisition prospectingClientAcquisition) {
        this.prospectingClientAcquisition = prospectingClientAcquisition;
    }

    @Override
    public void setProspectModalIsShowed(boolean isShowed) {
        this.prospectModalIsShowed = isShowed;
    }

    @Override
    public boolean getProspectModalIsShowed() {
        return prospectModalIsShowed;
    }

    @Override
    public ProspectingClientAcquisition getProspectObject() {
        return this.prospectingClientAcquisition;
    }

    @Override
    public void moveToPreviousFragment() {
        CustomArrowNavigationItem selectedItem = cptBottomBar.getSelectedItem();
        RegisterCustomerCommon fragment = (RegisterCustomerCommon) selectedItem.getTarget();
        if (fragment == null) return;
        fragment.persistCloneData();
    }

    @Override
    public void stepValidatedBack() {
        this.openPreviousFragmentWithAnimation(cptBottomBar.getTargetToPrevious());
    }

    @Override
    public void cancelFlow() {
        CustomerRegister customerRegister = presenter.getCustomerRegister();
        CustomerRegister customerRegisterClone = presenter.getCustomerRegisterClone();

        boolean isAcquisition = customerRegisterClone.getCustomerType() == ACQUISITION;
        boolean oneInteraction = registerCustomerPersonalFragment.haveInteraction();
        boolean twoInteraction = customerRegisterClone.getAddresses() != null && !customerRegisterClone.getAddresses().isEmpty();
        boolean threeInteraction = haveThreeInteraction();
        boolean fourInteraction = haveFourInteraction();
        boolean fiveInteraction = customerRegisterClone.getAttachments() != null && !customerRegisterClone.getAttachments().isEmpty();

        boolean isRegister = StringUtils.isEmpty(presenter.getCustomerRegister().getId());
        if (isRegister &&
                (isAcquisition && (oneInteraction || twoInteraction || threeInteraction || fourInteraction || fiveInteraction)
                        || !isAcquisition && (oneInteraction || twoInteraction || fiveInteraction))) {
            alertarSaida();
            return;
        }

        if (!isRegister && !customerRegister.equals(customerRegisterClone)) {
            alertarSaida();
            return;
        }

        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private boolean haveThreeInteraction() {
        CustomerRegister customerRegisterClone = presenter.getCustomerRegisterClone();
        return StringUtils.isNotEmpty(customerRegisterClone.getSgvCode())
                || customerRegisterClone.getBank() != null
                || StringUtils.isNotEmpty(customerRegisterClone.getBankAgency())
                || StringUtils.isNotEmpty(customerRegisterClone.getBankAgencyDigit())
                || StringUtils.isNotEmpty(customerRegisterClone.getBankAccount())
                || StringUtils.isNotEmpty(customerRegisterClone.getBankAccountDigit());
    }

    private boolean haveFourInteraction() {
        CustomerRegister customerRegisterClone = presenter.getCustomerRegisterClone();
        return customerRegisterClone.getForeseenRevenue() != null
                || StringUtils.isNotEmpty(customerRegisterClone.getRentalMachineDue())
                || customerRegisterClone.getExemption() != null
                || customerRegisterClone.getAnticipation() != null
                || customerRegisterClone.getDebitAutomatic() != null
                || customerRegisterClone.getPosList() != null && !customerRegisterClone.getPosList().isEmpty();
    }

    @Override
    public void moveToNextFragment() {
        CustomArrowNavigationItem selectedItem = cptBottomBar.getSelectedItem();
        RegisterCustomerCommon fragment = (RegisterCustomerCommon) selectedItem.getTarget();
        if (fragment == null) return;
        fragment.persistData();
    }

    @Override
    public void stepValidated() {
        this.openNextFragmentWithAnimation(cptBottomBar.getTargetToNext());
    }

    @Override
    public void doComplete() {
        presenter.saveCustomerRegister();
    }

    private void initializeFragments() {
        registerCustomerPersonalFragment = RegisterCustomerPersonalFragment.newInstance();
        registerCustomerPartnersFragment = RegisterCustomerPartnersFragment.newInstance();
        registerCustomerAddressListFragment = RegisterCustomerAddressListFragment.newInstance();
        registerCustomerFinancialFragment = RegisterCustomerFinancialFragment.newInstance();
        registerCustomerCommercialFragment = RegisterCustomerCommercialFragment.newInstance();
        registerCustomerAttachmentFragment = RegisterCustomerAttachmentFragment.newInstance();
        registerCustomerDadosECFragment = RegisterCustomerDadosECFragment.newInstance();
        registerCustomerDadosContatoFragment = RegisterCustomerDadosContatoFragment.newInstance();
    }

    private void initializeFlow() {
        initializeFacadeAttachment();

        prospectMdcIdExtra = getIntent().getIntExtra(FLAG_PROSPECT_MDR, NEGATIVE_SINGLE_INT);

        if (prospectMdcIdExtra >= SINGLE_INT) {
            presenter.getProspectValue(prospectMdcIdExtra);
        }

        int prospectIdExtra = getIntent().getIntExtra(FLAG_PROSPECT, NEGATIVE_SINGLE_INT);
        if (prospectIdExtra != NEGATIVE_SINGLE_INT) {
            presenter.getCustomerByProspect(prospectIdExtra);
            return;
        }

        int customerRegisterIdExtra = getIntent().getIntExtra(FLAG_CUSTOMER, NEGATIVE_SINGLE_INT);
        if (customerRegisterIdExtra != NEGATIVE_SINGLE_INT) {
            presenter.getCustomerByCustomerRegister(customerRegisterIdExtra);
            return;
        }

        String customerIdExtra = getIntent().getStringExtra(Config.CodigoRecadastro);
        if (StringUtils.isNotEmpty(customerIdExtra)) {
            presenter.getCustomerByCustomer(customerIdExtra);
            return;
        }

        // Inicializar Carregar dados do PID
        int idSolicicaoPid = getIntent().getIntExtra("IDSolicitacaoPid", NEGATIVE_SINGLE_INT);
        if (idSolicicaoPid != NEGATIVE_SINGLE_INT) {
            presenter.getCustomerBySolicitacaoPid(idSolicicaoPid);
            return;
        }

        cptBottomBar.initializeFirstFragment();
    }

    private void initializeFacadeAttachment() {
        String facadeAttachmentString = getIntent().getStringExtra(FLAG_FACADE_ATTACHMENT);
        if (facadeAttachmentString == null) {
            return;
        }

        CustomerRegisterAttachment facadeAttachment = Utilidades.getClassFromJson(facadeAttachmentString, CustomerRegisterAttachment.class);
        if (facadeAttachment == null) {
            return;
        }

        List<CustomerRegisterAttachment> attachments = new ArrayList<>();
        facadeAttachment.setActivated(true);
        attachments.add(facadeAttachment);
        CustomerRegister customerRegister = presenter.getCustomerRegister();
        customerRegister.setAttachments(attachments);
    }

    private void alertarSaida() {
        Alerta alerta = new Alerta(
                this,
                getString(R.string.customer_register_personal_alerta_cancelar_titulo),
                getString(R.string.customer_register_personal_alerta_cancelar_mensagem)
        );
        alerta.showConfirm(
                (dialog, which) -> {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                },
                null
        );
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backPublish.onNext(StringUtils.EMPTY_STRING);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void createEvents() {
        Disposable disposable = backPublish
                .throttleFirst(NumberUtils.FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(value -> onBackPressed(), Timber::e);
        disposables.add(disposable);
    }

    public interface RegisterCustomerActivityListener {
        void onBackPressed();
    }
}
