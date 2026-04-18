package com.axys.redeflexmobile.ui.redeflex.solicitacaopid;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.viewmodel.SolicitacaoPidViewModel;
import com.axys.redeflexmobile.shared.util.Alerta;

public class Activity_SimuladorPid extends AppCompatActivity {
    //public SolicitacaoPidViewModel solicitacaoPidViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulador_pid);

        // Inicializa o ViewModel no escopo da Activity
        //solicitacaoPidViewModel = new ViewModelProvider(this).get(SolicitacaoPidViewModel.class);

        montarActionBar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.simuladorpid_fragment_container, new Fragment_SimuladorPidTela1())
                    .commit();
        }
    }

    private void montarActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Simulador PID");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), "Tem certeza que gostaria de sair?");
                alerta.showConfirm((dialog, which) -> {
                    finish();
                }, null);
                break;
            default:
                break;
        }
        return true;
    }
}