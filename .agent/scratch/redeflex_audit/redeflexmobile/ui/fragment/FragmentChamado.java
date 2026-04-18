package com.axys.redeflexmobile.ui.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ChamadosAdapter;
import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.ChamadoActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

public class FragmentChamado extends Fragment {
    ListView lsChamados;
    int iTipo;
    ArrayList<Chamado> chamados;
    ChamadosAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getArguments();
            iTipo = bundle.getInt("tipo");
        } catch (Exception ex) {
            Mensagens.mensagemErro(getActivity(), ex.getMessage(), false);
        }
    }

    public FragmentChamado() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chamado, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lsChamados = (ListView) view.findViewById(R.id.listChamados);

        try {
            chamados = new DBChamados(getContext()).getChamados(iTipo);
            mAdapter = new ChamadosAdapter(getContext(), R.layout.item_chamados, chamados);
            lsChamados.setEmptyView(view.findViewById(R.id.empty));
            lsChamados.setAdapter(mAdapter);
            lsChamados.setOnItemClickListener((parent, view2, position, id) -> {
                Bundle bundle = new Bundle();
                bundle.putString(Config.CodigoChamado, view2.getTag().toString());
                Utilidades.openNewActivity(getContext(), ChamadoActivity.class, bundle, false);
            });
        } catch (Exception ex) {
            Mensagens.mensagemErro(getActivity(), ex.getMessage(), false);
        }
    }
}