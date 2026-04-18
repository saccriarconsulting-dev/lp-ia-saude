package com.axys.redeflexmobile.shared.util;

import androidx.annotation.Nullable;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.dao.PrecoDao;
import com.axys.redeflexmobile.shared.dao.VendaDao;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Venda;

import java.util.List;

public class PrecoDiferenciadoValidadorImpl implements PrecoDiferenciadoValidador {

    private PrecoDao precoDao;
    private VendaDao vendaDao;
    @Nullable private PrecoVendido precoVendido;

    public PrecoDiferenciadoValidadorImpl(PrecoDao precoDao, VendaDao vendaDao) {
        this.precoDao = precoDao;
        this.vendaDao = vendaDao;
    }

    public boolean validarPrecoDiferenciado(List<ItemVendaCombo> listaFinal, Venda venda) {
        boolean existeDiferenciado = false;
        int limiteVenda = 0;
        int quantidadeItemDiferenciado = 0;
        String idProduto = "";

        listaFinal = Stream.ofNullable(listaFinal)
                .sortBy(ItemVenda::getIdProduto)
                .toList();

        int count = 0;
        for (ItemVendaCombo item : listaFinal) {
            PrecoDiferenciado precoDiferenciado = (item.getIdPreco() > 0)
                    ? precoDao.getPrecoById(String.valueOf(item.getIdPreco()))
                    : null;

            List<ItemVendaCombo> itemsJaAdicionados = vendaDao
                    .getItensComboVendaByIdVenda(venda.getId());
            List<ItemVendaCombo> itemsIguais = Stream.ofNullable(itemsJaAdicionados)
                    .filter(value -> value.getIdProduto().equals(item.getIdProduto())
                            && value.getIdPreco() == item.getIdPreco())
                    .toList();

            if (idProduto.isEmpty()) {
                idProduto = item.getIdProduto();
            } else if (!idProduto.equals(item.getIdProduto())) {
                existeDiferenciado = false;
                limiteVenda = 0;
                quantidadeItemDiferenciado = 0;
                idProduto = item.getIdProduto();
                count = 0;
            }

            if (precoDiferenciado != null) {
                existeDiferenciado = true;
                limiteVenda = precoDiferenciado.getQtdPreco();
            }

            if (!itemsIguais.isEmpty()) {
                int qtdTotalPrecoDiferenciado = 0;
                for (ItemVendaCombo igual : itemsIguais) {
                    if (igual.getBipagem().equals(ItemVenda.LER_CODIGO_BARRA)) {
                        qtdTotalPrecoDiferenciado += igual.getQuantidadeSerial();
                    } else {
                        qtdTotalPrecoDiferenciado += igual.getQtde();
                    }
                }

                if (qtdTotalPrecoDiferenciado > limiteVenda) {
                    existeDiferenciado = false;
                }

                if (count < 1) {
                    quantidadeItemDiferenciado += item.getQtde() + qtdTotalPrecoDiferenciado;
                } else {
                    quantidadeItemDiferenciado += item.getQtde();
                }
            } else {
                quantidadeItemDiferenciado += item.getQtde();
            }

            if (existeDiferenciado && limiteVenda < quantidadeItemDiferenciado) {
                double preco = precoDiferenciado != null
                        ? precoDiferenciado.getValor()
                        : 0.0;
                precoVendido = new PrecoVendido(
                        item.getNomeProduto(),
                        preco,
                        quantidadeItemDiferenciado,
                        limiteVenda
                );

                return false;
            }

            count++;
        }

        return true;
    }

    @Nullable
    public PrecoVendido getPrecoVendido() {
        return precoVendido;
    }

    public static class PrecoVendido {
        private String nomeProduto;
        private double valorDiferenca;
        private int quantidadeDiferenciada;
        private int limiteVenda;

        PrecoVendido(String nomeProduto, double valorDiferenca, int quantidadeDiferenciada, int limiteVenda) {
            this.nomeProduto = nomeProduto;
            this.valorDiferenca = valorDiferenca;
            this.quantidadeDiferenciada = quantidadeDiferenciada;
            this.limiteVenda = limiteVenda;
        }

        public String getNomeProduto() {
            return nomeProduto;
        }

        public double getValorDiferenca() {
            return valorDiferenca;
        }

        public int getQuantidadeDiferenciada() {
            return quantidadeDiferenciada;
        }

        public int getLimiteVenda() {
            return limiteVenda;
        }
    }
}
