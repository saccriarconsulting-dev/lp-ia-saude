package com.axys.redeflexmobile.shared.util;

import android.content.Context;
import android.content.DialogInterface;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Visita;

/**
 * Created by joao.viana on 25/09/2017.
 */

public class Mensagens {
    public static void auditagemNaoRealizada(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Hoje não foi realizada a auditagem de estoque, Verifique!!!");
        alerta.show();
    }

    public static void osSemAgendamento(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Você possui OS sem agendamento, Favor agendar todas as ordem de serviço!!!");
        alerta.show();
    }

    public static void boletosPendentes(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Você possui boleto(s) pendente(s), Favor verificar com o setor de Negociação!!!");
        alerta.show();
    }

    public static void naoEstaNasImediacoes(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Você não está nas imediações do Ponto de Venda, Não é possível iniciar o atendimento!");
        alerta.show();
    }

    public static void naoEstaNasImediacoesPOS(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Você não está nas imediações do ponto de venda. Não é possível visualizar o IDterminal.");
        alerta.show();
    }

    public static void horarioComercial(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), pContext.getResources().getString(R.string.horario));
        alerta.showError();
    }

    public static void horarioComercial(Context pContext, DialogInterface.OnClickListener pDialog) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), pContext.getResources().getString(R.string.horario));
        alerta.show(pDialog);
    }

    public static void horarioComercialInicial(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getString(R.string.app_name), pContext.getString(R.string.horario_inicial));
        alerta.show();
    }

    public static void produtoNaoSelecionado(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Produto não selecionado, Verifique!");
        alerta.show();
    }

    public static void produtoJaIncluido(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Item já incluído, Verifique!");
        alerta.show();
    }

    public static void quantidadeNaoInformada(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Quantidade não informada, Verifique!");
        alerta.show();
    }

    public static void quantidadeSuperiorAEstoque(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name)
                , pContext.getResources().getString(R.string.pedido_venda_alerta_sem_estoque));
        alerta.show();
    }

    public static void mensagemErro(Context pContext, String pMensagem, boolean pFechar) {
        Alerta alerta = new Alerta(pContext, "Erro", pMensagem);
        if (!pFechar)
            alerta.show();
        else
            alerta.showError();
    }

    public static void mensagemErro(Context pContext, String title, String pMensagem, boolean pFechar) {
        Alerta alerta = new Alerta(pContext, title, pMensagem);
        if (!pFechar)
            alerta.show((dialog, which) -> {
            });
        else {
            alerta.showError();
        }
    }

    public static void colaboradorSemImeiVinculado(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), pContext.getResources().getString(R.string.aparelho_sem_vinculo));
        alerta.showError();
    }

    public static void produtoAdicionado(Context pContext) {
        Utilidades.retornaMensagem(pContext, "Produto adicionado", false);
    }

    public static void clienteNaoEncontrado(Context pContext, boolean pFechar) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente não encontrado");
        if (pFechar)
            alerta.showError();
        else
            alerta.show();
    }

    public static void clienteNaoEncontradoFinalizarAtend(final Context pContext, final Visita pVisita) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente não encontrado, O Atendimento será finalizado!");
        alerta.show((dialog, which) -> {
            try {
                Utilidades.finalizaVisita(pContext, true, null, null, pVisita, null, 5);
            } catch (Exception ex) {
                Mensagens.mensagemErro(pContext, ex.getMessage(), false);
            }
        });
    }

    public static void formaPagamentoNaoPermitida(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Forma de pagamento não permitida para este cliente, Verifique");
        alerta.show();
    }

    public static void valorUltrapassaLimiteCliente(Context pContext, double pValorDisponivel) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name)
                ,
                "Valor da venda ultrapassa o valor do limite de crédito do cliente. O valor máximo disponível é de R$" +
                        " " + Util_IO.formataValor(pValorDisponivel));
        alerta.show();
    }

    public static void vendaNaoIniciada(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Venda não iniciada.");
        alerta.showError();
    }

    public static void vendaNaoEncontrada(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Venda não encontrada.");
        alerta.showError();
    }

    public static void produtoNaoEncontrado(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Produto não encontrado.");
        alerta.showError();
    }

    public static void nenhumItemIncluido(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Nenhum item foi incluído, Verifique!");
        alerta.show();
    }

    public static void produtoComValorZerado(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Produto com valor zerado, Entrar em contato com o setor de cadastro!");
        alerta.show();
    }

    public static void erroDataInvalida(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "A Data do aparelho está incorreta favor verificar!!");
        alerta.show();
    }

    public static void semEstoque(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name),
                pContext.getResources().getString(R.string.pedido_venda_alerta_sem_estoque));
        alerta.show();
    }

    public static void promocaoAteTantasUnds(Context pContext, int pQtd) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name)
                , "A promoção é somente até " + pQtd + " unidade(s).");
        alerta.show();
    }

    public static void bonificadoAteTantasUnds(Context pContext, String produtoNome, String valorDif, int pQtd, int qtdAdicionado) {
        String mensagemAdicionado = qtdAdicionado == 1 ?
                "\nFoi adicionado " + qtdAdicionado + " unidade." :
                "\nForam adicionados " + qtdAdicionado + " unidades.";

        String valorMensagem = valorDif.equals("") ? "" : " com o valor " + valorDif;

        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name)
                , "O máximo de itens com preço diferenciado para o produto " + produtoNome
                + valorMensagem + " é de " + pQtd + " unidade(s)." + mensagemAdicionado);
        alerta.show();
    }

    public static void ordemServicoNaoEncontrada(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Ordem de serviço não encontrada.");
        alerta.showError();
    }

    public static void visitaNaoIniciada(final Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Nenhuma visita iniciada.");
        alerta.show((dialog, which) -> {
            try {
                Utilidades.openRota(pContext, true);
            } catch (Exception ex) {
                mensagemErro(pContext, ex.getMessage(), false);
            }
        });
    }

    public static void iccidJaVendido(Context pContext, CodBarra pCodBarra) {
        String mensagem = "ICCID já vendido";
        if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraInicial()))
            mensagem += " " + pCodBarra.getCodBarraInicial();
        if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraFinal()))
            mensagem += " " + pCodBarra.getCodBarraFinal();
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), mensagem);
        alerta.show();
    }

    public static void dataDeVencimentoInvalida(Context pContext) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Data de vencimento inválida!");
        alerta.show();
    }

    public static void codigoBarraNaoVinculadoParaEsteProduto(Context pContext, String pCodBarra, String pProduto, DialogInterface.OnClickListener pDialog) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name)
                , "Código: " + pCodBarra + " não está vinculado para o produto " + pProduto + ", Entrar em contato com o setor de logística!");
        if (pDialog != null)
            alerta.show(pDialog);
        else
            alerta.show();
    }

    public static void codigoBarraNaoInformado(Context pContext, DialogInterface.OnClickListener pDialog) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name)
                , "Código de barra não informado, Verifique");
        if (pDialog != null)
            alerta.show(pDialog);
        else
            alerta.show();
    }

    public static void faltaEscanearCodBarra(Context pContext, int pFalta) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name),
                "Está faltando escanear " + pFalta + " " + ((pFalta == 1) ? "código" : "codigos") + " de barra, Verifique!");
        alerta.show();
    }

    public static void dataMenor(Context context) {
        Alerta alerta = new Alerta(context, context.getString(R.string.app_name), "A data selecionada é menor que a data inicial.");
        alerta.show();
    }

    public static void dataMaior(Context context) {
        Alerta alerta = new Alerta(context, context.getString(R.string.app_name), "A data selecionada é maior que a data final.");
        alerta.show();
    }

    public static void internet(Context context) {
        Alerta alerta = new Alerta(context, context.getString(R.string.app_name), "Verifique sua conexão com a internet.");
        alerta.show();
    }
}
