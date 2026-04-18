package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterTax;

import java.util.ArrayList;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.bd.DBClienteCadastroTaxa.Tabela.COLUNA_ID_BANDEIRA;
import static com.axys.redeflexmobile.shared.bd.DBClienteCadastroTaxa.Tabela.COLUNA_ID_CLIENTE;

/**
 * @author Rogério Massa on 07/02/19.
 */

public class DBClienteCadastroTaxa {

    public static final String UPDATE_70_SQL_INSERIR_ANTECIPACAO = Tabela.atualizarTabela70Antecipacao();
    static final String SQL = DBClienteCadastroTaxa.Tabela.criarTabela();
    private Context context;

    public DBClienteCadastroTaxa(Context context) {
        this.context = context;
    }

    public void salvar(CustomerRegisterTax tax) {
        if (tax.getId() != null && pegarPorId(tax.getId()) != null) {
            atualizar(tax);
            return;
        }

        ContentValues contentValues = DBClienteCadastroTaxa.Tabela.converter(tax);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        DBClienteCadastroTaxa.Tabela.NOME,
                        null,
                        contentValues
                );
    }

    private void atualizar(CustomerRegisterTax tax) {
        ContentValues contentValues = DBClienteCadastroTaxa.Tabela.converter(tax);
        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        DBClienteCadastroTaxa.Tabela.NOME,
                        contentValues,
                        String.format("[%s] = ? AND [%s] = ?", COLUNA_ID_CLIENTE, COLUNA_ID_BANDEIRA),
                        new String[]{String.valueOf(tax.getIdCustomerRegister()), String.valueOf(tax.getFlag())}
                );
    }

    public CustomerRegisterTax pegarPorId(int id) {
        CustomerRegisterTax reason = null;
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .query(
                        Tabela.NOME,
                        Tabela.COLUNAS,
                        Tabela.filtroPadrao(),
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null
                )) {

            while (cursor.moveToNext()) {
                reason = Tabela.converter(cursor);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return reason;
    }

    public ArrayList<CustomerRegisterTax> pegarTodas(int idCustomer) {
        ArrayList<CustomerRegisterTax> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .query(
                        Tabela.NOME,
                        Tabela.COLUNAS,
                        String.format("[%s] = ?", COLUNA_ID_CLIENTE),
                        new String[]{String.valueOf(idCustomer)},
                        null,
                        null,
                        null
                )) {

            while (cursor.moveToNext()) {
                lista.add(Tabela.converter(cursor));
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return lista;
    }

    public void deletaTudo() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBClienteCadastroTaxa.Tabela.NOME, null, null);
    }

    public void deletaTudoPorId(long idClienteCadastro) {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBClienteCadastroTaxa.Tabela.NOME,
                        String.format("[%s] = ?", COLUNA_ID_CLIENTE),
                        new String[]{String.valueOf(idClienteCadastro)}
                );
    }

    static class Tabela {

        static final String NOME = "ClienteCadastroTaxa";

        static final String COLUNA_ID_APP = "id";
        static final String COLUNA_ID_CLIENTE = "id_cliente";
        static final String COLUNA_ID_BANDEIRA = "id_bandeira";
        static final String COLUNA_DEBITO = "debito";
        static final String COLUNA_CREDITO = "credito";
        static final String COLUNA_CREDITO_ATE_SEIS = "credito_ate_seis";
        static final String COLUNA_CREDITO_MAIOR_QUE_SEIS = "credito_maior_que_seis";
        static final String COLUNA_ANTECIPACAO = "antecipacao";

        //region Campos removidos
        static final String COLUNA_DEBITO_MIN = "debito_min";
        static final String COLUNA_DEBITO_MAX = "debito_max";
        static final String COLUNA_CREDITO_MIN = "credito_min";
        static final String COLUNA_CREDITO_MAX = "credito_max";
        static final String COLUNA_CREDITO_ATE_SEIS_MIN = "credito_ate_seis_min";
        static final String COLUNA_CREDITO_ATE_SEIS_MAX = "credito_ate_seis_max";
        static final String COLUNA_CREDITO_MAIOR_QUE_SEIS_MIN = "credito_maior_que_seis_min";
        static final String COLUNA_CREDITO_MAIOR_QUE_SEIS_MAX = "credito_maior_que_seis_max";
        //endregion

        static final String[] COLUNAS = new String[]{
                COLUNA_ID_APP,
                COLUNA_ID_CLIENTE,
                COLUNA_ID_BANDEIRA,
                COLUNA_DEBITO,
                COLUNA_DEBITO_MIN,
                COLUNA_DEBITO_MAX,
                COLUNA_CREDITO,
                COLUNA_CREDITO_MIN,
                COLUNA_CREDITO_MAX,
                COLUNA_CREDITO_ATE_SEIS,
                COLUNA_CREDITO_ATE_SEIS_MIN,
                COLUNA_CREDITO_ATE_SEIS_MAX,
                COLUNA_CREDITO_MAIOR_QUE_SEIS,
                COLUNA_CREDITO_MAIOR_QUE_SEIS_MIN,
                COLUNA_CREDITO_MAIOR_QUE_SEIS_MAX,
                COLUNA_ANTECIPACAO
        };

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)" +
                            ")",
                    NOME,
                    COLUNA_ID_APP,
                    COLUNA_ID_CLIENTE,
                    COLUNA_ID_BANDEIRA,
                    COLUNA_DEBITO,
                    COLUNA_DEBITO_MIN,
                    COLUNA_DEBITO_MAX,
                    COLUNA_CREDITO,
                    COLUNA_CREDITO_MIN,
                    COLUNA_CREDITO_MAX,
                    COLUNA_CREDITO_ATE_SEIS,
                    COLUNA_CREDITO_ATE_SEIS_MIN,
                    COLUNA_CREDITO_ATE_SEIS_MAX,
                    COLUNA_CREDITO_MAIOR_QUE_SEIS,
                    COLUNA_CREDITO_MAIOR_QUE_SEIS_MIN,
                    COLUNA_CREDITO_MAIOR_QUE_SEIS_MAX

            );
        }

        static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID_APP);
        }

        private static ContentValues converter(CustomerRegisterTax reason) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_APP, reason.getId());
            values.put(COLUNA_ID_CLIENTE, reason.getIdCustomerRegister());
            values.put(COLUNA_ID_BANDEIRA, reason.getFlag());
            values.put(COLUNA_DEBITO, reason.getDebit());
            values.put(COLUNA_CREDITO, reason.getCredit());
            values.put(COLUNA_CREDITO_ATE_SEIS, reason.getUntilSix());
            values.put(COLUNA_CREDITO_MAIOR_QUE_SEIS, reason.getBiggerSix());
            values.put(COLUNA_ANTECIPACAO, reason.getAnticipation());
            return values;
        }

        private static CustomerRegisterTax converter(Cursor cursor) {
            return new CustomerRegisterTax(
                    cursor.getInt(cursor.getColumnIndex(COLUNA_ID_APP)),
                    getInteger(cursor, COLUNA_ID_CLIENTE),
                    getInteger(cursor, COLUNA_ID_BANDEIRA),
                    getDouble(cursor, COLUNA_DEBITO),
                    getDouble(cursor, COLUNA_CREDITO),
                    getDouble(cursor, COLUNA_CREDITO_ATE_SEIS),
                    getDouble(cursor, COLUNA_CREDITO_MAIOR_QUE_SEIS),
                    getDouble(cursor, COLUNA_ANTECIPACAO)
            );
        }

        private static Integer getInteger(Cursor cursor, String coluna) {
            int integer = cursor.getInt(cursor.getColumnIndex(coluna));
            return integer == 0 ? null : integer;
        }

        private static Double getDouble(Cursor cursor, String coluna) {
            double value = cursor.getDouble(cursor.getColumnIndex(coluna));
            return value > 0 ? value : null;
        }

        private static String atualizarTabela70Antecipacao() {
            return "ALTER TABLE " + NOME + " ADD COLUMN [" + COLUNA_ANTECIPACAO + "] DECIMAL(20, 2)";
        }
    }
}
