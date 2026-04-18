package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.InformacaoCorbanTransacao;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.List;

public class InformacaoCorbanTransacaoAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<InformacaoCorbanTransacao> mLista;

    public InformacaoCorbanTransacaoAdapter(Context ct, ArrayList<InformacaoCorbanTransacao> informacaoCorbanTransacaoList) {
        this.inflater =(LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLista = informacaoCorbanTransacaoList;
    }

    @Override
    public int getCount() {
        return mLista.size();
    }

    @Override
    public Object getItem(int i) {
        return mLista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        InformacaoCorbanTransacao informacaoCorbanTransacao = mLista.get(i);
        View v = inflater.inflate(R.layout.linha_infocorbantransacao, null);
        String vMes = Utilidades.retornaMesExtenso(Integer.valueOf(Integer.toString(informacaoCorbanTransacao.getAnomes()).substring(5)));
        String vAno = Integer.toString(informacaoCorbanTransacao.getAnomes()).substring(0,4);
        ((TextView) v.findViewById(R.id.linhainfocorbantransacao_txtAnoMes)).setText(vMes + " / " + vAno);
        ((TextView) v.findViewById(R.id.linhainfocorbantransacao_txtQtdTransacao)).setText(Integer.toString(informacaoCorbanTransacao.getQtdtransacao()));
        return v;
    }
}
