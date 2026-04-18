package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.TaxasAdquirencia;

import java.util.ArrayList;
import java.util.List;

public class DBTaxaAdquirenciaPF {

    private Context context;

    public DBTaxaAdquirenciaPF(Context context) {
        this.context = context;
    }

    public void addTaxas(TaxasAdquirencia item) {
        ContentValues values = TaxaAdquirenciaPFHelper.converter(item);
        if (!isCadastrado(item.getId())) {
            values.put(TaxaAdquirenciaPFHelper.COLUNA_ID, item.getId());
            SimpleDbHelper.INSTANCE
                    .open(context)
                    .insert(TaxaAdquirenciaPFHelper.TABELA, null, values);

            return;
        }

        SimpleDbHelper.INSTANCE
                .open(context)
                .update(
                        TaxaAdquirenciaPFHelper.TABELA,
                        values,
                        TaxaAdquirenciaPFHelper.filtroId(),
                        new String[]{String.valueOf(item.getMcc())}
                );
    }

    private boolean isCadastrado(String codigo) {
        Cursor cursor = SimpleDbHelper.INSTANCE
                .open(context)
                .query(
                        TaxaAdquirenciaPFHelper.TABELA,
                        TaxaAdquirenciaPFHelper.COLUNAS,
                        TaxaAdquirenciaPFHelper.filtroId(),
                        new String[]{codigo},
                        null,
                        null,
                        null,
                        null
                );
        try {
            return (cursor != null && cursor.getCount() > 0);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public List<TaxasAdquirencia> getTaxas() {
        List<TaxasAdquirencia> taxas = new ArrayList<>();
        Cursor cursor = SimpleDbHelper.INSTANCE
                .open(context)
                .query(
                        TaxaAdquirenciaPFHelper.TABELA,
                        TaxaAdquirenciaPFHelper.COLUNAS,
                        null,
                        null,
                        null,
                        null,
                        null
                );
        try {
            while (cursor.moveToNext()) {
                TaxasAdquirencia taxasAdquirencia = TaxaAdquirenciaPFHelper.converter(cursor);
                taxas.add(taxasAdquirencia);
            }
        } finally {
            cursor.close();
        }

        return taxas;
    }

    public List<TaxasAdquirencia> getTaxasForPersonType(int personType) {
        List<TaxasAdquirencia> taxas = new ArrayList<>();

        String query = String.format("SELECT t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s], " +
                        "t1.[%s] " +
                        "FROM %s t1 " +
                        "LEFT JOIN %s t2 " +
                        "ON t1.[%s] = t2.[%s] " +
                        "WHERE t2.[%s] = ? " +
                        "GROUP BY t1.[%s] ",
                TaxaAdquirenciaPFHelper.COLUNA_ID,
                TaxaAdquirenciaPFHelper.COLUNA_RAMO_ATIVIDADE,
                TaxaAdquirenciaPFHelper.COLUNA_MCC,
                TaxaAdquirenciaPFHelper.COLUNA_SITUACAO,
                TaxaAdquirenciaPFHelper.COLUNA_MIN_DEBITO,
                TaxaAdquirenciaPFHelper.COLUNA_MIN_CREDITO_A_VISTA,
                TaxaAdquirenciaPFHelper.COLUNA_MIN_ATE_6,
                TaxaAdquirenciaPFHelper.COLUNA_MIN_MAIOR_6,
                TaxaAdquirenciaPFHelper.COLUNA_TAB_DEBITO,
                TaxaAdquirenciaPFHelper.COLUNA_TAB_CREDITO_A_VISTA,
                TaxaAdquirenciaPFHelper.COLUNA_TAB_ATE_6,
                TaxaAdquirenciaPFHelper.COLUNA_TAB_MAIOR_6,
                TaxaAdquirenciaPFHelper.COLUNA_MAX_DEBITO,
                TaxaAdquirenciaPFHelper.COLUNA_MAX_CREDITO_A_VISTA,
                TaxaAdquirenciaPFHelper.COLUNA_MAX_ATE_6,
                TaxaAdquirenciaPFHelper.COLUNA_MAX_MAIOR_6,
                TaxaAdquirenciaPFHelper.TABELA,
                DBTaxaMdr.Tabela.NOME,
                TaxaAdquirenciaPFHelper.COLUNA_MCC,
                DBTaxaMdr.Tabela.COLUNA_MCC,
                DBTaxaMdr.Tabela.COLUNA_TIPO_PESSOA,
                TaxaAdquirenciaPFHelper.COLUNA_ID);

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(query, new String[]{String.valueOf(personType)})) {
            while (cursor.moveToNext()) {
                TaxasAdquirencia taxasAdquirencia = TaxaAdquirenciaPFHelper.converter(cursor);
                taxas.add(taxasAdquirencia);
            }
        }
        return taxas;
    }

    public TaxasAdquirencia getTaxaById(String id) {
        Cursor cursor = SimpleDbHelper.INSTANCE
                .open(context)
                .query(
                        TaxaAdquirenciaPFHelper.TABELA,
                        TaxaAdquirenciaPFHelper.COLUNAS,
                        TaxaAdquirenciaPFHelper.filtroPadrao(),
                        new String[]{id},
                        null,
                        null,
                        null
                );
        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return TaxaAdquirenciaPFHelper.converter(cursor);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    public TaxasAdquirencia getTaxaByMcc(String mcc) {
        try (Cursor cursor = SimpleDbHelper.INSTANCE
                .open(context)
                .query(
                        TaxaAdquirenciaPFHelper.TABELA,
                        TaxaAdquirenciaPFHelper.COLUNAS,
                        TaxaAdquirenciaPFHelper.filtroMcc(),
                        new String[]{mcc},
                        null,
                        null,
                        null
                )
        ) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return TaxaAdquirenciaPFHelper.converter(cursor);
            }
        }

        return null;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE
                .open(context)
                .delete(TaxaAdquirenciaPFHelper.TABELA, null, null);
    }

