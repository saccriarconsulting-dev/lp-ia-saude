package com.axys.redeflexmobile.shared.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.core.text.HtmlCompat;
import androidx.appcompat.app.AlertDialog;

import com.axys.redeflexmobile.R;

import java.lang.ref.WeakReference;

/**
 * Created by joao.viana on 12/07/2016.
 */
public class AlertaHtml {
    private String mTitulo;
    private String mTexto;
    private Context mContexto;
    private WeakReference<Context> contexto;

    public AlertaHtml(Context pContexto, String pTitulo, String pTexto) {
        mTitulo = pTitulo;
        mTexto = pTexto;
        mContexto = pContexto;
        contexto = new WeakReference<>(pContexto);
    }

    public void setMensagem(String pMensagem) {
        this.mTexto = pMensagem;
    }

    public void show() {
        validarSePodeAbrir(context -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(HtmlCompat.fromHtml(mTitulo, HtmlCompat.FROM_HTML_MODE_COMPACT));
            dialog.setIcon(R.mipmap.ic_icone_new);
            dialog.setMessage(HtmlCompat.fromHtml(mTexto, HtmlCompat.FROM_HTML_MODE_COMPACT));
            dialog.setNeutralButton("OK", null);
            dialog.setCancelable(false);
            dialog.show();
        });
    }

    public void showError() {
        validarSePodeAbrir(context -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(HtmlCompat.fromHtml(mTitulo, HtmlCompat.FROM_HTML_MODE_COMPACT));
            dialog.setIcon(R.mipmap.ic_icone_new);
            dialog.setMessage(HtmlCompat.fromHtml(mTexto, HtmlCompat.FROM_HTML_MODE_COMPACT));
            dialog.setPositiveButton("OK", (dialog1, whichButton) -> ((Activity) mContexto).finish());
            dialog.setCancelable(false);
            dialog.show();
        });
    }

    public void showConfirm(DialogInterface.OnClickListener pDialogSim, DialogInterface.OnClickListener pDialogNao) {
        showConfirm(pDialogSim, "Sim", pDialogNao, "Não");
    }

    public void showConfirmRegistroPontoLogado(DialogInterface.OnClickListener pDialogSim, DialogInterface.OnClickListener pDialogNao) {
        showConfirmNeutral(pDialogSim, "CONFIRMAR", pDialogNao, "CANCELAR");
    }

    private void showConfirmNeutral(DialogInterface.OnClickListener pDialogSim, String pMensagemSim, DialogInterface.OnClickListener pDialogNao, String pMensagemNao) {
        validarSePodeAbrir(context -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(HtmlCompat.fromHtml(mTitulo, HtmlCompat.FROM_HTML_MODE_COMPACT));
            dialog.setIcon(R.mipmap.ic_icone_new);
            dialog.setMessage(HtmlCompat.fromHtml(mTexto, HtmlCompat.FROM_HTML_MODE_COMPACT));
            dialog.setPositiveButton(pMensagemSim, pDialogSim);
            dialog.setNeutralButton(pMensagemNao, pDialogNao);
            dialog.setCancelable(false);
            dialog.show();
        });
    }

    public void showConfirm(DialogInterface.OnClickListener pDialogSim, String pMensagemSim, DialogInterface.OnClickListener pDialogNao, String pMensagemNao) {
        validarSePodeAbrir(context -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContexto);
            dialog.setTitle(HtmlCompat.fromHtml(mTitulo, HtmlCompat.FROM_HTML_MODE_COMPACT));
            dialog.setIcon(R.mipmap.ic_icone_new);
            dialog.setMessage(HtmlCompat.fromHtml(mTexto, HtmlCompat.FROM_HTML_MODE_COMPACT));
            dialog.setPositiveButton(pMensagemSim, pDialogSim);
            dialog.setNegativeButton(pMensagemNao, pDialogNao);
            dialog.setCancelable(false);
            dialog.show();
        });
    }

    public void show(DialogInterface.OnClickListener pDialog) {
        validarSePodeAbrir(context -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContexto);
            dialog.setTitle(HtmlCompat.fromHtml(mTitulo, HtmlCompat.FROM_HTML_MODE_COMPACT));
            dialog.setIcon(R.mipmap.ic_icone_new);
            dialog.setMessage(HtmlCompat.fromHtml(mTexto, HtmlCompat.FROM_HTML_MODE_COMPACT));
            dialog.setPositiveButton("OK", pDialog);
            dialog.setCancelable(false);
            dialog.show();
        });
    }

    private void validarSePodeAbrir(Callback callback) {
        if (contexto.get() == null) {
            return;
        }

        if (!(contexto.get() instanceof Activity)) {
            return;
        }

        Activity activity = (Activity) contexto.get();
        if (activity.isFinishing() || activity.isDestroyed()) {
            return;
        }

        callback.execute(contexto.get());
    }
}