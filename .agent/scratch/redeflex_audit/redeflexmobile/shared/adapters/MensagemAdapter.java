package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBMensagem;
import com.axys.redeflexmobile.shared.models.Mensagem;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 03/06/2016.
 */
public class MensagemAdapter extends ArrayAdapter<Mensagem> {
    private ArrayList<Mensagem> listaMensagens;
    private Context mContext;

    public MensagemAdapter(Context context, int resourceId, ArrayList<Mensagem> objects) {
        super(context, resourceId, objects);
        this.mContext = context;
        this.listaMensagens = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Mensagem objectItem = listaMensagens.get(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_mensagem, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.txtMensagem)).setText(objectItem.getTexto());
        ((TextView) convertView.findViewById(R.id.txtDataMensagem)).setText(Util_IO.dateTimeToString(objectItem.getData()));
        ImageButton imgbtn_delete = (ImageButton) convertView.findViewById(R.id.imgbtn_delete);
        imgbtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alerta alerta = new Alerta(mContext, "Excluir", "Deseja remover o item?");
                alerta.showConfirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new DBMensagem(mContext).delete(objectItem.getId());
                            listaMensagens.remove(objectItem);
                            notifyDataSetChanged();
                        } catch (Exception ex) {
                            Mensagens.mensagemErro(mContext, ex.getMessage(), false);
                        }
                    }
                }, null);
            }
        });

        if (objectItem.getPermiteDeletar())
            imgbtn_delete.setVisibility(View.VISIBLE);
        else
            imgbtn_delete.setVisibility(View.GONE);

        convertView.setTag(objectItem.getId());
        return convertView;
    }
}