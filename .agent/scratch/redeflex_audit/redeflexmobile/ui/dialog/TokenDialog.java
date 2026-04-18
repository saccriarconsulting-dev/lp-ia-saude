package com.axys.redeflexmobile.ui.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBTokenCliente;
import com.axys.redeflexmobile.shared.models.TokenCliente;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.List;

/**
 * Created by joao.viana on 20/09/2017.
 */

public class TokenDialog extends DialogFragment {
    public OnCompleteListenet onCompleteListenet;
    TextView btnCancelar, btnOk;
    String codigoCliente;
    LinearLayout llTokenList;
    List<TokenCliente> myTokens;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.token_dialog, container);

        try {
            setCancelable(false);
            Bundle bundle = getArguments();
            codigoCliente = bundle.getString(Config.CodigoCliente);

            DBTokenCliente connection = new DBTokenCliente(getContext());

            criarObjetos(view);
            criarEventos(connection);

            myTokens = connection.getTokensByIdCliente(codigoCliente);
            setupTokens(llTokenList, myTokens);

        } catch (Exception ex) {
            Utilidades.retornaMensagem(view.getContext(), ex.getMessage(), true);
        }
        return view;
    }

    private void setupTokens(LinearLayout llTokenList, List<TokenCliente> list) {
        for (TokenCliente token : list) {
            View view = getLayoutInflater().inflate(R.layout.custom_token_bar, null);
            TextView tvCode = view.findViewById(R.id.txtValor);
            TextView tvLabel = view.findViewById(R.id.txtLabel);

            tvCode.setText(token.getToken());
            switch (token.getTipoToken()) {
                case 1: {
                    tvLabel.setText("Código Muxx");
                    break;
                }
                case 2: {
                    tvLabel.setText("Código Masters");
                    break;
                }
            }
            llTokenList.addView(view);
        }
    }

    private void criarEventos(DBTokenCliente connection) {
        btnCancelar.setOnClickListener((view) -> {
            dismiss();
        });

        btnOk.setOnClickListener((view) -> {
            try {
                connection.deleteTokenByIdCliente(codigoCliente);
                if (onCompleteListenet != null) {
                    onCompleteListenet.onCompleteToken(true);
                    dismiss();
                }
            } catch (Exception ex) {
                Utilidades.retornaMensagem(view.getContext(), ex.getMessage(), true);
            }
        });
    }

    private void criarObjetos(View view) {
        btnOk = view.findViewById(R.id.btnOk);
        btnCancelar = view.findViewById(R.id.btnCancelar);
        llTokenList = view.findViewById(R.id.llCodigo);
    }

    @FunctionalInterface
    public interface OnCompleteListenet {
        void onCompleteToken(boolean pValor);
    }
}