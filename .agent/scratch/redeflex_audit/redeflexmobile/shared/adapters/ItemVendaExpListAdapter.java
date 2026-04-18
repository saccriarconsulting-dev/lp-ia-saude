package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class ItemVendaExpListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<ItemVenda> listaItens;

    public ItemVendaExpListAdapter(Context context, ArrayList<ItemVenda> itens) {
        this.mContext = context;
        this.listaItens = itens;
    }

    @Override
    public int getGroupCount() {
        return this.listaItens.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listaItens.get(groupPosition).getCodigosList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listaItens.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listaItens.get(groupPosition).getCodigosList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return this.listaItens.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_venda_agrup, null);

        final ItemVenda item = listaItens.get(groupPosition);
        if (item != null) {
            ((TextView) convertView.findViewById(R.id.lbl_nome)).setText(item.getIdProduto() + " - " + item.getNomeProduto());
            ((TextView) convertView.findViewById(R.id.lbl_valorUN)).setText(Util_IO.formataValor(item.getValorUN()));
            ((TextView) convertView.findViewById(R.id.lbl_qtde)).setText(String.valueOf(item.getQtde()));

            Double valor = Double.valueOf(Util_IO.formataValor(item.getValorUN()).replace(".", "").replace(",", "."));
            ((TextView) convertView.findViewById(R.id.lbl_total)).setText("Total R$ " + Util_IO.formataValor(valor * item.getQtde()));

            ImageButton imgbtn_delete = (ImageButton) convertView.findViewById(R.id.imgbtn_delete);
            imgbtn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Alerta alerta = new Alerta(mContext, "Excluir", "Deseja remover o item?");
                    alerta.showConfirm(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Utilidades.removerItemVenda(mContext, item.getIdProduto(), item.getQtde(), item.getId());
                                listaItens.remove(item);
                                notifyDataSetChanged();
                            } catch (Exception ex) {
                                Mensagens.mensagemErro(mContext, ex.getMessage(), false);
                            }
                        }
                    }, null);
                }
            });
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_codigobarra_range, null);

        CodBarra item = listaItens.get(groupPosition).getCodigosList().get(childPosition);
        ((TextView) convertView.findViewById(R.id.lbInicial)).setText(item.getCodBarraInicial());
        ((TextView) convertView.findViewById(R.id.lbFinal)).setText(item.getCodBarraFinal());
        ((TextView) convertView.findViewById(R.id.lbQuantidadeRange)).setText(item.retornaQuantidade(UsoCodBarra.GERAL));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}