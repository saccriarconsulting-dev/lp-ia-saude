package com.axys.redeflexmobile.shared.dao;

import android.content.Context;
import android.database.Cursor;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Rota;
import com.axys.redeflexmobile.shared.util.Util_IO;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class RotaDaoImpl implements RotaDao {

    private Context context;

    public RotaDaoImpl(Context context) {
        this.context = context;
    }

    private LocalDateTime montarDataProximaVisita(int semanaAtual, int semana, int diaSemana) {
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

    public String getProximaVisita(String pIdCliente) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        ArrayList<Rota> listRotas = getRotasByIdCliente(pIdCliente);
        ArrayList<LocalDateTime> listDate = new ArrayList<>();
        Colaborador colaborador = new DBColaborador(context).get();
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

    public ArrayList<Rota> getRotasByIdCliente(String pIdCliente) {
        Util_IO.StringBuilder sb = queryRota();
        sb.appendLine("AND t0.[idCliente] = ?");

        Cursor cursor = null;
        ArrayList<Rota> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), new String[]{pIdCliente});
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
        sb.appendLine(",t0.diaSemana");
        sb.appendLine(",t0.ordem");
        sb.appendLine(",IFNULL(t1.latitude,0) AS latitude");
        sb.appendLine(",IFNULL(t1.longitude,0) AS longitude");
        sb.appendLine(",(SELECT COUNT(t2.id) FROM [Visita] t2 WHERE t2.[idCliente] = t1.[id] AND date(t2.[dataInicio]) = date('now','localtime')) AS Visitas");
        sb.appendLine(",(SELECT COUNT(t3.id) FROM [Venda] t3");
        sb.appendLine("                      JOIN [Visita] t2 ON t3.[idVisita] = t2.[id]");
        sb.appendLine("                     WHERE t2.[idCliente] = t1.[id] AND date(t2.[dataInicio]) = date('now','localtime')) AS Vendas");
        sb.appendLine(",t0.[idCliente]");
        sb.appendLine(",t0.[semana]");
        sb.appendLine("FROM [Rota] t0");
        sb.appendLine("JOIN [Cliente] t1 ON (t1.[id] = t0.[idCliente])");
        sb.appendLine("WHERE 1=1");
        sb.appendLine("AND t1.[ativo] = 'S'");
        return sb;
    }

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
            return rota;
        } else
            return null;
    }
}
