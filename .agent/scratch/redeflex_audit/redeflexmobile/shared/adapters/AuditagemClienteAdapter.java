package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.ui.venda.auditagem.AuditagemPdvActivity;

import java.util.ArrayList;

/**
 * Created by joao.viana on 11/01/2017.
 */

public class AuditagemClienteAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<AuditagemCliente> mLista;
    private boolean trocarIcone;
    private AuditagemPdvActivity.RemoverProduto removerProduto;

    public AuditagemClienteAdapter(Context pContext, ArrayList<AuditagemCliente> pLista) {
        mContext = pContext;
        mLista = pLista;
    }

    public AuditagemClienteAdapter(Context pContext, ArrayList<AuditagemCliente> pLista, boolean trocarIcone) {
        mContext = pContext;
        mLista = pLista;
        this.trocarIcone = trocarIcone;
    }

    @Override
    public int getGroupCount() {
        return mLista.size();
    }

    public int getChildrenCount(int groupPosition) {
        return mLista.get(groupPosition).getListaCodigos().size();
    }

    public Object getGroup(int groupPosition) {
        return mLista.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mLista.get(groupPosition).getListaCodigos().get(childPosition);
    }

    public long getGroupId(int groupPosition) {
        return mLista.get(groupPosition).getId();
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public boolean hasStableIds() {
        return true;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_auditagem_estoque, null);

        if (this.trocarIcone) {
            ((ImageButton) convertView.findViewById(R.id.imgbtn_delete)).setImageResource(R.drawable.ic_excluir);
        }

        final AuditagemCliente auditagemCliente = mLista.get(groupPosition);

        ((TextView) convertView.findViewById(R.id.txtProduto)).setText(auditagemCliente.getProduto());
        ((TextView) convertView.findViewById(R.id.txtQuantidade)).setText(String.valueOf(auditagemCliente.getQuantidade()));

        ImageButton imgbtn_delete = (ImageButton) convertView.findViewById(R.id.imgbtn_delete);
        imgbtn_delete.setOnClickListener((view) -> {
            Alerta alerta = new Alerta(mContext, mContext.getResources().getString(R.string.app_name), "Deseja realmente excluir o item " + auditagemCliente.getProduto() + " ?");
            alerta.showConfirm((dialog, which) -> {
                new DBEstoque(mContext).deletaAuditagemClienteById(String.valueOf(auditagemCliente.getId()));
                mLista.remove(auditagemCliente);
                notifyDataSetChanged();

                if (removerProduto != null) {
                    removerProduto.onRemover();
                }
            }, null);
        });

        if (!this.trocarIcone && auditagemCliente.isConfirmada()) {
            imgbtn_delete.setVisibility(View.GONE);
        }

        // Retirado edição do item no caso de auditagem por cliente
        ImageView btnAuditagemProduto = (ImageView)convertView.findViewById(R.id.imgbtn_edit);
        btnAuditagemProduto.setVisibility(View.GONE);

        return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_codigobarra_range, null);

        CodBarra item = mLista.get(groupPosition).getListaCodigos().get(childPosition);
        ((TextView) convertView.findViewById(R.id.lbInicial)).setText(item.getCodBarraInicial());
        ((TextView) convertView.findViewById(R.id.lbFinal)).setText(item.getCodBarraFinal());
        ((TextView) convertView.findViewById(R.id.lbQuantidadeRange)).setText(item.retornaQuantidade(UsoCodBarra.AUDITAGEM_CLIENTE));
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void remocaoProdutoAuditagemPdv(AuditagemPdvActivity.RemoverProduto removerProduto) {
        this.removerProduto = removerProduto;
    }
}