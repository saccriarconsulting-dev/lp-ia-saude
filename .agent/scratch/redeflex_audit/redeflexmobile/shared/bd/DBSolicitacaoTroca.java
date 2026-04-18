package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Predicate;
import com.axys.redeflexmobile.shared.enums.EnumStatusTrocaProduto;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaMotivo;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class DBSolicitacaoTroca {

    static final String CREATE_SOLICITACAO = TabelaSolicitacao.criarTabela();
    static final String CREATE_SOLICITACAO_DETALHES = TabelaSolicitacaoDetalhes.criarTabela();
    static final String CREATE_SOLICITACAO_CODIGO_BARRA = TabelaSolicitacaoCodigoBarra.criarTabela();
    static final String CREATE_SOLICITACAO_MOTIVO = TabelaSolicitacaoMotivo.criarTabela();

    private Context context;

    public DBSolicitacaoTroca(Context context) {
        this.context = context;
    }

    //region SOLICITACAO_TROCA
    public Completable iniciarSolicitacao(SolicitacaoTroca solicitacaoTroca) {
        return Completable.create(emitter -> {
            long lastId = TabelaSolicitacao.iniciarSolicitacao(context, solicitacaoTroca);
            if (lastId > -1) {
                emitter.onComplete();
            } else {
                emitter.onError(new SQLException("Não foi possível inserir o registro"));
            }
        });
    }

    public void salvarSolicitacaoSincronizacao(SolicitacaoTroca solicitacaoTroca) {
        try {
            TabelaSolicitacao.salvarSolicitacaoSincronizacao(context, solicitacaoTroca);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Single<List<SolicitacaoTroca>> obterSolicitacoes() {
        return Single.create(emitter -> {
            try {
                emitter.onSuccess(TabelaSolicitacao.obterSolicitacoes(context));
            } catch (Exception e) {
                emitter.onError(new SQLException("Não foi possível obter as solicitações"));
            }
        });
    }

    public Single<SolicitacaoTroca> obterSolicitacaoPorId(int solicitacaoId) {
        return Single.create(emitter -> {
            try {
                emitter.onSuccess(TabelaSolicitacao.obterSolicitacaoPorId(context, solicitacaoId));
            } catch (Exception e) {
                emitter.onError(new SQLException("Não foi possível obter a solicitação " + solicitacaoId));
            }
        });
    }

    public Completable cancelarSolicitacaoPorProduto(int solicitacaoId, String produtoId) {
        return Completable.create(emitter -> {
            try {
                TabelaSolicitacaoDetalhes.cancelarDetalhesProduto(context, solicitacaoId, produtoId);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(new SQLException("Não foi possível cancelar a solicitação do produto " + produtoId));
            }
        });
    }

    public Completable trocarProdutosSemBipagem(int solicitacaoId, String produtoId, int qtdeTrocada) {
        return Completable.create(emitter -> {
            try {
                SolicitacaoTrocaDetalhes produto = TabelaSolicitacaoDetalhes.obterDetalhePorId(context, solicitacaoId, produtoId);
                produto.setQtdeTrocado(qtdeTrocada);
                TabelaSolicitacaoDetalhes.realizarTroca(context, solicitacaoId, produto);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(new SQLException("Não foi possível confirmar a solicitação do produto " + produtoId));
            }
        });
    }

    public Completable trocarProdutosComBipagem(int solicitacaoId, SolicitacaoTrocaDetalhes produto) {
        return Completable.create(emitter -> {
            try {
                TabelaSolicitacaoDetalhes.realizarTroca(context, solicitacaoId, produto);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(new SQLException("Não foi possível confirmar a solicitação do produto " + produto.getProdutoCodigo()));
            }
        });
    }

    public List<SolicitacaoTroca> obterSolicitacoesSincronizacao(boolean atualizacao) {
        try {
            return TabelaSolicitacao.obterSolicitacoesSincronizacao(context, atualizacao);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void setarSyncSolicitacao(SolicitacaoTroca solicitacao) {
        try {
            Stream.ofNullable(solicitacao.getProdutos())
                    .forEach(produto -> TabelaSolicitacaoDetalhes
                            .setarSync(context, solicitacao.getIdApp(), produto.getProdutoCodigo()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        TabelaSolicitacao.deletar(context);
        TabelaSolicitacaoDetalhes.deletar(context);
        TabelaSolicitacaoCodigoBarra.deletar(context);
        TabelaSolicitacaoMotivo.deletar(context);
    }

    public void removerAntigasPorPeriodo() {
        TabelaSolicitacao.removerAposPeriodo(context);
    }
    //endregion

    //region SOLICITACAO_TROCA_MOTIVO
    public void salvarSolicitacaoTrocaMotivo(SolicitacaoTrocaMotivo solicitacaoTrocaMotivo) {
        try {
            TabelaSolicitacaoMotivo.addSolicitacaoTrocaMotivo(context, solicitacaoTrocaMotivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SolicitacaoTrocaMotivo> obterMotivos() {
        try {
            return TabelaSolicitacaoMotivo.obterMotivos(context);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    //endregion

    private static class TabelaSolicitacao {

        private static final String NOME_TABELA = "SolicitacaoTroca";

        private static final String ID_SERVER = "IdServer";
        private static final String ID_APP = "IdAppMobile";
        private static final String DATA_SOLICITACAO = "DataSolicitacao";
        private static final String VENDEDOR_ID = "VendedorId";
        private static final String CLIENTE_ID = "IdClienteIntraFlex";
        private static final String CLIENTE_NOME = "nomecliente";

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER NOT NULL,\n" +
                            "[%s] DATETIME,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(30),\n" +
                            "PRIMARY KEY (%s, %s));",
                    NOME_TABELA,
                    ID_SERVER,
                    ID_APP,
                    DATA_SOLICITACAO,
                    VENDEDOR_ID,
                    CLIENTE_ID,
                    CLIENTE_NOME,
                    ID_APP,
                    DATA_SOLICITACAO
            );
        }

        private static String obterQuerySolicitacao() {
            return String.format("SELECT %s, %s, %s, %s, %s, %s FROM %s",
                    ID_SERVER,
                    ID_APP,
                    DATA_SOLICITACAO,
                    VENDEDOR_ID,
                    CLIENTE_ID,
                    CLIENTE_NOME,
                    NOME_TABELA);
        }

        private static long criarId(Context context) {
            String sql = String.format(

                    "SELECT MAX(%s) FROM %S",
                    TabelaSolicitacao.ID_APP,
                    TabelaSolicitacao.NOME_TABELA
            );

            long id = 0;
            try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(sql, null)) {
                if (cursor.moveToNext()) {
                    id = cursor.getLong(0);
                }
            }

            id++;

            return id;
        }

        private static long iniciarSolicitacao(Context context, SolicitacaoTroca solicitacaoTroca) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_APP, TabelaSolicitacao.criarId(context));
            contentValues.put(DATA_SOLICITACAO, Util_IO.dateTimeToString(solicitacaoTroca.getDataSolicitacao(), Config.FormatDateTimeStringBanco));
            contentValues.put(VENDEDOR_ID, solicitacaoTroca.getVendedorId());
            contentValues.put(CLIENTE_ID, solicitacaoTroca.getClienteId());
            contentValues.put(CLIENTE_NOME, solicitacaoTroca.getClienteNome());
            long solicitacaoId = SimpleDbHelper.INSTANCE.open(context).insert(NOME_TABELA, null, contentValues);
            TabelaSolicitacaoDetalhes.salvarSolicitacaoDetalhes(context, solicitacaoId, solicitacaoTroca.getProdutos(), false);
            return solicitacaoId;
        }

        private static void salvarSolicitacaoSincronizacao(Context context, SolicitacaoTroca solicitacaoTroca) {
            SolicitacaoTroca solicitacaoLocal = obterSolicitacaoPorId(context, solicitacaoTroca.getIdApp());
            if (solicitacaoLocal == null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID_SERVER, solicitacaoTroca.getIdServer());
                contentValues.put(ID_APP, solicitacaoTroca.getIdApp());
                contentValues.put(DATA_SOLICITACAO, Util_IO.dateTimeToString(solicitacaoTroca.getDataSolicitacao(), Config.FormatDateTimeStringBanco));
                contentValues.put(VENDEDOR_ID, solicitacaoTroca.getVendedorId());
                contentValues.put(CLIENTE_ID, solicitacaoTroca.getClienteId());

                DBCliente dbCliente = new DBCliente(context);
                Cliente cliente = dbCliente.getById(solicitacaoTroca.getClienteId());
                if (cliente != null) {
                    contentValues.put(CLIENTE_NOME, cliente.getNomeFantasia());
                }
                SimpleDbHelper.INSTANCE.open(context).insert(NOME_TABELA, null, contentValues);
            }

            if (solicitacaoLocal != null) {
                atualizarSolicitacao(context, solicitacaoTroca);
            }
            TabelaSolicitacaoDetalhes.salvarSolicitacaoDetalhes(context, solicitacaoTroca.getIdApp(), solicitacaoTroca.getProdutos(), true);
        }

        private static void atualizarSolicitacao(Context context, SolicitacaoTroca solicitacaoTroca) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_SERVER, solicitacaoTroca.getIdServer());
            SimpleDbHelper.INSTANCE.open(context)
                    .update(
                            NOME_TABELA,
                            contentValues,
                            String.format("%s = ?", ID_APP),
                            new String[]{String.valueOf(solicitacaoTroca.getIdApp())}
                    );
        }

        private static List<SolicitacaoTroca> obterSolicitacoes(Context context) {
            List<SolicitacaoTroca> solicitacoes = new ArrayList<>();
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(obterQuerySolicitacao(), new String[]{});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    solicitacoes.add(parseSolicitacoesCursor(context, cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }
            return solicitacoes;
        }

        private static SolicitacaoTroca obterSolicitacaoPorId(Context context, int solicitacaoId) {
            SolicitacaoTroca solicitacao = null;
            String consulta = obterQuerySolicitacao() + " WHERE " + ID_APP + " = ?";
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(consulta, new String[]{String.valueOf(solicitacaoId)});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                solicitacao = parseSolicitacoesCursor(context, cursor);
                cursor.close();
            }
            return solicitacao;
        }

        private static SolicitacaoTroca parseSolicitacoesCursor(Context context, Cursor cursor) {
            SolicitacaoTroca item = new SolicitacaoTroca();
            item.setIdServer(cursor.getInt(0));
            item.setIdApp(cursor.getInt(1));
            item.setDataSolicitacao(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
            item.setVendedorId(cursor.getInt(3));
            item.setClienteId(cursor.getString(4));
            item.setClienteNome(cursor.getString(5));
            item.setProdutos(TabelaSolicitacaoDetalhes.obterDetalhesPorIdSolicitacao(context, item.getIdApp()));
            return item;
        }

        private static List<SolicitacaoTroca> obterSolicitacoesSincronizacao(Context context, boolean atualizacao) {
            List<SolicitacaoTroca> solicitacoes = new ArrayList<>();
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(obterQuerySolicitacao(), new String[]{});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    SolicitacaoTroca convertido = parseSolicitacoesCursorSincronizacao(context, cursor, atualizacao);
                    if (convertido != null) {
                        solicitacoes.add(convertido);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }

            return solicitacoes;
        }

        private static SolicitacaoTroca parseSolicitacoesCursorSincronizacao(Context context, Cursor cursor, boolean atualizacao) {
            int solicitacaoTrocaId = cursor.getInt(1);
            List<SolicitacaoTrocaDetalhes> produtos = TabelaSolicitacaoDetalhes
                    .obterDetalhesSincronizacao(context, solicitacaoTrocaId, atualizacao);
            if (produtos.isEmpty()) return null;

            SolicitacaoTroca item = new SolicitacaoTroca();
            item.setIdServer(cursor.getInt(0));
            item.setIdApp(solicitacaoTrocaId);
            item.setDataSolicitacao(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
            item.setVendedorId(cursor.getInt(3));
            item.setClienteId(cursor.getString(4));
            item.setClienteNome(cursor.getString(5));
            item.setProdutos(produtos);
            return item;
        }

        private static void deletar(Context context) {
            SimpleDbHelper.INSTANCE.open(context)
                    .delete(NOME_TABELA, null, null);
        }

        private static void deletarPorId(Context context, int id) {
            SimpleDbHelper.INSTANCE
                    .open(context)
                    .delete(
                            NOME_TABELA,
                            String.format("%s = ?", ID_APP),
                            new String[]{String.valueOf(id)}
                    );
        }

        private static void removerAposPeriodo(Context context) {
            String sql = String.format(
                    "SELECT  %s, %s, %s, %s, %s, %s FROM %s WHERE %s <= DATE('now', '-60 day')",
                    ID_SERVER,
                    ID_APP,
                    DATA_SOLICITACAO,
                    VENDEDOR_ID,
                    CLIENTE_ID,
                    CLIENTE_NOME,
                    NOME_TABELA,
                    DATA_SOLICITACAO
            );

            List<SolicitacaoTroca> trocas = new ArrayList<>();
            try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(sql, null)) {
                while (cursor.moveToNext()) {
                    trocas.add(parseSolicitacoesCursor(context, cursor));
                }
            }

            Stream.of(trocas)
                    .forEach(removerSolicitacaoTroca(context));
        }

        @NotNull
        private static Consumer<SolicitacaoTroca> removerSolicitacaoTroca(Context context) {
            return troca -> {
                List<SolicitacaoTrocaDetalhes> detalhes = Stream.of(troca.getProdutos())
                        .filter(filtrarPorInativos())
                        .toList();

                Stream.ofNullable(detalhes)
                        .forEach(removerDetalhes(context));

                if (detalhes.size() == troca.getProdutos().size()) {
                    deletarPorId(context, troca.getIdApp());
                }
            };
        }

        @NotNull
        private static Consumer<SolicitacaoTrocaDetalhes> removerDetalhes(Context context) {
            return value -> {
                TabelaSolicitacaoDetalhes.deletarPorId(context, value.getIdApp());
                Stream.of(value.getIccids())
                        .forEach(removerIccids(context));
            };
        }

        @NotNull
        private static Consumer<SolicitacaoTrocaCodBarras> removerIccids(Context context) {
            return codigo -> TabelaSolicitacaoCodigoBarra.deletarPorId(context, codigo.getIdApp());
        }

        @NotNull
        private static Predicate<SolicitacaoTrocaDetalhes> filtrarPorInativos() {
            return detalhe -> detalhe.getStatusId() == EnumStatusTrocaProduto.CANCELADO.valor
                    || detalhe.getStatusId() == EnumStatusTrocaProduto.CONCLUIDO.valor;
        }
    }

    private static class TabelaSolicitacaoDetalhes {

        private static final String NOME_TABELA = "SolicitacaoTrocaDetalhes";

        private static final String ID_APP = "IdAppMobile";
        private static final String SOLICITACAO_TROCA_ID = "SolicitacaoTrocaId";
        private static final String PRODUTO_ID = "CodigoItem";
        private static final String PRODUTO_NOME = "ProdutoNome";
        private static final String PRODUTO_QUANTIDADE = "Qtde";
        private static final String PRODUTO_QUANTIDADE_TROCADO = "QtdeTrocada";
        private static final String PRODUTO_VALOR_UNITARIO = "ValorUN";
        private static final String PRODUTO_VALOR_TOTAL = "ValorTotalItem";
        private static final String STATUS_ID = "StatusId";
        private static final String MOTIVO_ID = "MotivoId";
        private static final String ALTERACAO_DATA = "DataAlteracao";
        private static final String SYNC_DATA = "DataSync";
        private static final String SYNC = "Sync";
        private static final String ID_SERVER = "IdServer";

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER NOT NULL,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(10),\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] DECIMAL(6, 2),\n" +
                            "[%s] DECIMAL(6, 2),\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] DATETIME,\n" +
                            "[%s] DATETIME,\n" +
                            "[%s] INTEGER DEFAULT 0," +
                            "PRIMARY KEY (%s, %s));",
                    NOME_TABELA,
                    ID_APP,
                    SOLICITACAO_TROCA_ID,
                    ID_SERVER,
                    PRODUTO_ID,
                    PRODUTO_NOME,
                    PRODUTO_QUANTIDADE,
                    PRODUTO_QUANTIDADE_TROCADO,
                    PRODUTO_VALOR_UNITARIO,
                    PRODUTO_VALOR_TOTAL,
                    STATUS_ID,
                    MOTIVO_ID,
                    ALTERACAO_DATA,
                    SYNC_DATA,
                    SYNC,
                    ID_APP,
                    SOLICITACAO_TROCA_ID
            );
        }

        private static String obterSolicitacaoTrocaDetalhesQuery() {
            return String.format("SELECT %s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s, " +
                            "%s " +
                            "FROM %s " +
                            "WHERE %s = ?",
                    ID_APP,
                    SOLICITACAO_TROCA_ID,
                    PRODUTO_ID,
                    PRODUTO_NOME,
                    PRODUTO_QUANTIDADE,
                    PRODUTO_QUANTIDADE_TROCADO,
                    PRODUTO_VALOR_UNITARIO,
                    PRODUTO_VALOR_TOTAL,
                    STATUS_ID,
                    MOTIVO_ID,
                    ALTERACAO_DATA,
                    SYNC_DATA,
                    SYNC,
                    ID_SERVER,
                    NOME_TABELA,
                    SOLICITACAO_TROCA_ID);
        }

        private static long criarId(Context context) {
            String sql = String.format(
                    "SELECT MAX(%s) FROM %S",
                    TabelaSolicitacaoDetalhes.ID_APP,
                    TabelaSolicitacaoDetalhes.NOME_TABELA
            );

            long id = 0;
            try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(sql, null)) {
                if (cursor.moveToNext()) {
                    id = cursor.getLong(0);
                }
            }

            id++;

            return id;
        }

        private static SolicitacaoTrocaDetalhes obterDetalhePorId(Context context, int solicitacaoId, String produtoId) {
            SolicitacaoTrocaDetalhes produto = null;
            String consulta = obterSolicitacaoTrocaDetalhesQuery() + " AND " + PRODUTO_ID + " = ?";
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(consulta,
                    new String[]{String.valueOf(solicitacaoId), produtoId});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    produto = parseDetalhesCursor(context, cursor);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return produto;
        }

        private static List<SolicitacaoTrocaDetalhes> obterDetalhesPorIdSolicitacao(Context context, int solicitacaoId) {
            List<SolicitacaoTrocaDetalhes> produtos = new ArrayList<>();
            String consulta = obterSolicitacaoTrocaDetalhesQuery();
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(consulta,
                    new String[]{String.valueOf(solicitacaoId)});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    produtos.add(parseDetalhesCursor(context, cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }
            return produtos;
        }

        private static List<SolicitacaoTrocaDetalhes> obterDetalhesSincronizacao(Context context, int solicitacaoId, boolean atualizacao) {
            List<SolicitacaoTrocaDetalhes> produtos = new ArrayList<>();

            String statusValidacao = atualizacao
                    ? " > " + EnumStatusTrocaProduto.APROVADO.valor
                    : " = " + EnumStatusTrocaProduto.ANALISE.valor;

            String consulta = obterSolicitacaoTrocaDetalhesQuery()
                    + " AND " + STATUS_ID + statusValidacao
                    + " AND " + SYNC + " = 0";

            Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(consulta,
                    new String[]{String.valueOf(solicitacaoId)});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    produtos.add(parseDetalhesCursor(context, cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }
            return produtos;
        }

        private static SolicitacaoTrocaDetalhes parseDetalhesCursor(Context context, Cursor cursor) {
            SolicitacaoTrocaDetalhes item = new SolicitacaoTrocaDetalhes();
            item.setIdApp(cursor.getInt(0));
            item.setSolicitacaoTrocaId(cursor.getInt(1));
            item.setProdutoCodigo(cursor.getString(2));
            item.setProdutoNome(cursor.getString(3));
            item.setQtde(cursor.getInt(4));
            item.setQtdeTrocado(cursor.getInt(5));
            item.setValorUnitario(cursor.getDouble(6));
            item.setValorTotal(cursor.getDouble(7));
            item.setStatusId(cursor.getInt(8));
            item.setMotivoId(cursor.getInt(9));
            item.setAlteracaoData(Util_IO.stringToDate(cursor.getString(10), Config.FormatDateTimeStringBanco));
            item.setSyncDate(Util_IO.stringToDate(cursor.getString(11), Config.FormatDateTimeStringBanco));
            item.setSync(cursor.getInt(12));
            item.setIccids(TabelaSolicitacaoCodigoBarra.obterIccids(context, item.getSolicitacaoTrocaId(), item.getProdutoCodigo()));
            item.setIdServer(cursor.getInt(13));
            return item;
        }

        private static void cancelarDetalhesProduto(Context context, int solicitacaoId, String produtoId) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(STATUS_ID, EnumStatusTrocaProduto.CANCELADO.valor);
            contentValues.put(ALTERACAO_DATA, Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
            contentValues.put(SYNC_DATA, "");
            contentValues.put(SYNC, 0);

            String where = String.format("[%s] = ? AND [%s] = ?", SOLICITACAO_TROCA_ID, PRODUTO_ID);
            SimpleDbHelper.INSTANCE.open(context).update(NOME_TABELA, contentValues, where,
                    new String[]{String.valueOf(solicitacaoId), produtoId});
        }

        private static void salvarSolicitacaoDetalhes(Context context, long solicitacaoId, List<SolicitacaoTrocaDetalhes> produtos, boolean sync) {
            Stream.ofNullable(produtos).forEach(produto -> {
                if (sync) {
                    salvarSolicitacaoDetalhesSincronizacao(context, solicitacaoId, produto);
                } else {
                    salvarSolicitacaoDetalhes(context, solicitacaoId, produto);
                }
            });
        }

        private static void salvarSolicitacaoDetalhes(Context context, long solicitacaoId, SolicitacaoTrocaDetalhes produto) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_APP, TabelaSolicitacaoDetalhes.criarId(context));
            contentValues.put(SOLICITACAO_TROCA_ID, solicitacaoId);
            contentValues.put(PRODUTO_ID, produto.getProdutoCodigo());
            contentValues.put(PRODUTO_NOME, produto.getProdutoNome());
            contentValues.put(PRODUTO_QUANTIDADE, produto.getQtde());
            contentValues.put(PRODUTO_VALOR_UNITARIO, produto.getValorUnitario());
            contentValues.put(PRODUTO_VALOR_TOTAL, produto.getValorTotal());
            contentValues.put(STATUS_ID, EnumStatusTrocaProduto.ANALISE.valor);
            contentValues.put(MOTIVO_ID, produto.getMotivoId());
            contentValues.put(ALTERACAO_DATA, Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
            SimpleDbHelper.INSTANCE.open(context).insert(NOME_TABELA, null, contentValues);
            TabelaSolicitacaoCodigoBarra.salvarSolicitacaoCodBarra(context, solicitacaoId, produto.getIccids());
        }

        private static void salvarSolicitacaoDetalhesSincronizacao(Context context, long solicitacaoId, SolicitacaoTrocaDetalhes produto) {
            SolicitacaoTrocaDetalhes solicitacaoDetalhesLocal = obterDetalhePorId(context,
                    produto.getSolicitacaoTrocaId(), produto.getProdutoCodigo());

            if (solicitacaoDetalhesLocal == null || solicitacaoDetalhesLocal.getStatusId() < EnumStatusTrocaProduto.CANCELADO.valor) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(ID_APP, produto.getIdApp());
                contentValues.put(ID_SERVER, produto.getIdServer());
                contentValues.put(SOLICITACAO_TROCA_ID, solicitacaoId);
                contentValues.put(PRODUTO_ID, produto.getProdutoCodigo());
                contentValues.put(PRODUTO_QUANTIDADE, produto.getQtde());
                contentValues.put(PRODUTO_QUANTIDADE_TROCADO, produto.getQtdeTrocado());
                contentValues.put(PRODUTO_VALOR_UNITARIO, produto.getValorUnitario());
                contentValues.put(PRODUTO_VALOR_TOTAL, produto.getValorTotal());
                contentValues.put(STATUS_ID, produto.getStatusId());
                contentValues.put(MOTIVO_ID, produto.getMotivoId());
                contentValues.put(ALTERACAO_DATA, Util_IO.dateTimeToString(produto.getAlteracaoData(), Config.FormatDateTimeStringBanco));

                DBEstoque dbEstoque = new DBEstoque(context);
                Produto produtoLocal = dbEstoque.getProdutoById(produto.getProdutoCodigo());
                if (produtoLocal != null) {
                    contentValues.put(PRODUTO_NOME, produtoLocal.getNome());
                }
                SimpleDbHelper.INSTANCE.open(context).replace(NOME_TABELA, null, contentValues);

                Stream.ofNullable(produto.getIccids()).forEach(codBarras -> {
                    codBarras.setSolicitacaoTrocaId((int) solicitacaoId);
                    codBarras.setCodigoItem(produto.getProdutoCodigo());
                    TabelaSolicitacaoCodigoBarra.salvarSolicitacaoCodBarraSincronizacao(context, codBarras);
                });
            }
        }

        private static void realizarTroca(Context context, int solicitacaoId, SolicitacaoTrocaDetalhes produto) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PRODUTO_QUANTIDADE_TROCADO, produto.getQtdeTrocado());
            contentValues.put(STATUS_ID, EnumStatusTrocaProduto.CONCLUIDO.valor);
            contentValues.put(ALTERACAO_DATA, Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
            contentValues.put(SYNC_DATA, "");
            contentValues.put(SYNC, 0);

            String where = String.format("[%s] = ? AND [%s] = ?", SOLICITACAO_TROCA_ID, PRODUTO_ID);
            SimpleDbHelper.INSTANCE.open(context).update(NOME_TABELA, contentValues, where,
                    new String[]{String.valueOf(solicitacaoId), produto.getProdutoCodigo()});

            atualizarEstoqueProduto(context, produto);
        }

        private static void atualizarEstoqueProduto(Context context, SolicitacaoTrocaDetalhes produto) {
            DBEstoque dbEstoque = new DBEstoque(context);
            DBIccid dbIccid = new DBIccid(context);

            dbEstoque.atualizaEstoque(produto.getProdutoCodigo(), true, produto.getQtdeTrocado());
            Stream.ofNullable(produto.getIccids()).forEach(produtoCodBarra -> {
                dbIccid.deletaIccidPorCodBarra(produtoCodBarra.getIccidPara());
                TabelaSolicitacaoCodigoBarra.trocarProdutos(context, produtoCodBarra);
            });
        }

        private static void setarSync(Context context, int solicitacaoId, String produtoId) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SYNC_DATA, Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
            contentValues.put(SYNC, 1);

            String where = String.format("[%s] = ? AND [%s] = ?", SOLICITACAO_TROCA_ID, PRODUTO_ID);
            SimpleDbHelper.INSTANCE.open(context).update(NOME_TABELA, contentValues, where,
                    new String[]{String.valueOf(solicitacaoId), produtoId});
        }

        private static void deletar(Context context) {
            SimpleDbHelper.INSTANCE.open(context)
                    .delete(NOME_TABELA, null, null);
        }

        private static void deletarPorId(Context context, int id) {
            SimpleDbHelper.INSTANCE
                    .open(context)
                    .delete(
                            NOME_TABELA,
                            String.format("%s = ?", ID_APP),
                            new String[]{String.valueOf(id)}
                    );
        }
    }

    private static class TabelaSolicitacaoCodigoBarra {

        private static final String NOME_TABELA = "SolicitacaoTrocaCodBarras";

        private static final String ID_APP = "IdAppMobile";
        private static final String SOLICITACAO_TROCA_ID = "SolicitacaoTrocaId";
        private static final String PRODUTO_ID = "CodigoItem";
        private static final String ICCID_DE = "IccidDe";
        private static final String ICCID_PARA = "IccidPara";
        private static final String ID_SERVER = "IdServer";

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER NOT NULL,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] INTEGER,\n" +
                            "[%s] VARCHAR(10),\n" +
                            "[%s] VARCHAR(30),\n" +
                            "[%s] VARCHAR(30)," +
                            "PRIMARY KEY (%s, %s));",
                    NOME_TABELA,
                    ID_APP,
                    SOLICITACAO_TROCA_ID,
                    ID_SERVER,
                    PRODUTO_ID,
                    ICCID_DE,
                    ICCID_PARA,
                    ID_APP,
                    SOLICITACAO_TROCA_ID
            );
        }

        private static String obterSolicitacaoTrocaCodBarraQuery() {
            return String.format("SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = ? AND %s = ?",
                    ID_APP,
                    SOLICITACAO_TROCA_ID,
                    PRODUTO_ID,
                    ICCID_DE,
                    ICCID_PARA,
                    ID_SERVER,
                    NOME_TABELA,
                    SOLICITACAO_TROCA_ID,
                    PRODUTO_ID);
        }

        private static long criarId(Context context) {
            String sql = String.format(
                    "SELECT MAX(%s) FROM %S",
                    TabelaSolicitacaoCodigoBarra.ID_APP,
                    TabelaSolicitacaoCodigoBarra.NOME_TABELA
            );

            long id = 0;
            try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(sql, null)) {
                if (cursor.moveToNext()) {
                    id = cursor.getLong(0);
                }
            }

            id++;

            return id;
        }

        private static List<SolicitacaoTrocaCodBarras> obterIccids(Context context, int solicitacaoId, String produtoId) {
            List<SolicitacaoTrocaCodBarras> codBarras = new ArrayList<>();
            String consulta = obterSolicitacaoTrocaCodBarraQuery();
            String[] params = new String[]{String.valueOf(solicitacaoId), produtoId};
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(consulta, params);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    codBarras.add(parseIccidCursor(cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }

            return codBarras;
        }

        private static SolicitacaoTrocaCodBarras parseIccidCursor(Cursor cursor) {
            SolicitacaoTrocaCodBarras item = new SolicitacaoTrocaCodBarras();
            item.setIdApp(cursor.getInt(0));
            item.setSolicitacaoTrocaId(cursor.getInt(1));
            item.setCodigoItem(cursor.getString(2));
            item.setIccidDe(cursor.getString(3));
            item.setIccidPara(cursor.getString(4));
            item.setIdServer(cursor.getInt(5));
            return item;
        }

        private static void salvarSolicitacaoCodBarra(Context context, long solicitacaoId, List<SolicitacaoTrocaCodBarras> codBarras) {
            Stream.ofNullable(codBarras).forEach(codBarra -> salvarSolicitacaoCodBarra(context, solicitacaoId, codBarra));
        }

        private static void salvarSolicitacaoCodBarra(Context context, long solicitacaoId, SolicitacaoTrocaCodBarras codBarra) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_APP, TabelaSolicitacaoCodigoBarra.criarId(context));
            contentValues.put(SOLICITACAO_TROCA_ID, solicitacaoId);
            contentValues.put(PRODUTO_ID, codBarra.getCodigoItem());
            contentValues.put(ICCID_DE, codBarra.getIccidDe());
            contentValues.put(ICCID_PARA, codBarra.getIccidPara());
            contentValues.put(ID_SERVER, codBarra.getIdServer());
            SimpleDbHelper.INSTANCE.open(context).insert(NOME_TABELA, null, contentValues);
        }

        private static void salvarSolicitacaoCodBarraSincronizacao(Context context, SolicitacaoTrocaCodBarras codBarra) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_APP, codBarra.getIdApp());
            contentValues.put(SOLICITACAO_TROCA_ID, codBarra.getSolicitacaoTrocaId());
            contentValues.put(PRODUTO_ID, codBarra.getCodigoItem());
            contentValues.put(ICCID_DE, codBarra.getIccidDe());
            contentValues.put(ICCID_PARA, codBarra.getIccidPara());
            contentValues.put(ID_SERVER, codBarra.getIdServer());
            SimpleDbHelper.INSTANCE.open(context).replace(NOME_TABELA, null, contentValues);
        }

        private static void trocarProdutos(Context context, SolicitacaoTrocaCodBarras codBarras) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ICCID_PARA, codBarras.getIccidPara());

            String where = String.format("[%s] = ?", ID_APP);
            SimpleDbHelper.INSTANCE.open(context).update(NOME_TABELA, contentValues, where,
                    new String[]{String.valueOf(codBarras.getIdApp())});
        }

        private static void deletar(Context context) {
            SimpleDbHelper.INSTANCE.open(context)
                    .delete(NOME_TABELA, null, null);
        }

        private static void deletarPorId(Context context, int id) {
            SimpleDbHelper.INSTANCE
                    .open(context)
                    .delete(
                            NOME_TABELA,
                            String.format("%s = ?", ID_APP),
                            new String[]{String.valueOf(id)}
                    );
        }
    }

    private static class TabelaSolicitacaoMotivo {

        private static final String NOME_TABELA = "SolicitacaoTrocaMotivo";

        private static final String ID_SERVER = "IdServer";
        private static final String ID_MOTIVO = "MotivoId";
        private static final String DESCRICAO = "Descricao";
        private static final String ATIVO = "Ativo";

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] VARCHAR(10),\n" +
                            "[%s] VARCHAR(10),\n" +
                            "[%s] VARCHAR(100),\n" +
                            "[%s] INTEGER);",
                    NOME_TABELA,
                    ID_SERVER,
                    ID_MOTIVO,
                    DESCRICAO,
                    ATIVO
            );
        }

        private static List<SolicitacaoTrocaMotivo> obterMotivos(Context context) {
            List<SolicitacaoTrocaMotivo> motivos = new ArrayList<>();

            String consulta = String.format("SELECT %s, " +
                            "%s, " +
                            "%s, " +
                            "%s " +
                            "FROM %s " +
                            "WHERE %s = ?",
                    ID_SERVER,
                    ID_MOTIVO,
                    DESCRICAO,
                    ATIVO,
                    NOME_TABELA,
                    ATIVO);

            Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(consulta, new String[]{"1"});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    motivos.add(parseMotivosCursor(cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }
            return motivos;
        }

        private static SolicitacaoTrocaMotivo parseMotivosCursor(Cursor cursor) {
            SolicitacaoTrocaMotivo item = new SolicitacaoTrocaMotivo();
            item.setIdServer(cursor.getInt(0));
            item.setMotivoId(cursor.getString(1));
            item.setDescricao(cursor.getString(2));
            item.setAtivo(Util_IO.numberToBoolean(cursor.getInt(3)));
            return item;
        }

        private static long addSolicitacaoTrocaMotivo(Context context, SolicitacaoTrocaMotivo solicitacaoTrocaMotivo) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TabelaSolicitacaoMotivo.ID_SERVER, solicitacaoTrocaMotivo.getIdServer());
            contentValues.put(TabelaSolicitacaoMotivo.ID_MOTIVO, solicitacaoTrocaMotivo.getMotivoId());
            contentValues.put(TabelaSolicitacaoMotivo.DESCRICAO, solicitacaoTrocaMotivo.getDescricao());
            contentValues.put(TabelaSolicitacaoMotivo.ATIVO, Util_IO.booleanToNumber(solicitacaoTrocaMotivo.isAtivo()));
            if (existeMotivo(context, solicitacaoTrocaMotivo)) {
                String filtrarPeloMotivoId = ID_MOTIVO + " = ?";
                return SimpleDbHelper.INSTANCE.open(context)
                        .update(
                                TabelaSolicitacaoMotivo.NOME_TABELA,
                                contentValues,
                                filtrarPeloMotivoId,
                                new String[]{solicitacaoTrocaMotivo.getMotivoId()}
                        );
            }
            return SimpleDbHelper.INSTANCE.open(context).insert(TabelaSolicitacaoMotivo.NOME_TABELA, null, contentValues);
        }

        private static boolean existeMotivo(Context context, SolicitacaoTrocaMotivo motivo) {
            String consulta = String.format("SELECT %s, " +
                            "%s, " +
                            "%s, " +
                            "%s " +
                            "FROM %s " +
                            "WHERE %s = ?",
                    ID_SERVER,
                    ID_MOTIVO,
                    DESCRICAO,
                    ATIVO,
                    NOME_TABELA,
                    ID_SERVER);
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                    .rawQuery(consulta, new String[]{motivo.getMotivoId()});

            boolean temDados = cursor != null && cursor.getCount() > 0;

            if (cursor != null) {
                cursor.close();
            }

            return temDados;
        }

        private static void deletar(Context context) {
            SimpleDbHelper.INSTANCE.open(context)
                    .delete(NOME_TABELA, null, null);
        }
    }
}
