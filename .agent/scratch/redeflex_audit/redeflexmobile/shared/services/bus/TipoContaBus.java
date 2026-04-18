package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBCallReason;
import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastro;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRegisterMigrationSub;
import com.axys.redeflexmobile.shared.bd.DBTipoConta;
import com.axys.redeflexmobile.shared.models.CallReason;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Interacoes;
import com.axys.redeflexmobile.shared.models.TipoConta;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Lucas Trigueiro 02/10/2020
 */
public class TipoContaBus extends BaseBus {

    public static void get(int load, Context context) {
        try {
            DBTipoConta dbTipoConta = new DBTipoConta(context);
            DBRegisterMigrationSub dbRegisterMigrationSub = new DBRegisterMigrationSub(context);
            DBClienteCadastro dbClienteCadastro = new DBClienteCadastro(context);

            Colaborador colaborador = new DBColaborador(context).get();

            String url = URLs.URL_TIPO_CONTA + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + load;
            TipoConta[] array = Utilidades.getArrayObject(url, TipoConta[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (TipoConta tipoConta : array) {
                    if (tipoConta.isActive()) {
                        dbTipoConta.add(tipoConta);
                    } else {
                        deleteInactive(dbTipoConta, tipoConta, dbRegisterMigrationSub, dbClienteCadastro);
                    }
                    idList.add(tipoConta.getId());
                }
                setSync(URLs.URL_TIPO_CONTA, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Timber.e(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Timber.e(ex);
        }
    }

    private static void deleteInactive(DBTipoConta dbTipoConta, TipoConta tipoConta,
                                       DBRegisterMigrationSub dbRegisterMigrationSub,
                                       DBClienteCadastro dbClienteCadastro) {

        Boolean temMigracao = dbRegisterMigrationSub.existeCadastroPorTipoConta(tipoConta.getId());
        Boolean temCadastro = dbClienteCadastro.existeCadastroPorTipoConta(tipoConta.getId());

        if(temMigracao || temCadastro)
            return;

        dbTipoConta.deleteById(tipoConta.getId());
    }
}