    private static class TaxaAdquirenciaPFHelper {

        public static final String TABELA = "TaxasAdquirenciaPF";

        public static final String COLUNA_ID = "id";
        public static final String COLUNA_RAMO_ATIVIDADE = "ramoAtividade";
        public static final String COLUNA_MCC = "mcc";
        public static final String COLUNA_SITUACAO = "situacao";
        public static final String COLUNA_MIN_DEBITO = "minDebito";
        public static final String COLUNA_MIN_CREDITO_A_VISTA = "minCreditoAVista";
        public static final String COLUNA_MIN_ATE_6 = "minAte6";
        public static final String COLUNA_MIN_MAIOR_6 = "minMaior6";
        public static final String COLUNA_TAB_DEBITO = "tabDebito";
        public static final String COLUNA_TAB_CREDITO_A_VISTA = "tabCreditoAVista";
        public static final String COLUNA_TAB_ATE_6 = "tabAte6";
        public static final String COLUNA_TAB_MAIOR_6 = "tabMaior6";
        public static final String COLUNA_MAX_DEBITO = "maxDebito";
        public static final String COLUNA_MAX_CREDITO_A_VISTA = "maxCreditoAVista";
        public static final String COLUNA_MAX_ATE_6 = "maxAte6";
        public static final String COLUNA_MAX_MAIOR_6 = "maxMaior6";

        public static final String[] COLUNAS = new String[]{
                COLUNA_ID,
                COLUNA_RAMO_ATIVIDADE,
                COLUNA_MCC,
                COLUNA_SITUACAO,
                COLUNA_MIN_DEBITO,
                COLUNA_MIN_CREDITO_A_VISTA,
                COLUNA_MIN_ATE_6,
                COLUNA_MIN_MAIOR_6,
                COLUNA_TAB_DEBITO,
                COLUNA_TAB_CREDITO_A_VISTA,
                COLUNA_TAB_ATE_6,
                COLUNA_TAB_MAIOR_6,
                COLUNA_MAX_DEBITO,
                COLUNA_MAX_CREDITO_A_VISTA,
                COLUNA_MAX_ATE_6,
                COLUNA_MAX_MAIOR_6
        };

