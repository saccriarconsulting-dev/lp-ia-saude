package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.painelRelatorioMeta;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 20/05/2016.
 */
public class PainelRelatorioMetaAdapter extends ArrayAdapter<painelRelatorioMeta> {
    private Context mContext;
    private ArrayList<painelRelatorioMeta> mLista = new ArrayList<>();

    public PainelRelatorioMetaAdapter(Context pContext, int layoutResourceId, ArrayList<painelRelatorioMeta> pLista) {
        super(pContext, layoutResourceId, pLista);
        mContext = pContext;
        mLista = pLista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        painelRelatorioMeta objectItem = mLista.get(position);
        if (objectItem.isIndicador()) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_relatorio_meta_01, parent, false);

            ((TextView) convertView.findViewById(R.id.txt_titulo)).setText(objectItem.getTexto());
        } else {
            if (objectItem.isHeader()) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(R.layout.item_relatorio_meta_03, parent, false);

                if (objectItem.getTendencia()>0)
                    ((TextView) convertView.findViewById(R.id.txt_tpgrafico)).setText("% Tendência");
                else
                    ((TextView) convertView.findViewById(R.id.txt_tpgrafico)).setText("% Realizado");

            } else {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(R.layout.item_relatorio_meta_02, parent, false);

                ImageView img = convertView.findViewById(R.id.imageView);
                NumberFormat numberFormat = NumberFormat.getNumberInstance();

                ((TextView) convertView.findViewById(R.id.txt_meta)).setText(numberFormat.format(objectItem.getMeta()));
                ((TextView) convertView.findViewById(R.id.txt_realizado)).setText(numberFormat.format(objectItem.getRealizado()));

                int valoraux = objectItem.getMeta() - objectItem.getRealizado();
                if (valoraux < 0)
                    valoraux = 0;

                ((TextView) convertView.findViewById(R.id.txt_falta)).setText(numberFormat.format(valoraux));

                ProgressBar progressBar = convertView.findViewById(R.id.pb_progresso);
                valoraux = (int) ((((double) objectItem.getRealizado()) / ((double) objectItem.getMeta())) * 100);
                if (objectItem.getMeta() == 0 && objectItem.getRealizado() > 0) {
                    valoraux = 100;
                }

                if (objectItem.getTendencia()>0){
                    valoraux = (int) objectItem.getTendencia();
                }

                progressBar.setProgress(valoraux);
                TextView txtPorcento = convertView.findViewById(R.id.txt_pb);
                txtPorcento.setText(valoraux + "%");

                TextView t = convertView.findViewById(R.id.txt_op);
                t.setText(objectItem.getTexto());

                switch (objectItem.getIdoperadora()) {
                    case 1:
                        SetarBackGroundProgressbar(progressBar, valoraux);
                        break;
                    case 17:
                        img.setVisibility(View.VISIBLE);
                        t.setVisibility(View.GONE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.oi, mContext.getTheme()));
                        } else {
                            img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.oi));
                        }
                        SetarBackGroundProgressbar(progressBar, valoraux);
                        break;
                    case 4:
                        SetarBackGroundProgressbar(progressBar, valoraux);
                        break;
                    case 25:
                        img.setVisibility(View.VISIBLE);
                        t.setVisibility(View.GONE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.tim, mContext.getTheme()));
                        } else {
                            img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.tim));
                        }
                        SetarBackGroundProgressbar(progressBar, valoraux);
                        break;
                    case 2:
                        SetarBackGroundProgressbar(progressBar, valoraux);
                        break;
                    case 5:
                        img.setVisibility(View.VISIBLE);
                        t.setVisibility(View.GONE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.claro, mContext.getTheme()));
                        } else {
                            img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.claro));
                        }
                        SetarBackGroundProgressbar(progressBar, valoraux);
                        break;
                    case 3:
                        SetarBackGroundProgressbar(progressBar, valoraux);
                        break;
                    case 26:
                        img.setVisibility(View.VISIBLE);
                        t.setVisibility(View.GONE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.vivo, mContext.getTheme()));
                        } else {
                            img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.vivo));
                        }
                        SetarBackGroundProgressbar(progressBar, valoraux);
                        break;
                    default:
                        img.setVisibility(View.VISIBLE);
                        t.setVisibility(View.GONE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            img.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_icone_new, mContext.getTheme()));
                        } else {
                            img.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_icone_new));
                        }
                        SetarBackGroundProgressbar(progressBar, valoraux);
                        break;
                }

                if ((position % 2) == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        convertView.setBackgroundColor(mContext.getResources().getColor(R.color.cor_cinza, mContext.getTheme()));
                    else
                        convertView.setBackgroundColor(mContext.getResources().getColor(R.color.cor_cinza));
                }
            }
        }
        return convertView;
    }

    private void SetarBackGroundProgressbar(ProgressBar pProgress, int vValor)
    {
        if (vValor >= 100)
            pProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_verde, mContext.getTheme()));
        else if (vValor > 70 && vValor < 100)
            pProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_amarelo, mContext.getTheme()));
        else
            pProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_vermelho, mContext.getTheme()));
    }
}