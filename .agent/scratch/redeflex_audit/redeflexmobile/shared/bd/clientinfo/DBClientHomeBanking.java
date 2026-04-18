package com.axys.redeflexmobile.shared.bd.clientinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientHomeBanking;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lucasmarciano on 30/06/20
 */
public class DBClientHomeBanking {

    private final Context context;
    private final String TABLE_NAME = "ClienteDomicilioBancario";
    private final String ID_COLUMN = "id";
    private final String WHERE_ID = ID_COLUMN + " = ? ";

    public DBClientHomeBanking(Context context) {
        this.context = context;
    }

    public void insert(ClientHomeBanking clientHomeBanking) throws Exception {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, clientHomeBanking.getId());
        values.put("idCliente", clientHomeBanking.getIdClient());
        values.put("nomeBanco", clientHomeBanking.getBankName());
        values.put("tipoConta", clientHomeBanking.getCountType());
        values.put("agencia", clientHomeBanking.getAgency());
        values.put("agenciaDigito", clientHomeBanking.getDigitAgency());
        values.put("conta", clientHomeBanking.getCount());
        values.put("contaDigito", clientHomeBanking.getDigitCount());
        values.put("contaDigito", clientHomeBanking.getDigitCount());
        values.put("idBandeira", clientHomeBanking.getIdFlag());
        values.put("ativo", clientHomeBanking.isActive());

        if (!Util_DB.isCadastrado(context, TABLE_NAME, ID_COLUMN, String.valueOf(clientHomeBanking.getId())))
            SimpleDbHelper.INSTANCE.open(context).insert(
                    TABLE_NAME,
                    null,
                    values);
        else
            SimpleDbHelper.INSTANCE.open(context).update(
                    TABLE_NAME,
                    values,
                    WHERE_ID,
                    new String[]{String.valueOf(clientHomeBanking.getId())});
    }

    public ClientHomeBanking getById(int id) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND " + WHERE_ID);
        return Utilidades.firstOrDefault(getAll(sb.toString(), new String[]{String.valueOf(id)}));
    }

    public void deleteById(int id) {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, WHERE_ID, new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, null, null);
    }

    public ArrayList<ClientHomeBanking> getAll(String conditions, String[] fields) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine(",idCliente");
        sb.appendLine(",nomeBanco ");
        sb.appendLine(",tipoConta ");
        sb.appendLine(",agencia ");
        sb.appendLine(",agenciaDigito ");
        sb.appendLine(",conta ");
        sb.appendLine(",contaDigito ");
        sb.appendLine(",idBandeira ");
        sb.appendLine(",ativo ");
        sb.appendLine("FROM " + TABLE_NAME);
        sb.appendLine("WHERE ativo = 1 ");
        if (conditions != null)
            sb.append(conditions);

        return moverCursorDb(sb, fields);
    }

    public List<ClientHomeBanking> getAllByClientId(int clientId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine(",idCliente");
        sb.appendLine(",nomeBanco ");
        sb.appendLine(",tipoConta ");
        sb.appendLine(",agencia ");
        sb.appendLine(",agenciaDigito ");
        sb.appendLine(",conta ");
        sb.appendLine(",contaDigito ");
        sb.appendLine(",idBandeira ");
        sb.appendLine(",ativo ");
        sb.appendLine("FROM " + TABLE_NAME);
        sb.appendLine("WHERE ativo = 1 AND idCliente = ?");
        sb.appendLine("GROUP BY ");
        sb.appendLine("nomeBanco ");
        sb.appendLine(", tipoConta ");
        sb.appendLine(", agencia ");
        sb.appendLine(", agenciaDigito ");
        sb.appendLine(", conta ");
        sb.appendLine(", contaDigito ");

        List<ClientHomeBanking> listWithGroup =
                moverCursorDb(sb, new String[]{String.valueOf(clientId)});
        List<ClientHomeBanking> listCompleted =
                getAll("AND idCliente = ?", new String[]{String.valueOf(clientId)});

        HashMap<ClientHomeBanking, List<FlagsBank>> hashMap = new HashMap<>();

        for (ClientHomeBanking item : listWithGroup) {
            hashMap.put(item, null);
        }

        for (ClientHomeBanking item : hashMap.keySet()) {
            List<FlagsBank> flags = new ArrayList<>();
            for (ClientHomeBanking compareTo : listCompleted) {
                if (item.equals(compareTo)) {
                    flags.add(filterFlagById(compareTo.getIdFlag()));
                }
            }
            hashMap.put(item, flags);
        }
        return convertHashMapToList(hashMap);
    }

    private ArrayList<ClientHomeBanking> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<ClientHomeBanking> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), args);
            if (cursor.moveToFirst()) {
                do {
                    ClientHomeBanking clientTaxMdr = new ClientHomeBanking();
                    clientTaxMdr.setId(cursor.getInt(0));
                    clientTaxMdr.setIdClient(cursor.getInt(1));
                    clientTaxMdr.setBankName(cursor.getString(2));
                    clientTaxMdr.setCountType(cursor.getString(3));
                    clientTaxMdr.setAgency(cursor.getString(4));
                    clientTaxMdr.setDigitAgency(cursor.getString(5));
                    clientTaxMdr.setCount(cursor.getString(6));
                    clientTaxMdr.setDigitCount(cursor.getString(7));
                    clientTaxMdr.setIdFlag(cursor.getInt(8));
                    clientTaxMdr.setActive(cursor.getInt(9) == 1);
                    list.add(clientTaxMdr);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    private List<ClientHomeBanking> convertHashMapToList(HashMap<ClientHomeBanking, List<FlagsBank>> hashMap) {
        List<ClientHomeBanking> response = new ArrayList<>();
        for (ClientHomeBanking item : hashMap.keySet()) {
            item.setFlags(hashMap.get(item));
            response.add(item);
        }

        return response;
    }

    private FlagsBank filterFlagById(int id) {
        DBFlagsBank db = new DBFlagsBank(context);
        return db.getById(id);
    }
}
