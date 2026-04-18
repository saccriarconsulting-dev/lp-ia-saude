package com.axys.redeflexmobile.shared.bd;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.enums.EnumStatusVenda;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCodigoBarra;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.MobileCodBarra;
import com.axys.redeflexmobile.shared.models.OperadoraAtend;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.VendaConsulta;
import com.axys.redeflexmobile.shared.models.VendaConsultaItem;
import com.axys.redeflexmobile.shared.models.VendaMobile;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.VendaActivity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Desenvolvimento on 01/07/2016.
 */
public class DBVenda {

    private Context mContext;
    private String mTabelaVenda = "Venda";
    private String mTabelaItem = "ItemVenda";
    private String mTabelaCodBarra = "ItemVendaCodigoBarra";
    private String mTabelaCombo = "ItemVendaCombo";

    public DBVenda(Context pContext) {
        mContext = pContext;
    }

    public long novaVenda(int pIdVisita, String pIdCliente) {
        ContentValues values = new ContentValues();
        values.put("idVisita", pIdVisita);
        values.put("idCliente", pIdCliente);
        values.put("status", EnumStatusVenda.ANDAMENTO.getValue());
        return SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaVenda, null, values);
    }

    public long novaVendaConsignado(int pIdVisita, String pIdCliente, Consignado pConsignado) {
        ContentValues values = new ContentValues();
        values.put("idVisita", pIdVisita);
        values.put("idCliente", pIdCliente);
        values.put("IdConsignadoRefer", pConsignado.getIdServer());
        values.put("status", EnumStatusVenda.ANDAMENTO.getValue());
        return SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaVenda, null, values);
    }

    public long criarVendaAuditagemEstoque(Venda venda) {
        ContentValues values = new ContentValues();
        values.put("idFormaPagamento", 1);
        values.put("idCliente", venda.getIdCliente());
        values.put("valorTotal", venda.getValorTotal());
        values.put("idVisita", venda.getIdVisita());
        values.put("data", Util_IO.dateTimeToString(venda.getData(), Config.FormatDateTimeStringBanco));
        values.put("status", EnumStatusVenda.ANDAMENTO.getValue());
        values.put("dataVencimento", "");
        values.put("identificadorAuditagem", venda.getIdentificadorAutidagem());
        return SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaVenda, null, values);
    }

    public Venda getVendabyId(int pId) {
        Util_IO.StringBuilder sb = retornaQueryVenda();
        sb.appendLine("AND id = ?");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pId)});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return retornaVenda(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private Venda retornaVenda(Cursor cursor) {
        if (cursor != null) {
            Venda venda = new Venda();
            venda.setId(cursor.getInt(0));
            venda.setIdVisita(cursor.getInt(1));
            venda.setIdCliente(cursor.getString(2));
            venda.setData(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
            venda.setIdFormaPagamento(cursor.getInt(4));
            venda.setDataCobranca(Util_IO.stringToDate(cursor.getString(6), Config.FormatDateStringBanco));
            venda.setValorTotal(retornaValorTotalVenda(cursor.getInt(0)));
            venda.setIdConsignadoRefer(cursor.getInt(9));
            venda.setChaveCobranca(cursor.getString(10));
            venda.setPago(cursor.getInt(11));
            venda.setQrCodeLink(cursor.getString(12));
            venda.setDataExpiracaoPix(Util_IO.stringToDate(cursor.getString(13), Config.FormatDateTimeStringBanco));
            venda.setComprovantePagto(cursor.getString(14));
            return venda;
        } else
            return null;
    }

    private Util_IO.StringBuilder retornaQueryVenda() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine(",idVisita");
        sb.appendLine(",idCliente");
        sb.appendLine(",data");
        sb.appendLine(",IFNULL(idFormaPagamento,0)");
        sb.appendLine(",IFNULL(sync,0)");
        sb.appendLine(",CASE IFNULL(dataVencimento,'') WHEN '' THEN data ELSE dataVencimento END");
        sb.appendLine(",IFNULL(vendaBobina,0)");
        sb.appendLine(",IFNULL(valorTotal,0)");
        sb.appendLine(",IdConsignadoRefer");
        sb.appendLine(",ChaveCobranca");
        sb.appendLine(",IFNULL(Pago,0) as Pago");
        sb.appendLine(",QrCodeLink");
        sb.appendLine(",DataExpiracaoPix");
        sb.appendLine(",ComprovantePagto");
        sb.appendLine("FROM [Venda]");
        sb.appendLine("WHERE 1=1");
        return sb;
    }

    public Venda getVendabyIdVisita(int pIdVisita) {
        Util_IO.StringBuilder sb = retornaQueryVenda();
        sb.appendLine("AND idVisita = ?");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pIdVisita)});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return retornaVenda(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public long addItemVenda(Venda pVenda, Produto pProduto, List<CodBarra> pListCodigos, double pValor, ArrayList<ComboVenda> pListCombo, String pPrecoDiferenciadoId) throws Exception {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id, qtde");
        sb.appendLine("FROM [ItemVenda]");
        sb.appendLine("WHERE idVenda = ?");
        sb.appendLine("AND idProduto = ?");
        sb.appendLine("AND valorUN = ?");
        sb.appendLine("AND IFNULL(idPreco, 0) = " + (pPrecoDiferenciadoId == null ? "0" : pPrecoDiferenciadoId));

        long idItemVenda = 0;
        int quantidade = 0;

        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext)
                .rawQuery(
                        sb.toString(),
                        new String[]{String.valueOf(pVenda.getId()),
                                pProduto.getId(),
                                String.valueOf(Util_IO.getValor(pValor))}
                );

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                idItemVenda = cursor.getInt(0);
                quantidade = cursor.getInt(1);
            }
            cursor.close();
        }

        ContentValues values;
        if (idItemVenda == 0) {
            values = new ContentValues();
            values.put("idProduto", pProduto.getId());
            values.put("idVenda", pVenda.getId());
            values.put("dataVenda", Util_IO.dateTimeToString(pVenda.getData(), Config.FormatDateTimeStringBanco));
            values.put("qtde", pProduto.getQtde());
            values.put("valorUN", pValor);
            values.put("isCombo", (pListCombo == null || pListCombo.size() == 0) ? 0 : 1);
            values.put("idPreco", pPrecoDiferenciadoId);
            values.put("cancelado", 0);

            idItemVenda = SimpleDbHelper.INSTANCE.open(mContext)
                    .insert(
                            mTabelaItem,
                            null,
                            values
                    );

        } else {
            values = new ContentValues();
            quantidade += pProduto.getQtde();
            values.put("qtde", quantidade);

            SimpleDbHelper.INSTANCE.open(mContext)
                    .update(
                            mTabelaItem,
                            values,
                            "[id]=?",
                            new String[]{String.valueOf(idItemVenda)}
                    );
        }

        if (pListCombo != null && pListCombo.size() > 0) {
            for (ComboVenda comboVenda : pListCombo) {
                values = new ContentValues();
                values.put("idProduto", comboVenda.getIdProduto());
                values.put("qtd", comboVenda.getQuantidade());
                values.put("idVenda", pVenda.getId());
                values.put("idItemVenda", idItemVenda);
                values.put("cancelado", 0);

                SimpleDbHelper.INSTANCE.open(mContext)
                        .insert(
                                mTabelaCombo,
                                null,
                                values
                        );
            }
        }

        if (pListCodigos != null && pListCodigos.size() > 0) {
            for (CodBarra codBarra : pListCodigos) {
                values = new ContentValues();
                values.put("idVenda", pVenda.getId());
                values.put("dataVenda", Util_IO.dateTimeToString(pVenda.getData(), Config.FormatDateTimeStringBanco));
                values.put("idItemVenda", idItemVenda);
                values.put("idProduto", pProduto.getId());
                values.put("codigoBarra", codBarra.getCodBarraInicial());
                values.put("codigoBarraFinal", codBarra.getCodBarraFinal());
                values.put("quantidade", codBarra.retornaQuantidade(UsoCodBarra.GERAL));
                values.put("idProdutoSelect", codBarra.getIdProduto());
                values.put("cancelado", 0);

                SimpleDbHelper.INSTANCE.open(mContext)
                        .insert(
                                mTabelaCodBarra,
                                null,
                                values
                        );
            }
        }
        return idItemVenda;
    }

    public long addItemVenda(Venda pVenda, Produto pProduto, List<CodBarra> pListCodigos,
                             double pValor, List<ComboVenda> pListCombo,
                             String pPrecoDiferenciadoId, boolean isCombo, boolean unificar) throws Exception {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id, qtde");
        sb.appendLine("FROM [ItemVenda]");
        sb.appendLine("WHERE idVenda = ?");
        sb.appendLine("AND idProduto = ?");
        sb.appendLine("AND valorUN = ?");
        sb.appendLine("AND IFNULL(idPreco, 0) = " + (pPrecoDiferenciadoId == null ? "0" : pPrecoDiferenciadoId));

        long idItemVenda = 0;
        int quantidade = 0;

        if (unificar) {
            Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pVenda.getId())
                    , pProduto.getId(), String.valueOf(Util_IO.getValor(pValor))});
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    idItemVenda = cursor.getInt(0);
                    quantidade = cursor.getInt(1);
                }
                cursor.close();
            }
        }

        ContentValues values;
        if (idItemVenda == 0) {
            values = new ContentValues();
            values.put("idProduto", pProduto.getId());
            values.put("idVenda", pVenda.getId());
            values.put("dataVenda", Util_IO.dateTimeToString(pVenda.getData(), Config.FormatDateTimeStringBanco));
            values.put("qtde", pProduto.getQtde());
            values.put("valorUN", pValor);
            values.put("isCombo", !isCombo ? 0 : 1);
            values.put("idPreco", pPrecoDiferenciadoId);
            idItemVenda = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaItem, null, values);
        } else {
            values = new ContentValues();
            quantidade += pProduto.getQtde();
            values.put("qtde", quantidade);
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItem, values, "[id]=?", new String[]{String.valueOf(idItemVenda)});
        }

        if (pListCombo != null && pListCombo.size() > 0) {
            for (ComboVenda comboVenda : pListCombo) {
                values = new ContentValues();
                values.put("idProduto", comboVenda.getIdProduto());
                values.put("qtd", comboVenda.getQuantidade());
                values.put("idVenda", pVenda.getId());
                values.put("idItemVenda", idItemVenda);
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaCombo, null, values);
            }
        }

        if (pListCodigos != null && pListCodigos.size() > 0) {
            for (CodBarra codBarra : pListCodigos) {
                values = new ContentValues();
                values.put("idVenda", pVenda.getId());
                values.put("dataVenda", Util_IO.dateTimeToString(pVenda.getData(), Config.FormatDateTimeStringBanco));
                values.put("idItemVenda", codBarra.getIdVendaItem() == null ? String.valueOf(idItemVenda) : codBarra.getIdVendaItem());
                values.put("idProduto", pProduto.getId());
                values.put("codigoBarra", codBarra.getCodBarraInicial());
                values.put("codigoBarraFinal", codBarra.getCodBarraFinal());
                values.put("quantidade", codBarra.retornaQuantidade(UsoCodBarra.GERAL));
                values.put("idProdutoSelect", codBarra.getIdProduto());
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaCodBarra, null, values);
            }
        }
        return idItemVenda;
    }

    public long addItemVenda(Venda pVenda,
                             Produto pProduto,
                             List<CodBarra> pListCodigos,
                             double pValor,
                             List<ComboVenda> pListCombo,
                             String pPrecoDiferenciadoId,
                             long idItemVenda,
                             boolean atualizaQuantidade) throws Exception {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id, qtde");
        sb.appendLine("FROM [ItemVenda]");
        sb.appendLine("WHERE idVenda = ?");
        sb.appendLine("AND idProduto = ?");
        sb.appendLine("AND valorUN = ?");
        sb.appendLine("AND IFNULL(idPreco, 0) = " + (pPrecoDiferenciadoId == null ? "0" : pPrecoDiferenciadoId));

        int quantidade = 0;
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pVenda.getId())
                , pProduto.getId(), String.valueOf(Util_IO.getValor(pValor))});