        public static ContentValues converter(TaxasAdquirencia taxasAdquirencia) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_RAMO_ATIVIDADE, taxasAdquirencia.getRamoAtividade());
            values.put(COLUNA_MCC, taxasAdquirencia.getMcc());
            values.put(COLUNA_SITUACAO, taxasAdquirencia.getSituacao());
            values.put(COLUNA_MIN_DEBITO, taxasAdquirencia.getMinDebito());
            values.put(COLUNA_MIN_CREDITO_A_VISTA, taxasAdquirencia.getMinCreditoAVista());
            values.put(COLUNA_MIN_ATE_6, taxasAdquirencia.getMinAte6());
            values.put(COLUNA_MIN_MAIOR_6, taxasAdquirencia.getMaxMaior6());
            values.put(COLUNA_TAB_DEBITO, taxasAdquirencia.getTabDebito());
            values.put(COLUNA_TAB_CREDITO_A_VISTA, taxasAdquirencia.getTabCreditoAVista());
            values.put(COLUNA_TAB_ATE_6, taxasAdquirencia.getTabAte6());
            values.put(COLUNA_TAB_MAIOR_6, taxasAdquirencia.getTabMaior6());
            values.put(COLUNA_MAX_ATE_6, taxasAdquirencia.getMaxAte6());
            values.put(COLUNA_MAX_DEBITO, taxasAdquirencia.getMaxDebito());
            values.put(COLUNA_MAX_CREDITO_A_VISTA, taxasAdquirencia.getMaxCreditoAVista());
            values.put(COLUNA_TAB_ATE_6, taxasAdquirencia.getMaxAte6());
            values.put(COLUNA_MAX_MAIOR_6, taxasAdquirencia.getMaxMaior6());

            return values;
        }

        static TaxasAdquirencia converter(Cursor cursor) {
            TaxasAdquirencia taxa = new TaxasAdquirencia();
            taxa.setId(getString(cursor, COLUNA_ID));
            taxa.setRamoAtividade(getString(cursor, (COLUNA_RAMO_ATIVIDADE)));
            taxa.setMcc(getString(cursor, (COLUNA_MCC)));
            taxa.setSituacao(getString(cursor, (COLUNA_SITUACAO)));
            taxa.setMinDebito(getDouble(cursor, COLUNA_MIN_DEBITO));
            taxa.setMinCreditoAVista(getDouble(cursor, COLUNA_MIN_CREDITO_A_VISTA));
            taxa.setMinAte6(getDouble(cursor, COLUNA_MIN_ATE_6));
            taxa.setMaxMaior6(getDouble(cursor, COLUNA_MAX_MAIOR_6));
            taxa.setTabDebito(getDouble(cursor, COLUNA_TAB_DEBITO));
            taxa.setTabCreditoAVista(getDouble(cursor, COLUNA_TAB_CREDITO_A_VISTA));
            taxa.setTabAte6(getDouble(cursor, COLUNA_TAB_ATE_6));
            taxa.setTabMaior6(getDouble(cursor, COLUNA_TAB_MAIOR_6));
            taxa.setMaxDebito(getDouble(cursor, COLUNA_MAX_DEBITO));
            taxa.setMaxCreditoAVista(getDouble(cursor, COLUNA_MAX_CREDITO_A_VISTA));
            taxa.setMaxAte6(getDouble(cursor, COLUNA_MAX_ATE_6));
            taxa.setMaxMaior6(getDouble(cursor, COLUNA_MAX_MAIOR_6));

            return taxa;
        }

        static String filtroPadrao() {
            return COLUNA_ID + " = ? AND " + COLUNA_SITUACAO + " = 'A' ";
        }

        static String filtroMcc() {
            return COLUNA_MCC + " = ? AND " + COLUNA_SITUACAO + " = 'A' ";
        }

        static String filtroId() {
            return COLUNA_ID + " = ? ";
        }

        private static String getString(Cursor cursor, String coluna) {
            return cursor.getString(cursor.getColumnIndex(coluna));
        }

        private static double getDouble(Cursor cursor, String coluna) {
            return cursor.getDouble(cursor.getColumnIndex(coluna));
        }
    }
}
