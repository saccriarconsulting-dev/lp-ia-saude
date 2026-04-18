package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddress;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * @author Rogério Massa on 07/02/19.
 */

public class DBClienteCadastroEndereco {

    static final String SQL = DBClienteCadastroEndereco.Tabela.criarTabela();
    private Context context;

    public DBClienteCadastroEndereco(Context context) {
        this.context = context;
    }

    public void salvar(CustomerRegisterAddress address) {
        if (address.getId() != null && pegarPorId(address.getId()) != null) {
            atualizar(address);
            return;
        }

        ContentValues contentValues = DBClienteCadastroEndereco.Tabela.converter(address);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        DBClienteCadastroEndereco.Tabela.NOME,
                        null,
                        contentValues
                );
    }

    private void atualizar(CustomerRegisterAddress address) {
        ContentValues contentValues = DBClienteCadastroEndereco.Tabela.converter(address);
        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        DBClienteCadastroEndereco.Tabela.NOME,
                        contentValues,
                        DBClienteCadastroEndereco.Tabela.filtroPadrao(),
                        new String[]{String.valueOf(address.getId())}
                );
    }

    public CustomerRegisterAddress pegarPorId(int id) {
        CustomerRegisterAddress reason = null;
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            DBClienteCadastroEndereco.Tabela.NOME,
                            DBClienteCadastroEndereco.Tabela.COLUNAS,
                            DBClienteCadastroEndereco.Tabela.filtroPadrao(),
                            new String[]{String.valueOf(id)},
                            null,
                            null,
                            null
                    );

            while (cursor.moveToNext()) {
                reason = DBClienteCadastroEndereco.Tabela.converter(cursor);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return reason;
    }

    public ArrayList<CustomerRegisterAddress> pegarTodas(int idCustomer) {
        ArrayList<CustomerRegisterAddress> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            DBClienteCadastroEndereco.Tabela.NOME,
                            DBClienteCadastroEndereco.Tabela.COLUNAS,
                            String.format("[%s] = ? AND [%s] = ?",
                                    DBClienteCadastroEndereco.Tabela.COLUNA_ID_CLIENTE,
                                    DBClienteCadastroEndereco.Tabela.COLUNA_ATIVO),
                            new String[]{String.valueOf(idCustomer), String.valueOf(1)},
                            null,
                            null,
                            null
                    );

            while (cursor.moveToNext()) {
                lista.add(DBClienteCadastroEndereco.Tabela.converter(cursor));
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return lista;
    }

    public void atualizarSync(int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tabela.COLUNA_SYNC, 1);

        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        DBClienteCadastroEndereco.Tabela.NOME,
                        contentValues,
                        DBClienteCadastroEndereco.Tabela.filtroPadrao(),
                        new String[]{String.valueOf(id)});
    }

    public void atualizarIdServer(String id, String idServer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tabela.COLUNA_ID_SERVER, idServer);

        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        DBClienteCadastroEndereco.Tabela.NOME,
                        contentValues,
                        DBClienteCadastroEndereco.Tabela.filtroPadrao(),
                        new String[]{String.valueOf(id)});
    }

    public void deletaTudo() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBClienteCadastroEndereco.Tabela.NOME, null, null);
    }

    private static class Tabela {

        static final String NOME = "ClienteCadastroEndereco";

        static final String COLUNA_ID_APP = "id";
        static final String COLUNA_ID_SERVER = "id_server";
        static final String COLUNA_ID_CLIENTE = "id_cliente";
        static final String COLUNA_ID_VENDEDOR = "id_vendedor";
        static final String COLUNA_ID_TIPO_ENDERECO = "id_tipo_endereco";
        static final String COLUNA_ATIVO = "ativo";
        static final String COLUNA_CPF_CNPJ = "cpf_cnpj";
        static final String COLUNA_CEP = "cep";
        static final String COLUNA_TIPO_LOGRADOURO = "tipo_logradouro";
        static final String COLUNA_LOGRADOURO = "logradouro";
        static final String COLUNA_BAIRRO = "bairro";
        static final String COLUNA_NUMERO = "numero_logradouro";
        static final String COLUNA_COMPLEMENTO = "complemento_logradouro";
        static final String COLUNA_CIDADE = "cidade";
        static final String COLUNA_UF = "UF";
        static final String COLUNA_NOME_CONTATO = "nome_contato";
        static final String COLUNA_TELEFONE = "tel_residencial";
        static final String COLUNA_CELULAR = "tel_celular";
        static final String COLUNA_EMAIL = "email";
        static final String COLUNA_SYNC = "sync";

        static final String[] COLUNAS = new String[]{
                COLUNA_ID_APP,
                COLUNA_ID_SERVER,
                COLUNA_ID_CLIENTE,
                COLUNA_ID_VENDEDOR,
                COLUNA_ID_TIPO_ENDERECO,
                COLUNA_ATIVO,
                COLUNA_CPF_CNPJ,
                COLUNA_CEP,
                COLUNA_TIPO_LOGRADOURO,
                COLUNA_LOGRADOURO,
                COLUNA_BAIRRO,
                COLUNA_NUMERO,
                COLUNA_COMPLEMENTO,
                COLUNA_CIDADE,
                COLUNA_UF,
                COLUNA_NOME_CONTATO,
                COLUNA_TELEFONE,
                COLUNA_CELULAR,
                COLUNA_EMAIL,
                COLUNA_SYNC
        };

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER PRIMARY KEY ," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] VARCHAR(20)," +
                            "[%s] VARCHAR(10)," +
                            "[%s] VARCHAR(100)," +
                            "[%s] VARCHAR(200)," +
                            "[%s] VARCHAR(100)," +
                            "[%s] VARCHAR(5)," +
                            "[%s] VARCHAR(200)," +
                            "[%s] VARCHAR(100)," +
                            "[%s] VARCHAR(5)," +
                            "[%s] VARCHAR(200)," +
                            "[%s] VARCHAR(20)," +
                            "[%s] VARCHAR(20)," +
                            "[%s] VARCHAR(100)," +
                            "[%s] INTEGER" +
                            ")",
                    NOME,
                    COLUNA_ID_APP,
                    COLUNA_ID_SERVER,
                    COLUNA_ID_CLIENTE,
                    COLUNA_ID_VENDEDOR,
                    COLUNA_ID_TIPO_ENDERECO,
                    COLUNA_ATIVO,
                    COLUNA_CPF_CNPJ,
                    COLUNA_CEP,
                    COLUNA_TIPO_LOGRADOURO,
                    COLUNA_LOGRADOURO,
                    COLUNA_BAIRRO,
                    COLUNA_NUMERO,
                    COLUNA_COMPLEMENTO,
                    COLUNA_CIDADE,
                    COLUNA_UF,
                    COLUNA_NOME_CONTATO,
                    COLUNA_TELEFONE,
                    COLUNA_CELULAR,
                    COLUNA_EMAIL,
                    COLUNA_SYNC
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID_APP);
        }

        private static ContentValues converter(CustomerRegisterAddress reason) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_APP, reason.getId());
            values.put(COLUNA_ID_SERVER, reason.getIdServer());
            values.put(COLUNA_ID_CLIENTE, reason.getIdCustomer());
            values.put(COLUNA_ID_VENDEDOR, reason.getIdSalesman());
            values.put(COLUNA_ID_TIPO_ENDERECO, reason.getIdAddressType());
            values.put(COLUNA_CPF_CNPJ, reason.getCpfCnpj());
            values.put(COLUNA_CEP, reason.getPostalCode());
            values.put(COLUNA_TIPO_LOGRADOURO, Util_IO.trataString(reason.getAddressType()));
            values.put(COLUNA_LOGRADOURO, Util_IO.trataString(reason.getAddressName()));
            values.put(COLUNA_BAIRRO, Util_IO.trataString(reason.getNeighborhood()));
            values.put(COLUNA_NUMERO, Util_IO.trataString(reason.getAddressNumber()));
            values.put(COLUNA_COMPLEMENTO, Util_IO.trataString(reason.getAddressComplement()));
            values.put(COLUNA_CIDADE, Util_IO.trataString(reason.getCity()));
            values.put(COLUNA_UF, Util_IO.trataString(reason.getFederalState()));
            values.put(COLUNA_NOME_CONTATO, StringUtils.isNotEmpty(reason.getContactName())
                    ? Util_IO.trataString(reason.getContactName())
                    : reason.getContactName());
            values.put(COLUNA_TELEFONE, forceMaskField(reason.getPhoneNumber()));
            values.put(COLUNA_CELULAR, forceMaskField(reason.getCellphone()));
            values.put(COLUNA_EMAIL, reason.getEmail());
            values.put(COLUNA_ATIVO, reason.getId() == null
                    ? 1
                    : Util_IO.booleanToNumber(reason.isActivate()));
            values.put(Tabela.COLUNA_SYNC, 0);
            return values;
        }

        private static String forceMaskField(String phone) {
            if (phone == null) {
                return null;
            }

            final String cleaned = StringUtils.returnOnlyNumbers(phone);

            if (cleaned.length() == StringUtils.PHONE_LENGTH) {
                return StringUtils.maskPhone(phone);
            }

            return StringUtils.maskCellPhone(phone);
        }

        private static CustomerRegisterAddress converter(Cursor cursor) {
            return new CustomerRegisterAddress(
                    getInt(cursor, COLUNA_ID_APP),
                    getInteger(cursor, COLUNA_ID_SERVER),
                    getInt(cursor, COLUNA_ID_CLIENTE),
                    getInt(cursor, COLUNA_ID_VENDEDOR),
                    getInt(cursor, COLUNA_ID_TIPO_ENDERECO),
                    getString(cursor, COLUNA_CPF_CNPJ),
                    getString(cursor, COLUNA_CEP),
                    getString(cursor, COLUNA_TIPO_LOGRADOURO),
                    getString(cursor, COLUNA_LOGRADOURO),
                    getString(cursor, COLUNA_BAIRRO),
                    getString(cursor, COLUNA_NUMERO),
                    getString(cursor, COLUNA_COMPLEMENTO),
                    getString(cursor, COLUNA_UF),
                    getString(cursor, COLUNA_CIDADE),
                    getString(cursor, COLUNA_NOME_CONTATO),
                    getString(cursor, COLUNA_TELEFONE),
                    getString(cursor, COLUNA_CELULAR),
                    getString(cursor, COLUNA_EMAIL),
                    Util_IO.numberToBoolean(getInt(cursor, COLUNA_ATIVO)),
                    getInt(cursor, COLUNA_SYNC));
        }

        private static int getInt(Cursor cursor, String coluna) {
            return cursor.getInt(cursor.getColumnIndex(coluna));
        }

        private static Integer getInteger(Cursor cursor, String coluna) {
            int integer = cursor.getInt(cursor.getColumnIndex(coluna));
            return integer == 0 ? null : integer;
        }

        private static String getString(Cursor cursor, String coluna) {
            return cursor.getString(cursor.getColumnIndex(coluna));
        }
    }
}
