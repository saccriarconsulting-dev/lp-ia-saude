package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.OS;
import com.axys.redeflexmobile.shared.models.OrdemServico;
import com.axys.redeflexmobile.shared.models.OsConsulta;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Desenvolvimento on 09/02/2016.
 */
public class DBOs {
    private static final String mTabela = "OS";
    private Context mContext;

    public DBOs(Context _context) {
        this.mContext = _context;
    }

    public void add(OrdemServico obj) {
        if (!isCadastrado(obj.getId())) {
            ContentValues values = new ContentValues();
            values.put("id", obj.getId());
            values.put("idTipo", obj.getIdTipo());
            values.put("DescricaoTipo", obj.getDescricaoTipo());
            values.put("idClasse", obj.getIdClasse());
            values.put("classeSla", obj.getClasseSla());
            values.put("idCliente", obj.getIdCliente());
            values.put("nomeCliente", obj.getNomeCliente());
            values.put("obs", obj.getObs());
            values.put("Data", Util_IO.dateTimeToString(obj.getData(), "yyyy-MM-dd HH:mm:ss"));
            if (!Util_IO.isNullOrEmpty(obj.getDataAgend())) {
                values.put("DataAgendamento", obj.getDataAgend());
                values.put("AgendamentoSync", 1);
            } else
                values.put("AgendamentoSync", 0);
            values.put("DataLimiteAtend", Util_IO.dateTimeToString(obj.getDataLimiteAtend(), "yyyy-MM-dd HH:mm:ss"));
            values.put("AtendimentoSync", 0);
            long rows = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);

            if (rows > 0) {
                if (Utilidades.verificarHorarioComercial(mContext, false)) {
                    Notificacoes.OrdemServico("Nova OS, Clique para agendar!", obj, mContext);
                }
            }
        }
    }

    private boolean isCadastrado(int id) {
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(mTabela, new String[]{"id"}
                , "id=?", new String[]{String.valueOf(id)}, null, null, null, null);
        try {
            return (cursor != null && cursor.getCount() > 0);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public OS get(int id) {
        StringBuilder sb = retornaQuery();
        sb.append("WHERE id = " + String.valueOf(id));
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return retornaOS(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public void setAgendamentoAutomatico() {
        String selectQuery = "SELECT id,DataLimiteAtend FROM [OS] WHERE idClasse > 0 AND DataAgendamento IS NULL " +
                "AND DataAtendimento IS NULL AND date(DataLimiteAtend) <= date('now','localtime')";

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    ContentValues values = new ContentValues();
                    values.put("DataVisualizacao", Util_IO.dateTimeToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    values.put("DataAgendamento", cursor.getString(1));
                    SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{cursor.getString(0)});
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public ArrayList<OS> getPendenteAgendar() {
        StringBuilder sb = retornaQuery();
        sb.append("WHERE DataAgendamento IS NULL AND DataAtendimento IS NULL");
        Cursor cursor = null;
        ArrayList<OS> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            OS obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = retornaOS(cursor);
                    if (obj != null)
                        lista.add(obj);
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

    public ArrayList<OS> getPendenteAtenderAtrasadas() {
        StringBuilder sb = retornaQuery();
        sb.append("WHERE DataAtendimento IS NULL ");
        sb.append("AND DataAgendamento IS NOT NULL ");
        sb.append("AND date(DataAgendamento) <= date('now', 'localtime')");
        sb.append("ORDER BY data DESC");
        ArrayList<OS> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            OS obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = retornaOS(cursor);
                    if (obj != null)
                        lista.add(obj);
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

    public ArrayList<OS> getVisualizadasNaoSync() {
        StringBuilder sb = retornaQuery();
        sb.append("WHERE DataVisualizacao IS NOT NULL AND IFNULL(VisualizacaoSync,0) = 0");
        Cursor cursor = null;
        ArrayList<OS> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    OS obj = retornaOS(cursor);
                    if (obj != null)
                        lista.add(obj);
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

    public ArrayList<OS> getAgendadasNaoSync() {
        StringBuilder sb = retornaQuery();
        sb.append("WHERE DataAgendamento IS NOT NULL AND IFNULL(AgendamentoSync,0) = 0");

        Cursor cursor = null;
        ArrayList<OS> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    OS obj = retornaOS(cursor);
                    if (obj != null)
                        lista.add(obj);
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

    public ArrayList<OS> getAtendidasNaoSync() {
        StringBuilder sb = retornaQuery();
        sb.append("WHERE DataAtendimento IS NOT NULL ");
        sb.append("AND IFNULL(AtendimentoSync,0) = 0 ");

        ArrayList<OS> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    OS obj = retornaOS(cursor);
                    if (obj != null)
                        lista.add(obj);
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

    public boolean setOsVisualizacao(int id) {
        try {
            ContentValues values = new ContentValues();
            values.put("DataVisualizacao", Util_IO.dateTimeToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean setOsAgenda(int id, Date dataAgenda) {
        try {
            ContentValues values = new ContentValues();
            values.put("DataAgendamento", Util_IO.dateTimeToString(dataAgenda, "yyyy-MM-dd HH:mm:ss"));
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean setSyncVisualizacao(int id) {
        try {
            ContentValues values = new ContentValues();
            values.put("VisualizacaoSync", 1);
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean setSyncAgendamento(int id) {
        try {
            ContentValues values = new ContentValues();
            values.put("AgendamentoSync", 1);
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean setSyncAtendimento(int id) {
        try {
            ContentValues values = new ContentValues();
            values.put("AtendimentoSync", 1);
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean setOsAtendida(int id) {
        try {
            ContentValues values = new ContentValues();
            values.put("DataAtendimento", Util_IO.dateTimeToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void delete(int id) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "id=?", new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<OsConsulta> getConsultaOS(int tipo) {
        ArrayList<OsConsulta> list = new ArrayList<>();
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("SELECT ");
        if (tipo == 0)
            sbSql.append(" date([Data]) ");
        else if (tipo == 1)
            sbSql.append(" date([DataAgendamento]) ");
        else
            sbSql.append(" date([DataAtendimento]) ");
        sbSql.append("FROM [OS] ");
        sbSql.append("WHERE 1=1 ");
        if (tipo == 0)
            sbSql.append("AND DataAgendamento IS NULL AND DataAtendimento IS NULL");
        else if (tipo == 1)
            sbSql.append("AND DataAtendimento IS NULL AND DataAgendamento IS NOT NULL");
        else
            sbSql.append("AND DataAgendamento IS NOT NULL AND DataAtendimento IS NOT NULL");
        if (tipo == 0) {
            sbSql.append(" GROUP BY date([Data])");
            sbSql.append(" ORDER BY date([Data])");
        } else if (tipo == 1) {
            sbSql.append(" GROUP BY date([DataAgendamento])");
            sbSql.append(" ORDER BY date([DataAgendamento])");
        } else {
            sbSql.append(" GROUP BY date([DataAtendimento])");
            sbSql.append(" ORDER BY date([DataAtendimento])");
        }

        Cursor cursor = null;
        Cursor cursor2 = null;
        OsConsulta osConsulta;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        osConsulta = new OsConsulta();
                        osConsulta.setDataOs(Util_IO.stringToDate(cursor.getString(0), "yyyy-MM-dd"));

                        sbSql = retornaQuery();
                        sbSql.append("WHERE 1=1 ");
                        if (tipo == 0) {
                            sbSql.append("AND date([Data]) = date('" + cursor.getString(0) + "') ");
                            sbSql.append("AND DataAgendamento IS NULL AND DataAtendimento IS NULL ");
                            sbSql.append(" ORDER BY [data]");
                        } else if (tipo == 1) {
                            sbSql.append("AND DataAtendimento IS NULL AND DataAgendamento IS NOT NULL ");
                            sbSql.append("AND date([DataAgendamento]) = date('" + cursor.getString(0) + "') ");
                            sbSql.append(" ORDER BY [DataAgendamento]");
                        } else {
                            sbSql.append("AND DataAgendamento IS NOT NULL AND DataAtendimento IS NOT NULL ");
                            sbSql.append("AND date([DataAtendimento]) = date('" + cursor.getString(0) + "') ");
                            sbSql.append(" ORDER BY [DataAtendimento]");
                        }

                        ArrayList<OS> listaOS = new ArrayList<>();
                        cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
                        if (cursor2 != null && cursor2.getCount() > 0) {
                            if (cursor2.moveToFirst()) {
                                do {
                                    OS obj = retornaOS(cursor2);
                                    if (obj != null)
                                        listaOS.add(obj);
                                } while (cursor2.moveToNext());
                            }
                        }
                        osConsulta.setList(listaOS);
                        list.add(osConsulta);
                    } while (cursor.moveToNext());
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
        }

        return list;
    }

    public void deleteAtendidas() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id, DataAtendimento ");
        sb.append("FROM OS ");
        sb.append("WHERE 1=1 ");
        sb.append("AND AtendimentoSync = 1 ");
        sb.append("ORDER BY DataAtendimento ");
        Cursor cursor = null;
        int contador = 0;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    contador++;
                    if (contador > 30)
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "id=?", new String[]{String.valueOf(cursor.getInt(0))});
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void incluirObservacao(String obsvercao, int id) {
        try {
            ContentValues values = new ContentValues();
            values.put("obsVendedor", obsvercao);
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean existeOsPendenteAgendamento() {
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery("SELECT * FROM [OS] WHERE [DataAgendamento] IS NULL", null);
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    private StringBuilder retornaQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id ");
        sb.append(",idTipo ");
        sb.append(",DescricaoTipo ");
        sb.append(",idCliente ");
        sb.append(",nomeCliente ");
        sb.append(",obs ");
        sb.append(",Data ");
        sb.append(",DataVisualizacao ");
        sb.append(",DataAgendamento ");
        sb.append(",DataAtendimento ");
        sb.append(",DataLimiteAtend ");
        sb.append(",IFNULL(AgendamentoSync,0) ");
        sb.append(",IFNULL(AtendimentoSync,0) ");
        sb.append(",idClasse ");
        sb.append(",classeSla ");
        sb.append(",obsVendedor ");
        sb.append(" FROM [OS] ");
        return sb;
    }

    private OS retornaOS(Cursor cursor) {
        if (cursor != null) {
            OS obj = new OS();
            obj.setId(Integer.parseInt(cursor.getString(0)));
            obj.setIdTipo(Integer.parseInt(cursor.getString(1)));
            obj.setDescricaoTipo(cursor.getString(2));
            obj.setIdCliente(Integer.parseInt(cursor.getString(3)));
            obj.setNomeCliente(cursor.getString(4));
            obj.setObs(cursor.getString(5));
            obj.setData(Util_IO.stringToDate(cursor.getString(6), "yyyy-MM-dd HH:mm:ss"));
            obj.setDataVisualizacao(Util_IO.stringToDate(cursor.getString(7), "yyyy-MM-dd HH:mm:ss"));
            obj.setDataAgendamento(Util_IO.stringToDate(cursor.getString(8), "yyyy-MM-dd HH:mm:ss"));
            obj.setDataAtendimento(Util_IO.stringToDate(cursor.getString(9), "yyyy-MM-dd HH:mm:ss"));
            obj.setDataLimiteAtend(Util_IO.stringToDate(cursor.getString(10), "yyyy-MM-dd HH:mm:ss"));
            obj.setAgendamentoSync(cursor.getInt(11) == 1);
            obj.setAtendimentoSync(cursor.getInt(12) == 1);
            obj.setIdClasse(Integer.parseInt(cursor.getString(13)));
            obj.setClasseSla(cursor.getString(14));
            obj.setObsVendedor(cursor.getString(15));
            return obj;
        } else
            return null;
    }
}