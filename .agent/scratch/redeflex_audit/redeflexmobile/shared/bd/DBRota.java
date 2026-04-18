package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Rota;
import com.axys.redeflexmobile.shared.models.RotaMobile;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Desenvolvimento on 28/06/2016.
 */
public class DBRota {
    final private String mTabela = "Rota";
    private Context mContext;

    public DBRota(Context pContext) {
        this.mContext = pContext;
    }

    private static LocalDateTime montarDataProximaVisita(int semanaAtual, int semana, int diaSemana) {
        int incrementoSemana = 0;
        LocalDateTime data = LocalDate.now().atStartOfDay().plusDays(1);
        int dayOfWeek = data.getDayOfWeek().getValue() + 1;

        if (semana < semanaAtual) {
            incrementoSemana = 5 - semanaAtual;
            semanaAtual = 1;
        } else if ((semana == semanaAtual) && (diaSemana <= dayOfWeek)) {
            incrementoSemana = 4;
        }

        incrementoSemana = incrementoSemana + (semana - semanaAtual);

        int somarDias = (incrementoSemana * 7) - dayOfWeek + diaSemana;

        return data.plusDays(somarDias);
    }

    public void addRotaMobile(RotaMobile pRotaMobile) throws Exception {
        if (pRotaMobile.isIncluir()) {
            ContentValues values = new ContentValues();
            values.put("id", pRotaMobile.getId());
            values.put("idCliente", pRotaMobile.getIdCliente());
            values.put("diaSemana", pRotaMobile.getDiaSemana());
            values.put("ordem", pRotaMobile.getOrdem());
            values.put("idTipo", pRotaMobile.getIdTipo());
            values.put("semana", pRotaMobile.getSemana());
            if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pRotaMobile.getId())))
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            else
                SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pRotaMobile.getId())});
        } else
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=? ", new String[]{String.valueOf(pRotaMobile.getId())});
    }

    public ArrayList<Rota> getRotasByDiaSemana(int pDiaSemana, String pFantasia, int pSemana, int pVendedor) {
        Util_IO.StringBuilder sb = queryRota();
        if (!pFantasia.isEmpty())
            sb.appendLine("AND (t1.[nomeFantasia] LIKE '%" + pFantasia + "%' OR t1.[razaoSocial] LIKE '%" + pFantasia + "%')");
        else {
            sb.appendLine("AND t0.[diaSemana] = " + pDiaSemana);
            sb.appendLine("AND (t0.[semana] = 0 OR t0.[semana] = " + pSemana + ")");
        }

        if (pVendedor != 0)
            sb.appendLine("AND t1.[idVendedor] = " + pVendedor);

        sb.appendLine("ORDER BY t0.[ordem]");

        Cursor cursor = null;
        ArrayList<Rota> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    Rota obj = cursorToRota(cursor);
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

    public Rota getRotasByDiaSemanaCliente(int pDiaSemana, String idCliente, int pSemana) {
        Util_IO.StringBuilder sb = queryRota();
        sb.appendLine("AND t1.[id] =" + idCliente);
        sb.appendLine("AND t0.[diaSemana] = " + pDiaSemana);
        sb.appendLine("AND (t0.[semana] = 0 OR t0.[semana] = " + pSemana + ")");
        sb.appendLine("ORDER BY t0.[ordem]");

        Cursor cursor = null;
        //ArrayList<Rota> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    Rota obj = cursorToRota(cursor);
                    if (obj != null)
                        return obj;
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

   /* public ArrayList<Rota> getRotasByDiaSemanaNovaQuery(int pDiaSemana, String pFantasia, int pSemana, boolean isValidaOrdemRota) {

        Util_IO.StringBuilder sb = queryNovaRota();
        if (!pFantasia.isEmpty()) {
            sb.appendLine("AND (t1.[nomeFantasia] LIKE '%" + pFantasia + "%' OR t1.[razaoSocial] LIKE '%" + pFantasia + "%')");
        }

        if (isValidaOrdemRota) {
            sb.appendLine("AND t0.[diaSemana] = " + pDiaSemana);
            sb.appendLine("AND (t0.[semana] = 0 OR t0.[semana] = " + pSemana + ")");
        }

        sb.appendLine("ORDER BY t0.[ordem]");

        Cursor cursor = null;
        ArrayList<Rota> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    Rota obj = cursorToRotaNovaQuery(cursor);
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
    }*/

    public Rota getRotaByDiaSemana(int pDiaSemana, int pSemana) {
        Util_IO.StringBuilder sb = queryRota();
        sb.appendLine("AND t0.[diaSemana] = " + pDiaSemana);
        sb.appendLine("AND (t0.[semana] = 0 OR t0.[semana] = " + pSemana + ")");
        sb.appendLine("AND (SELECT COUNT(t2.id) FROM [Visita] t2 WHERE t2.[idCliente] = t1.[id] AND date(t2.[dataInicio]) = date('now','localtime')) = 0");
        sb.appendLine("ORDER BY t0.[ordem]");
        sb.appendLine("LIMIT 1");

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursorToRota(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public Rota getRotaByIdCliente(String pIdCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [nomeFantasia]");
        sb.appendLine(",CASE UPPER(IFNULL(exibirCodigo,''))");
        sb.appendLine("      WHEN 'SGV' THEN IFNULL(codigoSGV,'')");
        sb.appendLine("      WHEN 'EFRESH' THEN IFNULL(codigoeFresh,'')");
        sb.appendLine("      WHEN 'APLIC' THEN IFNULL(codigoAplic,'')");
        sb.appendLine("      ELSE IFNULL(idclienteintraflex,'')");
        sb.appendLine("END AS Codigo");
        sb.appendLine(",CASE WHEN IFNULL([tipoLogradouro],'') = '' THEN '' ELSE [tipoLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL([nomeLogradouro],'') = '' THEN '' ELSE ', '|| [nomeLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL([numeroLogradouro],'') = '' THEN '' ELSE ', '|| [numeroLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL([complementoLogradouro],'') = '' THEN '' ELSE ', '|| [complementoLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL([bairro],'') = '' THEN '' ELSE ', '|| [bairro] END ||");
        sb.appendLine("', ' || IFNULL([cidade],'')||'/'||IFNULL([estado],'')");
        sb.appendLine("AS Endereco");
        sb.appendLine(",0 AS diaSemana");
        sb.appendLine(",0 AS ordem");
        sb.appendLine(",IFNULL(latitude,0) AS latitude");
        sb.appendLine(",IFNULL(longitude,0) AS longitude");
        sb.appendLine(",0 AS Visitas");
        sb.appendLine(",0 AS Vendas");
        sb.appendLine(",id AS [idCliente]");
        sb.appendLine(",0 AS [Semana]");
        sb.appendLine(",[cpf_cnpj] as Documento");
        sb.appendLine(",CASE LENGTH(cpf_cnpj)");
        sb.appendLine("      WHEN 14 THEN 'CNPJ'");
        sb.appendLine("      WHEN 11 THEN 'CPF'");
        sb.appendLine("      ELSE ''");
        sb.appendLine("END AS TipoDocumento");
        sb.appendLine("FROM [Cliente]");
        sb.appendLine("WHERE 1=1");
        sb.appendLine("AND id = ?");

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pIdCliente});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursorToRota(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean existeRotaDia(int pDiaSemana, int pSemana) {
        Util_IO.StringBuilder sb = queryRota();
        sb.appendLine("AND t0.[diaSemana] = " + pDiaSemana);
        sb.appendLine("AND (t0.[semana] = 0 OR t0.[semana] = " + pSemana + ")");

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public boolean validaOrdemAtendimento(int pDiaSemana, int pSemana, int pOrdem, String pCodCliente) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM [Visita] ");
        sb.append("WHERE idCliente = ? ");
        sb.append("AND date(dataInicio) = date('now','localtime') ");
        sb.append("AND dataFim IS NOT NULL");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pCodCliente});
            if (cursor != null && cursor.getCount() > 0)
                return true;

            sb = new StringBuilder();
            sb.append("SELECT t0.id, t0.ordem ");
            sb.append("FROM [Rota] t0 ");
            sb.append("JOIN [Cliente] t1 ON (t1.[id] = t0.[idCliente]) ");
            sb.append("WHERE 1=1 ");
            sb.append("AND t1.[ativo] = 'S' ");
            sb.append("AND t0.[diaSemana] = ? ");
            sb.append("AND (t0.[semana] = 0 OR t0.[semana] = ?) ");
            sb.append("AND (SELECT COUNT(t2.id) FROM [Visita] t2 WHERE t2.[idCliente] = t1.[id] AND date(t2.[dataInicio]) = date('now','localtime')) = 0 ");
            sb.append("ORDER BY t0.[ordem]");

            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pDiaSemana), String.valueOf(pSemana)});
            if (cursor != null) {
                if (cursor.getCount() == 0)
                    return true;
                else {
                    cursor.moveToFirst();
                    if (cursor.getInt(1) >= pOrdem)
                        return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public void deleteRotaByIdCliente(String pIdCliente) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[idCliente]=? ", new String[]{pIdCliente});
    }

    private Util_IO.StringBuilder queryRota() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT DISTINCT t1.[nomeFantasia]");
        sb.appendLine(",CASE UPPER(IFNULL(t1.exibirCodigo,''))");
        sb.appendLine("      WHEN 'SGV' THEN IFNULL(t1.codigoSGV,'')");
        sb.appendLine("      WHEN 'EFRESH' THEN IFNULL(t1.codigoeFresh,'')");
        sb.appendLine("      WHEN 'APLIC' THEN IFNULL(t1.codigoAplic,'')");
        sb.appendLine("      ELSE IFNULL(t1.idclienteintraflex,'')");
        sb.appendLine("END AS Codigo");
        sb.appendLine(",CASE WHEN IFNULL(t1.[tipoLogradouro],'') = '' THEN '' ELSE t1.[tipoLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL(t1.[nomeLogradouro],'') = '' THEN '' ELSE ', '|| t1.[nomeLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL(t1.[numeroLogradouro],'') = '' THEN '' ELSE ', '|| t1.[numeroLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL(t1.[complementoLogradouro],'') = '' THEN '' ELSE ', '|| t1.[complementoLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL(t1.[bairro],'') = '' THEN '' ELSE ', '|| t1.[bairro] END ||");
        sb.appendLine("', ' || IFNULL(t1.[cidade],'')||'/'||IFNULL(t1.[estado],'') AS Endereco");
        sb.appendLine(", t0.diaSemana");
        sb.appendLine(", t0.ordem");
        sb.appendLine(",IFNULL(t1.latitude,0) AS latitude");
        sb.appendLine(",IFNULL(t1.longitude,0) AS longitude");
        sb.appendLine(",(SELECT COUNT(t2.id) FROM [Visita] t2 WHERE t2.[idCliente] = t1.[id] AND date(t2.[dataInicio]) = date('now','localtime')) AS Visitas");
        sb.appendLine(",(SELECT COUNT(t3.id) FROM [Venda] t3");
        sb.appendLine("                      JOIN [Visita] t2 ON t3.[idVisita] = t2.[id]");
        sb.appendLine("                     WHERE t2.[idCliente] = t1.[id] AND date(t2.[dataInicio]) = date('now','localtime')) AS Vendas");
        sb.appendLine(",t0.[idCliente]");
        sb.appendLine(",t0.[semana]");
        sb.appendLine(",t1.[cpf_cnpj] as Documento");
        sb.appendLine(",CASE LENGTH(t1.cpf_cnpj)");
        sb.appendLine("      WHEN 14 THEN 'CNPJ'");
        sb.appendLine("      WHEN 11 THEN 'CPF'");
        sb.appendLine("      ELSE ''");
        sb.appendLine("END AS TipoDocumento");
        sb.appendLine("FROM [Rota] t0");
        sb.appendLine("JOIN [Cliente] t1 ON (t1.[id] = t0.[idCliente])");
        sb.appendLine("WHERE 1=1");
        //sb.appendLine("AND t1.[ativo] = 'S'");
        return sb;
    }

  /*  private Util_IO.StringBuilder queryNovaRota() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT DISTINCT t1.[nomeFantasia]");
        sb.appendLine(",CASE UPPER(IFNULL(t1.exibirCodigo,''))");
        sb.appendLine("      WHEN 'SGV' THEN IFNULL(t1.codigoSGV,'')");
        sb.appendLine("      WHEN 'EFRESH' THEN IFNULL(t1.codigoeFresh,'')");
        sb.appendLine("      WHEN 'APLIC' THEN IFNULL(t1.codigoAplic,'')");
        sb.appendLine("      ELSE IFNULL(t1.idclienteintraflex,'')");
        sb.appendLine("END AS Codigo");
        sb.appendLine(",CASE WHEN IFNULL(t1.[tipoLogradouro],'') = '' THEN '' ELSE t1.[tipoLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL(t1.[nomeLogradouro],'') = '' THEN '' ELSE ', '|| t1.[nomeLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL(t1.[numeroLogradouro],'') = '' THEN '' ELSE ', '|| t1.[numeroLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL(t1.[complementoLogradouro],'') = '' THEN '' ELSE ', '|| t1.[complementoLogradouro] END ||");
        sb.appendLine("CASE WHEN IFNULL(t1.[bairro],'') = '' THEN '' ELSE ', '|| t1.[bairro] END ||");
        sb.appendLine("', ' || IFNULL(t1.[cidade],'')||'/'||IFNULL(t1.[estado],'') AS Endereco");
        sb.appendLine(",IFNULL(t1.latitude,0) AS latitude");
        sb.appendLine(",IFNULL(t1.longitude,0) AS longitude");
        sb.appendLine(",t0.[idCliente]");
        sb.appendLine(",t0.[semana]");
        sb.appendLine(",(SELECT COUNT(t2.id) FROM [Visita] t2 WHERE t2.[idCliente] = t1.[id] AND date(t2.[dataInicio]) = date('now','localtime')) AS Visitas ");
        sb.appendLine("FROM [Rota] t0");
        sb.appendLine("JOIN [Cliente] t1 ON (t1.[id] = t0.[idCliente])");
        sb.appendLine("WHERE t1.[ativo] = 'S'");
        return sb;
    }*/

    private Rota cursorToRota(Cursor cursor) {
        if (cursor != null) {
            Rota rota = new Rota();
            rota.setCliente(cursor.getString(0));
            rota.setExibirCodigo(cursor.getString(1));
            rota.setEndereco(cursor.getString(2));
            rota.setDiaSemana(cursor.getInt(3));
            rota.setOrdem(cursor.getInt(4));
            rota.setLatitude(cursor.getDouble(5));
            rota.setLongitude(cursor.getDouble(6));
            rota.setIdVisita(cursor.getInt(7));
            rota.setIdVenda(cursor.getInt(8));
            rota.setIdCliente(cursor.getString(9));
            rota.setAtivo("S");
            rota.setSemana(cursor.getInt(10));
            rota.setDocumento(cursor.getString(11));
            rota.setTipoDocumento(cursor.getString(12));
            return rota;
        } else
            return null;
    }

