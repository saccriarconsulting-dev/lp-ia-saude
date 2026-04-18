package com.axys.redeflexmobile.shared.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBSugestaoVenda;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.models.SugestaoVenda;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import timber.log.Timber;

public abstract class Tabela {
    private static final int NAO_SINCRONIZADO = 0;
    private static final int NAO = 0;

    private static String getString(Cursor cursor, String coluna) {
        return cursor.getString(cursor.getColumnIndex(coluna));
    }

    private static double getDouble(Cursor cursor, String coluna) {
        return cursor.getDouble(cursor.getColumnIndex(coluna));
    }

    private static int getInt(Cursor cursor, String coluna) {
        return cursor.getInt(cursor.getColumnIndex(coluna));
    }

    private static String join(String tabela, String campo) {
        return String.format("%s.%s", tabela, campo);
    }

    private static long getLong(Cursor cursor, String coluna) {
        return cursor.getLong(cursor.getColumnIndex(coluna));
    }

    public static class TabelaVenda extends Tabela {
        static final String TABELA = "Venda";
        static final String COLUNA_ID = "id";
        static final String COLUNA_ID_SERVER = "idServer";
        static final String COLUNA_ID_VISITA = "idVisita";
        static final String COLUNA_ID_FORMA_PAGAMENTO = "idFormaPagamento";
        static final String COLUNA_ID_CLIENTE_SGV = "idClienteSGV";
        static final String COLUNA_DATA = "data";
        static final String COLUNA_SYNC = "sync";
        static final String COLUNA_ID_CLIENTE = "idCliente";
        static final String COLUNA_DATA_VENCIMENTO = "dataVencimento";
        static final String COLUNA_VALOR_TOTAL = "valorTotal";
        static final String COLUNA_VENDA_BOBINA = "vendaBobina";
        static final String COLUNA_STATUS = "status";

        static final String Coluna_ChaveCobranca = "ChaveCobranca";
        static final String Coluna_QrCodeLink = "QrCodeLink";

        static String queryPadrao() {
            return String.format(
                    "SELECT " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "IFNULL(%s, 0) AS %s, " +
                            "IFNULL(%s, 0) AS %s, " +
                            "CASE IFNULL(%s, '') WHEN '' THEN %s ELSE %s END AS %s, " +
                            "IFNULL(%s, 0) AS %s, " +
                            "IFNULL(%s, 0) AS %s, " +
                            "%s, " +
                            "%s  " +
                            "FROM %s " +
                            "WHERE 1 =1 ",
                    COLUNA_ID,
                    COLUNA_ID_VISITA,
                    COLUNA_ID_CLIENTE,
                    COLUNA_DATA,
                    COLUNA_ID_FORMA_PAGAMENTO,
                    COLUNA_ID_FORMA_PAGAMENTO,
                    COLUNA_SYNC,
                    COLUNA_SYNC,
                    COLUNA_DATA_VENCIMENTO,
                    COLUNA_DATA_VENCIMENTO,
                    COLUNA_DATA,
                    COLUNA_DATA_VENCIMENTO,
                    COLUNA_VENDA_BOBINA,
                    COLUNA_VENDA_BOBINA,
                    COLUNA_VALOR_TOTAL,
                    COLUNA_VALOR_TOTAL,
                    Coluna_ChaveCobranca,
                    Coluna_QrCodeLink,
                    TABELA
            );
        }

