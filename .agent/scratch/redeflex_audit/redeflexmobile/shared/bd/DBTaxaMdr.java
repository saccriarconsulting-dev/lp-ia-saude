package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.bd.DBTaxaMdr.Tabela.COLUNA_ID_APP;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;

/**
 * @author Rogério Massa on 06/02/19.
 */

public class DBTaxaMdr {

    static final String UPDATE_70_SQL_INSERIR_ID_PRAZO_NEGOCIACAO = Tabela.atualizarTabela70PrazoNegociacao();
    static final String UPDATE_70_SQL_INSERIR_FATURAMENTO_INICIAL = Tabela.atualizarTabela70FaturamentoInicial();
    static final String UPDATE_70_SQL_INSERIR_FATURAMENTO_FINAL = Tabela.atualizarTabela70FaturamentoFinal();
    static final String UPDATE_70_SQL_INSERIR_TAXA_DEBITO = Tabela.atualizarTabela70Debito();
    static final String UPDATE_70_SQL_INSERIR_TAXA_CREDITO = Tabela.atualizarTabela70Credito();
    static final String UPDATE_70_SQL_INSERIR_TAXA_ATE_6 = Tabela.atualizarTabela70Ate6();
    static final String UPDATE_70_SQL_INSERIR_TAXA_MAIOR_6 = Tabela.atualizarTabela70Maior6();
    static final String SQL = DBTaxaMdr.Tabela.criarTabela();
    private final Context context;

    public DBTaxaMdr(Context context) {
        this.context = context;
    }

