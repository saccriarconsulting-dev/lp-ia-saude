package com.axys.redeflexmobile.ui.register.customer.dadosec;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_MCC_DADOSEC;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_SEGMENTO_DADOSEC;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterHorarioFunc;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;
import com.axys.redeflexmobile.ui.register.customer.dadosec.horariofunc.RegisterCustomerHorarioFuncFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

public class RegisterCustomerDadosECFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerDadosECView,
        RegisterCustomerDadosECHorarioFuncViewHolder.RegisterCustomerDadosECHorarioFuncViewHolderListener {

    @Inject RegisterCustomerDadosECPresenter presenter;
    @Inject RegisterCustomerDadosECHorarioFuncAdapter horarioAdapter;

    @BindView(R.id.dadosec_register_scroll_view) ScrollView scrollView;
    @BindView(R.id.dadosec_register_et_codigoSGV) CustomEditText etCodigoSGV;
    @BindView(R.id.dadosec_register_spn_MCC) CustomSpinner spnMCC;
    @BindView(R.id.dadosec_register_spn_Segmento) CustomSpinner spnSegmento;
    @BindView(R.id.dadosec_register_ll_add_horarioFunc) LinearLayout llAddHorario;
    @BindView(R.id.dadosec_register_rv_horarioFunc_list) RecyclerView rvHorarioFuncionamento;
    @BindView(R.id.dadosec_register_tv_title_number) TextView txtNumeroTitulo;

    private CompositeDisposable compositeDisposable;
    List<CustomerRegisterHorarioFunc> listaHorarios;

    public static RegisterCustomerDadosECFragment newInstance() {
        return new RegisterCustomerDadosECFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_dadosec;
    }

    @Override
    public void initialize() {
        presenter.attachView(this);
        presenter.getSegmento();
        presenter.getMcc();

        rvHorarioFuncionamento.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHorarioFuncionamento.setAdapter(horarioAdapter);
        compositeDisposable = new CompositeDisposable();

        presenter.initializeData();
        llAddHorario.setOnClickListener(view -> onHorarioAtendimentoClick());
        scrollView.post(() -> scrollView.scrollTo(EMPTY_INT, EMPTY_INT));
    }

    private void onHorarioAtendimentoClick() {
        parentActivity.getCustomerRegister().setSgvCode(etCodigoSGV.getText());
        parentActivity.getCustomerRegister().setMcc(spnMCC.getSelectedItemId());
        parentActivity.getCustomerRegister().setSegment(spnSegmento.getSelectedItemId());

        // Abre a tela de Horários
        parentActivity.openFragmentWithoutBottomBar(RegisterCustomerHorarioFuncFragment.newInstance());
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
    public void carregaLista_Mcc(List<ICustomSpinnerDialogModel> list) {
        spnMCC.setList(list);
    }

    @Override
    public void carregaLista_Segmento(List<ICustomSpinnerDialogModel> list) {
        spnSegmento.setList(list);
    }

    @Override
    public void initializeFieldValues(CustomerRegister customerRegister) {
        etCodigoSGV.setText(customerRegister.getSgvCode());
        spnMCC.doSelectWithCallback(customerRegister.getMcc());
        if (customerRegister.getMcc() != null)
        {
            if (customerRegister.getCustomerType().getIdValue() == EnumRegisterPersonType.JURIDICAL.getIdValue())
                spnMCC.setEnabled(false);
            else
                spnMCC.setEnabled(true);
        }
        else
            spnMCC.setEnabled(true);
        spnSegmento.doSelectWithCallback(customerRegister.getSegment());
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) return;
        moveFocusToFirstError(errors.get(EMPTY_INT));

        if (errors == null || errors.isEmpty()) return;
        if (errors.contains(SPN_MCC_DADOSEC)) spnMCC.showError();
        if (errors.contains(SPN_SEGMENTO_DADOSEC)) spnSegmento.showError();
        moveFocusToFirstError(errors.get(EMPTY_INT));
    }

    @Override
    public void onValidationSuccess() {
        parentActivity.stepValidated();
    }

    @Override
    public void onValidationSuccessBack() {
        parentActivity.stepValidatedBack();
    }

    private void moveFocusToFirstError(EnumRegisterFields error) {
        int margin = (int) getResources().getDimension(R.dimen.spacing_normal);
        switch (error) {
            case SPN_MCC_DADOSEC:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnMCC.getY() - margin);
                break;
            case SPN_SEGMENTO_DADOSEC:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnSegmento.getY() - margin);
                break;
        }
    }

    @Override
    public void persistData() {
        presenter.doSave(getStepRequest());
    }

    @Override
    public void persistCloneData() {
        presenter.saveData(getStepRequest(), true);
    }

    private CustomerRegister getStepRequest() {
        CustomerRegister customerRegister = new CustomerRegister();
        customerRegister.setSgvCode(etCodigoSGV.getText());
        customerRegister.setMcc(spnMCC.getSelectedItemId());
        customerRegister.setSegment(spnSegmento.getSelectedItemId());

        // Seta Horario de Funcionamento
        customerRegister.setHorarioFunc((ArrayList<CustomerRegisterHorarioFunc>) listaHorarios);

        return customerRegister;
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public void fillHorarioFuncList(List<CustomerRegisterHorarioFunc> horarioFuncList) {
        listaHorarios = horarioFuncList;
        horarioAdapter.setList(listaHorarios);
    }

    @Override
    public void setNumeroTitulo(String pNumero) {
        txtNumeroTitulo.setText(pNumero);
    }
}
