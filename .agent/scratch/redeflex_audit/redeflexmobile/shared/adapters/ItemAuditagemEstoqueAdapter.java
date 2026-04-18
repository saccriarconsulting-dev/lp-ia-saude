package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.AuditagemEstoque;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.ProdutoAuditagemActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 21/06/2016.
 */
public class ItemAuditagemEstoqueAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<AuditagemEstoque> listaAuditagem;
    private Boolean bExcluir;
    private boolean auditagemEstoque;

    public ItemAuditagemEstoqueAdapter(Context context, ArrayList<AuditagemEstoque> itens, Boolean exibir_imagem) {
        this.mContext = context;
        this.listaAuditagem = itens;
        this.bExcluir = exibir_imagem;
    }

    public ItemAuditagemEstoqueAdapter(Context context, ArrayList<AuditagemEstoque> itens, Boolean exibir_imagem, boolean auditagemEstoque) {
        this.mContext = context;
        this.listaAuditagem = itens;
        this.bExcluir = exibir_imagem;
        this.auditagemEstoque = auditagemEstoque;
    }

    @Override
    public int getGroupCount() {
        return this.listaAuditagem.size();
    }

    public int getChildrenCount(int groupPosition) {
        return listaAuditagem.get(groupPosition).getCodigosList().size();
    }

    public Object getGroup(int groupPosition) {
        return this.listaAuditagem.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return this.listaAuditagem.get(groupPosition).getCodigosList().get(childPosition);
    }

    public long getGroupId(int groupPosition) {
        return listaAuditagem.get(groupPosition).getId();
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

        final AuditagemEstoque obj = listaAuditagem.get(groupPosition);

        if (auditagemEstoque) {
            if (obj.getId() == -1) {
                convertView.findViewById(R.id.auditagem_layout_principal).setVisibility(View.GONE);
                convertView.findViewById(R.id.auditagem_layout_estoque).setVisibility(View.GONE);
                convertView.findViewById(R.id.auditagem_layout_total).setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.auditagem_total)).setText(Util_IO.formatDoubleToDecimalNonDivider(getTotalDivergente(listaAuditagem)));
            } else {
                convertView.findViewById(R.id.auditagem_layout_principal).setVisibility(View.GONE);
                convertView.findViewById(R.id.auditagem_layout_estoque).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.auditagem_layout_total).setVisibility(View.GONE);
                preencherValoresAuditagemEstoque(convertView, obj);
            }
        } else {
            TextView produto = convertView.findViewById(R.id.txtProduto);
            TextView quantidade = convertView.findViewById(R.id.txtQuantidade);

            produto.setText(obj.getNomeProduto());
            quantidade.setText(String.valueOf(obj.getQtdeInformada()));

            ImageButton imgbtn_delete = convertView.findViewById(R.id.imgbtn_delete);
            imgbtn_delete.setOnClickListener(v -> {
                Alerta alerta = new Alerta(mContext, "Excluir", "Deseja remover o produto " + obj.getNomeProduto() + "?");
                alerta.showConfirm((dialog, which) -> {
                    try {
                        DBEstoque dbEstoque = new DBEstoque(mContext);
                        dbEstoque.removerQuantidadeAuditagem(String.valueOf(obj.getId()));
                        dbEstoque.removerIccidAuditagem(String.valueOf(obj.getId()));
                        listaAuditagem.remove(obj);
                        notifyDataSetChanged();
                    } catch (Exception ex) {
                        Alerta alerta1 = new Alerta(mContext, "Erro", ex.getMessage());
                        alerta1.show();
                        return;
                    }
                }, null);
            });

            if (bExcluir)
                imgbtn_delete.setVisibility(View.VISIBLE);
            else
                imgbtn_delete.setVisibility(View.GONE);

            ImageView btnAuditagemProduto = (ImageView)convertView.findViewById(R.id.imgbtn_edit);
            btnAuditagemProduto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent fDadosAuditagemProduto = new Intent(view.getContext(), ProdutoAuditagemActivity.class);
                        fDadosAuditagemProduto.putExtra("CodigoProduto", obj.getIdProduto());
                        fDadosAuditagemProduto.putExtra("idAuditagem", String.valueOf(obj.getId()));
                        ((Activity) mContext).startActivityForResult(fDadosAuditagemProduto, RequestCode.ProdutoVenda);
                    } catch (Exception ex) {
                        Alerta alert = new Alerta(view.getContext(), "Erro", "Erro: " + ex.getMessage());
                        alert.show();
                        return;
                    }
                }
            });

            // Verificar se produto tem bipagem ou não
            DBEstoque dbEstoque = new DBEstoque(mContext);
            Produto produtoVO = dbEstoque.getProdutoById(obj.getIdProduto());
            if (produtoVO == null)
                btnAuditagemProduto.setVisibility(View.GONE);
            else if (produtoVO.getBipagem().equals("S"))
                btnAuditagemProduto.setVisibility(View.VISIBLE);
            else
                btnAuditagemProduto.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void preencherValoresAuditagemEstoque(View convertView, AuditagemEstoque auditagemEstoque) {
        TextView tvDescricao = convertView.findViewById(R.id.auditagem_descricao_produto);
        TextView tvEstoque = convertView.findViewById(R.id.auditagem_tv_estoque);
        TextView tvAuditado = convertView.findViewById(R.id.auditagem_tv_autitado);
        TextView tvDivergencia = convertView.findViewById(R.id.auditagem_tv_divergencia);
        TextView tvValorUnitario = convertView.findViewById(R.id.auditagem_tv_valor_unitario);
        TextView tvValorDivergencia = convertView.findViewById(R.id.auditagem_tv_valor_divergencia);

        int divergencia = auditagemEstoque.getQtdeReal() - auditagemEstoque.getQtdeInformada();

        tvDescricao.setText(auditagemEstoque.getNomeProduto());
        tvEstoque.setText(String.valueOf(auditagemEstoque.getQtdeReal()));
        tvAuditado.setText(String.valueOf(auditagemEstoque.getQtdeInformada()));
        tvDivergencia.setText(String.valueOf(divergencia));
        tvValorUnitario.setText(Util_IO.formatDoubleToDecimalNonDivider(auditagemEstoque.getValorUnitario()));
        tvValorDivergencia.setText(Util_IO.formatDoubleToDecimalNonDivider(auditagemEstoque.getValorUnitario() * divergencia));

        if (divergencia == 0) {
            tvDescricao.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            tvDescricao.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_auditagem_wrapped, 0);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_codigobarra_range, null);

        AuditagemEstoque obj = listaAuditagem.get(groupPosition);
        CodBarra item = listaAuditagem.get(groupPosition).getCodigosList().get(childPosition);
        ((TextView) convertView.findViewById(R.id.lbInicial)).setText(item.getCodBarraInicial());
        ((TextView) convertView.findViewById(R.id.lbFinal)).setText(item.getCodBarraFinal());
        ((TextView) convertView.findViewById(R.id.lbQuantidadeRange)).setText(item.retornaQuantidade(UsoCodBarra.GERAL, listaAuditagem.get(groupPosition).getIdProduto(), convertView.findViewById(R.id.lbQuantidadeRange).getContext()));

        ImageButton imgbtn_delete = convertView.findViewById(R.id.imgbtn_deleteRange);
        imgbtn_delete.setOnClickListener(v -> {
            Alerta alerta = new Alerta(mContext, "Excluir", "Deseja remover o código de barras?");
            alerta.showConfirm((dialog, which) -> {
                try {
                    DBEstoque dbEstoque = new DBEstoque(mContext);
                    // Atualiza Quantidade na Tabela AuditagemEstoque e Atualiza a Lista da Grid
                    int qtdTotalProduto = Integer.valueOf(item.retornaQuantidade(UsoCodBarra.GERAL, listaAuditagem.get(groupPosition).getIdProduto(),mContext)) * -1;
                    dbEstoque.atualizarQuantidadeAuditagem(obj.getId(), qtdTotalProduto);

                    int qtdViewAtualiza = listaAuditagem.get(groupPosition).getQtdeInformada() + qtdTotalProduto;
                    listaAuditagem.get(groupPosition).setQtdeInformada(qtdViewAtualiza);

                    // Remove o Item da tabela AuditagemEstoqueCodigoBarras e Atualiza Lista da Grid
                    dbEstoque.removerAuditagemPistolagem(item.getIdPistolagem());
                    listaAuditagem.get(groupPosition).getCodigosList().remove(childPosition);

                    // Remove grupo da listagem
                    if (listaAuditagem.get(groupPosition).getCodigosList().size() <= 0) {
                        dbEstoque.removerQuantidadeAuditagem(String.valueOf(listaAuditagem.get(groupPosition).getId()));
                        listaAuditagem.remove(groupPosition);
                    }

                    notifyDataSetChanged();
                } catch (Exception ex) {
                    Alerta alerta1 = new Alerta(mContext, "Erro", ex.getMessage());
                    alerta1.show();
                }
            }, null);
        });

        if (bExcluir)
            imgbtn_delete.setVisibility(View.VISIBLE);
        else
            imgbtn_delete.setVisibility(View.GONE);

        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private double getTotalDivergente(ArrayList<AuditagemEstoque> listItens) {
        double total = 0;

        for (AuditagemEstoque item : listItens) {
            total += item.getValorUnitario() * (item.getQtdeReal() - item.getQtdeInformada());
        }

        return total;
    }
}
