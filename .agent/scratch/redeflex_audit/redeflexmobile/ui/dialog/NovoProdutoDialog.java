package com.axys.redeflexmobile.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.UtilAdapter;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;

import java.util.ArrayList;

/**
 * Created by joao.viana on 26/09/2017.
 */

public class NovoProdutoDialog extends DialogFragment {
    public static ArrayList<Produto> listProdutos;
    public static boolean bOcultarQuantidade;

    public interface OnCompleteListener {
        void onCompleteNovoProduto(Produto produto);
    }

    Produto produto;
    public static final int REQUESTCODE = 1;

    SearchableSpinner cbproduto;
    EditText txtqtde;
    Button btncanc, btnok;

    public OnCompleteListener myCompleteListener;

    public NovoProdutoDialog() {
        NovoProdutoDialog.bOcultarQuantidade = false;
        if (NovoProdutoDialog.listProdutos == null)
            NovoProdutoDialog.listProdutos = new DBEstoque(getContext()).getProdutos();
        this.produto = null;
    }

    private void criarObjetos(View view) {
        txtqtde = (EditText) view.findViewById(R.id.edtQtde);
        btncanc = (Button) view.findViewById(R.id.btnCancelar);
        btnok = (Button) view.findViewById(R.id.btnOk);
        cbproduto = (SearchableSpinner) view.findViewById(R.id.spinnerProduto);
    }

    private void criarEventos() {
        cbproduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                produto = listProdutos.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (produto == null) {
                    Mensagens.produtoNaoSelecionado(getContext());
                    return;
                }

                if (!NovoProdutoDialog.bOcultarQuantidade) {
                    try {
                        produto.setQtde(Integer.parseInt(txtqtde.getText().toString()));
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        produto.setQtde(0);
                    }

                    if (produto.getQtde() == 0) {
                        Mensagens.quantidadeNaoInformada(getContext());
                        return;
                    }
                }
                if (myCompleteListener != null) {
                    myCompleteListener.onCompleteNovoProduto(produto);
                }
                onActivityResult(NovoProdutoDialog.REQUESTCODE, Activity.RESULT_OK, null);
                dismiss();
            }
        });

        btncanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActivityResult(NovoProdutoDialog.REQUESTCODE, Activity.RESULT_CANCELED, null);
                dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_novo_produto, container);

        try {
            setCancelable(false);
            criarObjetos(view);

            if (NovoProdutoDialog.bOcultarQuantidade) {
                view.findViewById(R.id.lblQtde).setVisibility(View.GONE);
                view.findViewById(R.id.edtQtde).setVisibility(View.GONE);
            }


            cbproduto.setAdapter(UtilAdapter.adapterProduto(getContext(), NovoProdutoDialog.listProdutos));

            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(getActivity(), ex.getMessage(), false);
        }
        return view;
    }
}