package com.axys.redeflexmobile.shared.bd;

import android.content.Context;

import com.axys.redeflexmobile.shared.models.venda.ProdutoMerchandising;
import com.axys.redeflexmobile.ui.venda.merchandising.ConstantesMerchandising;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DBProdutosMerchandising {

    private Context mContext;
    private String mTabela = "ProdutosMerchandising";

    public DBProdutosMerchandising(Context mContext) {
        this.mContext = mContext;
    }

    public List<ProdutoMerchandising> obterProdutos(String tipo) {
        //TODO: Implementar a query para buscar no banco

        switch (tipo) {
            case ConstantesMerchandising.VIVO:
                return Arrays.asList(
                        new ProdutoMerchandising(1, "Produto Vivo 1", ConstantesMerchandising.ID_VIVO, null),
                        new ProdutoMerchandising(2, "Produto Vivo 2", ConstantesMerchandising.ID_VIVO, null),
                        new ProdutoMerchandising(3, "Produto Vivo 3", ConstantesMerchandising.ID_VIVO, null),
                        new ProdutoMerchandising(4, "Produto Vivo 4", ConstantesMerchandising.ID_VIVO, null),
                        new ProdutoMerchandising(5, "Produto Vivo 5", ConstantesMerchandising.ID_VIVO, null),
                        new ProdutoMerchandising(6, "Produto Vivo 6", ConstantesMerchandising.ID_VIVO, null),
                        new ProdutoMerchandising(7, "Selecione o produto", ConstantesMerchandising.ID_VIVO, null)
                );
            case ConstantesMerchandising.CLARO:
                return Arrays.asList(
                        new ProdutoMerchandising(8, "Produto Claro 1", ConstantesMerchandising.ID_CLARO, null),
                        new ProdutoMerchandising(9, "Produto Claro 2", ConstantesMerchandising.ID_CLARO, null),
                        new ProdutoMerchandising(10, "Produto Claro 3", ConstantesMerchandising.ID_CLARO, null),
                        new ProdutoMerchandising(11, "Produto Claro 4", ConstantesMerchandising.ID_CLARO, null),
                        new ProdutoMerchandising(12, "Produto Claro 5", ConstantesMerchandising.ID_CLARO, null),
                        new ProdutoMerchandising(13, "Produto Claro 6", ConstantesMerchandising.ID_CLARO, null),
                        new ProdutoMerchandising(14, "Selecione o produto", ConstantesMerchandising.ID_CLARO, null)
                );
            case ConstantesMerchandising.TIM:
                return Arrays.asList(
                        new ProdutoMerchandising(15, "Produto Tim 1", ConstantesMerchandising.ID_TIM, null),
                        new ProdutoMerchandising(16, "Produto Tim 2", ConstantesMerchandising.ID_TIM, null),
                        new ProdutoMerchandising(17, "Produto Tim 3", ConstantesMerchandising.ID_TIM, null),
                        new ProdutoMerchandising(18, "Produto Tim 4", ConstantesMerchandising.ID_TIM, null),
                        new ProdutoMerchandising(19, "Produto Tim 5", ConstantesMerchandising.ID_TIM, null),
                        new ProdutoMerchandising(20, "Produto Tim 6", ConstantesMerchandising.ID_TIM, null),
                        new ProdutoMerchandising(21, "Selecione o produto", ConstantesMerchandising.ID_TIM, null)
                );
            case ConstantesMerchandising.OI:
                return Arrays.asList(
                        new ProdutoMerchandising(22, "Produto Oi 1", ConstantesMerchandising.ID_OI, null),
                        new ProdutoMerchandising(23, "Produto Oi 2", ConstantesMerchandising.ID_OI, null),
                        new ProdutoMerchandising(24, "Produto Oi 3", ConstantesMerchandising.ID_OI, null),
                        new ProdutoMerchandising(25, "Produto Oi 4", ConstantesMerchandising.ID_OI, null),
                        new ProdutoMerchandising(26, "Produto Oi 5", ConstantesMerchandising.ID_OI, null),
                        new ProdutoMerchandising(27, "Produto Oi 6", ConstantesMerchandising.ID_OI, null),
                        new ProdutoMerchandising(28, "Selecione o produto", ConstantesMerchandising.ID_OI, null)
                );
            default:
                return Collections.emptyList();
        }
    }
}
