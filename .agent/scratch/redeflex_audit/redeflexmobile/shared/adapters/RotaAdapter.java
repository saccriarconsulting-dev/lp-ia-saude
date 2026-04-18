package com.axys.redeflexmobile.shared.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Rota;
import com.axys.redeflexmobile.shared.util.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RotaAdapter extends ArrayAdapter<Rota> {
    private final ArrayList<Rota> listaRota;
    private final Context mContext;
    private final int layoutResourceId;

    public RotaAdapter(Context context, int resourceId, ArrayList<Rota> objects) {
        super(context, resourceId, objects);
        this.layoutResourceId = resourceId;
        this.mContext = context;
        this.listaRota = objects;
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public @NotNull View getView(int position, View convertView, @NotNull ViewGroup parent) {
        Rota objectItem = listaRota.get(position);

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.txtCliente)).setText(objectItem.getCliente());
        ((TextView) convertView.findViewById(R.id.txtEnderecoCliente)).setText(objectItem.getEndereco());
        String documento = objectItem.getTipoDocumento()+": "+ StringUtils.maskCpfCnpj(objectItem.getDocumento());
        ((TextView) convertView.findViewById(R.id.txtTipoCliente)).setText("Código: " + objectItem.getExibirCodigo()+" / "+documento);

        ImageView img = convertView.findViewById(R.id.img);
        if (objectItem.getIdVisita() == 0) {
            img.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_check_cinza, mContext.getTheme()));
        } else {
            img.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_check_ok, mContext.getTheme()));
        }

        convertView.setTag(
                objectItem.getIdCliente() + "," +
                        objectItem.getLongitude() + "," +
                        objectItem.getLatitude() + "," +
                        objectItem.getDiaSemana() + "," +
                        objectItem.getSemana()
        );
        return convertView;
    }
}