        static Venda obterVendaPorIdVisita(Context context, int idVisita) {
            String sql = String.format(
                    "%s AND %s = ?",
                    queryPadrao(),
                    COLUNA_ID_VISITA
            );

            Venda venda = null;
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{String.valueOf(idVisita)};
            try (Cursor cursor = db.rawQuery(sql, filtros)) {
                while (cursor.moveToNext()) {
                    venda = converter(cursor);
                    venda.setValorTotal(obterTotalVenda(context, venda.getId()));
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return venda;
        }

        static Venda obterVendaPorId(Context context, int id) {
            String sql = String.format(
                    "%s AND %s = ?",
                    queryPadrao(),
                    COLUNA_ID
            );

            Venda venda = null;
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{String.valueOf(id)};
            try (Cursor cursor = db.rawQuery(sql, filtros)) {
                while (cursor.moveToNext()) {
                    venda = converter(cursor);
                    venda.setValorTotal(obterTotalVenda(context, id));
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return venda;
        }

        static double obterTotalVenda(Context context, int idVenda) {
            String sql = String.format(
                    "SELECT SUM(%s * %s) AS %s FROM %s WHERE %s = ?",
                    TabelaItemVenda.COLUNA_QUANTIDADE,
                    TabelaItemVenda.COLUNA_QUANTIDADE,
                    COLUNA_VALOR_TOTAL,
                    TabelaItemVenda.TABELA,
                    TabelaItemVenda.COLUNA_ID_VENDA
            );

            double valorTotal = 0.0;
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{String.valueOf(idVenda)};
            try (Cursor cursor = db.rawQuery(sql, filtros)) {
                while (cursor.moveToNext()) {
                    valorTotal += getDouble(cursor, COLUNA_VALOR_TOTAL);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return valorTotal;
        }

        static long criarVendaSimples(Context context, int idVisita, String idCliente) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUNA_ID_VISITA, idVisita);
            contentValues.put(COLUNA_ID_CLIENTE, idCliente);
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            return db.insert(TABELA, null, contentValues);
        }

        static void cancelarVenda(Context context, Venda venda) {
            if (venda == null) {
                return;
            }

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String sqlVenda = String.format("%s = ?", TabelaVenda.COLUNA_ID);
            String sqlItem = String.format("%s = ?", TabelaItemVenda.COLUNA_ID_VENDA);
            String[] filtros = new String[]{String.valueOf(venda.getId())};

            db.delete(TabelaItemVendaCombo.TABELA, sqlItem, filtros);
            db.delete(TabelaItemVendaCodigoBarra.TABELA, sqlItem, filtros);
            db.delete(TabelaItemVenda.TABELA, sqlItem, filtros);
            db.delete(TabelaVenda.TABELA, sqlVenda, filtros);
        }

        private static Venda converter(Cursor cursor) {
            Venda venda = new Venda();
            venda.setId(getInt(cursor, COLUNA_ID));
            venda.setIdVisita(getInt(cursor, COLUNA_ID_VISITA));
            venda.setIdFormaPagamento(getInt(cursor, COLUNA_ID_FORMA_PAGAMENTO));
            venda.setData(Util_IO.stringToDate(getString(cursor, COLUNA_DATA), Config.FormatDateTimeStringBanco));
            venda.setIdCliente(getString(cursor, COLUNA_ID_CLIENTE));
            venda.setDataCobranca(Util_IO.stringToDate(getString(cursor, COLUNA_DATA_VENCIMENTO), Config.FormatDateStringBanco));
            venda.setChaveCobranca(getString(cursor, Coluna_ChaveCobranca));
            venda.setQrCodeLink(getString(cursor, Coluna_QrCodeLink));
            return venda;
        }

        private static ContentValues converter(Venda venda) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID, venda.getId());
            values.put(COLUNA_ID_SERVER, venda.getIdServer());
            values.put(COLUNA_ID_VISITA, venda.getIdVisita());
            values.put(COLUNA_ID_FORMA_PAGAMENTO, venda.getIdFormaPagamento());
            values.put(COLUNA_ID_CLIENTE_SGV, venda.getIdClienteSGV());
            values.put(COLUNA_DATA, Util_IO.dateTimeToString(venda.getData(), Config.FormatDateTimeStringBanco));
            values.put(COLUNA_SYNC, NAO_SINCRONIZADO);
            values.put(COLUNA_ID_CLIENTE, venda.getIdCliente());
            values.put(COLUNA_DATA_VENCIMENTO, Util_IO.dateTimeToString(venda.getDataCobranca(), Config.FormatDateTimeStringBanco));
            values.put(COLUNA_VALOR_TOTAL, venda.getValorTotal());

            return values;
        }
    }

    public static class TabelaItemVenda extends Tabela {
        static final String TABELA = "ItemVenda";
        static final String COLUNA_ID = "id";
        static final String COLUNA_ID_SERVER = "idServer";
        static final String COLUNA_ID_PRODUTO = "idProduto";
        static final String COLUNA_ID_VENDA = "idVenda";
        static final String COLUNA_DATA_VENDA = "dataVenda";
        static final String COLUNA_QUANTIDADE = "qtde";
        static final String COLUNA_VALOR_UNITARIO = "valorUN";
        static final String COLUNA_SYNC = "sync";
        static final String COLUNA_BOLETO_SYNC = "boletoSync";
        static final String COLUNA_COMBO = "isCombo";
        static final String COLUNA_ID_PRECO = "idPreco";
        static final String COLUNA_CANCELADO = "cancelado";
        static final String COLUNA_QTDE_COMBO = "qtdeCombo";

        static String queryPadrao() {
            return String.format(
                    "SELECT %s, " +
                            "%s, " +
                            "IFNULL(%s, '') AS %s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "IFNULL(%s, 0) AS %s, " +
                            "IFNULL(%s, 0) AS %s, " +
                            "IFNULL(%s, 0) AS %s, " +
                            "IFNULL(%s, 0) AS %s, " +
                            "IFNULL(%s, '') AS %s, " +
                            "IFNULL(%s, 0) AS %s " +
                            "FROM %s " +
                            "LEFT OUTER JOIN %s ON %s = %s " +
                            "WHERE 1 = 1",
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_ID),
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_ID_PRODUTO),
                    join(TabelaProduto.TABELA, TabelaProduto.COLUNA_NOME),
                    TabelaProduto.COLUNA_NOME,
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_ID_VENDA),
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_DATA_VENDA),
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_QUANTIDADE),
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_VALOR_UNITARIO),
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_SYNC),
                    TabelaItemVenda.COLUNA_SYNC,
                    join(TabelaProduto.TABELA, TabelaProduto.COLNA_GRUPO),
                    TabelaProduto.COLNA_GRUPO,
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_COMBO),
                    TabelaItemVenda.COLUNA_COMBO,
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_ID_PRECO),
                    TabelaItemVenda.COLUNA_ID_PRECO,
                    join(TabelaProduto.TABELA, TabelaProduto.COLUNA_BIPAGEM),
                    TabelaProduto.COLUNA_BIPAGEM,
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_QTDE_COMBO),
                    TabelaItemVenda.COLUNA_QTDE_COMBO,
                    TabelaItemVenda.TABELA,
                    TabelaProduto.TABELA,
                    join(TabelaProduto.TABELA, TabelaProduto.COLUNA_ID),
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_ID_PRODUTO)
            );
        }

        static List<ItemVenda> obterItensVendaPorIdVenda(Context context, int idVenda) {
            String sql = String.format(
                    "%s AND %s = ?",
                    queryPadrao(),
                    COLUNA_ID_VENDA
            );

            List<ItemVenda> lista = new ArrayList<>();
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{String.valueOf(idVenda)};
            try (Cursor cursor = db.rawQuery(sql, filtros)) {
                while (cursor.moveToNext()) {
                    ItemVenda itemVenda = converter(context, cursor);
                    lista.add(itemVenda);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return lista;
        }

        static ItemVenda obterItemVendaPorId(Context context, int idVendaItem) {
            String sql = String.format(
                    "%s AND %s = ?",
                    queryPadrao(),
                    join(TABELA, COLUNA_ID)
            );
            ItemVenda itemVenda = null;
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{String.valueOf(idVendaItem)};
            try (Cursor cursor = db.rawQuery(sql, filtros)) {
                while (cursor.moveToNext()) {
                    itemVenda = converter(context, cursor);

                    // Carrega Situacao Sugestao Venda
                    DBSugestaoVenda dbSugestaoVenda = new DBSugestaoVenda(context);
                    itemVenda.setSituacaosugestaovenda(dbSugestaoVenda.getSituacaoSugestaoRuptura(itemVenda.getIdVenda(), itemVenda.getIdProduto()));
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return itemVenda;
        }

        static List<ItemVendaCombo> obterItensComboVendaPorIdVenda(Context context, int idVenda) {
            String sql = String.format(
                    "%s AND %s = ?",
                    queryPadrao(),
                    COLUNA_ID_VENDA
            );

            List<ItemVendaCombo> lista = new ArrayList<>();
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{String.valueOf(idVenda)};
            try (Cursor cursor = db.rawQuery(sql, filtros)) {
                while (cursor.moveToNext()) {
                    ItemVendaCombo itemVendaCombo = (ItemVendaCombo) converter(context, cursor);
                    int quantidade = 0;
                    for (CodBarra barra : itemVendaCombo.getCodigosList()) {
                        quantidade += barra.somarQuantidade(UsoCodBarra.GERAL);
                    }
                    itemVendaCombo.setQuantidadeSerial(quantidade);
                    calcularQuantidadeItemCombo(context, itemVendaCombo);

                    // Carrega Situacao Sugestao Venda
                    DBSugestaoVenda dbSugestaoVenda = new DBSugestaoVenda(context);
                    itemVendaCombo.setSituacaosugestaovenda(dbSugestaoVenda.getSituacaoSugestaoRuptura(idVenda, itemVendaCombo.getIdProduto()));
                    lista.add(itemVendaCombo);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return lista;
        }

        static long inserirItemVenda(Context context, ItemVenda itemVenda) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_PRODUTO, itemVenda.getIdProduto());
            values.put(COLUNA_ID_VENDA, itemVenda.getIdVenda());
            values.put(COLUNA_DATA_VENDA, Util_IO.dateTimeToString(itemVenda.getDataVenda(), Config.FormatDateTimeStringBanco));
            values.put(COLUNA_QUANTIDADE, itemVenda.getQtde());
            values.put(COLUNA_VALOR_UNITARIO, itemVenda.getValorUN());
            values.put(COLUNA_COMBO, itemVenda.isCombo() || (itemVenda.getCodigosList() != null && !itemVenda.getCodigosList().isEmpty()) ? 1 : 0);
            values.put(COLUNA_ID_PRECO, itemVenda.getIdPreco());
            values.put(COLUNA_CANCELADO, NAO);
            values.put(COLUNA_QTDE_COMBO, ((ItemVendaCombo) itemVenda).getQtdCombo());

            return inserirItemVenda(context, values);
        }

        static void atualizarQuantidadeItemVenda(Context context, long idItemVenda, int quantidade) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_QUANTIDADE, quantidade);

            String sql = String.format("%s = ?", COLUNA_ID);
            String[] filtros = new String[]{String.valueOf(idItemVenda)};

            atualizarItemVenda(context, values, sql, filtros);
        }

        static void removerItemVenda(Context context, int idItemVenda) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String sqlItem = String.format("%s = ?", COLUNA_ID);
            String sqlCodigoBarra = String.format(
                    "%s = ?",
                    TabelaItemVendaCodigoBarra.COLUNA_ID_ITEM_VENDA
            );
            String[] filtro = new String[]{String.valueOf(idItemVenda)};

            db.delete(TABELA, sqlItem, filtro);
            db.delete(TabelaItemVendaCodigoBarra.TABELA, sqlCodigoBarra, filtro);
        }

        private static void atualizarItemVenda(Context context, ContentValues contentValues,
                                               String sql, String[] filtros) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);

            db.update(TABELA, contentValues, sql, filtros);
        }

        private static long inserirItemVenda(Context context, ContentValues values) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);

            return db.insert(TABELA, null, values);
        }

        private static void calcularQuantidadeItemCombo(Context context, ItemVendaCombo itemVendaCombo) {
            if (!itemVendaCombo.isCombo()) {
                return;
            }

            List<EstruturaProd> estrutura = TabelaEstrutura.obterEstruturaPeloItemPai(
                    context, itemVendaCombo.getIdProduto()
            );
            Stream.ofNullable(estrutura)
                    .forEach(estruturaProd -> Stream.ofNullable(itemVendaCombo.getCodigosList())
                            .filter(value -> value.getIdProduto().equalsIgnoreCase(estruturaProd.getItemFilho()))
                            .forEach(value -> {
                                value.setQuantidadeFormacaoCombo(estruturaProd.getQtd());
                                value.setQuantidadeTemporaria(estruturaProd.getQtd() * itemVendaCombo.getQtde());
                            }));

            List<Integer> quantidades = new ArrayList<>();
            Stream.of(itemVendaCombo.getCodigosList())
                    .groupBy(CodBarra::getIdProduto)
                    .forEach(entries -> {
                        int quantidade = 0;
                        for (CodBarra codbarra : entries.getValue()) {
                            quantidade += codbarra.somarQuantidade(UsoCodBarra.GERAL);
                        }
                        if (quantidade > 0) {
                            int quantidadeCalculada = quantidade / entries.getValue().get(0).getQuantidadeFormacaoCombo();
                            quantidades.add(quantidadeCalculada);
                        }
                    });

            int quantidadeCalculada = Stream.of(quantidades)
                    .min((esquerda, direita) -> {
                        if (esquerda < direita) {
                            return -1;
                        } else if (esquerda.equals(direita)) {
                            return 0;
                        }
                        return 1;
                    }).orElse(0);
            if (estrutura.size() > 1 && quantidades.size() <= 1) {
                quantidadeCalculada = 0;
            }

            itemVendaCombo.setQuantidadeSerial(quantidadeCalculada);
        }

        private static ItemVenda converter(Context context, Cursor cursor) {
            ItemVendaCombo itemVenda = new ItemVendaCombo();
            itemVenda.setId(getInt(cursor, COLUNA_ID));
            itemVenda.setIdProduto(getString(cursor, COLUNA_ID_PRODUTO));
            itemVenda.setNomeProduto(getString(cursor, TabelaProduto.COLUNA_NOME));
            itemVenda.setIdVenda(getInt(cursor, COLUNA_ID_VENDA));
            itemVenda.setDataVenda(Util_IO.stringToDate(getString(cursor, COLUNA_DATA_VENDA), Config.FormatDateTimeStringBanco));
            itemVenda.setQtde(getInt(cursor, COLUNA_QUANTIDADE));
            itemVenda.setValorUN(getDouble(cursor, COLUNA_VALOR_UNITARIO));
            List<CodBarra> lista = TabelaItemVendaCodigoBarra.obterCodigoBarras(
                    context,
                    String.valueOf(getInt(cursor, COLUNA_ID)),
                    getInt(cursor, TabelaProduto.COLNA_GRUPO)
            );
            itemVenda.setCodigosList(lista);
            itemVenda.setCombo(Util_IO.numberToBoolean(getInt(cursor, COLUNA_COMBO)));
            itemVenda.setBipagem(getString(cursor, TabelaProduto.COLUNA_BIPAGEM));
            itemVenda.setIdPreco(getInt(cursor, COLUNA_ID_PRECO));
            int quantidade = 0;
            for (CodBarra codBarra : itemVenda.getCodigosList()) {
                quantidade += codBarra.somarQuantidade(UsoCodBarra.GERAL);
            }
            itemVenda.setQuantidadeSerial(quantidade);
            itemVenda.setQtdCombo(getInt(cursor, COLUNA_QTDE_COMBO));

            return itemVenda;
        }
    }

    public static class TabelaItemVendaCodigoBarra extends Tabela {
        static final String TABELA = "ItemVendaCodigoBarra";
        static final String COLUNA_ID = "id";
        static final String COLUNA_ID_SERVER = "idServer";
        static final String COLUNA_ID_VENDA = "idVenda";
        static final String COLUNA_DATA_VENDA = "dataVenda";
        static final String COLUNA_ID_ITEM_VENDA = "idItemVenda";
        static final String COLUNA_ID_PRODUTO = "idProduto";
        static final String COLUNA_CODIGO_BARRA = "codigoBarra";
        static final String COLUNA_CODIGO_BARRA_FINAL = "codigoBarraFinal";
        static final String COLUNA_QUANTIDADE = "quantidade";
        static final String COLUNA_SYNC = "sync";
        static final String COLUNA_ID_PRODUTO_SELECT = "idProdutoSelect";
        static final String COLUNA_CANCELADO = "cancelado";
        private static final String SEM_CODIGO = "-1";

        static List<CodBarra> obterCodigoBarras(Context context, String idVendaItem, int idGrupo) {
            List<CodBarra> lista = new ArrayList<>();
            String sql = String.format(
                    "SELECT " +
                            "%s, " +
                            "IFNULL(%s,'-1') AS %s, " +
                            "%s, " +
                            "%s, " +
                            "%s " +
                            "FROM %s " +
                            "JOIN %s ON %s = %s " +
                            "WHERE %s = ?",
                    join(TabelaItemVendaCodigoBarra.TABELA, COLUNA_CODIGO_BARRA),
                    join(TabelaItemVendaCodigoBarra.TABELA, COLUNA_CODIGO_BARRA_FINAL),
                    COLUNA_CODIGO_BARRA_FINAL,
                    join(TabelaItemVendaCodigoBarra.TABELA, COLUNA_ID),
                    join(TabelaItemVendaCodigoBarra.TABELA, COLUNA_ID_PRODUTO_SELECT),
                    join(TabelaProduto.TABELA, TabelaProduto.COLUNA_NOME),
                    TABELA,
                    TabelaProduto.TABELA,
                    join(TabelaProduto.TABELA, TabelaProduto.COLUNA_ID),
                    join(TabelaItemVendaCodigoBarra.TABELA, COLUNA_ID_PRODUTO_SELECT),
                    join(TabelaItemVendaCodigoBarra.TABELA, COLUNA_ID_ITEM_VENDA)
            );

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{idVendaItem};
            try (Cursor cursor = db.rawQuery(sql, filtros)) {
                while (cursor.moveToNext()) {
                    CodBarra codBarra = converter(cursor, idGrupo);
                    lista.add(codBarra);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return lista;
        }

        static long inserirItemVendaCodigoBarra(Context context, CodBarra codBarra) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_VENDA, codBarra.getIdVenda());
            values.put(COLUNA_DATA_VENDA, Util_IO.dateTimeToString(codBarra.getDataVenda(), Config.FormatDateTimeStringBanco));
            values.put(COLUNA_ID_ITEM_VENDA, codBarra.getIdVendaItem());
            values.put(COLUNA_ID_PRODUTO, codBarra.getIdProdutoObjeto());
            values.put(COLUNA_CODIGO_BARRA, codBarra.getCodBarraInicial());
            values.put(COLUNA_CODIGO_BARRA_FINAL, codBarra.getCodBarraFinal());
            values.put(COLUNA_QUANTIDADE, codBarra.retornaQuantidade(UsoCodBarra.GERAL));
            values.put(COLUNA_ID_PRODUTO_SELECT, codBarra.getIdProduto());
            values.put(COLUNA_CANCELADO, NAO);

            return inserirItemVendaCodigoBarra(context, values);
        }

        static void removerCodigoBarra(Context context, CodBarra codBarra) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String sql = String.format("%s = ?", COLUNA_ID);
            String[] filtros = new String[]{String.valueOf(codBarra.getIdPistolagem())};
            db.delete(TABELA, sql, filtros);
        }

        private static long inserirItemVendaCodigoBarra(Context context, ContentValues values) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);

            return db.insert(TABELA, null, values);
        }

        private static CodBarra converter(Cursor cursor, int idGrupo) {
            CodBarra codBarra = new CodBarra();
            codBarra.setIdPistolagem(getInt(cursor, COLUNA_ID));
            codBarra.setCodBarraInicial(getString(cursor, COLUNA_CODIGO_BARRA));
            codBarra.setIdProduto(getString(cursor, COLUNA_ID_PRODUTO_SELECT));
            codBarra.setNomeProduto(getString(cursor, TabelaProduto.COLUNA_NOME));
            codBarra.setIndividual(true);
            codBarra.setGrupoProduto(idGrupo);

            String codigoBarraFinal = getString(cursor, COLUNA_CODIGO_BARRA_FINAL);
            if (!Util_IO.isNullOrEmpty(codigoBarraFinal) && !codigoBarraFinal.equals(SEM_CODIGO)) {
                codBarra.setIndividual(false);
                codBarra.setCodBarraFinal(codigoBarraFinal);
            }

            return codBarra;
        }
    }

    public static class TabelaItemVendaCombo extends Tabela {
        static final String TABELA = "ItemVendaCombo";
        static final String COLUNA_ID = "id";
        static final String COLUNA_ID_VENDA = "idVenda";
        static final String COLUNA_ID_ITEM_VENDA = "idItemVenda";
        static final String COLUNA_ID_PRODUTO = "idProduto";
        static final String COLUNA_QUANTIDADE = "qtd";
        static final String COLUNA_CANCELADO = "cancelado";

        static long inserirItemVendaCombo(Context context, ComboVenda comboVenda) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_PRODUTO, comboVenda.getIdProduto());
            values.put(COLUNA_QUANTIDADE, comboVenda.getQuantidade());
            values.put(COLUNA_ID_VENDA, comboVenda.getIdVenda());
            values.put(COLUNA_ID_ITEM_VENDA, comboVenda.getIdItemVenda());
            values.put(COLUNA_CANCELADO, NAO);

            return inserirItemVendaCombo(context, values);
        }

        private static long inserirItemVendaCombo(Context context, ContentValues contentValues) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);

            return db.insert(TABELA, null, contentValues);
        }
    }

    public static class TabelaProduto extends Tabela {
        static final String TABELA = "Produto";
        static final String COLUNA_ID = "id";
        static final String COLUNA_CODIGO_SGV = "codSgv";
        static final String COLUNA_NOME = "nome";
        static final String COLUNA_VALOR = "valor";
        static final String COLUNA_ESTOQUE_MAXIMO = "estoqueMax";
        static final String COLUNA_MEDIA_DIARIA_VENDA = "mediaDiariaVnd";
        static final String COLUNA_DIAS_ESTOQUE = "diasEstoque";
        static final String COLUNA_ESTOQUE_SUGERIDO = "estoqueSugerido";
        static final String COLUNA_ESTOQUE_ATUAL = "estoqueAtual";
        static final String COLUNA_ATIVO = "ativo";
        static final String COLUNA_BIPAGEM = "bipagem";
        static final String COLUNA_PRECO_VENDA = "precovenda";
        static final String COLUNA_PRECO_MEDIO = "precomedio";
        static final String COLUNA_BIPAGEM_AUDITORIA = "bipagemAuditoria";
        static final String COLUNA_QUANTIDADE_CODIGO_BARRA = "qtdCodBarra";
        static final String COLUNA_INICIA_CODIGO_BARRA = "iniciaCodBarra";
        static final String COLUNA_QUANTIDADE_COMBO = "qtdCombo";
        static final String COLUNA_PERMITE_VALOR_ZERO = "permiteValorZero";
        static final String COLNA_GRUPO = "grupo";
        static final String COLUNA_OPERADORA = "operadora";
        static final String COLUNA_PERMITE_VENDA_PRAZO = "permiteVendaPrazo";
    }

    public static class TabelaEstrutura extends Tabela {
        static final String TABELA = "EstruturaProd";
        static final String COLUNA_ID = "id";
        static final String COLUNA_ITEM_PAI = "itemPai";
        static final String COLUNA_ITEM_FILHO = "itemFilho";
        static final String COLUNA_PROPORCAO = "proporcao";
        static final String COLUNA_QUANTIDADE = "qtd";

        static String queryPadrao() {
            return String.format(
                    "SELECT %s, " +
                            "%s, " +
                            "%s, " +
                            "IFNULL(%s, 0) AS %s, " +
                            "IFNULL(%s, 0) AS %s " +
                            "FROM %s " +
                            "WHERE 1 = 1",
                    COLUNA_ID,
                    COLUNA_ITEM_PAI,
                    COLUNA_ITEM_FILHO,
                    COLUNA_QUANTIDADE,
                    COLUNA_QUANTIDADE,
                    COLUNA_PROPORCAO,
                    COLUNA_PROPORCAO,
                    TABELA
            );
        }

        static List<EstruturaProd> obterEstruturaPeloItemPai(Context context, String idProduto) {
            String sql = String.format("%s AND %s = ? ", queryPadrao(), COLUNA_ITEM_PAI);
            List<EstruturaProd> lista = new ArrayList<>();
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{idProduto};
            try (Cursor cursor = db.rawQuery(sql, filtros)) {
                while (cursor.moveToNext()) {
                    EstruturaProd estruturaProd = converter(cursor);
                    lista.add(estruturaProd);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return lista;
        }

        private static EstruturaProd converter(Cursor cursor) {
            EstruturaProd estruturaProd = new EstruturaProd();
            estruturaProd.setId(getInt(cursor, COLUNA_ID));
            estruturaProd.setItemPai(getString(cursor, COLUNA_ITEM_PAI));
            estruturaProd.setItemFilho(getString(cursor, COLUNA_ITEM_FILHO));
            estruturaProd.setQtd(getInt(cursor, COLUNA_QUANTIDADE));
            estruturaProd.setProporcao(getDouble(cursor, COLUNA_PROPORCAO));

            return estruturaProd;
        }
    }

    public static class TabelaPreco extends Tabela {
        static final String TABELA = "PrecoDiferenciado";
        static final String COLUNA_ID = "id";
        static final String COLUNA_ID_CLIENTE = "idCliente";
        static final String COLUNA_ID_PRODUTO = "idProduto";
        static final String COLUNA_DATA = "data";
        static final String COLUNA_DATA_INICIO = "datainicio";
        static final String COLUNA_DATA_FIM = "datafim";
        static final String COLUNA_PRECO = "preco";
        static final String COLUNA_SYNC = "sync";
        static final String COLUNA_QUANTIDADE = "qtd";
        static final String COLUNA_QUANTIDADE_VENDIDA = "qtdVendida";
        static final String COLUNA_SITUACAO = "situacao";

        static String queryPadrao() {
            return String.format(
                    "SELECT %s AS %s," +
                            "%s AS %s, " +
                            "%s AS %s," +
                            "%s AS %s, " +
                            "%s AS %s, " +
                            "%s AS %s, " +
                            "%s AS %s, " +
                            "%s AS %s, " +
                            "%s AS %s, " +
                            "%s AS %s, " +
                            "%s AS %s " +
                            "FROM %s " +
                            "LEFT JOIN %s " +
                            "ON %s = %s " +
                            "WHERE 1 = 1 ",
                    join(TABELA, COLUNA_ID),
                    COLUNA_ID,
                    join(TABELA, COLUNA_ID_CLIENTE),
                    COLUNA_ID_CLIENTE,
                    join(TABELA, COLUNA_ID_PRODUTO),
                    COLUNA_ID_PRODUTO,
                    join(TABELA, COLUNA_DATA),
                    COLUNA_DATA,
                    join(TABELA, COLUNA_SITUACAO),
                    COLUNA_SITUACAO,
                    join(TABELA, COLUNA_PRECO),
                    COLUNA_PRECO,
                    join(TABELA, COLUNA_DATA_INICIO),
                    COLUNA_DATA_INICIO,
                    join(TABELA, COLUNA_DATA_FIM),
                    COLUNA_DATA_FIM,
                    join(TABELA, COLUNA_QUANTIDADE),
                    COLUNA_QUANTIDADE,
                    join(TABELA, COLUNA_QUANTIDADE_VENDIDA),
                    COLUNA_QUANTIDADE_VENDIDA,
                    join(TabelaProduto.TABELA, TabelaProduto.COLUNA_NOME),
                    TabelaProduto.COLUNA_NOME,
                    TABELA,
                    TabelaProduto.TABELA,
                    join(TabelaProduto.TABELA, TabelaProduto.COLUNA_ID),
                    join(TABELA, COLUNA_ID_PRODUTO)
            );
        }

        static List<PrecoDiferenciado> obterPrecoDiferenciadoPorCliente(Context context,
                                                                        String idProduto,
                                                                        String idCliente) {
            String sql = String.format(
                    "%s " +
                            "AND %s = 'A' " +
                            "AND CAST((%s) AS INT) = ? " +
                            "AND %s = ? " +
                            "AND ((date(%s) <= date('now') AND date(%s) >= date('now')) OR (IFNULL(%s, 0) > 0)) " +
                            "AND %s > (SELECT IFNULL(SUM(%s), 0) FROM %s WHERE %s = %s AND %s = 0) " +
                            "AND NOT EXISTS (SELECT 1 FROM %s WHERE %s = %S AND %s >= %s)" +
                            "ORDER BY %s",
                    queryPadrao(),
                    join(TABELA, COLUNA_SITUACAO),
                    join(TABELA, COLUNA_ID_PRODUTO),
                    join(TABELA, COLUNA_ID_CLIENTE),
                    join(TABELA, COLUNA_DATA_INICIO),
                    join(TABELA, COLUNA_DATA_FIM),
                    join(TABELA, COLUNA_QUANTIDADE),
                    join(TABELA, COLUNA_QUANTIDADE),
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_QUANTIDADE),
                    TabelaItemVenda.TABELA,
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_ID_PRECO),
                    join(TabelaItemVenda.TABELA, TabelaItemVenda.COLUNA_CANCELADO),
                    join(TABELA, COLUNA_ID),
                    "PistolagemComboTemp",
                    join(TABELA, COLUNA_ID),
                    "PistolagemComboTemp.idPreco",
                    "PistolagemComboTemp.quantidade",
                    join(TABELA, COLUNA_QUANTIDADE),
                    join(TABELA, COLUNA_ID)
            );

            List<PrecoDiferenciado> precos = new ArrayList<>();
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{idProduto, idCliente};
            try (Cursor cursor = db.rawQuery(sql, filtros)) {
                while (cursor.moveToNext()) {
                    PrecoDiferenciado precoDiferenciado = converter(cursor);
                    precos.add(precoDiferenciado);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return precos;
        }


        private static PrecoDiferenciado converter(Cursor cursor) {
            int quantidade = getInt(cursor, COLUNA_QUANTIDADE);
            if (getInt(cursor, COLUNA_QUANTIDADE) > 0 && getInt(cursor, COLUNA_QUANTIDADE_VENDIDA) > 0) {
                quantidade = getInt(cursor, COLUNA_QUANTIDADE) - getInt(cursor, COLUNA_QUANTIDADE_VENDIDA);
                if (getInt(cursor, COLUNA_QUANTIDADE) <= getInt(cursor, COLUNA_QUANTIDADE_VENDIDA))
                    return null;
            }

            PrecoDiferenciado preco = new PrecoDiferenciado();
            preco.setId(getInt(cursor, COLUNA_ID));
            preco.setIdCliente(getString(cursor, COLUNA_ID_CLIENTE));
            preco.setIdProduto(getString(cursor, COLUNA_ID_PRODUTO));
            preco.setDataCadastro(Util_IO.stringToDate(getString(cursor, COLUNA_DATA), Config.FormatDateTimeStringBanco));
            preco.setSituacao(getString(cursor, COLUNA_SITUACAO));
            preco.setValor(getDouble(cursor, COLUNA_PRECO));
            preco.setDataInicial(Util_IO.stringToDate(getString(cursor, COLUNA_DATA_INICIO), Config.FormatDateStringBanco));
            preco.setDataFinal(Util_IO.stringToDate(getString(cursor, COLUNA_DATA_FIM), Config.FormatDateStringBanco));
            preco.setQtdPreco(quantidade);
            preco.setQtdVendida(getInt(cursor, COLUNA_QUANTIDADE_VENDIDA));

            return preco;
        }
    }

    public static class TabelaSegmento extends Tabela {

        static final String TABELA = "Segmento";
        static final String COLUNA_ID = "id";
        static final String COLUNA_DESCRICAO = "descricao";
        static final String COLUNA_SITUACAO = "situacao";

        static Segmento obterPorId(Context context, String id) {
            String sql = String.format(
                    "%s AND %s = ?",
                    queryPadrao(),
                    COLUNA_ID
            );
            Segmento segmento = null;
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{id};
            try (Cursor cursor = db.rawQuery(sql, filtros)) {
                while (cursor.moveToNext()) {
                    segmento = converter(cursor);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return segmento;
        }

        static String queryPadrao() {
            return String.format(
                    "SELECT %s, %s, %s FROM %s WHERE 1 = 1",
                    COLUNA_ID,
                    COLUNA_DESCRICAO,
                    COLUNA_SITUACAO,
                    TABELA
            );
        }

        private static Segmento converter(Cursor cursor) {
            Segmento segmento = new Segmento();
            segmento.setId(getString(cursor, COLUNA_ID));
            segmento.setDescricao(getString(cursor, COLUNA_DESCRICAO));
            segmento.setSituacao(getString(cursor, COLUNA_SITUACAO));

            return segmento;
        }
    }

    public static class TabelaSugestaoVenda extends Tabela {

        static final String TABELA = "SugestaoVenda";
        static final String ID = "id";
        static final String CLIENTE_ID = "clienteId";
        static final String VENDA_DIARIA = "vendaDiaria";
        static final String ESTOQUE_MINIMO = "estoqueMinimo";
        static final String ESTOQUE_SEGURANCA = "estoqueSeguranca";
        static final String ESTOQUE_IDEAL = "estoqueIdeal";
        static final String OPERADORA_ID = "operadoraId";
        static final String GRUPO_PRODUTO = "grupoProdutoSAP";
        static final String SITUACAOCLIENTE = "SituacaoCliente";
        static final String DESCRICAOGRUPO = "DescricaoGrupo";

        static final String[] CAMPOS = {
                ID,
                CLIENTE_ID,
                VENDA_DIARIA,
                ESTOQUE_MINIMO,
                ESTOQUE_SEGURANCA,
                ESTOQUE_IDEAL,
                OPERADORA_ID,
                GRUPO_PRODUTO,
                SITUACAOCLIENTE,
                DESCRICAOGRUPO
        };

        static ContentValues converter(SugestaoVenda sugestaoVenda) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, sugestaoVenda.getId());
            contentValues.put(CLIENTE_ID, sugestaoVenda.getIdCliente());
            contentValues.put(VENDA_DIARIA, sugestaoVenda.getVendaDia());
            contentValues.put(ESTOQUE_MINIMO, sugestaoVenda.getEstoqueMinimo());
            contentValues.put(ESTOQUE_SEGURANCA, sugestaoVenda.getEstoqueSeguranca());
            contentValues.put(ESTOQUE_IDEAL, sugestaoVenda.getEstoqueIdeal());
            contentValues.put(OPERADORA_ID, sugestaoVenda.getIdOperadora());
            contentValues.put(GRUPO_PRODUTO, sugestaoVenda.getGrupoProduto());
            contentValues.put(SITUACAOCLIENTE, sugestaoVenda.getSituacaocliente());
            contentValues.put(DESCRICAOGRUPO, sugestaoVenda.getDescricaogrupo());
            return contentValues;
        }

        static SugestaoVenda converter(Cursor cursor) {
            SugestaoVenda sugestaoVenda = new SugestaoVenda();
            sugestaoVenda.setId(getLong(cursor, ID));
            sugestaoVenda.setIdCliente(getLong(cursor, CLIENTE_ID));
            sugestaoVenda.setVendaDia(getInt(cursor, VENDA_DIARIA));
            sugestaoVenda.setEstoqueMinimo(getInt(cursor, ESTOQUE_MINIMO));
            sugestaoVenda.setEstoqueSeguranca(getInt(cursor, ESTOQUE_SEGURANCA));
            sugestaoVenda.setEstoqueIdeal(getInt(cursor, ESTOQUE_IDEAL));
            sugestaoVenda.setGrupoProduto(getInt(cursor, GRUPO_PRODUTO));
            sugestaoVenda.setIdOperadora(getInt(cursor, OPERADORA_ID));
            sugestaoVenda.setSituacaocliente(getString(cursor, SITUACAOCLIENTE));
            sugestaoVenda.setDescricaogrupo(getString(cursor, DESCRICAOGRUPO));
            return sugestaoVenda;
        }

        static String filtroPadrao() {
            return ID + " = ?";
        }

        static String filtroCliente() {
            return String.format(
                    "%s = ? AND %s = ? AND %s = ?",
                    CLIENTE_ID,
                    OPERADORA_ID,
                    GRUPO_PRODUTO
            );
        }

        static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] TEXT,\n" +
                            "[%s] TEXT,\n" +
                            "CONSTRAINT pkSugestaoVenda PRIMARY KEY ([id]))",
                    TABELA,
                    ID,
                    CLIENTE_ID,
                    VENDA_DIARIA,
                    ESTOQUE_MINIMO,
                    ESTOQUE_SEGURANCA,
                    SITUACAOCLIENTE,
                    DESCRICAOGRUPO
            );
        }

        static void salvar(Context context, SugestaoVenda sugestaoVenda) {
            ContentValues values = converter(sugestaoVenda);
            String id = String.valueOf(sugestaoVenda.getId());
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            if (!existe(context, id)) {
                db.insert(TABELA, null, values);

                return;
            }

            String[] filtros = new String[]{id};
            db.update(TABELA, values, filtroPadrao(), filtros);
        }

        static SugestaoVenda obterPorCliente(Context context,
                                             String idCliente,
                                             String idOperador, String grupo) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{idCliente, idOperador, grupo};
            SugestaoVenda sugestaoVenda = null;
            try (Cursor cursor = db.query(TABELA, CAMPOS, filtroCliente(), filtros, null, null, null)) {
                while (cursor.moveToNext()) {
                    sugestaoVenda = converter(cursor);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return sugestaoVenda;
        }

        static SugestaoVenda obterPorId(Context context, String id) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{id};
            SugestaoVenda sugestaoVenda = null;
            try (Cursor cursor = db.query(TABELA, CAMPOS, filtroPadrao(), filtros, null, null, null)) {
                while (cursor.moveToNext()) {
                    sugestaoVenda = converter(cursor);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return sugestaoVenda;
        }

        private static boolean existe(Context context, String id) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{id};
            try (Cursor cursor = db.query(TABELA, CAMPOS, filtroPadrao(), filtros, null, null, null)) {
                return (cursor != null && cursor.getCount() > 0);
            } catch (Exception e) {
                Timber.e(e);
            }

            return false;
        }
    }

    public static class TabelaInformacoesGeraisPOS extends Tabela {

        private static final String NOME_TABELA = "InformacoesGeraisPOS";

        private static final String ID = "Id";
        private static final String ID_CLIENTE = "IdCliente";
        private static final String CODIGO_MASTERS = "CodigoMasters";
        private static final String CODIGO_MUXX = "CodigoMuxx";
        private static final String NUMERO_SERIE = "NumeroSerie";
        private static final String ENTIDADE_PONTO_CAPTURA_ID = "EntidadePontoCapturaId";
        private static final String ADQUIRENCIA = "Adquirencia";
        private static final String RECARGA = "Recarga";
        private static final String DATA_INSTALACAO_ADQUIRENCIA = "DataInstalacaoAdquirencia";
        private static final String VALOR_ALUGUEL = "ValorAluguel";
        private static final String DATA_ULTIMA_TRANSACAO_ADQUIRENCIA = "DataUltimaTransacaoAdquirencia";
        private static final String DATA_ULTIMA_VENDA_RECARGA = "DataUltimaVendaRecarga";
        private static final String VALOR_TRANSACIONADO_ADQUIRENCIA = "ValorTransacionadoAdquirencia";
        private static final String TERMINAL_ALOCADO_SGV = "TerminalAlocadoSGV";
        private static final String PONTUACAO = "Pontuacao";
        private static final String ICCID = "ICCID";
        private static final String SITUACAO_PONTO_CAPTURA_ID = "SituacaoPontoCapturaId";
        private static final String SITUACAO_PONTO_CAPTURA = "SituacaoPontoCaptura";
        private static final String MODELO = "Modelo";
        private static final String DESCRICAO = "Transacionando";
        private static final String DATA_CADASTRO = "DataCadastro";
        private static final String VENDEDOR_INSTALACAO = "VendedorInstalacao";
        private static final String ATIVO = "ativo";

        private static final String VALORTRANSMESATUAL = "ValorTransMesAtual";
        private static final String VALORTRANSMESANTERIOR = "ValorTransMesAnterior";
        private static final String TRANSACIONADORECARGA = "TransacionadoRecarga";
        private static final String VALORRECARGA_TRANSACIONADO = "ValorRecarga_Transacionado";
        private static final String VALORRECARGA_TRANSMESATUAL = "ValorRecarga_TransMesAtual";
        private static final String VALORRECARGA_TRANSMESANTERIOR = "ValorRecarga_TransMesAnterior";



        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] VARCHAR(60),\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] DATETIME,\n" +
                            "[%s] DECIMAL(6,2),\n" +
                            "[%s] DATETIME,\n" +
                            "[%s] DATETIME,\n" +
                            "[%s] DECIMAL(6, 2),\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(255),\n" +
                            "[%s] VARCHAR(255));",
                    NOME_TABELA,
                    ID,
                    ID_CLIENTE,
                    CODIGO_MASTERS,
                    CODIGO_MUXX,
                    NUMERO_SERIE,
                    ENTIDADE_PONTO_CAPTURA_ID,
                    ADQUIRENCIA,
                    RECARGA,
                    DATA_INSTALACAO_ADQUIRENCIA,
                    VALOR_ALUGUEL,
                    DATA_ULTIMA_TRANSACAO_ADQUIRENCIA,
                    DATA_ULTIMA_VENDA_RECARGA,
                    VALOR_TRANSACIONADO_ADQUIRENCIA,
                    TERMINAL_ALOCADO_SGV,
                    PONTUACAO,
                    ICCID,
                    SITUACAO_PONTO_CAPTURA_ID,
                    SITUACAO_PONTO_CAPTURA,
                    MODELO,
                    DESCRICAO);
        }

        private static String obterQuerySelect() {
            return String.format("SELECT %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = 1 AND %s = ?",
                    ID,
                    ID_CLIENTE,
                    CODIGO_MASTERS,
                    CODIGO_MUXX,
                    NUMERO_SERIE,
                    ENTIDADE_PONTO_CAPTURA_ID,
                    ADQUIRENCIA,
                    RECARGA,
                    DATA_INSTALACAO_ADQUIRENCIA,
                    VALOR_ALUGUEL,
                    DATA_ULTIMA_TRANSACAO_ADQUIRENCIA,
                    DATA_ULTIMA_VENDA_RECARGA,
                    VALOR_TRANSACIONADO_ADQUIRENCIA,
                    TERMINAL_ALOCADO_SGV,
                    PONTUACAO,
                    ICCID,
                    SITUACAO_PONTO_CAPTURA_ID,
                    SITUACAO_PONTO_CAPTURA,
                    MODELO,
                    DESCRICAO,
                    DATA_CADASTRO,
                    VENDEDOR_INSTALACAO,
                    ATIVO,
                    VALORTRANSMESATUAL,
                    VALORTRANSMESANTERIOR,
                    TRANSACIONADORECARGA,
                    VALORRECARGA_TRANSACIONADO,
                    VALORRECARGA_TRANSMESATUAL,
                    VALORRECARGA_TRANSMESANTERIOR,
                    NOME_TABELA,
                    ATIVO,
                    ID_CLIENTE);
        }

        static void addInformacaoGeralPOS(Context context, InformacaoGeralPOS item) {
            ContentValues values = new ContentValues();
            values.put(ID, item.getId());
            values.put(ID_CLIENTE, item.getIdCliente());
            values.put(CODIGO_MASTERS, item.getCodigoMasters());
            values.put(CODIGO_MUXX, item.getCodigoMuxx());
            values.put(NUMERO_SERIE, item.getNumeroSerie());
            values.put(ENTIDADE_PONTO_CAPTURA_ID, item.getEntidadePontoCapturaId());
            values.put(ADQUIRENCIA, Util_IO.booleanToNumber(item.getAdquirencia()));
            values.put(RECARGA, item.getRecarga());
            values.put(DATA_INSTALACAO_ADQUIRENCIA, Util_IO.dateTimeToString(item.getDataInstalacaoAdquirencia(), Config.FormatDateTimeStringBanco));
            values.put(VALOR_ALUGUEL, item.getValorAluguel());
            values.put(DATA_ULTIMA_TRANSACAO_ADQUIRENCIA, Util_IO.dateTimeToString(item.getDataUltimaTransacaoAdquirencia(), Config.FormatDateTimeStringBanco));
            values.put(DATA_ULTIMA_VENDA_RECARGA, Util_IO.dateTimeToString(item.getDataUltimaVendaRecarga(), Config.FormatDateTimeStringBanco));
            values.put(VALOR_TRANSACIONADO_ADQUIRENCIA, item.getValorTransacionadoAdquirencia());
            values.put(TERMINAL_ALOCADO_SGV, Util_IO.booleanToNumber(item.getTerminalAlocadoSGV()));
            values.put(PONTUACAO, item.getPontuacao());
            values.put(ICCID, item.getIccid());
            values.put(SITUACAO_PONTO_CAPTURA_ID, item.getSituacaoPontoCapturaId());
            values.put(SITUACAO_PONTO_CAPTURA, item.getSituacaoPontoCaptura());
            values.put(MODELO, item.getModelo());
            values.put(DESCRICAO, item.getDescricao());
            values.put(DATA_CADASTRO, Util_IO.dateTimeToString(item.getDataCadastro(), Config.FormatDateTimeStringBanco));
            values.put(VENDEDOR_INSTALACAO, item.getVendedorInstalacao());
            SimpleDbHelper.INSTANCE.open(context).replace(NOME_TABELA, null, values);
        }

        static List<InformacaoGeralPOS> obterInformacoesGeraisPOSPorId(Context context, String clienteId) {
            List<InformacaoGeralPOS> lista = new ArrayList<>();
            String consulta = obterQuerySelect();
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] param = new String[]{clienteId};

            try (Cursor cursor = db.rawQuery(consulta, param)) {
                while (cursor.moveToNext()) {
                    lista.add(converter(cursor));
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return lista;
        }

        static InformacaoGeralPOS converter(Cursor cursor) {
            InformacaoGeralPOS obj = new InformacaoGeralPOS();
            obj.setId(getInt(cursor, ID));
            obj.setIdCliente(getInt(cursor, ID_CLIENTE));
            obj.setCodigoMasters(getString(cursor, CODIGO_MASTERS));
            obj.setCodigoMuxx(getString(cursor, CODIGO_MUXX));
            obj.setNumeroSerie(getString(cursor, NUMERO_SERIE));
            obj.setEntidadePontoCapturaId(getInt(cursor, ENTIDADE_PONTO_CAPTURA_ID));
            obj.setAdquirencia(Util_IO.numberToBoolean(getInt(cursor, ADQUIRENCIA)));
            obj.setRecarga(getInt(cursor, RECARGA));
            obj.setDataInstalacaoAdquirencia(Util_IO.stringToDate(getString(cursor, DATA_INSTALACAO_ADQUIRENCIA), Config.FormatDateTimeStringBanco));
            obj.setValorAluguel(getDouble(cursor, VALOR_ALUGUEL));
            obj.setDataUltimaTransacaoAdquirencia(Util_IO.stringToDate(getString(cursor, DATA_ULTIMA_TRANSACAO_ADQUIRENCIA), Config.FormatDateTimeStringBanco));
            obj.setDataUltimaVendaRecarga(Util_IO.stringToDate(getString(cursor, DATA_ULTIMA_VENDA_RECARGA), Config.FormatDateTimeStringBanco));
            obj.setValorTransacionadoAdquirencia(getDouble(cursor, VALOR_TRANSACIONADO_ADQUIRENCIA));
            obj.setTerminalAlocadoSGV(Util_IO.numberToBoolean(getInt(cursor, TERMINAL_ALOCADO_SGV)));
            obj.setPontuacao(getDouble(cursor, PONTUACAO));
            obj.setIccid(getString(cursor, ICCID));
            obj.setSituacaoPontoCapturaId(getInt(cursor, SITUACAO_PONTO_CAPTURA_ID));
            obj.setSituacaoPontoCaptura(getInt(cursor, SITUACAO_PONTO_CAPTURA));
            obj.setModelo(getString(cursor, MODELO));
            obj.setDescricao(getString(cursor, DESCRICAO));
            obj.setDataCadastro(Util_IO.stringToDate(getString(cursor, DATA_CADASTRO), Config.FormatDateTimeStringBanco));
            obj.setVendedorInstalacao(getString(cursor, VENDEDOR_INSTALACAO));
            obj.setAtivo(Util_IO.numberToBoolean(getInt(cursor, ATIVO)));
            obj.setValortransmesatual(getDouble(cursor, VALORTRANSMESATUAL));
            obj.setValortransmesanterior(getDouble(cursor, VALORTRANSMESANTERIOR));
            obj.setTransacionadorecarga(getString(cursor, TRANSACIONADORECARGA));
            obj.setValorrecarga_transacionado(getDouble(cursor, VALORRECARGA_TRANSACIONADO));
            obj.setValorrecarga_transmesatual(getDouble(cursor, VALORRECARGA_TRANSMESATUAL));
            obj.setValorrecarga_transmesanterior(getDouble(cursor, VALORRECARGA_TRANSMESANTERIOR));

            return obj;
        }

        static void remover(Context context, int id) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String filtro = "id = ?";
            String[] queryParam = new String[]{String.valueOf(id)};
            db.delete(NOME_TABELA, filtro, queryParam);
        }
    }

    public static class TabelaColaborador extends Tabela {

        private static final String NOME_TABELA = "Colaborador";
        private static final String ID = "id";
        private static final String NOME = "nome";
        private static final String ENVIAR_BASE = "enviarBase";
        private static final String DATA_DESBLOQUEIA = "dataDesbloqueia";
        private static final String DESBLOQUEIA_VENDA = "desbloqueiaVenda";
        private static final String BLOQUEIA_HORARIO = "bloqueiaHorario";
        private static final String TIPO = "tipo";
        private static final String VERSAO_APP = "versaoApp";
        private static final String DISTANCIA = "distancia";
        private static final String VALIDA_ICCID = "validaIccid";
        private static final String VALIDA_ORDEM_ROTA = "validaOrdemRota";
        private static final String OBRIGA_AUDITAGEM = "obrigaAuditagem";
        private static final String SEMANA_ROTA = "semanaRota";
        private static final String VALIDA_DATA_GPS = "validaDataGPS";
        private static final String VALIDA_CERCA_FINAL = "validaCercaFinal";
        private static final String NOVO_ATEND = "novoAtend";
        private static final String ID_CLIENTE = "idCliente";
        private static final String PIS = "pis";
        private static final String CNPJ_FILIAL = "cnpjFilial";
        private static final String CARTAO_PONTO = "cartaoPonto";
        private static final String VERIFICA_PENDENCIA = "verificaClientePendencia";
        private static final String EMAIL = "email";

        static Colaborador get(Context context) {
            String sql = "SELECT " + ID +
                    ", " + NOME +
                    ", " + ENVIAR_BASE +
                    ", " + DATA_DESBLOQUEIA +
                    ", " + DESBLOQUEIA_VENDA +
                    ", " + BLOQUEIA_HORARIO +
                    ", " + TIPO +
                    ", " + VERSAO_APP +
                    ", IFNULL(" + DISTANCIA + ", 0) AS " + DISTANCIA +
                    ", IFNULL(" + VALIDA_ICCID + ", 0) AS " + VALIDA_ICCID +
                    ", IFNULL(" + VALIDA_ORDEM_ROTA + ", 0) AS " + VALIDA_ORDEM_ROTA +
                    ", IFNULL(" + OBRIGA_AUDITAGEM + ", 0) AS " + OBRIGA_AUDITAGEM +
                    ", IFNULL(" + SEMANA_ROTA + ", 0) AS " + SEMANA_ROTA +
                    ", IFNULL(" + VALIDA_DATA_GPS + ", 0) AS " + VALIDA_DATA_GPS +
                    ", IFNULL(" + VALIDA_CERCA_FINAL + ", 0) AS " + VALIDA_CERCA_FINAL +
                    ", IFNULL(" + NOVO_ATEND + ", 0) AS " + NOVO_ATEND +
                    ", " + PIS +
                    ", " + CNPJ_FILIAL +
                    ", 1 AS ok" +
                    ", " + ID_CLIENTE +
                    ", " + CARTAO_PONTO +
                    ", " + VERIFICA_PENDENCIA +
                    ", " + EMAIL +
                    " FROM [" + NOME_TABELA + "]";

            Colaborador colaborador = null;
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            try (Cursor cursor = db.rawQuery(sql, null)) {
                while (cursor.moveToNext()) {
                    colaborador = converter(cursor);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return colaborador;
        }

        static Colaborador converter(Cursor cursor) {
            Colaborador colaborador = new Colaborador();
            colaborador.setId(getInt(cursor, ID));
            colaborador.setNome(getString(cursor, NOME));
            colaborador.setEnviarBase(getInt(cursor, ENVIAR_BASE));
            colaborador.setDataDesbloqueiaVenda(Util_IO.stringToDate(getString(cursor, DATA_DESBLOQUEIA)));
            colaborador.setDesbloqueiaVenda(getString(cursor, DESBLOQUEIA_VENDA));
            colaborador.setBloqueiaHorario(getString(cursor, BLOQUEIA_HORARIO));
            colaborador.setTipoColaborador(getString(cursor, TIPO));
            colaborador.setVersaoApp(getString(cursor, VERSAO_APP));
            colaborador.setDistancia(getDouble(cursor, DISTANCIA));
            colaborador.setValidaICCID(Util_IO.numberToBoolean(getInt(cursor, VALIDA_ICCID)));
            colaborador.setValidaOrdemRota(Util_IO.numberToBoolean(getInt(cursor, VALIDA_ORDEM_ROTA)));
            colaborador.setObrigaAuditagem(Util_IO.numberToBoolean(getInt(cursor, OBRIGA_AUDITAGEM)));
            colaborador.setSemanaRota(getInt(cursor, SEMANA_ROTA));
            colaborador.setValidaDataGps(Util_IO.numberToBoolean(getInt(cursor, VALIDA_DATA_GPS)));
            colaborador.setValidaCercaFinalAtend(Util_IO.numberToBoolean(getInt(cursor, VALIDA_CERCA_FINAL)));
            colaborador.setIdCliente(getString(cursor, ID_CLIENTE));
            colaborador.setPis(getString(cursor, PIS));
            colaborador.setCnpjFilial(getString(cursor, CNPJ_FILIAL));
            colaborador.setCartaoPonto(Util_IO.numberToBoolean(getInt(cursor, CARTAO_PONTO)));
            colaborador.setVerificaClientePendencia(Util_IO.numberToBoolean(getInt(cursor, VERIFICA_PENDENCIA)));
            colaborador.setEmail(getString(cursor, EMAIL));

            return colaborador;
        }
    }

    public static class TabelaConsignadoLimiteProduto extends Tabela {

        static final String TABELA = "ConsignadoLimiteProduto";
        static final String ID = "id";
        static final String CLIENTE_ID = "clienteId";
        static final String VENDA_DIARIA = "vendaDiaria";
        static final String ESTOQUE_MINIMO = "estoqueMinimo";
        static final String ESTOQUE_SEGURANCA = "estoqueSeguranca";
        static final String ESTOQUE_IDEAL = "estoqueIdeal";
        static final String OPERADORA_ID = "operadoraId";
        static final String GRUPO_PRODUTO = "grupoProdutoSAP";
        static final String SITUACAOCLIENTE = "SituacaoCliente";
        static final String DESCRICAOGRUPO = "DescricaoGrupo";

        static final String[] CAMPOS = {
                ID,
                CLIENTE_ID,
                VENDA_DIARIA,
                ESTOQUE_MINIMO,
                ESTOQUE_SEGURANCA,
                ESTOQUE_IDEAL,
                OPERADORA_ID,
                GRUPO_PRODUTO,
                SITUACAOCLIENTE,
                DESCRICAOGRUPO
        };

        static ContentValues converter(SugestaoVenda sugestaoVenda) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, sugestaoVenda.getId());
            contentValues.put(CLIENTE_ID, sugestaoVenda.getIdCliente());
            contentValues.put(VENDA_DIARIA, sugestaoVenda.getVendaDia());
            contentValues.put(ESTOQUE_MINIMO, sugestaoVenda.getEstoqueMinimo());
            contentValues.put(ESTOQUE_SEGURANCA, sugestaoVenda.getEstoqueSeguranca());
            contentValues.put(ESTOQUE_IDEAL, sugestaoVenda.getEstoqueIdeal());
            contentValues.put(OPERADORA_ID, sugestaoVenda.getIdOperadora());
            contentValues.put(GRUPO_PRODUTO, sugestaoVenda.getGrupoProduto());
            contentValues.put(SITUACAOCLIENTE, sugestaoVenda.getSituacaocliente());
            contentValues.put(DESCRICAOGRUPO, sugestaoVenda.getDescricaogrupo());
            return contentValues;
        }

        static SugestaoVenda converter(Cursor cursor) {
            SugestaoVenda sugestaoVenda = new SugestaoVenda();
            sugestaoVenda.setId(getLong(cursor, ID));
            sugestaoVenda.setIdCliente(getLong(cursor, CLIENTE_ID));
            sugestaoVenda.setVendaDia(getInt(cursor, VENDA_DIARIA));
            sugestaoVenda.setEstoqueMinimo(getInt(cursor, ESTOQUE_MINIMO));
            sugestaoVenda.setEstoqueSeguranca(getInt(cursor, ESTOQUE_SEGURANCA));
            sugestaoVenda.setEstoqueIdeal(getInt(cursor, ESTOQUE_IDEAL));
            sugestaoVenda.setGrupoProduto(getInt(cursor, GRUPO_PRODUTO));
            sugestaoVenda.setIdOperadora(getInt(cursor, OPERADORA_ID));
            sugestaoVenda.setSituacaocliente(getString(cursor, SITUACAOCLIENTE));
            sugestaoVenda.setDescricaogrupo(getString(cursor, DESCRICAOGRUPO));
            return sugestaoVenda;
        }

        static String filtroPadrao() {
            return ID + " = ?";
        }

        static String filtroCliente() {
            return String.format(
                    "%s = ? AND %s = ? AND %s = ?",
                    CLIENTE_ID,
                    OPERADORA_ID,
                    GRUPO_PRODUTO
            );
        }

        static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] TEXT,\n" +
                            "[%s] TEXT,\n" +
                            "CONSTRAINT pkSugestaoVenda PRIMARY KEY ([id]))",
                    TABELA,
                    ID,
                    CLIENTE_ID,
                    VENDA_DIARIA,
                    ESTOQUE_MINIMO,
                    ESTOQUE_SEGURANCA,
                    SITUACAOCLIENTE,
                    DESCRICAOGRUPO
            );
        }

        static void salvar(Context context, SugestaoVenda sugestaoVenda) {
            ContentValues values = converter(sugestaoVenda);
            String id = String.valueOf(sugestaoVenda.getId());
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            if (!existe(context, id)) {
                db.insert(TABELA, null, values);

                return;
            }

            String[] filtros = new String[]{id};
            db.update(TABELA, values, filtroPadrao(), filtros);
        }

        static SugestaoVenda obterPorCliente(Context context,
                                             String idCliente,
                                             String idOperador, String grupo) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{idCliente, idOperador, grupo};
            SugestaoVenda sugestaoVenda = null;
            try (Cursor cursor = db.query(TABELA, CAMPOS, filtroCliente(), filtros, null, null, null)) {
                while (cursor.moveToNext()) {
                    sugestaoVenda = converter(cursor);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return sugestaoVenda;
        }

        static SugestaoVenda obterPorId(Context context, String id) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{id};
            SugestaoVenda sugestaoVenda = null;
            try (Cursor cursor = db.query(TABELA, CAMPOS, filtroPadrao(), filtros, null, null, null)) {
                while (cursor.moveToNext()) {
                    sugestaoVenda = converter(cursor);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            return sugestaoVenda;
        }

        private static boolean existe(Context context, String id) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String[] filtros = new String[]{id};
            try (Cursor cursor = db.query(TABELA, CAMPOS, filtroPadrao(), filtros, null, null, null)) {
                return (cursor != null && cursor.getCount() > 0);
            } catch (Exception e) {
                Timber.e(e);
            }

            return false;
        }
    }
}
