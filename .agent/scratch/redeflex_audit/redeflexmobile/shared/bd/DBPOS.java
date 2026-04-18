package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.models.UltimaDataTransacaoPOS;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rogério Massa on 06/11/18.
 */

public class DBPOS {

    static final String CREATE_ULTIMA_DATA_TANSACAO_POS = TabelaUltimaDataTransacaoPOS.criarTabela();
    static final String CREATE_INFORMACOES_GERAIS_POS = TabelaInformacoesGeraisPOS.criarTabela();
    static final String UPDATE_INFORMACOES_GERAIS_POS_73 = TabelaInformacoesGeraisPOS.atualizarTabelaVersao73();

    private Context context;

    public DBPOS(Context context) {
        this.context = context;
    }

    //region Methods
    public void addUltimaDataTransacaoPOS(UltimaDataTransacaoPOS item) {
        TabelaUltimaDataTransacaoPOS.addUltimaDataTransacaoPOS(context, item);
    }

    private List<UltimaDataTransacaoPOS> obterUltimaDataTransacaoPOSPorId(int clienteId) {
        return TabelaUltimaDataTransacaoPOS.obterUltimaDataTransacaoPOSPorId(context, clienteId);
    }

    public void addInformacaoGeralPOS(InformacaoGeralPOS item) {
        TabelaInformacoesGeraisPOS.addInformacaoGeralPOS(context, item);
    }

    public void removerInformacoesGeralPOSInativa() {
        TabelaInformacoesGeraisPOS.removerInativa(context);
    }

    public List<InformacaoGeralPOS> obterInformacoesGeraisPOSPorId(String clienteId) {
        return Stream.ofNullable(TabelaInformacoesGeraisPOS
                .obterInformacoesGeraisPOSPorId(context, clienteId))
                .map(this::prepararDataMaisRecente)
                .toList();
    }

    public InformacaoGeralPOS obterUltimaInformacoesGeraisPOSPorId(String clienteId) {
        return Stream.ofNullable(TabelaInformacoesGeraisPOS
                .obterInformacoesGeraisPOSPorId(context, clienteId))
                .map(this::prepararDataMaisRecente)
                .sortBy(InformacaoGeralPOS::getDataUltimaTransacao)
                .findFirst()
                .orElse(null);
    }

    private InformacaoGeralPOS prepararDataMaisRecente(InformacaoGeralPOS informacaoGeralPOS) {
        informacaoGeralPOS.setDataUltimaTransacao(new Date());
        if (informacaoGeralPOS.getDataUltimaTransacaoAdquirencia() != null && informacaoGeralPOS.getDataUltimaVendaRecarga() == null) {
            informacaoGeralPOS.setDataUltimaTransacao(informacaoGeralPOS.getDataUltimaTransacaoAdquirencia());
        } else if (informacaoGeralPOS.getDataUltimaTransacaoAdquirencia() == null && informacaoGeralPOS.getDataUltimaVendaRecarga() != null) {
            informacaoGeralPOS.setDataUltimaTransacao(informacaoGeralPOS.getDataUltimaVendaRecarga());
        } else if (informacaoGeralPOS.getDataUltimaTransacaoAdquirencia() != null && informacaoGeralPOS.getDataUltimaTransacaoAdquirencia().after(informacaoGeralPOS.getDataUltimaVendaRecarga())) {
            informacaoGeralPOS.setDataUltimaTransacao(informacaoGeralPOS.getDataUltimaTransacaoAdquirencia());
        } else {
            informacaoGeralPOS.setDataUltimaTransacao(informacaoGeralPOS.getDataUltimaVendaRecarga());
        }
        return informacaoGeralPOS;
    }

    public void removerPos(int id) {
        TabelaInformacoesGeraisPOS.remover(context, id);
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(TabelaUltimaDataTransacaoPOS.NOME_TABELA, null, null);
        SimpleDbHelper.INSTANCE.open(context).delete(TabelaInformacoesGeraisPOS.NOME_TABELA, null, null);
    }
    //endregion

    //region Tables
    private static class TabelaUltimaDataTransacaoPOS {

        private static final String NOME_TABELA = "UltimaDataTransacaoPOS";

