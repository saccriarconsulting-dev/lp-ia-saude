package com.axys.redeflexmobile.ui.register.customer.partners;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_CELULAR;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_CPF;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_DATANASCIMENTO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_EMAIL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_NOME;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_PATRIMONIO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_PROFISSAO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_RENDA;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_TELEFONE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterPartners;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RegisterCustomerPartnersFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerPartnersView {

    @Inject RegisterCustomerPartnersPresenter presenter;

    @BindView(R.id.partners_register_scroll_view) ScrollView scrollView;
    @BindView(R.id.partners_register_et_nome_Socio) CustomEditText etNomeSocio;
    @BindView(R.id.partners_register_et_DataNascimento) CustomEditText etDataNascimento;
    @BindView(R.id.partners_register_et_Cpf) CustomEditText etCPF;
    @BindView(R.id.partners_register_spn_profissao) CustomSpinner spnProfissao;
    @BindView(R.id.partners_register_spn_renda) CustomSpinner spnRenda;
    @BindView(R.id.partners_register_spn_patrimonio) CustomSpinner spnPatrimonio;
    @BindView(R.id.partners_register_et_email) CustomEditText etEmail;
    @BindView(R.id.partners_register_et_Celular) CustomEditText etCelular;
    @BindView(R.id.partners_register_et_Telefone) CustomEditText etTelefone;
    @BindView(R.id.partners_register_tv_title_number) TextView txtNumeroTitulo;


    public static RegisterCustomerPartnersFragment newInstance() {
        return new RegisterCustomerPartnersFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_partners;
    }

    @Override
    public void initialize() {
        presenter.attachView(this);
        presenter.getProfissoes();
        presenter.getRenda();
        presenter.getPatrimonio();
        scrollView.post(() -> scrollView.scrollTo(EMPTY_INT, EMPTY_INT));
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
    public void initializeFieldValues(CustomerRegisterPartners customerRegister, String pNomeSocio ) {
        try {
            if (!StringUtils.isEmpty(customerRegister.getNome()))
                etNomeSocio.setText(customerRegister.getNome());
            else
                etNomeSocio.setText(pNomeSocio);

            if (customerRegister.getDataNascimento() != null)
                etDataNascimento.setText(Util_IO.dateToStringBr(customerRegister.getDataNascimento()));

            etCPF.setText(customerRegister.getCPF());
            //spnProfissao.doSelectWithCallback(customerRegister.getIdProfissao());
            //spnRenda.doSelectWithCallback(customerRegister.getIdRenda());
            //spnPatrimonio.doSelectWithCallback(customerRegister.getIdProfissao());
            etEmail.setText(customerRegister.getEmail());
            etCelular.setText(customerRegister.getCelular());
            etTelefone.setText(customerRegister.getTelefone());

            spnProfissao.hideVisibility();
            spnRenda.hideVisibility();
            spnPatrimonio.hideVisibility();
        }
        catch (Exception ex)
        {
            Log.d("Roni", "initializeFieldValues: " + ex.getMessage());
        }
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) return;
        if (errors.contains(ET_SOCIO_NOME)) etNomeSocio.showError();
        if (errors.contains(ET_SOCIO_DATANASCIMENTO)) etDataNascimento.showError();
        if (errors.contains(ET_SOCIO_CPF)) etCPF.showError();
        if (errors.contains(ET_SOCIO_PROFISSAO)) spnProfissao.showError();
        if (errors.contains(ET_SOCIO_RENDA)) spnRenda.showError();
        if (errors.contains(ET_SOCIO_PATRIMONIO)) spnPatrimonio.showError();
        if (errors.contains(ET_SOCIO_EMAIL)) etEmail.showError();
        if (errors.contains(ET_SOCIO_CELULAR)) etCelular.showError();
        if (errors.contains(ET_SOCIO_TELEFONE)) etTelefone.showError();
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

    @Override
    public void setNumeroTitulo(String pNumero) {
        txtNumeroTitulo.setText(pNumero);
    }

    @Override
    public void persistData() {
        presenter.doSave(getStepRequest());
    }

    @Override
    public void persistCloneData() {
        presenter.saveData(getStepRequest(), true);
    }

    private CustomerRegisterPartners getStepRequest() {
        CustomerRegisterPartners customerRegisterPartners = new CustomerRegisterPartners();
        customerRegisterPartners.setNome(etNomeSocio.getText());
        customerRegisterPartners.setDataNascimento(Util_IO.stringToDateBr(etDataNascimento.getText()));
        customerRegisterPartners.setCPF(etCPF.getText());
        //customerRegisterPartners.setIdProfissao(spnProfissao.getSelectedItemId());
        //customerRegisterPartners.setIdRenda(spnRenda.getSelectedItemId());
        //customerRegisterPartners.setIdPatrimonio(spnPatrimonio.getSelectedItemId());
        customerRegisterPartners.setEmail(etEmail.getText());
        customerRegisterPartners.setTelefone(etTelefone.getText());
        customerRegisterPartners.setCelular(etCelular.getText());
        customerRegisterPartners.setTipoSocio(0);
        return customerRegisterPartners;
    }

    private void moveFocusToFirstError(EnumRegisterFields error) {
        int margin = (int) getResources().getDimension(R.dimen.spacing_normal);
        switch (error) {
            case ET_SOCIO_NOME:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etNomeSocio.getY() - margin);
                break;
            case ET_SOCIO_DATANASCIMENTO:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etDataNascimento.getY() - margin);
                break;
            case ET_SOCIO_CPF:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etCPF.getY() - margin);
                break;
            case ET_SOCIO_PROFISSAO:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnProfissao.getY() - margin);
                break;
            case ET_SOCIO_RENDA:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnRenda.getY() - margin);
                break;
            case ET_SOCIO_PATRIMONIO:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnPatrimonio.getY() - margin);
                break;
            case ET_SOCIO_EMAIL:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etEmail.getY() - margin);
                break;
            case ET_SOCIO_CELULAR:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etCelular.getY() - margin);
                break;
            case ET_SOCIO_TELEFONE:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etTelefone.getY() - margin);
                break;
        }
    }
}