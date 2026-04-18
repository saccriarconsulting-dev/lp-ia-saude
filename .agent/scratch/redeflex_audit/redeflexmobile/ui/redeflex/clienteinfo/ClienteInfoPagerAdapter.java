package com.axys.redeflexmobile.ui.redeflex.clienteinfo;

import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoFragment.ClienteInfoItem;

import java.util.ArrayList;
import java.util.List;

import static com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoFragment.ClienteInfoFragmentType.PRECO_DIFERENCIADO;
import static com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoFragment.ClienteInfoFragmentType.ULTIMA_VENDA;

/**
 * @author Rogério Massa on 29/10/18.
 */

public class ClienteInfoPagerAdapter extends FragmentPagerAdapter implements TabLayout.BaseOnTabSelectedListener {

    private IClienteInfoPagerAdapterListener tabSelectedListener;
    private List<ClienteInfoPageAdapterItem> listaFragmentos;

    ClienteInfoPagerAdapter(FragmentManager fm,
                            List<ClienteInfoItem> precoDiferenciadoLista,
                            List<ClienteInfoItem> ultimasVendasLista,
                            IClienteInfoPagerAdapterListener tabAdapterListener) {
        super(fm);
        this.listaFragmentos = new ArrayList<>();
        tabSelectedListener = tabAdapterListener;

        if (precoDiferenciadoLista != null && !precoDiferenciadoLista.isEmpty()) {
            listaFragmentos.add(new ClienteInfoPageAdapterItem("PREÇOS DIFERENCIADOS",
                    ClienteInfoFragment.newInstance(precoDiferenciadoLista, PRECO_DIFERENCIADO)));
        }

        if (ultimasVendasLista != null && !ultimasVendasLista.isEmpty()) {
            listaFragmentos.add(new ClienteInfoPageAdapterItem("ÚLTIMA VENDA",
                    ClienteInfoFragment.newInstance(ultimasVendasLista, ULTIMA_VENDA)));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return listaFragmentos.get(position).fragment;
    }

    @Override
    public int getCount() {
        return listaFragmentos.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listaFragmentos.get(position).tab;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tabSelectedListener != null) {
            tabSelectedListener.onTabSelected(tab);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //unused
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        //unused
    }

    interface IClienteInfoPagerAdapterListener {
        void onTabSelected(TabLayout.Tab tab);
    }

    class ClienteInfoPageAdapterItem {
        public String tab;
        public ClienteInfoFragment fragment;

        public ClienteInfoPageAdapterItem(String tab, ClienteInfoFragment fragment) {
            this.tab = tab;
            this.fragment = fragment;
        }
    }
}
