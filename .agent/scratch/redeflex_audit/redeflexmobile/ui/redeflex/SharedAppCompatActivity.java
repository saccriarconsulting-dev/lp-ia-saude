package com.axys.redeflexmobile.ui.redeflex;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.EnumStatusAuditagem;
import com.axys.redeflexmobile.shared.models.AuditagemEstoqueResponse;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.RxBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * @author Rogério Massa on 2019-08-08.
 */

public abstract class SharedAppCompatActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;
    private ProgressDialog progressDialog;
    private boolean trava;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        criarCarregandoAuditagemEstoque();
        inicializarAuditagemDisposable();
    }

    @Override
    protected void onPause() {
        fecharCarregandoAuditagemEstoque();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        trava = false;
        super.onStart();
    }

    @Override
    protected void onStop() {
        trava = true;
        super.onStop();
    }

    public abstract void exibirConfirmacaoAuditagemEstoque();

    private void criarCarregandoAuditagemEstoque() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setMessage("Salvando auditagem, mantenha a aplicação aberta e aguarde...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }

    public void mostrarCarregandoAuditagemEstoque() {
        if (trava) {
            return;
        }

        if (progressDialog == null) {
            criarCarregandoAuditagemEstoque();
        }

        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressDialog.show();
        } catch (Exception e) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Timber.e(e);
        }
    }

    public void fecharCarregandoAuditagemEstoque() {
        if (progressDialog != null && progressDialog.isShowing()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressDialog.dismiss();
        }
    }

    public void mostrarFinalizarAuditagemEstoque() {
        if (isFinishing()) {
            return;
        }

        Alerta alerta = new Alerta(this, getString(R.string.app_name), "Auditoria concluída com sucesso!");
        try {
            alerta.show((dialog, which) -> {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                exibirConfirmacaoAuditagemEstoque();
            });
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void inicializarAuditagemDisposable() {
        compositeDisposable.add(RxBus.getInstance()
                .observer(AuditagemEstoqueResponse.class)
                .delay(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (EnumStatusAuditagem.CONCLUIDA.equals(response.getStatus())) {
                        auditagemFinalizada(response);
                        return;
                    }
                    mostrarCarregandoAuditagemEstoque();
                }, Timber::e)
        );
    }

    private void auditagemFinalizada(AuditagemEstoqueResponse response) {
        if (response.getHandler() != null && response.getRunnable() != null) {
            response.getHandler().removeCallbacks(response.getRunnable());
        }
        stopService(response.getServiceIntent());
        fecharCarregandoAuditagemEstoque();
        mostrarFinalizarAuditagemEstoque();
    }
}
