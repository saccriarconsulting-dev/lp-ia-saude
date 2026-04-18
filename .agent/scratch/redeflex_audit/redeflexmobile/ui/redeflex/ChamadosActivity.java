package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ViewPagerAdapter;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.fragment.FragmentChamado;

public class ChamadosActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        configurarViewPager();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabCadastro);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chamados");
        }

        floatingActionButton.setOnClickListener((view) -> {
            Utilidades.openNewActivity(ChamadosActivity.this, NovoChamadoActivity.class, null, false);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        configurarViewPager();
    }

    private void configurarViewPager() {
        Bundle args;
        FragmentChamado meusAtendimentos = new FragmentChamado();
        args = new Bundle();
        args.putInt("tipo", 0);
        meusAtendimentos.setArguments(args);

        FragmentChamado meusChamados = new FragmentChamado();
        args = new Bundle();
        args.putInt("tipo", 1);
        meusChamados.setArguments(args);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(meusAtendimentos, "Meus Atendimentos");
        viewPagerAdapter.addFragment(meusChamados, "Meus Solicitados");

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}