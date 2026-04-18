package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.Os_ExpListAdapter;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.models.OsConsulta;
import com.axys.redeflexmobile.shared.util.Mensagens;

import java.util.ArrayList;

public class OsFragment extends Fragment {
    int iTipo;
    ExpandableListView expandableListView;
    ArrayList<OsConsulta> osConsulta;
    Os_ExpListAdapter mAdapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_os, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            criarObjetos(view);
            carregarDados(view);
        } catch (Exception ex) {
            Mensagens.mensagemErro(getActivity(), ex.getMessage(), false);
        }
    }

    private void criarObjetos(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
    }

    private void carregarDados(View view) {
        try {
            osConsulta = new DBOs(getContext()).getConsultaOS(iTipo);
            mAdapter = new Os_ExpListAdapter(getContext(), osConsulta);
            expandableListView.setEmptyView(view.findViewById(R.id.empty));
            expandableListView.setAdapter(mAdapter);
        } catch (Exception ex) {
            Mensagens.mensagemErro(getActivity(), ex.getMessage(), false);
        }
    }
}