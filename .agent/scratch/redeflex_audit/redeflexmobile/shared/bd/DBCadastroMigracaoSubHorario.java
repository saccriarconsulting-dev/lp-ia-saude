package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.migracao.CadastroMigracaoSubHorario;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class DBCadastroMigracaoSubHorario {
    private Context context;

    public DBCadastroMigracaoSubHorario(Context context) {
        this.context = context;
    }

    public void addCadastroMigracaoSubHorario(CadastroMigracaoSubHorario cadastroMigracaoSubHorario) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("IdCadastroMigracao", cadastroMigracaoSubHorario.getIdCadastroMigracao());
        contentValues.put("DiaAtendimentoId", cadastroMigracaoSubHorario.getDiaAtendimentoId());
        contentValues.put("Aberto", cadastroMigracaoSubHorario.getAberto());
        contentValues.put("HoraInicio", cadastroMigracaoSubHorario.getHoraInicio());
        contentValues.put("HoraFim", cadastroMigracaoSubHorario.getHoraFim());

        if (!Util_IO.isNullOrEmpty(String.valueOf(cadastroMigracaoSubHorario.getId()))
                && getPorId(String.valueOf(cadastroMigracaoSubHorario.getId())) != null)
        {
            contentValues.put("Id", cadastroMigracaoSubHorario.getId());
            SimpleDbHelper.INSTANCE.open(context).update("CadastroMigracaoSubHorario", contentValues, "[id]=?", new String[]{String.valueOf(cadastroMigracaoSubHorario.getId())});
        }
        else
        {
            SimpleDbHelper.INSTANCE.open(context).insert("CadastroMigracaoSubHorario",null, contentValues);
        }
    }

    public CadastroMigracaoSubHorario getPorId(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[id] = ?");
        return Utilidades.firstOrDefault(getCadastroMigracaoSubHorario(sb.toString(), new String[]{pId}));
    }

    public @Nullable List<CadastroMigracaoSubHorario> getPorIdCadastroMigracao(int pIdCadastroMigracao) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[IdCadastroMigracao] = ?");
        return getCadastroMigracaoSubHorario(sb.toString(), new String[]{String.valueOf(pIdCadastroMigracao)});
    }


    public void deletaTudoPorId(long pIdCadastroMigracao) {
        SimpleDbHelper.INSTANCE.open(context)
                .delete("CadastroMigracaoSubHorario", String.format("[%s] = ?", "IdCadastroMigracao"),
                        new String[]{String.valueOf(pIdCadastroMigracao)}
                );
    }

    private ArrayList<CadastroMigracaoSubHorario> getCadastroMigracaoSubHorario(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.[Id]");
        sb.appendLine(",t0.[IdCadastroMigracao]");
        sb.appendLine(",t0.[DiaAtendimentoId]");
        sb.appendLine(",t0.[Aberto]");
        sb.appendLine(",t0.[HoraInicio]");
        sb.appendLine(",t0.[HoraFim]");
        sb.appendLine("FROM [CadastroMigracaoSubHorario] t0");
        sb.appendLine("WHERE 1=1");

        if (pCondicao != null) {
            sb.append(pCondicao);
        }

        ArrayList<CadastroMigracaoSubHorario> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), pCampos)) {
            if (cursor.moveToFirst()) {
                do {
                    lista.add(converter(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return lista;
    }

    private CadastroMigracaoSubHorario converter(Cursor cursor) {
        CadastroMigracaoSubHorario cadastroMigracaoSubHorario = new CadastroMigracaoSubHorario();
        cadastroMigracaoSubHorario.setId(cursor.getInt(0));
        cadastroMigracaoSubHorario.setIdCadastroMigracao(cursor.getInt(1));
        cadastroMigracaoSubHorario.setDiaAtendimentoId(cursor.getInt(2));
        cadastroMigracaoSubHorario.setAberto(cursor.getInt(3));
        cadastroMigracaoSubHorario.setHoraInicio(cursor.getString(4));
        cadastroMigracaoSubHorario.setHoraFim(cursor.getString(5));
        return cadastroMigracaoSubHorario;
    }
}
