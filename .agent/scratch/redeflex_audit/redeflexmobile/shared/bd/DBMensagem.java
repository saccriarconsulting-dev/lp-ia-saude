package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Mensagem;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Desenvolvimento on 03/06/2016.
 */
public class DBMensagem {
    private final String mTabela = "Mensagem";
    private Context mContext;

    public DBMensagem(Context _context) {
        this.mContext = _context;
    }

    public void add(Mensagem mensagem) {
        try {
            if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(mensagem.getId()))) {
                ContentValues values = new ContentValues();
                values.put("id", mensagem.getId());
                values.put("texto", mensagem.getTexto());
                values.put("data", Util_IO.dateTimeToString(mensagem.getData(), "yyyy-MM-dd HH:mm:ss"));
                long rows = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
                if (rows > 0) {
                    if (Utilidades.verificarHorarioComercial(mContext, false)) {
                        Notificacoes.Mensagem("Nova Mensagem, Clique para ver!", mensagem, mContext);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Mensagem> getVisualizadasNaoSync() {
        String selectQuery = "SELECT id,texto,data,dataVisualizacao FROM [Mensagem] where dataVisualizacao IS NOT NULL AND IFNULL(visualizacaoSync,0) = 0";
        ArrayList<Mensagem> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
            Mensagem obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = new Mensagem();
                    obj.setId(Integer.parseInt(cursor.getString(0)));
                    obj.setTexto(cursor.getString(1));
                    obj.setData(Util_IO.stringToDate(cursor.getString(2), "yyyy-MM-dd HH:mm:ss"));
                    obj.setDataVisualizacao(Util_IO.stringToDate(cursor.getString(3), "yyyy-MM-dd HH:mm:ss"));
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

    public ArrayList<Mensagem> getAll() {
        String selectQuery = "SELECT id,texto,data,IFNULL(visualizacaoSync,0) FROM [Mensagem] order by data";
        Cursor cursor = null;
        ArrayList<Mensagem> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
            Mensagem obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = new Mensagem();
                    obj.setId(Integer.parseInt(cursor.getString(0)));
                    obj.setTexto(cursor.getString(1));
                    obj.setData(Util_IO.stringToDate(cursor.getString(2), "yyyy-MM-dd HH:mm:ss"));
                    obj.setPermiteDeletar(cursor.getInt(3) == 1);
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

    public boolean setSyncVisualizacao(int pId) {
        ContentValues values = new ContentValues();
        values.put("visualizacaoSync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{String.valueOf(pId)});
        return true;
    }

    public boolean setVisualizacao(int pId, Date pData) {
        ContentValues values = new ContentValues();
        values.put("dataVisualizacao", Util_IO.dateTimeToString(pData, "yyyy-MM-dd HH:mm:ss"));
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pId)});

        return true;
    }

    public boolean setVisualizacaoAll() {
        ContentValues values = new ContentValues();
        values.put("dataVisualizacao", Util_IO.dateTimeToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "dataVisualizacao IS NULL", new String[]{});
        return true;
    }

    public Mensagem getById(int id) {
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(mTabela, new String[]{
                        "id",
                        "texto",
                        "data",
                        "visualizacaoSync"}
                , "id=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Mensagem obj = new Mensagem();
                obj.setId(Integer.parseInt(cursor.getString(0)));
                obj.setTexto(cursor.getString(1));
                obj.setData(Util_IO.stringToDate(cursor.getString(2), "yyyy-MM-dd HH:mm:ss"));
                obj.setPermiteDeletar(cursor.getInt(3) == 1);
                return obj;
            } else
                return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void delete(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}