        private static final String ID = "Id";
        private static final String ULTIMA_TRANSACAO_ID = "UltimaTransacaoPOSId";
        private static final String ID_TERMINAL = "IdTerminal";
        private static final String ID_CLIENTE = "IdCliente";
        private static final String DATA_TRANSACAO = "DataTransacao";

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] DATETIME);",
                    NOME_TABELA,
                    ID,
                    ULTIMA_TRANSACAO_ID,
                    ID_TERMINAL,
                    ID_CLIENTE,
                    DATA_TRANSACAO);
        }

        private static String obterQuerySelect() {
            return String.format("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = ?",
                    ID,
                    ULTIMA_TRANSACAO_ID,
                    ID_TERMINAL,
                    ID_CLIENTE,
                    DATA_TRANSACAO,
                    NOME_TABELA,
                    ID_CLIENTE);
        }

        private static void addUltimaDataTransacaoPOS(Context context, UltimaDataTransacaoPOS item) {
            ContentValues values = new ContentValues();
            values.put(ID, item.getId());
            values.put(ULTIMA_TRANSACAO_ID, item.getUltimaTransacaoPOSId());
            values.put(ID_TERMINAL, item.getIdTerminal());
            values.put(ID_CLIENTE, item.getIdCliente());
            values.put(DATA_TRANSACAO, Util_IO.dateTimeToString(item.getDataTransacao(), Config.FormatDateTimeStringBanco));
            SimpleDbHelper.INSTANCE.open(context).insert(NOME_TABELA, null, values);
        }

        private static List<UltimaDataTransacaoPOS> obterUltimaDataTransacaoPOSPorId(Context context, int clienteId) {
            List<UltimaDataTransacaoPOS> lista = new ArrayList<>();
            String consulta = obterQuerySelect();
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(consulta, new String[]{String.valueOf(clienteId)});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    UltimaDataTransacaoPOS obj = new UltimaDataTransacaoPOS();
                    obj.setId(cursor.getInt(0));
                    obj.setUltimaTransacaoPOSId(cursor.getInt(1));
                    obj.setIdTerminal(cursor.getString(2));
                    obj.setIdCliente(cursor.getInt(3));
                    obj.setDataTransacao(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
                    lista.add(obj);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return lista;
        }
    }

    private static class TabelaInformacoesGeraisPOS {

        private static final String NOME_TABELA = "InformacoesGeraisPOS";

        private static final String ID = "Id";
        private static final String ID_CLIENTE = "IdCliente";
        private static final String CODIGO_MASTERS = "CodigoMasters";
        private static final String CODIGO_MUXX = "CodigoMuxx";
        private static final String NUMERO_SERIE = "NumeroSerie";
        private static final String ENTIDADE_PONTO_CAPTURA_ID = "EntidadePontoCapturaId";
        private static final String ADQUIRENCIA = "Adquirencia";
        private static final String RECARGA = "Recarga";
        private static final String DATA_INSTALACAO_ADQUIRENCIA = "DataInstalacaoAdquirencia";
        private static final String VALOR_ALUGUEL = "ValorAluguel";
        private static final String DATA_ULTIMA_TRANSACAO_ADQUIRENCIA = "DataUltimaTransacaoAdquirencia";
        private static final String DATA_ULTIMA_VENDA_RECARGA = "DataUltimaVendaRecarga";
        private static final String VALOR_TRANSACIONADO_ADQUIRENCIA = "ValorTransacionadoAdquirencia";
        private static final String TERMINAL_ALOCADO_SGV = "TerminalAlocadoSGV";
        private static final String PONTUACAO = "Pontuacao";
        private static final String ICCID = "ICCID";
        private static final String SITUACAO_PONTO_CAPTURA_ID = "SituacaoPontoCapturaId";
        private static final String SITUACAO_PONTO_CAPTURA = "SituacaoPontoCaptura";
        private static final String MODELO = "Modelo";
        private static final String DESCRICAO = "Transacionando";
        private static final String DATA_CADASTRO = "DataCadastro";
        private static final String VENDEDOR_INSTALACAO = "VendedorInstalacao";
        private static final String ATIVO = "ativo";
        private static final String VALORTRANSMESATUAL = "valortransmesatual";
        private static final String VALORTRANSMESANTERIOR = "valortransmesanterior";
        private static final String TRANSACIONADORECARGA = "transacionadorecarga";
        private static final String VALORRECARGA_TRANSACIONADO = "valorrecarga_transacionado";
        private static final String VALORRECARGA_TRANSMESATUAL = "valorrecarga_transmesatual";
        private static final String VALORRECARGA_TRANSMESANTERIOR = "valorrecarga_transmesanterior";


        private static final String VALOR_PIX_TRANSACIONADO = "valorpix_transacionado";
        private static final String VALOR_PIX_TRANSMESATUAL =  "valor_pix_transmesatual";
        private static final String VALOR_PIX_TRANSMESANTERIOR = "valor_pix_transmesanterior";

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] VARCHAR(60),\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] DATETIME,\n" +
                            "[%s] DECIMAL(6,2),\n" +
                            "[%s] DATETIME,\n" +
                            "[%s] DATETIME,\n" +
                            "[%s] DECIMAL(6, 2),\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(255),\n" +
                            "[%s] VARCHAR(255));",
                    NOME_TABELA,
                    ID,
                    ID_CLIENTE,
                    CODIGO_MASTERS,
                    CODIGO_MUXX,
                    NUMERO_SERIE,
                    ENTIDADE_PONTO_CAPTURA_ID,
                    ADQUIRENCIA,
                    RECARGA,
                    DATA_INSTALACAO_ADQUIRENCIA,
                    VALOR_ALUGUEL,
                    DATA_ULTIMA_TRANSACAO_ADQUIRENCIA,
                    DATA_ULTIMA_VENDA_RECARGA,
                    VALOR_TRANSACIONADO_ADQUIRENCIA,
                    TERMINAL_ALOCADO_SGV,
                    PONTUACAO,
                    ICCID,
                    SITUACAO_PONTO_CAPTURA_ID,
                    SITUACAO_PONTO_CAPTURA,
                    MODELO,
                    DESCRICAO);
        }

        private static String atualizarTabelaVersao73() {
            return "ALTER TABLE [" + NOME_TABELA + "] ADD COLUMN [" + ATIVO + "] INTEGER DEFAULT 1; ";
        }

        private static String obterQuerySelect() {
            return String.format("SELECT %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = 1 AND %s = ?",
                    ID,
                    ID_CLIENTE,
                    CODIGO_MASTERS,
                    CODIGO_MUXX,
                    NUMERO_SERIE,
                    ENTIDADE_PONTO_CAPTURA_ID,
                    ADQUIRENCIA,
                    RECARGA,
                    DATA_INSTALACAO_ADQUIRENCIA,
                    VALOR_ALUGUEL,
                    DATA_ULTIMA_TRANSACAO_ADQUIRENCIA,
                    DATA_ULTIMA_VENDA_RECARGA,
                    VALOR_TRANSACIONADO_ADQUIRENCIA,
                    TERMINAL_ALOCADO_SGV,
                    PONTUACAO,
                    ICCID,
                    SITUACAO_PONTO_CAPTURA_ID,
                    SITUACAO_PONTO_CAPTURA,
                    MODELO,
                    DESCRICAO,
                    DATA_CADASTRO,
                    VENDEDOR_INSTALACAO,
                    VALORTRANSMESATUAL,            // 22
                    VALORTRANSMESANTERIOR,         // 23
                    TRANSACIONADORECARGA,          // 24
                    VALORRECARGA_TRANSACIONADO,    // 25
                    VALORRECARGA_TRANSMESATUAL,    // 26
                    VALORRECARGA_TRANSMESANTERIOR, // 27
                    VALOR_PIX_TRANSACIONADO,       // 28
                    VALOR_PIX_TRANSMESATUAL,       // 29
                    VALOR_PIX_TRANSMESANTERIOR,    // 30
                    ATIVO,                         // 31
                    NOME_TABELA,                   // 32
                    ATIVO,                         // 33
                    ID_CLIENTE);                   // 34
        }

        private static void addInformacaoGeralPOS(Context context, InformacaoGeralPOS item) {
            ContentValues values = new ContentValues();
            values.put(ID, item.getId());
            values.put(ID_CLIENTE, item.getIdCliente());
            values.put(CODIGO_MASTERS, item.getCodigoMasters());
            values.put(CODIGO_MUXX, item.getCodigoMuxx());
            values.put(NUMERO_SERIE, item.getNumeroSerie());
            values.put(ENTIDADE_PONTO_CAPTURA_ID, item.getEntidadePontoCapturaId());
            values.put(ADQUIRENCIA, Util_IO.booleanToNumber(item.getAdquirencia()));
            values.put(RECARGA, item.getRecarga());
            values.put(DATA_INSTALACAO_ADQUIRENCIA, Util_IO.dateTimeToString(item.getDataInstalacaoAdquirencia(), Config.FormatDateTimeStringBanco));
            values.put(VALOR_ALUGUEL, item.getValorAluguel());
            values.put(DATA_ULTIMA_TRANSACAO_ADQUIRENCIA, Util_IO.dateTimeToString(item.getDataUltimaTransacaoAdquirencia(), Config.FormatDateTimeStringBanco));
            values.put(DATA_ULTIMA_VENDA_RECARGA, Util_IO.dateTimeToString(item.getDataUltimaVendaRecarga(), Config.FormatDateTimeStringBanco));
            values.put(VALOR_TRANSACIONADO_ADQUIRENCIA, item.getValorTransacionadoAdquirencia());
            values.put(TERMINAL_ALOCADO_SGV, Util_IO.booleanToNumber(item.getTerminalAlocadoSGV()));
            values.put(PONTUACAO, item.getPontuacao());
            values.put(ICCID, item.getIccid());
            values.put(SITUACAO_PONTO_CAPTURA_ID, item.getSituacaoPontoCapturaId());
            values.put(SITUACAO_PONTO_CAPTURA, item.getSituacaoPontoCaptura());
            values.put(MODELO, item.getModelo());
            values.put(DESCRICAO, item.getDescricao());
            values.put(DATA_CADASTRO, Util_IO.dateTimeToString(item.getDataCadastro(), Config.FormatDateTimeStringBanco));
            values.put(VENDEDOR_INSTALACAO, item.getVendedorInstalacao());
            values.put(ATIVO, Util_IO.booleanToNumber(item.isAtivo()));
            values.put(VALORTRANSMESATUAL, item.getValortransmesatual());
            values.put(VALORTRANSMESANTERIOR, item.getValortransmesanterior());
            values.put(TRANSACIONADORECARGA, item.getTransacionadorecarga());
            values.put(VALORRECARGA_TRANSACIONADO, item.getValorrecarga_transacionado());
            values.put(VALORRECARGA_TRANSMESATUAL, item.getValorrecarga_transmesatual());
            values.put(VALORRECARGA_TRANSMESANTERIOR, item.getValorrecarga_transmesanterior());
            values.put(VALOR_PIX_TRANSACIONADO, item.getValorPix());
            values.put(VALOR_PIX_TRANSMESATUAL, item.getValorPixTransMesAtual());
            values.put(VALOR_PIX_TRANSMESANTERIOR, item.getValorPixTransMesAnterior());
            SimpleDbHelper.INSTANCE.open(context).replace(NOME_TABELA, null, values);
        }

        private static List<InformacaoGeralPOS> obterInformacoesGeraisPOSPorId(Context context, String clienteId) {
            List<InformacaoGeralPOS> lista = new ArrayList<>();
            String consulta = obterQuerySelect();
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(consulta, new String[]{clienteId});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    InformacaoGeralPOS obj = new InformacaoGeralPOS();
                    obj.setId(cursor.getInt(0));
                    obj.setIdCliente(cursor.getInt(1));
                    obj.setCodigoMasters(cursor.getString(2));
                    obj.setCodigoMuxx(cursor.getString(3));
                    obj.setNumeroSerie(cursor.getString(4));
                    obj.setEntidadePontoCapturaId(cursor.getInt(5));
                    obj.setAdquirencia(Util_IO.numberToBoolean(cursor.getInt(6)));
                    obj.setRecarga(cursor.getInt(7));
                    obj.setDataInstalacaoAdquirencia(Util_IO.stringToDate(cursor.getString(8), Config.FormatDateTimeStringBanco));
                    obj.setValorAluguel(cursor.getDouble(9));
                    obj.setDataUltimaTransacaoAdquirencia(Util_IO.stringToDate(cursor.getString(10), Config.FormatDateTimeStringBanco));
                    obj.setDataUltimaVendaRecarga(Util_IO.stringToDate(cursor.getString(11), Config.FormatDateTimeStringBanco));
                    obj.setValorTransacionadoAdquirencia(cursor.getDouble(12));
                    obj.setTerminalAlocadoSGV(Util_IO.numberToBoolean(cursor.getInt(13)));
                    obj.setPontuacao(cursor.getDouble(14));
                    obj.setIccid(cursor.getString(15));
                    obj.setSituacaoPontoCapturaId(cursor.getInt(16));
                    obj.setSituacaoPontoCaptura(cursor.getInt(17));
                    obj.setModelo(cursor.getString(18));
                    obj.setDescricao(cursor.getString(19));
                    obj.setDataCadastro(Util_IO.stringToDate(cursor.getString(20), Config.FormatDateTimeStringBanco));
                    obj.setVendedorInstalacao(cursor.getString(21));
                    obj.setAtivo(Util_IO.numberToBoolean(cursor.getInt(28)));
                    obj.setValortransmesatual(cursor.getDouble(22));
                    obj.setValortransmesanterior(cursor.getDouble(23));
                    obj.setTransacionadorecarga(cursor.getString(24));
                    obj.setValorrecarga_transacionado(cursor.getDouble(25));
                    obj.setValorrecarga_transmesatual(cursor.getDouble(26));
                    obj.setValorrecarga_transmesanterior(cursor.getDouble(27));
                    obj.setValorPix(cursor.getDouble(28));
                    obj.setValorPixTransMesAtual(cursor.getDouble(29));
                    obj.setValorPixTransMesAnterior(cursor.getDouble(30));
                    lista.add(obj);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return lista;
        }

        private static void remover(Context context, int id) {
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String filtro = "id = ?";
            String[] queryParam = new String[]{String.valueOf(id)};
            db.delete(NOME_TABELA, filtro, queryParam);
        }

        private static void removerInativa(Context context) {
            int inativo = 0;
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            String filtro = ATIVO + " = ?";
            String[] queryParam = new String[]{String.valueOf(inativo)};
            db.delete(NOME_TABELA, filtro, queryParam);
        }

    }
    //endregion
}
