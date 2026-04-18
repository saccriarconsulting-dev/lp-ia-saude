package com.axys.redeflexmobile.shared.models;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.util.CodigoBarra;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by joao.viana on 18/07/2016.
 */
public class CodBarra {

    private String codBarraInicial;
    private String codBarraFinal;
    private Boolean individual;
    private int grupoProduto;
    private String idProduto;
    private int idPistolagem;

    private String nomeProduto;
    private boolean isViewing = false;
    private String idVendaItem;
    private int quantidadeTemporaria;
    private int quantidadeFormacaoCombo;
    private int idVenda;
    private Date dataVenda;
    private String idProdutoObjeto;

    private String idConsignadoItem;
    private int idConsignado;
    private Date dataConsignado;

    private String AuditadoCons;

    public int getIdPistolagem() {
        return idPistolagem;
    }

    public void setIdPistolagem(int idPistolagem) {
        this.idPistolagem = idPistolagem;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getGrupoProduto() {
        return grupoProduto;
    }

    public void setGrupoProduto(int grupoProduto) {
        this.grupoProduto = grupoProduto;
    }

    public String getCodBarraInicial() {
        return codBarraInicial;
    }

    public void setCodBarraInicial(String codBarraInicial) {
        this.codBarraInicial = codBarraInicial;
    }

    public String getCodBarraFinal() {
        return codBarraFinal;
    }

    public void setCodBarraFinal(String codBarraFinal) {
        this.codBarraFinal = codBarraFinal;
    }

    public Boolean getIndividual() {
        return individual;
    }

    public void setIndividual(Boolean individual) {
        this.individual = individual;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public String retornaQuantidade(UsoCodBarra usoCodBarra) {
        // Problema comeca a partir daqui
        try {
            if (individual)
                return "1";
            else {
                if (grupoProduto == 100 || grupoProduto == 229 || grupoProduto == 235) {
                    BigInteger cinicial = CodigoBarra.retornaICCID(codBarraInicial, grupoProduto);
                    BigInteger cfinal = CodigoBarra.retornaICCID(codBarraFinal, grupoProduto);

                    BigInteger retorno = cinicial.subtract(cfinal);

                    if (retorno.compareTo(BigInteger.ZERO) < 0)
                        retorno = retorno.negate();

                    retorno = retorno.add(BigInteger.ONE);
                    return String.valueOf(retorno);
                } else {
                    BigInteger cinicial = new BigInteger(codBarraInicial);
                    BigInteger cfinal = new BigInteger(codBarraFinal);

                    if (UsoCodBarra.AUDITAGEM_CLIENTE.equals(usoCodBarra)) {
                        cinicial = new BigInteger(String.valueOf(CodigoBarra.retornaICCID(codBarraInicial, grupoProduto)));
                        cfinal = new BigInteger(String.valueOf(CodigoBarra.retornaICCID(codBarraFinal, grupoProduto)));
                    }

                    Long retorno = cfinal.subtract(cinicial).longValue();
                    if (retorno < 0)
                        retorno = retorno * -1;

                    retorno++;
                    return String.valueOf(retorno);
                }
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return "1";
        }
    }

    public String retornaQuantidade(UsoCodBarra usoCodBarra, String idProduto, Context context) {
        // Problema comeca a partir daqui
        try {
            if (individual)
                return "1";
            else {
                int contador = 0;
                if (grupoProduto == 100 || grupoProduto == 229 || grupoProduto == 235) {
                    BigInteger cinicial = CodigoBarra.retornaICCID(codBarraInicial, grupoProduto);
                    BigInteger cfinal = CodigoBarra.retornaICCID(codBarraFinal, grupoProduto);

                    BigInteger retorno = new BigInteger("0");
                    BigInteger naoEncontrado = new BigInteger("0");
                    DBIccid dbIccid = new DBIccid(context);

                    List<String> listaIccds = dbIccid.obterListaCodigoBarraPorIccid(idProduto);
                    for (BigInteger i = cinicial; i.compareTo(cfinal) < 0; i = i.add(BigInteger.ONE)) {
                        contador++;
                        if (verifica(listaIccds, i.toString())) {
                            retorno = retorno.add(BigInteger.ONE);
                        } else {
                            naoEncontrado = naoEncontrado.add(BigInteger.ONE);
                        }
                    }

                    if (retorno.compareTo(BigInteger.ZERO) < 0)
                        retorno = retorno.negate();

                    retorno = retorno.add(BigInteger.ONE);

                    return naoEncontrado.compareTo(BigInteger.ZERO) == 0 ? String.valueOf(retorno) : "0";
                } else {
                    BigInteger cinicial = new BigInteger(codBarraInicial);
                    BigInteger cfinal = new BigInteger(codBarraFinal);

                    if (UsoCodBarra.AUDITAGEM_CLIENTE.equals(usoCodBarra)) {
                        cinicial = new BigInteger(String.valueOf(CodigoBarra.retornaICCID(codBarraInicial, grupoProduto)));
                        cfinal = new BigInteger(String.valueOf(CodigoBarra.retornaICCID(codBarraFinal, grupoProduto)));
                    }

                    BigInteger retorno = new BigInteger("0");
                    DBIccid dbIccid = new DBIccid(context);
                    BigInteger naoEncontrado = new BigInteger("0");

                    List<String> listaIccds = dbIccid.obterListaCodigoBarraPorIccid(idProduto);
                    for (BigInteger i = cinicial; i.compareTo(cfinal) < 0; i = i.add(BigInteger.ONE)) {
                        contador++;
                        if (verifica(listaIccds, i.toString())) {
                            retorno = retorno.add(BigInteger.ONE);
                        } else {
                            naoEncontrado = naoEncontrado.add(BigInteger.ONE);
                        }
                    }

                    retorno = retorno.add(BigInteger.ONE);
                    return naoEncontrado.compareTo(BigInteger.ZERO) == 0 ? String.valueOf(retorno) : "0";
                }
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return "1";
        }
    }

    public long somarQuantidade(UsoCodBarra usoCodBarra) {
        // Problema comeca a partir daqui
        try {
            if (individual)
                return 1;
            else {
                if (grupoProduto == 100 || grupoProduto == 229 || grupoProduto == 235) {
                    BigInteger cinicial = CodigoBarra.retornaICCID(codBarraInicial, grupoProduto);
                    BigInteger cfinal = CodigoBarra.retornaICCID(codBarraFinal, grupoProduto);

                    BigInteger retorno = cinicial.subtract(cfinal);

                    if (retorno.compareTo(BigInteger.ZERO) < 0)
                        retorno = retorno.negate();

                    retorno = retorno.add(BigInteger.ONE);
                    return retorno.longValue();
                } else {
                    BigInteger cinicial = new BigInteger(codBarraInicial);
                    BigInteger cfinal = new BigInteger(codBarraFinal);

                    if (UsoCodBarra.AUDITAGEM_CLIENTE.equals(usoCodBarra)) {
                        cinicial = new BigInteger(String.valueOf(CodigoBarra.retornaICCID(codBarraInicial, grupoProduto)));
                        cfinal = new BigInteger(String.valueOf(CodigoBarra.retornaICCID(codBarraFinal, grupoProduto)));
                    }

                    long retorno = cfinal.subtract(cinicial).longValue();
                    if (retorno < 0)
                        retorno = retorno * -1;

                    retorno++;
                    return retorno;
                }
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return 1;
        }
    }

    private boolean verifica(List<String> codigoBarra, String codigo) {
        for (String item : codigoBarra) {
            if (item.contains(codigo)) {
                return true;
            }
        }

        return false;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public boolean isViewing() {
        return isViewing;
    }

    public void setViewing(boolean viewing) {
        isViewing = viewing;
    }

    public void changeViewing() {
        isViewing = !isViewing;
    }

    public String getIdVendaItem() {
        return idVendaItem;
    }

    public void setIdVendaItem(String idVendaItem) {
        this.idVendaItem = idVendaItem;
    }

    public int getQuantidadeTemporaria() {
        return quantidadeTemporaria;
    }

    public void setQuantidadeTemporaria(int quantidadeTemporaria) {
        this.quantidadeTemporaria = quantidadeTemporaria;
    }

    public int getQuantidadeFormacaoCombo() {
        return quantidadeFormacaoCombo;
    }

    public void setQuantidadeFormacaoCombo(int quantidadeFormacaoCombo) {
        this.quantidadeFormacaoCombo = quantidadeFormacaoCombo;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getIdProdutoObjeto() {
        return idProdutoObjeto;
    }

    public void setIdProdutoObjeto(String idProdutoObjeto) {
        this.idProdutoObjeto = idProdutoObjeto;
    }

    public String getIdConsignadoItem() {
        return idConsignadoItem;
    }

    public void setIdConsignadoItem(String idConsignadoItem) {
        this.idConsignadoItem = idConsignadoItem;
    }

    public int getIdConsignado() {
        return idConsignado;
    }

    public void setIdConsignado(int idConsignado) {
        this.idConsignado = idConsignado;
    }

    public Date getDataConsignado() {
        return dataConsignado;
    }

    public void setDataConsignado(Date dataConsignado) {
        this.dataConsignado = dataConsignado;
    }

    public String getAuditadoCons() {
        return AuditadoCons;
    }

    public void setAuditadoCons(String auditadoCons) {
        this.AuditadoCons = auditadoCons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodBarra codBarra = (CodBarra) o;

        if (grupoProduto != codBarra.grupoProduto) return false;
        if (idPistolagem != codBarra.idPistolagem) return false;
        if (codBarraInicial != null ? !codBarraInicial.equals(codBarra.codBarraInicial) : codBarra.codBarraInicial != null)
            return false;
        if (codBarraFinal != null ? !codBarraFinal.equals(codBarra.codBarraFinal) : codBarra.codBarraFinal != null)
            return false;
        if (individual != null ? !individual.equals(codBarra.individual) : codBarra.individual != null)
            return false;
        return idProduto != null ? idProduto.equals(codBarra.idProduto) : codBarra.idProduto == null;
    }

    @Override
    public int hashCode() {
        int result = codBarraInicial != null ? codBarraInicial.hashCode() : 0;
        result = 31 * result + (codBarraFinal != null ? codBarraFinal.hashCode() : 0);
        result = 31 * result + (individual != null ? individual.hashCode() : 0);
        result = 31 * result + grupoProduto;
        result = 31 * result + (idProduto != null ? idProduto.hashCode() : 0);
        result = 31 * result + idPistolagem;
        return result;
    }
}