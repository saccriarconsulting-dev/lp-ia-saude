package com.axys.redeflexmobile.shared.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterSexo;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.migracao.ClientMigrationResponse;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public class RegisterMigrationSubDaoImpl implements RegisterMigrationSubDao {
    private final String TABLE_NAME = "CadastroMigracaoSub";
    private final String QUERY_WHERE_ID = "[idAppMobile] = ?";

    private final Context context;

    public RegisterMigrationSubDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public @NotNull Single<RegisterMigrationSub> create(
            @NotNull RegisterMigrationSub registerMigrationSub) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                ContentValues values = mountContentValuesObject(registerMigrationSub);

                if (!Util_DB.isCadastrado(context, TABLE_NAME, "idAppMobile", String.valueOf(registerMigrationSub.getId()))) {
                    long createdId = SimpleDbHelper.INSTANCE.open(context).insert(TABLE_NAME, null, values);
                    registerMigrationSub.setId((int) createdId);
                } else {
                    SimpleDbHelper.INSTANCE.open(context).update(TABLE_NAME, values, QUERY_WHERE_ID, new String[]{String.valueOf(registerMigrationSub.getId())});
                }

                try {
                    // Grava os Dados Horario Funcionamento
                    for (int aa = 0; aa < registerMigrationSub.getHorarioFuncionamento().size(); aa++) {
                        ContentValues valuesHorario = new ContentValues();
                        valuesHorario.put("IdCliente", registerMigrationSub.getIdCliente());
                        valuesHorario.put("IdCadastroMigracao", registerMigrationSub.getId());
                        valuesHorario.put("DiaAtendimentoId", registerMigrationSub.getHorarioFuncionamento().get(aa).getDiaAtendimentoId());
                        valuesHorario.put("Aberto", registerMigrationSub.getHorarioFuncionamento().get(aa).getAberto());
                        valuesHorario.put("HoraInicio", registerMigrationSub.getHorarioFuncionamento().get(aa).getHoraInicio());
                        valuesHorario.put("HoraFim", registerMigrationSub.getHorarioFuncionamento().get(aa).getHoraFim());
                        SimpleDbHelper.INSTANCE.open(context).insert("CadastroMigracaoSubHorario", null, valuesHorario);
                    }
                }
                catch (Exception ex)
                {
                    Log.d("Redeflex", "create: " + ex.getMessage());
                }

                emitter.onSuccess(registerMigrationSub);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public @NotNull Observable<RegisterMigrationSub> obterPorId(String idAppMobile) {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            String sql = "SELECT " +
                    "idAppMobile, " +
                    "idVendedor, " +
                    "idCliente, " +
                    "email, " +
                    "telefoneCelular, " +
                    "tipoConta, " +
                    "idBanco, " +
                    "agencia, " +
                    "digitoAgencia, " +
                    "conta, " +
                    "digitoConta, " +
                    "versaoApp, " +
                    "idMcc, " +
                    "faturamentoMedioPrevisto, " +
                    "idPrazoNegociacao, " +
                    "antecipacao, " +
                    "idMotivoRecusa, " +
                    "observacaoRecusa, " +
                    "latitude, " +
                    "longitude, " +
                    "precisao, " +
                    "sync, " +
                    "dataCadastro, " +
                    "token, " +
                    "confirmado, " +
                    "criado_em, " +
                    "situacao, " +
                    "retorno " +
                    "Sexo," +
                    "RG," +
                    "IdProfissao," +
                    "IdRendaMensal," +
                    "IdPatrimonio," +
                    "FaturamentoBruto," +
                    "PercVendaCartao," +
                    "PercFaturamentoEcommerce," +
                    "PercFaturamento," +
                    "EntregaPosCompra," +
                    "PrazoEntrega," +
                    "PercEntregaImediata," +
                    "PercEntregaPosterior," +
                    "DataFundacaoPF," +
                    "TipoMigracao " +
                    "FROM [CadastroMigracaoSub] WHERE idAppMobile = ?";

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            Cursor cursor = db.rawQuery(sql, new String[]{idAppMobile});
            List<RegisterMigrationSub> items = moveCursor(cursor);
            cursor.close();

            emitter.onNext(items.size() > 0 ? items.get(0) : new RegisterMigrationSub());
            emitter.onComplete();
        });
    }

    @Override
    public @NotNull Observable<List<RegisterMigrationSub>> obterTodos() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            String sql = "SELECT " +
                    "idAppMobile, " +
                    "idVendedor, " +
                    "idCliente, " +
                    "email, " +
                    "telefoneCelular, " +
                    "tipoConta, " +
                    "idBanco, " +
                    "agencia, " +
                    "digitoAgencia, " +
                    "conta, " +
                    "digitoConta, " +
                    "versaoApp, " +
                    "idMcc, " +
                    "faturamentoMedioPrevisto, " +
                    "idPrazoNegociacao, " +
                    "antecipacao, " +
                    "idMotivoRecusa, " +
                    "observacaoRecusa, " +
                    "latitude, " +
                    "longitude, " +
                    "precisao, " +
                    "sync, " +
                    "dataCadastro, " +
                    "token, " +
                    "confirmado, " +
                    "criado_em, " +
                    "situacao, " +
                    "retorno " +
                    "Sexo," +
                    "RG," +
                    "IdProfissao," +
                    "IdRendaMensal," +
                    "IdPatrimonio," +
                    "FaturamentoBruto," +
                    "PercVendaCartao," +
                    "PercFaturamentoEcommerce," +
                    "PercFaturamento," +
                    "EntregaPosCompra," +
                    "PrazoEntrega," +
                    "PercEntregaImediata," +
                    "PercEntregaPosterior," +
                    "DataFundacaoPF," +
                    "TipoMigracao " +
                    "FROM [CadastroMigracaoSub]";

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            Cursor cursor = db.rawQuery(sql, null);
            List<RegisterMigrationSub> items = moveCursor(cursor);
            cursor.close();

            emitter.onNext(items);
            emitter.onComplete();
        });
    }

    @Override
    public @NotNull Single<RegisterMigrationSub> obterCadastroMigracaoPorClienteId(int clienteId, String pTipoMigracao) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                String sql = "SELECT " +
                        "idAppMobile, " +
                        "idVendedor, " +
                        "idCliente, " +
                        "email, " +
                        "telefoneCelular, " +
                        "tipoConta, " +
                        "idBanco, " +
                        "agencia, " +
                        "digitoAgencia, " +
                        "conta, " +
                        "digitoConta, " +
                        "versaoApp, " +
                        "idMcc, " +
                        "faturamentoMedioPrevisto, " +
                        "idPrazoNegociacao, " +
                        "antecipacao, " +
                        "idMotivoRecusa, " +
                        "observacaoRecusa, " +
                        "latitude, " +
                        "longitude, " +
                        "precisao, " +
                        "sync, " +
                        "dataCadastro, " +
                        "token, " +
                        "confirmado, " +
                        "criado_em, " +
                        "situacao, " +
                        "retorno " +
                        "Sexo," +
                        "RG," +
                        "IdProfissao," +
                        "IdRendaMensal," +
                        "IdPatrimonio," +
                        "FaturamentoBruto," +
                        "PercVendaCartao," +
                        "PercFaturamentoEcommerce," +
                        "PercFaturamento," +
                        "EntregaPosCompra," +
                        "PrazoEntrega," +
                        "PercEntregaImediata," +
                        "PercEntregaPosterior," +
                        "DataFundacaoPF," +
                        "TipoMigracao " +
                        "FROM [CadastroMigracaoSub] WHERE idCliente = " + clienteId + " and TipoMigracao = '" + pTipoMigracao + "'" ;

                SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
                Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(clienteId)});
                List<RegisterMigrationSub> items = moveCursor(cursor);
                cursor.close();
                if (items.size() > 0) {
                    emitter.onSuccess(items.get(0));
                } else {
                    emitter.onError(new Throwable("Não existe cadastro com o respectivo id : " + clienteId));
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public @NotNull Observable<List<ClientMigrationResponse>> mountListClientMigrationResponse(
            @NotNull List<Cliente> clients) {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            List<ClientMigrationResponse> response = new ArrayList<>();

            String sql = "SELECT " +
                    "idAppMobile, " +
                    "idVendedor, " +
                    "idCliente, " +
                    "email, " +
                    "telefoneCelular, " +
                    "tipoConta, " +
                    "idBanco, " +
                    "agencia, " +
                    "digitoAgencia, " +
                    "conta, " +
                    "digitoConta, " +
                    "versaoApp, " +
                    "idMcc, " +
                    "faturamentoMedioPrevisto, " +
                    "idPrazoNegociacao, " +
                    "antecipacao, " +
                    "idMotivoRecusa, " +
                    "observacaoRecusa, " +
                    "latitude, " +
                    "longitude, " +
                    "precisao, " +
                    "sync, " +
                    "dataCadastro, " +
                    "token, " +
                    "confirmado, " +
                    "criado_em, " +
                    "situacao, " +
                    "retorno, " +
                    "Sexo," +
                    "RG," +
                    "IdProfissao," +
                    "IdRendaMensal," +
                    "IdPatrimonio," +
                    "FaturamentoBruto," +
                    "PercVendaCartao," +
                    "PercFaturamentoEcommerce," +
                    "PercFaturamento," +
                    "EntregaPosCompra," +
                    "PrazoEntrega," +
                    "PercEntregaImediata," +
                    "PercEntregaPosterior," +
                    "DataFundacaoPF," +
                    "TipoMigracao " +
                    "FROM [CadastroMigracaoSub] WHERE idCliente = ? and (idMotivoRecusa IS NULL OR idMotivoRecusa = 0)";

            for (Cliente client : clients) {
                ClientMigrationResponse clientMigrationResponse = new ClientMigrationResponse();
                SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
                Cursor cursor = db.rawQuery(sql, new String[]{client.getId()});
                List<RegisterMigrationSub> registerMigrationSubList = moveCursor(cursor);
                cursor.close();

                clientMigrationResponse.setRegisterMigrationSub(registerMigrationSubList.size() > 0 ? registerMigrationSubList.get(0) : null);
                clientMigrationResponse.setClient(client);
                response.add(clientMigrationResponse);
            }

            emitter.onNext(response);
            emitter.onComplete();
        });
    }

    @Override
    public @NotNull Single<RegisterMigrationSub> createToken(
            @NotNull RegisterMigrationSub registerMigrationSub) {
        return Single.create(emitter -> {
            try {
                String token = subStringToken(registerMigrationSub.getDataCadastro());
                SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
                ContentValues values = new ContentValues();
                values.put("token", token);
                registerMigrationSub.setToken(token);
                int response = db.update(
                        TABLE_NAME,
                        values,
                        "idAppMobile = ?",
                        new String[]{String.valueOf(registerMigrationSub.getId())}
                );
                if (token.length() > 0 && response >= 1) {
                    emitter.onSuccess(registerMigrationSub);
                } else {
                    emitter.onError(new Throwable());
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public @NotNull Observable<Boolean> validateToken(int clientId, String token) {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            String sql = "SELECT " +
                    "idAppMobile, " +
                    "idVendedor, " +
                    "idCliente, " +
                    "email, " +
                    "telefoneCelular, " +
                    "tipoConta, " +
                    "idBanco, " +
                    "agencia, " +
                    "digitoAgencia, " +
                    "conta, " +
                    "digitoConta, " +
                    "versaoApp, " +
                    "idMcc, " +
                    "faturamentoMedioPrevisto, " +
                    "idPrazoNegociacao, " +
                    "antecipacao, " +
                    "idMotivoRecusa, " +
                    "observacaoRecusa, " +
                    "latitude, " +
                    "longitude, " +
                    "precisao, " +
                    "sync, " +
                    "dataCadastro, " +
                    "token, " +
                    "confirmado, " +
                    "criado_em, " +
                    "situacao, " +
                    "retorno, " +
                    "Sexo," +
                    "RG," +
                    "IdProfissao," +
                    "IdRendaMensal," +
                    "IdPatrimonio," +
                    "FaturamentoBruto," +
                    "PercVendaCartao," +
                    "PercFaturamentoEcommerce," +
                    "PercFaturamento," +
                    "EntregaPosCompra," +
                    "PrazoEntrega," +
                    "PercEntregaImediata," +
                    "PercEntregaPosterior," +
                    "DataFundacaoPF," +
                    "TipoMigracao " +
                    "FROM [CadastroMigracaoSub] WHERE idCliente = ? and (idMotivoRecusa IS NULL OR idMotivoRecusa = 0)";

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(clientId)});
            List<RegisterMigrationSub> list = moveCursor(cursor);
            cursor.close();

            if (list.isEmpty()) {
                emitter.onError(new IllegalStateException("Cliente não cadastrado na SUB."));
                return;
            }

            if (!subStringToken(list.get(0).getDataCadastro()).equals(token)) {
                emitter.onError(new IllegalStateException("Token inválido, verifique!."));
                return;
            }

            emitter.onNext(true);
            emitter.onComplete();
        });
    }

    @Override
    public @NotNull Single<Boolean> updateTokenConfirmation(int clientId) {
        return Single.create(emitter -> {
            ContentValues values = new ContentValues();
            values.put("confirmado", 1);
            values.put("sync", 0);
            values.put("situacao", 0);

            SimpleDbHelper.INSTANCE.open(context)
                    .update(
                            TABLE_NAME,
                            values,
                            "idCliente = ?",
                            new String[]{String.valueOf(clientId)}
                    );

            emitter.onSuccess(true);
        });
    }

    private String subStringToken(String dataRegister) {
        if (dataRegister.length() > 5) {
            dataRegister = StringUtils.returnOnlyNumbers(dataRegister).trim();

            int indexBegin = dataRegister.length() - 5;
            int indexEnd = dataRegister.length();

            return dataRegister.substring(indexBegin, indexEnd);
        } else {
            return "";
        }

    }

    private List<RegisterMigrationSub> moveCursor(@NotNull Cursor cursor) {
        List<RegisterMigrationSub> lista = new ArrayList<>();
        while (cursor.moveToNext()) {
            RegisterMigrationSub item = new RegisterMigrationSub();
            item.setId(cursor.getInt(0));
            item.setIdVendedor(cursor.getInt(1));
            item.setIdCliente(cursor.getInt(2));
            item.setEmail(cursor.getString(3));
            item.setTelefoneCelular(cursor.getString(4));
            item.setTipoConta(cursor.getInt(5));
            item.setIdBanco(cursor.getInt(6));
            item.setAgencia(cursor.getString(7));
            item.setDigitoAgencia(cursor.getString(8));
            item.setConta(cursor.getString(9));
            item.setDigitoConta(cursor.getString(10));
            item.setVersaoApp(cursor.getString(11));
            item.setIdMcc(cursor.getInt(12));
            item.setFaturamentoMedioPrevisto(cursor.getDouble(13));
            item.setIdPrazoNegociacao(cursor.getInt(14));
            item.setAntecipacao(cursor.getInt(15) == 1);
            item.setIdMotivoRecusa(cursor.getInt(16));
            item.setObservacaoRecusa(cursor.getString(17));
            item.setLatitude(cursor.getDouble(18));
            item.setLongitude(cursor.getDouble(19));
            item.setPrecisao(cursor.getDouble(20));
            item.setSync(cursor.getInt(21) == 1);
            item.setDataCadastro(cursor.getString(22));
            item.setToken(cursor.getString(23));
            item.setConfirmado(cursor.getInt(24));
            item.setCreatedAt(cursor.getString(25));
            item.setSituacao(cursor.getInt(26));
            item.setRetorno(cursor.getString(27));
            if (!Util_IO.isNullOrEmpty(cursor.getString(28)))
                item.setSexo(EnumRegisterSexo.getEnumByCharValue(cursor.getString(28)));

            item.setRG(cursor.getString(29));
            item.setIdProfissao(cursor.getInt(30));
            item.setIdRendaMensal(cursor.getInt(31));
            item.setIdPatrimonio(cursor.getInt(32));
            item.setFaturamentoBruto(cursor.getDouble(33));
            item.setPercVendaCartao(cursor.getInt(34));
            item.setPercFaturamentoEcommerce(cursor.getInt(35));
            item.setPercFaturamento(cursor.getInt(36));
            item.setEntregaPosCompra(cursor.getString(37));
            item.setPrazoEntrega(cursor.getInt(38));
            item.setPercEntregaImediata(cursor.getInt(39));
            item.setPercEntregaPosterior(cursor.getInt(40));
            item.setDataFundacaoPF(Util_IO.stringToDate(cursor.getString(41), Config.FormatDateStringBanco));
            if (Util_IO.isNullOrEmpty(cursor.getString(42)))
                item.setTipoMigracao("ADQ");
            else
                item.setTipoMigracao(cursor.getString(42));
            lista.add(item);
        }
        return lista;
    }

    private ContentValues mountContentValuesObject(
            @NotNull RegisterMigrationSub registerMigrationSub) {
        ContentValues values = new ContentValues();
        values.put("idAppMobile", registerMigrationSub.getId());
        values.put("idVendedor", registerMigrationSub.getIdVendedor());
        values.put("idCliente", registerMigrationSub.getIdCliente());
        values.put("email", registerMigrationSub.getEmail());
        values.put("telefoneCelular", StringUtils.returnOnlyNumbers(registerMigrationSub.getTelefoneCelular()));
        values.put("tipoConta", registerMigrationSub.getTipoConta());
        values.put("idBanco", registerMigrationSub.getIdBanco());
        values.put("agencia", registerMigrationSub.getAgencia());
        values.put("digitoAgencia", registerMigrationSub.getDigitoAgencia());
        values.put("conta", registerMigrationSub.getConta());
        values.put("digitoConta", registerMigrationSub.getDigitoConta());
        values.put("versaoApp", registerMigrationSub.getVersaoApp());
        values.put("idMcc", registerMigrationSub.getIdMcc());
        values.put("faturamentoMedioPrevisto", registerMigrationSub.getFaturamentoMedioPrevisto());
        values.put("idPrazoNegociacao", registerMigrationSub.getIdPrazoNegociacao());
        values.put("antecipacao", registerMigrationSub.isAntecipacao());
        values.put("idMotivoRecusa", registerMigrationSub.getIdMotivoRecusa());
        values.put("observacaoRecusa", registerMigrationSub.getObservacaoRecusa());
        values.put("latitude", registerMigrationSub.getLatitude());
        values.put("longitude", registerMigrationSub.getLongitude());
        values.put("precisao", registerMigrationSub.getPrecisao());
        values.put("dataCadastro", registerMigrationSub.getDataCadastro());
        values.put("sync", registerMigrationSub.isSync());
        values.put("token", registerMigrationSub.getToken());
        values.put("confirmado", registerMigrationSub.getConfirmado());
        if (registerMigrationSub.getSexo() != null)
            values.put("Sexo", registerMigrationSub.getSexo().getCharValue());

        values.put("RG", registerMigrationSub.getRG());
        values.put("IdProfissao", registerMigrationSub.getIdProfissao());
        values.put("IdRendaMensal", registerMigrationSub.getIdRendaMensal());
        values.put("IdPatrimonio", registerMigrationSub.getIdPatrimonio());
        values.put("FaturamentoBruto", registerMigrationSub.getFaturamentoBruto());
        values.put("PercVendaCartao", registerMigrationSub.getPercVendaCartao());
        values.put("PercFaturamentoEcommerce", registerMigrationSub.getPercFaturamentoEcommerce());
        values.put("PercFaturamento", registerMigrationSub.getPercFaturamento());
        values.put("EntregaPosCompra", registerMigrationSub.getEntregaPosCompra());
        values.put("PrazoEntrega", registerMigrationSub.getPrazoEntrega());
        values.put("PercEntregaImediata", registerMigrationSub.getPercEntregaImediata());
        values.put("PercEntregaPosterior", registerMigrationSub.getPercEntregaPosterior());
        values.put("DataFundacaoPF", Util_IO.dateTimeToString(registerMigrationSub.getDataFundacaoPF(), Config.FormatDateTimeStringBanco));
        values.put("TipoMigracao", registerMigrationSub.getTipoMigracao());
        return values;
    }
}
