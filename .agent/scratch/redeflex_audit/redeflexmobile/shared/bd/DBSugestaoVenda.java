package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.dao.Tabela;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.SugestaoVenda;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

import timber.log.Timber;

public class DBSugestaoVenda {

    public static final String SQL = Tabela.criarTabela();
    public static final String[] UPDATE_63 = Tabela.alterarTabelaVersao63();

    private Context context;

    public DBSugestaoVenda(Context context) {
        this.context = context;
    }

    public void salvar(SugestaoVenda sugestaoVenda) {
        ContentValues values = Tabela.converter(sugestaoVenda);
        String id = String.valueOf(sugestaoVenda.getId());
        if (!existe(id)) {
            SimpleDbHelper.INSTANCE
                    .open(context)
                    .insert(Tabela.TABELA, null, values);
            return;
        }

        SimpleDbHelper.INSTANCE
                .open(context)
                .update(
                        Tabela.TABELA,
                        values,
                        Tabela.filtroPadrao(),
                        new String[]{id}
                );
    }

    public SugestaoVenda obterPorCliente(String id) {
        Cursor cursor = SimpleDbHelper.INSTANCE
                .open(context)
                .query(
                        Tabela.TABELA,
                        Tabela.CAMPOS,
                        Tabela.filtroCliente(),
                        new String[]{id},
                        null,
                        null,
                        null
                );

        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                return Tabela.converter(cursor);
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return null;
    }

    public SugestaoVenda obterPorId(String id) {
        Cursor cursor = SimpleDbHelper.INSTANCE
                .open(context)
                .query(
                        Tabela.TABELA,
                        Tabela.CAMPOS,
                        Tabela.filtroPadrao(),
                        new String[]{id},
                        null,
                        null,
                        null
                );

        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                return Tabela.converter(cursor);
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return null;
    }

    private boolean existe(String id) {
        Cursor cursor = SimpleDbHelper.INSTANCE
                .open(context)
                .query(
                        Tabela.TABELA,
                        Tabela.CAMPOS,
                        Tabela.filtroPadrao(),
                        new String[]{id},
                        null,
                        null,
                        null
                );

        try {
            return (cursor != null && cursor.getCount() > 0);
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(Tabela.TABELA, null, null);
    }

    private static class Tabela {

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
            return CLIENTE_ID + " = ?";
        }

        private static int getInt(Cursor cursor, String coluna) {
            return cursor.getInt(cursor.getColumnIndex(coluna));
        }

        private static long getLong(Cursor cursor, String coluna) {
            return cursor.getLong(cursor.getColumnIndex(coluna));
        }

        private static double getDouble(Cursor cursor, String coluna) {
            return cursor.getDouble(cursor.getColumnIndex(coluna));
        }

        private static String getString(Cursor cursor, String coluna) {
            return cursor.getString(cursor.getColumnIndex(coluna));
        }

        static String[] alterarTabelaVersao63() {
            return new String[]{
                    String.format(
                            "ALTER TABLE [%S] ADD COLUMN [%s] INTEGER",
                            TABELA,
                            ESTOQUE_IDEAL
                    ),
                    String.format(
                            "ALTER TABLE [%S] ADD COLUMN [%s] INTEGER",
                            TABELA,
                            GRUPO_PRODUTO
                    ),
                    String.format(
                            "ALTER TABLE [%S] ADD COLUMN [%s] INTEGER",
                            TABELA,
                            OPERADORA_ID
                    )
            };
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
    }

    public ArrayList<SugestaoVenda> getSugestaoRuptura(String idCliente) {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("select t0.* from SugestaoVenda t0");
        sbSQL.appendLine("where t0.clienteid = " + idCliente);
        sbSQL.appendLine("and t0.situacaocliente in ('Ruptura','Pré-Ruptura')");
        Cursor cursor = null;
        ArrayList<SugestaoVenda> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sbSQL.toString(), null);
            while (cursor.moveToNext()) {
                lista.add(DBSugestaoVenda.Tabela.converter(cursor));
            }
            Timber.i("SugestaoVenda :" + sbSQL.toString());

        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            if (cursor != null) cursor.close();
        }
        return lista;
    }

    public String getSituacaoSugestaoRuptura(int idVenda, String idProduto) {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("select t2.* from Venda t0");
        sbSQL.appendLine("left join Produto t1 on t1.id = '" + idProduto + "'");
        sbSQL.appendLine("left join SugestaoVenda t2 on t2.clienteId = t0.idCliente and t2.grupoProdutoSAP = t1.grupo and t2.operadoraId = t1.operadora");
        sbSQL.appendLine("where t0.id = " + idVenda);
        Cursor cursor = null;
        ArrayList<SugestaoVenda> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sbSQL.toString(), null);

            if (!cursor.moveToFirst()) {
                return "";
            }
            else {
                return DBSugestaoVenda.Tabela.converter(cursor).getSituacaocliente();
            }
        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            if (cursor != null) cursor.close();
        }

        return "";
    }

    public boolean validaSugestaoVendaItem(int idVenda, int idGrupo, int idOperadora)
    {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("select t0.* from ItemVenda t0");
        sbSQL.appendLine("left join Produto t1 on t1.id = t0.idproduto");
        sbSQL.appendLine("where t0.idVenda = " + idVenda + " t1.grupo = " + idGrupo + " and t1.operadora = " + idOperadora);
        Cursor cursor = null;
        ArrayList<ItemVenda> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sbSQL.toString(), null);
            if (!cursor.moveToFirst()) {
                Timber.e("Sugestao não encontrada!");
            }
            else
                Timber.e("Dados: " + idVenda + " / " + idGrupo + " / " + idOperadora);

        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }
}