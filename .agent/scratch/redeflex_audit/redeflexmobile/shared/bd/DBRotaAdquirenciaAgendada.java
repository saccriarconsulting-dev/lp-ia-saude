package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRoutesProspectStatus;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect.TYPE_PROSPECT;
import static com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect.TYPE_QUALITY;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

/**
 * @author Diego Fernando on 2019-01-07.
 */
public class DBRotaAdquirenciaAgendada {

    static final String SQL = Tabela.SQL;

    private Context context;

    public DBRotaAdquirenciaAgendada(Context context) {
        this.context = context;
    }

    public long salvar(RoutesProspect routes) {
        ContentValues contentValues = Tabela.converter(routes);
        return SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        Tabela.NOME,
                        null,
                        contentValues
                );
    }

    public RoutesProspect pegarPorId(Integer id) {
        if (id == null) return null;
        RoutesProspect prospect = null;
        List<RoutesProspect> resultados = pegarDados(id, null);
        if (!resultados.isEmpty()) prospect = resultados.get(0);
        return prospect;
    }

    public List<RoutesProspect> pegarTodas(int dayOfWeek) {
        return pegarDados(null, dayOfWeek);
    }

    private List<RoutesProspect> pegarDados(Integer id, Integer dayOfWeek) {
        List<RoutesProspect> lista = new ArrayList<>();
        DBCliente dbCliente = new DBCliente(context);
        DBProspect dbProspect = new DBProspect(context);
        DBColaborador dbColaborador = new DBColaborador(context);

        String visitQuery = "SELECT COUNT(visita.id) " +
                "FROM [VisitaAdquirencia] visita " +
                "WHERE visita.[id_rota] = rotaAgendada.[" + Tabela.COLUNA_ID_ROTA_AGENDADA + "] " +
                "AND visita.[id_rota_agendada] isnull";

        String query = "SELECT rotaAgendada.[" + Tabela.COLUNA_ID + "], " +
                "rotaAgendada.[" + Tabela.COLUNA_CLIENTE + "], " +
                "rotaAgendada.[" + Tabela.COLUNA_PROSPECT + "], " +
                "rotaAgendada.[" + Tabela.COLUNA_DIA_SEMANA + "], " +
                "rotaAgendada.[" + Tabela.COLUNA_ORDEM + "], " +
                "rotaAgendada.[" + Tabela.COLUNA_INCLUIR + "], " +
                "rotaAgendada.[" + Tabela.COLUNA_TIPO_ID + "], " +
                "rotaAgendada.[" + Tabela.COLUNA_SEMANA + "], " +
                "rotaAgendada.[" + Tabela.COLUNA_ID_ROTA_AGENDADA + "], " +
                "(" + visitQuery + ") AS visitas " +
                "FROM [" + Tabela.NOME + "] rotaAgendada " +
                "WHERE rotaAgendada.[" + Tabela.COLUNA_SEMANA + "] = ? ";

        String[] args = new String[]{String.valueOf(dbColaborador.get().getSemanaRota())};
        if (id != null) {
            query = query + " AND rotaAgendada.[" + Tabela.COLUNA_ID_ROTA_AGENDADA + "] = ?";
            args = new String[]{String.valueOf(dbColaborador.get().getSemanaRota()), String.valueOf(id)};
        } else if (dayOfWeek != null) {
            query = query + " AND rotaAgendada.[" + Tabela.COLUNA_DIA_SEMANA + "] = ?";
            args = new String[]{String.valueOf(dbColaborador.get().getSemanaRota()), String.valueOf(dayOfWeek)};
        }
        query = query + " ORDER BY [" + Tabela.COLUNA_ORDEM + "]";

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(query, args);
            while (cursor.moveToNext()) {
                RoutesProspect routesProspect = Tabela.converter(cursor);
                routesProspect = tratarRota(dbCliente, dbProspect, routesProspect);
                if (routesProspect != null) lista.add(routesProspect);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) cursor.close();
        }

        return lista;
    }

    private RoutesProspect tratarRota(DBCliente dbCliente, DBProspect dbProspect, RoutesProspect item) {
        Cliente cliente = null;
        RouteClientProspect prospect = null;

        if (item.getVisitCount() > 0) {
            item.setStatus(EnumRoutesProspectStatus.COMPLETED.getValue());
        }

        if (item.getCustomerId() != null && item.getCustomerId() > EMPTY_INT) {
            cliente = dbCliente.getById(String.valueOf(item.getCustomerId()));
            if (cliente != null) {
                item.setCustomerName(StringUtils.isNotEmpty(cliente.getNomeFantasia())
                        ? cliente.getNomeFantasia()
                        : cliente.getRazaoSocial());
                item.setCustomerAddress(String.format("%s, %s, %s, %s - %s",
                        cliente.getNomeLogradouro(),
                        cliente.getNumeroLogradouro(),
                        cliente.getBairro(),
                        cliente.getCidade(),
                        cliente.getEstado()));
                item.setTypeAttendance(TYPE_QUALITY);
            }
        } else if (item.getProspectId() != null && item.getProspectId() > EMPTY_INT) {
            prospect = dbProspect.pegarPorId(item.getProspectId());
            if (prospect != null) {
                item.setCustomerName(StringUtils.isNotEmpty(prospect.getNameFantasy())
                        ? prospect.getNameFantasy()
                        : prospect.getNameFull());
                item.setCustomerAddress(String.format("%s, %s, %s, %s - %s",
                        prospect.getAddressName(),
                        prospect.getAddressNumber(),
                        prospect.getNeighborhood(),
                        prospect.getCity(),
                        prospect.getFederalState()));
                item.setTypeAttendance(TYPE_PROSPECT);
            }
        }
        return cliente != null || prospect != null ? item : null;
    }

    public void deletaTudo() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBRotaAdquirenciaAgendada.Tabela.NOME, null, null);
    }

    public void deletaTudoVencida() {
        try {
            Colaborador colaborador = new DBColaborador(context).get();
            if (colaborador == null) {
                return;
            }

            SimpleDbHelper.INSTANCE.open(context)
                    .delete(DBRotaAdquirenciaAgendada.Tabela.NOME,

                            String.format("[%s] = ? AND [%s] != ? OR [%s] != ?",
                                    Tabela.COLUNA_SEMANA,
                                    Tabela.COLUNA_DIA_SEMANA,
                                    Tabela.COLUNA_SEMANA),

                            new String[]{String.valueOf(colaborador.getSemanaRota()),
                                    String.valueOf(DateUtils.getDayOfWeek()),
                                    String.valueOf(colaborador.getSemanaRota())});
        } catch (RuntimeException e) {
            Timber.d(e);
        }
    }

    private static class Tabela {

        static final String NOME = "RotaAdquirenciaAgendada";

        static final String COLUNA_ID = "id";
        static final String COLUNA_CLIENTE = "id_cliente";
        static final String COLUNA_PROSPECT = "id_prospect";
        static final String COLUNA_DIA_SEMANA = "dia_semana";
        static final String COLUNA_ORDEM = "ordem";
        static final String COLUNA_INCLUIR = "incluir";
        static final String COLUNA_TIPO_ID = "id_tipo";
        static final String COLUNA_SEMANA = "semana";
        static final String COLUNA_ID_ROTA_AGENDADA = "id_rota_agendada";

        static final String SQL = criarTabela();

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER" +
                            ")",
                    NOME,
                    COLUNA_ID,
                    COLUNA_CLIENTE,
                    COLUNA_PROSPECT,
                    COLUNA_DIA_SEMANA,
                    COLUNA_ORDEM,
                    COLUNA_INCLUIR,
                    COLUNA_TIPO_ID,
                    COLUNA_SEMANA,
                    COLUNA_ID_ROTA_AGENDADA
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID);
        }

        private static ContentValues converter(RoutesProspect route) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID, route.getId());
            values.put(COLUNA_CLIENTE, route.getCustomerId());
            values.put(COLUNA_DIA_SEMANA, route.getDayOfWeek());
            values.put(COLUNA_ORDEM, route.getDayOfWeek());
            values.put(COLUNA_INCLUIR, Util_IO.booleanToNumber(route.isInsert()));
            values.put(COLUNA_TIPO_ID, route.getTypeId());
            values.put(COLUNA_SEMANA, route.getWeek());
            values.put(COLUNA_PROSPECT, route.getProspectId());
            values.put(COLUNA_ID_ROTA_AGENDADA, route.getIdScheduled());
            return values;
        }

        private static RoutesProspect converter(Cursor cursor) {
            return new RoutesProspect(
                    getInt(cursor, COLUNA_ID),
                    getInt(cursor, COLUNA_CLIENTE),
                    getInt(cursor, COLUNA_PROSPECT),
                    getInt(cursor, COLUNA_DIA_SEMANA),
                    getInt(cursor, COLUNA_ORDEM),
                    Util_IO.numberToBoolean(getInt(cursor, COLUNA_INCLUIR)),
                    getInt(cursor, COLUNA_TIPO_ID),
                    getInt(cursor, COLUNA_SEMANA),
                    getInt(cursor, COLUNA_ID_ROTA_AGENDADA),
                    getInt(cursor, "visitas"));
        }

        private static int getInt(Cursor cursor, String coluna) {
            return cursor.getInt(cursor.getColumnIndex(coluna));
        }
    }
}
