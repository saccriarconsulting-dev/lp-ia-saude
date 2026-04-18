package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorCreditoEfetivo;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

public class ItemSolicitacaoPidCustoEfetivoAdapter extends RecyclerView.Adapter<ItemSolicitacaoPidCustoEfetivoAdapter.ViewItemCustoEfetivoHolder> {
    private Context mContext;
    private ArrayList<SolicitacaoPidSimuladorCreditoEfetivo> mLista;
    private ArrayList<FlagsBank> mListaBandeiras;

    public ItemSolicitacaoPidCustoEfetivoAdapter(ArrayList<SolicitacaoPidSimuladorCreditoEfetivo> pLista, Context pContext, ArrayList<FlagsBank> pListaBandeiras) {
        mContext = pContext;
        mLista = pLista;
        mListaBandeiras = pListaBandeiras;
    }

    @NonNull
    @Override
    public ItemSolicitacaoPidCustoEfetivoAdapter.ViewItemCustoEfetivoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_solicitacaopid_creditoefetivo, parent, false);
        return new ItemSolicitacaoPidCustoEfetivoAdapter.ViewItemCustoEfetivoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSolicitacaoPidCustoEfetivoAdapter.ViewItemCustoEfetivoHolder holder, int position) {
        SolicitacaoPidSimuladorCreditoEfetivo item = mLista.get(position);

        // Monta cabeçalho
        if (position == 0) {
            holder.llCabecalho.setVisibility(View.VISIBLE);
            holder.imgBand1.setImageBitmap(decodeImageBitmap(mListaBandeiras.get(0).getBase64Image()));
            holder.imgBand2.setImageBitmap(decodeImageBitmap(mListaBandeiras.get(1).getBase64Image()));
            holder.imgBand3.setImageBitmap(decodeImageBitmap(mListaBandeiras.get(2).getBase64Image()));
            holder.imgBand4.setImageBitmap(decodeImageBitmap(mListaBandeiras.get(3).getBase64Image()));
            holder.imgBand5.setImageBitmap(decodeImageBitmap(mListaBandeiras.get(4).getBase64Image()));
        }
        else {
            holder.llCabecalho.setVisibility(View.GONE);
        }

        holder.tvDescricao.setText(item.getDescricao());
        holder.tvTaxaBand1.setText(Util_IO.formataValor(item.getTaxaBand1()));
        holder.tvTaxaBand2.setText(Util_IO.formataValor(item.getTaxaBand2()));
        holder.tvTaxaBand3.setText(Util_IO.formataValor(item.getTaxaBand3()));
        holder.tvTaxaBand4.setText(Util_IO.formataValor(item.getTaxaBand4()));
        holder.tvTaxaBand5.setText(Util_IO.formataValor(item.getTaxaBand5()));
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    public class ViewItemCustoEfetivoHolder extends RecyclerView.ViewHolder {
        TextView tvDescricao, tvTaxaBand1, tvTaxaBand2, tvTaxaBand3, tvTaxaBand4, tvTaxaBand5;
        ImageView imgBand1, imgBand2, imgBand3, imgBand4, imgBand5;
        LinearLayout llCabecalho;

        ViewItemCustoEfetivoHolder(@NonNull View itemView) {
            super(itemView);
            llCabecalho = itemView.findViewById(R.id.itemsolPidCusto_cabecalho);

            tvDescricao = itemView.findViewById(R.id.itemsolPidCusto_tvDescricao);
            tvTaxaBand1 = itemView.findViewById(R.id.itemsolPidCusto_tvTaxaBand1);
            tvTaxaBand2 = itemView.findViewById(R.id.itemsolPidCusto_tvTaxaBand2);
            tvTaxaBand3 = itemView.findViewById(R.id.itemsolPidCusto_tvTaxaBand3);
            tvTaxaBand4 = itemView.findViewById(R.id.itemsolPidCusto_tvTaxaBand4);
            tvTaxaBand5 = itemView.findViewById(R.id.itemsolPidCusto_tvTaxaBand5);

            imgBand1 = itemView.findViewById(R.id.itemsolPidCusto_imgBand1);
            imgBand2 = itemView.findViewById(R.id.itemsolPidCusto_imgBand2);
            imgBand3 = itemView.findViewById(R.id.itemsolPidCusto_imgBand3);
            imgBand4 = itemView.findViewById(R.id.itemsolPidCusto_imgBand4);
            imgBand5 = itemView.findViewById(R.id.itemsolPidCusto_imgBand5);
        }
    }

    private Bitmap decodeImageBitmap(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