/*    private Rota cursorToRotaNovaQuery(Cursor cursor) {
        if (cursor != null) {
            Rota rota = new Rota();
            rota.setCliente(cursor.getString(0));
            rota.setExibirCodigo(cursor.getString(1));
            rota.setEndereco(cursor.getString(2));
            rota.setLatitude(cursor.getDouble(3));
            rota.setLongitude(cursor.getDouble(4));
            rota.setIdCliente(cursor.getString(5));
            rota.setSemana(cursor.getInt(6));
            rota.setIdVisita(cursor.getInt(7));
            rota.setAtivo("S");
            return rota;
        } else
            return null;
    }*/

    public ArrayList<Rota> getRotasByIdCliente(String pIdCliente) {
        Util_IO.StringBuilder sb = queryRota();
        sb.appendLine("AND t0.[idCliente] = ?");

        Cursor cursor = null;
        ArrayList<Rota> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pIdCliente});
            if (cursor.moveToFirst()) {
                do {
                    Rota rota = cursorToRota(cursor);
                    if (rota != null)
                        lista.add(rota);
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

    public String getProximaVisita(String pIdCliente) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        ArrayList<Rota> listRotas = getRotasByIdCliente(pIdCliente);
        ArrayList<LocalDateTime> listDate = new ArrayList<>();
        Colaborador colaborador = new DBColaborador(mContext).get();
        int semanaColaborador = colaborador.getSemanaRota();
        if (listRotas != null && listRotas.size() > 0) {
            for (Rota rotaSemana : listRotas) {
                LocalDateTime data = montarDataProximaVisita(
                        semanaColaborador,
                        rotaSemana.getSemana(),
                        rotaSemana.getDiaSemana());
                listDate.add(data);
            }
        }

        if (listDate.isEmpty()) {
            return "";
        }

        LocalDateTime remove = Stream.of(listDate)
                .filter(data -> {
                    LocalDateTime hoje = LocalDate.now().atStartOfDay();
                    return data.isBefore(hoje) || data.isEqual(hoje);
                })
                .findFirst()
                .orElse(null);
        listDate.remove(remove);
        if (listDate.isEmpty()) {
            return "";
        }

        LocalDateTime primeiraData = Collections.min(listDate);
        return primeiraData.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - EEEE"));
    }
}