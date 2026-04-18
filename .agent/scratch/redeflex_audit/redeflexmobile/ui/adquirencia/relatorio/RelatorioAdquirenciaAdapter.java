package com.axys.redeflexmobile.ui.adquirencia.relatorio;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Adquirencia;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.axys.redeflexmobile.shared.util.Util_IO.formatToDecimal;

/**
 * @author Vitor Herrmann on 04/01/19.
 */
public class RelatorioAdquirenciaAdapter extends RecyclerView.Adapter<RelatorioAdquirenciaAdapter.RelatorioAdquirenciaViewHolder> {

    private Context context;
    private List<Adquirencia> adquirenciaList;

    RelatorioAdquirenciaAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Adquirencia> adquirenciaList) {
        this.adquirenciaList = adquirenciaList;
    }

    @NonNull
    @Override
    public RelatorioAdquirenciaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_relatorio_adquirencia, viewGroup, false);
        return new RelatorioAdquirenciaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatorioAdquirenciaViewHolder holder, int posicao) {

        Adquirencia adquirencia = adquirenciaList.get(posicao);
        holder.tvTitulo.setText(adquirencia.getIndicador());
        holder.tvMeta.setText(String.valueOf(formatToDecimal(adquirencia.getMeta())));
        holder.tvRealizado.setText(String.valueOf(formatToDecimal(adquirencia.getRealizado())));
        holder.tvFalta.setText(String.valueOf(formatToDecimal(adquirencia.getFalta())));

        int porcentagem = 0;
        if (adquirencia.getMeta() != 0) {
            try {
                porcentagem = (int) Math.ceil((adquirencia.getRealizado() * 100) / adquirencia.getMeta());
                porcentagem = porcentagem > 100 ? 100 : porcentagem;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.tvPorcentagem.setText(porcentagem + "%");
        holder.pbPorcentagem.setProgress(porcentagem);
    }

    @Override
    public int getItemCount() {
        return adquirenciaList.size();
    }

    static class RelatorioAdquirenciaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.relatorio_adquirencia_tv_titulo) TextView tvTitulo;
        @BindView(R.id.relatorio_adquirencia_tv_meta) TextView tvMeta;
        @BindView(R.id.relatorio_adquirencia_tv_realizado) TextView tvRealizado;
        @BindView(R.id.relatorio_adquirencia_tv_falta) TextView tvFalta;
        @BindView(R.id.relatorio_adquirencia_pb) ProgressBar pbPorcentagem;
        @BindView(R.id.relatorio_adquirencia_tv_porcentagem) TextView tvPorcentagem;

        RelatorioAdquirenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
