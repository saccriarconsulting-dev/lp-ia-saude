package com.axys.redeflexmobile.ui.cliente.atualizar;

import com.axys.redeflexmobile.shared.models.LocalizacaoCliente;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface AtualizarClientePresenter extends BasePresenter<AtualizarClienteView> {

    void carregarUsuarioAtualizado(String id);

    void salvar(String nomeFantasia, String razaoSocial, String nomeContato,
                String dddTelefone, String telefone, String dddCelular,
                String celular, String email, String tipoLogradouro, String nomeLogradouro,
                String numeroLogradouro, String complementoLogradouro, String cidade, String bairro,
                String estado, String cep, String segmento, String pontoReferencia, String idVendedor, LocalizacaoCliente localizaCLiente);

    boolean possoVoltar(String nomeFantasia, String razaoSocial, String nomeContato,
                        String dddTelefone, String telefone, String dddCelular,
                        String celular, String email, String tipoLogradouro, String nomeLogradouro,
                        String numeroLogradouro, String complementoLogradouro, String cidade,
                        String bairro, String estado, String cep, String segmento, String pontoReferencia);

    void obterInformacoesCep(String cep);

    boolean validaEndereco(String tipoLogradouro, String nomeLogradouro,
                                  String numeroLogradouro, String cidade, String bairro,
                                  String estado, String cep);
}
