package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.util.Util_DB;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import timber.log.Timber;

/**
 * @author lucasmarciano on 03/04/20
 */
public class DBRegisterMigrationSubTax {
    private final Context context;
    private final String TABLE_NAME = "CadastroMigracaoSubTaxa";
    private final String COLUMN_ID = "idTaxa";

    public DBRegisterMigrationSubTax(Context context) {
        this.context = context;
    }

    public void add(RegisterMigrationSubTax registerMigrationSubTax) throws Exception {
        if (!Util_DB.isCadastrado(context, TABLE_NAME,
                new String[]{
                        "idCliente",
                        "bandeiraTipoId"
                }, new String[]{
                        String.valueOf(registerMigrationSubTax.getIdCliente()),
                        String.valueOf(registerMigrationSubTax.getBandeiraTipoId())
                })) {
            SimpleDbHelper.INSTANCE.open(context).insert(TABLE_NAME, null, mountObjectValues(registerMigrationSubTax));
        } else {
            update(registerMigrationSubTax);
        }
    }

    public void addAll(List<RegisterMigrationSubTax> registerMigrationSubTaxList) throws Exception {
        for (RegisterMigrationSubTax registerMigrationSubTax : registerMigrationSubTaxList) {
            add(registerMigrationSubTax);
        }
    }

