package com.axys.redeflexmobile.shared.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.ClienteCadastroPOS;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.shared.util.PayEditTextWatcher;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rogério Massa on 26/10/18.
 */
public class CadastroClienteModeloPOSAdapter extends RecyclerView.Adapter<CadastroClienteModeloPOSAdapter.CadastroClienteModeloPOSViewHolder> {

    private Context context;
    private List<ClienteCadastroPOS> modelos;
    private TextView totalField;
    private LinearLayout llDivider;

    public CadastroClienteModeloPOSAdapter(Context context, TextView totalField, LinearLayout llDivider) {
        this.context = context;
        this.modelos = new ArrayList<>();
        this.totalField = totalField;
        this.llDivider = llDivider;
    }

    public List<ClienteCadastroPOS> getModelos() {
        return modelos;
    }

    public void setModelo(ModeloPOS modelo) {
        if (modelo == null) return;
        if (this.modelos == null) this.modelos = new ArrayList<>();

        ClienteCadastroPOS objeto = new ClienteCadastroPOS();
        objeto.setIdTipoMaquina(modelo.getIdTipoMaquina());
        objeto.setValorAluguel(modelo.getValorAluguelPadrao());
        objeto.setPosDescricao(modelo.getDescricao());
        objeto.setPosModelo(modelo.getModelo());
        this.modelos.add(objeto);

        notifyItemInserted(this.modelos.size() - 1);
        this.alterarValorTotal();
    }

    public void setModelos(List<ClienteCadastroPOS> modelos) {
        this.modelos = modelos;
        notifyDataSetChanged();
        this.alterarValorTotal();
    }

    @NonNull
    @Override
    public CadastroClienteModeloPOSViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CadastroClienteModeloPOSViewHolder(LayoutInflater.from(context).inflate(
                R.layout.activity_cadastro_cliente_contrato_aluguel_item, viewGroup, false),
                obterCallback());
    }

    @Override
    public void onBindViewHolder(@NonNull CadastroClienteModeloPOSViewHolder holder, int i) {
        holder.bind(modelos.get(i), i);
    }

    @Override
    public int getItemCount() {
        return modelos.size();
    }

    @SuppressLint("DefaultLocale")
    private void alterarValorTotal() {
        double total = 0;
        for (ClienteCadastroPOS modelo : modelos) {
            total += modelo.getValorAluguel();
        }
        boolean valido = total > 0;
        totalField.setText(valido ? Util_IO.formatDoubleToDecimalNonDivider(total) : "-");
        llDivider.setVisibility(valido ? View.VISIBLE : View.GONE);
    }

    private CadastroClienteModeloPOSViewHolderCallback obterCallback() {
        return new CadastroClienteModeloPOSViewHolderCallback() {
            @Override
            public void editTotalValue(int position, String value) {
                ClienteCadastroPOS modeloPOS = modelos.get(position);
                if (modeloPOS == null) return;
                try {
                    int valor = Integer.parseInt(value.replaceAll("[\\D]", ""));
                    value = String.format(Locale.getDefault(), "%.2f", valor * 0.01);
                    value = value.replace(",", ".");
                } catch (Exception e) {
                    value = value.replace(".", "");
                    value = value.replace(",", ".");
                }
                modeloPOS.setValorAluguel(Double.parseDouble(value));
                modelos.set(position, modeloPOS);
                alterarValorTotal();
            }

            @Override
            public void removePOS(int position) {
                modelos.remove(position);
                notifyItemRemoved(position);
                alterarValorTotal();
            }
        };
    }

    class CadastroClienteModeloPOSViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPOSDescricao) TextView tvPOSDescricao;
        @BindView(R.id.tvPOSModelo) TextView tvPOSModelo;
        @BindView(R.id.etPOSValor) EditText etPOSValor;
        @BindView(R.id.ivPOSDeletar) ImageView ivPOSDelete;

        private CadastroClienteModeloPOSViewHolderCallback callback;

        private CadastroClienteModeloPOSViewHolder(@NonNull View itemView,
                                                   CadastroClienteModeloPOSViewHolderCallback callback) {
            super(itemView);
            this.callback = callback;
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        private void bind(ClienteCadastroPOS modeloPOS, int position) {
            if (modeloPOS == null) return;

            tvPOSDescricao.setText(modeloPOS.getPosDescricao());
            tvPOSModelo.setText(modeloPOS.getPosModelo());
            etPOSValor.setText(String.format("%.2f", modeloPOS.getValorAluguel()));

            PayEditTextWatcher watcher = new PayEditTextWatcher(etPOSValor, "%,.2f");
            watcher.setCallback((texto) -> callback.editTotalValue(getAdapterPosition(), texto));
            etPOSValor.addTextChangedListener(watcher);

            ivPOSDelete.setOnClickListener(v -> callback.removePOS(getAdapterPosition()));
        }
    }

    private interface CadastroClienteModeloPOSViewHolderCallback {
        void editTotalValue(int position, String value);

        void removePOS(int position);
    }
}
