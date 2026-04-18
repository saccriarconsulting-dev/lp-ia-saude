package com.axys.redeflexmobile.ui.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.enums.EnumValidacao;
import com.axys.redeflexmobile.shared.models.Cadastro;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.component.InputText;
import com.axys.redeflexmobile.ui.redeflex.Config;

/**
 * Created by joao.viana on 16/12/2016.
 */

public class CadastroClienteDialog extends DialogFragment {

    Button btnCancelar, btnSalvar;
    String idCliente;
    Cliente cliente;
    DBCliente dbCliente;
    TextView txtCliente;
    InputText txtTelCelular, txtEmail;
    boolean bAlterarContato;

    public interface OnCompleteListener {
        void onComplete(Cadastro cadastroalterado);
    }

    public OnCompleteListener myCompleteListenerCadastro;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_cadastro_cliente, container);

        try {
            setCancelable(false);
            Bundle bundle = getArguments();
            idCliente = bundle.getString(Config.CodigoCliente);
            bAlterarContato = bundle.getBoolean(Config.AtualizaContato);
            criaObjetos(view);
            TextView txtInformativo = (TextView) view.findViewById(R.id.txtInformativo);
            if (bAlterarContato) {
                txtInformativo.setText("Atualização cadastral necessária");
            } else {
                txtInformativo.setText("Informar o número do celular com o DDD do responsável do ponto de venda para ser enviado a senha para realizar vendas!");
                txtEmail.setVisibility(View.GONE);
            }
            criaEventos();
            carregaDados(view);
        } catch (Exception ex) {
            Utilidades.retornaMensagem(view.getContext(), "Erro: " + ex.getMessage(), false);
        }

        return view;
    }

    private void criaObjetos(View view) {
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnSalvar = (Button) view.findViewById(R.id.btnOk);
        dbCliente = new DBCliente(view.getContext());
        txtCliente = (TextView) view.findViewById(R.id.txtCliente);
        txtTelCelular = (InputText) view.findViewById(R.id.txtTelCelular);
        txtEmail = (InputText) view.findViewById(R.id.txtEmail);
    }

    private void carregaDados(View view) {
        try {
            cliente = dbCliente.getById(idCliente);
            if (cliente == null) {
                Utilidades.retornaMensagem(view.getContext(), "Cliente não encontrado", false);
                dismiss();
                return;
            }
            txtCliente.setText(cliente.retornaCodigoExibicao() + " - " + cliente.getNomeFantasia());
        } catch (Exception ex) {
            Utilidades.retornaMensagem(view.getContext(), "Erro: " + ex.getMessage(), false);
        }
    }

    private void criaEventos() {
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txtTelCelular.addMask("(##)#####-####");
        txtTelCelular.addValidacao(EnumValidacao.CELULAR);
        txtEmail.addValidacao(EnumValidacao.EMAIL);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telefone = txtTelCelular.getText().trim();
                String email = txtEmail.getText().trim().toLowerCase();
                try {
                    if (!Validacoes.validaTelCelular(telefone)) {
                        txtTelCelular.setError("Telefone inválido, Verifique");
                        txtTelCelular.requestFocus();
                        return;
                    }

                    String ddd = telefone.replace("(", "").replace(")", "").substring(0, 2);
                    String celular = telefone.replace("(", "").replace(")", "").substring(2, telefone.length() - 2).replace("-", "");

                    if (bAlterarContato) {
                        if (!Validacoes.validaEmail(email)) {
                            txtEmail.setError("Email inválido, Verifique!");
                            txtEmail.requestFocus();
                            return;
                        }

                        if (email.contains("@redeflex")) {
                            txtEmail.setError("Não é permitido incluir email empresarial, Verifique!");
                            txtEmail.requestFocus();
                            return;
                        }
                    }

                    cliente.setDddCelular(ddd);
                    cliente.setCelular(celular);
                    if (bAlterarContato) {
                        cliente.setEmailCliente(email);
                        cliente.setAtualizaContato("N");
                    }

                    dbCliente.updateCadastro(cliente, bAlterarContato);
                    if (myCompleteListenerCadastro != null) {
                        Cadastro cadastroalterado = new Cadastro();
                        cadastroalterado.setAlterado("S");
                        cadastroalterado.setExibeSenha(!bAlterarContato);
                        myCompleteListenerCadastro.onComplete(cadastroalterado);
                    }

                    dismiss();
                } catch (Exception ex) {
                    Utilidades.retornaMensagem(v.getContext(), "Erro: " + ex.getMessage(), false);
                }
            }
        });
    }
}