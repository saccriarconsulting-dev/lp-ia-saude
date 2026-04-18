package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.SolicitacaoPidTaxaMDR;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class BDSolicitacaoPidTaxaMDR {
    private final Context mContext;
    private final String mTabela = "SolicitacaoPidTaxaMDR";

    public BDSolicitacaoPidTaxaMDR(Context pContext) {
        mContext = pContext;
    }

    public long addSolicitacaoPidTaxaMDR(SolicitacaoPidTaxaMDR pSolicitacaoPidTaxaMDR) throws Exception {
        ContentValues values = new ContentValues();

        // Campos que não são nulos
        values.put("IdSolicitacaoPid", pSolicitacaoPidTaxaMDR.getIdSolicitacaoPid());
        values.put("BandeiraTipoId", pSolicitacaoPidTaxaMDR.getBandeiraTipoId());

        // Verifica campos nulos
        if(pSolicitacaoPidTaxaMDR.getTaxaDebito().isPresent())
            values.put("TaxaDebito", pSolicitacaoPidTaxaMDR.getTaxaDebito().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaCredito().isPresent())
            values.put("TaxaCredito", pSolicitacaoPidTaxaMDR.getTaxaCredito().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaCredito6x().isPresent())
            values.put("TaxaCredito6x", pSolicitacaoPidTaxaMDR.getTaxaCredito6x().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaCredito12x().isPresent())
            values.put("TaxaCredito12x", pSolicitacaoPidTaxaMDR.getTaxaCredito12x().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaRavAutomatica().isPresent())
            values.put("TaxaRavAutomatica", pSolicitacaoPidTaxaMDR.getTaxaRavAutomatica().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaRavEventual().isPresent())
            values.put("TaxaRavEventual", pSolicitacaoPidTaxaMDR.getTaxaRavEventual().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaDebitoConcorrente().isPresent())
            values.put("TaxaDebitoConcorrente", pSolicitacaoPidTaxaMDR.getTaxaDebitoConcorrente().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaCreditoConcorrente().isPresent())
            values.put("TaxaCreditoConcorrente", pSolicitacaoPidTaxaMDR.getTaxaCreditoConcorrente().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaCredito6xConcorrente().isPresent())
            values.put("TaxaCredito6xConcorrente", pSolicitacaoPidTaxaMDR.getTaxaCredito6xConcorrente().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaCredito12xConcorrente().isPresent())
            values.put("TaxaCredito12xConcorrente", pSolicitacaoPidTaxaMDR.getTaxaCredito12xConcorrente().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaRavAutomaticaConcorrente().isPresent())
            values.put("TaxaRavAutomaticaConcorrente", pSolicitacaoPidTaxaMDR.getTaxaRavAutomaticaConcorrente().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaRavEventualConcorrente().isPresent())
            values.put("TaxaRavEventualConcorrente", pSolicitacaoPidTaxaMDR.getTaxaRavEventualConcorrente().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaDebitoContraproposta().isPresent())
            values.put("TaxaDebitoContraproposta", pSolicitacaoPidTaxaMDR.getTaxaDebitoContraproposta().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaCreditoContraproposta().isPresent())
            values.put("TaxaCreditoContraproposta", pSolicitacaoPidTaxaMDR.getTaxaCreditoContraproposta().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaCredito6xContraproposta().isPresent())
            values.put("TaxaCredito6xContraproposta", pSolicitacaoPidTaxaMDR.getTaxaCredito6xContraproposta().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaCredito12xContraproposta().isPresent())
            values.put("TaxaCredito12xContraproposta", pSolicitacaoPidTaxaMDR.getTaxaCredito12xContraproposta().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaRavAutomaticaContraproposta().isPresent())
            values.put("TaxaRavAutomaticaContraproposta", pSolicitacaoPidTaxaMDR.getTaxaRavAutomaticaContraproposta().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaRavEventualContraproposta().isPresent())
            values.put("TaxaRavEventualContraproposta", pSolicitacaoPidTaxaMDR.getTaxaRavEventualContraproposta().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorTPVDebito().isPresent())
            values.put("TaxaSimuladorTPVDebito", pSolicitacaoPidTaxaMDR.getTaxaSimuladorTPVDebito().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorTPVCredito().isPresent())
            values.put("TaxaSimuladorTPVCredito", pSolicitacaoPidTaxaMDR.getTaxaSimuladorTPVCredito().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorTPVCredito6x().isPresent())
            values.put("TaxaSimuladorTPVCredito6x", pSolicitacaoPidTaxaMDR.getTaxaSimuladorTPVCredito6x().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorTPVCredito12x().isPresent())
            values.put("TaxaSimuladorTPVCredito12x", pSolicitacaoPidTaxaMDR.getTaxaSimuladorTPVCredito12x().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorIntercambioDebito().isPresent())
            values.put("TaxaSimuladorIntercambioDebito", pSolicitacaoPidTaxaMDR.getTaxaSimuladorIntercambioDebito().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorIntercambioCredito().isPresent())
            values.put("TaxaSimuladorIntercambioCredito", pSolicitacaoPidTaxaMDR.getTaxaSimuladorIntercambioCredito().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorIntercambioCredito6x().isPresent())
            values.put("TaxaSimuladorIntercambioCredito6x", pSolicitacaoPidTaxaMDR.getTaxaSimuladorIntercambioCredito6x().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorIntercambioCredito12x().isPresent())
            values.put("TaxaSimuladorIntercambioCredito12x", pSolicitacaoPidTaxaMDR.getTaxaSimuladorIntercambioCredito12x().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorNetMDRDebito().isPresent())
            values.put("TaxaSimuladorNetMDRDebito", pSolicitacaoPidTaxaMDR.getTaxaSimuladorNetMDRDebito().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorNetMDRCredito().isPresent())
            values.put("TaxaSimuladorNetMDRCredito", pSolicitacaoPidTaxaMDR.getTaxaSimuladorNetMDRCredito().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorNetMDRCredito6x().isPresent())
            values.put("TaxaSimuladorNetMDRCredito6x", pSolicitacaoPidTaxaMDR.getTaxaSimuladorNetMDRCredito6x().get());
        if(pSolicitacaoPidTaxaMDR.getTaxaSimuladorNetMDRCredito12x().isPresent())
            values.put("TaxaSimuladorNetMDRCredito12x", pSolicitacaoPidTaxaMDR.getTaxaSimuladorNetMDRCredito12x().get());

        long codigo = 0;
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pSolicitacaoPidTaxaMDR.getId())))
            codigo = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        else
            codigo = SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pSolicitacaoPidTaxaMDR.getId())});
        return codigo;
    }

    public SolicitacaoPidTaxaMDR getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[id] = ?");
        return Utilidades.firstOrDefault(getSolicitacaoPidTaxaMDR(sb.toString(), new String[]{pId}));
    }

    public ArrayList<SolicitacaoPidTaxaMDR> getSolicitacaoPidTaxaMDR(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("Select Id, IdSolicitacaoPid,BandeiraTipoId,TaxaDebito,TaxaCredito,");
        sb.appendLine("TaxaCredito6x,TaxaCredito12x,TaxaRavAutomatica,TaxaRavEventual,TaxaDebitoConcorrente,");
        sb.appendLine("TaxaCreditoConcorrente,TaxaCredito6xConcorrente,TaxaCredito12xConcorrente,TaxaRavAutomaticaConcorrente,");
        sb.appendLine("TaxaRavEventualConcorrente,TaxaDebitoContraproposta,TaxaCreditoContraproposta,TaxaCredito6xContraproposta,");
        sb.appendLine("TaxaCredito12xContraproposta,TaxaRavAutomaticaContraproposta,TaxaRavEventualContraproposta,TaxaSimuladorTPVDebito,");
        sb.appendLine("TaxaSimuladorTPVCredito,TaxaSimuladorTPVCredito6x,TaxaSimuladorTPVCredito12x,TaxaSimuladorIntercambioDebito,");
        sb.appendLine("TaxaSimuladorIntercambioCredito,TaxaSimuladorIntercambioCredito6x,TaxaSimuladorIntercambioCredito12x,");
        sb.appendLine("TaxaSimuladorNetMDRDebito,TaxaSimuladorNetMDRCredito,TaxaSimuladorNetMDRCredito6x,TaxaSimuladorNetMDRCredito12x");
        sb.appendLine("from SolicitacaoPidTaxaMDR");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);
        sb.appendLine(" Order by Id");
        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<SolicitacaoPidTaxaMDR> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<SolicitacaoPidTaxaMDR> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            SolicitacaoPidTaxaMDR detalhe;
            if (cursor.moveToFirst()) {
                do {
                    detalhe = new SolicitacaoPidTaxaMDR();

                    // Campos que não são nulos
                    detalhe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
                    detalhe.setIdSolicitacaoPid(cursor.getInt(cursor.getColumnIndexOrThrow("IdSolicitacaoPid")));
                    detalhe.setBandeiraTipoId(cursor.getInt(cursor.getColumnIndexOrThrow("BandeiraTipoId")));

                    // Verifica campos nulos

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaDebito")))
                        detalhe.setTaxaDebito(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaDebito")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaCredito")))
                        detalhe.setTaxaCredito(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaCredito")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaCredito6x")))
                        detalhe.setTaxaCredito6x(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaCredito6x")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaCredito12x")))
                        detalhe.setTaxaCredito12x(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaCredito12x")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaRavAutomatica")))
                        detalhe.setTaxaRavAutomatica(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaRavAutomatica")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaRavEventual")))
                        detalhe.setTaxaRavEventual(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaRavEventual")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaDebitoConcorrente")))
                        detalhe.setTaxaDebitoConcorrente(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaDebitoConcorrente")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaCreditoConcorrente")))
                        detalhe.setTaxaCreditoConcorrente(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaCreditoConcorrente")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaCredito6xConcorrente")))
                        detalhe.setTaxaCredito6xConcorrente(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaCredito6xConcorrente")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaCredito12xConcorrente")))
                        detalhe.setTaxaCredito12xConcorrente(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaCredito12xConcorrente")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaRavAutomaticaConcorrente")))
                        detalhe.setTaxaRavAutomaticaConcorrente(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaRavAutomaticaConcorrente")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaRavEventualConcorrente")))
                        detalhe.setTaxaRavEventualConcorrente(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaRavEventualConcorrente")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaDebitoContraproposta")))
                        detalhe.setTaxaDebitoContraproposta(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaDebitoContraproposta")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaCreditoContraproposta")))
                        detalhe.setTaxaCreditoContraproposta(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaCreditoContraproposta")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaCredito6xContraproposta")))
                        detalhe.setTaxaCredito6xContraproposta(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaCredito6xContraproposta")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaCredito12xContraproposta")))
                        detalhe.setTaxaCredito12xContraproposta(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaCredito12xContraproposta")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaRavAutomaticaContraproposta")))
                        detalhe.setTaxaRavAutomaticaContraproposta(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaRavAutomaticaContraproposta")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaRavEventualContraproposta")))
                        detalhe.setTaxaRavEventualContraproposta(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaRavEventualContraproposta")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorTPVDebito")))
                        detalhe.setTaxaSimuladorTPVDebito(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorTPVDebito")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorTPVCredito")))
                        detalhe.setTaxaSimuladorTPVCredito(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorTPVCredito")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorTPVCredito6x")))
                        detalhe.setTaxaSimuladorTPVCredito6x(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorTPVCredito6x")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorTPVCredito12x")))
                        detalhe.setTaxaSimuladorTPVCredito12x(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorTPVCredito12x")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorIntercambioDebito")))
                        detalhe.setTaxaSimuladorIntercambioDebito(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorIntercambioDebito")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorIntercambioCredito")))
                        detalhe.setTaxaSimuladorIntercambioCredito(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorIntercambioCredito")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorIntercambioCredito6x")))
                        detalhe.setTaxaSimuladorIntercambioCredito6x(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorIntercambioCredito6x")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorIntercambioCredito12x")))
                        detalhe.setTaxaSimuladorIntercambioCredito12x(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorIntercambioCredito12x")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorNetMDRDebito")))
                        detalhe.setTaxaSimuladorNetMDRDebito(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorNetMDRDebito")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorNetMDRCredito")))
                        detalhe.setTaxaSimuladorNetMDRCredito(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorNetMDRCredito")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorNetMDRCredito6x")))
                        detalhe.setTaxaSimuladorNetMDRCredito6x(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorNetMDRCredito6x")));

                    if(!cursor.isNull(cursor.getColumnIndexOrThrow("TaxaSimuladorNetMDRCredito12x")))
                        detalhe.setTaxaSimuladorNetMDRCredito12x(cursor.getDouble(cursor.getColumnIndexOrThrow("TaxaSimuladorNetMDRCredito12x")));

                    lista.add(detalhe);
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
}
