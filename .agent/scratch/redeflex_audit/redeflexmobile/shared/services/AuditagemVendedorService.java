package com.axys.redeflexmobile.shared.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.enums.EnumStatusAuditagem;
import com.axys.redeflexmobile.shared.models.AuditagemEstoque;
import com.axys.redeflexmobile.shared.models.AuditagemEstoqueResponse;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.RxBus;
import com.axys.redeflexmobile.shared.util.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;

/**
 * @author Rogério Massa on 2019-08-07.
 */

public class AuditagemVendedorService extends Service {

    private final int TEMPO_TIMER = 1000;

    private DBEstoque dbEstoque;
    private DBVisita dbVisita;
    private DBVenda dbVenda;
    private DBIccid dbIccid;
    private Colaborador colaborador;
    private Venda venda;
    private List<AuditagemEstoque> listItens;
    private Handler handler;
    private Runnable runnable;
    private EnumStatusAuditagem statusAuditagem;
    private Intent serviceIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        dbEstoque = new DBEstoque(this);
        dbVisita = new DBVisita(this);
        dbVenda = new DBVenda(this);
        dbIccid = new DBIccid(this);
        colaborador = new DBColaborador(this).get();
    }

    @Override
    public void onDestroy() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        listItens = Stream.of(dbEstoque.getAuditagemEstoqueHojeProcessando())
                .sortBy(value -> -value.getQtdeInformada())
                .toList();

        if (!listItens.isEmpty()) {
            processarAuditagem(intent);
        }
        return START_NOT_STICKY;
    }

    private void processarAuditagem(Intent intent) {
        serviceIntent = intent;
        statusAuditagem = EnumStatusAuditagem.ABERTA;

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                RxBus.getInstance().send(new AuditagemEstoqueResponse(statusAuditagem,
                        serviceIntent, handler, this));
                handler.postDelayed(this, TEMPO_TIMER);
            }
        };

        new Thread(() -> {
            runnable.run();

            if (getValorDivergencia() > 0) {
                gerarPedidoDeVendaComDivergencia();
            } else {
                gerarPedidoDeVendaSemDivergencia();
            }
        }).start();
    }

    private List<AuditagemEstoque> obterListaDeItemsAuditados() {
        return Stream.of(listItens)
                .filter(value -> StringUtils.isNotEmpty(value.getIdProduto()))
                .toList();
    }

    private List<CodBarra> obterIccidParaVenda(AuditagemEstoque auditagemEstoque) {
        Produto produto = gerarObjetoProduto(auditagemEstoque);
        if (Produto.NAO_DEVE_AUDITAR_ESTOQUE.equalsIgnoreCase(produto.getBipagemAuditoria())) {
            return new ArrayList<>();
        }

        DBIccid dbIccid = new DBIccid(this);
        List<CodBarra> codBarras = dbIccid.obterListaCodigoBarraPorProduto(auditagemEstoque.getIdProduto(), produto.getGrupo());
        List<CodBarra> tempCodbarra = new ArrayList<>();

        Stream.of(auditagemEstoque.getCodigosList()).forEach(codigoBarra -> {
            int tamanho = codBarras.size() - 1;
            for (int i = tamanho; i >= 0; i--) {
                if (codigoBarra.getCodBarraFinal() != null) {
                    BigInteger codigoInicial = new BigInteger(codigoBarra.getCodBarraInicial());
                    BigInteger codigoFinal = new BigInteger(codigoBarra.getCodBarraFinal());
                    for (BigInteger x = codigoInicial; x.compareTo(codigoFinal) <= 0; x = x.add(BigInteger.ONE)) {
                        if (x.toString().equals(codBarras.get(i).getCodBarraInicial()) || x.toString().equals(codBarras.get(i).getCodBarraFinal())) {
                            tempCodbarra.add(codBarras.get(i));
                        }
                    }
                }
                if (codigoBarra.getCodBarraFinal() == null && codigoBarra.getCodBarraInicial().equals(codBarras.get(i).getCodBarraInicial())) {
                    codBarras.remove(i);
                    break;
                }
            }
        });

        codBarras.removeAll(tempCodbarra);
        return codBarras;
    }

    public double getValorDivergencia() {
        double total = 0;
        for (AuditagemEstoque item : listItens) {
            total += item.getValorUnitario() * (item.getQtdeReal() - item.getQtdeInformada());
        }
        return total;
    }

    private Visita gerarObjetoVisita(Colaborador colaborador) {
        GPSTracker gpsTracker = new GPSTracker(this);
        Visita visita = new Visita();
        visita.setIdVendedor(String.valueOf(colaborador.getId()));
        visita.setIdMotivo(0);
        visita.setDataInicio(new Date());
        visita.setIdCliente(colaborador.getIdCliente());
        visita.setVersaoApp(BuildConfig.VERSION_NAME);
        visita.setLatitude(gpsTracker.getLatitude());
        visita.setLongitude(gpsTracker.getLongitude());
        visita.setPrecisao(gpsTracker.getPrecisao());
        visita.setDistancia(0);
        return visita;
    }

    private Venda gerarObjetoVenda(Colaborador colaborador, long idVisita) {
        Venda venda = new Venda();
        venda.setIdCliente(colaborador.getIdCliente());
        venda.setIdFormaPagamento(1);
        venda.setValorTotal(getValorDivergencia());
        venda.setData(new Date());
        venda.setIdVisita(Integer.parseInt(String.valueOf(idVisita)));
        venda.setIdentificadorAutidagem(UUID.randomUUID().toString());
        return venda;
    }

    private Produto gerarObjetoProduto(AuditagemEstoque item) {
        Produto produto = new DBEstoque(this).getProdutoById(item.getIdProduto());
        produto.setId(item.getIdProduto());
        produto.setQtde(item.getQtdeReal() - item.getQtdeInformada());
        produto.setPrecovenda(item.getValorUnitario());
        return produto;
    }

    private void atualizaIdentificadorAuditagemVenda(Venda venda) {
        Stream.ofNullable(listItens)
                .forEach(auditagemEstoque -> dbEstoque.setAuditagemEstoqueIdentificadorVenda(
                        auditagemEstoque, venda.getIdentificadorAutidagem()));
    }

    private Venda obterVenda() {
        if (venda != null) {
            return venda;
        }

        try {
            Visita visita = gerarObjetoVisita(colaborador);
            if (visita == null) {
                throw new NullPointerException("Visita não pode ser iniciada, verificar GPS");
            }

            venda = gerarObjetoVenda(colaborador, dbVisita.addVisita(visita));
            long idVenda = dbVenda.criarVendaAuditagemEstoque(venda);
            venda.setId((int) idVenda);
            atualizaIdentificadorAuditagemVenda(venda);
            return venda;

        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    private List<CodBarra> pegarIccdsNaoBipados(AuditagemEstoque item, Produto produto) {
        List<CodBarra> tempCodbarra = new ArrayList<>();
        List<CodBarra> codBarras = dbIccid.obterListaCodigoBarraPorProduto(item.getIdProduto(), produto.getGrupo());

        for (CodBarra codigoBarra : codBarras) {
            boolean contains = false;
            for (CodBarra codBarraSelecionado : item.getCodigosList()) {
                if (codBarraSelecionado.getCodBarraFinal() != null) {
                    BigInteger codigoInicial = new BigInteger(codBarraSelecionado.getCodBarraInicial());
                    BigInteger codigoFinal = new BigInteger(codBarraSelecionado.getCodBarraFinal());
                    for (BigInteger x = codigoInicial; x.compareTo(codigoFinal) <= 0; x = x.add(BigInteger.ONE)) {
                        if (x.toString().equals(codigoBarra.getCodBarraInicial())) {
                            contains = true;
                        }
                    }
                    continue;
                }
                if (codigoBarra.getCodBarraInicial().equals(codBarraSelecionado.getCodBarraInicial())) {
                    contains = true;
                }
            }
            if (!contains) {
                tempCodbarra.add(codigoBarra);
            }
        }
        return tempCodbarra;
    }

    private void baixarEstoque(AuditagemEstoque auditagemEstoque) {
        if (auditagemEstoque == null) {
            return;
        }
        dbEstoque.atualizaEstoque(auditagemEstoque.getIdProduto(), true,
                auditagemEstoque.getQuantidadeDivergente());
    }

    private void adicionarIccIdNaoCotadoItemVenda(AuditagemEstoque auditagemEstoque,
                                                  Produto produto) {

        List<CodBarra> tempCodbarra = pegarIccdsNaoBipados(auditagemEstoque, produto);
        if (tempCodbarra.isEmpty()) {
            return;
        }

        Venda venda = obterVenda();
        if (venda == null) {
            return;
        }

        try {
            dbVenda.addItemVenda(venda, produto, tempCodbarra, 0, null, null);
            dbIccid.deletaIccidAuditagemEstoque(venda.getId());
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void gerarPedidoDeVendaSemDivergencia() {
        for (AuditagemEstoque item : obterListaDeItemsAuditados()) {
            Produto produto = gerarObjetoProduto(item);
            if (produto.getId() == null) {
                continue;
            }
            adicionarIccIdNaoCotadoItemVenda(item, produto);
        }
        conferirProdutosComEstoqueZerado();
    }

    private void gerarPedidoDeVendaComDivergencia() {
        for (AuditagemEstoque item : obterListaDeItemsAuditados()) {

            Produto produto = gerarObjetoProduto(item);
            if (produto.getId() == null) {
                continue;
            }

            if (item.getQtdeReal() - item.getQtdeInformada() == 0) {
                adicionarIccIdNaoCotadoItemVenda(item, produto);
                continue;
            }

            Venda venda = obterVenda();
            if (venda == null) {
                return;
            }


            if (produto.getQtde() < 0 || (item.getQtdeReal() - item.getQtdeInformada()) < 0) {
                continue;
            }

            try {
                dbVenda.addItemVenda(venda, produto, obterIccidParaVenda(item), produto.getPrecovenda(), null, null);
                baixarEstoque(item);
                dbIccid.deletaIccidAuditagemEstoque(venda.getId());
            } catch (Exception e) {
                Timber.e(e);
                return;
            }
        }

        conferirProdutosComEstoqueZerado();
    }

    private void conferirProdutosComEstoqueZerado() {
        for (Produto produto : dbEstoque.getProdutosSemEstoque()) {
            List<CodBarra> iccids = dbIccid.obterListaCodigoBarraPorProduto(produto.getId(), produto.getGrupo());
            if (iccids.isEmpty()) {
                continue;
            }

            Venda venda = obterVenda();
            if (venda == null) {
                return;
            }

            try {
                dbVenda.addItemVenda(venda, produto, iccids, 0, null, null);
                dbIccid.deletaIccidAuditagemEstoque(venda.getId());
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        confirmarAuditagem();
    }

    private void confirmarAuditagem() {
        Stream.ofNullable(listItens).forEach(auditagemEstoque ->
                dbEstoque.confirmaAuditagem(auditagemEstoque));

        Venda venda = obterVenda();
        if (venda != null) {
            dbVenda.concluirVenda(venda);
            dbVisita.encerrarVisita(venda.getIdVisita());
        }

        statusAuditagem = EnumStatusAuditagem.CONCLUIDA;
    }
}
