package com.axys.redeflexmobile.ui.dialog.cliente;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ProgressBar;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.AlertaHtml;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.base.BaseDaggerDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ClienteInfoDialog extends BaseDaggerDialog implements ClienteInfoView {

    public static final int BUTTON_DELAY_DURATION = 1800;
    private static final String CLIENTE_JSON = "CLIENTE_JSON";
    private static final String SENHA_JSON = "SENHA_JSON";

    @Inject ClienteInfoPresenter presenter;

    @BindView(R.id.cliente_info_btn_ok) AppCompatTextView btnPositive;
    @BindView(R.id.cliente_info_btn_solicitar) AppCompatTextView btnSolicitar;
    @BindView(R.id.cliente_info_tv_telefone_principal) AppCompatTextView tvTelefonePrincipal;
    @BindView(R.id.cliente_info_tv_telefone) AppCompatTextView tvTelefone;
    @BindView(R.id.cliente_info_tv_celular) AppCompatTextView tvCelular;
    @BindView(R.id.cliente_info_tv_celular_opcional) AppCompatTextView tvCelularOpcional;
    @BindView(R.id.cliente_info_tv_email) AppCompatTextView tvEmail;
    @BindView(R.id.cliente_info_tv_sem_dados) AppCompatTextView tvSemDados;

    @BindView(R.id.cliente_cb_contato_1) AppCompatCheckBox cbContato1;
    @BindView(R.id.cliente_cb_contato_2) AppCompatCheckBox cbContato2;
    @BindView(R.id.cliente_cb_contato_3) AppCompatCheckBox cbContato3;
    @BindView(R.id.cliente_cb_contato_4) AppCompatCheckBox cbContato4;
    @BindView(R.id.cliente_cb_contato_5) AppCompatCheckBox cbContato5;

    @BindView(R.id.cliente_info_pb_loading) ProgressBar pbLoading;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static ClienteInfoDialog newInstance(String clienteJson, String senhaJson) {
        ClienteInfoDialog baixaPosDialog = new ClienteInfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CLIENTE_JSON, clienteJson);
        bundle.putString(SENHA_JSON, senhaJson);
        baixaPosDialog.setArguments(bundle);

        return baixaPosDialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        List<Fragment> frags = manager.getFragments();
        Optional<Fragment> fragmento = Stream.of(frags)
                .filter(frag -> frag.getTag() != null && frag.getTag().equals(tag))
                .findFirst();

        if (fragmento.isEmpty() || !fragmento.get().isAdded()) {
            super.show(manager, tag);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_cliente_info;
    }

    @Override
    protected void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        criarEventos();
        popularTela();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void mostrarErro(String message) {
        if (getContext() == null) {
            return;
        }
        AlertaHtml alerta = new AlertaHtml(getContext(), getString(R.string.app_titulo), message);
        alerta.show((dialog, which) -> dismissAllowingStateLoss());
    }

    @Override
    public void mostrarErro(String title, String description) {
        if (getContext() == null) {
            return;
        }
        AlertaHtml alerta = new AlertaHtml(getContext(), title, description);
        alerta.show();
    }

    @Override
    public void mostrarSucesso(String title, String description) {
        if (getContext() == null) {
            return;
        }
        AlertaHtml alerta = new AlertaHtml(getContext(), title, description);
        alerta.show((dialog, which) -> dismissAllowingStateLoss());
    }

    @Override
    public void apresentarMensagemSemDados() {
        tvSemDados.setVisibility(View.VISIBLE);
        btnSolicitar.setVisibility(View.GONE);
    }

    @Override
    public void apresentarTelefonePrincipal(final String telefone) {
        atribuirTexto(
                tvTelefonePrincipal,
                cbContato1,
                "TELEFONE 1:",
                telefone
        );
    }

    @Override
    public void apresentarTelefone(final String telefone) {
        atribuirTexto(
                tvTelefone,
                cbContato2,
                "TELEFONE 2:",
                telefone
        );
    }

    @Override
    public void apresentarCelular(final String celular) {
        atribuirTexto(
                tvCelular,
                cbContato3,
                "CELULAR 1:",
                celular
        );
    }

    @Override
    public void apresentarCelularOpcional(final String celular) {
        atribuirTexto(
                tvCelularOpcional,
                cbContato4,
                "CELULAR 2:",
                celular
        );
    }

    @Override
    public void apresentarEmail(String email) {
        atribuirTexto(
                tvEmail,
                cbContato5,
                "E-MAIL:",
                email
        );
    }

    @Override
    public void showLoading() {
        desabilitarCheckbox();
        btnPositive.animate().alpha(0);
        btnSolicitar.animate().alpha(0);

        btnPositive.setVisibility(View.INVISIBLE);
        btnSolicitar.setVisibility(View.GONE);

        pbLoading.setVisibility(View.VISIBLE);
        pbLoading.animate().alpha(1);
    }

    @Override
    public void hideLoading() {
        habilitarCheckbox();
        pbLoading.animate().alpha(0);
        pbLoading.setVisibility(View.GONE);

        btnPositive.setVisibility(View.VISIBLE);
        btnSolicitar.setVisibility(View.VISIBLE);

        btnPositive.animate().alpha(1);
        btnSolicitar.animate().alpha(1);
    }

    private void desabilitarCheckbox() {
        cbContato1.setEnabled(false);
        cbContato2.setEnabled(false);
        cbContato3.setEnabled(false);
        cbContato4.setEnabled(false);
        cbContato5.setEnabled(false);
    }

    private void habilitarCheckbox() {
        cbContato1.setEnabled(true);
        cbContato2.setEnabled(true);
        cbContato3.setEnabled(true);
        cbContato4.setEnabled(true);
        cbContato5.setEnabled(true);
    }

    private void criarEventos() {
        Disposable positiveDisposable = RxView.clicks(btnPositive)
                .throttleFirst(BUTTON_DELAY_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(view -> dismissAllowingStateLoss());
        compositeDisposable.add(positiveDisposable);

        Disposable solicitarDisposable = RxView.clicks(btnSolicitar)
                .throttleFirst(BUTTON_DELAY_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(view -> solicitarSenha());
        compositeDisposable.add(solicitarDisposable);

        List<Observable<Boolean>> contatos = new ArrayList<>();
        contatos.add(RxCompoundButton.checkedChanges(cbContato1));
        contatos.add(RxCompoundButton.checkedChanges(cbContato2));
        contatos.add(RxCompoundButton.checkedChanges(cbContato3));
        contatos.add(RxCompoundButton.checkedChanges(cbContato4));
        contatos.add(RxCompoundButton.checkedChanges(cbContato5));

        Disposable contatosDisposable = Observable.combineLatest(contatos, lista -> {
            List<Boolean> novaLista = Stream.of(lista)
                    .map(temp -> (Boolean) temp)
                    .filter(selecionado -> selecionado)
                    .toList();

            return !novaLista.isEmpty();
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(selecionado -> {
                    btnSolicitar.setVisibility(View.GONE);
                    btnPositive.setText(android.R.string.ok);
                    if (selecionado) {
                        btnSolicitar.setVisibility(View.VISIBLE);
                        btnPositive.setText(R.string.cancelar);
                    }
                });
        compositeDisposable.add(contatosDisposable);
    }

    private void popularTela() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }

        String clienteJson = bundle.getString(CLIENTE_JSON, null);
        String senhaJson = bundle.getString(SENHA_JSON, null);
        if (clienteJson == null || senhaJson == null) {
            return;
        }

        presenter.iniciar(clienteJson, senhaJson);
    }

    private void solicitarSenha() {
        presenter.solicitarSenhaSgv(
                cbContato1.isChecked(),
                cbContato2.isChecked(),
                cbContato3.isChecked(),
                cbContato4.isChecked(),
                cbContato5.isChecked()
        );
    }

    private void atribuirTexto(AppCompatTextView field, AppCompatCheckBox checkBox,
                               String label, String value) {
        if (field == null || label == null || checkBox == null) return;

        if (Util_IO.isNullOrEmpty(value)) {
            field.setText("");
            field.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString span1 = new SpannableString(label);
        span1.setSpan(
                new ForegroundColorSpan(Color.BLACK),
                0,
                span1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        builder.append(span1).append(" ").append(value);
        field.setText(builder);
    }
}
