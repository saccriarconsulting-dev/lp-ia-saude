package com.axys.redeflexmobile.ui.redeflex.clienteinfo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axys.redeflexmobile.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rogério Massa on 07/11/18.
 */

public class ClienteInfoFragment extends Fragment {

    @BindView(R.id.cliente_info_fragment_rv_lista) RecyclerView rvLista;

    private List<ClienteInfoItem> lista;
    private ClienteInfoFragmentType tipoFragmento;

    public static ClienteInfoFragment newInstance(List<ClienteInfoItem> lista,
                                                  ClienteInfoFragmentType tipoFragmento) {
        ClienteInfoFragment fragment = new ClienteInfoFragment();
        fragment.setLista(lista);
        fragment.setTipoFragmento(tipoFragmento);
        return fragment;
    }

    public void setLista(List<ClienteInfoItem> lista) {
        this.lista = lista;
    }

    public void setTipoFragmento(ClienteInfoFragmentType tipoFragmento) {
        this.tipoFragmento = tipoFragmento;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cliente_info_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {
        rvLista.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLista.setNestedScrollingEnabled(false);
        ClienteInfoAdapter adapter = new ClienteInfoAdapter(getContext(), tipoFragmento);
        rvLista.setAdapter(adapter);
        adapter.setLista(lista);
    }

    static class ClienteInfoItem {
        String primeiroCampo;
        String segundoCampo;
        String terceiroCampo;
        String quartoCampo;
        String quintoCampo;
        String validador;
        ClienteInfoItemType tipo;

        ClienteInfoItem() {
            this.primeiroCampo = "Produto";
            this.segundoCampo = "Quantidade";
            this.terceiroCampo = "Valor";
            this.tipo = ClienteInfoItemType.HEADER_ITEM;
        }

        ClienteInfoItem(String validador) {
            this.primeiroCampo = "Produto";
            this.segundoCampo = "Qtd.";
            this.terceiroCampo = "R$ Face";
            this.quartoCampo = "R$ Total";
            this.quintoCampo = "Data";
            this.validador = validador;
            this.tipo = ClienteInfoItemType.HEADER_ITEM;
        }

        ClienteInfoItem(String primeiroCampo,
                        String validador) {
            this.primeiroCampo = primeiroCampo;
            this.validador = validador;
            this.tipo = ClienteInfoItemType.HEADER;
        }

        ClienteInfoItem(String primeiroCampo,
                        String segundoCampo,
                        String terceiroCampo,
                        String quartoCampo,
                        String quintoCampo,
                        String validador) {
            this.primeiroCampo = primeiroCampo;
            this.segundoCampo = segundoCampo;
            this.terceiroCampo = terceiroCampo;
            this.quartoCampo = quartoCampo;
            this.quintoCampo = quintoCampo;
            this.validador = validador;
            this.tipo = ClienteInfoItemType.ITEM;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClienteInfoItem that = (ClienteInfoItem) o;

            if (primeiroCampo != null ? !primeiroCampo.equals(that.primeiroCampo) : that.primeiroCampo != null)
                return false;
            if (segundoCampo != null ? !segundoCampo.equals(that.segundoCampo) : that.segundoCampo != null)
                return false;
            if (terceiroCampo != null ? !terceiroCampo.equals(that.terceiroCampo) : that.terceiroCampo != null)
                return false;
            if (quartoCampo != null ? !quartoCampo.equals(that.quartoCampo) : that.quartoCampo != null)
                return false;
            if (quintoCampo != null ? !quintoCampo.equals(that.quintoCampo) : that.quintoCampo != null)
                return false;
            if (validador != null ? !validador.equals(that.validador) : that.validador != null)
                return false;
            return tipo == that.tipo;
        }

        @Override
        public int hashCode() {
            int result = primeiroCampo != null ? primeiroCampo.hashCode() : 0;
            result = 31 * result + (segundoCampo != null ? segundoCampo.hashCode() : 0);
            result = 31 * result + (terceiroCampo != null ? terceiroCampo.hashCode() : 0);
            result = 31 * result + (validador != null ? validador.hashCode() : 0);
            result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
            return result;
        }
    }

    enum ClienteInfoItemType {
        HEADER, HEADER_ITEM, ITEM
    }

    enum ClienteInfoFragmentType {
        PRECO_DIFERENCIADO, ULTIMA_VENDA
    }
}
