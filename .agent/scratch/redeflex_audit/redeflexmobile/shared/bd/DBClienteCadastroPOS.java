package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.models.ClienteCadastroPOS;
import com.axys.redeflexmobile.shared.util.DataSyncManager;
import com.axys.redeflexmobile.shared.util.DataSyncValidator;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class DBClienteCadastroPOS {

    private final String mTabelaPOS = "ClienteCadastroPOS";
    private final Context context;

    private final List<Integer> dataSyncFixIds = new ArrayList<>();

    public DBClienteCadastroPOS(@NonNull Context context) {
        this.context = context;
    }

    void salvarModelosPOS(long idCadastro, boolean cadastronovo, @Nullable List<ClienteCadastroPOS> modelos) {
        if (cadastronovo && (modelos == null || modelos.isEmpty())) {
            return;
        }

        if (cadastronovo) {
            Stream.ofNullable(modelos).forEach(modeloPOS -> {
                modeloPOS.setIdClienteCadastro((int) idCadastro);
                addPOS(modeloPOS);
            });
            return;
        }

        String query = "SELECT idAppMobile FROM [ClienteCadastroPOS] WHERE idClienteCadastro = ?";
        Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .rawQuery(query, new String[]{String.valueOf(idCadastro)});
        List<Integer> idsAntigos = new ArrayList<>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    idsAntigos.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        Stream.ofNullable(idsAntigos)
                .filter(value -> Stream.ofNullable(modelos)
                        .filter(modelo -> modelo.getIdAppMobile() != null)
                        .filter(modelo -> modelo.getIdAppMobile().equals(value))
                        .findFirst()
                        .orElse(null) == null)
                .forEach(lastId -> deletarPOS(String.valueOf(lastId)));

        Stream.ofNullable(modelos)
                .filter(modeloPOS -> modeloPOS.getIdAppMobile() == null)
                .forEach(modeloPOS -> {
                    modeloPOS.setIdClienteCadastro((int) idCadastro);
                    addPOS(modeloPOS);
                });

        Stream.ofNullable(modelos)
                .filter(modeloPOS -> modeloPOS.getIdAppMobile() != null
                        && idsAntigos.contains(modeloPOS.getIdAppMobile()))
                .forEach(this::alterarPOS);
    }

    private void addPOS(@NonNull ClienteCadastroPOS modeloPOS) {
        ContentValues values = new ContentValues();
        values.put("id", modeloPOS.getId());
        values.put("idClienteCadastro", modeloPOS.getIdClienteCadastro());
        values.put("idTipoMaquina", modeloPOS.getIdTipoMaquina());
        values.put("valorAluguel", modeloPOS.getValorAluguel());
        values.put("idTerminal", modeloPOS.getIdTerminal());

        final String ds = DataSyncValidator.toDbIfValid(modeloPOS.getDataSync());
        if (ds != null) {
            values.put("dataSync", ds);
        } else {
            values.putNull("dataSync");
            if (modeloPOS.getDataSync() != null) {
                final @Nullable String raw = DataSyncManager.toDbString(modeloPOS.getDataSync());
                Timber.w(
                        "[DATASYNC][ClienteCadastroPOS] DataSync inválido na gravação; definindo como NULL. " +
                                "idAppMobile=%s idClienteCadastro=%s raw=%s",
                        modeloPOS.getIdAppMobile(),
                        modeloPOS.getIdClienteCadastro(),
                        raw
                );
            }
        }

        values.put("cpfCnpjCliente", modeloPOS.getCpfCnpjCliente());
        values.put("tipoConexao", modeloPOS.getTipoConexao());
        values.put("idOperadora", modeloPOS.getIdOperadora());
        values.put("metragemCabo", modeloPOS.getMetragemCabo());

        if (modeloPOS.getIdAppMobile() != null) {
            values.put("idAppMobile", modeloPOS.getIdAppMobile());
            SimpleDbHelper.INSTANCE.open(context).replace(mTabelaPOS, null, values);
            return;
        }

        try {
            SimpleDbHelper.INSTANCE.open(context).insertOrThrow(mTabelaPOS, null, values);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public void addPOSSync(@NonNull ClienteCadastroPOS modeloPOS) {
        if (modeloPOS.getIdAppMobile() == null) {
            return;
        }

        final ContentValues values = new ContentValues();
        values.put("id", modeloPOS.getId());

        values.put("idTerminalString", modeloPOS.getIdTerminal());
        values.put("situacao", modeloPOS.getSituacao());

        final String ds = DataSyncValidator.toDbIfValid(modeloPOS.getDataSync());
        if (ds != null) {
            values.put("dataSync", ds);
        } else {
            values.putNull("dataSync");
            if (modeloPOS.getDataSync() != null) {
                final @Nullable String raw = DataSyncManager.toDbString(modeloPOS.getDataSync());
                Timber.w(
                        "[DATASYNC][ClienteCadastroPOS] DataSync inválido em addPOSSync; definindo como NULL. " +
                                "idAppMobile=%s raw=%s",
                        modeloPOS.getIdAppMobile(),
                        raw
                );
            }
        }

        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        mTabelaPOS,
                        values,
                        "[idAppMobile] = ?",
                        new String[]{String.valueOf(modeloPOS.getIdAppMobile())}
                );
    }

    private void alterarPOS(@NonNull ClienteCadastroPOS modeloPOS) {
        ContentValues values = new ContentValues();
        values.put("valorAluguel", modeloPOS.getValorAluguel());
        values.put("cpfCnpjCliente", modeloPOS.getCpfCnpjCliente());

        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        mTabelaPOS,
                        values,
                        "[idAppMobile]=?",
                        new String[]{String.valueOf(modeloPOS.getIdAppMobile())}
                );
    }

    private void deletarPOS(@NonNull String pId) {
        ContentValues values = new ContentValues();
        values.put("situacao", 0);
        SimpleDbHelper.INSTANCE.open(context)
                .update(mTabelaPOS, values, "[idAppMobile]=?", new String[]{pId});
    }

    public List<ClienteCadastroPOS> obterPOS(int idClienteCadastro, boolean apenasAtivas) {
        String consulta = obterPOSQuery(apenasAtivas) + " AND idClienteCadastro = ? " +
                "GROUP BY " +
                "ccp.[id], " +
                "ccp.[idAppMobile], " +
                "ccp.[idClienteCadastro], " +
                "ccp.[idTipoMaquina], " +
                "ccp.[valorAluguel], " +
                "ccp.[idTerminal], " +
                "ccp.[dataSync], " +
                "ccp.[situacao], " +
                "ccp.[tipoConexao], " +
                "mp.[descricao], " +
                "mp.[modelo], " +
                "ccp.[cpfCnpjCliente], " +
                "ccp.[idOperadora], " +
                "ccp.[metragemCabo] ";
        return buscarPOS(consulta, new String[]{String.valueOf(idClienteCadastro)});
    }

    private String obterPOSQuery(boolean apenasAtiva) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT ccp.[id],");
        sb.appendLine("ccp.[idAppMobile],");
        sb.appendLine("ccp.[idClienteCadastro],");
        sb.appendLine("ccp.[idTipoMaquina],");
        sb.appendLine("ccp.[valorAluguel],");
        sb.appendLine("ccp.[idTerminalString],");
        sb.appendLine("ccp.[dataSync],");
        sb.appendLine("ccp.[situacao],");
        sb.appendLine("ccp.[tipoConexao],");
        sb.appendLine("mp.[descricao],");
        sb.appendLine("mp.[modelo], ");
        sb.appendLine("ccp.[cpfCnpjCliente], ");
        sb.appendLine("IFNULL(ccp.[idOperadora], 0), ");
        sb.appendLine("IFNULL(ccp.[metragemCabo], 0) ");
        sb.appendLine("FROM " + mTabelaPOS + " ccp ");
        sb.appendLine("LEFT JOIN ModeloPOS mp ");
        sb.appendLine("ON ccp.[idTipoMaquina] = mp.[idTipoMaquina] ");
        sb.appendLine("WHERE 1 = 1 ");
        if (apenasAtiva) {
            sb.appendLine("AND ccp.[situacao] = 1 ");
        }
        return sb.toString();
    }

    private List<ClienteCadastroPOS> buscarPOS(String consulta, String[] args) {
        if (args == null) args = new String[]{};

        dataSyncFixIds.clear();

        List<ClienteCadastroPOS> modelos = new ArrayList<>();
        Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(consulta, args);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    final ClienteCadastroPOS modelo = new ClienteCadastroPOS();
                    modelo.setId(cursor.getInt(0));
                    modelo.setIdAppMobile(cursor.getInt(1));
                    modelo.setIdClienteCadastro(cursor.getInt(2));
                    modelo.setIdTipoMaquina(cursor.getInt(3));
                    modelo.setValorAluguel(cursor.getDouble(4));
                    modelo.setIdTerminal(cursor.getString(5));

                    final String rawDs = cursor.isNull(6) ? null : cursor.getString(6);
                    if (rawDs != null) {
                        final java.util.Date parsed = DataSyncManager.parse(rawDs);
                        if (parsed != null) {
                            modelo.setDataSync(parsed);
                        } else {
                            modelo.setDataSync(null);
                            final int id = cursor.getInt(0);
                            Timber.w(
                                    "[DATASYNC][ClienteCadastroPOS] DataSync inválido lido da base; agendando correção para NULL. id=%s raw=%s",
                                    id, rawDs
                            );
                            DataSyncValidator.warnInvalid(
                                    rawDs,
                                    "ClienteCadastroPOS",
                                    "id",
                                    String.valueOf(id)
                            );
                            enqueueDataSyncFix(id);
                        }
                    } else {
                        modelo.setDataSync(null);
                    }

                    modelo.setSituacao(cursor.getInt(7));
                    modelo.setTipoConexao(cursor.getInt(8));
                    modelo.setPosDescricao(cursor.getString(9));
                    modelo.setPosModelo(cursor.getString(10));
                    modelo.setCpfCnpjCliente(cursor.getString(11));

                    final int idOperadora = cursor.getInt(12);
                    modelo.setIdOperadora(idOperadora == 0 ? null : idOperadora);

                    final int metragemCabo = cursor.getInt(13);
                    modelo.setMetragemCabo(metragemCabo == 0 ? null : metragemCabo);

                    modelos.add(modelo);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            runDataSyncFixQueue();
        }
        return modelos;
    }

    private void enqueueDataSyncFix(int id) {
        dataSyncFixIds.add(id);
    }

    private void runDataSyncFixQueue() {
        if (dataSyncFixIds.isEmpty()) return;

        for (Integer id : dataSyncFixIds) {
            final ContentValues fix = new ContentValues();
            fix.putNull("dataSync");
            try {
                SimpleDbHelper.INSTANCE.open(context)
                        .update(mTabelaPOS, fix, "[id]=?", new String[]{String.valueOf(id)});
                Timber.i(
                        "[DATASYNC][ClienteCadastroPOS] Correção automática aplicada: dataSync=NULL id=%s",
                        id
                );
            } catch (Exception e) {
                Timber.e(
                        e,
                        "[DATASYNC][ClienteCadastroPOS] Erro ao aplicar correção automática de dataSync para id=%s",
                        id
                );
            }
        }
        dataSyncFixIds.clear();
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaPOS, null, null);
    }
}