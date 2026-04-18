package com.axys.redeflexmobile.shared.enums;

import com.axys.redeflexmobile.R;

import java.util.Arrays;
import java.util.List;

public enum EnumStatusPID {
    EmEdicao("EDI", "Em edição", "Em Edição", R.drawable.status_label_grey),
    Enviado("ENV", "Enviado para análise", "Enviado", R.drawable.status_label_blue),
    AprovadoAlcadaVendedor("APV", "Aprovado", "Aprovado", R.drawable.status_label_green),
    AprovadoAlcadaSupervisor("APS", "Aprovado pelo Supervisor", "Aprovado", R.drawable.status_label_green),
    AprovadoAlcadaGerente("APG", "Aprovado pelo Gerente", "Aprovado", R.drawable.status_label_green),
    AprovadoAlcadaDiretor("APD", "Aprovado pelo Diretor", "Aprovado", R.drawable.status_label_green),
    AprovadoTimePreco("APT", "Aprovado pelo Time de Preço", "Aprovado", R.drawable.status_label_green),
    AnaliseSupervisor("ANS", "Em análise pelo Supervisor", "Aguardando Análise", R.drawable.status_label_yellow),
    AnaliseGerente("ANG", "Em análise pelo Gerente", "Aguardando Análise", R.drawable.status_label_yellow),
    AnaliseDiretor("AND", "Em análise pelo Diretor", "Aguardando Análise", R.drawable.status_label_yellow),
    AnaliseTimePreco("ANT", "Em análise pelo Time de Preço", "Aguardando Análise", R.drawable.status_label_yellow),
    DevolvidoPeloCliente("DEC", "Cliente não assinou o termo de incentivo", "Devolvido", R.drawable.status_label_red),
    DevolvidoPrazoExpirado("DEP", "Pedido expirado (proposta não foi lançada)", "Devolvido", R.drawable.status_label_red),
    DevolvidoSupervisor("DES", "Negado pelo Supervisor", "Devolvido", R.drawable.status_label_red),
    DevolvidoGerente("DEG", "Negado pelo Gerente", "Devolvido", R.drawable.status_label_red),
    DevolvidoDiretor("DED", "Negado pelo Diretor", "Devolvido", R.drawable.status_label_red),
    DevolvidoTimePreco("DET", "Negado pelo Time de Preço", "Devolvido", R.drawable.status_label_red),
    Contraproposta("CON", "Contraproposta do Time de Preço", "Avaliar Contraproposta", R.drawable.status_label_grey),
    Reanalise("REA", "Contraproposta Negada", "Enviado para Reanálise", R.drawable.status_label_grey),
    TermoDeIncentivo("TER", "Termo de incentivo enviado", "Aguardando Assinatura", R.drawable.status_label_blue),
    Finalizado("FIN", "Proposta Gerada", "Finalizado", R.drawable.status_label_green);

    public final String valor;
    public final String descricao;
    public final String short_descricao;
    public final int background;

    EnumStatusPID(String valor, String descricao, String short_descricao, int background) {
        this.valor = valor;
        this.descricao = descricao;
        this.short_descricao = short_descricao;
        this.background = background;
    }

    public static EnumStatusPID getPorId(String value) {
        for (EnumStatusPID tp : EnumStatusPID.values()) {
            if (tp.valor.equals(value)) {
                return tp;
            }
        }
        return null;
    }

    public static EnumStatusPID getPorDescricao(String descricao) {
        for (EnumStatusPID tp : EnumStatusPID.values()) {
            if (tp.descricao.equals(descricao)) {
                return tp;
            }
        }
        return null;
    }

    public static List<EnumStatusPID> getEnumList() {
        return Arrays.asList(EmEdicao, Enviado, AprovadoAlcadaVendedor, AprovadoAlcadaSupervisor, AprovadoAlcadaGerente, AprovadoAlcadaDiretor,
                AprovadoTimePreco, AnaliseSupervisor, AnaliseGerente, AnaliseDiretor, AnaliseTimePreco, DevolvidoPeloCliente, DevolvidoPrazoExpirado,
                DevolvidoSupervisor, DevolvidoGerente, DevolvidoDiretor, DevolvidoTimePreco, Contraproposta, Reanalise, TermoDeIncentivo, Finalizado);
    }

    public static List<String> getEnumDisplayList() {
        return Arrays.asList(EmEdicao.descricao, Enviado.descricao, AprovadoAlcadaVendedor.descricao, AprovadoAlcadaSupervisor.descricao, AprovadoAlcadaGerente.descricao,
                AprovadoAlcadaDiretor.descricao, AprovadoTimePreco.descricao, AnaliseSupervisor.descricao, AnaliseGerente.descricao, AnaliseDiretor.descricao,
                AnaliseTimePreco.descricao, DevolvidoPeloCliente.descricao, DevolvidoPrazoExpirado.descricao, DevolvidoSupervisor.descricao, DevolvidoGerente.descricao,
                DevolvidoDiretor.descricao, DevolvidoTimePreco.descricao, Contraproposta.descricao, Reanalise.descricao,
                TermoDeIncentivo.descricao, Finalizado.descricao);
    }
}
