package com.axys.redeflexmobile.shared.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;

public class VendaDaoImpl implements VendaDao {

    private final Context context;

    public VendaDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<ItemVendaCombo> getItensComboVendaByIdVenda(int idVenda) {
        return Tabela.TabelaItemVenda.obterItensComboVendaPorIdVenda(context, idVenda);
    }

    @Override
    public List<CodBarra> getCodBarraItens(String idVendaItem, int grupo) {
        return Tabela.TabelaItemVendaCodigoBarra.obterCodigoBarras(context, idVendaItem, grupo);
    }

    @Override
    public Venda getVendaByIdVisita(int idVisita) {
        return Tabela.TabelaVenda.obterVendaPorIdVisita(context, idVisita);
    }

    @Override
    public Venda getVendaById(int id) {
        return Tabela.TabelaVenda.obterVendaPorId(context, id);
    }

    @Override
    public List<ItemVenda> getItensVendaByIdVenda(int idVenda) {
        return Tabela.TabelaItemVenda.obterItensVendaPorIdVenda(context, idVenda);
    }

    @Override
    public ItemVenda getItemVendaById(int idVendaItem) {
        return Tabela.TabelaItemVenda.obterItemVendaPorId(context, idVendaItem);
    }

    @Override
    public long novaVenda(int idVisita, String idCliente) {
        return Tabela.TabelaVenda.criarVendaSimples(context, idVisita, idCliente);
    }