    public void salvar(TaxaMdr tax) {
        if (tax.getId() != null && checkIfExists(tax.getId())) {
            atualizar(tax);
            return;
        }

        ContentValues contentValues = Tabela.converter(tax);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        Tabela.NOME,
                        null,
                        contentValues
                );
    }

    private void atualizar(TaxaMdr tax) {
        ContentValues contentValues = Tabela.converter(tax);
        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        Tabela.NOME,
                        contentValues,
                        Tabela.filtroPadrao(),
                        new String[]{String.valueOf(tax.getId())}
                );
    }

    public boolean checkIfExists(int id) {
        Cursor cursor = SimpleDbHelper.INSTANCE.open(context).query(
                Tabela.NOME,
                new String[]{ COLUNA_ID_APP },
                COLUNA_ID_APP + "=?",
                new String[]{ String.valueOf(id) },
                null,
                null,
                null,
                "1"   // LIMIT 1
        );

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public TaxaMdr pegarPorId(int id) {
        TaxaMdr tax = null;
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .query(
                        Tabela.NOME,
                        Tabela.COLUNAS,
                        Tabela.filtroPadrao(),
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null
                )) {

            while (cursor.moveToNext()) {
                tax = Tabela.converter(cursor);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return tax;
    }

    public TaxaMdr pegarPorMcc(String mcc) {
        TaxaMdr tax = null;
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .query(
                        Tabela.NOME,
                        Tabela.COLUNAS,
                        Tabela.filtroMcc(),
                        new String[]{mcc},
                        Tabela.COLUNA_MCC,
                        null,
                        null
                )) {

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return Tabela.converter(cursor);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return tax;
    }

    public List<TaxaMdr> pegarTodas() {
        List<TaxaMdr> list = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .query(
                        Tabela.NOME,
                        Tabela.COLUNAS,
                        String.format("[%s] = ?", Tabela.COLUNA_ATIVO),
                        new String[]{String.valueOf(1)},
                        null,
                        null,
                        null)) {

            while (cursor.moveToNext()) {
                list.add(Tabela.converter(cursor));
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return list;
    }

    public List<TaxaMdr> getAllMccByPersonType(int personType) {
        List<TaxaMdr> taxList = new ArrayList<>();

        String filter = String.format("[%s] = ? AND [%s] is not null",
                Tabela.COLUNA_TIPO_PESSOA,
                Tabela.COLUNA_TIPO_BANDEIRA);

        String[] params = new String[]{String.valueOf(personType)};

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .query(
                        Tabela.NOME,
                        Tabela.COLUNAS,
                        filter,
                        params,
                        Tabela.COLUNA_MCC,
                        null,
                        null
                )) {

            while (cursor.moveToNext()) {
                taxList.add(Tabela.converter(cursor));
            }

        } catch (Exception e) {
            Timber.e(e);
        }

        return taxList;
    }

    public List<TaxaMdr> getMccListByPersonAndIdNegotiationPeriod(int personType, int idPeriodNegotiation) {
        List<TaxaMdr> taxList = new ArrayList<>();

        String filter = String.format("[%s] = ? AND [%s] = ? AND [%s] IS NOT NULL",
                Tabela.COLUNA_TIPO_PESSOA,
                Tabela.COLUNA_ID_PRAZO_NEGOCIACAO,
                Tabela.COLUNA_TIPO_BANDEIRA);

        String[] params = new String[]{String.valueOf(personType), String.valueOf(idPeriodNegotiation)};

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .query(
                        Tabela.NOME,
                        Tabela.COLUNAS,
                        filter,
                        params,
                        Tabela.COLUNA_MCC,
                        null,
                        null
                )) {

            while (cursor.moveToNext()) {
                taxList.add(Tabela.converter(cursor));
            }

        } catch (Exception e) {
            Timber.e(e);
        }

        return taxList;
    }

    @NonNull
    public List<TaxaMdr> getAllTaxFlagTypes(int personTypeId,
                                            @androidx.annotation.Nullable Double foreseenRevenue,
                                            int negotiationTermId,
                                            int mcc) {
        final List<TaxaMdr> taxList = new ArrayList<>();

        final double raw  = (foreseenRevenue != null ? foreseenRevenue : 0d);
        final double safe = (Double.isFinite(raw) ? raw : 0d);
        final String revenueArg = java.math.BigDecimal.valueOf(safe)
                .setScale(2, java.math.RoundingMode.HALF_UP)
                .toPlainString();

        final String selection =
                "[" + Tabela.COLUNA_TIPO_PESSOA         + "] = ? AND " +
                        "[" + Tabela.COLUNA_FATURAMENTO_INICIAL + "] <= ? AND " +
                        "[" + Tabela.COLUNA_FATURAMENTO_FINAL   + "] >= ? AND " +
                        "[" + Tabela.COLUNA_ID_PRAZO_NEGOCIACAO + "] = ? AND " +
                        "[" + Tabela.COLUNA_MCC                 + "] = ? AND " +
                        "[" + Tabela.COLUNA_ATIVO               + "] = ?";

        final String[] selectionArgs = new String[] {
                String.valueOf(personTypeId),
                revenueArg,
                revenueArg,
                String.valueOf(negotiationTermId),
                String.valueOf(mcc),
                "1"
        };

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).query(
                Tabela.NOME, Tabela.COLUNAS, selection, selectionArgs, null, null, Tabela.COLUNA_TIPO_BANDEIRA)) {

            while (cursor != null && cursor.moveToNext()) {
                taxList.add(Tabela.converter(cursor));
            }

            timber.log.Timber.d("[MDR][QUERY] personType=%d revenue=%s termId=%d mcc=%d -> rows=%d",
                    personTypeId, revenueArg, negotiationTermId, mcc, taxList.size());

        } catch (Exception e) {
            timber.log.Timber.e(e, "[MDR][QUERY][ERROR] personType=%d revenue=%s termId=%d mcc=%d",
                    personTypeId, revenueArg, negotiationTermId, mcc);
        }

        return taxList;
    }


    public TaxaMdr getTaxasBandeira(int personTypeId,
                                    Double foreseenRevenue,
                                    int negotiationTermId,
                                    int mcc,
                                    int bandeiraId,
                                    int TipoClassificacao,
                                    int TipoNegociacao,
                                    int Rav) {

        TaxaMdr tax = null;

        String filter = String.format("[%s] = ? AND [%s] <= ? AND [%s] >= ? AND [%s] = ? AND [%s] = ? AND [%s] = ? AND [%s] = ? AND [%s] = ? AND [%s] = ? AND [%s] = ?",
                Tabela.COLUNA_TIPO_PESSOA,
                Tabela.COLUNA_FATURAMENTO_INICIAL,
                Tabela.COLUNA_FATURAMENTO_FINAL,
                Tabela.COLUNA_ID_PRAZO_NEGOCIACAO,
                Tabela.COLUNA_MCC,
                Tabela.COLUNA_TIPO_BANDEIRA,
                Tabela.COLUNA_ATIVO,
                Tabela.COLUNA_TIPO_CLASSIFICACAO,
                Tabela.COLUNA_TIPO_NEGOCIACAO,
                Tabela.COLUNA_RAV);

        String[] params = new String[]{
                String.valueOf(personTypeId),
                String.valueOf(foreseenRevenue),
                String.valueOf(foreseenRevenue),
                String.valueOf(negotiationTermId),
                String.valueOf(mcc),
                String.valueOf(bandeiraId),
                String.valueOf(1),
                String.valueOf(TipoClassificacao),
                String.valueOf(TipoNegociacao),
                String.valueOf(Rav)
        };

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .query(
                        Tabela.NOME,
                        Tabela.COLUNAS,
                        filter,
                        params,
                        Tabela.COLUNA_TIPO_BANDEIRA,
                        null,
                        Tabela.COLUNA_TIPO_BANDEIRA
                )) {

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return Tabela.converter(cursor);
            }

        } catch (Exception e) {
            Timber.e(e);
        }

        return tax;
    }

    public double getAnticipation(int personType) {
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .query(
                        Tabela.NOME,
                        Tabela.COLUNAS,
                        String.format("[%s] = ?", Tabela.COLUNA_TIPO_PESSOA),
                        new String[]{String.valueOf(personType)},
                        null,
                        null,
                        null
                )) {

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                TaxaMdr tax = Tabela.converter(cursor);
                return tax.getAnticipation();
            }

        } catch (Exception e) {
            Timber.e(e);
        }

        return EMPTY_DOUBLE;
    }

    public void deletaTudo() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBTaxaMdr.Tabela.NOME, null, null);
    }

    public static class Tabela {
        static final String NOME = "TaxaMdr";
        static final String COLUNA_ID_APP = "id";
        static final String COLUNA_MCC = "mcc";
        static final String COLUNA_MCC_DESCRICAO = "descricao";
        static final String COLUNA_ID_PRAZO_NEGOCIACAO = "idPrazoNegociacao";
        static final String COLUNA_TIPO_PESSOA = "tipoPessoa";
        static final String COLUNA_TIPO_BANDEIRA = "tipoBandeira";
        static final String COLUNA_FATURAMENTO_INICIAL = "faturamentoInicial";
        static final String COLUNA_FATURAMENTO_FINAL = "faturamentoFinal";
        static final String COLUNA_DEBITO = "taxaDebito";
        static final String COLUNA_CREDITO = "taxaCreditoAVista";
        static final String COLUNA_ATE_6 = "taxaAte6";
        static final String COLUNA_MAIOR_6 = "taxaMaior6";
        static final String COLUNA_ANTECIPACAO = "antecipacaoAutomatica";

        //region Campos removidos
        static final String COLUNA_ATIVO = "ativo";
        static final String COLUNA_MIN_DEBITO = "minDebito";
        static final String COLUNA_MIN_CREDITO = "minCreditoAVista";
        static final String COLUNA_MIN_ATE_6 = "minAte6";
        static final String COLUNA_MIN_MAIOR_6 = "minMaior6";
        static final String COLUNA_MAX_DEBITO = "maxDebito";
        static final String COLUNA_MAX_CREDITO = "maxCreditoAVista";
        static final String COLUNA_MAX_ATE_6 = "maxAte6";
        static final String COLUNA_MAX_MAIOR_6 = "maxMaior6";
        //endregion
        static final String COLUNA_TIPO_CLASSIFICACAO = "TipoClassificacao";
        static final String COLUNA_TIPO_NEGOCIACAO = "TipoNegociacao";
        static final String COLUNA_RAV = "RAV";

        static final String[] COLUNAS = new String[]{
                COLUNA_ID_APP,
                COLUNA_MCC,
                COLUNA_MCC_DESCRICAO,
                COLUNA_ID_PRAZO_NEGOCIACAO,
                COLUNA_TIPO_PESSOA,
                COLUNA_TIPO_BANDEIRA,
                COLUNA_FATURAMENTO_INICIAL,
                COLUNA_FATURAMENTO_FINAL,
                COLUNA_DEBITO,
                COLUNA_CREDITO,
                COLUNA_ATE_6,
                COLUNA_MAIOR_6,
                COLUNA_ANTECIPACAO,
                COLUNA_ATIVO,
                COLUNA_TIPO_CLASSIFICACAO,
                COLUNA_TIPO_NEGOCIACAO,
                COLUNA_RAV
        };

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2)," +
                            "[%s] DECIMAL(20, 2))",
                    NOME,
                    COLUNA_ID_APP,
                    COLUNA_MCC,
                    COLUNA_TIPO_PESSOA,
                    COLUNA_TIPO_BANDEIRA,
                    COLUNA_ATIVO,
                    COLUNA_MIN_DEBITO,
                    COLUNA_MIN_CREDITO,
                    COLUNA_MIN_ATE_6,
                    COLUNA_MIN_MAIOR_6,
                    COLUNA_MAX_DEBITO,
                    COLUNA_MAX_CREDITO,
                    COLUNA_MAX_ATE_6,
                    COLUNA_MAX_MAIOR_6,
                    COLUNA_ANTECIPACAO
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID_APP);
        }

        static String filtroMcc() {
            return COLUNA_MCC + " = ? ";
        }

        private static ContentValues converter(TaxaMdr tax) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_APP, tax.getId());
            values.put(COLUNA_MCC, tax.getMcc());
            values.put(COLUNA_MCC_DESCRICAO, tax.getDescription());
            values.put(COLUNA_TIPO_PESSOA, tax.getPersonType());
            values.put(COLUNA_TIPO_BANDEIRA, tax.getFlagType());
            values.put(COLUNA_ID_PRAZO_NEGOCIACAO, tax.getNegotiationTermId());
            values.put(COLUNA_FATURAMENTO_INICIAL, tax.getBillingInitial());
            values.put(COLUNA_FATURAMENTO_FINAL, tax.getBillingFinal());
            values.put(COLUNA_DEBITO, tax.getTaxDebit());
            values.put(COLUNA_CREDITO, tax.getTaxCredit());
            values.put(COLUNA_ATE_6, tax.getTaxUntilSix());
            values.put(COLUNA_MAIOR_6, tax.getTaxBiggerSix());
            values.put(COLUNA_ANTECIPACAO, tax.getAnticipation());
            values.put(COLUNA_ATIVO, Util_IO.booleanToNumber(tax.isActivated()));
            values.put(COLUNA_TIPO_CLASSIFICACAO, tax.getTipoClassificacao());
            values.put(COLUNA_TIPO_NEGOCIACAO, tax.getTipoNegociacao());
            values.put(COLUNA_RAV, Util_IO.booleanToNumber(tax.isRAV()));
            return values;
        }

        private static TaxaMdr converter(Cursor cursor) {
            return new TaxaMdr(
                    getInt(cursor, COLUNA_ID_APP),
                    getInt(cursor, COLUNA_MCC),
                    getString(cursor, COLUNA_MCC_DESCRICAO),
                    getInt(cursor, COLUNA_ID_PRAZO_NEGOCIACAO),
                    getInt(cursor, COLUNA_TIPO_PESSOA),
                    getInt(cursor, COLUNA_TIPO_BANDEIRA),
                    getDouble(cursor, COLUNA_FATURAMENTO_INICIAL),
                    getDouble(cursor, COLUNA_FATURAMENTO_FINAL),
                    getDouble(cursor, COLUNA_DEBITO),
                    getDouble(cursor, COLUNA_CREDITO),
                    getDouble(cursor, COLUNA_ATE_6),
                    getDouble(cursor, COLUNA_MAIOR_6),
                    getDouble(cursor, COLUNA_ANTECIPACAO),
                    Util_IO.numberToBoolean(getInt(cursor, COLUNA_ATIVO)),
                    getString(cursor, COLUNA_TIPO_CLASSIFICACAO),
                    getInt(cursor, COLUNA_TIPO_NEGOCIACAO),
                    Util_IO.numberToBoolean(getInt(cursor, COLUNA_RAV))
            );
        }

        private static String atualizarTabela70PrazoNegociacao() {
            return "ALTER TABLE " + NOME + " ADD COLUMN [" + COLUNA_ID_PRAZO_NEGOCIACAO + "] INTEGER";
        }

        private static String atualizarTabela70FaturamentoInicial() {
            return "ALTER TABLE " + NOME + " ADD COLUMN [" + COLUNA_FATURAMENTO_INICIAL + "] DECIMAL(20, 2)";
        }

        private static String atualizarTabela70FaturamentoFinal() {
            return "ALTER TABLE " + NOME + " ADD COLUMN [" + COLUNA_FATURAMENTO_FINAL + "] DECIMAL(20, 2)";
        }

        private static String atualizarTabela70Debito() {
            return "ALTER TABLE " + NOME + " ADD COLUMN [" + COLUNA_DEBITO + "] DECIMAL(20, 2)";
        }

        private static String atualizarTabela70Credito() {
            return "ALTER TABLE " + NOME + " ADD COLUMN [" + COLUNA_CREDITO + "] DECIMAL(20, 2)";
        }

        private static String atualizarTabela70Ate6() {
            return "ALTER TABLE " + NOME + " ADD COLUMN [" + COLUNA_ATE_6 + "] DECIMAL(20, 2)";
        }

        private static String atualizarTabela70Maior6() {
            return "ALTER TABLE " + NOME + " ADD COLUMN [" + COLUNA_MAIOR_6 + "] DECIMAL(20, 2)";
        }

        private static String getString(Cursor cursor, String coluna) {
            return cursor.getString(cursor.getColumnIndex(coluna));
        }

        private static int getInt(Cursor cursor, String coluna) {
            return cursor.getInt(cursor.getColumnIndex(coluna));
        }

        private static double getDouble(Cursor cursor, String coluna) {
            return cursor.getDouble(cursor.getColumnIndex(coluna));
        }
    }
}