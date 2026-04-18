package com.axys.redeflexmobile.ui.cliente.atualizar;

import android.util.Log;

import com.axys.redeflexmobile.shared.bd.DBLocalizacaoCliente;
import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.dao.SegmentoDao;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.EnumAtualizarCliente;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.models.AtualizaCliente;
import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.LocalizacaoCliente;
import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.repository.AtualizarClienteRepository;
import com.axys.redeflexmobile.shared.services.bus.LocalizacaoClienteBus;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.Date;

public class AtualizarClientePresenterImpl extends BasePresenterImpl<AtualizarClienteView>
        implements AtualizarClientePresenter {

    private static final int TAMANHO_TELEFONE = 10;
    private static final int TAMANHO_CELULAR = 11;
    private static final String TEXTO_VAZIO = "";
    private static final String TEXTO_SEPARADOR = "-";
    private static final int LENGTH_DDD = 2;
    private static final int LENGTH_TELEFONE = 8;
    private static final int LENGTH_CELULAR = 9;
    private AtualizarClienteRepository repository;
    private ClienteDao clienteDao;
    private String id;
    private AtualizaCliente atualizaCliente;
    private ClienteManager clienteManager;
    private SegmentoDao segmentoDao;
    private DBLocalizacaoCliente localizacaoClienteDAO;

    AtualizarClientePresenterImpl(AtualizarClienteView view,
                                  SchedulerProvider schedulerProvider,
                                  ExceptionUtils exceptionUtils,
                                  AtualizarClienteRepository repository,
                                  ClienteDao clienteDao,
                                  ClienteManager clienteManager,
                                  SegmentoDao segmentoDao,
                                  DBLocalizacaoCliente localizacaoClienteDAO) {
        super(view, schedulerProvider, exceptionUtils);
        this.repository = repository;
        this.clienteDao = clienteDao;
        this.clienteManager = clienteManager;
        this.segmentoDao = segmentoDao;
        this.localizacaoClienteDAO = localizacaoClienteDAO;
    }

    @Override
    public void carregarUsuarioAtualizado(String id) {
        this.id = id;
        repository.obterPorId(id, cliente -> {
            if (cliente == null) {
                carregarUsuario(id);
                return;
            }

            atualizaCliente = cliente;
            Segmento segmento = segmentoDao.obterSegmentPorId(cliente.getSegmento());
            getView().popularTela(cliente, segmento);
        });
    }

    @Override
    public void salvar(String nomeFantasia, String razaoSocial, String nomeContato,
                       String dddTelefone, String telefone, String dddCelular,
                       String celular, String email, String tipoLogradouro, String nomeLogradouro,
                       String numeroLogradouro, String complementoLogradouro, String cidade,
                       String bairro, String estado, String cep, String segmento, String pontoReferencia,
                       String idVendedor, LocalizacaoCliente localizacaoCliente) {

        boolean valido = validarDados(nomeFantasia, razaoSocial, nomeContato, dddTelefone, telefone,
                dddCelular, celular, email, tipoLogradouro, nomeLogradouro, numeroLogradouro, cidade,
                bairro, estado, cep, segmento);

        if (!valido) {
            return;
        }

        AtualizaCliente cliente = new AtualizaCliente();
        cliente.setId(Util_IO.trataString(id));
        cliente.setNomeFantasia(Util_IO.trataString(nomeFantasia));
        cliente.setRazaoSocial(Util_IO.trataString(razaoSocial));
        cliente.setNomeContato(Util_IO.trataString(nomeContato));
        cliente.setDddTelefone(Util_IO.trataString(dddTelefone));
        cliente.setTelefone(Util_IO.trataString(telefone));
        cliente.setDddCelular(Util_IO.trataString(dddCelular));
        cliente.setCelular(Util_IO.trataString(celular));
        cliente.setTipoLogradouro(Util_IO.trataString(tipoLogradouro));
        cliente.setNomeLogradouro(Util_IO.trataString(nomeLogradouro));
        cliente.setNumeroLogradouro(Util_IO.trataString(numeroLogradouro));
        cliente.setComplementoLogradouro(Util_IO.trataString(complementoLogradouro));
        cliente.setCidade(Util_IO.trataString(cidade));
        cliente.setBairro(Util_IO.trataString(bairro));
        cliente.setEstado(Util_IO.trataString(estado));
        cliente.setCep(Util_IO.trataString(cep));
        cliente.setSegmento(Util_IO.trataString(segmento));
        cliente.setPontoReferencia(Util_IO.trataString(pontoReferencia));
        cliente.setEmail(Util_IO.trataString(email));
        cliente.setIdVendedor(idVendedor);
        cliente.setDataHora(new Date());
        cliente.setStatus(EnumAtualizarCliente.ANDAMENTO.getValue());
        repository.salvar(cliente);

        // Armazena dados localização do Cliente
        if (localizacaoCliente != null) {
            localizacaoClienteDAO.addLocalizacao(localizacaoCliente);
        }
        getView().mostrarSucesso();
    }

    @Override
    public boolean possoVoltar(String nomeFantasia, String razaoSocial, String nomeContato,
                               String dddTelefone, String telefone, String dddCelular, String celular,
                               String email, String tipoLogradouro, String nomeLogradouro, String numeroLogradouro,
                               String complementoLogradouro, String cidade, String bairro,
                               String estado, String cep, String segmento, String pontoReferencia) {

        if (atualizaCliente.getNomeFantasia() != null
                && !atualizaCliente.getNomeFantasia().equals(Util_IO.trataString(nomeFantasia))) {
            return false;
        }

        if (atualizaCliente.getSegmento() != null
                && !atualizaCliente.getSegmento().equals(Util_IO.trataString(segmento))) {
            return false;
        }

        if (atualizaCliente.getRazaoSocial() != null
                && !atualizaCliente.getRazaoSocial().equals(Util_IO.trataString(razaoSocial))) {
            return false;
        }

        if (atualizaCliente.getNomeContato() != null
                && !atualizaCliente.getNomeContato().equals(Util_IO.trataString(nomeContato))) {
            return false;
        }

        if (atualizaCliente.getDddTelefone() != null
                && !atualizaCliente.getDddTelefone().equals(Util_IO.trataString(dddTelefone))) {
            return false;
        }

        if (atualizaCliente.getTelefone() != null
                && !atualizaCliente.getTelefone().equals(Util_IO.trataString(telefone))) {
            return false;
        }

        if (atualizaCliente.getDddCelular() != null
                && !atualizaCliente.getDddCelular().equals(Util_IO.trataString(dddCelular))) {
            return false;
        }

        if (atualizaCliente.getCelular() != null
                && !atualizaCliente.getCelular().equals(Util_IO.trataString(celular))) {
            return false;
        }

        if (atualizaCliente.getTipoLogradouro() != null
                && !atualizaCliente.getTipoLogradouro().equals(Util_IO.trataString(tipoLogradouro))) {
            return false;
        }

        if (atualizaCliente.getNomeLogradouro() != null
                && !atualizaCliente.getNomeLogradouro().equals(Util_IO.trataString(nomeLogradouro))) {
            return false;
        }

        if (atualizaCliente.getNumeroLogradouro() != null
                && !atualizaCliente.getNumeroLogradouro().equals(Util_IO.trataString(numeroLogradouro))) {
            return false;
        }

        if (atualizaCliente.getComplementoLogradouro() != null
                && !atualizaCliente.getComplementoLogradouro().equals(Util_IO.trataString(complementoLogradouro))) {
            return false;
        }

        if (atualizaCliente.getCidade() != null
                && !atualizaCliente.getCidade().equals(Util_IO.trataString(cidade))) {
            return false;
        }

        if (atualizaCliente.getBairro() != null
                && !atualizaCliente.getBairro().equals(Util_IO.trataString(bairro))) {
            return false;
        }

        if (atualizaCliente.getEstado() != null
                && !atualizaCliente.getEstado().equals(Util_IO.trataString(estado))) {
            return false;
        }

        return atualizaCliente.getCep() == null
                || atualizaCliente.getCep().equals(Util_IO.trataString(cep));
    }

    @Override
    public void obterInformacoesCep(String cep) {

        compositeDisposable.add(clienteManager.obterInformacoesCep(cep)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(dispose -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::onObterInformacoesCep, this::onErrorObterInformacoes)
        );
    }

    private void onErrorObterInformacoes(Throwable throwable) {
        getView().exibirErroCep();
    }

    private void onObterInformacoesCep(Cep[] cep) {
        if (cep.length > 0) {
            getView().preencherCamposCep(cep[0]);
        }
    }

    private void carregarUsuario(final String id) {
        new Thread(() -> {
            Cliente cliente = clienteDao.obterPorId(id);
            Segmento segmento = segmentoDao.obterSegmentPorId(cliente.getIdSegmentoSGV());
            if (segmento == null) {
                cliente.setIdSegmentoSGV(null);
            }
            preencherAtualizaCliente(cliente);
            getView().popularTela(cliente, segmento);

            if (Validacoes.cpfValido(cliente.getCpf_cnpj())) {
                getView().trocarLabelsPessoaFisica();
            }

        }).start();
    }

    private boolean validarDados(String nomeFantasia, String razaoSocial, String nomeContato,
                                 String dddTelefone, String telefone, String dddCelular,
                                 String celular, String email, String tipoLogradouro,
                                 String nomeLogradouro, String numeroLogradouro, String cidade,
                                 String bairro, String estado, String cep, String segmento) {


        if (dadosPessoaisInvalidos(nomeFantasia, razaoSocial, segmento)) return false;

        if (dadosContatoInvalidos(nomeContato, dddTelefone, telefone, dddCelular, celular, email))
            return false;

        return !dadosEnderecoInvalidos(
                tipoLogradouro,
                nomeLogradouro,
                numeroLogradouro,
                cidade,
                bairro,
                estado,
                cep
        );
    }

    private boolean dadosPessoaisInvalidos(String nomeFantasia, String razaoSocial, String segmento) {
        if (Util_IO.isNullOrEmpty(nomeFantasia)) {
            getView().mostrarErroNomeFantasia();
            return true;
        }

        if (Util_IO.isNullOrEmpty(segmento)) {
            getView().mostrarErroSegmento();
            return true;
        }

        if (Util_IO.isNullOrEmpty(razaoSocial)) {
            getView().mostrarErroRazaoSocial();
            return true;
        }

        return false;
    }

    private boolean dadosContatoInvalidos(String nomeContato, String dddTelefone, String telefone,
                                          String dddCelular, String celular, String email) {
        if (Util_IO.isNullOrEmpty(nomeContato)) {
            getView().mostrarErroNomeContato();
            return true;
        }

        if (validacaoNumeroTelefoneCelular(dddTelefone, dddCelular, telefone, celular)) {
            getView().mostrarErroTelefoneCelular();
            return true;
        }

        if (!Util_IO.isNullOrEmpty(dddTelefone) && dddTelefone.length() < LENGTH_DDD) {
            getView().mostrarErroDDDTelefone();
            return true;
        }

        if (!Util_IO.isNullOrEmpty(telefone)
                && telefone.replace(TEXTO_SEPARADOR, TEXTO_VAZIO).length() < LENGTH_TELEFONE) {
            getView().mostrarErroTelefone();
            return true;
        }

        if (Util_IO.isNullOrEmpty(dddCelular)) {
            getView().mostrarErroDDDCelular();
            return true;
        }

        if (!Util_IO.isNullOrEmpty(dddCelular) && dddCelular.length() < LENGTH_DDD) {
            getView().mostrarErroDDDCelular();
            return true;
        }

        if (Util_IO.isNullOrEmpty(celular)) {
            getView().mostrarErroCelular();
            return true;
        }

        if (!Util_IO.isNullOrEmpty(celular) && celular.replace(TEXTO_SEPARADOR, TEXTO_VAZIO).length() < LENGTH_CELULAR) {
            getView().mostrarErroCelular();
            return true;
        }

        if (Util_IO.isNullOrEmpty(email)) {
            getView().mostrarErroEmail();
            return true;
        }

        if (!Util_IO.isNullOrEmpty(email) && !Validacoes.validaEmail(email)) {
            getView().mostrarErroEmail();
            return true;
        }
        return false;
    }

    private boolean dadosEnderecoInvalidos(String tipoLogradouro, String nomeLogradouro,
                                           String numeroLogradouro, String cidade, String bairro,
                                           String estado, String cep) {
        if (Util_IO.isNullOrEmpty(tipoLogradouro)) {
            getView().mostrarErroTipoLogradouro();
            return true;
        }

        if (Util_IO.isNullOrEmpty(nomeLogradouro)) {
            getView().mostrarErroNomeLogradouro();
            return true;
        }

        if (Util_IO.isNullOrEmpty(numeroLogradouro)) {
            getView().mostrarErroNumeroLogradouro();
            return true;
        }

        if (Util_IO.isNullOrEmpty(cidade)) {
            getView().mostrarErroCidade();
            return true;
        }

        if (Util_IO.isNullOrEmpty(bairro)) {
            getView().mostrarErroBairro();
            return true;
        }

        if (Util_IO.isNullOrEmpty(estado)) {
            getView().mostrarErroEstado();
            return true;
        }

        if (Util_IO.isNullOrEmpty(cep)) {
            getView().mostrarErroCep();
            return true;
        }
        return false;
    }

    private boolean validacaoNumeroTelefoneCelular(String dddTelefone, String dddCelular,
                                                   String telefone, String celular) {
        String checarTelefone = dddTelefone + telefone.replace(TEXTO_SEPARADOR, TEXTO_VAZIO);
        String checarCelular = dddCelular + celular.replace(TEXTO_SEPARADOR, TEXTO_VAZIO);

        return checarTelefone.length() < TAMANHO_TELEFONE && checarCelular.length() < TAMANHO_CELULAR;
    }

    private void preencherAtualizaCliente(Cliente clienteBanco) {
        atualizaCliente = new AtualizaCliente();
        atualizaCliente.setId(clienteBanco.getId());
        atualizaCliente.setNomeFantasia(clienteBanco.getNomeFantasia());
        atualizaCliente.setRazaoSocial(clienteBanco.getRazaoSocial());
        atualizaCliente.setNomeContato(clienteBanco.getContato());
        atualizaCliente.setDddTelefone(clienteBanco.getDddTelefone());
        atualizaCliente.setTelefone(clienteBanco.getTelefone());
        atualizaCliente.setDddCelular(clienteBanco.getDddCelular());
        atualizaCliente.setCelular(clienteBanco.getCelular());
        atualizaCliente.setEmail(clienteBanco.getEmailCliente());
        atualizaCliente.setTipoLogradouro(clienteBanco.getTipoLogradouro());
        atualizaCliente.setNomeLogradouro(clienteBanco.getNomeLogradouro());
        atualizaCliente.setNumeroLogradouro(clienteBanco.getNumeroLogradouro());
        atualizaCliente.setComplementoLogradouro(clienteBanco.getComplementoLogradouro());
        atualizaCliente.setCidade(clienteBanco.getCidade());
        atualizaCliente.setBairro(clienteBanco.getBairro());
        atualizaCliente.setEstado(clienteBanco.getEstado());
        atualizaCliente.setCep(clienteBanco.getCep());
        atualizaCliente.setSegmento(clienteBanco.getIdSegmentoSGV());
        atualizaCliente.setPontoReferencia(clienteBanco.getPontoReferencia());
    }

    public boolean validaEndereco(String tipoLogradouro, String nomeLogradouro,
                                  String numeroLogradouro, String cidade, String bairro,
                                  String estado, String cep)
    {
        boolean vValidaEndereco = true;

        if (!atualizaCliente.getBairro().equals(bairro)) {
            vValidaEndereco = false;
        }

        if (!atualizaCliente.getTipoLogradouro().equals(tipoLogradouro)) {
            vValidaEndereco = false;
        }

        if (!atualizaCliente.getNomeLogradouro().equals(nomeLogradouro)) {
            vValidaEndereco = false;
        }

        if (!atualizaCliente.getNumeroLogradouro().equals(numeroLogradouro)) {
            vValidaEndereco = false;
        }

        if (!atualizaCliente.getCidade().equals(cidade)) {
            vValidaEndereco = false;
        }

        if (!atualizaCliente.getEstado().equals(estado)) {
            vValidaEndereco = false;
        }

        if (!atualizaCliente.getCep().equals(cep.replace("-",""))) {
            vValidaEndereco = false;
        }

        return vValidaEndereco;
    }
}
