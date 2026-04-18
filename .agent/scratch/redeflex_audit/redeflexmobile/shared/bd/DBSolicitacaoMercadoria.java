package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.enums.EnumStatusSolicitacao;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.SolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 06/04/2016.
 */
public class DBSolicitacaoMercadoria {
    private Context mContext;
    private final String TABELA = "SolicMerc";
    private final String TABELA_ITEM = "ItensSolicMerc";
    private final String TABELA_SOLICITACAO = "SituacaoSolicitacao";

    public DBSolicitacaoMercadoria(Context _context) {
        mContext = _context;
    }

    public long NovaSolicitacao(ArrayList<Produto> itens) {
        ContentValues values = new ContentValues();
        values.put("idServer", 0);
        long id = SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA, null, values);

        for (Produto obj : itens) {
            values = new ContentValues();
            values.put("idSolicMerc", id);
            values.put("idProduto", obj.getId());
            values.put("qtde", obj.getQtde());
            values.put("qtdeSugerida", obj.getEstoqueSugerido());
            values.put("estoqueMax", obj.getEstoqueMax());
            values.put("mediaDiariaVnd", obj.getMediaDiariaVnd());
            values.put("diasEstoque", obj.getDiasEstoque());
            values.put("estoqueAtual", obj.getEstoqueAtual());
            SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA_ITEM, null, values);
        }

        addSituacao(id, EnumStatusSolicitacao.Pendente.getValue(), "");
        return id;
    }

    private int getIdIfSolicitacaoCadastrada(int idServer) {
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).query(TABELA, new String[]{"id"}
                    , "idServer=?", new String[]{String.valueOf(idServer)}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return Integer.parseInt(cursor.getString(0));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    public long NovaSolicitacaoCriadaNoServer(SolicitacaoMercadoria model) {
        int idAppMobile = getIdIfSolicitacaoCadastrada(model.getIdServer());
        if (idAppMobile == 0) {
            ContentValues values = new ContentValues();
            values.put("idServer", model.getId());
            values.put("dataCriacao", Util_IO.dateTimeToString(model.getDataCriacao(), "yyyy-MM-dd HH:mm:ss"));

            long id = SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA, null, values);

            for (Produto obj : model.getItens()) {
                values = new ContentValues();
                values.put("idSolicMerc", id);
                values.put("idProduto", obj.getId());
                values.put("qtde", obj.getQtde());
                values.put("qtdeSugerida", obj.getEstoqueSugerido());
                values.put("estoqueMax", obj.getEstoqueMax());
                values.put("mediaDiariaVnd", obj.getMediaDiariaVnd());
                values.put("diasEstoque", obj.getDiasEstoque());
                values.put("estoqueAtual", obj.getEstoqueAtual());
                SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA_ITEM, null, values);
            }

            addSituacao(id, EnumStatusSolicitacao.Aprovada.getValue(), Util_IO.dateTimeToString(model.getDataCriacao(), "yyyy-MM-dd HH:mm:ss"));
            return id;
        } else
            return idAppMobile;
    }

    public long AddSolicitacaoByReSync(SolicitacaoMercadoria model) {
        int idAppMobile = getIdIfSolicitacaoCadastrada(model.getId());//nesse caso o isServer ta no campo id pq acabou de vir do server
        if (idAppMobile == 0) {
            ContentValues values = new ContentValues();
            values.put("idServer", model.getId());
            values.put("dataCriacao", Util_IO.dateTimeToString(model.getDataCriacao(), "yyyy-MM-dd HH:mm:ss"));

            long id = SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA, null, values);

            for (Produto obj : model.getItens()) {
                values = new ContentValues();
                values.put("idSolicMerc", id);
                values.put("idProduto", obj.getId());
                values.put("qtde", obj.getQtde());
                values.put("qtdeSugerida", obj.getEstoqueSugerido());
                values.put("estoqueMax", obj.getEstoqueMax());
                values.put("mediaDiariaVnd", obj.getMediaDiariaVnd());
                values.put("diasEstoque", obj.getDiasEstoque());
                values.put("estoqueAtual", obj.getEstoqueAtual());
                SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA_ITEM, null, values);
            }
            return id;
        } else
            return idAppMobile;
    }

    public void updateIdServer(String idServer, int idapp) {
        ContentValues values = new ContentValues();
        values.put("idServer", idServer);
        SimpleDbHelper.INSTANCE.open(mContext).update(TABELA, values, "id=?", new String[]{String.valueOf(idapp)});
        addSituacao(idapp, EnumStatusSolicitacao.Enviada.getValue(), "");
    }

    private boolean isSituacaoCadastrada(long idSolicMerc, int idStatus) {
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).query(TABELA_SOLICITACAO, new String[]{"idSolicMerc"}
                    , "idSolicMerc=? AND idStatus=?", new String[]{String.valueOf(idSolicMerc), String.valueOf(idStatus)}, null, null, null, null);
            return (cursor != null && cursor.getCount() > 0);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public boolean addSituacao(long idSolicitacao, int idStatus, String data) {
        if (!isSituacaoCadastrada(idSolicitacao, idStatus)) {
            ContentValues values = new ContentValues();
            values.put("idSolicMerc", idSolicitacao);
            values.put("idStatus", idStatus);
            if (!data.equals(""))
                values.put("data", data);
            long id = SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA_SOLICITACAO, null, values);
            return id > 0;
        } else
            return true;
    }

    public SolicitacaoMercadoria getById(int id) {
        String selectQuery = "SELECT id,idServer,dataCriacao FROM [SolicMerc] WHERE id = " + id;
        Cursor cursorPedido = null, cursorSt = null, cursorItemPedido = null;
        SolicitacaoMercadoria pedido = null;
        try {
            cursorPedido = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
            if (cursorPedido != null && cursorPedido.getCount() > 0) {
                cursorPedido.moveToFirst();
                pedido = new SolicitacaoMercadoria();
                pedido.setId(Integer.parseInt(cursorPedido.getString(0)));
                pedido.setIdServer(Integer.parseInt(cursorPedido.getString(1)));
                pedido.setDataCriacao(Util_IO.stringToDate(cursorPedido.getString(2), "yyyy-MM-dd HH:mm:ss"));

                selectQuery = "SELECT idStatus,data FROM [SituacaoSolicitacao] WHERE idSolicMerc = " + id + " ORDER BY data DESC,idStatus DESC";
                cursorSt = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);

                if (cursorSt != null && cursorSt.getCount() > 0) {
                    cursorSt.moveToFirst();
                    pedido.setStatus(Integer.parseInt(cursorSt.getString(0)));
                    pedido.setDataUltimaAtualizacao(Util_IO.stringToDate(cursorSt.getString(1), "yyyy-MM-dd HH:mm:ss"));
                }

                Produto itemPedido;

                selectQuery = "SELECT i.idProduto," +
                        " IFNULL(p.nome,0) AS nome," +
                        " IFNULL(i.qtde,0) AS qtde," +
                        " IFNULL(i.estoqueMax,0) AS estoqueMax,  " +
                        " IFNULL(i.mediaDiariaVnd,0) AS mediaDiariaVnd,  " +
                        " IFNULL(i.diasEstoque,0) AS diasEstoque,  " +
                        " IFNULL(i.qtdeSugerida,0) AS qtdeSugerida,  " +
                        " IFNULL(i.estoqueAtual,0) AS estoqueAtual  " +
                        " FROM [ItensSolicMerc] i" +
                        " LEFT OUTER JOIN [Produto] p ON (i.idProduto = p.id) " +
                        " WHERE i.idSolicMerc = " + pedido.getId() + " ORDER BY i.id ";
                cursorItemPedido = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
                if (cursorItemPedido != null && cursorItemPedido.getCount() > 0) {
                    if (cursorItemPedido.moveToFirst()) {
                        do {
                            itemPedido = new Produto();
                            itemPedido.setId(cursorItemPedido.getString(0));
                            itemPedido.setNome(cursorItemPedido.getString(1));
                            itemPedido.setQtde(Integer.parseInt(cursorItemPedido.getString(2)));
                            itemPedido.setEstoqueMax(Integer.parseInt(cursorItemPedido.getString(3)));
                            itemPedido.setMediaDiariaVnd(Double.parseDouble(cursorItemPedido.getString(4)));
                            itemPedido.setDiasEstoque(Integer.parseInt(cursorItemPedido.getString(5)));
                            itemPedido.setEstoqueSugerido(Integer.parseInt(cursorItemPedido.getString(6)));
                            itemPedido.setEstoqueAtual(Integer.parseInt(cursorItemPedido.getString(7)));
                            pedido.getItens().add(itemPedido);
                        } while (cursorItemPedido.moveToNext());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursorPedido != null)
                cursorPedido.close();
            if (cursorSt != null)
                cursorSt.close();
            if (cursorItemPedido != null)
                cursorItemPedido.close();
        }
        return pedido;
    }

    public SolicitacaoMercadoria getByIdSemItens(int id) {
        String selectQuery = "SELECT id,idServer,dataCriacao FROM [SolicMerc] WHERE id = " + id;
        Cursor cursorPedido = null, cursorSt = null;
        SolicitacaoMercadoria pedido = null;
        try {
            cursorPedido = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
            if (cursorPedido != null && cursorPedido.getCount() > 0) {
                cursorPedido.moveToFirst();
                pedido = new SolicitacaoMercadoria();
                pedido.setId(Util_IO.integerTryParse(cursorPedido.getString(0)));
                pedido.setIdServer(Util_IO.integerTryParse(cursorPedido.getString(1)));
                pedido.setDataCriacao(Util_IO.stringToDate(cursorPedido.getString(2), "yyyy-MM-dd HH:mm:ss"));

                selectQuery = "SELECT idStatus,data FROM [SituacaoSolicitacao] WHERE idSolicMerc = " + id + " ORDER BY data DESC,idStatus DESC";
                cursorSt = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
                if (cursorSt != null && cursorSt.getCount() > 0) {
                    cursorSt.moveToFirst();
                    pedido.setStatus(Util_IO.integerTryParse(cursorSt.getString(0)));
                    pedido.setDataUltimaAtualizacao(Util_IO.stringToDate(cursorSt.getString(1), "yyyy-MM-dd HH:mm:ss"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursorPedido != null)
                cursorPedido.close();
            if (cursorSt != null)
                cursorSt.close();
        }
        return pedido;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public ArrayList<SolicitacaoMercadoria> getByAll() {
        ArrayList<SolicitacaoMercadoria> list = new ArrayList<>();
        Cursor cursorSt = null, cursor = null;
        try {
            String selectQuery = "SELECT id,idServer,dataCriacao FROM [SolicMerc] ORDER BY dataCriacao DESC ";
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        SolicitacaoMercadoria pedido = new SolicitacaoMercadoria();
                        pedido.setId(Util_IO.integerTryParse(cursor.getString(0)));
                        pedido.setIdServer(Util_IO.integerTryParse(cursor.getString(1)));
                        pedido.setDataCriacao(Util_IO.stringToDate(cursor.getString(2), "yyyy-MM-dd HH:mm:ss"));

                        selectQuery = "SELECT idStatus,data FROM [SituacaoSolicitacao] WHERE idSolicMerc = " + pedido.getId() + " ORDER BY data DESC, idStatus DESC";

                        cursorSt = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
                        if (cursorSt != null && cursorSt.getCount() > 0) {
                            cursorSt.moveToFirst();
                            pedido.setStatus(Util_IO.integerTryParse(cursorSt.getString(0)));
                            pedido.setDataUltimaAtualizacao(Util_IO.stringToDate(cursorSt.getString(1), "yyyy-MM-dd HH:mm:ss"));
                        }
                        list.add(pedido);
                    } while (cursor.moveToNext());
                }
            }

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursorSt != null)
                cursorSt.close();
        }

        return list;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public ArrayList<SolicitacaoMercadoria> getPendentes() {
        ArrayList<SolicitacaoMercadoria> list = new ArrayList<>();
        Cursor cursor = null, cursorSt = null, cursorItemPedido = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT t0.id,t0.idServer,t0.dataCriacao,(SELECT id FROM Colaborador) AS idVendedor,t1.[idStatus],t1.data  ");
            sb.append("FROM [SolicMerc]  t0 ");
            sb.append("JOIN (SELECT * FROM [SituacaoSolicitacao] ta ");
            sb.append("             WHERE EXISTS (SELECT * FROM (SELECT MAX(idStatus) AS idStatus, idSolicMerc ");
            sb.append("                                                 FROM [SituacaoSolicitacao] GROUP BY idSolicMerc) tb ");
            sb.append("                                     WHERE tb.idStatus = ta.idStatus AND tb.idSolicMerc = ta.idSolicMerc) ");
            sb.append("                           ) t1 ON (t1.idSolicMerc = t0.id) ");
            sb.append("WHERE IFNULL(t1.[idStatus],0) = 0");

            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            SolicitacaoMercadoria pedido;
            String selectQuery = "";
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        pedido = new SolicitacaoMercadoria();
                        pedido.setId(Util_IO.integerTryParse(cursor.getString(0)));
                        pedido.setIdServer(Util_IO.integerTryParse(cursor.getString(1)));
                        pedido.setDataCriacao(Util_IO.stringToDate(cursor.getString(2), "yyyy-MM-dd HH:mm:ss"));
                        pedido.setIdVendedor(Util_IO.integerTryParse(cursor.getString(3)));

                        selectQuery = "SELECT idStatus,data FROM [SituacaoSolicitacao] WHERE idSolicMerc = " + pedido.getId() + " ORDER BY data DESC,idStatus DESC";
                        cursorSt = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);

                        if (cursorSt != null && cursorSt.getCount() > 0) {
                            cursorSt.moveToFirst();
                            pedido.setStatus(Util_IO.integerTryParse(cursorSt.getString(0)));
                            pedido.setDataUltimaAtualizacao(Util_IO.stringToDate(cursorSt.getString(1), "yyyy-MM-dd HH:mm:ss"));
                        }

                        Produto itemPedido;
                        sb = new StringBuilder();
                        sb.append("SELECT i.idProduto,");
                        sb.append(" IFNULL(p.nome,0) AS nome,");
                        sb.append(" IFNULL(i.qtde,0) AS qtde,");
                        sb.append(" IFNULL(i.estoqueMax,0) AS estoqueMax,  ");
                        sb.append(" IFNULL(i.mediaDiariaVnd,0) AS mediaDiariaVnd,  ");
                        sb.append(" IFNULL(i.diasEstoque,0) AS diasEstoque,  ");
                        sb.append(" IFNULL(i.qtdeSugerida,0) AS qtdeSugerida,  ");
                        sb.append(" IFNULL(i.estoqueAtual,0) AS estoqueAtual");
                        sb.append(" FROM [ItensSolicMerc] i");
                        sb.append(" LEFT OUTER JOIN [Produto] p ON (i.idProduto = p.id) ");
                        sb.append(" WHERE i.idSolicMerc = " + pedido.getId());
                        sb.append(" ORDER BY i.id ");

                        cursorItemPedido = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
                        if (cursorItemPedido != null && cursorItemPedido.getCount() > 0) {
                            if (cursorItemPedido.moveToFirst()) {
                                do {
                                    itemPedido = new Produto();
                                    itemPedido.setId(cursorItemPedido.getString(0));
                                    itemPedido.setNome(cursorItemPedido.getString(1));
                                    itemPedido.setQtde(Util_IO.integerTryParse(cursorItemPedido.getString(2)));
                                    itemPedido.setEstoqueMax(Util_IO.integerTryParse(cursorItemPedido.getString(3)));
                                    itemPedido.setMediaDiariaVnd(Double.parseDouble(cursorItemPedido.getString(4)));
                                    itemPedido.setDiasEstoque(Util_IO.integerTryParse(cursorItemPedido.getString(5)));
                                    itemPedido.setEstoqueSugerido(Util_IO.integerTryParse(cursorItemPedido.getString(6)));
                                    itemPedido.setEstoqueAtual(Util_IO.integerTryParse(cursorItemPedido.getString(7)));

                                    pedido.getItens().add(itemPedido);
                                } while (cursorItemPedido.moveToNext());
                            }
                        }
                        list.add(pedido);
                    } while (cursor.moveToNext());
                }
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursorSt != null)
                cursorSt.close();
            if (cursorItemPedido != null)
                cursorItemPedido.close();
        }
        return list;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA_ITEM, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA_SOLICITACAO, null, null);
    }

    public void deleteFinalizados() {
        Cursor cursor = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT t0.id ");
            sb.append("FROM [SolicMerc] t0 ");
            sb.append("JOIN (SELECT * FROM [SituacaoSolicitacao] ta ");
            sb.append("        WHERE EXISTS (SELECT * FROM (SELECT MAX(idStatus) AS idStatus, idSolicMerc ");
            sb.append("                FROM [SituacaoSolicitacao] GROUP BY idSolicMerc) tb ");
            sb.append("                WHERE tb.idStatus = ta.idStatus AND tb.idSolicMerc = ta.idSolicMerc)) t1 ON (t1.idSolicMerc = t0.id) ");
            sb.append("WHERE 1=1 ");
            sb.append("AND t1.idStatus IN (3,4) ");
            sb.append("AND t1.data < datetime('now', '-60 day') ");
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA_SOLICITACAO, "[idSolicMerc]=? ", new String[]{cursor.getString(0)});
                        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA_ITEM, "[idSolicMerc]=? ", new String[]{cursor.getString(0)});
                        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, "[id]=? ", new String[]{cursor.getString(0)});
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }
}