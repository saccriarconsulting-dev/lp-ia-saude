package com.axys.redeflexmobile.ui.redeflex;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCartaoPonto;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirenciaAgendada;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.CartaoPontoService;
import com.axys.redeflexmobile.shared.services.bus.ColaboradorBus;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.login.LoginActivity;

import java.io.IOException;

import timber.log.Timber;

public class SplashScreen extends AppCompatActivity {
    public static final int PERMISSION_CODE = 1;
    ProgressDialog mDialog;

    private final String[] PERMISSIONS_LIST = getPermissions();
    int permissionPosition = 0;

    private static String[] getPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            return new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.CAMERA
            };
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.CAMERA};
        } else {
            return new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA};
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        resetAcquisitionScheduled();
        requestPermissionStart(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ((TextView) findViewById(R.id.txtVersao)).setText(getString(R.string.label_versao, BuildConfig.VERSION_NAME));
        } catch (Exception ex) {
            Alerta msg = new Alerta(SplashScreen.this, "Informação", "Erro ao carregar versão do aplicativo");
            msg.show();
        }

        startService(new Intent(this, CartaoPontoService.class));
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionPosition == PERMISSIONS_LIST.length - 1) {
            requestPermission();
        } else {
            permissionPosition = permissionPosition + 1;
            requestPermissionStart(permissionPosition);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
        }
    }


    private boolean requestPermission(int position) {
        String permission = PERMISSIONS_LIST[position];
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                DialogInterface.OnClickListener dialogListener = (dialog, which) -> {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_CODE);
                };
                Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), getResources().getString(
                        permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION ? R.string.mensagem_dialog_permissao_background_location :
                                R.string.mensagem_dialog_permissao));
                alerta.show(dialogListener);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_CODE);
            }
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissionStart(int startPosition) {
        for (int i = startPosition; i < PERMISSIONS_LIST.length; i++) {
            permissionPosition = i;
            if (requestPermission(permissionPosition)) {
                break;
            }

            if (permissionPosition == PERMISSIONS_LIST.length - 1) {
                requestPermission();
            }
        }
    }

    private void requestPermission() {
        if (checkSelfPermissions()) {
            if (checkShouldPermissions()) {
                DialogInterface.OnClickListener dialogListener = (dialog, which) -> requestPermissionStart(0);
                Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name),
                        getResources().getString(R.string.mensagem_dialog_permissao));
                alerta.show(dialogListener);
            } else {
                requestPermissionStart(0);
            }
        } else {
            new AutenticacaoTask().execute();
        }
    }

    private boolean checkSelfPermissions() {
        for (String permission : PERMISSIONS_LIST) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    private boolean checkShouldPermissions() {
        for (String permission : PERMISSIONS_LIST) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    private void resetAcquisitionScheduled() {
        DBRotaAdquirenciaAgendada dbRotaAdquirenciaAgendada = new DBRotaAdquirenciaAgendada(this);
        dbRotaAdquirenciaAgendada.deletaTudoVencida();
    }

    private class AutenticacaoTask extends AsyncTask<String, Void, Colaborador> {
        DBColaborador dbColaborador;

        AutenticacaoTask() {
            dbColaborador = new DBColaborador(SplashScreen.this);
        }

        @Override
        protected Colaborador doInBackground(String... params) {
            try {
                Utilidades.ativaInativaAlarmes(SplashScreen.this, false);
                Colaborador atual = dbColaborador.get();

                Utilidades.getDataServidorESalvaBanco(SplashScreen.this);
                try {
                    new DBCartaoPonto(SplashScreen.this).apagarRegistroComMais60Dias();
                } catch (RuntimeException e) {
                    Timber.d(e);
                }

                if (Utilidades.isConectado(SplashScreen.this)) {
                    Colaborador server = ColaboradorBus.getServer(SplashScreen.this);

                    Utilidades.getDataServidorESalvaBanco(SplashScreen.this);

                    if (server != null) {
                        if (server.isOk()) {
                            if (atual == null) {
                                if (server.getId() > 0) {
                                    dbColaborador.add(server);
                                    Utilidades.addRegId(server.getId());
                                    server.setAcao("BAIXAR");
                                    return server;
                                } else {
                                    server.setAcao("NAOOK");
                                    return server;
                                }
                            } else {
                                if (server.getId() == 0) {
                                    server.setAcao("OFF");
                                    return server;
                                } else {
                                    if (atual.getId() != server.getId()) {
                                        Utilidades.removeRegId(atual.getId());
                                        Utilidades.addRegId(server.getId());
                                        Utilidades.deletarTudo(SplashScreen.this);
                                        dbColaborador.atualiza(server);
                                        server.setAcao("BAIXAR");
                                        return server;
                                    } else {
                                        server.setAcao("OK");
                                        if (server.isRegistrarGcm()) {
                                            Utilidades.addRegId(server.getId());
                                        }
                                        dbColaborador.atualiza(server);
                                        return server;
                                    }
                                }
                            }

                        } else {
                            if (atual != null) {
                                dbColaborador.delete(atual.getId());
                                Utilidades.deletarTudo(SplashScreen.this);
                                Utilidades.removeRegId(atual.getId());
                                server.setAcao("REMOVEU");
                                return server;
                            } else {
                                server.setAcao("NAOOK");
                                return server;
                            }
                        }
                    } else {
                        if (atual != null) {
                            atual.setOk(true);
                            atual.setAcao("OFF");
                        }
                        return atual;
                    }
                } else {
                    if (atual != null) {
                        atual.setOk(true);
                        atual.setAcao("OFF");
                    }
                    return atual;
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(SplashScreen.this, getResources().getString(R.string.app_name), "Aguarde....", false, false);
        }

        @Override
        protected void onPostExecute(Colaborador result) {
            super.onPostExecute(result);
            try {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                    mDialog = null;
                }

                if (result != null) {
                    if (result.isOk()) {
                        if (result.getAcao().equals("BAIXAR")) {
                            //new BaixarTudoTask(SplashScreen.this, true).execute();
                            Utilidades.openNewActivity(SplashScreen.this, LoginActivity.class, null, true);
                        } else //OFF ou OK
                            Utilidades.openNewActivity(SplashScreen.this, LoginActivity.class, null, true);
                    } else {
                        if (result.getAcao().equals("REMOVEU") || result.getAcao().equals("NAOOK")) {
                            if (!(SplashScreen.this).isFinishing()) {
                                Utilidades.openNewActivity(SplashScreen.this, LoginActivity.class, null, true);
                            }
                        }
                    }
                } else
                    Utilidades.openNewActivity(SplashScreen.this, LoginActivity.class, null, true);
            } catch (Exception ex) {
                if (!(SplashScreen.this).isFinishing()) {
                    Utilidades.openNewActivity(SplashScreen.this, LoginActivity.class, null, true);
                }
            } finally {
                SimpleDbHelper.INSTANCE.close();
            }
        }
    }
}
