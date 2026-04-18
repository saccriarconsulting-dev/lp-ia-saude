package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;

import com.axys.redeflexmobile.shared.adapters.ProjetoTradeAdapter;
import com.axys.redeflexmobile.shared.adapters.SemanaRotaAdapter;
import com.axys.redeflexmobile.shared.adapters.VendedorAdapter;
import com.axys.redeflexmobile.shared.bd.BDVendedor;
import com.axys.redeflexmobile.shared.models.ProjetoTrade;
import com.axys.redeflexmobile.shared.models.SemanaRota;
import com.axys.redeflexmobile.shared.models.Vendedor;
import com.axys.redeflexmobile.ui.dialog.ProjetoTradeDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ViewPagerAdapter;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.fragment.FragmentRota;

import java.util.ArrayList;

public class RotaActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton btnNova;
    int dayOfWeek;
    boolean bloqueiaRota;

    private VendedorAdapter mAdapter;
    private SemanaRotaAdapter mAdapterSemana;
    Spinner ddlVendedor, ddlSemana;
    public static ArrayList<Vendedor> listVendedores;
    public static ArrayList<SemanaRota> listSemanas;

    public static int idVendedor, weekOfMonth;
    LinearLayout ll_Supervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rota2);

        try {
            criarObjetos();
            CriaEventos();

            setSupportActionBar(toolbar);

            // Carrega Vendedores
            idVendedor = 0;
            alimentaDados();

            Colaborador colaborador = new DBColaborador(RotaActivity.this).get();
            int[] atendimento = Utilidades.retornaDiaSemanaAtendimento(colaborador);
            dayOfWeek = atendimento[0];
            weekOfMonth = atendimento[1];

            configurarViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Rota");
            }

            btnNova.setOnClickListener((view) -> {
                if (bloqueiaRota) {
                    return;
                }
                bloqueiaRota = true;
                try {
                    Utilidades.openRota(RotaActivity.this, true);
                } catch (Exception ex) {
                    bloqueiaRota = false;
                    Mensagens.mensagemErro(RotaActivity.this, ex.getMessage(), false);
                }
            });

            if (colaborador.isValidaOrdemRota()) {
                btnNova.hide();
            }

            // Atualiza Spinner da Semana
            AtualizaSemana(weekOfMonth);

        } catch (Exception ex) {
            bloqueiaRota = false;
            Mensagens.mensagemErro(RotaActivity.this, ex.getMessage(), true);
        }
    }

    public boolean isBloqueiaRota() {
        return bloqueiaRota;
    }

    private void criarObjetos() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);
        btnNova = findViewById(R.id.fabNovaRota);
        toolbar = findViewById(R.id.toolbar);
        ddlVendedor = (Spinner) findViewById(R.id.ddlVendedor);
        ddlSemana = (Spinner) findViewById(R.id.ddlSemana);
        ll_Supervisor = findViewById(R.id.ll_Supervisor);
    }

    private void CriaEventos() {
        ddlVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Colaborador colaborador = new DBColaborador(RotaActivity.this).get();
                    int[] atendimento = new int[0];
                    try {
                        atendimento = Utilidades.retornaDiaSemanaAtendimento(colaborador);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    dayOfWeek = atendimento[0];
                    weekOfMonth = atendimento[1];
                    idVendedor = 0;
                } else {
                    idVendedor = (int) listVendedores.get(position).getIdVendedor();
                    weekOfMonth = (int) listVendedores.get(position).getSemanaRota();
                }

                InicializaViewPager();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ddlSemana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weekOfMonth = (int) listSemanas.get(position).getIdSemana();
                InicializaViewPager();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void alimentaDados() {
        listVendedores = new BDVendedor(RotaActivity.this).getVendedor(null, null);
        mAdapter = new VendedorAdapter(RotaActivity.this, R.layout.custom_spinner_title_bar, listVendedores);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlVendedor.setAdapter(mAdapter);

        // Carrega Ciclo de Semanas
        int cicloSemana = new DBColaborador(RotaActivity.this).get().getCicloRoteirizacao();
        if (cicloSemana > 0)
            atualizaListaSemana(cicloSemana);
        else {
            atualizaListaSemana(4);
        }

        if (listVendedores != null && listVendedores.size() > 1)
            ll_Supervisor.setVisibility(View.VISIBLE);
        else
            ll_Supervisor.setVisibility(View.GONE);
    }

    private void atualizaListaSemana(int ciclo) {
        listSemanas = new ArrayList<SemanaRota>();
        SemanaRota semanaRota = new SemanaRota();
        for (int aa = 1; aa <= ciclo; aa++) {
            semanaRota = new SemanaRota();
            semanaRota.setIdSemana(aa);
            semanaRota.setSemana("Semana " + aa);
            listSemanas.add(semanaRota);
        }
        mAdapterSemana = new SemanaRotaAdapter(RotaActivity.this, R.layout.custom_spinner_title_bar, listSemanas);
        mAdapterSemana.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlSemana.setAdapter(mAdapterSemana);
    }

    private void configurarViewPager(ViewPager viewPager) {
        Bundle args;

        FragmentRota segunda = new FragmentRota();
        args = new Bundle();
        args.putInt("dia", 2);
        args.putInt("semana", weekOfMonth);
        args.putInt("vendedor", idVendedor);
        segunda.setArguments(args);

        FragmentRota terca = new FragmentRota();
        args = new Bundle();
        args.putInt("dia", 3);
        args.putInt("semana", weekOfMonth);
        args.putInt("vendedor", idVendedor);
        terca.setArguments(args);

        FragmentRota quarta = new FragmentRota();
        args = new Bundle();
        args.putInt("dia", 4);
        args.putInt("semana", weekOfMonth);
        args.putInt("vendedor", idVendedor);
        quarta.setArguments(args);

        FragmentRota quinta = new FragmentRota();
        args = new Bundle();
        args.putInt("dia", 5);
        args.putInt("semana", weekOfMonth);
        args.putInt("vendedor", idVendedor);
        quinta.setArguments(args);

        FragmentRota sexta = new FragmentRota();
        args = new Bundle();
        args.putInt("dia", 6);
        args.putInt("semana", weekOfMonth);
        args.putInt("vendedor", idVendedor);
        sexta.setArguments(args);

        FragmentRota sabado = new FragmentRota();
        args = new Bundle();
        args.putInt("dia", 7);
        args.putInt("semana", weekOfMonth);
        args.putInt("vendedor", idVendedor);
        sabado.setArguments(args);

        FragmentRota domingo = new FragmentRota();
        args = new Bundle();
        args.putInt("dia", 1);
        args.putInt("semana", weekOfMonth);
        args.putInt("vendedor", idVendedor);
        domingo.setArguments(args);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.clearFragments();
        viewPagerAdapter.addFragment(segunda, "Segunda");
        viewPagerAdapter.addFragment(terca, "Terça");
        viewPagerAdapter.addFragment(quarta, "Quarta");
        viewPagerAdapter.addFragment(quinta, "Quinta");
        viewPagerAdapter.addFragment(sexta, "Sexta");
        viewPagerAdapter.addFragment(sabado, "Sabado");
        viewPagerAdapter.addFragment(domingo, "Domingo");

        // Notifique o adaptador sobre a mudança
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();

        switch (dayOfWeek) {
            case 2:
                viewPager.setCurrentItem(0);
                break;
            case 3:
                viewPager.setCurrentItem(1);
                break;
            case 4:
                viewPager.setCurrentItem(2);
                break;
            case 5:
                viewPager.setCurrentItem(3);
                break;
            case 6:
                viewPager.setCurrentItem(4);
                break;
            case 7:
                viewPager.setCurrentItem(5);
                break;
            case 1:
                viewPager.setCurrentItem(6);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (!Utilidades.verificarHorarioComercial(RotaActivity.this, true)) {
                Mensagens.horarioComercial(RotaActivity.this);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(RotaActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void InicializaViewPager() {
        //viewPager = findViewById(R.id.viewPager);
        AtualizaSemana(weekOfMonth);
        configurarViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void AtualizaSemana(int weekOfMonth)
    {
        ddlSemana.setSelection(weekOfMonth-1);
    }
}