    private @Nullable RegisterMigrationSubTax getByFlagAndClient(int clientId, int flagId) {
        String sql = "SELECT " +
                "idTaxa, " +
                "idCadastroMigracaoSub, " +
                "idCliente, " +
                "bandeiraTipoId, " +
                "debito, " +
                "creditoAVista, " +
                "creditoAte6, " +
                "creditoMaior6, " +
                "antecipacaoAutomatica, " +
                "ativo " +
                "FROM [CadastroMigracaoSubTaxa] " +
                "WHERE bandeiraTipoId = ? AND idCliente = ?";
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sql,
                new String[]{
                        String.valueOf(flagId),
                        String.valueOf(clientId)
        })) {
            List<RegisterMigrationSubTax> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(moveCursor(cursor));
            }
            return list.size() > 0 ? list.get(0) : null;
        } catch (Exception ex) {
            Timber.e(ex);
            return null;
        }
    }

    public @Nullable RegisterMigrationSubTax get(int id) {
        String sql = "SELECT " +
                "idTaxa, " +
                "idCadastroMigracaoSub, " +
                "idCliente, " +
                "bandeiraTipoId, " +
                "debito, " +
                "creditoAVista, " +
                "creditoAte6, " +
                "creditoMaior6, " +
                "antecipacaoAutomatica, " +
                "ativo " +
                "FROM [CadastroMigracaoSubTaxa] " +
                "WHERE idTaxa = ?";
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sql, new String[]{String.valueOf(id)})) {
            List<RegisterMigrationSubTax> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(moveCursor(cursor));
            }
            return list.size() > 0 ? list.get(0) : null;
        } catch (Exception ex) {
            Timber.e(ex);
            return null;
        }
    }

    public @Nullable List<RegisterMigrationSubTax> getByMigrationId(int idCadastroMigracaoSub) {
        String sql = "SELECT " +
                "idTaxa, " +
                "idCadastroMigracaoSub, " +
                "idCliente, " +
                "bandeiraTipoId, " +
                "debito, " +
                "creditoAVista, " +
                "creditoAte6, " +
                "creditoMaior6, " +
                "antecipacaoAutomatica, " +
                "ativo " +
                "FROM [CadastroMigracaoSubTaxa] " +
                "WHERE idCadastroMigracaoSub = ?";
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sql, new String[]{String.valueOf(idCadastroMigracaoSub)})) {
            List<RegisterMigrationSubTax> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(moveCursor(cursor));
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            Timber.e(ex);
            return null;
        }
    }

    public @NotNull List<RegisterMigrationSubTax> getByClientId(int idClient) {
        String sql = "SELECT " +
                "id as idTaxa, " +
                "0 as idCadastroMigracaoSub, " +
                "idCliente, " +
                "bandeiraTipoId, " +
                "taxaDebito as debito, " +
                "taxaCredito as creditoAVista, " +
                "taxaCredito6x as creditoAte6, " +
                "taxaCredito12x as creditoMaior6, " +
                "taxaAntecipacao as antecipacaoAutomatica, " +
                "ativo " +
                "FROM [ClienteTaxaMdr] " +
                "WHERE idCliente = ? ORDER BY bandeiraTipoId";
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sql, new String[]{String.valueOf(idClient)})) {
            List<RegisterMigrationSubTax> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(moveCursor(cursor));
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            Timber.e(ex);
            return new ArrayList<>();
        }
    }

    public @Nullable List<RegisterMigrationSubTax> getAll() {
        String sql = "SELECT " +
                "idTaxa, " +
                "idCadastroMigracaoSub, " +
                "idCliente, " +
                "bandeiraTipoId, " +
                "debito, " +
                "creditoAVista, " +
                "creditoAte6, " +
                "creditoMaior6, " +
                "antecipacaoAutomatica, " +
                "ativo " +
                "FROM [CadastroMigracaoSubTaxa]";
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sql, null)) {
            List<RegisterMigrationSubTax> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(moveCursor(cursor));
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            Timber.e(ex);
            return null;
        }
    }

    public int update(RegisterMigrationSubTax registerMigrationSubTax) {
        RegisterMigrationSubTax taxToUpdate = getByFlagAndClient(
                registerMigrationSubTax.getIdCliente(),
                registerMigrationSubTax.getBandeiraTipoId()
        );
        registerMigrationSubTax.setId(Objects.requireNonNull(taxToUpdate).getId());
        return SimpleDbHelper.INSTANCE.open(context).update(
                TABLE_NAME,
                mountObjectValues(registerMigrationSubTax),
                "[" + COLUMN_ID + "] = ? ",
                new String[]{
                        String.valueOf(taxToUpdate.getId()),
                });
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, null, null);
    }

    public void deleteById(int id) {
        SimpleDbHelper.INSTANCE.open(context).delete(
                TABLE_NAME, "[" + COLUMN_ID + "] = ?", new String[]{String.valueOf(id)});
    }

    private ContentValues mountObjectValues(RegisterMigrationSubTax item) {
        ContentValues values = new ContentValues();
   //     values.put(COLUMN_ID, item.getBandeiraTipoId());
        values.put("idCadastroMigracaoSub", item.getIdCadastroMigracaoSub());
        values.put("idCliente", item.getIdCliente());
        values.put("bandeiraTipoId", item.getBandeiraTipoId());
        values.put("debito", item.getDebito());
        values.put("creditoAVista", item.getCreditoAVista());
        values.put("creditoAte6", item.getCreditoAte6());
        values.put("creditoMaior6", item.getCreditoMaior6());
        values.put("antecipacaoAutomatica", item.getAntecipacaoAutomatica());
        values.put("ativo", item.isAtivo() ? 1 : 0);
        return values;
    }

    private RegisterMigrationSubTax moveCursor(Cursor cursor) {
        RegisterMigrationSubTax item = new RegisterMigrationSubTax();
        item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        item.setIdCadastroMigracaoSub(cursor.getInt(cursor.getColumnIndex("idCadastroMigracaoSub")));
        item.setIdCliente(cursor.getInt(cursor.getColumnIndex("idCliente")));
        item.setBandeiraTipoId(cursor.getInt(cursor.getColumnIndex("bandeiraTipoId")));
        item.setDebito(cursor.getDouble(cursor.getColumnIndex("debito")));
        item.setCreditoAVista(cursor.getDouble(cursor.getColumnIndex("creditoAVista")));
        item.setCreditoAte6(cursor.getDouble(cursor.getColumnIndex("creditoAte6")));
        item.setCreditoMaior6(cursor.getDouble(cursor.getColumnIndex("creditoMaior6")));
        item.setAntecipacaoAutomatica(cursor.getDouble(cursor.getColumnIndex("antecipacaoAutomatica")));
        item.setAtivo(cursor.getInt(cursor.getColumnIndex("ativo")) == 1);

        return item;
    }
}
