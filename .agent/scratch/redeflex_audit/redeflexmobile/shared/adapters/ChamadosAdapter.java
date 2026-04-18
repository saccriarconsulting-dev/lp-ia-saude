package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.util.Util_IO;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by joao.viana on 07/03/2017.
 */

public class ChamadosAdapter extends ArrayAdapter<Chamado> {
    private ArrayList<Chamado> mLista;
    private Context mContext;
    int layoutResourceId;

    public ChamadosAdapter(Context pContext, int resourceId, ArrayList<Chamado> pLista) {
        super(pContext, resourceId, pLista);
        this.layoutResourceId = resourceId;
        this.mContext = pContext;
        this.mLista = pLista;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Chamado objectItem = mLista.get(position);
        final String TITLE_SEPARATOR = " - ";

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        String title = String.valueOf(objectItem.getChamadoID()) + TITLE_SEPARATOR + objectItem.getAssunto();
        ((TextView) convertView.findViewById(R.id.txtChamado)).setText(title);
        ((TextView) convertView.findViewById(R.id.txtUsuario)).setText(objectItem.getSolicitante());

        TextView txtNomeFantasia = (TextView) convertView.findViewById(R.id.txtNomeFantasia);
        txtNomeFantasia.setText(objectItem.getNomeFantasiaCliente());
        txtNomeFantasia.setVisibility(StringUtils.isEmpty(objectItem.getNomeFantasiaCliente()) ? View.GONE : View.VISIBLE);

        this.prepareImageView(objectItem, (ImageView) convertView.findViewById(R.id.imagemChamado));

        ((TextView) convertView.findViewById(R.id.txtSituacao)).setText(objectItem.retornaSituacao());
        ((TextView) convertView.findViewById(R.id.txtDataAbertura)).setText(Util_IO.dateTimeToStringBr(objectItem.getDataCadastro()));
        ((TextView) convertView.findViewById(R.id.txtUltima)).setText(Util_IO.dateTimeToStringBr(objectItem.getDataAlteracao()));

        this.setMargins(convertView, position);
        convertView.setTag(objectItem.getIdAppMobile());
        return convertView;
    }

    private void prepareImageView(Chamado objectItem, ImageView imageView) {
        switch (objectItem.getStatusID()) {
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_relogio, mContext.getTheme()));
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_relogio));
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_chat, mContext.getTheme()));
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_chat));
                break;
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_relogio, mContext.getTheme()));
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_relogio));
                break;
            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_relogio, mContext.getTheme()));
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_relogio));
                break;
            case 5:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_relogio, mContext.getTheme()));
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_relogio));
                break;
            case 6:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_check_ok, mContext.getTheme()));
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_check_ok));
                break;
            case 7:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_cancelar, mContext.getTheme()));
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_cancelar));
                break;
            case 8:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_relogio, mContext.getTheme()));
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_relogio));
                break;
        }
    }

    private void setMargins(View view, int position) {
        LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        int marginHalf = (int) view.getResources().getDimension(R.dimen.chamados_card_margin_half);
        int marginFull = (int) view.getResources().getDimension(R.dimen.chamados_card_margin_full);

        if (position == 0) {
            layoutParams.setMargins(marginFull, marginFull, marginFull, marginHalf);
        } else if (position + 1 < mLista.size()) {
            layoutParams.setMargins(marginFull, marginHalf, marginFull, marginHalf);
        } else {
            layoutParams.setMargins(marginFull, marginHalf, marginFull, marginFull);
        }

        view.findViewById(R.id.cardContainer).setLayoutParams(layoutParams);
    }
}