package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.Pendencia;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;
import com.axys.redeflexmobile.shared.util.Util_DB;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public class DBPendencia {
    private final Context mContext;
    private final String TABLE_NAME_PENDENCIA = "Pendencia";
    private final String TABLE_NAME_PENDENCIA_CLIENTE = "PendenciaCliente";
    private final String TABLE_NAME_PENDENCIA_MOTIVO = "PendenciaMotivo";

    public DBPendencia(@NonNull Context pContext) {
        mContext = pContext;
    }

    public void addPendencia(@NonNull Pendencia pendencia) throws Exception {
        final SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(mContext);

        final ContentValues values = new ContentValues();
        values.put("id", pendencia.getId());
        values.put("pendenciaId", pendencia.getPendenciaId());
        values.put("descricao", pendencia.getDescricao());
        values.put("ativo", pendencia.isAtivo());

        final String pendId = String.valueOf(pendencia.getPendenciaId());

        final boolean cadastrado = Util_DB.isCadastrado(
                mContext, TABLE_NAME_PENDENCIA, "pendenciaId", pendId
        );

        try {
            if (!cadastrado) {
                db.insert(TABLE_NAME_PENDENCIA, null, values);
            } else {
                db.update(TABLE_NAME_PENDENCIA, values, "pendenciaId = ?", new String[]{pendId});
            }
        } catch (SQLiteConstraintException dup) {
            Timber.w(dup, "DBPendencia.addPendencia: UNIQUE em id; aplicando UPDATE por pendenciaId=%s", pendId);
            db.update(TABLE_NAME_PENDENCIA, values, "pendenciaId = ?", new String[]{pendId});
        }
    }

    public void addPendenciaMotivo(@NonNull PendenciaMotivo motivo) throws Exception {
        final ContentValues values = new ContentValues();
        values.put("id", motivo.getId());
        values.put("pendenciaId", motivo.getPendenciaId());
        values.put("pendenciaMotivoId", motivo.getPendenciaMotivoId());
        values.put("descricao", motivo.getDescricao());
        values.put("ativo", motivo.isAtivo());

        if (!Util_DB.isCadastrado(mContext, TABLE_NAME_PENDENCIA_MOTIVO, "pendenciaMotivoId",
                String.valueOf(motivo.getPendenciaMotivoId()))) {
            SimpleDbHelper.INSTANCE.open(mContext).insert(TABLE_NAME_PENDENCIA_MOTIVO, null, values);
        } else {
            SimpleDbHelper.INSTANCE.open(mContext).update(
                    TABLE_NAME_PENDENCIA_MOTIVO,
                    values,
                    "pendenciaMotivoId = ?",
                    new String[]{String.valueOf(motivo.getPendenciaMotivoId())}
            );
        }
    }

    public void addPendenciaCliente(@NonNull PendenciaCliente client) throws Exception {
        final ContentValues values = new ContentValues();
        values.put("pendenciaClienteId", client.getId());
        values.put("clienteId", client.getClienteId());
        values.put("pendenciaId", client.getPendenciaId());
        values.put("pendenciaMotivoId", client.getPendenciaMotivoId());
        values.put("observacao", client.getObservacao());
        values.put("latitude", client.getLatitude());
        values.put("longitude", client.getLongitude());
        values.put("precisao", client.getPrecision());
        values.put("dataVisualizacao", client.getDataVisualizacao());
        values.put("dataResposta", client.getDataResposta());
        values.put("ativo", client.isAtivo());
        values.put("ordem", client.getOrdem());
        values.put("exibeExplicacao", client.isExibeExplicacao());
        values.put("explicacao", client.getExplicacao());

        if (!Util_DB.isCadastrado(
                mContext,
                TABLE_NAME_PENDENCIA_CLIENTE,
                new String[]{"pendenciaId", "clienteId", "pendenciaMotivoId"},
                new String[]{
                        String.valueOf(client.getPendenciaId()),
                        String.valueOf(client.getClienteId()),
                        "0"
                })) {
            SimpleDbHelper.INSTANCE.open(mContext).insert(TABLE_NAME_PENDENCIA_CLIENTE, null, values);
        } else {
            SimpleDbHelper.INSTANCE.open(mContext).update(
                    TABLE_NAME_PENDENCIA_CLIENTE,
                    values,
                    "pendenciaId = ? AND clienteId = ? AND pendenciaMotivoId = 0",
                    new String[]{
                            String.valueOf(client.getPendenciaId()),
                            String.valueOf(client.getClienteId())
                    }
            );
        }
    }
    @Nullable
    public List<PendenciaCliente> getAllPendenciaCliente() {
        Cursor cursor = null;
        try {
            final String sql = "SELECT * FROM [" + TABLE_NAME_PENDENCIA_CLIENTE + "] " +
                    "WHERE dataResposta != '' ORDER BY pendenciaClienteId ASC";
            final SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(mContext);
            cursor = db.rawQuery(sql, null);

            final List<PendenciaCliente> items = new ArrayList<>();
            while (cursor.moveToNext()) {
                PendenciaCliente item = new PendenciaCliente();
                item.setId(cursor.getString(0));
                item.setClienteId(cursor.getInt(1));
                item.setPendenciaId(cursor.getInt(2));
                item.setPendenciaMotivoId(cursor.getInt(3));
                item.setObservacao(cursor.getString(4));
                item.setLatitude(cursor.getDouble(5));
                item.setLongitude(cursor.getDouble(6));
                item.setPrecision(cursor.getDouble(7));
                item.setDataVisualizacao(cursor.getString(8));
                item.setDataResposta(cursor.getString(9));
                item.setAtivo(cursor.getInt(10) == 1);
                item.setOrdem(cursor.getInt(11));
                item.setExibeExplicacao(cursor.getInt(12) == 1);
                item.setExplicacao(cursor.getString(13));
                items.add(item);
            }
            return items;
        } catch (Exception e) {
            Timber.e(e);
            return null;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public void removePendencia(@NonNull Pendencia pendencia) {
        final SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(mContext);
        final String filtro = "pendenciaId = ?";
        final String[] args = new String[]{String.valueOf(pendencia.getPendenciaId())}; // corrigido
        db.delete(TABLE_NAME_PENDENCIA, filtro, args);
    }

    public void removePendenciaMotivo(@NonNull PendenciaMotivo pendenciaMotivo) {
        final SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(mContext);
        final String filtro = "pendenciaMotivoId = ?";
        final String[] args = new String[]{String.valueOf(pendenciaMotivo.getPendenciaMotivoId())};
        db.delete(TABLE_NAME_PENDENCIA_MOTIVO, filtro, args);
    }

    public void removePendenciaCliente(@NonNull PendenciaCliente pendenciaCliente) {
        final SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(mContext);
        final String filtro = "pendenciaClienteId = ?";
        final String[] args = new String[]{String.valueOf(pendenciaCliente.getId())};
        db.delete(TABLE_NAME_PENDENCIA_CLIENTE, filtro, args);
    }

    public void delete60diasPendenciaCliente() {
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(
                    "SELECT pendenciaClienteId FROM [" + TABLE_NAME_PENDENCIA_CLIENTE + "] " +
                            "WHERE [dataVisualizacao] < datetime('now', '-60 day')", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SimpleDbHelper.INSTANCE.open(mContext).delete(
                            TABLE_NAME_PENDENCIA_CLIENTE,
                            "pendenciaClienteId = ?",
                            new String[]{cursor.getString(0)}
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public void deleteAll() {
        final SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(mContext);
        db.delete(TABLE_NAME_PENDENCIA, null, null);
        db.delete(TABLE_NAME_PENDENCIA_CLIENTE, null, null);
        db.delete(TABLE_NAME_PENDENCIA_MOTIVO, null, null);
    }
}