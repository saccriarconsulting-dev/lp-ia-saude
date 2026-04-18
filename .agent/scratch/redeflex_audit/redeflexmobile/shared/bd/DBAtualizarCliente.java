package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.axys.redeflexmobile.shared.enums.EnumAtualizarCliente;
import com.axys.redeflexmobile.shared.models.AtualizaCliente;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class DBAtualizarCliente {

    public static final String SQL = Tabela.criarTabela();
    public static final String SQL_ALTER_69 = Tabela.atualizarTabela69();

    public static final int SINCRONIZADO = 1;
    private final Context context;

    public DBAtualizarCliente(Context context) {
        this.context = context;
    }

    public void salvar(AtualizaCliente cliente) {
        ContentValues values = Tabela.converter(cliente);
        String id = String.valueOf(cliente.getId());
        if (isExist(id)) {
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

    public void atualizarStatusConcluido(String id) {
        if (isExist(id)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(Tabela.STATUS, EnumAtualizarCliente.CONCLUIDO.getValue());
        String[] filtro = new String[]{id};

        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        db.update(Tabela.TABELA, values, Tabela.filtroPadrao(), filtro);
    }

    public AtualizaCliente obterPorId(String id) {
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
        } catch (Exception e) {
            Timber.e(e);
        }

        return null;
    }

    public List<AtualizaCliente> obterTodosClientesAlterados() {
        List<AtualizaCliente> lista = new ArrayList<>();
        Cursor cursor = null;
        try {

            String query = "SELECT " + Tabela.ID +
                    ", " + Tabela.NOME_FANTASIA +
                    ", " + Tabela.RAZAO_SOCIAL +
                    ", " + Tabela.NOME_CONTATO +
                    ", " + Tabela.DDD_TELEFONE +
                    ", " + Tabela.TELEFONE +
                    ", " + Tabela.DDD_CELULAR +
                    ", " + Tabela.CELULAR +
                    ", " + Tabela.TIPO_LOGRADOURO +
                    ", " + Tabela.NOME_LOGRADOURO +
                    ", " + Tabela.NUMERO_LOGRADOURO +
                    ", " + Tabela.COMPLEMENTO_LOGRADOURO +
                    ", " + Tabela.CIDADE +
                    ", " + Tabela.BAIRRO +
                    ", " + Tabela.ESTADO +
                    ", " + Tabela.CEP +
                    ", " + Tabela.ID_SEGMENTO +
                    ", " + Tabela.PONTO_REFERENCIA +
                    ", " + Tabela.EMAIL +
                    ", " + Tabela.ID_VENDEDOR +
                    ", " + Tabela.DATA_HORA +
                    " FROM " + Tabela.TABELA +
                    " WHERE " + Tabela.SYNC + " = ? AND " +
                    Tabela.STATUS + " = " + EnumAtualizarCliente.CONCLUIDO.getValue();

            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(
                            query,
                            new String[]{String.valueOf(Tabela.NAO_SINCRONIZADO)}
                    );
            AtualizaCliente atualizaCliente;
            if (cursor.moveToFirst()) {
                do {
                    atualizaCliente = new AtualizaCliente();

                    atualizaCliente.setId(cursor.getString(0));
                    atualizaCliente.setNomeFantasia(cursor.getString(1));
                    atualizaCliente.setRazaoSocial(cursor.getString(2));
                    atualizaCliente.setNomeContato(cursor.getString(3));
                    atualizaCliente.setDddTelefone(cursor.getString(4));
                    atualizaCliente.setTelefone(cursor.getString(5));
                    atualizaCliente.setDddCelular(cursor.getString(6));
                    atualizaCliente.setCelular(cursor.getString(7));
                    atualizaCliente.setTipoLogradouro(cursor.getString(8));
                    atualizaCliente.setNomeLogradouro(cursor.getString(9));
                    atualizaCliente.setNumeroLogradouro(cursor.getString(10));
                    atualizaCliente.setComplementoLogradouro(cursor.getString(11));
                    atualizaCliente.setCidade(cursor.getString(12));
                    atualizaCliente.setBairro(cursor.getString(13));
                    atualizaCliente.setEstado(cursor.getString(14));
                    atualizaCliente.setCep(cursor.getString(15));
                    atualizaCliente.setSegmento(cursor.getString(16));
                    atualizaCliente.setPontoReferencia(cursor.getString(17));
                    atualizaCliente.setEmail(cursor.getString(18));
                    atualizaCliente.setIdVendedor(cursor.getString(19));
                    atualizaCliente.setDataHora(Util_IO.stringToDate(cursor.getString(20), Config.FormatDateTimeStringBanco));

                    lista.add(atualizaCliente);
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

    private boolean isExist(String id) {

        try (Cursor cursor = SimpleDbHelper.INSTANCE
                .open(context)
                .query(
                        Tabela.TABELA,
                        Tabela.CAMPOS,
                        Tabela.filtroPadrao(),
                        new String[]{id},
                        null,
                        null,
                        null
                )) {
            return (cursor == null || cursor.getCount() <= 0);
        }
    }

    /*public void excluirClientesAprovados(List<Integer> listaClientesAprovados) {
        for (int i = 0; i < listaClientesAprovados.size(); i++) {
            SimpleDbHelper.INSTANCE.open(context).delete(Tabela.TABELA, "[id]=? AND [sync] =? ", new String[]{String.valueOf(listaClientesAprovados.get(i)), String.valueOf(SINCRONIZADO) });
        }
    }*/

    public void atualizarSync(String id) {
        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        ContentValues values = new ContentValues();
        values.put(Tabela.SYNC, SINCRONIZADO);
        db.update(Tabela.TABELA, values, Tabela.filtroPadrao(), new String[]{id});
    }

    /*public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(Tabela.TABELA, null, null);
    }*/

    private static class Tabela {

        static final String TABELA = "ClienteAtualizar";
        static final String ID = "id";
        static final String NOME_FANTASIA = "nomeFantasia";
        static final String RAZAO_SOCIAL = "razaoSocial";
        static final String NOME_CONTATO = "nomeContato";
        static final String DDD_TELEFONE = "dddFixo";
        static final String TELEFONE = "telefone";
        static final String DDD_CELULAR = "dddCelular";
        static final String CELULAR = "celular";
        static final String TIPO_LOGRADOURO = "tipoLogradouro";
        static final String NOME_LOGRADOURO = "nomeLogradouro";
        static final String NUMERO_LOGRADOURO = "numeroLogradouro";
        static final String COMPLEMENTO_LOGRADOURO = "complementoLogradouro";
        static final String CIDADE = "cidade";
        static final String BAIRRO = "bairro";
        static final String ESTADO = "estado";
        static final String CEP = "cep";
        static final String ID_SEGMENTO = "idSegmentoSGV";
        static final String PONTO_REFERENCIA = "pontoReferencia";
        static final String EMAIL = "email";
        static final String ID_VENDEDOR = "idVendedor";
        static final String DATA_HORA = "dataHora";
        static final String SYNC = "sync";
        static final String STATUS = "status";

        static final int NAO_SINCRONIZADO = 0;

        static final String[] CAMPOS = {
                ID,
                NOME_FANTASIA,
                RAZAO_SOCIAL,
                NOME_CONTATO,
                DDD_TELEFONE,
                TELEFONE,
                DDD_CELULAR,
                CELULAR,
                TIPO_LOGRADOURO,
                NOME_LOGRADOURO,
                NUMERO_LOGRADOURO,
                COMPLEMENTO_LOGRADOURO,
                CIDADE,
                BAIRRO,
                ESTADO,
                CEP,
                ID_SEGMENTO,
                PONTO_REFERENCIA,
                EMAIL,
                ID_VENDEDOR,
                DATA_HORA,
                STATUS
        };

        static ContentValues converter(AtualizaCliente cliente) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(ID, cliente.getId());
            contentValues.put(NOME_FANTASIA, cliente.getNomeFantasia());
            contentValues.put(RAZAO_SOCIAL, cliente.getRazaoSocial());
            contentValues.put(NOME_CONTATO, cliente.getNomeContato());
            contentValues.put(DDD_TELEFONE, cliente.getDddTelefone());
            contentValues.put(TELEFONE, cliente.getTelefone());
            contentValues.put(DDD_CELULAR, cliente.getDddCelular());
            contentValues.put(CELULAR, cliente.getCelular());
            contentValues.put(TIPO_LOGRADOURO, cliente.getTipoLogradouro());
            contentValues.put(NOME_LOGRADOURO, cliente.getNomeLogradouro());
            contentValues.put(NUMERO_LOGRADOURO, cliente.getNumeroLogradouro());
            contentValues.put(COMPLEMENTO_LOGRADOURO, cliente.getComplementoLogradouro());
            contentValues.put(CIDADE, cliente.getCidade());
            contentValues.put(BAIRRO, cliente.getBairro());
            contentValues.put(ESTADO, cliente.getEstado());
            contentValues.put(CEP, cliente.getCep());
            contentValues.put(ID_SEGMENTO, cliente.getSegmento());
            contentValues.put(PONTO_REFERENCIA, cliente.getPontoReferencia());
            contentValues.put(EMAIL, cliente.getEmail());
            contentValues.put(ID_VENDEDOR, cliente.getIdVendedor());
            contentValues.put(DATA_HORA, Util_IO.dateTimeToString(cliente.getDataHora(), Config.FormatDateTimeStringBanco));
            contentValues.put(SYNC, NAO_SINCRONIZADO);
            contentValues.put(STATUS, cliente.getStatus());

            return contentValues;
        }

        static AtualizaCliente converter(Cursor cursor) {
            AtualizaCliente cliente = new AtualizaCliente();

            cliente.setId(getString(cursor, ID));
            cliente.setNomeFantasia(getString(cursor, NOME_FANTASIA));
            cliente.setRazaoSocial(getString(cursor, RAZAO_SOCIAL));
            cliente.setNomeContato(getString(cursor, NOME_CONTATO));
            cliente.setDddTelefone(getString(cursor, DDD_TELEFONE));
            cliente.setTelefone(getString(cursor, TELEFONE));
            cliente.setDddCelular(getString(cursor, DDD_CELULAR));
            cliente.setCelular(getString(cursor, CELULAR));
            cliente.setTipoLogradouro(getString(cursor, TIPO_LOGRADOURO));
            cliente.setNomeLogradouro(getString(cursor, NOME_LOGRADOURO));
            cliente.setNumeroLogradouro(getString(cursor, NUMERO_LOGRADOURO));
            cliente.setComplementoLogradouro(getString(cursor, COMPLEMENTO_LOGRADOURO));
            cliente.setCidade(getString(cursor, CIDADE));
            cliente.setBairro(getString(cursor, BAIRRO));
            cliente.setEstado(getString(cursor, ESTADO));
            cliente.setCep(getString(cursor, CEP));
            cliente.setSegmento(getString(cursor, ID_SEGMENTO));
            cliente.setPontoReferencia(getString(cursor, PONTO_REFERENCIA));
            cliente.setEmail(getString(cursor, EMAIL));
            cliente.setIdVendedor(getString(cursor, ID_VENDEDOR));
            cliente.setDataHora(Util_IO.stringToDate(getString(cursor, DATA_HORA), Config.FormatDateTimeStringBanco));
            cliente.setStatus(getInt(cursor, STATUS));

            return cliente;
        }

        static String filtroPadrao() {
            return ID + " = ?";
        }

        private static String getString(Cursor cursor, String coluna) {
            return cursor.getString(cursor.getColumnIndex(coluna));
        }

        private static int getInt(Cursor cursor, String coluna) {
            return cursor.getInt(cursor.getColumnIndex(coluna));
        }

        static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] VARCHAR(20),\n" +
                            "[%s] VARCHAR(200),\n" +
                            "[%s] VARCHAR(200),\n" +
                            "[%s] VARCHAR(200),\n" +
                            "[%s] VARCHAR(2),\n" +
                            "[%s] VARCHAR(10),\n" +
                            "[%s] VARCHAR(2),\n" +
                            "[%s] VARCHAR(10),\n" +
                            "[%s] VARCHAR(50),\n" +
                            "[%s] VARCHAR(200),\n" +
                            "[%s] VARCHAR(20),\n" +
                            "[%s] VARCHAR(200),\n" +
                            "[%s] VARCHAR(100),\n" +
                            "[%s] VARCHAR(100),\n" +
                            "[%s] VARCHAR(50),\n" +
                            "[%s] VARCHAR(10),\n" +
                            "[%s] VARCHAR(5),\n" +
                            "[%s] VARCHAR(100),\n" +
                            "[%s] VARCHAR(100),\n" +
                            "[%s] VARCHAR(100),\n" +
                            "[%s] VARCHAR(100),\n" +
                            "[%s] INT DEFAULT 0,\n" +
                            "CONSTRAINT pkAtualizaCliente PRIMARY KEY ([id]))",
                    TABELA,
                    ID,
                    NOME_FANTASIA,
                    RAZAO_SOCIAL,
                    NOME_CONTATO,
                    DDD_TELEFONE,
                    TELEFONE,
                    DDD_CELULAR,
                    CELULAR,
                    TIPO_LOGRADOURO,
                    NOME_LOGRADOURO,
                    NUMERO_LOGRADOURO,
                    COMPLEMENTO_LOGRADOURO,
                    CIDADE,
                    BAIRRO,
                    ESTADO,
                    CEP,
                    ID_SEGMENTO,
                    PONTO_REFERENCIA,
                    EMAIL,
                    ID_VENDEDOR,
                    DATA_HORA,
                    SYNC
            );
        }

        static String atualizarTabela69() {
            return "ALTER TABLE " + TABELA + " ADD COLUMN [" + STATUS + "] INTEGER DEFAULT " + EnumAtualizarCliente.ANDAMENTO.getValue();
        }
    }
}
