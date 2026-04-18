package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.LimiteCliente;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.shared.models.ModeloPOSConexao;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;

public class DBModeloPOS {

    public static final String UPDATE_70_SQL_INSERIR_OPERADORA = TabelaModeloPOSConexao.atualizarTabela70Operadora();
    public static final String UPDATE_70_SQL_INSERIR_METRAGEM = TabelaModeloPOSConexao.atualizarTabela70Metragem();

    static final String SQL = TabelaModeloPOS.criarTabela();
    static final String SQL_CONEXAO = TabelaModeloPOSConexao.criarTabela();

    private Context context;

    public DBModeloPOS(Context context) {
        this.context = context;
    }

    public void salvarModeloPOS(ModeloPOS modelo) {
        TabelaModeloPOS.salvarModeloPOS(context, modelo);
    }

    public List<ModeloPOS> obterModelosPOS() {
        return TabelaModeloPOS.obterModelosPOS(context);
    }

    public void salvarModeloPOSConexao(ModeloPOSConexao conexao) {
        TabelaModeloPOSConexao.salvarConexao(context, conexao);
    }

    public List<ModeloPOSConexao> obterModelosPOSConexoes(int idModeloPos) {
        return TabelaModeloPOSConexao.obterConexoes(context, idModeloPos);
    }

    public ModeloPOSConexao obterTipoConexao(int id, int idModeloPos) {
        return TabelaModeloPOSConexao.obterTipoConexao(context,id, idModeloPos);
    }

    public void deletaTudo() {
        TabelaModeloPOS.deletaTudo(context);
        TabelaModeloPOSConexao.deletaTudo(context);
    }

    public ModeloPOS getById(int id) {
        return TabelaModeloPOS.getById(context, id);
    }

    private static class TabelaModeloPOS {

        private static final String NOME_TABELA = "ModeloPOS";