    @Override
    public void removeEstoqueComboByIdVendaItem(int idVendaItem) {
        try {
            DBEstoque dbEstoque = new DBEstoque(context);
            String query = "SELECT id, qtd, idProduto FROM [ItemVendaCombo] WHERE idItemVenda = ? GROUP BY idProduto";
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(query, new String[]{String.valueOf(idVendaItem)});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        dbEstoque.atualizaEstoque(cursor.getString(2), false, cursor.getInt(1));
                        String mTabelaCombo = "ItemVendaCombo";
                        SimpleDbHelper.INSTANCE.open(context).delete(mTabelaCombo, "[id]=?", new String[]{String.valueOf(cursor.getInt(0))});
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteItemByIdItem(int idItemVenda) {
        Tabela.TabelaItemVenda.removerItemVenda(context, idItemVenda);
    }

    @Override
    public long addItemVenda(Venda venda, Produto produto, List<CodBarra> codigosBarra,
                             double valor, List<ComboVenda> listaCombo,
                             String idPrecoDiferenciado) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id, qtde");
        sb.appendLine("FROM [ItemVenda]");
        sb.appendLine("WHERE idVenda = ?");
        sb.appendLine("AND idProduto = ?");
        sb.appendLine("AND valorUN = ?");
        sb.appendLine("AND IFNULL(idPreco, 0) = " + (idPrecoDiferenciado == null ? "0" : idPrecoDiferenciado));

        long idItemVenda = 0;
        int quantidade = 0;
        Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), new String[]{String.valueOf(venda.getId())
                , produto.getId(), String.valueOf(Util_IO.getValor(valor))});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                idItemVenda = cursor.getInt(0);
                quantidade = cursor.getInt(1);
            }
            cursor.close();
        }

        if (idItemVenda == 0) {
            ItemVendaCombo itemVendaCombo = new ItemVendaCombo();
            itemVendaCombo.setIdProduto(produto.getId());
            itemVendaCombo.setDataVenda(venda.getData());
            itemVendaCombo.setQtde(produto.getQtde());
            itemVendaCombo.setValorUN(valor);
            itemVendaCombo.setCombo(listaCombo != null && listaCombo.size() != 0);
            itemVendaCombo.setIdPreco(idPrecoDiferenciado);

            idItemVenda = Tabela.TabelaItemVenda.inserirItemVenda(context, itemVendaCombo);
        } else {
            quantidade += produto.getQtde();
            Tabela.TabelaItemVenda.atualizarQuantidadeItemVenda(context, idItemVenda, quantidade);
        }

        if (listaCombo != null && listaCombo.size() > 0) {
            for (ComboVenda comboVenda : listaCombo) {
                comboVenda.setIdVenda(venda.getId());
                comboVenda.setIdItemVenda(idItemVenda);
                Tabela.TabelaItemVendaCombo.inserirItemVendaCombo(context, comboVenda);
            }
        }

        if (codigosBarra != null && codigosBarra.size() > 0) {
            for (CodBarra codBarra : codigosBarra) {
                codBarra.setIdVenda(venda.getId());
                codBarra.setIdVendaItem(
                        codBarra.getIdVendaItem() == null
                                ? String.valueOf(idItemVenda)
                                : codBarra.getIdVendaItem()
                );
                codBarra.setDataVenda(venda.getData());
                codBarra.setIdProdutoObjeto(produto.getId());
                Tabela.TabelaItemVendaCodigoBarra.inserirItemVendaCodigoBarra(context, codBarra);
            }
        }
        return idItemVenda;
    }

    @Override
    public long addItemVenda(Venda venda, Produto produto, List<CodBarra> codigosBarra,
                             double valor, List<ComboVenda> listaCombo,
                             String idPrecoDiferenciado, boolean isCombo, boolean unificar) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id, qtde");
        sb.appendLine("FROM [ItemVenda]");
        sb.appendLine("WHERE idVenda = ?");
        sb.appendLine("AND idProduto = ?");
        sb.appendLine("AND valorUN = ?");
        sb.appendLine("AND IFNULL(idPreco, 0) = " + (idPrecoDiferenciado == null ? "0" : idPrecoDiferenciado));

        long idItemVenda = 0;
        int quantidade = 0;

        if (listaCombo.size() <= 0) {
            listaCombo = getItemsCombo(produto.getId(), produto.getQtde());
        }

        if (unificar) {
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), new String[]{String.valueOf(venda.getId())
                    , produto.getId(), String.valueOf(Util_IO.getValor(valor))});
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    idItemVenda = cursor.getInt(0);
                    quantidade = cursor.getInt(1);
                }
                cursor.close();
            }
        }

        if (idItemVenda == 0) {
            ItemVendaCombo itemVendaCombo = new ItemVendaCombo();
            itemVendaCombo.setIdProduto(produto.getId());
            itemVendaCombo.setIdVenda(venda.getId());
            itemVendaCombo.setDataVenda(venda.getData());
            itemVendaCombo.setQtde(produto.getQtde());
            itemVendaCombo.setValorUN(valor);
            itemVendaCombo.setCombo(isCombo);
            itemVendaCombo.setIdPreco(idPrecoDiferenciado);
            itemVendaCombo.setQtdCombo(produto.getQtdCombo());
            idItemVenda = Tabela.TabelaItemVenda.inserirItemVenda(context, itemVendaCombo);
        } else {
            quantidade += produto.getQtde();
            Tabela.TabelaItemVenda.atualizarQuantidadeItemVenda(context, idItemVenda, quantidade);
        }

        if (listaCombo != null && listaCombo.size() > 0) {
            for (ComboVenda comboVenda : listaCombo) {
                comboVenda.setIdVenda(venda.getId());
                comboVenda.setIdItemVenda(idItemVenda);
                Tabela.TabelaItemVendaCombo.inserirItemVendaCombo(context, comboVenda);
            }
        }

        if (codigosBarra != null && codigosBarra.size() > 0) {
            for (CodBarra codBarra : codigosBarra) {
                codBarra.setIdVenda(venda.getId());
                codBarra.setIdVendaItem(
                        codBarra.getIdVendaItem() == null
                                ? String.valueOf(idItemVenda)
                                : codBarra.getIdVendaItem()
                );
                codBarra.setDataVenda(venda.getData());
                codBarra.setIdProdutoObjeto(produto.getId());
                Tabela.TabelaItemVendaCodigoBarra.inserirItemVendaCodigoBarra(context, codBarra);
            }
        }

        return idItemVenda;
    }

    @Override
    public void atualizarQuantidadeCombo(int quantidade, String idVendaItem) {
        ContentValues values = new ContentValues();
        values.put("qtde", quantidade);
        String mTabelaItem = "ItemVenda";
        SimpleDbHelper.INSTANCE.open(context).update(mTabelaItem, values, "[id]=?", new String[]{idVendaItem});
    }

    @Override
    public long addItemVenda(Venda venda, Produto produto, List<CodBarra> codigosBarra,
                             double valor, List<ComboVenda> listaCombo, String idPrecoDiferenciado,
                             long idItemVenda, boolean atualizaQuantidade) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id, qtde");
        sb.appendLine("FROM [ItemVenda]");
        sb.appendLine("WHERE idVenda = ?");
        sb.appendLine("AND idProduto = ?");
        sb.appendLine("AND valorUN = ?");
        sb.appendLine("AND IFNULL(idPreco, 0) = " + (idPrecoDiferenciado == null ? "0" : idPrecoDiferenciado));

        int quantidade = 0;
        if (idItemVenda == 0) {
            ItemVendaCombo itemVendaCombo = new ItemVendaCombo();
            itemVendaCombo.setIdProduto(produto.getId());
            itemVendaCombo.setIdVenda(venda.getId());
            itemVendaCombo.setDataVenda(venda.getData());
            itemVendaCombo.setQtde(produto.getQtde());
            itemVendaCombo.setValorUN(valor);
            itemVendaCombo.setCombo(listaCombo != null && listaCombo.size() != 0);
            itemVendaCombo.setIdPreco(idPrecoDiferenciado);
        }

        if (atualizaQuantidade) {
            ItemVenda quantidadeAnterior = getItemVendaById((int) idItemVenda);
            int atualizaEstoque = produto.getQtde() - quantidadeAnterior.getQtde();
            String query = "UPDATE [Produto] SET [estoqueAtual] = [estoqueAtual] - " + atualizaEstoque + " WHERE [id] = '" + produto.getId() + "'";
            SimpleDbHelper.INSTANCE.open(context).execSQL(query);

            quantidade += produto.getQtde();
            Tabela.TabelaItemVenda.atualizarQuantidadeItemVenda(context, idItemVenda, quantidade);
        }

        if (listaCombo != null && listaCombo.size() > 0) {
            for (ComboVenda comboVenda : listaCombo) {
                comboVenda.setIdVenda(venda.getId());
                comboVenda.setIdItemVenda(idItemVenda);
                Tabela.TabelaItemVendaCombo.inserirItemVendaCombo(context, comboVenda);
            }
        }

        if (codigosBarra != null && codigosBarra.size() > 0) {
            for (CodBarra codBarra : codigosBarra) {
                codBarra.setIdVenda(venda.getId());
                codBarra.setIdVendaItem(
                        codBarra.getIdVendaItem() == null
                                ? String.valueOf(idItemVenda)
                                : codBarra.getIdVendaItem()
                );
                codBarra.setDataVenda(venda.getData());
                codBarra.setIdProdutoObjeto(produto.getId());
                Tabela.TabelaItemVendaCodigoBarra.inserirItemVendaCodigoBarra(context, codBarra);
            }
        }
        return idItemVenda;
    }

    @Override
    public int retornaQtdPrecoDiferenciado(String pIdPreco) {
        Cursor cursor = null;
        int retorno = 0;
        try {
            String querySql = "SELECT SUM(qtde) FROM [ItemVenda] WHERE idPreco = ? AND cancelado = 0";
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(querySql, new String[]{pIdPreco});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        retorno += cursor.getInt(0);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            retorno = 0;
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return retorno;
    }

    @Override
    public void removerCodigoBarra(CodBarra codBarra) {
        Tabela.TabelaItemVendaCodigoBarra.removerCodigoBarra(context, codBarra);
    }

    @Override
    public void deleteVendaByIdVisita(int idVisita) {
        Venda venda = getVendaByIdVisita(idVisita);
        Tabela.TabelaVenda.cancelarVenda(context, venda);
    }

    @Override
    public double retornaValorTotalVenda(int idVenda) {
        return Tabela.TabelaVenda.obterTotalVenda(context, idVenda);
    }

    public boolean iccidVendido(CodBarra pCodBarra) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine("FROM [ItemVendaCodigoBarra]");
        sb.appendLine("WHERE [cancelado] = 0");
        if (pCodBarra != null) {
            if (pCodBarra.getIndividual()) {
                sb.appendLine("AND ([codigoBarra] = '" + pCodBarra.getCodBarraInicial() + "'");
                sb.appendLine("OR [codigoBarraFinal] = '" + pCodBarra.getCodBarraInicial() + "')");
            } else {
                sb.appendLine("AND (([codigoBarra] = '" + pCodBarra.getCodBarraInicial() + "'");
                sb.appendLine("OR [codigoBarraFinal] = '" + pCodBarra.getCodBarraInicial() + "')");
                sb.appendLine("OR ([codigoBarra] = '" + pCodBarra.getCodBarraFinal() + "'");
                sb.appendLine("OR [codigoBarraFinal] = '" + pCodBarra.getCodBarraFinal() + "'))");
            }
        } else
            return false;
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), null)) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public List<ComboVenda> getItemsCombo(String codigoProd, int qtd) {
        List<ComboVenda> lista = new ArrayList<>();
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT itemPai ");
        sb.appendLine("FROM [EstruturaProd]");
        sb.appendLine("WHERE itemPai = '" + codigoProd + "'");
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), null)) {
            if (cursor.moveToFirst()) {
                do {
                    ComboVenda item = new ComboVenda();
                    item.setIdProduto(cursor.getString(0));
                    item.setQuantidade(qtd);
                    lista.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return lista;
        }
        return lista;
    }
}
