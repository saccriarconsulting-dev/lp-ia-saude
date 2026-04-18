package com.axys.redeflexmobile.shared.dao;

import android.content.Context;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;

public class PosDaoImpl implements PosDao {

    private Context context;

    public PosDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public Single<List<InformacaoGeralPOS>> obterInformacoesPosPorCliente(String clienteId) {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                emitter.onError(new IllegalStateException("Single already disposed"));
                return;
            }

            List<InformacaoGeralPOS> pos = Stream.ofNullable(Tabela.TabelaInformacoesGeraisPOS
                    .obterInformacoesGeraisPOSPorId(context, clienteId))
                    .map(this::prepararDataMaisRecente)
                    .toList();

            if (pos.isEmpty()) {
                emitter.onError(new IllegalStateException("List of POS is empty"));
                return;
            }

            emitter.onSuccess(pos);
        });
    }

    @Override
    public void removerPos(int id) {
        Tabela.TabelaInformacoesGeraisPOS.remover(context, id);
    }

    private InformacaoGeralPOS prepararDataMaisRecente(InformacaoGeralPOS informacaoGeralPOS) {
        informacaoGeralPOS.setDataUltimaTransacao(new Date());
        if (informacaoGeralPOS.getDataUltimaTransacaoAdquirencia() != null && informacaoGeralPOS.getDataUltimaVendaRecarga() == null) {
            informacaoGeralPOS.setDataUltimaTransacao(informacaoGeralPOS.getDataUltimaTransacaoAdquirencia());
        } else if (informacaoGeralPOS.getDataUltimaTransacaoAdquirencia() == null && informacaoGeralPOS.getDataUltimaVendaRecarga() != null) {
            informacaoGeralPOS.setDataUltimaTransacao(informacaoGeralPOS.getDataUltimaVendaRecarga());
        } else if (informacaoGeralPOS.getDataUltimaTransacaoAdquirencia().after(informacaoGeralPOS.getDataUltimaVendaRecarga())) {
            informacaoGeralPOS.setDataUltimaTransacao(informacaoGeralPOS.getDataUltimaTransacaoAdquirencia());
        } else {
            informacaoGeralPOS.setDataUltimaTransacao(informacaoGeralPOS.getDataUltimaVendaRecarga());
        }

        return informacaoGeralPOS;
    }
}
