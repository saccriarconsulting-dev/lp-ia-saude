package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.venda.merchandising.MerchandisingEnvelopamento;
import com.axys.redeflexmobile.shared.models.venda.merchandising.MerchandisingFachada;
import com.axys.redeflexmobile.shared.models.venda.merchandising.MerchandisingNenhum;
import com.axys.redeflexmobile.shared.models.venda.merchandising.padrao.MerchandisingPadrao;
import com.axys.redeflexmobile.shared.models.venda.merchandising.padrao.MerchandisingProdutoPadrao;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.List;

public class DBMerchandising {

    private Context context;
    public static final String tabelaMerchandising = "Merchandising";
    public static final String tabelaMerchandisingEnvelopamento = "MerchandisingEnvelopamento";
    public static final String tabelaMerchandisingNenhum = "MerchandisingNenhum";
    public static final String tabelaMerchandisingFachada = "MerchandisingFachada";
    public static final String tabelaMerchandisingPadrao = "MerchandisingPadrao";
    public static final String tabelaMerchandisingProdutoPadrao = "MerchandisingProdutoPadrao";

    public DBMerchandising(Context context) {
        this.context = context;
    }

    public boolean temMerchadising(String idVisita) {
        String query = "SELECT * FROM [" + tabelaMerchandising + "] WHERE idVisita = ?";

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(query, new String[]{idVisita});
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

    public long obterIdMerchandisingPorIdVisita(int idVisita, int tipoMerchandising) {
        String query = "SELECT * FROM [" + tabelaMerchandising + "] WHERE idVisita = ?";

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(query, new String[]{String.valueOf(idVisita)});

            if (cursor.moveToNext()) {
                return cursor.getInt(cursor.getColumnIndex("id"));
            }

            ContentValues values = new ContentValues();
            values.put("tipo", tipoMerchandising);
            values.put("idVisita", idVisita);
            return SimpleDbHelper.INSTANCE.open(context).insert(tabelaMerchandising, null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return -1;
    }

    public long obterIdMerchandisingPadraoPorIdMerchandising(int idMerchandising) {
        String query = "SELECT * FROM [" + tabelaMerchandisingPadrao + "] WHERE idMerchandising = ?";

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(query, new String[]{String.valueOf(idMerchandising)});

            if (cursor.moveToNext()) {
                return cursor.getInt(cursor.getColumnIndex("id"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return -1;
    }

    public void salvarMerchandisingEnvelopamento(MerchandisingEnvelopamento merchandisingEnvelopamento) {
        ContentValues values = new ContentValues();
        values.put("idMerchandising", merchandisingEnvelopamento.getIdMerchandising());
        values.put("idOperadora", merchandisingEnvelopamento.getIdOperadora());
        values.put("caminhoFoto", merchandisingEnvelopamento.getCaminhoFoto());
        SimpleDbHelper.INSTANCE.open(context).insert(tabelaMerchandisingEnvelopamento, null, values);
    }

    public void salvarMerchandisingNenhum(MerchandisingNenhum merchandisingNenhum) {
        ContentValues values = new ContentValues();
        values.put("idMerchandising", merchandisingNenhum.getIdMerchandising());
        values.put("merchandising", merchandisingNenhum.permiteMerchandising());
        values.put("caminhoFoto", merchandisingNenhum.getCaminhoFoto());
        SimpleDbHelper.INSTANCE.open(context).insert(tabelaMerchandisingNenhum, null, values);
    }

    public void salvarMerchandisingFachada(MerchandisingFachada merchandisingFachada) {
        ContentValues values = new ContentValues();
        values.put("idMerchandising", merchandisingFachada.getIdMerchandising());
        values.put("merchanInterno", merchandisingFachada.getMerchanInterno());
        values.put("merchanExterno", merchandisingFachada.getMerchanExterno());
        values.put("caminhoFotoInterno", merchandisingFachada.getCaminhoFotoInterno());
        values.put("caminhoFotoExterno", merchandisingFachada.getCaminhoFotoExterno());
        SimpleDbHelper.INSTANCE.open(context).insert(tabelaMerchandisingFachada, null, values);
    }

    public void salvarMerchandisingPadrao(MerchandisingPadrao merchandisingPadrao) {
        ContentValues values = new ContentValues();
        values.put("idMerchandising", merchandisingPadrao.getIdMerchandising());
        values.put("merchanInterno", merchandisingPadrao.getMerchanInterno());
        values.put("merchanExterno", merchandisingPadrao.getMerchanExterno());
        values.put("caminhoFotoInterno", merchandisingPadrao.getCaminhoFotoInterno());
        values.put("caminhoFotoExterno", merchandisingPadrao.getCaminhoFotoExterno());
        try {
            SimpleDbHelper.INSTANCE.open(context).insert(tabelaMerchandisingPadrao, null, values);
        } catch (Exception e) {
            String teste = e.getMessage();
        }
    }

    public long obterIdUltimoRegistro() {
        String query = "SELECT * FROM [" + tabelaMerchandisingPadrao + "] ORDER BY id DESC";

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(query, new String[]{});
            if (cursor.moveToNext()) {
                return cursor.getInt(cursor.getColumnIndex("id"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return -1;
    }

    public void adicionarProdutos(List<MerchandisingProdutoPadrao> produtoPadraoList) {
        for (MerchandisingProdutoPadrao item : produtoPadraoList) {
            ContentValues values = new ContentValues();
            values.put("idProduto", item.getIdProduto());
            values.put("idPadrao", item.getIdPadrao());
            values.put("tipoMerchandising", item.getTipoMerchandising());
            values.put("data", Util_IO.dateTimeToString(item.getData(), Config.FormatDateTimeStringBanco));
            SimpleDbHelper.INSTANCE.open(context).insert(tabelaMerchandisingProdutoPadrao, null, values);
        }
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(tabelaMerchandising, null, null);
        SimpleDbHelper.INSTANCE.open(context).delete(tabelaMerchandisingEnvelopamento, null, null);
        SimpleDbHelper.INSTANCE.open(context).delete(tabelaMerchandisingNenhum, null, null);
        SimpleDbHelper.INSTANCE.open(context).delete(tabelaMerchandisingFachada, null, null);
        SimpleDbHelper.INSTANCE.open(context).delete(tabelaMerchandisingPadrao, null, null);
        SimpleDbHelper.INSTANCE.open(context).delete(tabelaMerchandisingProdutoPadrao, null, null);
    }
}
