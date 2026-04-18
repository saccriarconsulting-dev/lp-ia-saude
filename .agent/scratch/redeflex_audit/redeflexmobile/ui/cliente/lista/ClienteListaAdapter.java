package com.axys.redeflexmobile.ui.cliente.lista;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.util.Function;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.redeflex.ConsignadoActivity;
import com.axys.redeflexmobile.ui.redeflex.ProdutoAuditagemActivity;
import com.axys.redeflexmobile.ui.venda.abertura.VendaAberturaActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ClienteListaAdapter extends RecyclerView.Adapter<ClienteListaAdapter.ViewHolder> {

    public static final int BUTTON_WAIT_DURATION = 1200;

    private List<Cliente> clientes = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Function<Cliente> func;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cliente_lista_item_layout, viewGroup, false);
        return new ViewHolder(view, compositeDisposable, func);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder clienteViewHolder, int i) {
        clienteViewHolder.bind(clientes.get(clienteViewHolder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        compositeDisposable.dispose();
    }

    public void setCallback(Function<Cliente> func) {
        this.func = func;
    }

    public void setClientes(@NonNull List<Cliente> clientes) {
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(
                new ClienteListaAdapterDiff(this.clientes, clientes)
        );

        this.clientes.clear();
        this.clientes.addAll(clientes);

        diff.dispatchUpdatesTo(this);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CompositeDisposable compositeDisposable;
        private final Function<Cliente> func;
        @BindView(R.id.cliente_lista_item_tv_nome) AppCompatTextView tvNome;
        @BindView(R.id.cliente_lista_item_tv_endereco) AppCompatTextView tvEndereco;
        @BindView(R.id.cliente_lista_item_tv_codigo_sgv) AppCompatTextView tvCodigoSgv;
        @BindView(R.id.cliente_lista_item_tv_visualizarConsignacao) AppCompatTextView tvConsignacaoAtiva;

        ViewHolder(@NonNull View itemView, CompositeDisposable compositeDisposable, Function<Cliente> func) {
            super(itemView);
            this.compositeDisposable = compositeDisposable;
            this.func = func;
            ButterKnife.bind(this, itemView);
        }

        void bind(Cliente cliente) {
            tvNome.setText(cliente.getNomeFantasia());
            tvEndereco.setText(cliente.retornaEnderecoCompleto());

            String documento = cliente.personType() == EnumRegisterPersonType.JURIDICAL?
                    "CNPJ: "+ StringUtils.maskCpfCnpj(cliente.getCpf_cnpj()):"CPF: "+ StringUtils.maskCpfCnpj(cliente.getCpf_cnpj());

            String codigo = itemView.getContext()
                    .getString(R.string.cliente_lista_item_tv_codigo, cliente.retornaCodigoExibicao(), documento );
            tvCodigoSgv.setText(codigo);

            if (cliente.isConsignacaoAtiva())
                tvConsignacaoAtiva.setVisibility(View.VISIBLE);
            else
                tvConsignacaoAtiva.setVisibility(View.INVISIBLE);

            criarEventos(cliente);
        }


        private void criarEventos(Cliente cliente) {
            final Disposable disposable = RxView.clicks(itemView)
                    .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                    .subscribe(view -> {
                        if (func != null) {
                            func.execute(cliente);
                        }
                    }, error -> {
                    });
            compositeDisposable.add(disposable);

            tvConsignacaoAtiva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ConsignadoActivity.class);
                    intent.putExtra("IdVisita", 0);
                    intent.putExtra("IdCliente", cliente.getId());
                    intent.putExtra("Operacao", 2);
                    ((Activity) v.getContext()).startActivity(intent);
                }
            });
        }
    }
}
