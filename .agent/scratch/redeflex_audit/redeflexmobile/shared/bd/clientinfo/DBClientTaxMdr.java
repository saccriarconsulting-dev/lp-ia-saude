package com.axys.redeflexmobile.shared.bd.clientinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientTaxMdr;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * @author lucasmarciano on 30/06/20
 */
public class DBClientTaxMdr {
    private final Context context;
    private final String TABLE_NAME = "ClienteTaxaMdr";
    private final String ID_COLUMN = "id";
    private final String WHERE_ID = ID_COLUMN + " = ? ";

    public DBClientTaxMdr(Context context) {
        this.context = context;
    }

    public void insert(ClientTaxMdr clientTaxMdr) throws Exception {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, clientTaxMdr.getId());
        values.put("idCliente", clientTaxMdr.getIdClient());
        values.put("bandeiraTipoId", clientTaxMdr.getFlagId());
        values.put("taxaDebito", clientTaxMdr.getDebitTax());
        values.put("taxaCredito", clientTaxMdr.getCreditTax());
        values.put("taxaCredito6x", clientTaxMdr.getCreditTaxSixTimes());
        values.put("taxaCredito12x", clientTaxMdr.getCreditTaxTwelveTimes());
        values.put("taxaAntecipacao", clientTaxMdr.getTaxAnticipation());
        values.put("ativo", clientTaxMdr.isActive());

        if (!Util_DB.isCadastrado(context, TABLE_NAME, ID_COLUMN, String.valueOf(clientTaxMdr.getId())))
            SimpleDbHelper.INSTANCE.open(context).insert(
                    TABLE_NAME,
                    null,
                    values);
        else
            SimpleDbHelper.INSTANCE.open(context).update(
                    TABLE_NAME,
                    values,
                    ID_COLUMN + " = ? ",
                    new String[]{String.valueOf(clientTaxMdr.getId())});
    }

    public ClientTaxMdr getById(int id) {
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

    public ArrayList<ClientTaxMdr> getAll(String conditions, String[] fields) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT B.idBandeira AS idBandeira");
        sb.appendLine(", B.nomeBandeira AS nomeBandeira");
        sb.appendLine(", B.imagem AS imagemBandeira");
        sb.appendLine(", B.ativo AS ativoBandeira");
        sb.appendLine(", CTM.id AS id");
        sb.appendLine(", CTM.idCliente AS idCliente");
        sb.appendLine(", CTM.bandeiraTipoId AS bandeiraTipoId");
        sb.appendLine(", CTM.taxaDebito AS taxaDebito");
        sb.appendLine(", CTM.taxaCredito AS taxaCredito");
        sb.appendLine(", CTM.taxaCredito6x AS taxaCredito6x");
        sb.appendLine(", CTM.taxaCredito12x");
        sb.appendLine(", CTM.taxaAntecipacao AS taxaAntecipacao");
        sb.appendLine(", CTM.ativo AS ctmAtivo ");
        sb.appendLine("FROM Bandeiras AS B JOIN ClienteTaxaMdr AS CTM ");
        sb.appendLine("ON B.idBandeira = CTM.bandeiraTipoId ");
        sb.appendLine("WHERE B.ativo = 1 AND CTM.ativo = 1 ");
        if (conditions != null)
            sb.append(conditions);

        return moverCursorDb(sb, fields);
    }

    private ArrayList<ClientTaxMdr> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<ClientTaxMdr> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), args);
            if (cursor.moveToFirst()) {
                do {
                    FlagsBank flagsBank = new FlagsBank();
                    ClientTaxMdr clientTaxMdr = new ClientTaxMdr();

                    //Bandeiras
                    flagsBank.setId(cursor.getInt(0));
                    flagsBank.setName(cursor.getString(1));
                    flagsBank.setImage(new String(cursor.getBlob(2)));
                    flagsBank.setActive(cursor.getInt(3) == 1);
                    clientTaxMdr.setFlag(flagsBank);

                    //ClienteTaxaMdr
                    clientTaxMdr.setId(cursor.getInt(4));
                    clientTaxMdr.setIdClient(cursor.getInt(5));
                    clientTaxMdr.setFlagId(cursor.getInt(6));
                    clientTaxMdr.setDebitTax(getDoubleOrNull(cursor, 7));
                    clientTaxMdr.setCreditTax(getDoubleOrNull(cursor, 8));
                    clientTaxMdr.setCreditTaxSixTimes(getDoubleOrNull(cursor, 9));
                    clientTaxMdr.setCreditTaxTwelveTimes(getDoubleOrNull(cursor, 10));
                    clientTaxMdr.setTaxAnticipation(getDoubleOrNull(cursor, 11));
                    clientTaxMdr.setActive(cursor.getInt(12) == 1);

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

    @Nullable
    private Double getDoubleOrNull(Cursor cursor, int columnId) {
        if (cursor.isNull(columnId)) return null;

        return cursor.getDouble(columnId);
    }
}
