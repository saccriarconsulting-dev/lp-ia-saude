package com.axys.redeflexmobile.ui.register.customer.contato;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CONTATOPRINC_CELULAR;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CONTATOPRINC_EMAIL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CONTATOPRINC_NOME;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_MCC_DADOSEC;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_SEGMENTO_DADOSEC;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContato;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContatoPrincipal;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

public class RegisterCustomerDadosContatoFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerDadosContatoView,
        RegisterCustomerDadosContatoViewHolder.RegisterCustomerDadosContatoViewHolderListener {

    @Inject
    RegisterCustomerDadosContatoPresenter presenter;
    @Inject
    RegisterCustomerDadosContatoAdapter contatoAdapter;

    @BindView(R.id.dadocontato_register_scroll_view)
    ScrollView scrollView;
    @BindView(R.id.dadocontato_register_et_nomeContato)
    CustomEditText etNomeContato;
    @BindView(R.id.dadocontato_register_et_Celular)
    CustomEditText etCelular;
    @BindView(R.id.dadocontato_register_btn_Adicionar)
    Button btnAddContato;
    @BindView(R.id.dadocontato_register_rv_contato_list)
    RecyclerView rvContatos;
    @BindView(R.id.dadocontato_register_tv_title_number)
    TextView txtNumeroTitulo;
    @BindView(R.id.dadocontato_register_tv_title)
    TextView txtTitulo;

    // Dados Contato Principal
    @BindView(R.id.dadocontato_register_llContatoPrincipal)
    LinearLayout llContatoPrincipal;
    @BindView(R.id.dadocontato_register_et_nomeContatoPrincipal)
    CustomEditText etNomeContatoPrincipal;
    @BindView(R.id.dadocontato_register_et_CelularPrincipal)
    CustomEditText etCelularPrincipal;
    @BindView(R.id.dadocontato_register_et_TelefonePrincipal)
    CustomEditText etTelefonePrincipal;
    @BindView(R.id.dadocontato_register_et_EmailPrincipal)
    CustomEditText etEmailPrincipal;

    List<CustomerRegisterContato> listaDados = new ArrayList<>();

    private CompositeDisposable compositeDisposable;

    public static RegisterCustomerDadosContatoFragment newInstance() {
        return new RegisterCustomerDadosContatoFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_contato;
    }

    @Override
    public void initialize() {
        presenter.attachView(this);
        rvContatos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContatos.setAdapter(contatoAdapter);
        compositeDisposable = new CompositeDisposable();

        presenter.initializeData();
        btnAddContato.setOnClickListener(view -> onContatoAdicionarClick());
        scrollView.post(() -> scrollView.scrollTo(EMPTY_INT, EMPTY_INT));
    }

    private void onContatoAdicionarClick() {
        if (StringUtils.isEmpty(etNomeContato.getText())) {
            Mensagens.mensagemErro(requireContext(),"Alerta", "Nome Contato não Informado. Verifique!", false);
            return;
        }

        if (StringUtils.isEmpty(etCelular.getText())) {
            Mensagens.mensagemErro(requireContext(), "Alerta","Celular não Informado. Verifique!", false);
            return;
        } else if (!StringUtils.isCellphone(etCelular.getText())) {
            Mensagens.mensagemErro(requireContext(), "Alerta","Celular Incorreto. Verifique!", false);
            return;
        }

        if (listaDados.size() >= 2) {
            Mensagens.mensagemErro(requireContext(),"Alerta", "Só poderão ser informados 2 Contatos de refeferência", false);
            return;
        }

        CustomerRegisterContato customerRegisterContato = new CustomerRegisterContato();
        customerRegisterContato.setNome(etNomeContato.getText());
        customerRegisterContato.setCelular(etCelular.getText());
        listaDados.add(customerRegisterContato);
        fillDadosContatoList(listaDados);

        etNomeContato.setText(null);
        etCelular.setText(null);
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
    public void onPosRemove(int position) {
        listaDados.remove(position);
        fillDadosContatoList(listaDados);
    }

    @Override
    public void initializeFieldValues(CustomerRegister customerRegister) {
        etNomeContato.setText(null);
        etCelular.setText(null);

        // No caso de Pessoa Juridica o Contato PRincipal não é informado, são coletadas as informações de Dados de Socio
        if ((customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.ACQUISITION.getIdValue() || customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.SUBADQUIRENCIA.getIdValue()) &&
                customerRegister.getPersonType().getIdValue() == EnumRegisterPersonType.JURIDICAL.getIdValue())
            llContatoPrincipal.setVisibility(View.GONE);
        else
            llContatoPrincipal.setVisibility(View.VISIBLE);

        if (customerRegister.getContatoPrincipal() != null) {
            if (!Util_IO.isNullOrEmpty(customerRegister.getContatoPrincipal().getNome()))
                etNomeContatoPrincipal.setText(customerRegister.getContatoPrincipal().getNome());

            if (!Util_IO.isNullOrEmpty(customerRegister.getContatoPrincipal().getCelular()))
                etCelularPrincipal.setText(customerRegister.getContatoPrincipal().getCelular());

            if (!Util_IO.isNullOrEmpty(customerRegister.getContatoPrincipal().getTelefone()))
                etTelefonePrincipal.setText(customerRegister.getContatoPrincipal().getTelefone());

            if (!Util_IO.isNullOrEmpty(customerRegister.getContatoPrincipal().getEmail()))
                etEmailPrincipal.setText(customerRegister.getContatoPrincipal().getEmail());
        }

        fillDadosContatoList(customerRegister.getContatos());
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) return;

        if (errors.contains(ET_CONTATOPRINC_NOME))
            etNomeContatoPrincipal.showError();

        if (errors.contains(ET_CONTATOPRINC_EMAIL))
            etEmailPrincipal.showError();

        if (errors.contains(ET_CONTATOPRINC_CELULAR))
            etCelularPrincipal.showError();

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
            case ET_CONTATOPRINC_NOME:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etNomeContatoPrincipal.getY() - margin);
                break;
            case ET_CONTATOPRINC_CELULAR:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etCelularPrincipal.getY() - margin);
                break;
            case ET_EMAIL:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etEmailPrincipal.getY() - margin);
                break;
        }
    }

    @Override
    public void fillDadosContatoList(List<CustomerRegisterContato> dadosContatoList) {
        contatoAdapter.setList(dadosContatoList);
    }

    @Override
    public void setNumeroTitulo(String pNumero) {
        txtNumeroTitulo.setText(pNumero);
    }

    @Override
    public void setTitulo(String ptitulo) {
        txtTitulo.setText(ptitulo);
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
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
        // Armazena os dados de Contatos
        customerRegister.setContatos((ArrayList<CustomerRegisterContato>) listaDados);

        // Armazena os dados do Contato Principal
        CustomerRegisterContatoPrincipal customerRegisterContatoPrincipal = new CustomerRegisterContatoPrincipal();
        customerRegisterContatoPrincipal.setNome(etNomeContatoPrincipal.getText());
        customerRegisterContatoPrincipal.setCelular(etCelularPrincipal.getText());
        customerRegisterContatoPrincipal.setTelefone(etTelefonePrincipal.getText());
        customerRegisterContatoPrincipal.setEmail(etEmailPrincipal.getText());
        customerRegister.setContatoPrincipal(customerRegisterContatoPrincipal);

        return customerRegister;
    }
}
