package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.customerregister.PrazoNegociacao;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.util.Util_IO.booleanToNumber;
import static com.axys.redeflexmobile.shared.util.Util_IO.numberToBoolean;

/**
 * @author Rogério Massa on 2019-09-12.
 */

public class DBPrazoNegociacao {

    static final String SQL = Tabela.criarTabela();
    private final Context context;

    public DBPrazoNegociacao(Context context) {
        this.context = context;
    }

    public void salvar(PrazoNegociacao prazoNegociacao) {
        if (pegarPorId(prazoNegociacao.getId()) != null) {
            atualizar(prazoNegociacao);
            return;
        }

        ContentValues contentValues = Tabela.converter(prazoNegociacao);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        Tabela.NOME,
                        null,
                        contentValues
                );
    }

    private void atualizar(PrazoNegociacao prazoNegociacao) {
        ContentValues contentValues = Tabela.converter(prazoNegociacao);
        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        Tabela.NOME,
                        contentValues,
                        Tabela.filtroPadrao(),
                        new String[]{String.valueOf(prazoNegociacao.getId())}
                );
    }

    public PrazoNegociacao pegarPorId(int id) {
        PrazoNegociacao prazoNegociacao = null;
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
                prazoNegociacao = Tabela.converter(cursor);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return prazoNegociacao;
    }

    @NotNull
    public Single<PrazoNegociacao> getById(Integer id) {
        return Single.create(emitter -> {
            PrazoNegociacao prazoNegociacao = null;
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
                    prazoNegociacao = Tabela.converter(cursor);
                }
                if (prazoNegociacao != null) {
                    emitter.onSuccess(prazoNegociacao);
                } else {
                    emitter.onError(new Throwable("Não encontrado prazo de negociação  ou o id é nulo"));
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @NotNull
    public Single<List<PrazoNegociacao>> getAll() {
        return Single.create(emitter -> {
            List<PrazoNegociacao> list = new ArrayList<>();
            try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).query(
                    Tabela.NOME,
                    Tabela.COLUNAS,
                    String.format("[%s] = ?", Tabela.COLUNA_ATIVO),
                    new String[]{"1"},
                    null,
                    null,
                    null
            )) {
                while (cursor.moveToNext()) {
                    list.add(Tabela.converter(cursor));
                }
                emitter.onSuccess(list);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public List<PrazoNegociacao> pegarTodas() {
        List<PrazoNegociacao> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .query(
                        Tabela.NOME,
                        Tabela.COLUNAS,
                        String.format("[%s] = ?", Tabela.COLUNA_ATIVO),
                        new String[]{"1"},
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
                .delete(Tabela.NOME, null, null);
    }

    private static class Tabela {

        static final String NOME = "PrazoNegociacao";

        static final String COLUNA_ID = "id";
        static final String COLUNA_DESCRICAO = "descricao";
        static final String COLUNA_ATIVO = "ativo";
        static final String COLUNA_CONFIGURA_ANTECIPACAO = "configuraAntecipacao";

        static final String[] COLUNAS = new String[]{
                COLUNA_ID,
                COLUNA_DESCRICAO,
                COLUNA_ATIVO,
                COLUNA_CONFIGURA_ANTECIPACAO
        };

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER PRIMARY KEY," +
                            "[%s] VARCHAR(300)," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER" +
                            ")",
                    NOME,
                    COLUNA_ID,
                    COLUNA_DESCRICAO,
                    COLUNA_ATIVO,
                    COLUNA_CONFIGURA_ANTECIPACAO
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID);
        }

        private static ContentValues converter(PrazoNegociacao prazoNegociacao) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID, prazoNegociacao.getId());
            values.put(COLUNA_DESCRICAO, prazoNegociacao.getDescricao());
            values.put(COLUNA_ATIVO, booleanToNumber(prazoNegociacao.isAtivo()));
            values.put(COLUNA_CONFIGURA_ANTECIPACAO, booleanToNumber(prazoNegociacao.isConfiguraAntecipacao()));
            return values;
        }

        private static PrazoNegociacao converter(Cursor cursor) {
            return new PrazoNegociacao(
                    cursor.getInt(cursor.getColumnIndex(COLUNA_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUNA_DESCRICAO)),
                    numberToBoolean(cursor.getColumnIndex(COLUNA_ATIVO)),
                    numberToBoolean(cursor.getInt(cursor.getColumnIndex(COLUNA_CONFIGURA_ANTECIPACAO)))
            );
        }
    }
}
