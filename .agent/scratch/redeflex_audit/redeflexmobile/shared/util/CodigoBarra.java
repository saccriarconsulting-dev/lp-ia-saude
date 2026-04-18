package com.axys.redeflexmobile.shared.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.axys.redeflexmobile.shared.bd.BDConsignado;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBPistolagemTemp;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.models.Chip;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.RetCodBarra;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao.viana on 30/05/2017.
 */

public class CodigoBarra {
    public static final int PRODUTO_FISICO = 103;

    public static RetCodBarra validacao(String pCodigoBarra, Produto pProduto, int pQtdBipagem, CodBarra pCodBarra, ArrayList<CodBarra> pList, boolean bIndivual, boolean bVenda, Context pContext) {
        RetCodBarra retCodBarra = new RetCodBarra();
        DBVenda dbVenda = new DBVenda(pContext);
        ArrayList<CodBarra> listValidacao = new ArrayList<>();
        listValidacao.addAll(pList);

        if (!Util_IO.isNumber(pCodigoBarra)) {
            String mensagem = "Codigo de Barras informado contém caractere(s) inválido(s). " + pCodigoBarra;
            retCodBarra.setMensagem(mensagem);
            retCodBarra.setInclusaoOK(false);
            retCodBarra.setQuantidade(pQtdBipagem);
            return retCodBarra;
        }

        if (!bVenda)
            listValidacao.addAll(new DBPistolagemTemp(pContext).GetPistolagemTemp());

        if (bIndivual) {
            pCodBarra = new CodBarra();
            pCodBarra.setGrupoProduto(pProduto.getGrupo());
            pCodBarra.setIndividual(true);
            pCodBarra.setCodBarraInicial(pCodigoBarra);

            if (verificaICCIDDuplicado(pCodBarra, listValidacao)) {
                retCodBarra.setMensagem("Item já adicionado.");
                retCodBarra.setInclusaoOK(false);
                retCodBarra.setQuantidade(pQtdBipagem);
                return retCodBarra;
            }

            if (bVenda && dbVenda.iccidVendido(pCodBarra)) {
                String mensagem = "ICCID já vendido";
                if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraInicial()))
                    mensagem += " " + pCodBarra.getCodBarraInicial();
                if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraFinal()))
                    mensagem += " " + pCodBarra.getCodBarraFinal();
                retCodBarra.setMensagem(mensagem);
                retCodBarra.setInclusaoOK(false);
                retCodBarra.setQuantidade(pQtdBipagem);
                return retCodBarra;
            }

            retCodBarra.setCodBarra(pCodBarra);
            retCodBarra.setInclusaoOK(true);
            retCodBarra.setQuantidade(0);
            return retCodBarra;
        } else {
            if (pQtdBipagem == 0) {
                pCodBarra = new CodBarra();
                pCodBarra.setIndividual(false);
                pCodBarra.setGrupoProduto(pProduto.getGrupo());
                pCodBarra.setCodBarraInicial(pCodigoBarra);

                retCodBarra.setCodBarra(pCodBarra);
                retCodBarra.setQuantidade(1);
                retCodBarra.setInclusaoOK(true);
                return retCodBarra;
            } else {
                if (pCodBarra == null) {
                    pCodBarra = new CodBarra();
                    pCodBarra.setIndividual(false);
                    pCodBarra.setGrupoProduto(pProduto.getGrupo());
                    pCodBarra.setCodBarraInicial(pCodigoBarra);
                } else {
                    if (pCodBarra.getCodBarraInicial().equals(pCodigoBarra)) {
                        retCodBarra.setMensagem("Sequêncial já informado!");
                        retCodBarra.setInclusaoOK(false);
                        return retCodBarra;
                    } else
                        pCodBarra.setCodBarraFinal(pCodigoBarra);
                }

                try {
                    if (Integer.parseInt(pCodBarra.retornaQuantidade(UsoCodBarra.GERAL, pProduto.getId(), pContext)) < 2) {
                        retCodBarra.setMensagem("Verifique o ICCID não é sequêncial!");
                        retCodBarra.setInclusaoOK(false);
                        retCodBarra.setQuantidade(0);
                        return retCodBarra;
                    }

                    CodBarra codValidacao = pCodBarra;

                    if (retornaICCID(codValidacao.getCodBarraInicial(), codValidacao.getGrupoProduto()).compareTo(retornaICCID(codValidacao.getCodBarraFinal(), codValidacao.getGrupoProduto())) > -1) {
                        retCodBarra.setMensagem("Verifique o ICCID não é sequêncial!");
                        retCodBarra.setInclusaoOK(false);
                        retCodBarra.setQuantidade(0);
                        return retCodBarra;
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    retCodBarra.setMensagem("Verifique o ICCID não é sequêncial!");
                    retCodBarra.setInclusaoOK(false);
                    retCodBarra.setQuantidade(0);
                    return retCodBarra;
                }

                if (verificaICCIDDuplicado(pCodBarra, listValidacao)) {
                    retCodBarra.setMensagem("Item já adicionado.");
                    retCodBarra.setInclusaoOK(false);
                    retCodBarra.setQuantidade(0);
                    return retCodBarra;
                }

                if (bVenda && dbVenda.iccidVendido(pCodBarra)) {
                    String mensagem = "ICCID já vendido";
                    if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraInicial()))
                        mensagem += " " + pCodBarra.getCodBarraInicial();
                    if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraFinal()))
                        mensagem += " " + pCodBarra.getCodBarraFinal();
                    retCodBarra.setMensagem(mensagem);
                    retCodBarra.setInclusaoOK(false);
                    retCodBarra.setQuantidade(pQtdBipagem);
                    return retCodBarra;
                }

                retCodBarra.setCodBarra(pCodBarra);
                retCodBarra.setInclusaoOK(true);
                retCodBarra.setQuantidade(0);
                return retCodBarra;
            }
        }
    }

    public static RetCodBarra validacaoConsignacao(String pCodigoBarra, Produto pProduto, int pQtdBipagem, CodBarra pCodBarra, ArrayList<CodBarra> pList, boolean bIndivual, boolean bConsignado, Context pContext) {
        RetCodBarra retCodBarra = new RetCodBarra();
        BDConsignado dbConsignado = new BDConsignado(pContext);
        ArrayList<CodBarra> listValidacao = new ArrayList<>();
        listValidacao.addAll(pList);

        if (!Util_IO.isNumber(pCodigoBarra)) {
            String mensagem = "Codigo de Barras informado contém caractere(s) inválido(s). " + pCodigoBarra;
            retCodBarra.setMensagem(mensagem);
            retCodBarra.setInclusaoOK(false);
            retCodBarra.setQuantidade(pQtdBipagem);
            return retCodBarra;
        }

        if (!bConsignado)
            listValidacao.addAll(new DBPistolagemTemp(pContext).GetPistolagemTemp());

        if (bIndivual) {
            pCodBarra = new CodBarra();
            pCodBarra.setGrupoProduto(pProduto.getGrupo());
            pCodBarra.setIndividual(true);
            pCodBarra.setCodBarraInicial(pCodigoBarra);

            if (verificaICCIDDuplicado(pCodBarra, listValidacao)) {
                retCodBarra.setMensagem("Item já adicionado.");
                retCodBarra.setInclusaoOK(false);
                retCodBarra.setQuantidade(pQtdBipagem);
                return retCodBarra;
            }

            if (bConsignado && dbConsignado.iccidVendido(pCodBarra)) {
                String mensagem = "ICCID já vendido";
                if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraInicial()))
                    mensagem += " " + pCodBarra.getCodBarraInicial();
                if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraFinal()))
                    mensagem += " " + pCodBarra.getCodBarraFinal();
                retCodBarra.setMensagem(mensagem);
                retCodBarra.setInclusaoOK(false);
                retCodBarra.setQuantidade(pQtdBipagem);
                return retCodBarra;
            }

            retCodBarra.setCodBarra(pCodBarra);
            retCodBarra.setInclusaoOK(true);
            retCodBarra.setQuantidade(0);
            return retCodBarra;
        }
        else {
            if (pQtdBipagem == 0) {
                pCodBarra = new CodBarra();
                pCodBarra.setIndividual(false);
                pCodBarra.setGrupoProduto(pProduto.getGrupo());
                pCodBarra.setCodBarraInicial(pCodigoBarra);

                retCodBarra.setCodBarra(pCodBarra);
                retCodBarra.setQuantidade(1);
                retCodBarra.setInclusaoOK(true);
                return retCodBarra;
            }
            else {
                if (pCodBarra == null) {
                    pCodBarra = new CodBarra();
                    pCodBarra.setIndividual(false);
                    pCodBarra.setGrupoProduto(pProduto.getGrupo());
                    pCodBarra.setCodBarraInicial(pCodigoBarra);
                } else {
                    if (pCodBarra.getCodBarraInicial().equals(pCodigoBarra)) {
                        retCodBarra.setMensagem("Sequêncial já informado!");
                        retCodBarra.setInclusaoOK(false);
                        return retCodBarra;
                    } else
                        pCodBarra.setCodBarraFinal(pCodigoBarra);
                }

                try {
                    if (Integer.parseInt(pCodBarra.retornaQuantidade(UsoCodBarra.GERAL, pProduto.getId(), pContext)) < 2) {
                        retCodBarra.setMensagem("Verifique o ICCID não é sequêncial!");
                        retCodBarra.setInclusaoOK(false);
                        retCodBarra.setQuantidade(0);
                        return retCodBarra;
                    }

                    CodBarra codValidacao = pCodBarra;

                    if (retornaICCID(codValidacao.getCodBarraInicial(), codValidacao.getGrupoProduto()).compareTo(retornaICCID(codValidacao.getCodBarraFinal(), codValidacao.getGrupoProduto())) > -1) {
                        retCodBarra.setMensagem("Verifique o ICCID não é sequêncial!");
                        retCodBarra.setInclusaoOK(false);
                        retCodBarra.setQuantidade(0);
                        return retCodBarra;
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    retCodBarra.setMensagem("Verifique o ICCID não é sequêncial!");
                    retCodBarra.setInclusaoOK(false);
                    retCodBarra.setQuantidade(0);
                    return retCodBarra;
                }

                if (verificaICCIDDuplicado(pCodBarra, listValidacao)) {
                    retCodBarra.setMensagem("Item já adicionado.");
                    retCodBarra.setInclusaoOK(false);
                    retCodBarra.setQuantidade(0);
                    return retCodBarra;
                }

                if (bConsignado && dbConsignado.iccidVendido(pCodBarra)) {
                    String mensagem = "ICCID já vendido";
                    if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraInicial()))
                        mensagem += " " + pCodBarra.getCodBarraInicial();
                    if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraFinal()))
                        mensagem += " " + pCodBarra.getCodBarraFinal();
                    retCodBarra.setMensagem(mensagem);
                    retCodBarra.setInclusaoOK(false);
                    retCodBarra.setQuantidade(pQtdBipagem);
                    return retCodBarra;
                }

                retCodBarra.setCodBarra(pCodBarra);
                retCodBarra.setInclusaoOK(true);
                retCodBarra.setQuantidade(0);
                return retCodBarra;
            }
        }
    }

    public static int quantidadeBipada(ArrayList<CodBarra> pLista, UsoCodBarra usoCodBarra) {
        try {
            int quantidadebipaga = 0;
            for (CodBarra codBarra : pLista) {
                quantidadebipaga += Integer.valueOf(codBarra.retornaQuantidade(usoCodBarra));
            }
            return quantidadebipaga;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static int quantidadeBipada(ArrayList<CodBarra> pLista, UsoCodBarra usoCodBarra, Context context) {
        try {
            int quantidadebipaga = 0;
            for (CodBarra codBarra : pLista) {
                quantidadebipaga += Integer.valueOf(codBarra.retornaQuantidade(usoCodBarra, codBarra.getIdProduto(), context));
            }
            return quantidadebipaga;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static boolean verificaICCIDDuplicado(CodBarra pCodBarra, ArrayList<CodBarra> pLista) {
        boolean verifica = false;
        try {
            ArrayList<Iccid> lista = listaICCID(pCodBarra);
            for (Iccid pIccid : lista) {
                for (CodBarra codBarra : pLista) {
                    // Teve que fazer esta validação porque quando é Codigo Barras Unitario o CodigoBarras Final é igual a 0;
                    if (codBarra.getIndividual()) {
                        if (new BigInteger(pIccid.getCodigoSemVerificador()).longValue() == retornaICCID(codBarra.getCodBarraInicial(), codBarra.getGrupoProduto()).longValue()) {
                            verifica = true;
                            break;
                        }
                    } else
                    {
                        if (new BigInteger(pIccid.getCodigoSemVerificador()).longValue() >= retornaICCID(codBarra.getCodBarraInicial(), codBarra.getGrupoProduto()).longValue() &&
                            new BigInteger(pIccid.getCodigoSemVerificador()).longValue() <= retornaICCID(codBarra.getCodBarraFinal(), codBarra.getGrupoProduto()).longValue()) {
                        verifica = true;
                        break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            verifica = false;
        }
        return verifica;
    }

    public static boolean verificaICCIDConsignado(CodBarra pCodBarra, ArrayList<CodBarra> pLista) {
        boolean verifica = false;
        try {
            ArrayList<Iccid> lista = listaICCID(pCodBarra);
            for (Iccid pIccid : lista) {
                for (CodBarra codBarra : pLista) {
                    // Teve que fazer esta validação porque quando é Codigo Barras Unitario o CodigoBarras Final é igual a 0;
                    if (codBarra.getIndividual()) {
                        if (new BigInteger(pIccid.getCodigoSemVerificador()).longValue() == retornaICCID(codBarra.getCodBarraInicial(), codBarra.getGrupoProduto()).longValue()) {
                            verifica = true;
                            break;
                        }
                    } else
                    {
                        if (new BigInteger(pIccid.getCodigoSemVerificador()).longValue() >= retornaICCID(codBarra.getCodBarraInicial(), codBarra.getGrupoProduto()).longValue() &&
                                new BigInteger(pIccid.getCodigoSemVerificador()).longValue() <= retornaICCID(codBarra.getCodBarraFinal(), codBarra.getGrupoProduto()).longValue()) {
                            verifica = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            verifica = false;
        }
        return verifica;
    }

    public static BigInteger retornaICCID(String pCodigo, int idGrupo) {
        try {
            if (idGrupo == PRODUTO_FISICO) {
                return new BigInteger(pCodigo);
            }

            return new BigInteger(retornaICCIDSemDigito(pCodigo));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return new BigInteger("0");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BigInteger("0");
        }
    }

    public static String retornaICCIDSemDigito(String pCodigo) {
        try {
            if (pCodigo.trim().length() > 18) {
                return pCodigo.substring(0, pCodigo.trim().length() - 1);
            } else {
                return pCodigo;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return pCodigo;
        }
    }

    public static String retornaDigitoVerificadorICCID(String pCodigo) {
        try {
            if (pCodigo.trim().length() == 20) {
                return pCodigo.substring(pCodigo.trim().length() - 1, pCodigo.trim().length());
            } else {
                throw new IllegalArgumentException("Tamanho do código de barra incorreto. Verifique!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return pCodigo;
        }
    }

    public static ProdutoCombo retornaCombo(int pQtdCombo, List<CodBarra> pListaCodBarra, UsoCodBarra usoCodBarra) {
        ProdutoCombo produtoCombo = new ProdutoCombo();
        int qtdTotal = 0, qtdFalta, qtdbipagem = 0;
        if (pListaCodBarra != null && pListaCodBarra.size() > 0) {
            for (CodBarra codBarra : pListaCodBarra) {
                qtdbipagem += Integer.parseInt(codBarra.retornaQuantidade(usoCodBarra));
                if (codBarra.getIndividual()) {
                    if (qtdbipagem == pQtdCombo) {
                        qtdTotal++;
                        qtdbipagem = 0;
                    }
                } else {
                    if (qtdbipagem == pQtdCombo) {
                        qtdTotal++;
                        qtdbipagem = 0;
                    } else if (qtdbipagem > pQtdCombo) {
                        while (qtdbipagem > pQtdCombo) {
                            qtdTotal++;
                            qtdbipagem = qtdbipagem - pQtdCombo;
                        }
                    }
                }
            }

            if (qtdbipagem == pQtdCombo) {
                qtdTotal++;
                qtdbipagem = 0;
            }

            if (qtdbipagem == 0)
                qtdFalta = 0;
            else
                qtdFalta = pQtdCombo - qtdbipagem;
        } else
            qtdFalta = pQtdCombo;

        if (qtdFalta < 0)
            qtdFalta = qtdFalta * -1;

        produtoCombo.setQtdTotal(qtdTotal);
        produtoCombo.setQtdFalta(qtdFalta);

        return produtoCombo;
    }

    public static int ultimoDigito(String pIccid) {
        ArrayList<Chip> lista = new ArrayList<>();
        Chip item;
        int retorno;
        char[] digito = pIccid.toCharArray();

        try {
            int pos = 0;
            for (char texto : digito) {
                try {
                    retorno = Integer.valueOf(String.valueOf(texto));
                    item = new Chip();
                    item.setPosition(pos + 1);
                    item.setThisChar(String.valueOf(retorno));
                    item.setDoubled(null);
                    item.setSummed(null);
                    lista.add(item);
                    pos++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    pos++;
                }
            }

            boolean positivo = false;
            if ((lista.size() % 2) == 0)
                positivo = true;

            int idouble;
            for (int i = 0; i < lista.size(); i++) {
                if (positivo) {
                    if ((lista.get(i).getPosition() % 2) == 0) {
                        idouble = Integer.valueOf(lista.get(i).getThisChar()) * 2;
                        lista.get(i).setDoubled(idouble);
                    } else
                        continue;
                } else {
                    if ((lista.get(i).getPosition() % 2) == 0)
                        continue;
                    else {
                        idouble = Integer.valueOf(lista.get(i).getThisChar()) * 2;
                        lista.get(i).setDoubled(idouble);
                    }
                }
            }

            for (int i = 0; i < lista.size(); i++) {
                Integer doubled = lista.get(i).getDoubled();
                int resultado;
                if (doubled == null) {
                    resultado = Integer.valueOf(lista.get(i).getThisChar());
                    lista.get(i).setSummed(resultado);
                } else {
                    if (doubled <= 9)
                        lista.get(i).setSummed(doubled);
                    else {
                        resultado = (doubled / 10) + (doubled - 10);
                        lista.get(i).setSummed(resultado);
                    }
                }
            }

            retorno = 0;

            for (Chip chip : lista)
                retorno += chip.getSummed();

            int resto = retorno % 10;

            resto = 10 - resto;

            if (resto == 10)
                resto = 0;

            return resto;
        } catch (Exception ex) {
            return -1;
        }
    }

    public static ArrayList<Iccid> listaICCID(CodBarra pCodBarra) throws Exception {
        ArrayList<Iccid> listRet = new ArrayList<>();
        Iccid codigo;
        String imei = "";
        if (!Util_IO.isNullOrEmpty(pCodBarra.getCodBarraFinal())) {
            if (pCodBarra.getGrupoProduto() == 100 || pCodBarra.getGrupoProduto() == 229 || pCodBarra.getGrupoProduto() == 235) {
                BigInteger cInicial = retornaICCID(pCodBarra.getCodBarraInicial(), pCodBarra.getGrupoProduto());
                BigInteger cFinal = retornaICCID(pCodBarra.getCodBarraFinal(), pCodBarra.getGrupoProduto());
                BigInteger cResultado = cFinal.subtract(cInicial);

                if (cResultado.compareTo(BigInteger.ZERO) < 0) {
                    cInicial = retornaICCID(pCodBarra.getCodBarraFinal(), pCodBarra.getGrupoProduto());
                    cFinal = retornaICCID(pCodBarra.getCodBarraInicial(), pCodBarra.getGrupoProduto());
                    cResultado = cFinal.subtract(cInicial);
                }
                cResultado = cResultado.add(BigInteger.ONE);

                BigInteger iContador = cResultado;
                BigInteger cImei = cInicial;
                while (iContador.compareTo(BigInteger.ZERO) > 0) {
                    if (!iContador.equals(cResultado))
                        cImei = cImei.add(BigInteger.ONE);

                    imei = String.valueOf(cImei);
                    codigo = new Iccid();
                    codigo.setCodigoSemVerificador(imei);
                    codigo.setCodigo(imei + String.valueOf(ultimoDigito(imei)));
                    listRet.add(codigo);

                    iContador = iContador.subtract(BigInteger.ONE);
                    if (iContador.compareTo(BigInteger.ZERO) == 0 && !imei.equalsIgnoreCase(String.valueOf(cFinal))) {
                        codigo = new Iccid();
                        imei = String.valueOf(cFinal);
                        codigo.setCodigoSemVerificador(imei);
                        codigo.setCodigo(imei + String.valueOf(ultimoDigito(imei)));
                        listRet.add(codigo);
                    }
                }
            } else {
                BigInteger cInicial = new BigInteger(pCodBarra.getCodBarraInicial());
                BigInteger cFinal = new BigInteger(pCodBarra.getCodBarraFinal());

                Long cResultado = cFinal.subtract(cInicial).longValue();
                if (cResultado < 0) {
                    cInicial = new BigInteger(pCodBarra.getCodBarraFinal());
                    cFinal = new BigInteger(pCodBarra.getCodBarraInicial());
                    cResultado = cFinal.subtract(cInicial).longValue();
                }

                Long iContador = cResultado;
                BigInteger cImei = cInicial;
                while (iContador > 0) {
                    if (!iContador.equals(cResultado))
                        cImei = cImei.add(new BigInteger("1"));
                    imei = String.valueOf(cImei);

                    codigo = new Iccid();
                    codigo.setCodigoSemVerificador(imei);
                    codigo.setCodigo(imei);
                    listRet.add(codigo);

                    iContador--;
                    if (iContador == 0 && !imei.equalsIgnoreCase(String.valueOf(cFinal))) {
                        imei = String.valueOf(cFinal);
                        codigo = new Iccid();
                        codigo.setCodigoSemVerificador(imei);
                        codigo.setCodigo(imei);
                        listRet.add(codigo);
                    }
                }
            }
        } else {
            codigo = new Iccid();
            imei = pCodBarra.getCodBarraInicial();
            codigo.setCodigo(imei);
            if (pCodBarra.getGrupoProduto() == 100 || pCodBarra.getGrupoProduto() == 229 || pCodBarra.getGrupoProduto() == 235)
                codigo.setCodigoSemVerificador(retornaICCIDSemDigito(imei));
            else
                codigo.setCodigoSemVerificador(imei);
            listRet.add(codigo);
        }

        return listRet;
    }

    public static int retornaQtCombo(String pIdProduto, Context pContext) {
        int qtdCombo = 0;
        ArrayList<EstruturaProd> itensEstruturaProd = new DBEstoque(pContext).getEstruturaByItemPai(pIdProduto);

        for (EstruturaProd estruturaProd : itensEstruturaProd)
            qtdCombo += estruturaProd.getQtd();

        return qtdCombo;
    }

    public static boolean validaQuantidadeRange(CodBarra linha) {
        int grupoProduto = linha.getGrupoProduto();

        BigInteger initialCodebar = grupoProduto == 103
                ? new BigInteger(linha.getCodBarraInicial())
                : new BigInteger(CodigoBarra.retornaICCIDSemDigito(linha.getCodBarraInicial()));

        BigInteger finalCodebar = grupoProduto == 103
                ? new BigInteger(linha.getCodBarraFinal())
                : new BigInteger(CodigoBarra.retornaICCIDSemDigito(linha.getCodBarraFinal()));

        return finalCodebar.subtract(initialCodebar).add(new BigInteger("1"))
                .compareTo(new BigInteger("1000")) <= 0;
    }
}