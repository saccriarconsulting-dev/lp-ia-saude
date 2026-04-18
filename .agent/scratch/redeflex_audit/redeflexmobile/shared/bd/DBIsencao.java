package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterExemption;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Rogério Massa on 06/02/19.
 */

public class DBIsencao {

    public static final String UPDATE_70_SQL_INSERIR_QTDE_MESES_STRING = Tabela.atualizarTabela70QtdeMesesString();
    static final String SQL = DBIsencao.Tabela.criarTabela();
    private Context context;

    public DBIsencao(Context context) {
        this.context = context;
    }

    public void salvar(CustomerRegisterExemption reason) {
        if (reason.getId() != null && pegarPorId(reason.getId()) != null) {
            atualizar(reason);
            return;
        }

        ContentValues contentValues = Tabela.converter(reason);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        Tabela.NOME,
                        null,
                        contentValues
                );
    }

    private void atualizar(CustomerRegisterExemption reason) {
        ContentValues contentValues = Tabela.converter(reason);
        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        Tabela.NOME,
                        contentValues,
                        Tabela.filtroPadrao(),
                        new String[]{String.valueOf(reason.getId())}
                );
    }

    private CustomerRegisterExemption pegarPorId(int id) {
        CustomerRegisterExemption reason = null;
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            Tabela.NOME,
                            Tabela.COLUNAS,
                            Tabela.filtroPadrao(),
                            new String[]{String.valueOf(id)},
                            null,
                            null,
                            null
                    );

            while (cursor.moveToNext()) {
                reason = Tabela.converter(cursor);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return reason;
    }

    public List<CustomerRegisterExemption> pegarTodas() {
        List<CustomerRegisterExemption> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            Tabela.NOME,
                            Tabela.COLUNAS,
                            String.format("[%s] = ?", Tabela.COLUNA_ATIVO),
                            new String[]{String.valueOf(1)},
                            null,
                            null,
                            null);

            while (cursor.moveToNext()) {
                lista.add(Tabela.converter(cursor));
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public void deletaTudo() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBIsencao.Tabela.NOME, null, null);
    }

    private static class Tabela {

        static final String NOME = "Isencao";

        static final String COLUNA_ID_APP = "id";
        static final String COLUNA_QTDE_MESES = "qtde_meses";
        static final String COLUNA_ATIVO = "ativo";
        static final String COLUNA_QTDE_MESES_STRING = "qtde_meses_string";

        static final String[] COLUNAS = new String[]{
                COLUNA_ID_APP,
                COLUNA_QTDE_MESES_STRING,
                COLUNA_ATIVO
        };

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER)",
                    NOME,
                    COLUNA_ID_APP,
                    COLUNA_QTDE_MESES,
                    COLUNA_ATIVO
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID_APP);
        }

        private static ContentValues converter(CustomerRegisterExemption reason) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_APP, reason.getId());
            values.put(COLUNA_QTDE_MESES_STRING, reason.getMonthCount());
            values.put(COLUNA_ATIVO, Util_IO.booleanToNumber(reason.isActivate()));
            return values;
        }

        private static CustomerRegisterExemption converter(Cursor cursor) {
            return new CustomerRegisterExemption(
                    cursor.getInt(cursor.getColumnIndex(COLUNA_ID_APP)),
                    cursor.getString(cursor.getColumnIndex(COLUNA_QTDE_MESES_STRING)),
                    Util_IO.numberToBoolean(cursor.getInt(cursor.getColumnIndex(COLUNA_ATIVO)))
            );
        }

        static String atualizarTabela70QtdeMesesString() {
            return "ALTER TABLE " + NOME + " ADD COLUMN [" + COLUNA_QTDE_MESES_STRING + "] VARCHAR(50);";
        }
    }
}
