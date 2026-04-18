package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRoutesProspectStatus;
import com.axys.redeflexmobile.shared.models.Cliente;
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
public class DBRotaAdquirencia {

    static final String SQL = Tabela.SQL;

    private Context context;

    public DBRotaAdquirencia(Context context) {
        this.context = context;
    }

    public void salvar(RoutesProspect routes) {
        if (pegarPorId(routes.getId()) != null) {
            atualizar(routes);
            return;
        }

        ContentValues contentValues = Tabela.converter(routes);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        Tabela.NOME,
                        null,
                        contentValues
                );
    }

    public void atualizar(RoutesProspect routesProspect) {
        ContentValues contentValues = Tabela.converter(routesProspect);
        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        Tabela.NOME,
                        contentValues,
                        Tabela.filtroPadrao(),
                        new String[]{String.valueOf(routesProspect.getId())}
                );
    }

    public RoutesProspect pegarPorId(Integer id) {
        if (id == null) return null;
        RoutesProspect prospect;

        DBRotaAdquirenciaAgendada dbRotaAdquirenciaAgendada = new DBRotaAdquirenciaAgendada(context);
        prospect = dbRotaAdquirenciaAgendada.pegarPorId(id);
        if (prospect != null) return prospect;

        List<RoutesProspect> resultados = pegarDados(id);
        if (!resultados.isEmpty()) prospect = resultados.get(0);
        return prospect;
    }

    public List<RoutesProspect> pegarTodas() {
        return pegarDados(null);
    }

    private List<RoutesProspect> pegarDados(Integer id) {
        List<RoutesProspect> lista = new ArrayList<>();
        DBCliente dbCliente = new DBCliente(context);
        DBProspect dbProspect = new DBProspect(context);
        DBColaborador dbColaborador = new DBColaborador(context);

        String visitQuery = "SELECT COUNT(visita.id) " +
                "FROM [VisitaAdquirencia] visita " +
                "WHERE visita.[id_rota] = rota.[" + Tabela.COLUNA_ID + "]";

        String query = "SELECT rota.[" + Tabela.COLUNA_ID + "], " +
                "rota.[" + Tabela.COLUNA_CLIENTE + "], " +
                "rota.[" + Tabela.COLUNA_PROSPECT + "], " +
                "rota.[" + Tabela.COLUNA_DIA_SEMANA + "], " +
                "rota.[" + Tabela.COLUNA_ORDEM + "], " +
                "rota.[" + Tabela.COLUNA_INCLUIR + "], " +
                "rota.[" + Tabela.COLUNA_TIPO_ID + "], " +
                "rota.[" + Tabela.COLUNA_SEMANA + "], " +
                "(" + visitQuery + ") AS visitas " +
                "FROM [" + Tabela.NOME + "] rota " +
                "WHERE rota.[" + Tabela.COLUNA_SEMANA + "] = ?";

        String[] args = new String[]{String.valueOf(dbColaborador.get().getSemanaRota())};
        if (id != null) {
            query = query + " AND rota.[" + Tabela.COLUNA_ID + "] = ?";
            args = new String[]{String.valueOf(dbColaborador.get().getSemanaRota()), String.valueOf(id)};
        }
        query = query + " ORDER BY [" + Tabela.COLUNA_ORDEM + "]";

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(query, args)) {
            while (cursor.moveToNext()) {
                RoutesProspect routesProspect = Tabela.converter(cursor);
                routesProspect = tratarRota(dbCliente, dbProspect, routesProspect);
                if (routesProspect != null) lista.add(routesProspect);
            }
        } catch (Exception e) {
            Timber.e(e);
        }

        if (id != null) return lista;

        DBRotaAdquirenciaAgendada dbRotaAdquirenciaAgendada = new DBRotaAdquirenciaAgendada(context);
        lista.addAll(dbRotaAdquirenciaAgendada.pegarTodas(DateUtils.getDayOfWeek()));

        return tratarProximaVisita(lista);
    }

    private RoutesProspect tratarRota(DBCliente dbCliente, DBProspect dbProspect, RoutesProspect item) {
        Cliente cliente = null;
        RouteClientProspect prospect = null;

        if (item.getVisitCount() > 0) {
            item.setStatus(EnumRoutesProspectStatus.COMPLETED.getValue());
        }

        if (item.getCustomerId() != null && item.getCustomerId() != EMPTY_INT) {
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
        } else if (item.getProspectId() != null && item.getProspectId() != EMPTY_INT) {
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

    private List<RoutesProspect> tratarProximaVisita(List<RoutesProspect> lista) {
        RoutesProspect nextRoute = Stream.ofNullable(lista)
                .filter(value -> value.getDayOfWeek() == DateUtils.getDayOfWeek() && value.getVisitCount() == 0)
                .findFirst()
                .orElse(null);

        if (nextRoute == null) {
            return lista;
        }

        for (RoutesProspect route : lista) {
            if (route.getId().equals(nextRoute.getId())) {
                route.setStatus(EnumRoutesProspectStatus.NEXT_VISIT.getValue());
                break;
            }
        }
        return lista;
    }

    public void deletaTudo() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBRotaAdquirencia.Tabela.NOME, null, null);
    }

    private static class Tabela {

        static final String NOME = "RotaAdquirencia";

        static final String COLUNA_ID = "id";
        static final String COLUNA_CLIENTE = "id_cliente";
        static final String COLUNA_PROSPECT = "id_prospect";
        static final String COLUNA_DIA_SEMANA = "dia_semana";
        static final String COLUNA_ORDEM = "ordem";
        static final String COLUNA_INCLUIR = "incluir";
        static final String COLUNA_TIPO_ID = "id_tipo";
        static final String COLUNA_SEMANA = "semana";

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
                            "[%s] INTEGER" +
                            ")",
                    NOME,
                    COLUNA_ID,
                    COLUNA_CLIENTE,
                    COLUNA_DIA_SEMANA,
                    COLUNA_ORDEM,
                    COLUNA_INCLUIR,
                    COLUNA_TIPO_ID,
                    COLUNA_SEMANA,
                    COLUNA_PROSPECT
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
                    getInt(cursor, "visitas"));
        }

        private static int getInt(Cursor cursor, String coluna) {
            return cursor.getInt(cursor.getColumnIndex(coluna));
        }
    }
}
