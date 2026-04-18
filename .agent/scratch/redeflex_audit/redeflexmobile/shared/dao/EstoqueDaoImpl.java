package com.axys.redeflexmobile.shared.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.AuditagemEstoque;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class EstoqueDaoImpl implements EstoqueDao {

    private final Context context;
    private final String mTabelaAuditagemCliente = "AuditagemCliente";
    private final String mTabelaPistolagemCombo = "PistolagemComboTemp";
    private final String mTabelaCombo = "ComboTemp";
    private final String mTabelaPistolagem = "PistolagemTemp";

    public EstoqueDaoImpl(Context context) {
        this.context = context;
    }

    public boolean isProdutoCombo(String pItemCode) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine("FROM [EstruturaProd]");
        sb.appendLine("WHERE itemPai = ?");
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), new String[]{pItemCode})) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Util_IO.StringBuilder queryEstrutura() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine(",itemPai");
        sb.appendLine(",itemFilho");
        sb.appendLine(",IFNULL(qtd,0)");
        sb.appendLine(",IFNULL(proporcao,0)");
        sb.appendLine("FROM [EstruturaProd]");
        sb.appendLine("WHERE 1=1");
        return sb;
    }

    private EstruturaProd getEstruturaProdCursor(Cursor cursor) {
        if (cursor != null) {
            EstruturaProd estruturaProd = new EstruturaProd();
            estruturaProd.setId(cursor.getInt(0));
            estruturaProd.setItemPai(cursor.getString(1));
            estruturaProd.setItemFilho(cursor.getString(2));
            estruturaProd.setQtd(cursor.getInt(3));
            estruturaProd.setProporcao(cursor.getDouble(4));
            return estruturaProd;
        } else
            return null;
    }

    public ArrayList<EstruturaProd> getEstruturaByItemPai(String pItemCode) {
        Util_IO.StringBuilder sb = queryEstrutura();
        sb.appendLine("AND itemPai = ?");
        Cursor cursor = null;
        ArrayList<EstruturaProd> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), new String[]{pItemCode});
            if (cursor.moveToFirst()) {
                EstruturaProd estruturaProd;
                do {
                    estruturaProd = getEstruturaProdCursor(cursor);
                    if (estruturaProd != null)
                        lista.add(estruturaProd);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lista;
    }

    public Produto getProdutoById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.id = ?");
        return Utilidades.firstOrDefault(getProdutos(sb.toString(), new String[]{pId}));
    }

    private ArrayList<Produto> getProdutos(String pCondicao, String[] pCampos) {
        return getProdutos(null, pCondicao, pCampos);
    }

    public ArrayList<Produto> getProdutos() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.ativo = 'S'");
        return getProdutos(sb.toString(), null);
    }

    public ArrayList<Produto> getProdutosComboENaoPistolados() {
        Util_IO.StringBuilder sb = retornaQueryProduto();
        sb.appendLine("WHERE t0.ativo = 'S'");
        sb.appendLine("AND IFNULL(t0.estoqueAtual,0) > 0");
        sb.appendLine("UNION");
        sb.appendLine(retornaQueryProduto().toString());
        sb.appendLine("JOIN EstruturaProd t1 ON (t1.itemPai = t0.id)");
        sb.appendLine("WHERE t0.ativo = 'S' AND IFNULL(t0.estoqueAtual,0) > 0");

        return getProdutos(sb.toString(), null, null);
    }

    public void atualizaEstoque(String pIdProduto, boolean pRemove, int pQtd) {
        String query = "UPDATE [Produto] SET [estoqueAtual] = [estoqueAtual] " + ((pRemove) ? "- " : "+ ") + pQtd + " WHERE [id] = '" + pIdProduto + "'";
        SimpleDbHelper.INSTANCE.open(context).execSQL(query);
    }

    public boolean verificaEstoque(String pIdProduto, int pQtd, int quantidadeAtual) {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("SELECT IFNULL([estoqueAtual],0)");
        sbSQL.appendLine("FROM [Produto]");
        sbSQL.appendLine("WHERE [id] = ?");

        Cursor cursor = null;
        try {
            int estoqueAtual = 0, estoqueFinal;
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sbSQL.toString(), new String[]{pIdProduto});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                estoqueAtual = cursor.getInt(0);
            }

            estoqueFinal = (estoqueAtual + quantidadeAtual) - pQtd;
            if (estoqueFinal >= 0) {
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public ArrayList<AuditagemCliente> getAuditagensCliente(String pIdCliente) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t0.[id] ");
        sb.append(",t0.[idCliente] ");
        sb.append(",t0.[idProduto] ");
        sb.append(",t0.[qtdeInformada] ");
        sb.append(",t1.[nome] ");
        sb.append(",t0.[confirmado] ");
        sb.append(",t0.[importado] ");
        sb.append("FROM [AuditagemCliente] t0 ");
        sb.append("JOIN [Produto] t1 ON (t0.[idProduto] = t1.[id]) ");
        sb.append("WHERE 1=1 ");
        sb.append("AND date(t0.[data]) = date('now','localtime') ");
        sb.append("AND t0.[idCliente] = ? ");

        Cursor cursor = null, cursor2 = null;
        ArrayList<AuditagemCliente> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), new String[]{pIdCliente});
            AuditagemCliente auditagemCliente;
            if (cursor.moveToFirst()) {
                do {
                    auditagemCliente = new AuditagemCliente();
                    auditagemCliente.setId(cursor.getInt(0));
                    auditagemCliente.setIdCliente(cursor.getString(1));
                    auditagemCliente.setIdProduto(cursor.getString(2));
                    auditagemCliente.setQuantidade(cursor.getInt(3));
                    auditagemCliente.setProduto(cursor.getString(4));
                    auditagemCliente.setConfirmada((cursor.getString(5).equals("S")));
                    boolean importado = cursor.getInt(6) == 1;
                    auditagemCliente.setImportado(importado);

                    String selectQuery = "SELECT codigoBarra, IFNULL(codigoBarraFinal,'-1') FROM [AuditagemClienteCodBarra] WHERE idAuditagem = ?";
                    cursor2 = SimpleDbHelper.INSTANCE.open(context).rawQuery(selectQuery, new String[]{String.valueOf(auditagemCliente.getId())});

                    ArrayList<CodBarra> listBarra = new ArrayList<>();
                    if (cursor2 != null && cursor2.getCount() > 0) {
                        if (cursor2.moveToFirst()) {
                            do {
                                CodBarra codBarra = new CodBarra();
                                codBarra.setCodBarraInicial(cursor2.getString(0));
                                if (cursor2.getString(1).equals("-1") || Util_IO.isNullOrEmpty(cursor2.getString(1)))
                                    codBarra.setIndividual(true);
                                else {
                                    codBarra.setIndividual(false);
                                    codBarra.setCodBarraFinal(cursor2.getString(1));
                                }
                                listBarra.add(codBarra);
                            } while (cursor2.moveToNext());
                        }
                    }
                    auditagemCliente.setListaCodigos(listBarra);
                    lista.add(auditagemCliente);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
        }
        return lista;
    }

    public void atualizarImportado(String id) {
        ContentValues values = new ContentValues();
        values.put("importado", 1);
        SimpleDbHelper.INSTANCE
                .open(context)
                .update(
                        mTabelaAuditagemCliente,
                        values,
                        "[id]=?",
                        new String[]{id}
                );
    }

    private Util_IO.StringBuilder retornaQueryProduto() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",IFNULL(t0.codSgv,'')");
        sb.appendLine(",t0.nome");
        sb.appendLine(",IFNULL(t0.valor,0)");
        sb.appendLine(",IFNULL(t0.estoqueMax,0)");
        sb.appendLine(",IFNULL(t0.mediaDiariaVnd,0)");
        sb.appendLine(",IFNULL(t0.diasEstoque,0)");
        sb.appendLine(",IFNULL(t0.estoqueAtual,0)");
        sb.appendLine(",IFNULL(t0.estoqueSugerido,0)");
        sb.appendLine(",t0.ativo");
        sb.appendLine(",IFNULL(t0.bipagem,'N')");
        sb.appendLine(",IFNULL(t0.precovenda,0)");
        sb.appendLine(",IFNULL(t0.bipagemAuditoria,'N')");
        sb.appendLine(",IFNULL(t0.qtdCodBarra,0)");
        sb.appendLine(",IFNULL(t0.iniciaCodBarra,'')");
        sb.appendLine(",IFNULL(t0.bipagemCliente,'N')");
        sb.appendLine(",IFNULL(t0.qtdCombo,0)");
        sb.appendLine(",IFNULL(t0.permiteValorZero,'N')");
        sb.appendLine(",IFNULL(t0.grupo,0)");
        sb.appendLine(",IFNULL(t0.operadora,0)");
        sb.appendLine(",IFNULL(t0.permiteVendaPrazo,'S')");
        sb.appendLine(",IFNULL(t0.VendaAvista,'N')");
        sb.appendLine("FROM [Produto] t0");
        return sb;
    }

    private ArrayList<Produto> getProdutos(String pQuery, String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb;
        if (pQuery == null)
            sb = retornaQueryProduto();
        else {
            sb = new Util_IO.StringBuilder();
            sb.append(pQuery);
        }
        if (!sb.toString().contains("WHERE"))
            sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<Produto> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), pCampos);
            Produto produto;
            if (cursor.moveToFirst()) {
                do {
                    produto = new Produto();
                    produto.setId(cursor.getString(0));
                    produto.setCodSgv(cursor.getString(1));
                    produto.setNome(cursor.getString(2));
                    produto.setValor(cursor.getDouble(3));
                    produto.setEstoqueMax(cursor.getInt(4));
                    produto.setMediaDiariaVnd(cursor.getInt(5));
                    produto.setDiasEstoque(cursor.getInt(6));
                    produto.setEstoqueAtual(cursor.getInt(7));
                    produto.setEstoqueSugerido(cursor.getInt(8));
                    produto.setAtivo(cursor.getString(9));
                    produto.setBipagem(cursor.getString(10));
                    produto.setPrecovenda(cursor.getDouble(11));
                    produto.setBipagemAuditoria(cursor.getString(12));
                    produto.setQtdCodBarra(cursor.getInt(13));
                    produto.setIniciaCodBarra(cursor.getString(14));
                    produto.setBipagemCliente(cursor.getString(15));
                    produto.setQtdCombo(cursor.getInt(16));
                    produto.setPermiteVendaSemValor(cursor.getString(17));
                    produto.setGrupo(cursor.getInt(18));
                    produto.setOperadora(cursor.getInt(19));
                    produto.setPermiteVendaPrazo(cursor.getString(20));
                    produto.setVendaAvista(cursor.getString(21));
                    lista.add(produto);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lista;
    }

    public void addAuditagemCliente(AuditagemCliente pAuditagemCliente) {
        ContentValues values = new ContentValues();
        values.put("idCliente", pAuditagemCliente.getIdCliente());
        values.put("idProduto", pAuditagemCliente.getIdProduto());
        values.put("qtdeInformada", pAuditagemCliente.getQuantidade());
        values.put("versaoApp", BuildConfig.VERSION_NAME);
        long id = SimpleDbHelper.INSTANCE.open(context).insert(mTabelaAuditagemCliente, null, values);

        if (pAuditagemCliente.getListaCodigos() != null) {
            for (CodBarra codBarra : pAuditagemCliente.getListaCodigos()) {
                values = new ContentValues();
                values.put("idAuditagem", id);
                values.put("codigoBarra", codBarra.getCodBarraInicial());
                values.put("codigoBarraFinal", codBarra.getCodBarraFinal());
                values.put("quantidade", codBarra.retornaQuantidade(UsoCodBarra.AUDITAGEM_CLIENTE));
                String mTabelaAuditagemClienteCodBarra = "AuditagemClienteCodBarra";
                SimpleDbHelper.INSTANCE.open(context).insert(mTabelaAuditagemClienteCodBarra, null, values);
            }
        }
    }

    public void deletaAuditagemClienteNaoConfirmada(String pIdCliente) {
        try {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("DELETE FROM [AuditagemClienteCodBarra]");
            sb.appendLine("WHERE [idAuditagem] IN (SELECT id FROM [AuditagemCliente] WHERE confirmado = 'N' AND idCliente = '" + pIdCliente + "')");
            SimpleDbHelper.INSTANCE.open(context).execSQL(sb.toString());
            SimpleDbHelper.INSTANCE.open(context).delete(mTabelaAuditagemCliente, "[confirmado]=? AND [idCliente]=?", new String[]{"N", pIdCliente});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void confirmaAuditagemCliente(String pIdCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("UPDATE [AuditagemCliente] SET [confirmado] = 'S'");
        sb.appendLine("WHERE date([data]) = date('now','localtime') AND [confirmado] = 'N' AND [idCliente] = '" + pIdCliente + "'");
        SimpleDbHelper.INSTANCE.open(context).execSQL(sb.toString());
    }

    public void confirmaAuditagem(AuditagemEstoque auditagemEstoque) {
        ContentValues values = new ContentValues();
        values.put("confirmado", "S");
        String mTabelaAuditagem = "AuditagemEstoque";
        SimpleDbHelper.INSTANCE.open(context).update(mTabelaAuditagem, values, "[id]=?", new String[]{String.valueOf(auditagemEstoque.getId())});
    }

    public void deletaTodaAuditagemCliente(String pIdCliente) {
        try {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("DELETE FROM [AuditagemClienteCodBarra]");
            sb.appendLine("WHERE [idAuditagem] IN (SELECT id FROM [AuditagemCliente] WHERE confirmado = 'N' AND idCliente = '" + pIdCliente + "')");
            SimpleDbHelper.INSTANCE.open(context).execSQL(sb.toString());
            SimpleDbHelper.INSTANCE.open(context).delete(mTabelaAuditagemCliente, "[idCliente]=?", new String[]{pIdCliente});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Produto> getProdutosComEstoque() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.ativo = 'S'");
        sb.appendLine("AND IFNULL(t0.estoqueAtual,0) > 0");
        return getProdutos(sb.toString(), null);
    }

    public void deletarPistolagemByComboId(int idCombo) {
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaCombo, "[idCombo]=?", new String[]{String.valueOf(idCombo)});
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPistolagem, "[idCombo]=?", new String[]{String.valueOf(idCombo)});
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPistolagemCombo, "[id]=?", new String[]{String.valueOf(idCombo)});
    }

    public EstruturaProd obterEstruturaPeloPaiEFilho(String itemPai, String itemFilho) {
        Util_IO.StringBuilder sb = queryEstrutura();
        sb.appendLine("AND itemPai = ? AND itemFilho = ?");

        try (Cursor cursor = SimpleDbHelper.INSTANCE
                .open(context)
                .rawQuery(sb.toString(), new String[]{itemPai, itemFilho})) {
            if (cursor.moveToFirst()) {
                return getEstruturaProdCursor(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void confirmarPistolagens(Venda venda) {
        ContentValues values = new ContentValues();
        values.put("finalizado", 1);
        SimpleDbHelper.INSTANCE.open(context).update(mTabelaCombo, values, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(venda.getId())});
        SimpleDbHelper.INSTANCE.open(context).update(mTabelaPistolagem, values, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(venda.getId())});
        SimpleDbHelper.INSTANCE.open(context).update(mTabelaPistolagemCombo, values, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(venda.getId())});
    }

    public void confirmarPistolagens(Consignado consignado) {
        ContentValues values = new ContentValues();
        values.put("finalizado", 1);
        SimpleDbHelper.INSTANCE.open(context).update(mTabelaCombo, values, "[IdConsignado]=? AND [finalizado]=0", new String[]{String.valueOf(consignado.getId())});
        SimpleDbHelper.INSTANCE.open(context).update(mTabelaPistolagem, values, "[IdConsignado]=? AND [finalizado]=0", new String[]{String.valueOf(consignado.getId())});
        SimpleDbHelper.INSTANCE.open(context).update(mTabelaPistolagemCombo, values, "[IdConsignado]=? AND [finalizado]=0", new String[]{String.valueOf(consignado.getId())});
    }

    public List<CodBarra> getPistolagemNaoFinalizada(String idProduto) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.finalizado = 0");
        sb.appendLine("AND t0.idProduto = ?");
        sb.appendLine("AND t0.idCliente IS NULL");
        sb.appendLine("AND t0.idVenda IS NULL");
        sb.appendLine("ORDER BY t0.id");
        return getPistolagem(sb.toString(), new String[]{idProduto});
    }

    private ArrayList<CodBarra> getPistolagem(String condicao, String[] campos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.codigoBarra");
        sb.appendLine(",IFNULL(t0.codigoBarraFinal,'')");
        sb.appendLine(",t0.idProduto");
        sb.appendLine(",IFNULL(t1.grupo,0)");
        sb.appendLine(",t0.id");
        sb.appendLine(",IFNULL(t0.AuditadoCons,'N')");
        sb.appendLine("FROM [PistolagemTemp] t0");
        sb.appendLine("LEFT OUTER JOIN [Produto] t1 ON (t0.idProduto = t1.id)");
        sb.appendLine("WHERE 1=1");
        if (condicao != null)
            sb.append(condicao);

        ArrayList<CodBarra> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), campos)) {
            CodBarra codBarra;
            if (cursor.moveToFirst()) {
                do {
                    codBarra = new CodBarra();
                    codBarra.setIdProduto(cursor.getString(2));
                    codBarra.setCodBarraInicial(cursor.getString(0));
                    codBarra.setCodBarraFinal(cursor.getString(1));
                    codBarra.setIndividual(Util_IO.isNullOrEmpty(cursor.getString(1)));
                    codBarra.setGrupoProduto(cursor.getInt(3));
                    codBarra.setIdPistolagem(cursor.getInt(4));
                    codBarra.setAuditadoCons(cursor.getString(5));
                    lista.add(codBarra);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // Pistolagens de Venda
    public ItemVendaCombo getPistolagem(Venda venda, String idProduto, String idPreco) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[finalizado] = 0");
        sb.appendLine("AND t0.[idvenda] = ?");
        sb.appendLine("AND t0.[idProduto] = ?");
        sb.appendLine("AND IFNULL(t0.[idPreco],0) = IFNULL(CAST(? AS INTEGER),0)");

        return Utilidades.firstOrDefault(getPistolagens(sb.toString(), new String[]{String.valueOf(venda.getId()), idProduto, idPreco}));
    }

    // Pistolagens de Consignado
    public ItemVendaCombo getPistolagem(Consignado consignado, String idProduto, String idPreco, int pStatus) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        //sb.appendLine("AND t0.[finalizado] = " + pStatus);
        sb.appendLine("AND t0.[IdConsignado] = " + consignado.getId());
        sb.appendLine("AND t0.[idProduto] = '" + idProduto + "'");
        return Utilidades.firstOrDefault(getPistolagensConsignado(sb.toString(),null));
    }

    public ArrayList<ItemVendaCombo> getPistolagemItem(Consignado consignado, String idProduto) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        //sb.appendLine("AND t0.[finalizado] = 1");
        sb.appendLine("AND t0.[IdConsignado] = " + consignado.getId());
        sb.appendLine("AND t0.[idProduto] = '" + idProduto + "'");
        return getPistolagensConsignado(sb.toString(), null);
    }

    private ArrayList<ItemVendaCombo> getPistolagens(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.[id]");
        sb.appendLine(",t0.[qtdCombo]");
        sb.appendLine(",t0.[idProduto]");
        sb.appendLine(",t0.[isCombo]");
        sb.appendLine(",IFNULL(t0.[idPreco],0) AS idPreco");
        sb.appendLine(",t0.[valor]");
        sb.appendLine(",t0.[quantidade]");
        sb.appendLine(",t0.[finalizado]");
        sb.appendLine(",t0.[idvenda]");
        sb.appendLine(",t0.[data]");
        sb.appendLine(",t1.[idCliente]");
        sb.appendLine(",t2.[nome]");
        sb.appendLine("FROM [PistolagemComboTemp] t0");
        sb.appendLine("LEFT OUTER JOIN [Venda] t1 ON (t0.[idvenda] = t1.[id])");
        sb.appendLine("JOIN [Produto] t2 ON (t2.[id] = t0.[idProduto])");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<ItemVendaCombo> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), pCampos);
            ItemVendaCombo itemVendaCombo;
            if (cursor.moveToFirst()) {
                do {
                    itemVendaCombo = new ItemVendaCombo();
                    itemVendaCombo.setId(cursor.getInt(0));
                    itemVendaCombo.setQtdCombo(cursor.getInt(1));
                    itemVendaCombo.setIdProduto(cursor.getString(2));
                    itemVendaCombo.setListItens(getCombosVendaNaoFinalizado(cursor.getInt(0)));
                    itemVendaCombo.setQtde(cursor.getInt(6));
                    itemVendaCombo.setNomeProduto(cursor.getString(11));
                    itemVendaCombo.setCodigosList(getPistolagemTempVenda(cursor.getInt(0), cursor.getInt(8), cursor.getString(10)));
                    itemVendaCombo.setCombo(Util_IO.numberToBoolean(cursor.getInt(3)));
                    itemVendaCombo.setValorUN(cursor.getDouble(5));
                    itemVendaCombo.setIdPreco(cursor.getInt(4));
                    lista.add(itemVendaCombo);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lista;
    }

    private ArrayList<ItemVendaCombo> getPistolagensConsignado(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.[id]");
        sb.appendLine(",t0.[qtdCombo]");
        sb.appendLine(",t0.[idProduto]");
        sb.appendLine(",t0.[isCombo]");
        sb.appendLine(",IFNULL(t0.[idPreco],0) AS idPreco");
        sb.appendLine(",t2.[PrecoVenda]");
        sb.appendLine(",t0.[quantidade]");
        sb.appendLine(",t0.[finalizado]");
        sb.appendLine(",t0.[IdConsignado]");
        sb.appendLine(",t0.[data]");
        sb.appendLine(",t1.[idCliente]");
        sb.appendLine(",t2.[nome]");
        sb.appendLine("FROM [PistolagemComboTemp] t0");
        sb.appendLine("LEFT OUTER JOIN [Consignado] t1 ON (t0.[IdConsignado] = t1.[id])");
        sb.appendLine("JOIN [Produto] t2 ON (t2.[id] = t0.[idProduto])");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<ItemVendaCombo> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), pCampos);
            ItemVendaCombo itemVendaCombo;
            if (cursor.moveToFirst()) {
                do {
                    itemVendaCombo = new ItemVendaCombo();
                    itemVendaCombo.setId(cursor.getInt(0));
                    itemVendaCombo.setQtdCombo(cursor.getInt(1));
                    itemVendaCombo.setIdProduto(cursor.getString(2));
                    itemVendaCombo.setListItens(getCombosVendaNaoFinalizado(cursor.getInt(0)));
                    itemVendaCombo.setQtde(cursor.getInt(6));
                    itemVendaCombo.setNomeProduto(cursor.getString(11));
                    itemVendaCombo.setCodigosList(getPistolagemTempConsignado(cursor.getInt(0), cursor.getInt(8), cursor.getString(10)));
                    itemVendaCombo.setCombo(Util_IO.numberToBoolean(cursor.getInt(3)));
                    itemVendaCombo.setValorUN(cursor.getDouble(5));
                    itemVendaCombo.setIdPreco(cursor.getInt(4));
                    lista.add(itemVendaCombo);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lista;
    }

    private ArrayList<CodBarra> getPistolagemTempVenda(int pIdCombo, int pIdVenda, String pIdCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [idCombo] = ?");
        sb.appendLine("AND [idVenda] = ?");
        sb.appendLine("AND [idCliente] = ?");
        sb.appendLine("AND [finalizado] = 0");
        return getPistolagem(sb.toString(), new String[]{String.valueOf(pIdCombo), String.valueOf(pIdVenda), pIdCliente});
    }

    private ArrayList<CodBarra> getPistolagemTempConsignado(int pIdCombo, int pIdConsignado, String pIdCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[idCombo] = " + pIdCombo);
        sb.appendLine("AND t0.[IdConsignado] = " + pIdConsignado);
        sb.appendLine("AND t0.[idCliente] = " + pIdCliente);
        return getPistolagem(sb.toString(), null);
    }

    private ArrayList<ComboVenda> getCombosVendaNaoFinalizado(int pIdCombo) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[idCombo]");
        sb.appendLine(",[idProduto]");
        sb.appendLine(",[quantidade]");
        sb.appendLine(",[finalizado]");
        sb.appendLine(",[data]");
        sb.appendLine("FROM [ComboTemp]");
        sb.appendLine("WHERE 1=1");
        sb.appendLine("AND [finalizado] = 0");
        sb.appendLine("AND [idCombo] = ?");

        Cursor cursor = null;
        ArrayList<ComboVenda> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), new String[]{String.valueOf(pIdCombo)});
            ComboVenda comboVenda;
            if (cursor.moveToFirst()) {
                do {
                    comboVenda = new ComboVenda();
                    comboVenda.setId(cursor.getInt(0));
                    comboVenda.setIdProduto(cursor.getString(2));
                    comboVenda.setQuantidade(cursor.getInt(3));
                    lista.add(comboVenda);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lista;
    }

    // Carrega as Bipagens não Finalizadas Venda
    public List<ItemVendaCombo> getPistolagensComboNaoFinalizada(Venda venda) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[idvenda] = ?");
        sb.appendLine("AND t0.[finalizado] = 0");
        return getPistolagens(sb.toString(), new String[]{String.valueOf(venda.getId())});
    }

    // Carrega as Bipagens não Finalizadas Consignado
    public List<ItemVendaCombo> getPistolagensComboNaoFinalizada(Consignado pConsignado) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[IdConsignado] = ?");
        sb.appendLine("AND t0.[finalizado] = 0");
        return getPistolagensConsignado(sb.toString(), new String[]{String.valueOf(pConsignado.getId())});
    }

    public List<ItemVendaCombo> getPistolagensConsignadoItens(Consignado pConsignado) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[IdConsignado] = ?");
        return getPistolagensConsignado(sb.toString(), new String[]{String.valueOf(pConsignado.getId())});
    }

    // Deletar temporarias de pistolagens Venda
    public void deletarPistolagensComboNaoFinalizadas(Venda pVenda) {
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaCombo, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(pVenda.getId())});
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPistolagem, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(pVenda.getId())});
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPistolagemCombo, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(pVenda.getId())});
    }

    // Deletar temporarias de pistolagens Consignado
    public void deletarPistolagensComboNaoFinalizadas(Consignado pConsignado) {
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaCombo, "[IdConsignado]=? AND [finalizado]=0", new String[]{String.valueOf(pConsignado.getId())});
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPistolagem, "[IdConsignado]=? AND [finalizado]=0", new String[]{String.valueOf(pConsignado.getId())});
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPistolagemCombo, "[IdConsignado]=? AND [finalizado]=0", new String[]{String.valueOf(pConsignado.getId())});
    }

    public void deletarPistolagensConsignado(Consignado pConsignado) {
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaCombo, "[IdConsignado]=?", new String[]{String.valueOf(pConsignado.getId())});
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPistolagem, "[IdConsignado]=?", new String[]{String.valueOf(pConsignado.getId())});
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPistolagemCombo, "[IdConsignado]=?", new String[]{String.valueOf(pConsignado.getId())});
    }

    public void incluirComboPistolagem(ItemVendaCombo itemVendaCombo, Venda venda) {
        ContentValues values = new ContentValues();
        values.put("qtdCombo", itemVendaCombo.getQtdCombo());
        values.put("idProduto", itemVendaCombo.getIdProduto());
        values.put("isCombo", Util_IO.booleanToNumber(itemVendaCombo.isCombo()));
        values.put("idPreco", itemVendaCombo.getIdPreco());
        values.put("valor", itemVendaCombo.getValorUN());
        values.put("quantidade", itemVendaCombo.getQtde());
        values.put("finalizado", 0);
        values.put("idvenda", venda.getId());
        long codigo = itemVendaCombo.getId();
        if (codigo > 0)
            SimpleDbHelper.INSTANCE.open(context).update(mTabelaPistolagemCombo, values, "[id]=?", new String[]{String.valueOf(codigo)});
        else
            codigo = SimpleDbHelper.INSTANCE.open(context).insert(mTabelaPistolagemCombo, null, values);

        if (itemVendaCombo.getCodigosList() != null && itemVendaCombo.getCodigosList().size() > 0) {
            for (CodBarra codBarra : itemVendaCombo.getCodigosList()) {
                values = new ContentValues();
                values.put("idProduto", codBarra.getIdProduto());
                values.put("idCliente", venda.getIdCliente());
                values.put("codigoBarra", codBarra.getCodBarraInicial());
                if (!Util_IO.isNullOrEmpty(codBarra.getCodBarraFinal()))
                    values.put("codigoBarraFinal", codBarra.getCodBarraFinal());
                values.put("quantidade", codBarra.retornaQuantidade(UsoCodBarra.GERAL));
                values.put("finalizado", 0);
                values.put("idVenda", venda.getId());
                values.put("idCombo", codigo);
                values.put("AuditadoCons",codBarra.getAuditadoCons());
                if (codBarra.getIdPistolagem() > 0)
                    SimpleDbHelper.INSTANCE.open(context).update(mTabelaPistolagem, values, "[id]=?", new String[]{String.valueOf(codBarra.getIdPistolagem())});
                else
                    SimpleDbHelper.INSTANCE.open(context).insert(mTabelaPistolagem, null, values);
            }
        }

        if (itemVendaCombo.getListItens() != null && itemVendaCombo.getListItens().size() > 0) {
            for (ComboVenda comboVenda : itemVendaCombo.getListItens()) {
                values = new ContentValues();
                values.put("idCombo", codigo);
                values.put("idProduto", comboVenda.getIdProduto());
                values.put("quantidade", comboVenda.getQuantidade());
                values.put("finalizado", 0);
                values.put("idVenda", venda.getId());
                if (comboVenda.getId() > 0)
                    SimpleDbHelper.INSTANCE.open(context).update(mTabelaCombo, values, "[id]=?", new String[]{String.valueOf(comboVenda.getId())});
                else
                    SimpleDbHelper.INSTANCE.open(context).insert(mTabelaCombo, null, values);
            }
        }
    }

    public void incluirComboPistolagem(ItemVendaCombo itemVendaCombo, Consignado consignado) {
        ContentValues values = new ContentValues();
        values.put("qtdCombo", itemVendaCombo.getQtdCombo());
        values.put("idProduto", itemVendaCombo.getIdProduto());
        values.put("isCombo", Util_IO.booleanToNumber(itemVendaCombo.isCombo()));
        values.put("idPreco", itemVendaCombo.getIdPreco());
        values.put("valor", itemVendaCombo.getValorUN());
        values.put("quantidade", itemVendaCombo.getQtde());
        values.put("finalizado", 0);
        values.put("IdConsignado", consignado.getId());
        long codigo = itemVendaCombo.getId();
        if (codigo > 0)
            SimpleDbHelper.INSTANCE.open(context).update(mTabelaPistolagemCombo, values, "[id]=?", new String[]{String.valueOf(codigo)});
        else
            codigo = SimpleDbHelper.INSTANCE.open(context).insert(mTabelaPistolagemCombo, null, values);

        if (itemVendaCombo.getCodigosList() != null && itemVendaCombo.getCodigosList().size() > 0) {
            for (CodBarra codBarra : itemVendaCombo.getCodigosList()) {
                values = new ContentValues();
                values.put("idProduto", codBarra.getIdProduto());
                values.put("idCliente", consignado.getIdCliente());
                values.put("codigoBarra", codBarra.getCodBarraInicial());
                if (!Util_IO.isNullOrEmpty(codBarra.getCodBarraFinal()))
                    values.put("codigoBarraFinal", codBarra.getCodBarraFinal());
                values.put("quantidade", codBarra.retornaQuantidade(UsoCodBarra.GERAL));
                values.put("finalizado", 0);
                values.put("IdConsignado", consignado.getId());
                values.put("idCombo", codigo);
                values.put("AuditadoCons",codBarra.getAuditadoCons());
                if (codBarra.getIdPistolagem() > 0)
                    SimpleDbHelper.INSTANCE.open(context).update(mTabelaPistolagem, values, "[id]=?", new String[]{String.valueOf(codBarra.getIdPistolagem())});
                else
                    SimpleDbHelper.INSTANCE.open(context).insert(mTabelaPistolagem, null, values);
            }
        }

        if (itemVendaCombo.getListItens() != null && itemVendaCombo.getListItens().size() > 0) {
            for (ComboVenda comboVenda : itemVendaCombo.getListItens()) {
                values = new ContentValues();
                values.put("idCombo", codigo);
                values.put("idProduto", comboVenda.getIdProduto());
                values.put("quantidade", comboVenda.getQuantidade());
                values.put("finalizado", 0);
                values.put("idConsignado", consignado.getId());
                if (comboVenda.getId() > 0)
                    SimpleDbHelper.INSTANCE.open(context).update(mTabelaCombo, values, "[id]=?", new String[]{String.valueOf(comboVenda.getId())});
                else
                    SimpleDbHelper.INSTANCE.open(context).insert(mTabelaCombo, null, values);
            }
        }
    }

    @Override
    public Single<List<EstruturaProd>> getItemsCombo(String codigoProd) {
        return Single.create(emitter -> {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("SELECT id, itemPai, itemFilho, qtd ");
            sb.appendLine("FROM [EstruturaProd]");
            sb.appendLine("WHERE itemPai = ?");
            try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), new String[]{codigoProd})) {
                EstruturaProd estruturaProd;
                List<EstruturaProd> lista = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        estruturaProd = new EstruturaProd();
                        estruturaProd.setId(cursor.getInt(0));
                        estruturaProd.setItemPai(cursor.getString(1));
                        estruturaProd.setItemFilho(cursor.getString(2));
                        estruturaProd.setQtd(cursor.getInt(3));
                        lista.add(estruturaProd);
                    } while (cursor.moveToNext());

                    if (lista.size() > 0) {
                        emitter.onSuccess(lista);
                    } else {
                        emitter.onError(new Throwable("Produto não é combo"));
                    }
                }
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    @Override
    public ArrayList<Produto> getProdutosPorSugestaoVenda(String clienteId) {
        Util_IO.StringBuilder sb;
        sb = retornaQueryProduto();
        sb.appendLine(" INNER JOIN [SugestaoVenda] t1 ON t1.grupoProdutoSAP = t0.grupo and t1.operadoraId = t0.operadora ");
        sb.appendLine(" WHERE t0.ativo = 'S' AND t0.estoqueAtual > 0 AND t1.clienteId = ?");

        Cursor cursor = null;
        ArrayList<Produto> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(),new String[]{clienteId});
            Produto produto;
            if (cursor.moveToFirst()) {
                do {
                    produto = new Produto();
                    produto.setId(cursor.getString(0));
                    produto.setCodSgv(cursor.getString(1));
                    produto.setNome(cursor.getString(2));
                    produto.setValor(cursor.getDouble(3));
                    produto.setEstoqueMax(cursor.getInt(4));
                    produto.setMediaDiariaVnd(cursor.getInt(5));
                    produto.setDiasEstoque(cursor.getInt(6));
                    produto.setEstoqueAtual(cursor.getInt(7));
                    produto.setEstoqueSugerido(cursor.getInt(8));
                    produto.setAtivo(cursor.getString(9));
                    produto.setBipagem(cursor.getString(10));
                    produto.setPrecovenda(cursor.getDouble(11));
                    produto.setBipagemAuditoria(cursor.getString(12));
                    produto.setQtdCodBarra(cursor.getInt(13));
                    produto.setIniciaCodBarra(cursor.getString(14));
                    produto.setBipagemCliente(cursor.getString(15));
                    produto.setQtdCombo(cursor.getInt(16));
                    produto.setPermiteVendaSemValor(cursor.getString(17));
                    produto.setGrupo(cursor.getInt(18));
                    produto.setOperadora(cursor.getInt(19));
                    produto.setPermiteVendaPrazo(cursor.getString(20));
                    produto.setVendaAvista(cursor.getString(21));
                    lista.add(produto);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lista;
    }

    @Override
    public ArrayList<Produto> getProdutosPorConsignacao(String clienteId) {
        Util_IO.StringBuilder sb;
        sb = retornaQueryProduto();
        sb.appendLine("join ConsignadoLimiteCliente t1 on t1.idCliente = ? ");
        sb.appendLine("join ConsignadoLimiteProduto t2 on t2.idConsignadolimiteCliente = t1.id and  t2.IdProduto = t0.id");
        sb.appendLine(" WHERE t0.ativo = 'S'"); // AND t0.estoqueAtual > 0");

        Cursor cursor = null;
        ArrayList<Produto> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(),new String[]{clienteId});
            Produto produto;
            if (cursor.moveToFirst()) {
                do {
                    produto = new Produto();
                    produto.setId(cursor.getString(0));
                    produto.setCodSgv(cursor.getString(1));
                    produto.setNome(cursor.getString(2));
                    produto.setValor(cursor.getDouble(3));
                    produto.setEstoqueMax(cursor.getInt(4));
                    produto.setMediaDiariaVnd(cursor.getInt(5));
                    produto.setDiasEstoque(cursor.getInt(6));
                    produto.setEstoqueAtual(cursor.getInt(7));
                    produto.setEstoqueSugerido(cursor.getInt(8));
                    produto.setAtivo(cursor.getString(9));
                    produto.setBipagem(cursor.getString(10));
                    produto.setPrecovenda(cursor.getDouble(11));
                    produto.setBipagemAuditoria(cursor.getString(12));
                    produto.setQtdCodBarra(cursor.getInt(13));
                    produto.setIniciaCodBarra(cursor.getString(14));
                    produto.setBipagemCliente(cursor.getString(15));
                    produto.setQtdCombo(cursor.getInt(16));
                    produto.setPermiteVendaSemValor(cursor.getString(17));
                    produto.setGrupo(cursor.getInt(18));
                    produto.setOperadora(cursor.getInt(19));
                    produto.setPermiteVendaPrazo(cursor.getString(20));
                    produto.setVendaAvista(cursor.getString(21));
                    lista.add(produto);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lista;
    }

    public void confirmarPistolagensConsignadoAuditagem(Consignado consignado, Produto produto) {
        ContentValues values = new ContentValues();
        values.put("finalizado", 1);
        SimpleDbHelper.INSTANCE.open(context).update(mTabelaPistolagem, values, "[IdConsignado]=? AND [IdProduto]=? AND [finalizado]=0", new String[]{String.valueOf(consignado.getId()),String.valueOf(produto.getId())});
        SimpleDbHelper.INSTANCE.open(context).update(mTabelaPistolagemCombo, values, "[IdConsignado]=? AND [IdProduto]=? AND [finalizado]=0", new String[]{String.valueOf(consignado.getId()),String.valueOf(produto.getId())});
    }

    public void deletarPistolagemByIdConsignado(int IdConsignado) {
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaCombo, "[IdConsignado]=?", new String[]{String.valueOf(IdConsignado)});
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPistolagem, "[IdConsignado]=?", new String[]{String.valueOf(IdConsignado)});
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPistolagemCombo, "[IdConsignado]=?", new String[]{String.valueOf(IdConsignado)});
    }
}
