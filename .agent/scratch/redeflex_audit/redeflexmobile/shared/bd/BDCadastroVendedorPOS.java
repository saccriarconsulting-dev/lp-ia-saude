package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.CadastroVendedorPOS;
import com.axys.redeflexmobile.shared.util.DataSyncManager;
import com.axys.redeflexmobile.shared.util.DataSyncValidator;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BDCadastroVendedorPOS {
    @NonNull private final Context mContext;
    @NonNull private final String mTabela = "CadastroVendedorPOS";

    @NonNull private final List<Runnable> dataSyncFixQueue = new java.util.ArrayList<>();

    public BDCadastroVendedorPOS(@NonNull Context pContext) {
        mContext = pContext;
    }

    public void addPOS(@NonNull CadastroVendedorPOS pPOS) throws Exception {
        final ContentValues values = new ContentValues();
        values.put("Id", pPOS.getId());
        values.put("TipoMaquinaId", pPOS.getTipoMaquinaId());
        values.put("IdVendedor", pPOS.getIdVendedor());

        final @Nullable String dsStr = DataSyncValidator.toDbIfValid(pPOS.getDataSync());
        if (dsStr != null) {
            values.put("DataSync", dsStr);
        } else {
            values.putNull("DataSync");
            if (pPOS.getDataSync() != null) {
                Log.w(
                        "BDCadastroVendedorPOS",
                        "[DATASYNC] DataSync inválido na gravação; definindo como NULL. Id=" + pPOS.getId()
                );
            }
        }

        values.put("ValorAluguel", pPOS.getValorAluguel());
        values.put("IdLimite", pPOS.getIdLimite());

        final ContentValues values2 = new ContentValues();
        values2.put("TipoMaquinaId", pPOS.getTipoMaquinaId());
        if (pPOS.getTipoMaquina() != null) {
            values2.put("Modelo", pPOS.getTipoMaquina().getModelo());
            values2.put("ValorAluguelPadrao", pPOS.getTipoMaquina().getValorAluguelPadrao());
        }

        if (!Util_DB.isCadastrado(mContext, mTabela, "Id", String.valueOf(pPOS.getId()))) {
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            SimpleDbHelper.INSTANCE.open(mContext).insert("TipoMaquina", null, values2);
        } else {
            SimpleDbHelper.INSTANCE.open(mContext).update(
                    mTabela,
                    values,
                    "[Id]=?",
                    new String[]{ String.valueOf(pPOS.getId()) }
            );
            SimpleDbHelper.INSTANCE.open(mContext).update(
                    "TipoMaquina",
                    values2,
                    "[TipoMaquinaId]=?",
                    new String[]{ String.valueOf(pPOS.getTipoMaquinaId()) }
            );
        }
    }

    public @Nullable CadastroVendedorPOS getById(@NonNull Integer pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [Id] = ?");
        return Utilidades.firstOrDefault(
                getVendedorPOS(sb.toString(), new String[]{ String.valueOf(pId) })
        );
    }

    public void deleteById(@NonNull String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[Id]=?", new String[]{ pId });
    }

    public boolean existeVendedorPOS() {
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext)
                    .rawQuery("SELECT [Id] FROM [CadastroVendedorPOS]", null);
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    private @NonNull ArrayList<CadastroVendedorPOS> getVendedorPOS(@Nullable String pCondicao,
                                                                   @Nullable String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [Id]");
        sb.appendLine(",[TipoMaquinaId]");
        sb.appendLine(",[IdVendedor]");
        sb.appendLine(",[DataSync]");
        sb.appendLine(",[ValorAluguel]");
        sb.appendLine(",[IdLimite]");
        sb.appendLine("FROM [CadastroVendedorPOS]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null) sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<CadastroVendedorPOS> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CadastroVendedorPOS vendedorPOS = new CadastroVendedorPOS();
                    final int idLocal = cursor.getInt(0);

                    vendedorPOS.setId(idLocal);
                    vendedorPOS.setTipoMaquinaId(cursor.getInt(1));
                    vendedorPOS.setIdVendedor(cursor.getInt(2));

                    final String rawDs = cursor.isNull(3) ? null : cursor.getString(3);
                    Date parsed = null;
                    if (rawDs != null) {
                        parsed = DataSyncManager.parse(rawDs);
                        if (parsed == null) {
                            DataSyncValidator.warnInvalid(
                                    rawDs,
                                    "CadastroVendedorPOS",
                                    "Id",
                                    String.valueOf(idLocal)
                            );
                            enqueueDataSyncFix(idLocal);
                        }
                    }
                    vendedorPOS.setDataSync(parsed);

                    vendedorPOS.setValorAluguel(cursor.isNull(4) ? 0d : cursor.getDouble(4));
                    vendedorPOS.setIdLimite(cursor.isNull(5) ? 0 : cursor.getInt(5));

                    lista.add(vendedorPOS);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        runDataSyncFixQueue();
        return lista;
    }

    private void enqueueDataSyncFix(final int id) {
        dataSyncFixQueue.add(() -> {
            ContentValues fix = new ContentValues();
            fix.putNull("DataSync");
            try {
                SimpleDbHelper.INSTANCE.open(mContext)
                        .update(mTabela, fix, "[Id]=?", new String[]{ String.valueOf(id) });
                Log.i("BDCadastroVendedorPOS",
                        "[DATASYNC] DataSync fix aplicado para id=" + id);
            } catch (Exception e) {
                Log.e("BDCadastroVendedorPOS",
                        "Erro ao aplicar fix DataSync para id=" + id, e);
            }
        });
    }

    private void runDataSyncFixQueue() {
        for (Runnable r : dataSyncFixQueue) {
            try {
                r.run();
            } catch (Exception e) {
                Log.e("BDCadastroVendedorPOS",
                        "Erro ao executar correção de DataSync", e);
            }
        }
        dataSyncFixQueue.clear();
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}