package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.enums.EnumStatusAuditagem;
import com.axys.redeflexmobile.shared.enums.EnumTipoAuditagem;
import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.AuditagemEstoque;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.ValorPorFormaPagto;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 01/04/2016.
 */
public class DBEstoque {

    public static final int NAO_EXISTE_AUDITAGEM = -1;
    private final Context mContext;
    private final String mTabelaProduto = "Produto";
    private final String mTabelaAuditagem = "AuditagemEstoque";
    private final String mTabelaAuditagemCodBarra = "AuditagemEstoqueCodigoBarra";
    private final String mTabelaEstrutura = "EstruturaProd";
    private final String mTabelaPistolagem = "PistolagemTemp";
    private final String mTabelaAuditagemCliente = "AuditagemCliente";
    private final String mTabelaAuditagemClienteCodBarra = "AuditagemClienteCodBarra";
    private final String mTabelaPistolagemCombo = "PistolagemComboTemp";
    private final String mTabelaCombo = "ComboTemp";

    public DBEstoque(Context pContext) {
        mContext = pContext;
    }

    public void addAuditagemCliente(AuditagemCliente pAuditagemCliente) {
        ContentValues values = new ContentValues();
        values.put("idCliente", pAuditagemCliente.getIdCliente());
        values.put("idProduto", pAuditagemCliente.getIdProduto());
        values.put("qtdeInformada", pAuditagemCliente.getQuantidade());
        values.put("versaoApp", BuildConfig.VERSION_NAME);
        long id = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaAuditagemCliente, null, values);

