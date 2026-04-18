package com.axys.redeflexmobile.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.RotaAdapter;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRota;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Rota;
import com.axys.redeflexmobile.shared.services.BlipProvider;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.clientevalidacao.ClienteActivity;
import com.axys.redeflexmobile.ui.redeflex.ChatbotActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.RotaActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by joao.viana on 25/08/2016.
 */
public class FragmentRota extends Fragment implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {
    ArrayList<Rota> listaRota;
    RotaAdapter mAdapter;
    int dayOfWeek, weekOfMonth;

    ListView listView;
    boolean bloqueiaRota;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setHasOptionsMenu(true);
            Bundle bundle = getArguments();
            dayOfWeek = bundle.getInt("dia");
            weekOfMonth = bundle.getInt("semana");
        } catch (Exception ex) {
            Mensagens.mensagemErro(getContext(), ex.getMessage(), true);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pesquisa, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Pesquisar cliente...");
        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        menu.findItem(R.id.call_chat_bot).setVisible(DeviceUtils.getChatPermission(getActivity()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call_chat_bot) {
            Colaborador colaborador = new DBColaborador(getActivity()).get();
            String url = BuildConfig.CHATBOT_URL + "?ci=" + colaborador.getUsuarioChatbot() + "&servico=" + BuildConfig.CHATBOT_SERVICO + "&aplicacao=persona";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_rota, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            criarObjetos(view);
            carregarDados("");
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(getContext(), ex.getMessage(), false);
        }
    }

    private void criarObjetos(View view) {
        listView = view.findViewById(R.id.lvRota);
    }

    private void carregarDados(String pParametro) {
        try {
            int pVendedor = RotaActivity.idVendedor;
            int pweekOfMonth = RotaActivity.weekOfMonth;

            listaRota = new DBRota(getContext()).getRotasByDiaSemana(dayOfWeek, pParametro, pweekOfMonth, pVendedor);

            mAdapter = new RotaAdapter(getContext(), R.layout.item_rota, listaRota);
            listView.setEmptyView(getActivity().findViewById(R.id.empty));
            mAdapter.notifyDataSetChanged();
            listView.setAdapter(mAdapter);
        } catch (Exception ex) {
            Mensagens.mensagemErro(getContext(), ex.getMessage(), false);
        }
    }

    private void criarEventos() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            RotaActivity rotaActivity = (RotaActivity) getActivity();
            if (rotaActivity != null && rotaActivity.isBloqueiaRota()) {
                return;
            }
            if (bloqueiaRota) {
                return;
            }

            bloqueiaRota = true;
            String[] arr = view.getTag().toString().split(",");
            Bundle bundle = new Bundle();
            bundle.putString(Config.CodigoCliente, arr[0]);
            bundle.putString(Config.Longitude, arr[1]);
            bundle.putString(Config.Latitude, arr[2]);
            bundle.putString(Config.DIA_SEMANA, arr[3]);
            bundle.putString(Config.SEMANA, arr[4]);
            Utilidades.openNewActivity(getContext(), ClienteActivity.class, bundle, true);
        });
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 2) {
            carregarDados(newText);
        } else {
            carregarDados("");
        }
        return true;
    }
}
