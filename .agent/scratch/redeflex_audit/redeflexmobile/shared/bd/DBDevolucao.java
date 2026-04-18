package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Devolucao;
import com.axys.redeflexmobile.shared.models.DevolucaoEnvio;
import com.axys.redeflexmobile.shared.models.DevolucaoICCID;
import com.axys.redeflexmobile.shared.models.DevolucaoItens;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

/**
 * Created by joao.viana on 31/08/2017.
 */

public class DBDevolucao {
    private Context mContext;
    private final String mTabela = "Devolucao";
    private final String mTabelatens = "DevolucaoItens";
    private final String mTabelaICCID = "DevolucaoItensICCID";

    public DBDevolucao(Context _context) {
        mContext = _context;
    }

    public long addDevolucao(String pCliente) {
        ContentValues values = new ContentValues();
        values.put("idCliente", pCliente);
        values.put("situacao", 1);
        values.put("tipo", 1);
        return SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
    }

    public void finalizaDevolucao(int pCodigo, String pCliente) {
        ContentValues values = new ContentValues();
        values.put("idCliente", pCliente);
        values.put("finalizado", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{String.valueOf(pCodigo)});
    }

    public long addItemDevolucao(int pDevolucao, String pProduto, int pQuantidade) {
        ContentValues values = new ContentValues();
        values.put("idDevolucao", pDevolucao);
        values.put("idProduto", pProduto);
        values.put("quantidade", pQuantidade);
        return SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelatens, null, values);
    }

    public void addIccid(int pDevolucao, int pCodigoItem, ArrayList<DevolucaoICCID> list) {
        if (list != null) {
            for (DevolucaoICCID item : list) {
                ContentValues values = new ContentValues();
                values.put("idDevolucaoItem", pCodigoItem);
                values.put("idDevolucao", pDevolucao);
                values.put("ICCIDEntrada", item.getIccidEntrada());
                values.put("ICCIDSaida", item.getIccidSaida());
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaICCID, null, values);
            }
        }
    }

    public Devolucao getNaoFinalizada() {
        Cursor cursor = null;
        StringBuilder sb = retornaQuery();
        sb.append("AND finalizado = 0");
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursorToDev(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public ArrayList<DevolucaoItens> getItensByIdDev(String pCodigo) {
        StringBuilder sb = retornaQueryItem();
        sb.append("AND idDevolucao = ").append(pCodigo);
        Cursor cursor = null, cursor2 = null;
        ArrayList<DevolucaoItens> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            DevolucaoItens item;
            if (cursor.moveToFirst()) {
                do {
                    item = cursorToItem(cursor);
                    if (item != null) {
                        sb = retornaQueryIccid();
                        sb.append(" AND idDevolucaoItem = " + cursor.getInt(0));
                        sb.append(" AND idDevolucao = ").append(pCodigo);

                        ArrayList<DevolucaoICCID> listIccid = new ArrayList<>();
                        DevolucaoICCID devolucaoICCID;
                        cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
                        if (cursor2.moveToFirst()) {
                            do {
                                devolucaoICCID = cursorToIccid(cursor2);
                                if (devolucaoICCID != null)
                                    listIccid.add(devolucaoICCID);
                            } while (cursor2.moveToNext());
                        }
                        item.setListICCID(listIccid);
                        list.add(item);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
        }
        return list;
    }

    private DevolucaoICCID cursorToIccid(Cursor cursor) {
        if (cursor != null) {
            DevolucaoICCID devolucaoICCID = new DevolucaoICCID();
            devolucaoICCID.setId(cursor.getInt(0));
            devolucaoICCID.setIccidEntrada(cursor.getString(3));
            devolucaoICCID.setIccidSaida(cursor.getString(4));
            return devolucaoICCID;
        } else
            return null;
    }

    private DevolucaoItens cursorToItem(Cursor cursor) {
        if (cursor != null) {
            DevolucaoItens item = new DevolucaoItens();
            item.setId(cursor.getInt(0));
            item.setIdDevolucao(cursor.getInt(1));
            item.setIdProduto(cursor.getString(2));
            item.setQuantidade(cursor.getInt(3));
            return item;
        } else
            return null;
    }

    private StringBuilder retornaQueryItem() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id ");
        sb.append(",idDevolucao ");
        sb.append(",idProduto ");
        sb.append(",quantidade ");
        sb.append("FROM [DevolucaoItens] ");
        sb.append("WHERE 1=1 ");
        return sb;
    }

    private StringBuilder retornaQueryIccid() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id ");
        sb.append(",idDevolucaoItem ");
        sb.append(",idDevolucao ");
        sb.append(",ICCIDEntrada ");
        sb.append(",IFNULL(ICCIDSaida,'') ");
        sb.append("FROM [DevolucaoItensICCID] ");
        sb.append("WHERE 1=1 ");
        return sb;
    }

    public Devolucao getById(String pCodigo) {
        Cursor cursor = null;
        StringBuilder sb = retornaQuery();
        sb.append("AND id = ").append(pCodigo);

        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursorToDev(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private StringBuilder retornaQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id ");
        sb.append(",IFNULL(idCliente,0) ");
        sb.append(",data ");
        sb.append(",situacao ");
        sb.append(",tipo ");
        sb.append(",finalizado ");
        sb.append(",IFNULL(idServer,'') ");
        sb.append("FROM Devolucao ");
        sb.append("WHERE 1=1 ");
        return sb;
    }

    private Devolucao cursorToDev(Cursor cursor) {
        if (cursor != null) {
            Devolucao devolucao = new Devolucao();
            devolucao.setId(cursor.getInt(0));
            devolucao.setIdCliente(cursor.getString(1));
            devolucao.setData(Util_IO.stringToDate(cursor.getString(2), "yyyy-MM-dd HH:mm:ss"));
            devolucao.setSituacao(cursor.getInt(3));
            devolucao.setTipo(cursor.getInt(4));
            devolucao.setFinalizado(Util_IO.numberToBoolean(cursor.getInt(5)));
            devolucao.setIdServer(cursor.getString(6));
            return devolucao;
        } else
            return null;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaICCID, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelatens, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public void deleteById(String pCodigo) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaICCID, "[idDevolucao]=?", new String[]{pCodigo});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelatens, "[idDevolucao]=?", new String[]{pCodigo});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pCodigo});
    }

    public void atualizaSync(int pCodigo, String pServer) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        values.put("idServer", pServer);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "id=?", new String[]{String.valueOf(pCodigo)});

        values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelatens, values, "idDevolucao=?", new String[]{String.valueOf(pCodigo)});
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaICCID, values, "idDevolucao=?", new String[]{String.valueOf(pCodigo)});
    }

    public void deleteItemById(String pCodigo) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaICCID, "[idDevolucaoItem]=?", new String[]{pCodigo});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelatens, "[id]=?", new String[]{pCodigo});
    }

    public ArrayList<DevolucaoEnvio> getPendenteSync() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t0.id ");
        sb.append(",IFNULL(t0.idCliente,'') ");
        sb.append(",t0.data ");
        sb.append(",t0.situacao ");
        sb.append(",t0.tipo ");
        sb.append(",t0.finalizado ");
        sb.append(",(SELECT id FROM Colaborador) idVendedor ");
        sb.append("FROM [Devolucao] t0 ");
        sb.append("WHERE t0.finalizado = 1 ");
        sb.append("AND t0.sync = 0 ");
        sb.append("UNION ");
        sb.append("SELECT t0.id ");
        sb.append(",IFNULL(t0.idCliente,'') ");
        sb.append(",t0.data ");
        sb.append(",t0.situacao ");
        sb.append(",t0.tipo ");
        sb.append(",t0.finalizado ");
        sb.append(",(SELECT id FROM Colaborador) idVendedor ");
        sb.append("FROM [Devolucao] t0 ");
        sb.append("WHERE t0.id IN (SELECT idDevolucao FROM [DevolucaoItensICCID] WHERE sync = 0) ");
        sb.append("UNION ");
        sb.append("SELECT t0.id ");
        sb.append(",IFNULL(t0.idCliente,'') ");
        sb.append(",t0.data ");
        sb.append(",t0.situacao ");
        sb.append(",t0.tipo ");
        sb.append(",t0.finalizado ");
        sb.append(",(SELECT id FROM Colaborador) idVendedor ");
        sb.append("FROM [Devolucao] t0 ");
        sb.append("WHERE t0.id IN (SELECT idDevolucao FROM [DevolucaoItens] WHERE sync = 0) ");

        ArrayList<DevolucaoEnvio> lista = new ArrayList<>();
        Cursor cursor = null, cursor2 = null, cursor3 = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            DevolucaoEnvio devolucaoEnvio;
            if (cursor.moveToFirst()) {
                do {
                    devolucaoEnvio = new DevolucaoEnvio();
                    devolucaoEnvio.setId(cursor.getInt(0));
                    devolucaoEnvio.setIdVendedor(cursor.getString(6));
                    devolucaoEnvio.setIdCliente(cursor.getString(1));
                    devolucaoEnvio.setData(Util_IO.stringToDate(cursor.getString(2), "yyyy-MM-dd HH:mm:ss"));
                    devolucaoEnvio.setSituacao(cursor.getInt(4));
                    devolucaoEnvio.setTipo(cursor.getInt(5));
                    ArrayList<DevolucaoItens> listItens = new ArrayList<>();

                    sb = retornaQueryItem();
                    sb.append("AND idDevolucao = ").append(cursor.getInt(0));
                    sb.append(" AND sync = 0 ");
                    sb.append("UNION ");
                    sb.append("SELECT id ");
                    sb.append(",idDevolucao ");
                    sb.append(",idProduto ");
                    sb.append(",quantidade ");
                    sb.append("FROM [DevolucaoItens] ");
                    sb.append("WHERE 1=1 ");
                    sb.append("AND id IN (SELECT idDevolucaoItem FROM [DevolucaoItensICCID] WHERE sync = 0)");
                    cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);

                    DevolucaoItens item;
                    if (cursor2.moveToFirst()) {
                        do {
                            item = cursorToItem(cursor2);
                            if (item != null) {
                                sb = retornaQueryIccid();
                                sb.append(" AND idDevolucaoItem = " + cursor2.getInt(0));
                                sb.append(" AND idDevolucao = ").append(cursor.getInt(0));
                                sb.append(" AND sync = 0");

                                ArrayList<DevolucaoICCID> listIccid = new ArrayList<>();
                                DevolucaoICCID devolucaoICCID;
                                cursor3 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
                                if (cursor3.moveToFirst()) {
                                    do {
                                        devolucaoICCID = cursorToIccid(cursor3);
                                        if (devolucaoICCID != null)
                                            listIccid.add(devolucaoICCID);
                                    } while (cursor3.moveToNext());
                                }
                                item.setListICCID(listIccid);
                                listItens.add(item);
                            }
                        } while (cursor2.moveToNext());
                    }
                    devolucaoEnvio.setListItens(listItens);
                    lista.add(devolucaoEnvio);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
            if (cursor3 != null)
                cursor3.close();
        }
        return lista;
    }

    public ArrayList<Devolucao> getDevolucoes() {
        StringBuilder sb = retornaQuery();
        ArrayList<Devolucao> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            Devolucao devolucao;
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    devolucao = cursorToDev(cursor);
                    if (devolucao != null)
                        list.add(devolucao);
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
}