        private static final String ID_APP = "idAppMobile";
        private static final String ID_TIPO_MAQUINA = "idTipoMaquina";
        private static final String DESCRICAO = "descricao";
        private static final String MODELO = "modelo";
        private static final String VALOR_ALUGUEL_PADRAO = "valorAluguelPadrao";

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] DECIMAL(6, 2))",
                    NOME_TABELA,
                    ID_APP,
                    ID_TIPO_MAQUINA,
                    DESCRICAO,
                    MODELO,
                    VALOR_ALUGUEL_PADRAO
            );
        }

        private static String obterQuerySelect() {
            return String.format("SELECT %s, %s, %s, %s, %s FROM %s",
                    ID_APP,
                    ID_TIPO_MAQUINA,
                    DESCRICAO,
                    MODELO,
                    VALOR_ALUGUEL_PADRAO,
                    NOME_TABELA);
        }

        private static List<ModeloPOS> obterModelosPOS(Context context) {
            List<ModeloPOS> modelos = new ArrayList<>();
            String consulta = obterQuerySelect();

            Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(consulta, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    modelos.add(prepararModeloPOSCursor(context, cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }
            return modelos;
        }

        private static ModeloPOS getById(Context context, int pId) {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("Select idAppMobile, idTipoMaquina, descricao, modelo, valorAluguelPadrao from ModeloPOS");
            sb.appendLine("Where [idAppMobile] = ?");
            Cursor cursor = null;
            try {
                cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), new String[]{String.valueOf(pId)});
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    return prepararModeloPOSCursor(context, cursor);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        private static ModeloPOS prepararModeloPOSCursor(Context context, Cursor cursor) {
            ModeloPOS modeloPOS = new ModeloPOS();
            modeloPOS.setIdAppMobile(cursor.getInt(0));
            modeloPOS.setIdTipoMaquina(cursor.getInt(1));
            modeloPOS.setDescricao(cursor.getString(2));
            modeloPOS.setModelo(cursor.getString(3));
            modeloPOS.setValorAluguelPadrao(cursor.getDouble(4));
            modeloPOS.setConnections(TabelaModeloPOSConexao.obterConexoes(context, modeloPOS.getIdAppMobile()));
            return modeloPOS;
        }

        private static void salvarModeloPOS(Context context, ModeloPOS modeloPOS) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_TIPO_MAQUINA, modeloPOS.getIdTipoMaquina());
            contentValues.put(DESCRICAO, modeloPOS.getDescricao());
            contentValues.put(MODELO, modeloPOS.getModelo());
            contentValues.put(VALOR_ALUGUEL_PADRAO, modeloPOS.getValorAluguelPadrao());

            if (modeloPOS.getIdAppMobile() != null) {
                contentValues.put(ID_APP, modeloPOS.getIdAppMobile());
                SimpleDbHelper.INSTANCE.open(context).replace(NOME_TABELA, null, contentValues);
                return;
            }

            SimpleDbHelper.INSTANCE.open(context).insert(NOME_TABELA, null, contentValues);
        }

        private static void deletaTudo(Context context) {
            SimpleDbHelper.INSTANCE.open(context).delete(NOME_TABELA, null, null);
        }
    }

    private static class TabelaModeloPOSConexao {

        private static final String NOME_TABELA = "ModeloPOSConexao";

        private static final String ID_APP = "idAppMobile";
        private static final String ID_MODELO_POS = "idModeloPos";
        private static final String DESCRICAO = "descricao";
        private static final String OPERADORA = "operadora";
        private static final String METRAGEM_CABO = "metragemCabo";

        private static String atualizarTabela70Operadora() {
            return "ALTER TABLE " + NOME_TABELA + " ADD COLUMN [" + OPERADORA + "] INTEGER DEFAULT 0";
        }

        private static String atualizarTabela70Metragem() {
            return "ALTER TABLE " + NOME_TABELA + " ADD COLUMN [" + METRAGEM_CABO + "] INTEGER DEFAULT 0";
        }

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(30),\n" +
                            "PRIMARY KEY (%s, %s))",
                    NOME_TABELA,
                    ID_APP,
                    ID_MODELO_POS,
                    DESCRICAO,
                    ID_APP,
                    ID_MODELO_POS);
        }

        private static String obterQuerySelect() {
            return String.format("SELECT %s, %s, %s, %s, %s FROM %s",
                    ID_APP,
                    ID_MODELO_POS,
                    DESCRICAO,
                    OPERADORA,
                    METRAGEM_CABO,
                    NOME_TABELA);
        }

        private static List<ModeloPOSConexao> obterConexoes(Context context, int idModeloPos) {
            List<ModeloPOSConexao> conexoes = new ArrayList<>();
            String consulta = obterQuerySelect() + String.format(" WHERE %s = ?", ID_MODELO_POS);

            Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(consulta, new String[]{String.valueOf(idModeloPos)});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    conexoes.add(prepararConexaoCursor(cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }
            return conexoes;
        }

        private static ModeloPOSConexao obterConexao(Context context, int id) {
            String query = String.format("%s WHERE %s = ?", obterQuerySelect(), TabelaModeloPOSConexao.ID_APP);

            Cursor cursor = SimpleDbHelper.INSTANCE
                    .open(context)
                    .rawQuery(query, new String[]{String.valueOf(id)});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ModeloPOSConexao item = prepararConexaoCursor(cursor);
                cursor.close();
                return item;
            }
            return null;
        }

        private static ModeloPOSConexao obterTipoConexao(Context context, int id, int idModeloPos) {
            String query = String.format("%s WHERE %s = ? and %s = ?", obterQuerySelect(), TabelaModeloPOSConexao.ID_APP, TabelaModeloPOSConexao.ID_MODELO_POS);

            Cursor cursor = SimpleDbHelper.INSTANCE
                    .open(context)
                    .rawQuery(query, new String[]{String.valueOf(id), String.valueOf(idModeloPos)});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ModeloPOSConexao item = prepararConexaoCursor(cursor);
                cursor.close();
                return item;
            }
            return null;
        }

        private static ModeloPOSConexao prepararConexaoCursor(Cursor cursor) {
            ModeloPOSConexao conexao = new ModeloPOSConexao();
            conexao.setId(cursor.getInt(cursor.getColumnIndex(TabelaModeloPOSConexao.ID_APP)));
            conexao.setIdModelPos(cursor.getInt(cursor.getColumnIndex(TabelaModeloPOSConexao.ID_MODELO_POS)));
            conexao.setDescription(cursor.getString(cursor.getColumnIndex(TabelaModeloPOSConexao.DESCRICAO)));
            conexao.setCableLength(Util_IO.numberToBoolean(cursor.getInt(cursor.getColumnIndexOrThrow(TabelaModeloPOSConexao.METRAGEM_CABO))));
            conexao.setCarrier(Util_IO.numberToBoolean(cursor.getInt(cursor.getColumnIndexOrThrow(TabelaModeloPOSConexao.OPERADORA))));
            return conexao;
        }

        private static void salvarConexao(Context context, ModeloPOSConexao conexao) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_APP, conexao.getId());
            contentValues.put(ID_MODELO_POS, conexao.getIdModelPos());
            contentValues.put(DESCRICAO, conexao.getDescription());
            contentValues.put(OPERADORA, conexao.isCarrier());
            contentValues.put(METRAGEM_CABO, conexao.isCableLength());

            if (obterConexao(context, conexao.getId()) == null) {
                SimpleDbHelper.INSTANCE.open(context).insert(NOME_TABELA, null, contentValues);
            } else {
                SimpleDbHelper.INSTANCE.open(context).replace(NOME_TABELA, null, contentValues);
            }
        }

        private static void deletaTudo(Context context) {
            SimpleDbHelper.INSTANCE.open(context).delete(NOME_TABELA, null, null);
        }
    }
}