//        if (cursor != null) {
//            if (cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                idItemVenda = cursor.getInt(0);
//                quantidade = cursor.getInt(1);
//            }
//            cursor.close();
//        }

        ContentValues values;
        if (idItemVenda == 0) {
            values = new ContentValues();
            values.put("idProduto", pProduto.getId());
            values.put("idVenda", pVenda.getId());
            values.put("dataVenda", Util_IO.dateTimeToString(pVenda.getData(), Config.FormatDateTimeStringBanco));
            values.put("qtde", pProduto.getQtde());
            values.put("valorUN", pValor);
            values.put("isCombo", (pListCombo == null || pListCombo.size() == 0) ? 0 : 1);
            values.put("qtdeCombo", pProduto.getQtdCombo());
            values.put("idPreco", pPrecoDiferenciadoId);
            idItemVenda = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaItem, null, values);
        }

        if (atualizaQuantidade) {
            ItemVenda quantidadeAnterior = getItemVendaById((int) idItemVenda);
            int atualizaEstoque = pProduto.getQtde() - quantidadeAnterior.getQtde();
            String query = "UPDATE [Produto] SET [estoqueAtual] = [estoqueAtual] - " + atualizaEstoque + " WHERE [id] = '" + pProduto.getId() + "'";
            SimpleDbHelper.INSTANCE.open(mContext).execSQL(query);

            quantidade += pProduto.getQtde();
            values = new ContentValues();
            values.put("qtde", quantidade);
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItem, values, "[id]=?", new String[]{String.valueOf(idItemVenda)});
        }

        if (pListCombo != null && pListCombo.size() > 0) {
            for (ComboVenda comboVenda : pListCombo) {
                values = new ContentValues();
                values.put("idProduto", comboVenda.getIdProduto());
                values.put("qtd", comboVenda.getQuantidade());
                values.put("idVenda", pVenda.getId());
                values.put("idItemVenda", idItemVenda);
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaCombo, null, values);
            }
        }

        if (pListCodigos != null && pListCodigos.size() > 0) {
            for (CodBarra codBarra : pListCodigos) {
                values = new ContentValues();
                values.put("idVenda", pVenda.getId());
                values.put("dataVenda", Util_IO.dateTimeToString(pVenda.getData(), Config.FormatDateTimeStringBanco));
                values.put("idItemVenda", codBarra.getIdVendaItem() == null ? String.valueOf(idItemVenda) : codBarra.getIdVendaItem());
                values.put("idProduto", pProduto.getId());
                values.put("codigoBarra", codBarra.getCodBarraInicial());
                values.put("codigoBarraFinal", codBarra.getCodBarraFinal());
                values.put("quantidade", codBarra.retornaQuantidade(UsoCodBarra.GERAL));
                values.put("idProdutoSelect", codBarra.getIdProduto());
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaCodBarra, null, values);
            }
        }
        return idItemVenda;
    }

    public ArrayList<CodBarra> getCodBarraItens(String pIdVendaItem, int pGrupoProduto) {
        ArrayList<CodBarra> listBarra = new ArrayList<>();
        Cursor cursorCod = null;
        String selectQuery = "SELECT codigoBarra, IFNULL(codigoBarraFinal,'-1'), id FROM [ItemVendaCodigoBarra] WHERE idItemVenda = ? AND cancelado = 0";
        try {
            cursorCod = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, new String[]{pIdVendaItem});
            if (cursorCod != null && cursorCod.getCount() > 0) {
                if (cursorCod.moveToFirst()) {
                    do {
                        CodBarra codBarra = new CodBarra();
                        codBarra.setIdPistolagem(cursorCod.getInt(2));
                        codBarra.setCodBarraInicial(cursorCod.getString(0));
                        if (cursorCod.getString(1).equals("-1") || Util_IO.isNullOrEmpty(cursorCod.getString(1)))
                            codBarra.setIndividual(true);
                        else {
                            codBarra.setIndividual(false);
                            codBarra.setCodBarraFinal(cursorCod.getString(1));
                        }
                        codBarra.setGrupoProduto(pGrupoProduto);
                        listBarra.add(codBarra);
                    } while (cursorCod.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursorCod != null)
                cursorCod.close();
        }
        return listBarra;
    }

    private Util_IO.StringBuilder queryItens() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",t0.idProduto");
        sb.appendLine(",IFNULL(t1.nome,'')");
        sb.appendLine(",t0.idVenda");
        sb.appendLine(",t0.dataVenda");
        sb.appendLine(",t0.qtde");
        sb.appendLine(",t0.valorUN");
        sb.appendLine(",IFNULL(t0.sync,0)");
        sb.appendLine(",IFNULL(t1.grupo,0)");
        sb.appendLine(",IFNULL(t0.isCombo,0)");
        sb.appendLine(",IFNULL(t0.idPreco,0)");
        sb.appendLine("FROM [ItemVenda] t0");
        sb.appendLine("LEFT OUTER JOIN [Produto] t1 ON (t1.id = t0.idProduto)");
        sb.appendLine("WHERE cancelado = 0");
        return sb;
    }

    public ArrayList<ItemVendaCombo> getItensComboVendabyIdVenda(int pIdVenda) {
        Util_IO.StringBuilder sb = queryItens();
        sb.appendLine("AND t0.idVenda = ?");
        ArrayList<ItemVendaCombo> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            ItemVendaCombo itemVendaCombo;
            ArrayList<CodBarra> listBarra;
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pIdVenda)});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        itemVendaCombo = new ItemVendaCombo();
                        itemVendaCombo.setId(cursor.getInt(0));
                        itemVendaCombo.setIdProduto(cursor.getString(1));
                        itemVendaCombo.setNomeProduto(cursor.getString(2));
                        itemVendaCombo.setIdVenda(cursor.getInt(3));
                        itemVendaCombo.setDataVenda(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
                        itemVendaCombo.setQtde(cursor.getInt(5));
                        itemVendaCombo.setValorUN(cursor.getDouble(6));
                        listBarra = getCodBarraItens(String.valueOf(cursor.getInt(0)), cursor.getInt(8));
                        itemVendaCombo.setCodigosList(listBarra);
                        itemVendaCombo.setCombo(cursor.getInt(9) > 0);
                        itemVendaCombo.setIdPreco(cursor.getInt(10));
                        list.add(itemVendaCombo);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return list;
    }

    public ArrayList<ItemVenda> getItensVendabyIdVenda(int pIdVenda) {
        Util_IO.StringBuilder sb = queryItens();
        sb.appendLine("AND t0.idVenda = ?");
        Cursor cursor = null;
        ArrayList<ItemVenda> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pIdVenda)});
            ItemVenda item;
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        item = getItembyCursor(cursor);
                        if (item != null)
                            list.add(item);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public ItemVenda getItemVendaById(int pIdVendaItem) {
        Util_IO.StringBuilder sb = queryItens();
        sb.appendLine("AND t0.id = ?");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pIdVendaItem)});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return getItembyCursor(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public ArrayList<ItemVenda> getItensVendabyIdVendaTrade(int pIdVenda) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",t0.idProduto");
        sb.appendLine(",t1.descProduto");
        sb.appendLine(",t0.idVenda");
        sb.appendLine(",t0.dataVenda");
        sb.appendLine(",t0.qtde");
        sb.appendLine(",t0.valorUN");
        sb.appendLine(",IFNULL(t0.sync,0)");
        sb.appendLine(",IFNULL(t2.grupo,0)");
        sb.appendLine("FROM [ItemVenda] t0");
        sb.appendLine("JOIN [ProjetoTradeItens] t1 ON (t1.idProduto = t0.idProduto)");
        sb.appendLine("LEFT OUTER JOIN [Produto] t2 ON (t2.id = t0.idProduto)");
        sb.appendLine("WHERE t0.cancelado = 0");
        sb.appendLine("AND t0.idVenda = ?");
        Cursor cursor = null;
        ArrayList<ItemVenda> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pIdVenda)});
            ItemVenda itemVenda;
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        itemVenda = getItembyCursor(cursor);
                        if (itemVenda != null)
                            list.add(itemVenda);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    private ItemVenda getItembyCursor(Cursor cursor) {
        try {
            if (cursor != null) {
                ItemVenda item = new ItemVenda();
                item.setId(cursor.getInt(0));
                item.setIdProduto(cursor.getString(1));
                item.setNomeProduto(cursor.getString(2));
                item.setIdVenda(cursor.getInt(3));
                item.setDataVenda(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
                item.setQtde(cursor.getInt(5));
                item.setValorUN(cursor.getDouble(6));
                ArrayList<CodBarra> listBarra = getCodBarraItens(String.valueOf(cursor.getInt(0)), cursor.getInt(8));
                item.setCodigosList(listBarra);
                item.setCombo(Util_IO.numberToBoolean(cursor.getInt(9)));
                return item;
            } else
                return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public void deleteItemByIdItem(int pIdItem) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCodBarra, "[idItemVenda]=?", new String[]{String.valueOf(pIdItem)});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaItem, "[id]=?", new String[]{String.valueOf(pIdItem)});
    }

    public void deleteItemCodBarraById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCodBarra, "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void deleteVendaByIdVisita(int pIdVisita) {
        Venda venda = getVendabyIdVisita(pIdVisita);
        if (venda != null) {
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCombo, "[idVenda]=?", new String[]{String.valueOf(venda.getId())});
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCodBarra, "[idVenda]=?", new String[]{String.valueOf(venda.getId())});
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaItem, "[idVenda]=?", new String[]{String.valueOf(venda.getId())});
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaVenda, "[id]=?", new String[]{String.valueOf(venda.getId())});
        }
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCombo, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCodBarra, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaItem, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaVenda, null, null);
    }

    public ArrayList<Venda> getVendasPendentes() {
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT t0.id");
        sbSql.appendLine(",IFNULL(t0.idServer,0)");
        sbSql.appendLine(",t0.idVisita");
        sbSql.appendLine(",t0.idFormaPagamento");
        sbSql.appendLine(",t0.idClienteSGV");
        sbSql.appendLine(",t0.data");
        sbSql.appendLine(",(SELECT id FROM Colaborador) idVendedor");
        sbSql.appendLine(",t0.idCliente");
        sbSql.appendLine(",IFNULL(t0.IdConsignadoRefer,0) as IdConsignadoRefer");
        sbSql.appendLine(",t0.ChaveCobranca");
        sbSql.appendLine(",IFNULL(t0.Pago,0) as Pago");
        sbSql.appendLine(",t0.QrCodeLink");
        sbSql.appendLine(",t0.DataExpiracaoPix");
        sbSql.appendLine(",t0.ComprovantePagto");
        sbSql.appendLine("FROM [Venda] t0");
        sbSql.appendLine("JOIN [Visita] t1 ON (t1.id = t0.idVisita)");
        sbSql.appendLine("WHERE t1.dataFim IS NOT NULL AND t0.sync = 0");
        sbSql.appendLine("AND t0.status = " + EnumStatusVenda.CONCLUIDO.getValue());

        Cursor cursor = null;
        ArrayList<Venda> list = new ArrayList<>();
        Venda venda;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        venda = new Venda();
                        venda.setId(cursor.getInt(0));
                        venda.setIdServer(cursor.getInt(1));
                        venda.setIdVisita(cursor.getInt(2));
                        venda.setIdFormaPagamento(cursor.getInt(3));
                        venda.setIdClienteSGV(cursor.getString(4));
                        venda.setData(Util_IO.stringToDate(cursor.getString(5), Config.FormatDateTimeStringBanco));
                        venda.setIdVendedor(cursor.getString(6));
                        venda.setIdCliente(cursor.getString(7));
                        venda.setIdConsignadoRefer(cursor.getInt(8));
                        venda.setChaveCobranca(cursor.getString(9));
                        venda.setPago(cursor.getInt(10));
                        venda.setQrCodeLink(cursor.getString(11));
                        venda.setDataExpiracaoPix(Util_IO.stringToDate(cursor.getString(12), Config.FormatDateTimeStringBanco));
                        venda.setComprovantePagto(cursor.getString(13));
                        list.add(venda);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public void updateSyncVenda(int pIdApp) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVenda, values, "[id]=?", new String[]{String.valueOf(pIdApp)});
    }

    public void updateChaveCobranca(int pIdApp, String pChave, int pPago, String pQrCodeLink, Date pDataExpiracao, String pComprovantePagto) {
        ContentValues values = new ContentValues();
        values.put("ChaveCobranca", pChave);
        values.put("Pago", pPago);
        values.put("QrCodeLink", pQrCodeLink);
        values.put("DataExpiracaoPix", Util_IO.dateTimeToString(pDataExpiracao, Config.FormatDateTimeStringBanco));
        values.put("ComprovantePagto", pComprovantePagto);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVenda, values, "[id]=?", new String[]{String.valueOf(pIdApp)});
    }

    public void updateValorTotalVenda(double pValor, int pIdApp) {
        ContentValues values = new ContentValues();
        values.put("valorTotal", pValor);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVenda, values, "[id]=?", new String[]{String.valueOf(pIdApp)});
    }

    public void updateFormaPagamentoVenda(int pIdApp, int pIdFormaPagamento, Date pDatavencimento) {
        ContentValues values = new ContentValues();
        values.put("idFormaPagamento", pIdFormaPagamento);
        if (pDatavencimento != null)
            values.put("dataVencimento", Util_IO.dateTimeToString(pDatavencimento, Config.FormatDateTimeStringBanco));
        else
            values.put("dataVencimento", "");
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVenda, values, "[id]=?", new String[]{String.valueOf(pIdApp)});
    }

    public ArrayList<ItemVenda> getItemVendaPendentes() {
        ArrayList<ItemVenda> list = new ArrayList<>();
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",t0.[idServer]");
        sb.appendLine(",t0.idProduto");
        sb.appendLine(",t0.idVenda");
        sb.appendLine(",t0.dataVenda");
        sb.appendLine(",t0.[qtde]");
        sb.appendLine(",t0.[valorUN]");
        sb.appendLine(",(SELECT id FROM Colaborador) idVendedor");
        sb.appendLine("FROM [ItemVenda] t0");
        sb.appendLine("JOIN [Venda] t1 ON (t1.id = t0.idVenda)");
        sb.appendLine("JOIN [Visita] t2 ON (t2.id = t1.idVisita)");
        sb.appendLine("WHERE t2.dataFim IS NOT NULL AND t0.sync = 0");
        sb.appendLine("AND t1.status = " + EnumStatusVenda.CONCLUIDO.getValue());

        Cursor cursor = null;
        ItemVenda itemVenda;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        itemVenda = new ItemVenda();
                        itemVenda.setId(cursor.getInt(0));
                        itemVenda.setIdServer(cursor.getInt(1));
                        itemVenda.setIdProduto(cursor.getString(2));
                        itemVenda.setIdVenda(cursor.getInt(3));
                        itemVenda.setDataVenda(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
                        itemVenda.setQtde(cursor.getInt(5));
                        itemVenda.setValorUN(cursor.getDouble(6));
                        itemVenda.setIdVendedor(cursor.getString(7));
                        list.add(itemVenda);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public void updateSyncItemVenda(int pIdApp) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItem, values, "[id]=?", new String[]{String.valueOf(pIdApp)});
    }

    public ArrayList<ItemVendaCodigoBarra> getItemVendaCodigoBarraPendentes() {
        ArrayList<ItemVendaCodigoBarra> list = new ArrayList<>();
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",t0.[idServer]");
        sb.appendLine(",t0.idProduto");
        sb.appendLine(",t0.idVenda");
        sb.appendLine(",t0.dataVenda");
        sb.appendLine(",t0.idItemVenda");
        sb.appendLine(",t0.codigoBarra");
        sb.appendLine(",(SELECT id FROM Colaborador) idVendedor");
        sb.appendLine(",quantidade");
        sb.appendLine(",IFNULL(codigoBarraFinal,'')");
        sb.appendLine("FROM [ItemVendaCodigoBarra] t0");
        sb.appendLine("JOIN [Venda] t1 ON (t1.id = t0.idVenda)");
        sb.appendLine("JOIN [Visita] t2 ON (t2.id = t1.idVisita)");
        sb.appendLine("WHERE t2.dataFim IS NOT NULL AND t0.sync = 0");
        sb.appendLine("AND t1.status = " + EnumStatusVenda.CONCLUIDO.getValue());

        Cursor cursor = null;
        ItemVendaCodigoBarra itemVendaCodigoBarra;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        itemVendaCodigoBarra = new ItemVendaCodigoBarra();
                        itemVendaCodigoBarra.setId(cursor.getInt(0));
                        itemVendaCodigoBarra.setIdServer(cursor.getInt(1));
                        itemVendaCodigoBarra.setIdProduto(cursor.getString(2));
                        itemVendaCodigoBarra.setIdVenda(cursor.getInt(3));
                        itemVendaCodigoBarra.setDataVenda(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
                        itemVendaCodigoBarra.setIdItemVenda(cursor.getInt(5));
                        itemVendaCodigoBarra.setCodigoBarra(cursor.getString(6));
                        itemVendaCodigoBarra.setIdVendedor(cursor.getString(7));
                        itemVendaCodigoBarra.setQuantidade(cursor.getInt(8));
                        itemVendaCodigoBarra.setCodigoBarraFinal(cursor.getString(9));
                        list.add(itemVendaCodigoBarra);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public void updateSyncItemVendaCodigoBarra(int pIdApp) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCodBarra, values, "[id]=?", new String[]{String.valueOf(pIdApp)});
    }

    public double retornaValorPendente() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT SUM(valorUN * qtde) FROM [ItemVenda] WHERE 1=1");
        sb.appendLine("AND boletoSync = 0");
        sb.appendLine("AND idVenda IN (SELECT id FROM [Venda] WHERE [idFormaPagamento] = 1 ");
        sb.appendLine("AND status = " + EnumStatusVenda.CONCLUIDO.getValue() + ")");

        Cursor cursor = null;
        double retorno = 0.0;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        retorno += cursor.getDouble(0);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            retorno = 0.0;
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return retorno;
    }

    @SuppressLint("Range")
    public String retornaUltimaVenda() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT strftime('%d/%m/%Y', Data) As DataVenda FROM VENDA WHERE status = "
                + EnumStatusVenda.CONCLUIDO.getValue() + " ORDER BY DATA DESC LIMIT 1");

        Cursor cursor = null;
        String retornaData = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                retornaData = cursor.getString(cursor.getColumnIndex("DataVenda"));
            }
        } catch (Exception ex) {
            retornaData = null;
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return retornaData;
    }

    public void updateCobrancaItemVenda(String pIdApp) {
        ContentValues values = new ContentValues();
        values.put("boletoSync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItem, values, "[idVenda]=?", new String[]{pIdApp});
    }

    public ArrayList<VendaMobile> getVendasMobilePendentes() {

        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",(SELECT id FROM Colaborador) AS idVendedor");
        sb.appendLine(",t0.idCliente");
        sb.appendLine(",t0.idVisita");
        sb.appendLine(",t0.data");
        sb.appendLine(",t0.idFormaPagamento");
        sb.appendLine(",IFNULL(t1.latitude,0) as latitude");
        sb.appendLine(",IFNULL(t1.longitude,0) as longitude");
        sb.appendLine(",IFNULL(t1.precisao,0)as precisao");
        sb.appendLine(",t1.versaoApp");
        sb.appendLine(",IFNULL(t2.id,0)as idItem");
        sb.appendLine(",IFNULL(t2.idProduto,'')as idProduto");
        sb.appendLine(",IFNULL(t2.qtde,0)as qtde");
        sb.appendLine(",IFNULL(t2.valorUN,0)as valorUN");
        sb.appendLine(",IFNULL(t0.dataVencimento,t0.data)as dataCobranca");
        sb.appendLine(",IFNULL(t1.idProjeto,0)as idProjeto");
        sb.appendLine(",IFNULL(t0.valorTotal,0)as valorTotal");
        sb.appendLine(",IFNULL(t2.isCombo,0)as combo");
        sb.appendLine(",IFNULL(t2.IdPreco,'') as IdPrecoDif");
        sb.appendLine(",IFNULL(t0.IdConsignadoRefer,0) as IdConsignadoRefer");
        sb.appendLine(",t0.ChaveCobranca");
        sb.appendLine(",IFNULL(t0.Pago,0) as Pago");
        sb.appendLine(",t0.QrCodeLink");
        sb.appendLine(",t0.ComprovantePagto");
        sb.appendLine("FROM [Venda] t0");
        sb.appendLine("JOIN [Visita] t1 ON (t1.id = t0.idVisita)");
        sb.appendLine("LEFT OUTER JOIN [ItemVenda] t2 ON (t0.id = t2.idVenda)");
        sb.appendLine("WHERE t0.status = " + EnumStatusVenda.CONCLUIDO.getValue());
        sb.appendLine("AND t1.dataFim IS NOT NULL");
        sb.appendLine("AND IFNULL(t0.idFormaPagamento,0) > 0");
        sb.appendLine("AND IFNULL(t1.idMotivo,0) = 0");
        sb.appendLine("AND ((t0.sync = 0) OR (t2.sync = 0))");

        try {
            return Util_DB.RetornaLista(mContext, VendaMobile.class, sb.toString(), null);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public double retornaValorTotalVenda(int pIdApp) {
        Cursor cursor = null;
        Double retorno = 0.0;
        try {
            String querySql = "SELECT SUM(valorUN * qtde) FROM [ItemVenda] WHERE idVenda = ?";
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(querySql, new String[]{String.valueOf(pIdApp)});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        retorno += cursor.getDouble(0);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            retorno = 0.0;
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return retorno;
    }

    public double retornaValorVendidoByCliente(String pCodigoCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT SUM(t1.valorUN * t1.qtde)");
        sb.appendLine("FROM [Venda] t0");
        sb.appendLine("JOIN [ItemVenda] t1 ON (t0.id = t1.idVenda)");
        sb.appendLine("JOIN [Visita] t2 ON (t0.idVisita = t2.id)");
        sb.appendLine("WHERE t0.status = " + EnumStatusVenda.CONCLUIDO.getValue());
        sb.appendLine("AND t0.idFormaPagamento = 2");
        sb.appendLine("AND t2.dataFim IS NOT NULL");
        sb.appendLine("AND t0.idCliente = ?");

        Cursor cursor = null;
        Double retorno = 0.0;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pCodigoCliente});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        retorno += cursor.getDouble(0);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            retorno = 0.0;
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return retorno;
    }

    public ArrayList<MobileCodBarra> getVendasMobileCodBarraPendentes() {
        ArrayList<MobileCodBarra> list = new ArrayList<>();
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT t0.id");
        sbSql.appendLine(",t0.idItemVenda");
        sbSql.appendLine(",(SELECT id FROM Colaborador) idVendedor");
        sbSql.appendLine(",t0.idVenda");
        sbSql.appendLine(",t1.data");
        sbSql.appendLine(",t1.idCliente");
        sbSql.appendLine(",t2.id");
        sbSql.appendLine(",IFNULL(t0.codigoBarra,'')");
        sbSql.appendLine(",IFNULL(t0.codigoBarraFinal,'')");
        sbSql.appendLine(",t0.quantidade");
        sbSql.appendLine(",IFNULL(t0.idProdutoSelect,'')");
        sbSql.appendLine("FROM [ItemVendaCodigoBarra] t0");
        sbSql.appendLine("JOIN [Venda] t1 ON (t1.id = t0.idVenda)");
        sbSql.appendLine("JOIN [Visita] t2 ON (t2.id = t1.idVisita)");
        sbSql.appendLine("WHERE t1.status = " + EnumStatusVenda.CONCLUIDO.getValue());
        sbSql.appendLine("AND t2.dataFim IS NOT NULL AND t0.sync = 0");
        sbSql.appendLine("AND IFNULL(t1.idFormaPagamento,0) > 0");

        Cursor cursor = null;
        MobileCodBarra codigo;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        codigo = new MobileCodBarra();
                        codigo.setId(cursor.getInt(0));
                        codigo.setIdVendaItem(cursor.getInt(1));
                        codigo.setIdVendedor(cursor.getString(2));
                        codigo.setIdVenda(cursor.getInt(3));
                        codigo.setData(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
                        codigo.setIdCliente(cursor.getString(5));
                        codigo.setIdVisita(cursor.getInt(6));
                        codigo.setCodigoBarra(cursor.getString(7));
                        codigo.setCodigoBarraFinal(cursor.getString(8));
                        codigo.setQuantidade(cursor.getInt(9));
                        codigo.setIdProduto(cursor.getString(10));
                        list.add(codigo);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public ArrayList<VendaConsulta> getConsultaVendas(String pDataInicial, String pDataFinal, String pIdCliente) {
        ArrayList<VendaConsulta> list = new ArrayList<>();
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT date(t0.[data])");
        sbSql.appendLine(",SUM(t2.valorUN * t2.qtde)");
        sbSql.appendLine("FROM [Venda] t0");
        sbSql.appendLine("JOIN [Visita] t1 ON (t1.id = t0.idVisita)");
        sbSql.appendLine("JOIN [ItemVenda] t2 ON (t0.id = t2.idVenda)");
        sbSql.appendLine("WHERE t1.dataFim IS NOT NULL");
        sbSql.appendLine("AND t0.status = " + EnumStatusVenda.CONCLUIDO.getValue());

        if (Util_IO.isNullOrEmpty(pIdCliente)) {
            if (!Util_IO.isNullOrEmpty(pDataInicial)) {
                sbSql.appendLine("AND strftime('%d/%m/%Y',t0.[data]) >= strftime('%d/%m/%Y', '" + pDataInicial.trim() + "')");
                sbSql.appendLine("AND strftime('%d/%m/%Y',t0.[data]) <= strftime('%d/%m/%Y', '" + pDataFinal.trim() + "')");
            } else {
                sbSql.appendLine("AND date(t0.[data]) >= date('now', '-7 day')");
            }
        }
        else {
            sbSql.appendLine("AND t0.[idCliente] = " + pIdCliente);
        }
        sbSql.appendLine("AND IFNULL(t0.idFormaPagamento,0) > 0");
        sbSql.appendLine("GROUP BY date(t0.[data])");
        sbSql.appendLine("ORDER BY date(t0.[data]) DESC");

        Cursor cursor = null, cursor2 = null;
        VendaConsulta vendaConsulta;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        vendaConsulta = new VendaConsulta();
                        vendaConsulta.setDataVenda(Util_IO.stringToDate(cursor.getString(0), Config.FormatDateStringBanco));
                        vendaConsulta.setValor(cursor.getDouble(1));

                        sbSql = new Util_IO.StringBuilder();
                        sbSql.appendLine("SELECT t0.id");
                        sbSql.appendLine(",IFNULL(t1.nomeFantasia,'')");
                        sbSql.appendLine(",t0.data");
                        sbSql.appendLine(",SUM(t2.valorUN * t2.qtde)");
                        sbSql.appendLine("FROM [Venda] t0");
                        sbSql.appendLine("LEFT OUTER JOIN [Cliente] t1 ON t1.id = t0.idCliente");
                        sbSql.appendLine("LEFT OUTER JOIN [ItemVenda] t2 ON t0.id = t2.idVenda");
                        sbSql.appendLine("WHERE date(t0.[data]) = date('" + cursor.getString(0) + "')");
                        sbSql.appendLine("AND t0.status = " + EnumStatusVenda.CONCLUIDO.getValue());
                        sbSql.appendLine("AND IFNULL(t0.idFormaPagamento,0) > 0");
                        sbSql.appendLine("GROUP BY t0.id,IFNULL(t1.nomeFantasia,''),t0.data");
                        sbSql.appendLine("ORDER BY t0.[data]");

                        cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
                        ArrayList<VendaConsultaItem> item = new ArrayList<>();
                        if (cursor2 != null && cursor2.getCount() > 0) {
                            if (cursor2.moveToFirst()) {
                                do {
                                    VendaConsultaItem vendaConsultaItem = new VendaConsultaItem();
                                    vendaConsultaItem.setId(cursor2.getInt(0));
                                    vendaConsultaItem.setCliente(cursor2.getString(1));
                                    vendaConsultaItem.setHoravenda(Util_IO.timeToString(Util_IO.stringToDate(cursor2.getString(2), Config.FormatDateTimeStringBanco)));
                                    vendaConsultaItem.setValor(cursor2.getDouble(3));
                                    item.add(vendaConsultaItem);
                                } while (cursor2.moveToNext());
                            }
                        }

                        vendaConsulta.setList(item);
                        list.add(vendaConsulta);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
        }
        return list;
    }

    public void delete60dias() {
        try {
            Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery("SELECT id FROM [Venda] WHERE [data] < datetime('now', '-60 day') AND [sync] = 1", null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCodBarra, "[idVenda]=? AND [sync]=1", new String[]{String.valueOf(cursor.getInt(0))});
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaItem, "[idVenda]=? AND [sync]=1", new String[]{String.valueOf(cursor.getInt(0))});
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCombo, "[idVenda]=?", new String[]{String.valueOf(cursor.getInt(0))});
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaVenda, "[id]=?", new String[]{String.valueOf(cursor.getInt(0))});
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<CodBarra> listCodigosbyVenda(int pIdVenda) {
        ArrayList<CodBarra> list = new ArrayList<>();
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT t0.id");
        sbSql.appendLine(",IFNULL(t0.codigoBarra,'')");
        sbSql.appendLine(",IFNULL(t0.codigoBarraFinal,'')");
        sbSql.appendLine(",t0.quantidade");
        sbSql.appendLine(",IFNULL(t1.grupo,0)");
        sbSql.appendLine(",t1.id");
        sbSql.appendLine("FROM [ItemVendaCodigoBarra] t0");
        sbSql.appendLine("LEFT OUTER JOIN [Produto] t1 ON (t1.id = t0.idProduto)");
        sbSql.appendLine("WHERE t0.cancelado = 0");
        sbSql.appendLine("AND t0.idVenda = ?");

        Cursor cursor = null;
        CodBarra codBarra;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), new String[]{String.valueOf(pIdVenda)});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        codBarra = new CodBarra();
                        codBarra.setIndividual(cursor.getString(2).length() == 0);
                        codBarra.setGrupoProduto(cursor.getInt(4));
                        codBarra.setIdProduto(cursor.getString(5));
                        if (!codBarra.getIndividual()) {
                            if (cursor.getInt(4) == 100 || cursor.getInt(4) == 229 || cursor.getInt(4) == 235) {
                                BigInteger cinicial = CodigoBarra.retornaICCID(cursor.getString(1), cursor.getInt(4));
                                BigInteger cfinal = CodigoBarra.retornaICCID(cursor.getString(2), cursor.getInt(4));
                                if (cfinal.compareTo(cinicial) > 0) {
                                    codBarra.setCodBarraInicial(cursor.getString(1));
                                    codBarra.setCodBarraFinal(cursor.getString(2));
                                } else {
                                    codBarra.setCodBarraInicial(cursor.getString(2));
                                    codBarra.setCodBarraFinal(cursor.getString(1));
                                }
                            } else {
                                BigInteger cinicial = new BigInteger(cursor.getString(1));
                                BigInteger cfinal = new BigInteger(cursor.getString(2));
                                Long retorno = cfinal.subtract(cinicial).longValue();
                                if (retorno < 0) {
                                    codBarra.setCodBarraInicial(cursor.getString(1));
                                    codBarra.setCodBarraFinal(cursor.getString(2));
                                } else {
                                    codBarra.setCodBarraInicial(cursor.getString(2));
                                    codBarra.setCodBarraFinal(cursor.getString(1));
                                }
                            }
                        } else {
                            codBarra.setCodBarraInicial(cursor.getString(1));
                            codBarra.setCodBarraFinal(cursor.getString(2));
                        }
                        list.add(codBarra);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public void removeEstoqueComboByIdVendaItem(int pIdVendaItem) {
        try {
            DBEstoque dbEstoque = new DBEstoque(mContext);
            String query = "SELECT id, qtd, idProduto FROM [ItemVendaCombo] WHERE cancelado = 0 AND idItemVenda = ?";
            Cursor cursor3 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(query, new String[]{String.valueOf(pIdVendaItem)});
            if (cursor3 != null && cursor3.getCount() > 0) {
                if (cursor3.moveToFirst()) {
                    do {
                        dbEstoque.atualizaEstoque(cursor3.getString(2), false, cursor3.getInt(1));
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCombo, "[id]=?", new String[]{String.valueOf(cursor3.getInt(0))});
                    } while (cursor3.moveToNext());
                }
            }
            if (cursor3 != null)
                cursor3.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteVendaNaoFinalizada() {
        try {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("SELECT t0.id");
            sb.appendLine("FROM [Venda] t0");
            sb.appendLine("JOIN [Visita] t1 ON (t1.id = t0.idVisita)");
            sb.appendLine("WHERE t0.status <> " + EnumStatusVenda.CANCELADO.getValue());
            sb.appendLine("AND IFNULL(t0.[idFormaPagamento],0) = 0");
            sb.appendLine("AND t1.[dataFim] IS NOT NULL");

            DBEstoque dbEstoque = new DBEstoque(mContext);
            Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCodBarra, "[idVenda]=?", new String[]{String.valueOf(cursor.getInt(0))});

                        sb = new Util_IO.StringBuilder();
                        sb.append("SELECT id, qtde, idProduto, IFNULL(isCombo,0) FROM [ItemVenda] WHERE idVenda = ?");
                        Cursor cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(cursor.getInt(0))});
                        if (cursor2 != null && cursor2.getCount() > 0) {
                            if (cursor2.moveToFirst()) {
                                do {
                                    if (cursor2.getInt(3) > 0)
                                        removeEstoqueComboByIdVendaItem(cursor2.getInt(0));
                                    else
                                        dbEstoque.atualizaEstoque(cursor2.getString(2), false, cursor2.getInt(1));
                                    SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaItem, "[id]=?", new String[]{String.valueOf(cursor2.getInt(0))});
                                } while (cursor2.moveToNext());
                            }
                        }
                        if (cursor2 != null)
                            cursor2.close();
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaVenda, "[id]=?", new String[]{String.valueOf(cursor.getInt(0))});
                    }
                    while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean iccidVendido(CodBarra pCodBarra) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine("FROM [ItemVendaCodigoBarra]");
        sb.appendLine("WHERE cancelado = 0");
        if (pCodBarra != null) {
            if (pCodBarra.getIndividual()) {
                sb.appendLine("AND ([codigoBarra] = '" + pCodBarra.getCodBarraInicial() + "'");
                sb.appendLine("OR [codigoBarraFinal] = '" + pCodBarra.getCodBarraInicial() + "')");
            } else {
                sb.appendLine("AND (([codigoBarra] = '" + pCodBarra.getCodBarraInicial() + "'");
                sb.appendLine("OR [codigoBarraFinal] = '" + pCodBarra.getCodBarraInicial() + "')");
                sb.appendLine("OR ([codigoBarra] = '" + pCodBarra.getCodBarraFinal() + "'");
                sb.appendLine("OR [codigoBarraFinal] = '" + pCodBarra.getCodBarraFinal() + "'))");
            }
        } else
            return false;
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

    public ArrayList<OperadoraAtend> getAtendido30Dias(String pIdCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT x0.Descricao, IFNULL(x1.idCliente,''), IFNULL(x1.data,'')");
        sb.appendLine("FROM (");
        sb.appendLine("SELECT 1 AS CodOperadora,'Oi' AS Descricao");
        sb.appendLine("UNION SELECT 2,'Claro'");
        sb.appendLine("UNION SELECT 3,'Vivo'");
        sb.appendLine("UNION SELECT 4,'Tim'");
        sb.appendLine(") x0");
        sb.appendLine("LEFT OUTER JOIN (SELECT t2.operadora, t0.idCliente, MAX(DATE(t0.data)) AS data");
        sb.appendLine("FROM Venda t0");
        sb.appendLine("JOIN ItemVenda t1 ON (t0.id = t1.idVenda)");
        sb.appendLine("JOIN Produto t2 ON (t1.idProduto = t2.id)");
        sb.appendLine("JOIN Visita t3 ON (t3.id = t0.idVisita)");
        sb.appendLine("WHERE t0.idCliente = ?");
        sb.appendLine("AND t0.status = " + EnumStatusVenda.CONCLUIDO.getValue());
        sb.appendLine("AND t0.data > datetime('now', '-31 day')");
        sb.appendLine("AND IFNULL(t0.idformapagamento,0) > 0");
        sb.appendLine("AND t3.dataFim IS NOT NULL");
        sb.appendLine("GROUP BY t2.operadora, t0.idCliente) x1 ON (x0.CodOperadora = x1.operadora)");
        ArrayList<OperadoraAtend> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pIdCliente});
            OperadoraAtend operadoraAtend;
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        operadoraAtend = new OperadoraAtend();
                        operadoraAtend.setDescricao(cursor.getString(0));
                        operadoraAtend.setAtendido(!Util_IO.isNullOrEmpty(cursor.getString(1)));
                        operadoraAtend.setData(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateStringBanco));
                        list.add(operadoraAtend);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public ArrayList<Produto> getProdutosVendidosHoje() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t2.id, t2.nome, IFNULL(SUM(t0.qtde),0), t2.operadora");
        sb.appendLine("FROM [ItemVenda] t0");
        sb.appendLine("JOIN [Venda] t1 ON (t0.idVenda = t1.id)");
        sb.appendLine("JOIN [Produto] t2 ON (t0.idProduto = t2.id)");
        sb.appendLine("JOIN [Visita] t3 ON (t1.idVisita = t3.id)");
        sb.appendLine("WHERE t1.status = " + EnumStatusVenda.CONCLUIDO.getValue());
        sb.appendLine("AND IFNULL(t0.qtde,0) > 0");
        sb.appendLine("AND date(t1.data) = date('now', 'localtime')");
        sb.appendLine("AND t3.dataFim IS NOT NULL");
        sb.appendLine("AND IFNULL(t1.idFormaPagamento,0) > 0");
        sb.appendLine("GROUP BY t2.id, t2.nome, t2.operadora");
        ArrayList<Produto> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            Produto produto;
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        produto = new Produto();
                        produto.setId(cursor.getString(0));
                        produto.setNome(cursor.getString(1));
                        produto.setQtde(cursor.getInt(2));
                        produto.setOperadora(cursor.getInt(3));
                        list.add(produto);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public int retornaQtdPrecoDiferenciado(String pIdPreco) {
        Cursor cursor = null;
        int retorno = 0;
        try {
            String querySql = "SELECT SUM(qtde) FROM [ItemVenda] WHERE idPreco = ? AND cancelado = 0";
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(querySql, new String[]{pIdPreco});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        retorno += cursor.getInt(0);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            retorno = 0;
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return retorno;
    }

    public void updateQtdItemVenda(int pCodigoItemVenda, int pQuantidade) {
        ContentValues values = new ContentValues();
        values.put("qtde", pQuantidade);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItem, values, "[id]=?", new String[]{String.valueOf(pCodigoItemVenda)});
    }

    public void removerCodigoBarra(CodBarra codBarra) {
        SimpleDbHelper.INSTANCE
                .open(mContext)
                .delete(
                        mTabelaCodBarra,
                        "[id]=?",
                        new String[]{String.valueOf(codBarra.getIdPistolagem())}
                );
    }

    public VendaMobile getvendasMobileById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",(SELECT id FROM Colaborador) AS idVendedor");
        sb.appendLine(",t0.idCliente");
        sb.appendLine(",t0.idVisita");
        sb.appendLine(",t0.data");
        sb.appendLine(",t0.idFormaPagamento");
        sb.appendLine(",IFNULL(t1.latitude,0) as latitude");
        sb.appendLine(",IFNULL(t1.longitude,0) as longitude");
        sb.appendLine(",IFNULL(t1.precisao,0)as precisao");
        sb.appendLine(",t1.versaoApp");
        sb.appendLine(",IFNULL(t2.id,0)as idItem");
        sb.appendLine(",IFNULL(t2.idProduto,'')as idProduto");
        sb.appendLine(",IFNULL(t2.qtde,0)as qtde");
        sb.appendLine(",IFNULL(t2.valorUN,0)as valorUN");
        sb.appendLine(",IFNULL(t0.dataVencimento,t0.data)as dataCobranca");
        sb.appendLine(",IFNULL(t1.idProjeto,0)as idProjeto");
        sb.appendLine(",IFNULL(t0.valorTotal,0)as valorTotal");
        sb.appendLine(",IFNULL(t2.isCombo,0)as combo");
        sb.appendLine(",IFNULL(t2.IdPreco,'') as IdPrecoDif");
        sb.appendLine(",IFNULL(t0.IdConsignadoRefer,0) as IdConsignadoRefer");
        sb.appendLine(",t0.ChaveCobranca");
        sb.appendLine(",IFNULL(t0.Pago,0) as Pago");
        sb.appendLine(",t0.QrCodeLink");
        sb.appendLine("FROM [Venda] t0");
        sb.appendLine("JOIN [Visita] t1 ON (t1.id = t0.idVisita)");
        sb.appendLine("LEFT OUTER JOIN [ItemVenda] t2 ON (t0.id = t2.idVenda)");
        sb.appendLine("WHERE t0.status = " + EnumStatusVenda.CONCLUIDO.getValue());
        sb.appendLine("AND t1.dataFim IS NOT NULL");
        sb.appendLine("AND IFNULL(t0.idFormaPagamento,0) > 0");
        sb.appendLine("AND IFNULL(t1.idMotivo,0) = 0");
        sb.appendLine("AND t0.id = ?");

        try {
            ArrayList<VendaMobile> list = Util_DB.RetornaLista(mContext, VendaMobile.class, sb.toString(), new String[]{String.valueOf(pId)});
            if (!list.isEmpty()) {
                return list.get(0);
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void cancelaVendaByIdVisita(int pIdVisita) {
        Venda venda = getVendabyIdVisita(pIdVisita);
        if (venda != null) {

            ContentValues values = new ContentValues();
            values.put("status", EnumStatusVenda.CANCELADO.getValue());

            ContentValues valuesItem = new ContentValues();
            valuesItem.put("cancelado", 1);

            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCombo, valuesItem, "[idVenda]=?", new String[]{String.valueOf(venda.getId())});
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCodBarra, valuesItem, "[idVenda]=?", new String[]{String.valueOf(venda.getId())});
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItem, valuesItem, "[idVenda]=?", new String[]{String.valueOf(venda.getId())});
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVenda, values, "[id]=?", new String[]{String.valueOf(venda.getId())});
        }
    }

    public void concluiVendaByIdVisita(int pIdVisita) {
        concluirVenda(getVendabyIdVisita(pIdVisita));
    }

    public void concluirVenda(Venda venda) {
        if (venda == null) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put("status", EnumStatusVenda.CONCLUIDO.getValue());

        ContentValues valuesItem = new ContentValues();
        valuesItem.put("cancelado", 0);

        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCombo, valuesItem, "[idVenda]=?", new String[]{String.valueOf(venda.getId())});
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCodBarra, valuesItem, "[idVenda]=?", new String[]{String.valueOf(venda.getId())});
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItem, valuesItem, "[idVenda]=?", new String[]{String.valueOf(venda.getId())});
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVenda, values, "[id]=?", new String[]{String.valueOf(venda.getId())});
    }

    public void atualizarQuantidadeCombo(int quantidade, String idVendaItem) {
        ContentValues values = new ContentValues();
        values.put("qtde", quantidade);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItem, values, "[id]=?", new String[]{idVendaItem});
    }

    public Venda getVendabyNSU(String pNSU) {
        Util_IO.StringBuilder sb = retornaQueryVenda();
        sb.appendLine("AND status = " + EnumStatusVenda.CONCLUIDO.getValue());
        sb.appendLine("AND IFNULL(idFormaPagamento,0) > 0");
        sb.appendLine("AND ChaveCobranca = '" + pNSU.trim() + "'");

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return retornaVenda(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}