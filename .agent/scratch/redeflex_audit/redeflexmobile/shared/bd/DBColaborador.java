package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import io.reactivex.Single;

/**
 * Created by Desenvolvimento on 08/02/2016.
 */
public class DBColaborador {
    private final String mTabela = "Colaborador";
    private final Context mContext;

    public DBColaborador(Context pContext) {
        mContext = pContext;
    }

    public void add(Colaborador pColaborador) {
        ContentValues values = alimentaDados(pColaborador);
        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        Utilidades.idVendedor = String.valueOf(pColaborador.getId());
    }

    private ContentValues alimentaDados(Colaborador pColaborador) {
        ContentValues values = new ContentValues();
        values.put("id", pColaborador.getId());
        values.put("nome", pColaborador.getNome());
        values.put("enviarBase", pColaborador.getEnviarBase());
        values.put("dataDesbloqueia", Util_IO.dateTimeToString(pColaborador.getDataDesbloqueiaVenda(), Config.FormatDateStringBanco));
        values.put("desbloqueiaVenda", pColaborador.getDesbloqueiaVenda());
        values.put("bloqueiaHorario", pColaborador.getBloqueiaHorario());
        values.put("tipo", pColaborador.getTipoColaborador());
        values.put("versaoApp", pColaborador.getVersaoApp());
        values.put("distancia", pColaborador.getDistancia());
        values.put("validaIccid", Util_IO.booleanToNumber(pColaborador.isValidaICCID()));
        values.put("validaOrdemRota", Util_IO.booleanToNumber(pColaborador.isValidaOrdemRota()));
        values.put("obrigaAuditagem", Util_IO.booleanToNumber(pColaborador.isObrigaAuditagem()));
        values.put("semanaRota", pColaborador.getSemanaRota());
        values.put("validaDataGPS", Util_IO.booleanToNumber(pColaborador.isValidaDataGps()));
        values.put("validaCercaFinal", Util_IO.booleanToNumber(pColaborador.isValidaCercaFinalAtend()));
        values.put("novoAtend", Util_IO.booleanToNumber(pColaborador.isNovoAtend()));
        values.put("idCliente", pColaborador.getIdCliente());
        values.put("pis", pColaborador.getPis());
        values.put("cnpjFilial", pColaborador.getCnpjFilial());
        values.put("cartaoPonto", pColaborador.isCartaoPonto());
        values.put("verificaClientePendencia", Util_IO.booleanToNumber(pColaborador.isVerificaClientePendencia()));
        values.put("email", pColaborador.getEmail());
        values.put("CicloRoteirizacao", pColaborador.getCicloRoteirizacao());
        return values;
    }

    public void atualiza(Colaborador pColaborador) {
        ContentValues values = alimentaDados(pColaborador);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, null, null);
    }

    public void setEnviarBase(boolean pEnviaBase) {
        ContentValues values = new ContentValues();
        values.put("enviarBase", Util_IO.booleanToNumber(pEnviaBase));
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, null, null);
    }

    public Colaborador get() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine(",nome");
        sb.appendLine(",enviarBase");
        sb.appendLine(",dataDesbloqueia");
        sb.appendLine(",desbloqueiaVenda");
        sb.appendLine(",bloqueiaHorario");
        sb.appendLine(",tipo");
        sb.appendLine(",versaoApp");
        sb.appendLine(",IFNULL(distancia,0) AS distancia");
        sb.appendLine(",IFNULL(validaIccid,0) AS validaICCID");
        sb.appendLine(",IFNULL(validaOrdemRota,0) AS validaOrdemRota");
        sb.appendLine(",IFNULL(obrigaAuditagem,0) AS obrigaAuditagem");
        sb.appendLine(",IFNULL(semanaRota,0) AS semanaRota");
        sb.appendLine(",IFNULL(validaDataGPS,0) AS validaDataGps");
        sb.appendLine(",IFNULL(validaCercaFinal,0) AS validaCercaFinalAtend");
        sb.appendLine(",IFNULL(novoAtend,0) AS novoAtend");
        sb.appendLine(",pis");
        sb.appendLine(",cnpjFilial");
        sb.appendLine(",1 AS ok");
        sb.appendLine(",idCliente");
        sb.appendLine(",cartaoPonto");
        sb.appendLine(",verificaClientePendencia");
        sb.appendLine(",email");
        sb.appendLine(",CicloRoteirizacao");
        sb.appendLine("FROM [Colaborador]");

        try {
            Colaborador colaborador = Utilidades.firstOrDefault(Util_DB.RetornaLista(mContext, Colaborador.class, sb.toString(), null));
            if(colaborador == null){
                colaborador = new Colaborador();
                colaborador.setId(76561);
            }
            return colaborador;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Single<Colaborador> getCurrent() {
        return Single.create(emitter -> {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("SELECT id");
            sb.appendLine(",nome");
            sb.appendLine(",enviarBase");
            sb.appendLine(",dataDesbloqueia");
            sb.appendLine(",desbloqueiaVenda");
            sb.appendLine(",bloqueiaHorario");
            sb.appendLine(",tipo");
            sb.appendLine(",versaoApp");
            sb.appendLine(",IFNULL(distancia,0) AS distancia");
            sb.appendLine(",IFNULL(validaIccid,0) AS validaICCID");
            sb.appendLine(",IFNULL(validaOrdemRota,0) AS validaOrdemRota");
            sb.appendLine(",IFNULL(obrigaAuditagem,0) AS obrigaAuditagem");
            sb.appendLine(",IFNULL(semanaRota,0) AS semanaRota");
            sb.appendLine(",IFNULL(validaDataGPS,0) AS validaDataGps");
            sb.appendLine(",IFNULL(validaCercaFinal,0) AS validaCercaFinalAtend");
            sb.appendLine(",IFNULL(novoAtend,0) AS novoAtend");
            sb.appendLine(",pis");
            sb.appendLine(",cnpjFilial");
            sb.appendLine(",1 AS ok");
            sb.appendLine(",idCliente");
            sb.appendLine(",cartaoPonto");
            sb.appendLine(",verificaClientePendencia");
            sb.appendLine(",email");
            sb.appendLine(",CicloRoteirizacao");
            sb.appendLine("FROM [Colaborador]");
            try {
                Colaborador colaborador = Utilidades.firstOrDefault(Util_DB.RetornaLista(mContext, Colaborador.class, sb.toString(), null));
                emitter.onSuccess(colaborador);
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    public void delete(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public void addOrUpdate(Colaborador user) {
        Colaborador local = get();
        if (local == null || local.getId() != user.getId()) {
            deleteAll();
            add(user);
            return;
        }

        SimpleDbHelper.INSTANCE.open(mContext).update(
                mTabela, alimentaDados(user), "id = ?", new String[]{String.valueOf(user.getId())}
        );
    }
}
