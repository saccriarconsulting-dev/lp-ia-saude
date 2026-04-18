package com.axys.redeflexmobile.ui.clientemigracao.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.util.NumberUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.clientemigracao.register.personal.RegisterMigrationPersonalFragment;
import com.axys.redeflexmobile.ui.clientemigracao.register.proposal.RegisterMigrationProposalFragment;
import com.axys.redeflexmobile.ui.clientemigracao.register.taxes.RegisterMigrationTaxesFragment;
import com.axys.redeflexmobile.ui.component.ComponentProgressLoading;
import com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigation;
import com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigation.CustomArrowNavigationListener;
import com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigationItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.util.NumberUtils.SINGLE_INT;

/**
 * @author lucasmarciano on 02/04/20
 */
@SuppressLint("NonConstantResourceId")
public class RegisterMigrationActivity extends BaseActivity
        implements RegisterMigrationView, CustomArrowNavigationListener {

    public static final String CLIENT_ID_KEY = "CLIENT_ID_KEY";
    public static final int TIMEOUT_BACK = 300;

    @BindView(R.id.migration_arrow_navigation) CustomArrowNavigation cptBottomBar;
    @BindView(R.id.migration_cpl_loading) ComponentProgressLoading cptLoading;

    @Inject RegisterMigrationPresenter presenter;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private RegisterMigrationSub registerMigrationSub = new RegisterMigrationSub();

    private int clientId = -1;
    private String tipoMigracao = "";
    private RegisterMigrationPersonalFragment registerMigrationPersonalFragment;
    private RegisterMigrationProposalFragment registerMigrationProposalFragment;
    private RegisterMigrationTaxesFragment registerMigrationTaxesFragment;
    private final PublishSubject<String> backPublish = PublishSubject.create();

    @Override
    protected ComponentProgressLoading getComponentProgressLoading() {
        return cptLoading;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_migration_client_register;
    }

    @Override
    protected Integer getFrameLayoutId() {
        return R.id.fl_main_content;
    }

    @Override
    protected void initialize() {
        setTitle(R.string.title_clients_migration);
        showBackButtonToolbar();
        setupVariableBundle();
        initializeFragment();
        initializeFlow();
        setupEvents();
        cptBottomBar.setCallback(this);
        cptBottomBar.initializeFirstFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
        presenter.detach();
        cptBottomBar.detach();
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
            if (fragment instanceof RegisterMigrationActivityListener) {
                ((RegisterMigrationActivityListener) fragment).onBackPressed();
                return;
            }
        } catch (RuntimeException e) {
            Timber.d(e);
        }

        this.cptBottomBar.moveToPreviousFragment();
    }

    @Override
    public void saveObjectMigration(RegisterMigrationSub registerMigrationSub) {
        this.registerMigrationSub = new RegisterMigrationSub(registerMigrationSub);
    }

    @Override
    public RegisterMigrationSub recoverObjectMigration() {
        return registerMigrationSub;
    }

    @Override
    public void onSaveSuccess() {
        Toast.makeText(this, R.string.message_migration_success, Toast.LENGTH_LONG).show();
        setResult(Activity.RESULT_OK);
        finish();
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
    public void openFragmentWithoutBottomBar(Fragment fragment) {
        onKeyboardOpen();
        openNextFragmentWithAnimation(fragment);
    }

    @Override
    public void closeFragmentWithoutBottomBarFromPersonal() {
        openPreviousFragmentWithAnimation(RegisterMigrationPersonalFragment.newInstance());
        new Handler().postDelayed(this::onKeyboardClose, TIMEOUT_BACK);
    }

    @Override
    public void closeFragmentWithoutBottomBarFromProposal() {
        openPreviousFragmentWithAnimation(registerMigrationProposalFragment);
        new Handler().postDelayed(this::onKeyboardClose, TIMEOUT_BACK);
    }

    @Override
    public void closeFragmentWithoutBottomBarFromHorarioFunc() {
        openPreviousFragmentWithAnimation(registerMigrationPersonalFragment);
        new Handler().postDelayed(this::onKeyboardClose, TIMEOUT_BACK);
    }

    @Override
    public void initializeFlow() {
        List<CustomArrowNavigationItem> fragments = new ArrayList<>();
        fragments.add(new CustomArrowNavigationItem(registerMigrationPersonalFragment));
        fragments.add(new CustomArrowNavigationItem(registerMigrationProposalFragment));
        if (!tipoMigracao.equals("ADQ"))
            fragments.add(new CustomArrowNavigationItem(registerMigrationTaxesFragment));
        cptBottomBar.setListItems(fragments);
    }

    @Override
    public void moveToPreviousFragment() {
        CustomArrowNavigationItem selectedItem = cptBottomBar.getSelectedItem();
        RegisterMigrationCommon fragment = (RegisterMigrationCommon) selectedItem.getTarget();
        if (fragment == null) return;
        fragment.persistCloneData();
        this.cptBottomBar.moveToPreviousFragment();
    }

    @Override
    public void moveToNextFragment() {
        CustomArrowNavigationItem selectedItem = cptBottomBar.getSelectedItem();
        RegisterMigrationCommon fragment = (RegisterMigrationCommon) selectedItem.getTarget();
        if (fragment == null) return;
        fragment.persistData();
    }

    @Override
    public void doComplete() {
        presenter.saveMigration(registerMigrationSub);
    }

    @Override
    public int getClientId() {
        return clientId;
    }

    @Override
    public String getTipoMigracao() {
        return tipoMigracao;
    }

    @Override
    public void stepValidated() {
        this.openNextFragmentWithAnimation(cptBottomBar.getTargetToNext());
    }

    @Override
    public void stepValidatedBack() {
        this.openPreviousFragmentWithAnimation(cptBottomBar.getTargetToPrevious());
    }

    @Override
    public void cancelFlow() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void setupEvents() {
        Disposable disposable = backPublish
                .throttleFirst(NumberUtils.FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(value -> onBackPressed(), Timber::e);
        disposables.add(disposable);
    }

    /**
     * Get value in the bundle.
     */
    private void setupVariableBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            clientId = bundle.getInt(CLIENT_ID_KEY, -1);
            tipoMigracao = bundle.getString("TIPO_MIGRACAO", "");
        }
    }

    private void initializeFragment() {
        registerMigrationPersonalFragment = RegisterMigrationPersonalFragment.newInstance();
        registerMigrationProposalFragment = RegisterMigrationProposalFragment.newInstance();
        if (!tipoMigracao.equals("ADQ"))
            registerMigrationTaxesFragment = RegisterMigrationTaxesFragment.newInstance();
    }

    public interface RegisterMigrationActivityListener {
        void onBackPressed();
    }
}
