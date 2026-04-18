package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Bruno Pimentel on 22/01/19.
 */
public class DBProspect {

    static final String SQL = Tabela.SQL;
    private Context context;

    public DBProspect(Context context) {
        this.context = context;
    }

    private static int getInt(Cursor cursor, String coluna) {
        return cursor.getInt(cursor.getColumnIndex(coluna));
    }

    private static String getString(Cursor cursor, String coluna) {
        return cursor.getString(cursor.getColumnIndex(coluna));
    }

    private static Double getDouble(Cursor cursor, String coluna) {
        return cursor.getDouble(cursor.getColumnIndex(coluna));
    }

    public void salvar(RouteClientProspect prospect) {
        if (prospect.getId() != null && pegarPorId(prospect.getId()) != null) {
            atualizar(prospect);
            return;
        }

        ContentValues contentValues = Tabela.converter(prospect);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        Tabela.NOME,
                        null,
                        contentValues
                );
    }

    private void atualizar(RouteClientProspect prospect) {
        ContentValues contentValues = Tabela.converter(prospect);
        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        Tabela.NOME,
                        contentValues,
                        Tabela.FILTRO_PADRAO,
                        new String[]{String.valueOf(prospect.getId())}
                );
    }

    public RouteClientProspect pegarPorId(int id) {
        RouteClientProspect prospect = null;
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            Tabela.NOME,
                            Tabela.COLUNAS,
                            Tabela.FILTRO_PADRAO,
                            new String[]{String.valueOf(id)},
                            null,
                            null,
                            null
                    );

            if (cursor.moveToNext()) {
                prospect = Tabela.converter(cursor);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return prospect;
    }

    public List<RouteClientProspect> pegarTodas() {
        List<RouteClientProspect> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            Tabela.NOME,
                            Tabela.COLUNAS,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

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
        SimpleDbHelper.INSTANCE.open(context).delete(DBProspect.Tabela.NOME, null, null);
    }

    public static class Tabela {
        static final String NOME = "Prospect";

        static final String COLUNA_ID = "id";
        static final String COLUNA_NOME = "nome";
        static final String COLUNA_NOME_FANTASIA = "nome_fantasia";
        static final String COLUNA_CPF_CNPJ = "cpf_cnpj";
        static final String COLUNA_CEP = "cep";
        static final String COLUNA_BAIRRO = "bairro";
        static final String COLUNA_LOGRADOURO = "logradouro";
        static final String COLUNA_NUMERO = "numero";
        static final String COLUNA_COMPLEMENTO = "complemento";
        static final String COLUNA_TIPO_LOGRADOURO = "tipo_logradouro";
        static final String COLUNA_CIDADE = "cidade";
        static final String COLUNA_ESTADO = "estado";
        static final String COLUNA_DDD_TELEFONE = "ddd_telefone";
        static final String COLUNA_TELEFONE = "telefone";
        static final String COLUNA_DDD_CELULAR = "ddd_celular";
        static final String COLUNA_CELULAR = "celular";
        static final String COLUNA_CONTATO = "contato";
        static final String COLUNA_LATITUDE = "latitude";
        static final String COLUNA_LONGITUDE = "longitude";
        static final String COLUNA_EMAIL = "email";
        static final String[] COLUNAS = new String[]{
                COLUNA_ID,
                COLUNA_NOME,
                COLUNA_NOME_FANTASIA,
                COLUNA_CPF_CNPJ,
                COLUNA_CEP,
                COLUNA_BAIRRO,
                COLUNA_LOGRADOURO,
                COLUNA_NUMERO,
                COLUNA_COMPLEMENTO,
                COLUNA_TIPO_LOGRADOURO,
                COLUNA_CIDADE,
                COLUNA_ESTADO,
                COLUNA_DDD_TELEFONE,
                COLUNA_TELEFONE,
                COLUNA_DDD_CELULAR,
                COLUNA_CELULAR,
                COLUNA_CONTATO,
                COLUNA_LATITUDE,
                COLUNA_LONGITUDE,
                COLUNA_EMAIL
        };

        static final String SQL = criarTabela();

        static final String FILTRO_PADRAO = filtroPadrao();

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER PRIMARY KEY AUTOINCREMENT, " + //id
                            "[%s] VARCHAR(255), " + //nome
                            "[%s] VARCHAR(255), " + //nome fantasia
                            "[%s] VARCHAR(20), " + //cpf / cnpj
                            "[%s] VARCHAR(20), " + //cep
                            "[%s] VARCHAR(255), " + //bairro
                            "[%s] VARCHAR(255), " + //logradouro
                            "[%s] VARCHAR(20), " + //numero
                            "[%s] VARCHAR(255), " + //complemento
                            "[%s] VARCHAR(255), " + //tipo logradouro
                            "[%s] VARCHAR(255), " + //cidade
                            "[%s] VARCHAR(2), " + //estado
                            "[%s] VARCHAR(2), " + //ddd telefone
                            "[%s] VARCHAR(20), " + //telefone
                            "[%s] VARCHAR(2), " + //ddd celular
                            "[%s] VARCHAR(20), " + //celular
                            "[%s] VARCHAR(255), " + //contato
                            "[%s] DECIMAL(20, 16), " + //latitude
                            "[%s] DECIMAL(20, 16), " + //longitude
                            "[%s] VARCHAR(255)" + //email
                            ")",
                    NOME,
                    COLUNA_ID,
                    COLUNA_NOME,
                    COLUNA_NOME_FANTASIA,
                    COLUNA_CPF_CNPJ,
                    COLUNA_CEP,
                    COLUNA_BAIRRO,
                    COLUNA_LOGRADOURO,
                    COLUNA_NUMERO,
                    COLUNA_COMPLEMENTO,
                    COLUNA_TIPO_LOGRADOURO,
                    COLUNA_CIDADE,
                    COLUNA_ESTADO,
                    COLUNA_DDD_TELEFONE,
                    COLUNA_TELEFONE,
                    COLUNA_DDD_CELULAR,
                    COLUNA_CELULAR,
                    COLUNA_CONTATO,
                    COLUNA_LATITUDE,
                    COLUNA_LONGITUDE,
                    COLUNA_EMAIL
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID);
        }

        public static RouteClientProspect converter(Cursor cursor) {
            return new RouteClientProspect(
                    getInt(cursor, COLUNA_ID),
                    getString(cursor, COLUNA_NOME),
                    getString(cursor, COLUNA_NOME_FANTASIA),
                    getString(cursor, COLUNA_CPF_CNPJ),
                    getString(cursor, COLUNA_CEP),
                    getString(cursor, COLUNA_BAIRRO),
                    getString(cursor, COLUNA_LOGRADOURO),
                    getString(cursor, COLUNA_NUMERO),
                    getString(cursor, COLUNA_COMPLEMENTO),
                    getString(cursor, COLUNA_TIPO_LOGRADOURO),
                    getString(cursor, COLUNA_CIDADE),
                    getString(cursor, COLUNA_ESTADO),
                    getString(cursor, COLUNA_DDD_TELEFONE),
                    getString(cursor, COLUNA_TELEFONE),
                    getString(cursor, COLUNA_DDD_CELULAR),
                    getString(cursor, COLUNA_CELULAR),
                    getString(cursor, COLUNA_CONTATO),
                    getDouble(cursor, COLUNA_LATITUDE),
                    getDouble(cursor, COLUNA_LONGITUDE),
                    getString(cursor, COLUNA_EMAIL)
            );
        }

        public static ContentValues converter(RouteClientProspect prospect) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID, prospect.getId());
            values.put(COLUNA_NOME, prospect.getNameFull());
            values.put(COLUNA_NOME_FANTASIA, prospect.getNameFantasy());
            values.put(COLUNA_CPF_CNPJ, prospect.getCpfCnpj());
            values.put(COLUNA_CEP, prospect.getPostalCode());
            values.put(COLUNA_BAIRRO, prospect.getNeighborhood());
            values.put(COLUNA_LOGRADOURO, prospect.getAddressName());
            values.put(COLUNA_NUMERO, prospect.getAddressNumber());
            values.put(COLUNA_COMPLEMENTO, prospect.getAddressComplement());
            values.put(COLUNA_TIPO_LOGRADOURO, prospect.getAddressType());
            values.put(COLUNA_CIDADE, prospect.getCity());
            values.put(COLUNA_ESTADO, prospect.getFederalState());
            values.put(COLUNA_DDD_TELEFONE, prospect.getDddTelephone());
            values.put(COLUNA_TELEFONE, prospect.getTelephone());
            values.put(COLUNA_DDD_CELULAR, prospect.getDddCellphone());
            values.put(COLUNA_CELULAR, prospect.getCellphone());
            values.put(COLUNA_CONTATO, prospect.getContact());
            values.put(COLUNA_LATITUDE, prospect.getLatitude());
            values.put(COLUNA_LONGITUDE, prospect.getLongitude());
            values.put(COLUNA_EMAIL, prospect.getEmail());

            return values;
        }
    }
}
