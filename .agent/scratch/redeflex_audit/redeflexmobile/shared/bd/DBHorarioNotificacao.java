package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.axys.redeflexmobile.shared.models.HorarioNotificacao;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Denis Gasparoto on 07/05/2019.
 */

public class DBHorarioNotificacao {

    public static final String SQL = TabelaHorarioNotificacao.criarTabela();

    private final Context context;

    public DBHorarioNotificacao(Context context) {
        this.context = context;
    }

    private static HorarioNotificacao obterHorarioNotificacaoPorId(Context context,
                                                                   int horarioNotificacaoId) {
        HorarioNotificacao horarioNotificacao = null;
        String consulta = TabelaHorarioNotificacao.getHorariosNotificacaoQuery() + " WHERE "
                + TabelaHorarioNotificacao.ID_SERVER + " = ?";

        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        String[] filtro = new String[]{String.valueOf(horarioNotificacaoId)};

        try (Cursor cursor = db.rawQuery(consulta, filtro)) {
            while (cursor.moveToNext()) {
                horarioNotificacao = TabelaHorarioNotificacao
                        .parseHorariosNotificacaoCursor(cursor);
            }
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return horarioNotificacao;
    }

    public List<HorarioNotificacao> getHorariosNotificacao() {
        try {
            return TabelaHorarioNotificacao.getHorariosNotificacao(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public HorarioNotificacao getHorarioNotificacaoPorDiaSemana(int diaSemana) {
        try {
            return TabelaHorarioNotificacao.getHorarioNotificacaoPorSemana(context, diaSemana);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void salvarHorariosNotificacao(HorarioNotificacao horarioNotificacao) {
        try {
            TabelaHorarioNotificacao.salvarHorariosNotificacao(context, horarioNotificacao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        TabelaHorarioNotificacao.deleteAll(context);
    }

    private static class TabelaHorarioNotificacao {

        private static final String NOME_TABELA = "HorarioNotificacao";
        private static final String ID_SERVER = "id";
        private static final String DIA_SEMANA = "DiaSemana";
        private static final String HORA_UM = "Hora1";
        private static final String HORA_DOIS = "Hora2";
        private static final String HORA_TRES = "Hora3";
        private static final String HORA_QUATRO = "Hora4";
        private static final String PUSH = "Push";
        private static final String TEMPO_LEITURA = "tempoLeitura";

        private static void deleteAll(Context context) {
            SimpleDbHelper.INSTANCE.open(context).delete(NOME_TABELA, null, null);
        }

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(5),\n" +
                            "[%s] VARCHAR(5),\n" +
                            "[%s] VARCHAR(5),\n" +
                            "[%s] VARCHAR(5),\n" +
                            "[%s] VARCHAR(5),\n" +
                            "[%s] INTEGER,\n" +
                            "PRIMARY KEY (%s, %s));",
                    NOME_TABELA,
                    ID_SERVER,
                    DIA_SEMANA,
                    HORA_UM,
                    HORA_DOIS,
                    HORA_TRES,
                    HORA_QUATRO,
                    PUSH,
                    TEMPO_LEITURA,
                    ID_SERVER,
                    DIA_SEMANA
            );
        }

        private static String getHorariosNotificacaoQuery() {
            return String.format("SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM %s",
                    ID_SERVER,
                    DIA_SEMANA,
                    HORA_UM,
                    HORA_DOIS,
                    HORA_TRES,
                    HORA_QUATRO,
                    PUSH,
                    TEMPO_LEITURA,
                    NOME_TABELA);
        }

        private static List<HorarioNotificacao> getHorariosNotificacao(Context context) {
            List<HorarioNotificacao> horariosNotificacao = new ArrayList<>();

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);

            try (Cursor cursor = db.rawQuery(getHorariosNotificacaoQuery(), null)) {
                while (cursor.moveToNext()) {
                    horariosNotificacao.add(parseHorariosNotificacaoCursor(cursor));
                }
            } catch (Exception ex) {
                Timber.e(ex);
            }

            return horariosNotificacao;
        }

        private static HorarioNotificacao getHorarioNotificacaoPorSemana(Context context, int diaSemana) {
            String query = String.format("%s WHERE %s = ?", getHorariosNotificacaoQuery(), DIA_SEMANA);

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(diaSemana)})) {
                if (cursor != null && cursor.moveToFirst()) {
                    return parseHorariosNotificacaoCursor(cursor);
                }
            } catch (Exception ex) {
                Timber.e(ex);
            }
            return null;
        }

        private static void salvarHorariosNotificacao(Context context,
                                                      HorarioNotificacao horarioNotificacao) {
            HorarioNotificacao horarioNotificacaoLocal = obterHorarioNotificacaoPorId(context,
                    horarioNotificacao.getIdServer());

            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_SERVER, horarioNotificacao.getIdServer());
            contentValues.put(DIA_SEMANA, horarioNotificacao.getDiaSemana());
            contentValues.put(HORA_UM, horarioNotificacao.getHoraUm());
            contentValues.put(HORA_DOIS, horarioNotificacao.getHoraDois());
            contentValues.put(HORA_TRES, horarioNotificacao.getHoraTres());
            contentValues.put(HORA_QUATRO, horarioNotificacao.getHoraQuatro());
            contentValues.put(PUSH, horarioNotificacao.getPush());
            contentValues.put(TEMPO_LEITURA, horarioNotificacao.getTempoLeitura());

            if (horarioNotificacaoLocal == null) {
                SimpleDbHelper.INSTANCE.open(context)
                        .insert(
                                TabelaHorarioNotificacao.NOME_TABELA,
                                null,
                                contentValues
                        );
                return;
            }

            SimpleDbHelper.INSTANCE.open(context)
                    .update(
                            NOME_TABELA,
                            contentValues,
                            String.format("%s = ?", ID_SERVER),
                            new String[]{String.valueOf(horarioNotificacao.getIdServer())}
                    );
        }

        private static HorarioNotificacao parseHorariosNotificacaoCursor(Cursor cursor) {
            HorarioNotificacao item = new HorarioNotificacao();
            item.setIdServer(getInt(cursor, ID_SERVER));
            item.setDiaSemana(getInt(cursor, DIA_SEMANA));
            item.setHoraUm(getString(cursor, HORA_UM));
            item.setHoraDois(getString(cursor, HORA_DOIS));
            item.setHoraTres(getString(cursor, HORA_TRES));
            item.setHoraQuatro(getString(cursor, HORA_QUATRO));
            item.setPush(getString(cursor, PUSH));
            item.setTempoLeitura(getInt(cursor, TEMPO_LEITURA));
            return item;
        }

        private static int getInt(Cursor cursor, String coluna) {
            return cursor.getInt(cursor.getColumnIndex(coluna));
        }

        private static String getString(Cursor cursor, String coluna) {
            return cursor.getString(cursor.getColumnIndex(coluna));
        }
    }
}
