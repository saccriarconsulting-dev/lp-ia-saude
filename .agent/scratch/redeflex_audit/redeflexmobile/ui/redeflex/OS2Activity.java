package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ViewPagerAdapter;
import com.axys.redeflexmobile.shared.services.tasks.OsSyncTask;

public class OS2Activity extends AppCompatActivity {
    Toolbar toolbar;
    private ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        configurarViewPager();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Ordem de Serviço");
        }
    }

    private void configurarViewPager() {
        Bundle args;
        OsFragment pendente = new OsFragment();
        args = new Bundle();
        args.putInt("tipo", 0);
        pendente.setArguments(args);

        OsFragment agendada = new OsFragment();
        args = new Bundle();
        args.putInt("tipo", 1);
        agendada.setArguments(args);

        OsFragment atendida = new OsFragment();
        args = new Bundle();
        args.putInt("tipo", 2);
        atendida.setArguments(args);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(pendente, "OS Pendente");
        viewPagerAdapter.addFragment(agendada, "OS Agendada");
        viewPagerAdapter.addFragment(atendida, "OS Atendida");

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RequestCodeOS && resultCode == Activity.RESULT_OK) {
            configurarViewPager();
            new OsSyncTask(OS2Activity.this, 1).execute();
        }
    }
}