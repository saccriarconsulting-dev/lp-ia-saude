package com.axys.redeflexmobile.ui.clientemigracao.register.personal;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterSexo;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.migracao.CadastroMigracaoSubHorario;
import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.clientemigracao.dialog.RegisterMotiveMigrationCancelDialog;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationCommonImpl;
import com.axys.redeflexmobile.ui.clientemigracao.register.personal.horariofunc.RegisterMigrationHorarioFuncFragment;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.jakewharton.rxbinding2.view.RxMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_ACCOUNT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_ACCOUNT_DIGIT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_AGENCY;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_AGENCY_DIGIT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_DATA_FUNDACAO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_EMAIL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FIRST_PHONE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_RG;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_ACCOUNT_TYPE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_BANK;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_PATRIMONIO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_PROFISSAO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_RENDA;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_SEXO;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

/**
 * @author Lucas Marciano on 02/04/2020.
 */

public class RegisterMigrationPersonalFragment extends RegisterMigrationCommonImpl implements
        RegisterMigrationPersonalView,
        RegisterMigrationPersonalHorarioFuncViewHolder.RegisterMigrationPersonalHorarioFuncViewHolderListener {

    @Inject RegisterMigrationPersonalPresenter presenter;
    @Inject RegisterMigrationPersonalHorarioFuncAdapter horarioAdapter;

    @BindView(R.id.migration_cet_email) CustomEditText etEmail;
    @BindView(R.id.migration_cet_phone) CustomEditText etPhone;
    @BindView(R.id.migration_cet_agency) CustomEditText etAgency;
    @BindView(R.id.migration_cet_digit_agency) CustomEditText etAgencyDigit;
    @BindView(R.id.migration_cet_count) CustomEditText etCount;
    @BindView(R.id.migration_cet_digit_count) CustomEditText etCountDigit;
    @BindView(R.id.migration_cs_type_count) CustomSpinner spCountType;
    @BindView(R.id.migration_cs_bank) CustomSpinner spBank;
    @BindView(R.id.migration_sv_content) ScrollView scrollView;
    @BindView(R.id.migration_cs_profissao) CustomSpinner spnProfissao;
    @BindView(R.id.migration_cs_rendamensal) CustomSpinner spnRenda;
    @BindView(R.id.migration_cs_patrimonio) CustomSpinner spnPatrimonio;
    @BindView(R.id.migration_cs_sexo) CustomSpinner spnSexo;
    @BindView(R.id.migration_cet_rg) CustomEditText etRG;
    @BindView(R.id.migration_cet_datafundacao) CustomEditText etDataFundacaoPF;
    @BindView(R.id.migration_ll_add_horarioFunc) LinearLayout llAddHorario;
    @BindView(R.id.migration_rv_horarioFunc_list) RecyclerView rvHorarioFuncionamento;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int FROZEN_DURATION = 2000;

    List<CadastroMigracaoSubHorario> listaHorarios;

    public static RegisterMigrationPersonalFragment newInstance() {
        return new RegisterMigrationPersonalFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_migration_register_personal;
    }

    @Override
    public void initialize() {
        presenter.attachView(this);
        rvHorarioFuncionamento.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHorarioFuncionamento.setAdapter(horarioAdapter);
        presenter.loadDataBank();
        presenter.loadTypeCount();
        presenter.loadProfissoes();
        presenter.loadFaixaSalarial();
        presenter.loadPatrimonio();
        presenter.loadSexo();
        compositeDisposable = new CompositeDisposable();

        presenter.initializeData(parentActivity.getClientId());
        llAddHorario.setOnClickListener(view -> onHorarioAtendimentoClick());
        scrollView.post(() -> scrollView.scrollTo(EMPTY_INT, EMPTY_INT));
    }

    private void onHorarioAtendimentoClick() {
        parentActivity.recoverObjectMigration().setEmail(etEmail.getText());
        parentActivity.recoverObjectMigration().setTelefoneCelular(etPhone.getText());
        parentActivity.recoverObjectMigration().setRG(etRG.getText());
        parentActivity.recoverObjectMigration().setSexo(spnSexo.getSelectedItemId() != null ?
                EnumRegisterSexo.getEnumByIdValue(spnSexo.getSelectedItemId())
                : null);
        parentActivity.recoverObjectMigration().setIdProfissao(spnProfissao.getSelectedItemId());
        parentActivity.recoverObjectMigration().setIdRendaMensal(spnRenda.getSelectedItemId());
        parentActivity.recoverObjectMigration().setIdPatrimonio(spnPatrimonio.getSelectedItemId());

        if (DateUtils.isDateValid(etDataFundacaoPF.getText(), true, 1900)) {
            parentActivity.recoverObjectMigration().setDataFundacaoPF(Util_IO.stringToDateBr(etDataFundacaoPF.getText()));
        }
        parentActivity.recoverObjectMigration().setTipoMigracao(parentActivity.getTipoMigracao());

        // Abre a tela de Horários
        parentActivity.openFragmentWithoutBottomBar(RegisterMigrationHorarioFuncFragment.newInstance());
    }
    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
        presenter.clearDispose();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register_migration_taxes, menu);
        MenuItem refuseItem = menu.findItem(R.id.action_refuse);
        compositeDisposable.add(
                RxMenuItem.clicks(refuseItem)
                        .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(unused -> presenter.loadMotivesCancelMigration(), Timber::e)
        );
    }

    @Override
    public void callModalMotivesCancelMigration(List<MotiveMigrationSub> motiveMigrationSubs) {
        final RegisterMotiveMigrationCancelDialog dialog = RegisterMotiveMigrationCancelDialog
                .newInstance(presenter::saveMigrationCancelMotive, motiveMigrationSubs);
        dialog.show(getChildFragmentManager(), RegisterMotiveMigrationCancelDialog.class.getName());
    }

    @Override
    public void showResponseMotivesCancelMigration() {
        if (getActivity() == null) {
            return;
        }

        Toast.makeText(
                getActivity(),
                R.string.message_success_cancel_migration,
                Toast.LENGTH_LONG
        ).show();

        getActivity().finish();
    }

    @Override
    public void persistData() {
        clearErrors();
        RegisterMigrationSub request = getViewValues();
        if (request != null) {
            presenter.doSave(request);
        }
    }

    @Override
    public void persistCloneData() {
        RegisterMigrationSub request = getViewValues();
        if (request != null) {
            presenter.finalizeFlow(request, true);
        }
    }

    @Override
    public void fillSpinnerTypeCount(List<ICustomSpinnerDialogModel> list) {
        spCountType.setList(list);
    }

    @Override
    public void fillSpinnerBank(List<ICustomSpinnerDialogModel> list) {
        spBank.setList(list);
    }

    @Override
    public void fillProfissoes(List<ICustomSpinnerDialogModel> profissoes) {
        spnProfissao.setList(profissoes);
    }

    @Override
    public void fillRenda(List<ICustomSpinnerDialogModel> renda) {
        spnRenda.setList(renda);
    }

    @Override
    public void fillPatrimonio(List<ICustomSpinnerDialogModel> patrimonio) {
        spnPatrimonio.setList(patrimonio);
    }

    @Override
    public void fillSexo(List<ICustomSpinnerDialogModel> list) {
        spnSexo.setList(list);
    }

    @Override
    public void initializeInterface(Cliente client,
                                    @Nullable ICustomSpinnerDialogModel countType,
                                    @Nullable ICustomSpinnerDialogModel bank) {
        clearErrors();
        if (countType != null)
            spCountType.doSelect(countType);
        if (bank != null)
            spBank.doSelect(bank);
        etEmail.setText(client.getEmailSub());
        etPhone.setText(client.getTelefoneSub());
        if (StringUtils.isNotEmpty(client.getAgencia()))
            etAgency.setText(client.getAgencia());
        etAgencyDigit.setText(client.getDigitoAgencia());
        etCount.setText(client.getConta());
        etCountDigit.setText(client.getDigitoConta());
        presenter.fillMigration(parentActivity.recoverObjectMigration());

        if (parentActivity.getTipoMigracao().equals("ADQ")) {
            etAgency.setVisibility(View.GONE);
            etAgencyDigit.setVisibility(View.GONE);
            etCount.setVisibility(View.GONE);
            etCountDigit.setVisibility(View.GONE);
            spBank.setVisibility(View.GONE);
            spCountType.setVisibility(View.GONE);

            if (client.getCpf_cnpj().length()>11)
            {
                spnSexo.setVisibility(View.GONE);
                etRG.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) {
            return;
        }
        if (errors.contains(ET_EMAIL)) {
            etEmail.showError();
        }
        if (errors.contains(ET_FIRST_PHONE)) {
            etPhone.showError();
        }
        if (errors.contains(ET_BANK_AGENCY)) {
            etAgency.showError();
        }
        if (errors.contains(ET_BANK_AGENCY_DIGIT)) {
            etAgencyDigit.showError();
        }
        if (errors.contains(ET_BANK_ACCOUNT)) {
            etCount.showError();
        }
        if (errors.contains(ET_BANK_ACCOUNT_DIGIT)) {
            etCountDigit.showError();
        }
        if (errors.contains(SPN_ACCOUNT_TYPE)) {
            spCountType.showError();
        }
        if (errors.contains(SPN_BANK)) {
            spBank.showError();
        }
        if (errors.contains(SPN_SEXO)) {
            spnSexo.showError();
        }
        if (errors.contains(ET_RG)) {
            etRG.showError();
        }
        if (errors.contains(SPN_PROFISSAO)) {
            spnProfissao.showError();
        }
        if (errors.contains(SPN_RENDA)) {
            spnRenda.showError();
        }
        if (errors.contains(SPN_PATRIMONIO)) {
            spnPatrimonio.showError();
        }
        if (errors.contains(ET_DATA_FUNDACAO)) {
            etDataFundacaoPF.showError();
        }
    }

    @Override
    public void onValidationSuccess(RegisterMigrationSub request) {
        parentActivity.saveObjectMigration(request);
        parentActivity.stepValidated();
    }

    @Override
    public void onValidationSuccessBack() {
        parentActivity.cancelFlow();
    }

    @Override
    public void fillMigrationFields(final RegisterMigrationSub migrationSub) {
        etEmail.setText(migrationSub.getEmail());
        etPhone.setText(migrationSub.getTelefoneCelular());
        etAgency.setText(migrationSub.getAgencia());
        etAgencyDigit.setText(migrationSub.getDigitoAgencia());
        etCount.setText(migrationSub.getConta());
        etCountDigit.setText(migrationSub.getDigitoConta());
        spBank.doSelectWithCallback(migrationSub.getIdBanco());
        spCountType.doSelectWithCallback(migrationSub.getTipoConta());

        spnSexo.doSelect(migrationSub.getSexo() != null ? migrationSub.getSexo() : null);
        etRG.setText(migrationSub.getRG());
        if (migrationSub.getIdProfissao() != null)
            spnProfissao.doSelectWithCallback(migrationSub.getIdProfissao());
        if (migrationSub.getIdRendaMensal() != null)
            spnRenda.doSelectWithCallback(migrationSub.getIdRendaMensal());
        if (migrationSub.getIdPatrimonio() != null)
            spnPatrimonio.doSelectWithCallback(migrationSub.getIdPatrimonio());
        if (migrationSub.getDataFundacaoPF() != null)
            etDataFundacaoPF.setText(Util_IO.dateToStringBr(migrationSub.getDataFundacaoPF()));

        // Carrega Dados Horario Funcionamento
        fillHorarioFuncList(parentActivity.recoverObjectMigration().getHorarioFuncionamento());
    }

    private void clearErrors() {
        etEmail.hideError();
        etPhone.hideError();
        etAgency.hideError();
        etAgencyDigit.hideError();
        etCount.hideError();
        etCountDigit.hideError();
        spCountType.hideError();
        spBank.hideError();
        spnSexo.hideError();
        etRG.hideError();
        spnProfissao.hideError();
        spnRenda.hideError();
        spnPatrimonio.hideError();
        etDataFundacaoPF.hideError();
    }

    private RegisterMigrationSub getViewValues() {
        RegisterMigrationSub request = new RegisterMigrationSub();
        GPSTracker gpsTracker = new GPSTracker(parentActivity);
        request.setEmail(etEmail.getText().trim());
        request.setTelefoneCelular(StringUtils.returnOnlyNumbers(etPhone.getText()));
        request.setAgencia(etAgency.getText());
        request.setDigitoAgencia(etAgencyDigit.getText());
        request.setConta(etCount.getText());
        request.setDigitoConta(etCountDigit.getText());
        request.setTipoConta(spCountType.getSelectedItemId());
        request.setIdBanco(spBank.getSelectedItemId());
        request.setRG(etRG.getText());
        request.setSexo(spnSexo.getSelectedItemId() != null
                ? EnumRegisterSexo.getEnumByIdValue(spnSexo.getSelectedItemId())
                : null);
        request.setIdProfissao(spnProfissao.getSelectedItemId());
        request.setIdRendaMensal(spnRenda.getSelectedItemId());
        request.setIdPatrimonio(spnPatrimonio.getSelectedItemId());

        if (DateUtils.isDateValid(etDataFundacaoPF.getText(), true, 1900)) {
            request.setDataFundacaoPF(Util_IO.stringToDateBr(etDataFundacaoPF.getText()));
        }
        request.setTipoMigracao(parentActivity.getTipoMigracao());

        // Seta Horario de Funcionamento
        request.setHorarioFuncionamento((ArrayList<CadastroMigracaoSubHorario>) listaHorarios);
        if (gpsTracker.isGPSEnabled) {
            request.setLatitude(gpsTracker.getLatitude());
            request.setLongitude(gpsTracker.getLongitude());
            request.setPrecisao(gpsTracker.getPrecisao());
            return request;
        } else {
            gpsTracker.showSettingsAlert();
            return null;
        }
    }

    @Override
    public void fillHorarioFuncList(List<CadastroMigracaoSubHorario> horarioFuncList) {
        listaHorarios = horarioFuncList;
        horarioAdapter.setList(listaHorarios);
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }
}
