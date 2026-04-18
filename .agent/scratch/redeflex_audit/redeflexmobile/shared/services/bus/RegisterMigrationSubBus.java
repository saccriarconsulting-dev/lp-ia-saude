package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBCadastroMigracaoSubHorario;
import com.axys.redeflexmobile.shared.bd.DBRegisterMigrationSub;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.migracao.CadastroMigracaoSubHorario;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.network.util.JsonUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.services.bus.BaseBus.setSync;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public class RegisterMigrationSubBus {
    private static final String FILTRO_PENDENTE = "sync = ? AND (confirmado IS NULL OR confirmado != 1)";
    private static final String PENDENT_VALUE = "0";
    private static final String FILTRO_PENDENTE_CONFIRMACAO = "sync = ? AND confirmado = 1";

    public static void get(int pTipoCarga, Context pContext) {
        try {
            DBRegisterMigrationSub db = new DBRegisterMigrationSub(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.URL_CADASTRO_MIGRACAO_SUB + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + pTipoCarga;
            RegisterMigrationSub[] array = Utilidades.getArrayObject(urlfinal, RegisterMigrationSub[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (RegisterMigrationSub item : array) {
                    if (item.getSituacao() != EnumRegisterStatus.SENT.getValue()) {
                        item.setSync(true);
                        db.add(item);
                        idList.add(item.getIdServer());
                    }
                }
                setSync(URLs.URL_CADASTRO_MIGRACAO_SUB, idList, colaborador.getId());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            Timber.e(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Timber.e(ex);
        }
    }

    public static void send(Context pContext) {
        DBRegisterMigrationSub db = new DBRegisterMigrationSub(pContext);
        DBRegisterMigrationSubTax dbTax = new DBRegisterMigrationSubTax(pContext);
        DBCadastroMigracaoSubHorario dbhorario = new DBCadastroMigracaoSubHorario(pContext);

        Colaborador colaborador = new DBColaborador(pContext).get();
        try {
            ArrayList<RegisterMigrationSub> listMigration = db.getAll(FILTRO_PENDENTE, new String[]{PENDENT_VALUE});
            // Carraga Listas
            Stream.ofNullable(listMigration).forEach(migration -> {
                try {
                    // Carrega as Taxas Informadas
                    List<RegisterMigrationSubTax> taxes = dbTax.getByMigrationId(migration.getId());
                    migration.setTaxesList(taxes);
                    migration.setIdVendedor(colaborador.getId());

                    // Carrega os Horários Funcionamento
                    List<CadastroMigracaoSubHorario> horarios = dbhorario.getPorIdCadastroMigracao(migration.getId());
                    migration.setHorarioFuncionamento(horarios);

                    String request = JsonUtils.getGsonInstance().toJson(migration);
                    if (!JsonUtils.isJson(request)) {
                        throw new JsonParseException("Cannot deserialize object: " + request);
                    }
                    String response = Utilidades.putRegistros(new URL(URLs.URL_CADASTRO_MIGRACAO_SUB), request);
                    if (response != null && !response.equals("-1")) {
                        update(db, migration);
                        db.updateStatus(migration);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void update(DBRegisterMigrationSub db, RegisterMigrationSub register) {
        db.updateSync(register);
    }

    public static void sendConfirmation(Context context) {
        DBRegisterMigrationSub db = new DBRegisterMigrationSub(context);
        try {
            ArrayList<RegisterMigrationSub> listMigration = db.getAll(FILTRO_PENDENTE_CONFIRMACAO, new String[]{PENDENT_VALUE});
            Stream.ofNullable(listMigration).forEach(migration -> {
                try {
                    String request = JsonUtils.getGsonInstance().toJson(migration);
                    String response = Utilidades.postRegistros(new URL(URLs.CONFIRMA_MIGRACAO_CLIENTE), request);
                    if (response != null && !response.equals("-1")) {
                        update(db, migration);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