        if (pAuditagemCliente.getListaCodigos() != null) {
            for (CodBarra codBarra : pAuditagemCliente.getListaCodigos()) {
                values = new ContentValues();
                values.put("idAuditagem", id);
                values.put("codigoBarra", codBarra.getCodBarraInicial());
                values.put("codigoBarraFinal", codBarra.getCodBarraFinal());
                values.put("quantidade", codBarra.retornaQuantidade(UsoCodBarra.AUDITAGEM_CLIENTE));
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaAuditagemClienteCodBarra, null, values);
            }
        }
    }

    public void deletaAuditagemClienteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagemClienteCodBarra, "[idAuditagem]=?", new String[]{pId});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagemCliente, "[id]=?", new String[]{pId});
    }

    public ArrayList<AuditagemCliente> getAuditagensCliente(String pIdCliente) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t0.[id] ");
        sb.append(",t0.[idCliente] ");
        sb.append(",t0.[idProduto] ");
        sb.append(",t0.[qtdeInformada] ");
        sb.append(",t1.[nome] ");
        sb.append(",t0.[confirmado] ");
        sb.append("FROM [AuditagemCliente] t0 ");
        sb.append("JOIN [Produto] t1 ON (t0.[idProduto] = t1.[id]) ");
        sb.append("WHERE 1=1 ");
        sb.append("AND date(t0.[data]) = date('now','localtime') ");
        sb.append("AND t0.[idCliente] = ? ");

        Cursor cursor = null, cursor2 = null;
        ArrayList<AuditagemCliente> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pIdCliente});
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

                    String selectQuery = "SELECT codigoBarra, IFNULL(codigoBarraFinal,'-1') FROM [AuditagemClienteCodBarra] WHERE idAuditagem = ?";
                    cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, new String[]{String.valueOf(auditagemCliente.getId())});

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

    public void addEstrutura(EstruturaProd pItem) throws Exception {
        if (pItem.isAtivo()) {
            ContentValues values = new ContentValues();
            values.put("itemPai", pItem.getItemPai());
            values.put("itemFilho", pItem.getItemFilho());
            values.put("qtd", pItem.getQtd());
            values.put("proporcao", pItem.getProporcao());
            if (!Util_DB.isCadastrado(mContext, mTabelaEstrutura, "id", String.valueOf(pItem.getId()))) {
                values.put("id", pItem.getId());
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaEstrutura, null, values);
            } else
                SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaEstrutura, values, "[id]=?", new String[]{String.valueOf(pItem.getId())});
        } else
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaEstrutura, "[id]=?", new String[]{String.valueOf(pItem.getId())});
    }

    public void addPistolagem(CodBarra pCodBarra, String pIdProduto, String pIdCliente, String pIdVenda) {
        ContentValues values = new ContentValues();
        values.put("idProduto", pIdProduto);
        if (!Util_IO.isNullOrEmpty(pIdCliente))
            values.put("idCliente", pIdCliente);
        if (!Util_IO.isNullOrEmpty(pIdVenda))
            values.put("idVenda", pIdVenda);
        values.put("codigoBarra", pCodBarra.getCodBarraInicial());
        values.put("codigoBarraFinal", pCodBarra.getCodBarraFinal());
        values.put("quantidade", pCodBarra.retornaQuantidade(UsoCodBarra.GERAL));
        values.put("finalizado", 0);
        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaPistolagem, null, values);
    }

    public void deletarPistolagemNaoFinalizadas(EnumTipoAuditagem pEnumTipoAuditagem) {
        String query = "";
        switch (pEnumTipoAuditagem) {
            case AuditagemVendedor:
                query = "[finalizado]=0 AND [idCliente] IS NULL AND [idVenda] IS NULL";
                break;
            case Venda:
                query = "[finalizado]=0 AND [idVenda] IS NOT NULL";
                break;
            case AuditagemCliente:
                query = "[finalizado]=0 AND [idCliente] IS NOT NULL AND [idVenda] IS NULL";
                break;
        }
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagem, query, null);
    }

    public void deletarPistolagemById(int pIdPistolagem) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagem, "[id]=?", new String[]{String.valueOf(pIdPistolagem)});
    }

    public void confirmaPistolagem(String pIdProduto) {
        ContentValues values = new ContentValues();
        values.put("finalizado", 1);
        String query = "[finalizado]=0 AND [idCliente] IS NULL AND [idProduto]=? AND [idVenda] IS NULL";
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaPistolagem, values, query, new String[]{pIdProduto});
    }

    public void confirmaPistolagem(String pIdProduto, String pIdCliente, String pIdVenda) {
        ContentValues values = new ContentValues();
        values.put("finalizado", 1);
        String query = "[finalizado]=0 AND [idCliente]=? AND [idProduto]=? AND [idVenda]=?";
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaPistolagem, values, query, new String[]{pIdCliente, pIdProduto, pIdVenda});
    }

    public ArrayList<CodBarra> getPistolagemNaoFinalizada(String pIdProduto) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.finalizado = 0");
        sb.appendLine("AND t0.idProduto = ?");
        sb.appendLine("AND t0.idCliente IS NULL");
        sb.appendLine("AND t0.idVenda IS NULL");
        sb.appendLine("ORDER BY t0.id");
        return getPistolagem(sb.toString(), new String[]{pIdProduto});
    }

    public ArrayList<CodBarra> getPistolagemNaoFinalizada(String pIdProduto, String pIdCliente, String pIdVenda) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.finalizado = 0");
        sb.appendLine("AND t0.idProduto = ?");
        sb.appendLine("AND t0.idCliente = ?");
        sb.appendLine("AND t0.idVenda = ?");
        sb.appendLine("ORDER BY t0.id");
        return getPistolagem(sb.toString(), new String[]{pIdProduto, pIdCliente, pIdVenda});
    }

    private ArrayList<CodBarra> getPistolagem(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.codigoBarra");
        sb.appendLine(",IFNULL(t0.codigoBarraFinal,'')");
        sb.appendLine(",t0.idProduto");
        sb.appendLine(",IFNULL(t1.grupo,0)");
        sb.appendLine(",t0.id");
        sb.appendLine("FROM [PistolagemTemp] t0");
        sb.appendLine("LEFT OUTER JOIN [Produto] t1 ON (t0.idProduto = t1.id)");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        ArrayList<CodBarra> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos)) {
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
                    lista.add(codBarra);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public ArrayList<EstruturaProd> getEstruturaByItemPai(String pItemCode) {
        Util_IO.StringBuilder sb = queryEstrutura();
        sb.appendLine("AND itemPai = ?");
        Cursor cursor = null;
        ArrayList<EstruturaProd> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pItemCode});
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

    public boolean isProdutoCombo(String pItemCode) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine("FROM [EstruturaProd]");
        sb.appendLine("WHERE itemPai = ?");
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pItemCode})) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void addProduto(Produto produto) throws Exception {
        ContentValues values = new ContentValues();
        values.put("id", produto.getId());
        values.put("codSgv", produto.getCodSgv());
        values.put("nome", produto.getNome());
        values.put("valor", produto.getValor());
        values.put("estoqueMax", produto.getEstoqueMax());
        values.put("mediaDiariaVnd", produto.getMediaDiariaVnd());
        values.put("diasEstoque", produto.getDiasEstoque());
        values.put("estoqueAtual", produto.getEstoqueAtual());
        values.put("estoqueSugerido", produto.getEstoqueSugerido());
        values.put("ativo", produto.getAtivo());
        values.put("bipagem", produto.getBipagem());
        values.put("precovenda", produto.getPrecovenda());
        //values.put("precomedio", produto.getPrecoMedio());
        values.put("bipagemAuditoria", produto.getBipagemAuditoria());
        values.put("qtdCodBarra", produto.getQtdCodBarra());
        values.put("iniciaCodBarra", produto.getIniciaCodBarra());
        values.put("bipagemCliente", produto.getBipagemCliente());
        values.put("qtdCombo", produto.getQtdCombo());
        values.put("permiteValorZero", produto.getPermiteVendaSemValor());
        values.put("grupo", produto.getGrupo());
        values.put("operadora", produto.getOperadora());
        values.put("permiteVendaPrazo", produto.getPermiteVendaPrazo());
        values.put("precoVendaMinimo", produto.getPrecovendaminimo());
        values.put("VendaAvista", produto.getVendaAvista());
        if (produto.getValorPorFormaPagto() != null) {
            values.put("valorPorFormaPagto",
                    new com.google.gson.Gson().toJson(produto.getValorPorFormaPagto()));
        } else {
            values.put("valorPorFormaPagto", "");
        }
        if (!Util_DB.isCadastrado(mContext, mTabelaProduto, "id", String.valueOf(produto.getId())))
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaProduto, null, values);
        else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaProduto, values, "[id]=?", new String[]{produto.getId()});
    }

    public Produto getProdutoById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.id = ?");
        return Utilidades.firstOrDefault(getProdutos(sb.toString(), new String[]{pId}));
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
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
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
                    produto.setPrecovendaminimo(cursor.getDouble(21));
                    produto.setVendaAvista(cursor.getString(22));
                    String jsonValorForma = cursor.getString(23);
                    ValorPorFormaPagto valorForma = ValorPorFormaPagto.fromJson(jsonValorForma);
                    produto.setValorPorFormaPagto(valorForma);
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

    private ArrayList<Produto> getProdutos(String pCondicao, String[] pCampos) {
        return getProdutos(null, pCondicao, pCampos);
    }

    public ArrayList<Produto> getProdutos() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.ativo = 'S'");
        return getProdutos(sb.toString(), null);
    }

    public ArrayList<Produto> getProdutosComEstoque() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.ativo = 'S'");
        sb.appendLine("AND IFNULL(t0.estoqueAtual,0) > 0");
        return getProdutos(sb.toString(), null);
    }

    public ArrayList<Produto> getProdutosSemEstoque() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.bipagemAuditoria = 'S'");
        sb.appendLine("AND (IFNULL(t0.estoqueAtual,0) <= 0 OR t0.ativo = 'N')");
        return getProdutos(sb.toString(), null);
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
        sb.appendLine(",IFNULL(t0.precoVendaMinimo,0)");
        sb.appendLine(",IFNULL(t0.VendaAvista,'N')");
        sb.appendLine(",IFNULL(t0.valorPorFormaPagto,'')");
        sb.appendLine("FROM [Produto] t0");
        return sb;
    }

    public ArrayList<Produto> getProdutosComboENaoPistolados() {
        Util_IO.StringBuilder sb = retornaQueryProduto();
        sb.appendLine("WHERE t0.ativo = 'S'");
        sb.appendLine("AND IFNULL(t0.estoqueAtual,0) > 0");
        sb.appendLine("AND IFNULL(t0.bipagem,'N') = 'N'");
        sb.appendLine("UNION");
        sb.appendLine(retornaQueryProduto().toString());
        sb.appendLine("JOIN EstruturaProd t1 ON (t1.itemPai = t0.id)");
        sb.appendLine("WHERE t0.ativo = 'S'");

        return getProdutos(sb.toString(), null, null);
    }

    public ArrayList<Produto> getEstoqueSugerido() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND IFNULL(t0.estoqueSugerido,0) > 0");
        sb.appendLine("AND t0.ativo = 'S'");

        return getProdutos(sb.toString(), null);
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaProduto, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagem, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagemCodBarra, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaEstrutura, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagem, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagemCliente, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagemClienteCodBarra, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagemCombo, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCombo, null, null);
    }

    public void deletaAuditagemClienteNaoConfirmada(String pIdCliente) {
        try {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("DELETE FROM [AuditagemClienteCodBarra]");
            sb.appendLine("WHERE [idAuditagem] IN (SELECT id FROM [AuditagemCliente] WHERE confirmado = 'N' AND idCliente = '" + pIdCliente + "')");
            SimpleDbHelper.INSTANCE.open(mContext).execSQL(sb.toString());
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagemCliente, "[confirmado]=? AND [idCliente]=?", new String[]{"N", pIdCliente});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deletaTodaAuditagemCliente(String pIdCliente) {
        try {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("DELETE FROM [AuditagemClienteCodBarra]");
            sb.appendLine("WHERE [idAuditagem] IN (SELECT id FROM [AuditagemCliente] WHERE idCliente = '" + pIdCliente + "')");
            SimpleDbHelper.INSTANCE.open(mContext).execSQL(sb.toString());
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagemCliente, "[idCliente]=?", new String[]{pIdCliente});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void confirmaAuditagemCliente(String pIdCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("UPDATE [AuditagemCliente] SET [confirmado] = 'S'");
        sb.appendLine("WHERE date([data]) = date('now','localtime') AND [confirmado] = 'N' AND [idCliente] = '" + pIdCliente + "'");
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(sb.toString());
    }

    public void setSyncAuditagemCliente(int iId) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAuditagemCliente, values, "[id]=?", new String[]{String.valueOf(iId)});
    }

    public void setSyncCodBarraCliente(int iId) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAuditagemClienteCodBarra, values, "[id]=?", new String[]{String.valueOf(iId)});
    }

    public void deletaAuditagemCliente() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("DELETE FROM [AuditagemClienteCodBarra]");
        sb.appendLine("WHERE [idAuditagem] IN (SELECT id FROM [AuditagemCliente] WHERE date([data]) != date('now','localtime') AND [sync] = 1)");
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(sb.toString());

        sb = new Util_IO.StringBuilder();
        sb.appendLine("DELETE FROM [AuditagemCliente] WHERE date([data]) != date('now','localtime') AND [sync] = 1");
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(sb.toString());
    }

    public ArrayList<AuditagemCliente> getAuditagensClientePendentes() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.[idCliente]");
        sb.appendLine(",t0.[idProduto]");
        sb.appendLine(",t0.[data]");
        sb.appendLine(",t0.[qtdeInformada]");
        sb.appendLine(",IFNULL(t1.[codigoBarra],'')");
        sb.appendLine(",IFNULL(t1.[codigoBarraFinal],'')");
        sb.appendLine(",IFNULL(t1.[id],0)");
        sb.appendLine(",t0.[id]");
        sb.appendLine(",t0.[versaoApp]");
        sb.appendLine("FROM [AuditagemCliente] t0");
        sb.appendLine("LEFT OUTER JOIN [AuditagemClienteCodBarra] t1 ON (t0.[id] = t1.[idAuditagem])");
        sb.appendLine("WHERE t0.[confirmado] = 'S'");
        sb.appendLine("AND ((t0.[sync] = 0) OR (t0.[sync] = 1 AND t1.[sync] = 0))");
        Cursor cursor = null;
        ArrayList<AuditagemCliente> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    AuditagemCliente auditagemCliente = new AuditagemCliente();
                    auditagemCliente.setIdCliente(cursor.getString(0));
                    auditagemCliente.setIdProduto(cursor.getString(1));
                    auditagemCliente.setData(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
                    auditagemCliente.setQuantidade(cursor.getInt(3));
                    auditagemCliente.setCodigoBarra(cursor.getString(4));
                    auditagemCliente.setCodigoBarraFinal(cursor.getString(5));
                    auditagemCliente.setIdCodigoBarra(cursor.getInt(6));
                    auditagemCliente.setId(cursor.getInt(7));
                    auditagemCliente.setVersaoApp(cursor.getString(8));
                    lista.add(auditagemCliente);
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

    public void addAuditagemEstoque(AuditagemEstoque auditagemEstoque) {
        ContentValues values = new ContentValues();
        long tempID = existeProdutoAuditado(auditagemEstoque);
        if (tempID != NAO_EXISTE_AUDITAGEM) {
            values.put("id", tempID);
        }
        values.put("idProduto", auditagemEstoque.getIdProduto());
        values.put("qtdeInformada", auditagemEstoque.getQtdeInformada());
        values.put("qtdeReal", auditagemEstoque.getQtdeReal());
        long id = SimpleDbHelper.INSTANCE.open(mContext).replace(mTabelaAuditagem, null, values);

        if (auditagemEstoque.getCodigosList() != null && auditagemEstoque.getCodigosList().size() > 0) {
            for (CodBarra codBarra : auditagemEstoque.getCodigosList()) {
                values = new ContentValues();
                values.put("idAuditagem", id);
                values.put("idProduto", auditagemEstoque.getIdProduto());
                values.put("codigoBarra", codBarra.getCodBarraInicial());
                values.put("codigoBarraFinal", codBarra.getCodBarraFinal());
                values.put("quantidade", codBarra.retornaQuantidade(UsoCodBarra.GERAL));
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaAuditagemCodBarra, null, values);
            }
        }
    }

    private long existeProdutoAuditado(AuditagemEstoque auditagemEstoque) {
        long id = NAO_EXISTE_AUDITAGEM;
        String sql = "SELECT id FROM AuditagemEstoque WHERE idProduto = ? AND date([data]) = date('now','localtime') AND confirmado = 'N'";
        try (Cursor cursor = SimpleDbHelper.INSTANCE
                .open(mContext)
                .rawQuery(
                        sql,
                        new String[]{String.valueOf(auditagemEstoque.getIdProduto())}
                )) {

            if (cursor.moveToNext()) {
                id = cursor.getInt(0);
            }
        }

        return id;
    }

    public void addAuditagemEstoque(AuditagemEstoque auditagemEstoque, Context context) {
        ContentValues values = new ContentValues();
        values.put("idProduto", auditagemEstoque.getIdProduto());
        values.put("qtdeInformada", auditagemEstoque.getQtdeInformada());
        values.put("qtdeReal", auditagemEstoque.getQtdeReal());
        long tempID = existeProdutoAuditado(auditagemEstoque);
        if (tempID != NAO_EXISTE_AUDITAGEM) {
            values.put("id", tempID);
        }
        long id = SimpleDbHelper.INSTANCE.open(mContext).replace(mTabelaAuditagem, null, values);

        if (auditagemEstoque.getCodigosList() != null && auditagemEstoque.getCodigosList().size() > 0) {
            for (CodBarra codBarra : auditagemEstoque.getCodigosList()) {
                values = new ContentValues();
                values.put("idAuditagem", id);
                values.put("idProduto", auditagemEstoque.getIdProduto());
                values.put("codigoBarra", codBarra.getCodBarraInicial());
                values.put("codigoBarraFinal", codBarra.getCodBarraFinal());
                values.put("quantidade", codBarra.retornaQuantidade(UsoCodBarra.GERAL, auditagemEstoque.getIdProduto(), context));
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaAuditagemCodBarra, null, values);
            }
        }
    }

    public void setAuditagemEstoqueIdentificadorVenda(AuditagemEstoque auditagem, String identificador) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("idetificadorVenda", identificador);
        SimpleDbHelper.INSTANCE
                .open(mContext)
                .update(
                        mTabelaAuditagem,
                        contentValues,
                        "[id]=?",
                        new String[]{String.valueOf(auditagem.getId())}
                );
    }

    public void confirmaAuditagem(AuditagemEstoque auditagemEstoque) {
        alterarStatusAuditagem(auditagemEstoque, EnumStatusAuditagem.CONCLUIDA.getValue());
    }

    public void confirmaAuditagemProcessando(AuditagemEstoque auditagemEstoque) {
        alterarStatusAuditagem(auditagemEstoque, EnumStatusAuditagem.PROCESSANDO.getValue());
    }

    private void alterarStatusAuditagem(AuditagemEstoque auditagemEstoque, String status) {
        ContentValues values = new ContentValues();
        values.put("confirmado", status);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAuditagem, values, "[id]=?", new String[]{String.valueOf(auditagemEstoque.getId())});
    }

    public void setSyncAuditagemEstoque(int pId) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAuditagem, values, "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void setSyncAuditagemEstoqueItem(int pId) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAuditagemCodBarra, values, "[id]=?", new String[]{String.valueOf(pId)});
    }

    public boolean isProdutoSendoVendido(String pCodigo) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t2.[id]");
        sb.appendLine("FROM [Visita] t0");
        sb.appendLine("JOIN [Venda] t1 ON (t0.[id] = t1.[idVisita])");
        sb.appendLine("JOIN [ItemVenda] t2 ON (t2.[idVenda] = t1.[id])");
        sb.appendLine("WHERE t0.[dataFim] IS NULL");
        sb.appendLine("AND t2.[idProduto] = ?");
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pCodigo})) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public ArrayList<AuditagemEstoque> getAuditagemEstoquePendentesSync() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",t0.idProduto");
        sb.appendLine(",t0.qtdeInformada");
        sb.appendLine(",t0.qtdeReal");
        sb.appendLine(",t0.data");
        sb.appendLine(",IFNULL(t1.codigoBarra,'')");
        sb.appendLine(",IFNULL(t1.codigoBarraFinal,'')");
        sb.appendLine(",IFNULL(t1.id,0)");
        sb.appendLine("FROM [AuditagemEstoque] t0");
        sb.appendLine("LEFT OUTER JOIN [AuditagemEstoqueCodigoBarra] t1 ON (t1.idAuditagem = t0.id AND t1.sync = 0)");
        sb.appendLine("WHERE ((t0.sync = 0) OR (t0.sync = 1 AND t1.sync = 0))");
        sb.appendLine("AND t0.confirmado = 'S'");
        ArrayList<AuditagemEstoque> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null)) {
            AuditagemEstoque auditagemEstoque;
            if (cursor.moveToFirst()) {
                do {
                    auditagemEstoque = new AuditagemEstoque();
                    auditagemEstoque.setId(cursor.getInt(0));
                    auditagemEstoque.setIdProduto(cursor.getString(1));
                    auditagemEstoque.setQtdeInformada(cursor.getInt(2));
                    auditagemEstoque.setQtdeReal(cursor.getInt(3));
                    auditagemEstoque.setData(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
                    auditagemEstoque.setCodigoBarra(cursor.getString(5));
                    auditagemEstoque.setCodigoBarraFinal(cursor.getString(6));
                    auditagemEstoque.setIdCodigoBarra(cursor.getInt(7));
                    lista.add(auditagemEstoque);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public ArrayList<CodBarra> getCodBarras(String pCodigoItem, int pGrupoProduto) {
        ArrayList<CodBarra> listBarra = new ArrayList<>();
        String selectQuery = "SELECT id as idPistolagem, codigoBarra, IFNULL(codigoBarraFinal,'-1') FROM [AuditagemEstoqueCodigoBarra] WHERE idAuditagem = ?";
        try
                (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, new String[]{pCodigoItem})) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        CodBarra codBarra = new CodBarra();
                        codBarra.setIdPistolagem(cursor.getInt(0));
                        codBarra.setCodBarraInicial(cursor.getString(1));
                        if (cursor.getString(2).equalsIgnoreCase("-1") || Util_IO.isNullOrEmpty(cursor.getString(2)))
                            codBarra.setIndividual(true);
                        else {
                            codBarra.setIndividual(false);
                            codBarra.setCodBarraFinal(cursor.getString(2));
                        }
                        codBarra.setGrupoProduto(pGrupoProduto);
                        listBarra.add(codBarra);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listBarra;
    }

    private void forcarValorBobinaVendedor(AuditagemEstoque auditagemEstoque) {
        String bobinaId = "04075";
        double valorCobrancaBobina = 1.03d;
        if (bobinaId.equalsIgnoreCase(auditagemEstoque.getIdProduto())) {
            auditagemEstoque.setValorUnitario(valorCobrancaBobina);
        }
    }

    public ArrayList<AuditagemEstoque> getAuditagemEstoqueHoje() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.[id]");
        sb.appendLine(",t0.[idProduto]");
        sb.appendLine(",t0.[qtdeInformada]");
        sb.appendLine(",t0.[data]");
        sb.appendLine(",t1.[nome]");
        sb.appendLine(",t1.[estoqueAtual]");
        sb.appendLine(",t1.[precovenda]");
        sb.appendLine(",IFNULL(t1.grupo,0)");
        sb.appendLine("FROM [AuditagemEstoque] t0");
        sb.appendLine("JOIN [Produto] t1 ON (t0.[idProduto] = t1.[id])");
        sb.appendLine("WHERE date(t0.[data]) = date('now','localtime')");
        sb.appendLine("AND confirmado = 'N'");

        ArrayList<AuditagemEstoque> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            AuditagemEstoque auditagemEstoque;
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    auditagemEstoque = new AuditagemEstoque();
                    auditagemEstoque.setId(cursor.getInt(0));
                    auditagemEstoque.setIdProduto(cursor.getString(1));
                    auditagemEstoque.setQtdeInformada(cursor.getInt(2));
                    auditagemEstoque.setData(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                    auditagemEstoque.setNomeProduto(cursor.getString(4));
                    auditagemEstoque.setQtdeReal(cursor.getInt(5));
                    auditagemEstoque.setValorUnitario(cursor.getDouble(6));
                    ArrayList<CodBarra> listBarra = getCodBarras(String.valueOf(cursor.getInt(0)), cursor.getInt(7));
                    auditagemEstoque.setCodigosList(listBarra);

                    forcarValorBobinaVendedor(auditagemEstoque);

                    lista.add(auditagemEstoque);
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

    public ArrayList<AuditagemEstoque> getAuditagemEstoqueHojeProcessando() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.[id]");
        sb.appendLine(",t0.[idProduto]");
        sb.appendLine(",t0.[qtdeInformada]");
        sb.appendLine(",t0.[data]");
        sb.appendLine(",t1.[nome]");
        sb.appendLine(",t1.[estoqueAtual]");
        sb.appendLine(",t1.[precovenda]");
        sb.appendLine(",IFNULL(t1.grupo,0)");
        sb.appendLine("FROM [AuditagemEstoque] t0");
        sb.appendLine("JOIN [Produto] t1 ON (t0.[idProduto] = t1.[id])");
        sb.appendLine("WHERE date(t0.[data]) = date('now','localtime')");
        sb.appendLine("AND confirmado = 'P'");

        ArrayList<AuditagemEstoque> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            AuditagemEstoque auditagemEstoque;
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    auditagemEstoque = new AuditagemEstoque();
                    auditagemEstoque.setId(cursor.getInt(0));
                    auditagemEstoque.setIdProduto(cursor.getString(1));
                    auditagemEstoque.setQtdeInformada(cursor.getInt(2));
                    auditagemEstoque.setData(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                    auditagemEstoque.setNomeProduto(cursor.getString(4));
                    auditagemEstoque.setQtdeReal(cursor.getInt(5));
                    auditagemEstoque.setValorUnitario(cursor.getDouble(6));
                    ArrayList<CodBarra> listBarra = getCodBarras(String.valueOf(cursor.getInt(0)), cursor.getInt(7));
                    auditagemEstoque.setCodigosList(listBarra);

                    forcarValorBobinaVendedor(auditagemEstoque);

                    lista.add(auditagemEstoque);
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

    public ArrayList<AuditagemEstoque> getAuditagem() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.[id]");
        sb.appendLine(",t0.[idProduto]");
        sb.appendLine(",t0.[qtdeInformada]");
        sb.appendLine(",t0.[data]");
        sb.appendLine(",t1.[nome]");
        sb.appendLine(",IFNULL(t1.grupo,0)");
        sb.appendLine("FROM [AuditagemEstoque] t0");
        sb.appendLine("JOIN [Produto] t1 ON (t0.[idProduto] = t1.[id])");
        sb.appendLine("WHERE date(t0.[data]) = date('now','localtime')");
        sb.appendLine("AND confirmado = 'N'");

        Cursor cursor = null;
        ArrayList<AuditagemEstoque> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            AuditagemEstoque auditagemEstoque;
            if (cursor.moveToFirst()) {
                do {
                    auditagemEstoque = new AuditagemEstoque();
                    auditagemEstoque.setId(cursor.getInt(0));
                    auditagemEstoque.setIdProduto(cursor.getString(1));
                    auditagemEstoque.setQtdeInformada(cursor.getInt(2));
                    auditagemEstoque.setData(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                    auditagemEstoque.setNomeProduto(cursor.getString(4));
                    ArrayList<CodBarra> listBarra = getCodBarras(String.valueOf(cursor.getInt(0)), cursor.getInt(5));
                    auditagemEstoque.setCodigosList(listBarra);
                    lista.add(auditagemEstoque);
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

    public void deleteAuditagens() {
        try {
            Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery("SELECT id FROM [AuditagemEstoque] WHERE date([data]) < date('now','-20') AND [sync] = 1", null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagemCodBarra, "[idAuditagem]=? AND [sync]=1", new String[]{String.valueOf(cursor.getInt(0))});
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagem, "[id]=?", new String[]{String.valueOf(cursor.getInt(0))});
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();

            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagem, "[finalizado]=1 AND date([data]) < date('now','-5')", null);

            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("DELETE FROM [AuditagemEstoqueCodigoBarra] WHERE id IN (");
            sb.appendLine("SELECT t0.id FROM [AuditagemEstoqueCodigoBarra] t0");
            sb.appendLine("LEFT OUTER JOIN [AuditagemEstoque] t1 ON (t0.idAuditagem = t1.id)");
            sb.appendLine("WHERE t1.idProduto IS NULL)");

            SimpleDbHelper.INSTANCE.open(mContext).execSQL(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAuditagensNaoConfirmados() {
        SimpleDbHelper.INSTANCE.open(mContext).execSQL("DELETE FROM [AuditagemEstoqueCodigoBarra] WHERE [idAuditagem] IN (SELECT id FROM [AuditagemEstoque] WHERE confirmado = 'N')");
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAuditagem, "[confirmado]=?", new String[]{"N"});
    }

    public void removerQuantidadeAuditagem(String auditagemId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("qtdeinformada", 0);
        SimpleDbHelper.INSTANCE
                .open(mContext)
                .update(
                        mTabelaAuditagem,
                        contentValues,
                        "id = ?",
                        new String[]{auditagemId}
                );
    }

    public void atualizarQuantidadeAuditagem(int auditagemId, int quantidade) {
        try {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("Update AuditagemEstoque set qtdeinformada = qtdeinformada + " + quantidade);
            sb.appendLine("Where id = " + auditagemId);
            SimpleDbHelper.INSTANCE.open(mContext).execSQL(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removerAuditagemPistolagem(int idPistolagem) {
        try {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("Delete from AuditagemEstoqueCodigoBarra");
            sb.appendLine("Where id = " + idPistolagem);
            SimpleDbHelper.INSTANCE.open(mContext).execSQL(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removerIccidAuditagem(String auditagemId) {
        SimpleDbHelper.INSTANCE
                .open(mContext)
                .delete(
                        mTabelaAuditagemCodBarra,
                        "[idAuditagem] = ?",
                        new String[]{auditagemId}
                );
    }

    public ArrayList<Produto> getProdutosEstoque(String pNome) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        // Carrega dados de estoque da Tabela Produtos
        sb.appendLine("select id, nome, sum(estoqueatual) as estoqueatual, sum(QtdConsignado) as QtdConsignado");
        sb.appendLine("from (");
        sb.appendLine("Select t0.id, t0.nome, t0.estoqueatual, 0 as QtdConsignado");
        sb.appendLine("from Produto t0");
        sb.appendLine("Where t0.ativo = 'S'");

        if (!Util_IO.isNullOrEmpty(pNome))
            sb.appendLine("AND t0.[nome] LIKE '%" + pNome + "%'");

        if (!Util_IO.isNullOrEmpty(pNome))
            sb.appendLine("AND t0.estoqueAtual >= 0");
        else
            sb.appendLine("AND t0.estoqueAtual > 0");
        sb.appendLine("group by t0.id, t0.nome, t0.estoqueatual");

        sb.appendLine("union all");

        // Carrega dados de quantidades consignadas a clientes
        sb.appendLine("Select t3.id, t3.nome, 0 as estoqueatual, coalesce(sum(t2.qtd),0) as QtdConsignado");
        sb.appendLine("from Consignado t1");
        sb.appendLine("left join ConsignadoItem t2 on t2.IdConsignado = t1.Id");
        sb.appendLine("left join Produto t3 on t3.id = t2.IdProduto");
        sb.appendLine("where t1.status = 0 and t1.TipoConsignado = 'CON'");

        if (!Util_IO.isNullOrEmpty(pNome))
            sb.appendLine("AND t0.[nome] LIKE '%" + pNome + "%'");

        sb.appendLine("group by t3.id, t3.nome");
        sb.appendLine(")");
        sb.appendLine("group by id, nome");
        sb.appendLine("order by 2,3");

        ArrayList<Produto> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null)) {
            Produto produto;
            if (cursor.moveToFirst()) {
                do {
                    produto = new Produto();
                    produto.setId(cursor.getString(0));
                    produto.setNome(cursor.getString(1));
                    produto.setQtde(cursor.getInt(2));
                    produto.setQuantidadeConsignada(cursor.getInt(3));
                    lista.add(produto);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public boolean auditagensRealizadasHoje() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine("FROM [AuditagemEstoque]");
        sb.appendLine("WHERE date([data]) = date('now','localtime')");
        sb.appendLine("AND confirmado = 'S'");
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null)) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean verificaProdutoAuditadoHoje(String pIdProduto) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine("FROM [AuditagemEstoque]");
        sb.appendLine("WHERE date([data]) = date('now','localtime')");
        sb.appendLine("AND confirmado = 'S'");
        sb.appendLine("AND idProduto = ?");
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pIdProduto})) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void atualizaEstoque(String pIdProduto, boolean pRemove, int pQtd) {
        String query = "UPDATE [Produto] SET [estoqueAtual] = [estoqueAtual] " + ((pRemove) ? "- " : "+ ") + pQtd + " WHERE [id] = '" + pIdProduto + "'";
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(query);
    }

    public boolean verificaEstoque(String pIdProduto, int pQtd) {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("SELECT IFNULL([estoqueAtual],0)");
        sbSQL.appendLine("FROM [Produto]");
        sbSQL.appendLine("WHERE [id] = ?");

        Cursor cursor = null;
        try {
            int estoqueAtual = 0, estoqueFinal;
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), new String[]{pIdProduto});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                estoqueAtual = cursor.getInt(0);
            }

            estoqueFinal = estoqueAtual - pQtd;
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

    public void atualizaEstoqueVenda(ArrayList<ItemVenda> pItensVenda) {
        if (pItensVenda != null && !pItensVenda.isEmpty()) {
            DBIccid dbIccid = new DBIccid(mContext);

            for (ItemVenda itemVenda : pItensVenda) {
                if (itemVenda.isCombo()) {
                    for (CodBarra codBarra : itemVenda.getCodigosList()) {
                        int quantidade = Integer.parseInt(codBarra.retornaQuantidade(UsoCodBarra.GERAL));
                        Iccid iccid = dbIccid.getByCodigo(codBarra.getCodBarraInicial());
                        atualizaEstoque(iccid.getItemCode(), false, quantidade);
                    }
                }

                atualizaEstoque(itemVenda.getIdProduto(), false, itemVenda.getQtde());
            }
        }
    }

    public void deletarPistolagensComboNaoFinalizadas(Venda pVenda) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCombo, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(pVenda.getId())});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagem, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(pVenda.getId())});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagemCombo, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(pVenda.getId())});
    }

    public void deletarPistolagensVenda(Venda pVenda) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCombo, "[idVenda]=?", new String[]{String.valueOf(pVenda.getId())});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagem, "[idVenda]=?", new String[]{String.valueOf(pVenda.getId())});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagemCombo, "[idVenda]=?", new String[]{String.valueOf(pVenda.getId())});
    }

    public void deletarPistolagemByComboId(int pIdCombo) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCombo, "[idCombo]=?", new String[]{String.valueOf(pIdCombo)});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagem, "[idCombo]=?", new String[]{String.valueOf(pIdCombo)});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagemCombo, "[id]=?", new String[]{String.valueOf(pIdCombo)});
    }

    public void deletarPistolagensFinalizadas() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCombo, "[finalizado]=1 AND date([data]) <= date('now','localtime')", null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagem, "[finalizado]=1 AND date([data]) <= date('now','localtime')", null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagemCombo, "[finalizado]=1 AND date([data]) <= date('now','localtime')", null);
    }

    public void deletarPitolagemUnitario(int pIdPistolagem) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPistolagem, "[id]=?", new String[]{String.valueOf(pIdPistolagem)});
    }

    public void updateQtdPistolagem(int pIdCombo, int pQuantidade) {
        ContentValues values = new ContentValues();
        values.put("quantidade", pQuantidade);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaPistolagemCombo, values, "[id]=?", new String[]{String.valueOf(pIdCombo)});
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
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
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

    public ArrayList<ItemVendaCombo> getPistolagensComboNaoFinalizada(Venda pVenda) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[idvenda] = ?");
        sb.appendLine("AND t0.[finalizado] = 0");
        return getPistolagens(sb.toString(), new String[]{String.valueOf(pVenda.getId())});
    }

    public ItemVendaCombo getPistolagem(Venda pVenda, String pIdProduto, String pIdPreco) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[finalizado] = 0");
        sb.appendLine("AND t0.[idvenda] = ?");
        sb.appendLine("AND t0.[idProduto] = ?");
        sb.appendLine("AND IFNULL(t0.[idPreco],0) = IFNULL(CAST(? AS INTEGER),0)");

        return Utilidades.firstOrDefault(getPistolagens(sb.toString(), new String[]{String.valueOf(pVenda.getId()), pIdProduto, pIdPreco}));
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
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pIdCombo)});
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

    private ArrayList<CodBarra> getPistolagemTempVenda(int pIdCombo, int pIdVenda, String pIdCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [idCombo] = ?");
        sb.appendLine("AND [idVenda] = ?");
        sb.appendLine("AND [idCliente] = ?");
        sb.appendLine("AND [finalizado] = 0");
        return getPistolagem(sb.toString(), new String[]{String.valueOf(pIdCombo), String.valueOf(pIdVenda), pIdCliente});
    }

    public void incluirComboPistolagem(ItemVendaCombo pItemVendaCombo, Venda pVenda) {
        ContentValues values = new ContentValues();
        values.put("qtdCombo", pItemVendaCombo.getQtdCombo());
        values.put("idProduto", pItemVendaCombo.getIdProduto());
        values.put("isCombo", Util_IO.booleanToNumber(pItemVendaCombo.isCombo()));
        values.put("idPreco", pItemVendaCombo.getIdPreco());
        values.put("valor", pItemVendaCombo.getValorUN());
        values.put("quantidade", pItemVendaCombo.getQtde());
        values.put("finalizado", 0);
        values.put("idvenda", pVenda.getId());
        long codigo = pItemVendaCombo.getId();
        if (codigo > 0)
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaPistolagemCombo, values, "[id]=?", new String[]{String.valueOf(codigo)});
        else
            codigo = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaPistolagemCombo, null, values);

        if (pItemVendaCombo.getCodigosList() != null && pItemVendaCombo.getCodigosList().size() > 0) {
            for (CodBarra codBarra : pItemVendaCombo.getCodigosList()) {
                values = new ContentValues();
                values.put("idProduto", codBarra.getIdProduto());
                values.put("idCliente", pVenda.getIdCliente());
                values.put("codigoBarra", codBarra.getCodBarraInicial());
                if (!Util_IO.isNullOrEmpty(codBarra.getCodBarraFinal()))
                    values.put("codigoBarraFinal", codBarra.getCodBarraFinal());
                values.put("quantidade", codBarra.retornaQuantidade(UsoCodBarra.GERAL));
                values.put("finalizado", 0);
                values.put("idVenda", pVenda.getId());
                values.put("idCombo", codigo);
                if (codBarra.getIdPistolagem() > 0)
                    SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaPistolagem, values, "[id]=?", new String[]{String.valueOf(codBarra.getIdPistolagem())});
                else
                    SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaPistolagem, null, values);
            }
        }

        if (pItemVendaCombo.getListItens() != null && pItemVendaCombo.getListItens().size() > 0) {
            for (ComboVenda comboVenda : pItemVendaCombo.getListItens()) {
                values = new ContentValues();
                values.put("idCombo", codigo);
                values.put("idProduto", comboVenda.getIdProduto());
                values.put("quantidade", comboVenda.getQuantidade());
                values.put("finalizado", 0);
                values.put("idVenda", pVenda.getId());
                if (comboVenda.getId() > 0)
                    SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCombo, values, "[id]=?", new String[]{String.valueOf(comboVenda.getId())});
                else
                    SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaCombo, null, values);
            }
        }
    }

    public void confirmarPistolagens(Venda pVenda) {
        ContentValues values = new ContentValues();
        values.put("finalizado", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCombo, values, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(pVenda.getId())});
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaPistolagem, values, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(pVenda.getId())});
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaPistolagemCombo, values, "[idVenda]=? AND [finalizado]=0", new String[]{String.valueOf(pVenda.getId())});
    }

    public ArrayList<Produto> obterProdutosSolicitacaoTroca() {
        String consulta = "SELECT id, " +
                "codSgv, " +
                "nome, " +
                "valor, " +
                "estoqueMax, " +
                "mediaDiariaVnd, " +
                "diasEstoque, " +
                "estoqueSugerido, " +
                "estoqueAtual, " +
                "ativo, " +
                "precovenda, " +
                //"precomedio, " +
                "bipagem, " +
                "bipagemAuditoria, " +
                "iniciaCodBarra, " +
                "qtdCodBarra, " +
                "bipagemCliente, " +
                "permiteValorZero, " +
                "qtdCombo, " +
                "operadora, " +
                "grupo, " +
                "permiteVendaPrazo " +
                "FROM [Produto] WHERE grupo IN(100, 102, 103, 229, 235) AND Ativo = 'S'";
        ArrayList<Produto> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(consulta, new String[]{})) {
            Produto produto;
            if (cursor.moveToFirst()) {
                do {
                    produto = new Produto();
                    produto.setId(cursor.getString(0));
                    produto.setCodSgv(cursor.getString(1));
                    produto.setNome(cursor.getString(2));
                    produto.setValor(cursor.getDouble(3));
                    produto.setEstoqueMax(cursor.getInt(4));
                    produto.setMediaDiariaVnd(cursor.getDouble(5));
                    produto.setDiasEstoque(cursor.getInt(6));
                    produto.setEstoqueSugerido(cursor.getInt(7));
                    produto.setEstoqueAtual(cursor.getInt(8));
                    produto.setAtivo(cursor.getString(9));
                    produto.setPrecovenda(cursor.getDouble(10));
                    produto.setBipagem(cursor.getString(11));
                    produto.setBipagemAuditoria(cursor.getString(12));
                    produto.setIniciaCodBarra(cursor.getString(13));
                    produto.setQtdCodBarra(cursor.getInt(14));
                    produto.setBipagemCliente(cursor.getString(15));
                    produto.setPermiteVendaSemValor(cursor.getString(16));
                    produto.setQtdCombo(cursor.getInt(17));
                    produto.setOperadora(cursor.getInt(18));
                    produto.setGrupo(cursor.getInt(19));
                    produto.setPermiteVendaPrazo(cursor.getString(20));
                    lista.add(produto);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}
