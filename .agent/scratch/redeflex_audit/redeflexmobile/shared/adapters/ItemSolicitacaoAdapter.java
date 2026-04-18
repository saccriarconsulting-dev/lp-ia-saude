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
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.ui.component.NumberPickerListener;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 05/04/2016.
 */
public class ItemSolicitacaoAdapter extends ArrayAdapter<Produto> {
    public interface OnListener {
        void onUpdateItem(Produto item, int newqtde);
    }

    public OnListener myListener;
    Context mContext;
    int layoutResourceId;
    private ArrayList<Produto> listaProdutos;

    public ItemSolicitacaoAdapter(Context mContext, int layoutResourceId, ArrayList<Produto> data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.listaProdutos = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        final Produto objectItem = listaProdutos.get(position);
        TextView txtnome = convertView.findViewById(R.id.lbl_nome);
        TextView txtqtdesug = convertView.findViewById(R.id.lbl_qtde_sug);
        final com.axys.redeflexmobile.ui.component.NumberPicker npQtde = convertView.findViewById(R.id.npQtde);
        ImageButton imgbtn_delete = convertView.findViewById(R.id.imgbtn_delete);

        txtnome.setText(objectItem.getNome());
        txtqtdesug.setText(String.valueOf(objectItem.getEstoqueSugerido()));

        imgbtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alerta alerta = new Alerta(mContext, "Excluir", "Deseja remover o item?");
                alerta.showConfirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listaProdutos.remove(objectItem);
                        notifyDataSetChanged();
                    }
                }, null);
            }
        });

        npQtde.setOnNumberPickerListener(new NumberPickerListener() {
            @Override
            public void OnChangeNumber(int number) {
                objectItem.setQtde(number);
                notifyDataSetChanged();
                if (myListener != null) {
                    myListener.onUpdateItem(objectItem, number);
                }
            }
        });

        if (objectItem.getQtde() == 0)
            objectItem.setQtde(objectItem.getEstoqueSugerido() == 0 ? 1 : objectItem.getEstoqueSugerido());

        npQtde.setNumber(objectItem.getQtde());
        convertView.setTag(objectItem);
        return convertView;
    }
}