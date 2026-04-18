package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterSexo;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus;
import com.axys.redeflexmobile.shared.models.migracao.CadastroMigracaoSubHorario;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public class DBRegisterMigrationSub {
    private final Context mContext;
    private final String TABLE_NAME = "CadastroMigracaoSub";
    private final String WHERE_ID_CONDITION = "[idAppMobile] = ?";

    public DBRegisterMigrationSub(Context pContext) {
        mContext = pContext;
    }

    public void add(RegisterMigrationSub item) throws Exception {
        ContentValues values = new ContentValues();
        if (item.getId() != null) {
            values.put("idAppMobile", item.getId());
        }
        values.put("idServer", item.getIdServer());

        if (item.getIdVendedor() != null) {
            values.put("idVendedor", item.getIdVendedor());
        }
        if (item.getIdCliente() != null) {
            values.put("idCliente", item.getIdCliente());
        }
        if (item.getEmail() != null) {
            values.put("email", item.getEmail());
        }
        if (item.getTelefoneCelular() != null) {
            values.put("telefoneCelular", item.getTelefoneCelular());
        }
        if (item.getTipoConta() != null) {
            values.put("tipoConta", item.getTipoConta());
        }
        if (item.getIdBanco() != null) {
            values.put("idBanco", item.getIdBanco());
        }
        if (item.getAgencia() != null) {
            values.put("agencia", item.getAgencia());
        }
        if (item.getDigitoAgencia() != null) {
            values.put("digitoAgencia", item.getDigitoAgencia());
        }
        if (item.getConta() != null) {
            values.put("conta", item.getConta());
        }
        if (item.getDigitoConta() != null) {
            values.put("digitoConta", item.getDigitoConta());
        }
        if (item.getVersaoApp() != null) {
            values.put("versaoApp", item.getVersaoApp());
        }
        if (item.getIdMcc() != null) {
            values.put("idMcc", item.getIdMcc());
        }
        if (item.getFaturamentoMedioPrevisto() > 0) {
            values.put("faturamentoMedioPrevisto", item.getFaturamentoMedioPrevisto());
        }
        if (item.getIdPrazoNegociacao() != null) {
            values.put("idPrazoNegociacao", item.getIdPrazoNegociacao());
        }
        if (item.isAntecipacao() != null) {
            values.put("antecipacao", item.isAntecipacao());
        }
        if (item.getIdMotivoRecusa() != null) {
            values.put("idMotivoRecusa", item.getIdMotivoRecusa());
        }
        if (item.getObservacaoRecusa() != null) {
            values.put("observacaoRecusa", item.getObservacaoRecusa());
        }
        if (item.getLatitude() > 0) {
            values.put("latitude", item.getLatitude());
        }
        if (item.getLongitude() > 0) {
            values.put("longitude", item.getLongitude());
        }
        if (item.getPrecisao() > 0) {
            values.put("precisao", item.getPrecisao());
        }
        values.put("sync", item.isSync());
        if (item.getToken() != null) {
            values.put("token", item.getToken());
        }
        if (item.getConfirmado() != null) {
            values.put("confirmado", item.getConfirmado());
        }
        if (item.getCreatedAt() != null) {
            values.put("criado_em", item.getCreatedAt());
        }
        if (item.getSituacao() != null) {
            values.put("situacao", item.getSituacao());
        }
        if (item.getRetorno() != null) {
            values.put("retorno", item.getRetorno());
        }
        if (item.getSexo() != null) {
            values.put("Sexo", item.getSexo().getCharValue());
        }
        if (item.getRG() != null) {
            values.put("RG", item.getRG());
        }
        if (item.getIdProfissao()  != null) {
            values.put("IdProfissao", item.getIdProfissao());
        }
        if (item.getIdRendaMensal()  != null) {
            values.put("IdRendaMensal", item.getIdRendaMensal());
        }
        if (item.getIdPatrimonio()  != null) {
            values.put("IdPatrimonio", item.getIdPatrimonio());
        }
        if (item.getFaturamentoBruto() > 0) {
            values.put("FaturamentoBruto", item.getFaturamentoBruto());
        }
        if (item.getPercVendaCartao() > 0) {
            values.put("PercVendaCartao", item.getPercVendaCartao());
        }
        if (item.getPercFaturamentoEcommerce() > 0) {
            values.put("PercFaturamentoEcommerce", item.getPercFaturamentoEcommerce());
        }
        if (item.getPercFaturamento() > 0) {
            values.put("PercFaturamento", item.getPercFaturamento());
        }
        if (item.getEntregaPosCompra() != null) {
            values.put("EntregaPosCompra", item.getEntregaPosCompra());
        }
        if (item.getPrazoEntrega() > 0) {
            values.put("PrazoEntrega", item.getPrazoEntrega());
        }
        if (item.getPercEntregaImediata() > 0) {
            values.put("PercEntregaImediata", item.getPercEntregaImediata());
        }
        if (item.getPercEntregaPosterior() > 0) {
            values.put("PercEntregaPosterior", item.getPercEntregaPosterior());
        }
        values.put("DataFundacaoPF", Util_IO.dateTimeToString(item.getDataFundacaoPF(), Config.FormatDateTimeStringBanco));

        if (item.getTipoMigracao() != null) {
            values.put("TipoMigracao", item.getTipoMigracao());
        }

        if (!Util_DB.isCadastrado(mContext, TABLE_NAME, "idAppMobile", String.valueOf(item.getId())))
            item.setId((int) SimpleDbHelper.INSTANCE.open(mContext).insert(TABLE_NAME, null, values));
        else
            SimpleDbHelper.INSTANCE.open(mContext).update(TABLE_NAME, values, WHERE_ID_CONDITION,
                    new String[]{String.valueOf(item.getId())});

        // Atualiza os dados de Horario Funcionamento
        addHorarioFuncionamento(item.getId(), item.getHorarioFuncionamento());
    }

    private void addHorarioFuncionamento(long pIdCadastroMigracao, List<CadastroMigracaoSubHorario> horarioList) {
        if (horarioList == null || horarioList.isEmpty()) return;
        DBCadastroMigracaoSubHorario dbCadastroMigracaoSubHorario = new DBCadastroMigracaoSubHorario(mContext);
        dbCadastroMigracaoSubHorario.deletaTudoPorId(pIdCadastroMigracao);
        Stream.of(horarioList)
                .map(tax -> {
                    tax.setIdCadastroMigracao((int) pIdCadastroMigracao);
                    return tax;
                })
                .forEach(dbCadastroMigracaoSubHorario::addCadastroMigracaoSubHorario);
    }

    public void updateSync(RegisterMigrationSub item) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        values.put("confirmado", 0);
        SimpleDbHelper.INSTANCE.open(mContext).update(TABLE_NAME, values, WHERE_ID_CONDITION,
                new String[]{String.valueOf(item.getId())});
    }

    public void updateStatus(RegisterMigrationSub item) {
        ContentValues values = new ContentValues();
        values.put("situacao", EnumRegisterStatus.SENT.getValue());
        SimpleDbHelper.INSTANCE.open(mContext).update(TABLE_NAME, values, WHERE_ID_CONDITION,
                new String[]{String.valueOf(item.getId())});
    }

    public RegisterMigrationSub getById(String idAppMobile) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine(WHERE_ID_CONDITION);
        return Utilidades.firstOrDefault(getAll(sb.toString(), new String[]{idAppMobile}));
    }

    public void deleteById(String idAppMobile) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABLE_NAME, WHERE_ID_CONDITION, new String[]{idAppMobile});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABLE_NAME, null, null);
    }

    public ArrayList<RegisterMigrationSub> getAll(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT idAppMobile");
        sb.appendLine(", idVendedor ");
        sb.appendLine(", idCliente ");
        sb.appendLine(", email ");
        sb.appendLine(", telefoneCelular ");
        sb.appendLine(", tipoConta ");
        sb.appendLine(", idBanco ");
        sb.appendLine(", agencia ");
        sb.appendLine(", digitoAgencia ");
        sb.appendLine(", conta ");
        sb.appendLine(", digitoConta ");
        sb.appendLine(", versaoApp ");
        sb.appendLine(", idMcc ");
        sb.appendLine(", faturamentoMedioPrevisto ");
        sb.appendLine(", idPrazoNegociacao ");
        sb.appendLine(", antecipacao ");
        sb.appendLine(", idMotivoRecusa ");
        sb.appendLine(", observacaoRecusa ");
        sb.appendLine(", latitude ");
        sb.appendLine(", longitude ");
        sb.appendLine(", precisao ");
        sb.appendLine(", dataCadastro ");
        sb.appendLine(", token ");
        sb.appendLine(", sync ");
        sb.appendLine(", confirmado ");
        sb.appendLine(", criado_em ");
        sb.appendLine(", situacao ");
        sb.appendLine(", retorno ");
        sb.appendLine(", idServer ");
        sb.appendLine(", Sexo ");
        sb.appendLine(", RG ");
        sb.appendLine(", IdProfissao ");
        sb.appendLine(", IdRendaMensal ");
        sb.appendLine(", IdPatrimonio ");
        sb.appendLine(", FaturamentoBruto ");
        sb.appendLine(", PercVendaCartao ");
        sb.appendLine(", PercFaturamentoEcommerce ");
        sb.appendLine(", PercFaturamento ");
        sb.appendLine(", EntregaPosCompra ");
        sb.appendLine(", PrazoEntrega ");
        sb.appendLine(", PercEntregaImediata ");
        sb.appendLine(", PercEntregaPosterior ");
        sb.appendLine(", DataFundacaoPF ");
        sb.appendLine(", TipoMigracao ");
        sb.appendLine("FROM [" + TABLE_NAME + "] ");
        sb.appendLine("WHERE 1 = 1 ");
        if (pCondicao != null)
            sb.append("AND " + pCondicao);

        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<RegisterMigrationSub> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<RegisterMigrationSub> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            if (cursor.moveToFirst()) {
                do {
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
                    item.setDataCadastro(cursor.getString(21));
                    item.setToken(cursor.getString(22));
                    item.setSync(cursor.getInt(23) == 1);
                    item.setConfirmado(cursor.getInt(24));
                    item.setCreatedAt(cursor.getString(25));
                    item.setSituacao(cursor.getInt(26));
                    item.setRetorno(cursor.getString(27));
                    item.setIdServer(cursor.getInt(28));

                    if (!Util_IO.isNullOrEmpty(cursor.getString(29)))
                        item.setSexo(EnumRegisterSexo.getEnumByCharValue(cursor.getString(29)));

                    item.setRG(cursor.getString(30));
                    item.setIdProfissao(cursor.getInt(31));
                    item.setIdRendaMensal(cursor.getInt(32));
                    item.setIdPatrimonio(cursor.getInt(33));
                    item.setFaturamentoBruto(cursor.getDouble(34));
                    item.setPercVendaCartao(cursor.getInt(35));
                    item.setPercFaturamentoEcommerce(cursor.getInt(36));
                    item.setPercFaturamento(cursor.getInt(37));
                    item.setEntregaPosCompra(cursor.getString(38));
                    item.setPrazoEntrega(cursor.getInt(39));
                    item.setPercEntregaImediata(cursor.getInt(40));
                    item.setPercEntregaPosterior(cursor.getInt(41));
                    item.setDataFundacaoPF(Util_IO.stringToDate(cursor.getString(42), Config.FormatDateTimeStringBanco));
                    item.setTipoMigracao(cursor.getString(43));
                    lista.add(item);
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

    public boolean existeCadastroPorTipoConta(Integer tipoConta) {
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext)
                .query(
                        TABLE_NAME,
                        new String[]{"idAppMobile"},
                        "[tipoConta]=?",
                        new String[]{tipoConta.toString()},
                        null,
                        null,
                        null,
                        null
                )
        ) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            Timber.e(ex);
            return false;
        }
    }
}
