package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.dialog.GenericaDialog;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

import static com.axys.redeflexmobile.shared.util.StringUtils.CPF_LENGTH;
import static com.axys.redeflexmobile.ui.redeflex.Config.VALOR_PADRAO_PRIMEIRA_VENDA_NOVOS_CREDENCIADOS;

/**
 * Created by Desenvolvimento on 24/06/2016.
 */

public class Cliente implements Serializable, ICustomSpinnerDialogModel, GenericaDialog.GenericaItem {

    public static final String DEVE_ATUALIZAR_LOCAL = "S";
    public static final String DEVE_ATUALIZAR_CONTATO = "S";

    //region atributos
    private String id;
    private String nomeFantasia;
    private String razaoSocial;
    private String tipoLogradouro;
    private String nomeLogradouro;
    private String numeroLogradouro;
    private String complementoLogradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String dddTelefone;
    private String telefone;
    private String dddCelular;
    private String cpf_cnpj;
    private String rg_ie;
    private String celular;
    private String codigoSGV;
    private String ativo;
    private String codigoIntraFlex;
    private String codigoeFresh;
    private String exibirCodigo;
    private String codigoAplic;
    private String atualizaLocal;
    private String senha;
    private String pedeSenha;
    private String auditagem;
    private String statuseFresh;
    private String statusSGV;
    private String atualizaContato;
    private String fechamentoFatura;
    private String emailCliente;
    private boolean incluir;
    private double latitude;
    private double longitude;
    private int diasCortes;
    private String curvaRecarga;
    private boolean bloqueiaVendaVista;
    private boolean atualizaBinario;
    private String curvaChip;
    private boolean adquirencia;
    private boolean eletronico;
    private boolean fisico;
    private int qtdBoletoPendente;
    private double valorBoletoPendente;
    private int cerca;
    private boolean bloqueiaAtendimento;
    private int qtdLocReprovada;
    private boolean recadastro;
    private String curvaAdquirencia;
    private boolean clienteFisico;
    private boolean clienteEletronico;
    private boolean clienteAdquirencia;
    private boolean clienteAppFlex;
    private boolean clienteCorban;
    private String merchandising;
    private String idSegmentoSGV;
    private String pontoReferencia;
    private String contato;
    private String possuiRecadastro;
    private String retornoRecadastro;
    @SerializedName("dddTelefone2") private String dddTelefone2;
    private String telefone2;
    private String dddCelular2;
    private String celular2;
    private String vencimentoFatura;
    private double limitePrimeiraVenda = VALOR_PADRAO_PRIMEIRA_VENDA_NOVOS_CREDENCIADOS;
    private boolean clienteMigracaoSub;
    private Boolean antecipacaoAutomatica;
    private String prazoDeNegociacao;
    private int idDomicilio;
    private String nomeBanco;
    private String tipoConta;
    private String agencia;
    private String digitoAgencia;
    private String conta;
    private String digitoConta;
    @Expose(serialize = false, deserialize = false) private boolean negociadoMigracaoSub;
    private int idBanco;
    private Integer idMcc;
    private Integer clienteTipoAdq;

    @Expose(serialize = false, deserialize = false)
    private List<Pendencia> pendencias;
    private String telefoneSub;
    private String emailSub;
    private boolean premium;

    private boolean VendaConsignada;

    private boolean ConsignacaoAtiva;

    private boolean AuditagemConsignadaObriga;
    private boolean clienteMigracaoAdq;

    private Integer idVendedor;
    //endregion

    //region GET/SET
    public boolean isRecadastro() {
        return recadastro;
    }

    public void setRecadastro(boolean recadastro) {
        this.recadastro = recadastro;
    }

    public int getQtdLocReprovada() {
        return qtdLocReprovada;
    }

    public void setQtdLocReprovada(int qtdLocReprovada) {
        this.qtdLocReprovada = qtdLocReprovada;
    }

    public boolean isBloqueiaAtendimento() {
        return bloqueiaAtendimento;
    }

    public void setBloqueiaAtendimento(boolean bloqueiaAtendimento) {
        this.bloqueiaAtendimento = bloqueiaAtendimento;
    }

    public int getCerca() {
        return cerca;
    }

    public void setCerca(int cerca) {
        this.cerca = cerca;
    }

    public String getCurvaRecarga() {
        return curvaRecarga;
    }

    public void setCurvaRecarga(String curvaRecarga) {
        this.curvaRecarga = curvaRecarga;
    }

    public String getCurvaChip() {
        return curvaChip;
    }

    public void setCurvaChip(String curvaChip) {
        this.curvaChip = curvaChip;
    }

    public boolean isAdquirencia() {
        return adquirencia;
    }

    public void setAdquirencia(boolean adquirencia) {
        this.adquirencia = adquirencia;
    }

    public boolean isEletronico() {
        return eletronico;
    }

    public void setEletronico(boolean eletronico) {
        this.eletronico = eletronico;
    }

    public boolean isFisico() {
        return fisico;
    }

    public void setFisico(boolean fisico) {
        this.fisico = fisico;
    }

    public int getQtdBoletoPendente() {
        return qtdBoletoPendente;
    }

    public void setQtdBoletoPendente(int qtdBoletoPendente) {
        this.qtdBoletoPendente = qtdBoletoPendente;
    }

    public double getValorBoletoPendente() {
        return valorBoletoPendente;
    }

    public void setValorBoletoPendente(double valorBoletoPendente) {
        this.valorBoletoPendente = valorBoletoPendente;
    }

    public boolean isAtualizaBinario() {
        return atualizaBinario;
    }

    public void setAtualizaBinario(boolean atualizaBinario) {
        this.atualizaBinario = atualizaBinario;
    }

    public boolean isBloqueiaVendaVista() {
        return bloqueiaVendaVista;
    }

    public void setBloqueiaVendaVista(boolean bloqueiaVendaVista) {
        this.bloqueiaVendaVista = bloqueiaVendaVista;
    }

    public int getDiasCortes() {
        return diasCortes;
    }

    public void setDiasCortes(int diasCortes) {
        this.diasCortes = diasCortes;
    }

    public boolean isIncluir() {
        return incluir;
    }

    public void setIncluir(boolean incluir) {
        this.incluir = incluir;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getAtualizaContato() {
        return atualizaContato;
    }

    public void setAtualizaContato(String atualizaContato) {
        this.atualizaContato = atualizaContato;
    }

    public String getFechamentoFatura() {
        return fechamentoFatura;
    }

    public void setFechamentoFatura(String fechamentoFatura) {
        this.fechamentoFatura = fechamentoFatura;
    }

    public String getStatusSGV() {
        return statusSGV;
    }

    public void setStatusSGV(String statusSGV) {
        this.statusSGV = statusSGV;
    }

    public String getStatuseFresh() {
        return statuseFresh;
    }

    public void setStatuseFresh(String statuseFresh) {
        this.statuseFresh = statuseFresh;
    }

    public String getAuditagem() {
        return auditagem;
    }

    public void setAuditagem(String auditagem) {
        this.auditagem = auditagem;
    }

    public String getPedeSenha() {
        return pedeSenha;
    }

    public void setPedeSenha(String pedeSenha) {
        this.pedeSenha = pedeSenha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAtualizaLocal() {
        return atualizaLocal;
    }

    public void setAtualizaLocal(String atualizaLocal) {
        this.atualizaLocal = atualizaLocal;
    }

    public String getCodigoAplic() {
        return codigoAplic;
    }

    public void setCodigoAplic(String codigoAplic) {
        this.codigoAplic = codigoAplic;
    }

    public String getExibirCodigo() {
        return exibirCodigo;
    }

    public void setExibirCodigo(String exibirCodigo) {
        this.exibirCodigo = exibirCodigo;
    }

    public String getCodigoeFresh() {
        return codigoeFresh;
    }

    public void setCodigoeFresh(String codigoeFresh) {
        this.codigoeFresh = codigoeFresh;
    }

    public String getCodigoIntraFlex() {
        return codigoIntraFlex;
    }

    public void setCodigoIntraFlex(String codigoIntraFlex) {
        this.codigoIntraFlex = codigoIntraFlex;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getDescricao() {
        return null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getNomeLogradouro() {
        return nomeLogradouro;
    }

    public void setNomeLogradouro(String nomeLogradouro) {
        this.nomeLogradouro = nomeLogradouro;
    }

    public String getNumeroLogradouro() {
        return numeroLogradouro;
    }

    public void setNumeroLogradouro(String numeroLogradouro) {
        this.numeroLogradouro = numeroLogradouro;
    }

    public String getComplementoLogradouro() {
        return complementoLogradouro;
    }

    public void setComplementoLogradouro(String complementoLogradouro) {
        this.complementoLogradouro = complementoLogradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getDddTelefone() {
        return dddTelefone;
    }

    public void setDddTelefone(String dddTelefone) {
        this.dddTelefone = dddTelefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDddCelular() {
        return dddCelular;
    }

    public void setDddCelular(String dddCelular) {
        this.dddCelular = dddCelular;
    }

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public String getRg_ie() {
        return rg_ie;
    }

    public void setRg_ie(String rg_ie) {
        this.rg_ie = rg_ie;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCodigoSGV() {
        return codigoSGV;
    }

    public void setCodigoSGV(String codigoSGV) {
        this.codigoSGV = codigoSGV;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public List<Pendencia> getPendencias() {
        return pendencias;
    }

    public void setPendencias(List<Pendencia> pendencias) {
        this.pendencias = pendencias;
    }

    public String getTelefoneSub() {
        return telefoneSub;
    }

    public void setTelefoneSub(String telefoneSub) {
        this.telefoneSub = telefoneSub;
    }

    public String getEmailSub() {
        return emailSub;
    }

    public void setEmailSub(String emailSub) {
        this.emailSub = emailSub;
    }

    public String retornaCodigoExibicao() {
        try {
            if (getExibirCodigo() != null) {
                switch (getExibirCodigo()) {
                    case "SGV":
                        return getCodigoSGV();
                    case "EFRESH":
                        return getCodigoeFresh();
                    case "APLIC":
                        return getCodigoAplic();
                }
            }
            return getCodigoIntraFlex();
        } catch (Exception ex) {
            ex.printStackTrace();
            return getCodigoIntraFlex();
        }
    }

    public String retornaCodigoSvgIntraflex() {
        if (getCodigoSGV() != null && getCodigoSGV().length() > 0) {
            return getCodigoSGV();
        } else {
            if (getCodigoIntraFlex() != null && getCodigoIntraFlex().length() > 0) {
                return getCodigoIntraFlex();
            } else {
                return "";
            }
        }
    }

    public String retornaSituacao() {
        String retorno = "";
        try {
            if (getExibirCodigo() != null) {
                if (getExibirCodigo().equalsIgnoreCase("SGV")) {
                    retorno = getStatusSGV();
                } else if (getExibirCodigo().equalsIgnoreCase("EFRESH")) {
                    switch (getStatuseFresh()) {
                        case "Y":
                            retorno = "Ativo";
                            break;
                        case "H":
                            retorno = "Bloqueado";
                            break;
                        case "I":
                            retorno = "Temporariamente Inativo";
                            break;
                        case "C":
                            retorno = "Fechado";
                            break;
                        case "B":
                            retorno = "Lista Negra";
                            break;
                        case "D":
                            retorno = "Duplicado";
                            break;
                        case "A":
                            retorno = "Aprovado";
                            break;
                        case "T":
                            retorno = "Pendencia Comercial";
                            break;
                        case "F":
                            retorno = "Pendência Financeira";
                            break;
                        case "S":
                            retorno = "Em Instalação";
                            break;
                    }
                } else {
                    if (getAtivo().equalsIgnoreCase("S"))
                        retorno = "Ativo";
                    else
                        retorno = "Inativo";
                }
            } else {
                if (getAtivo().equalsIgnoreCase("S"))
                    retorno = "Ativo";
                else
                    retorno = "Inativo";
            }
            return retorno;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

    public String retornaNumeroTelefone() {
        String retorno = "";
        try {
            if (!Util_IO.isNullOrEmpty(getCelular())) {
                if (!Util_IO.isNullOrEmpty(getDddCelular()))
                    retorno += "(" + getDddCelular() + ") ";
                retorno += getCelular();
            } else if (!Util_IO.isNullOrEmpty(getTelefone())) {
                if (!Util_IO.isNullOrEmpty(getDddTelefone()))
                    retorno += "(" + getDddTelefone() + ") ";
                retorno += getTelefone();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

    public String retornaEnderecoCompleto() {
        String retorno = "";
        try {
            if (!Util_IO.isNullOrEmpty(getTipoLogradouro()))
                retorno += getTipoLogradouro();
            if (!Util_IO.isNullOrEmpty(getNomeLogradouro()))
                retorno += ", " + getNomeLogradouro();
            if (!Util_IO.isNullOrEmpty(getNumeroLogradouro()))
                retorno += ", " + getNumeroLogradouro();
            if (!Util_IO.isNullOrEmpty(getComplementoLogradouro()))
                retorno += ", " + getComplementoLogradouro();
            if (!Util_IO.isNullOrEmpty(getBairro()))
                retorno += ", " + getBairro();
            if (!Util_IO.isNullOrEmpty(getCidade()))
                retorno += ", " + getCidade();
            if (!Util_IO.isNullOrEmpty(getEstado()))
                retorno += "/" + getEstado();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

    public String getCurvaAdquirencia() {
        return curvaAdquirencia;
    }

    public void setCurvaAdquirencia(String curvaAdquirencia) {
        this.curvaAdquirencia = curvaAdquirencia;
    }
    public boolean getClienteCorban() {
        return clienteCorban;
    }

    public void setClienteCorban(boolean clienteCorban) {
        this.clienteCorban = clienteCorban;
    }

    public boolean getClienteFisico() {
        return clienteFisico;
    }

    public void setClienteFisico(boolean clienteFisico) {
        this.clienteFisico = clienteFisico;
    }

    public boolean getClienteEletronico() {
        return clienteEletronico;
    }

    public void setClienteEletronico(boolean clienteEletronico) {
        this.clienteEletronico = clienteEletronico;
    }

    public boolean getClienteAdquirencia() {
        return clienteAdquirencia;
    }

    public void setClienteAdquirencia(boolean clienteAdquirencia) {
        this.clienteAdquirencia = clienteAdquirencia;
    }

    public boolean getClienteAppFlex() {
        return clienteAppFlex;
    }

    public void setClienteAppFlex(boolean clienteAppFlex) {
        this.clienteAppFlex = clienteAppFlex;
    }

    public String getMerchandising() {
        return merchandising;
    }

    public void setMerchandising(String merchandising) {
        this.merchandising = merchandising;
    }

    public String getIdSegmentoSGV() {
        return idSegmentoSGV;
    }

    public void setIdSegmentoSGV(String idSegmentoSGV) {
        this.idSegmentoSGV = idSegmentoSGV;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getPossuiRecadastro() {
        return possuiRecadastro;
    }

    public void setPossuiRecadastro(String possuiRecadastro) {
        this.possuiRecadastro = possuiRecadastro;
    }

    public String getRetornoRecadastro() {
        return retornoRecadastro;
    }

    public void setRetornoRecadastro(String retornoRecadastro) {
        this.retornoRecadastro = retornoRecadastro;
    }

    public String getDddTelefone2() {
        return dddTelefone2;
    }

    public void setDddTelefone2(String dddTelefone2) {
        this.dddTelefone2 = dddTelefone2;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getDddCelular2() {
        return dddCelular2;
    }

    public void setDddCelular2(String dddCelular2) {
        this.dddCelular2 = dddCelular2;
    }

    public String getCelular2() {
        return celular2;
    }

    public void setCelular2(String celular2) {
        this.celular2 = celular2;
    }

    public String getVencimentoFatura() {
        return vencimentoFatura;
    }

    public void setVencimentoFatura(String vencimentoFatura) {
        this.vencimentoFatura = vencimentoFatura;
    }

    public void setLimitePrimeiraVenda(Double limitePrimeiraVenda) {
        if (limitePrimeiraVenda > 0)
            this.limitePrimeiraVenda = limitePrimeiraVenda;
    }

    public Double getLimitePrimeiraVenda() {
        return this.limitePrimeiraVenda;
    }

    public boolean isClienteMigracaoSub() {
        return clienteMigracaoSub;
    }

    public void setClienteMigracaoSub(boolean clienteMigracaoSub) {
        this.clienteMigracaoSub = clienteMigracaoSub;
    }

    public Boolean isAntecipacaoAutomatica() {
        return antecipacaoAutomatica;
    }

    public void setAntecipacaoAutomatica(Boolean antecipacaoAutomatica) {
        this.antecipacaoAutomatica = antecipacaoAutomatica;
    }

    public String getPrazoDeNegociacao() {
        return prazoDeNegociacao;
    }

    public void setPrazoDeNegociacao(String prazoDeNegociacao) {
        this.prazoDeNegociacao = prazoDeNegociacao;
    }

    public int getIdDomicilio() {
        return idDomicilio;
    }

    public void setIdDomicilio(int idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getDigitoAgencia() {
        return digitoAgencia;
    }

    public void setDigitoAgencia(String digitoAgencia) {
        this.digitoAgencia = digitoAgencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getDigitoConta() {
        return digitoConta;
    }

    public void setDigitoConta(String digitoConta) {
        this.digitoConta = digitoConta;
    }

    public boolean isNegociadoMigracaoSub() {
        return negociadoMigracaoSub;
    }

    public void setNegociadoMigracaoSub(boolean negociadoMigracaoSub) {
        this.negociadoMigracaoSub = negociadoMigracaoSub;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public Integer getIdMcc() {
        return idMcc;
    }

    public void setIdMcc(Integer idMcc) {
        this.idMcc = idMcc;
    }

    public Integer getClienteTipoAdq() {
        return clienteTipoAdq;
    }

    public void setClienteTipoAdq(Integer clienteTipoAdq) {
        this.clienteTipoAdq = clienteTipoAdq;
    }

    public boolean getPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isVendaConsignada() {
        return VendaConsignada;
    }

    public void setVendaConsignada(boolean vendaConsignada) {
        this.VendaConsignada = vendaConsignada;
    }

    public boolean isConsignacaoAtiva() {
        return ConsignacaoAtiva;
    }

    public void setConsignacaoAtiva(boolean consignacaoAtiva) {
        this.ConsignacaoAtiva = consignacaoAtiva;
    }

    public boolean isAuditagemConsignadaObriga() {
        return AuditagemConsignadaObriga;
    }

    public void setAuditagemConsignadaObriga(boolean auditagemConsignadaObriga) {
        AuditagemConsignadaObriga = auditagemConsignadaObriga;
    }

    public boolean isClienteMigracaoAdq() {
        return clienteMigracaoAdq;
    }

    public void setClienteMigracaoAdq(boolean clienteMigracaoAdq) {
        this.clienteMigracaoAdq = clienteMigracaoAdq;
    }

    //endregion

    public String getCelularCompleto() {
        if (StringUtils.isEmpty(getDddCelular())) {
            return StringUtils.EMPTY_STRING;
        }

        return getDddCelular() + getCelular();
    }

    public String getCelularCompleto2() {
        if (StringUtils.isEmpty(getDddCelular2())) {
            return StringUtils.EMPTY_STRING;
        }

        return getDddCelular2() + getCelular2();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente cliente = (Cliente) o;

        if (incluir != cliente.incluir) return false;
        if (Double.compare(cliente.latitude, latitude) != 0) return false;
        if (Double.compare(cliente.longitude, longitude) != 0) return false;
        if (diasCortes != cliente.diasCortes) return false;
        if (bloqueiaVendaVista != cliente.bloqueiaVendaVista) return false;
        if (atualizaBinario != cliente.atualizaBinario) return false;
        if (adquirencia != cliente.adquirencia) return false;
        if (eletronico != cliente.eletronico) return false;
        if (fisico != cliente.fisico) return false;
        if (qtdBoletoPendente != cliente.qtdBoletoPendente) return false;
        if (Double.compare(cliente.valorBoletoPendente, valorBoletoPendente) != 0)
            return false;
        if (cerca != cliente.cerca) return false;
        if (bloqueiaAtendimento != cliente.bloqueiaAtendimento) return false;
        if (qtdLocReprovada != cliente.qtdLocReprovada) return false;
        if (recadastro != cliente.recadastro) return false;
        if (clienteFisico != cliente.clienteFisico) return false;
        if (clienteCorban != cliente.clienteCorban) return false;
        if (clienteEletronico != cliente.clienteEletronico) return false;
        if (clienteAdquirencia != cliente.clienteAdquirencia) return false;
        if (clienteAppFlex != cliente.clienteAppFlex) return false;
        if (!id.equals(cliente.id)) return false;
        if (nomeFantasia != null ? !nomeFantasia.equals(cliente.nomeFantasia) : cliente.nomeFantasia != null)
            return false;
        if (razaoSocial != null ? !razaoSocial.equals(cliente.razaoSocial) : cliente.razaoSocial != null)
            return false;
        if (tipoLogradouro != null ? !tipoLogradouro.equals(cliente.tipoLogradouro) : cliente.tipoLogradouro != null)
            return false;
        if (nomeLogradouro != null ? !nomeLogradouro.equals(cliente.nomeLogradouro) : cliente.nomeLogradouro != null)
            return false;
        if (numeroLogradouro != null ? !numeroLogradouro.equals(cliente.numeroLogradouro) : cliente.numeroLogradouro != null)
            return false;
        if (complementoLogradouro != null ? !complementoLogradouro.equals(cliente.complementoLogradouro) : cliente.complementoLogradouro != null)
            return false;
        if (bairro != null ? !bairro.equals(cliente.bairro) : cliente.bairro != null) return false;
        if (cidade != null ? !cidade.equals(cliente.cidade) : cliente.cidade != null) return false;
        if (estado != null ? !estado.equals(cliente.estado) : cliente.estado != null) return false;
        if (cep != null ? !cep.equals(cliente.cep) : cliente.cep != null) return false;
        if (dddTelefone != null ? !dddTelefone.equals(cliente.dddTelefone) : cliente.dddTelefone != null)
            return false;
        if (telefone != null ? !telefone.equals(cliente.telefone) : cliente.telefone != null)
            return false;
        if (dddCelular != null ? !dddCelular.equals(cliente.dddCelular) : cliente.dddCelular != null)
            return false;
        if (cpf_cnpj != null ? !cpf_cnpj.equals(cliente.cpf_cnpj) : cliente.cpf_cnpj != null)
            return false;
        if (rg_ie != null ? !rg_ie.equals(cliente.rg_ie) : cliente.rg_ie != null) return false;
        if (celular != null ? !celular.equals(cliente.celular) : cliente.celular != null)
            return false;
        if (codigoSGV != null ? !codigoSGV.equals(cliente.codigoSGV) : cliente.codigoSGV != null)
            return false;
        if (ativo != null ? !ativo.equals(cliente.ativo) : cliente.ativo != null) return false;
        if (codigoIntraFlex != null ? !codigoIntraFlex.equals(cliente.codigoIntraFlex) : cliente.codigoIntraFlex != null)
            return false;
        if (codigoeFresh != null ? !codigoeFresh.equals(cliente.codigoeFresh) : cliente.codigoeFresh != null)
            return false;
        if (exibirCodigo != null ? !exibirCodigo.equals(cliente.exibirCodigo) : cliente.exibirCodigo != null)
            return false;
        if (codigoAplic != null ? !codigoAplic.equals(cliente.codigoAplic) : cliente.codigoAplic != null)
            return false;
        if (atualizaLocal != null ? !atualizaLocal.equals(cliente.atualizaLocal) : cliente.atualizaLocal != null)
            return false;
        if (senha != null ? !senha.equals(cliente.senha) : cliente.senha != null) return false;
        if (pedeSenha != null ? !pedeSenha.equals(cliente.pedeSenha) : cliente.pedeSenha != null)
            return false;
        if (auditagem != null ? !auditagem.equals(cliente.auditagem) : cliente.auditagem != null)
            return false;
        if (statuseFresh != null ? !statuseFresh.equals(cliente.statuseFresh) : cliente.statuseFresh != null)
            return false;
        if (statusSGV != null ? !statusSGV.equals(cliente.statusSGV) : cliente.statusSGV != null)
            return false;
        if (atualizaContato != null ? !atualizaContato.equals(cliente.atualizaContato) : cliente.atualizaContato != null)
            return false;
        if (fechamentoFatura != null ? !fechamentoFatura.equals(cliente.fechamentoFatura) : cliente.fechamentoFatura != null)
            return false;
        if (emailCliente != null ? !emailCliente.equals(cliente.emailCliente) : cliente.emailCliente != null)
            return false;
        if (curvaRecarga != null ? !curvaRecarga.equals(cliente.curvaRecarga) : cliente.curvaRecarga != null)
            return false;
        if (curvaChip != null ? !curvaChip.equals(cliente.curvaChip) : cliente.curvaChip != null)
            return false;
        if (curvaAdquirencia != null ? !curvaAdquirencia.equals(cliente.curvaAdquirencia) : cliente.curvaAdquirencia != null)
            return false;
        if (merchandising != null ? !merchandising.equals(cliente.merchandising) : cliente.merchandising != null)
            return false;
        if (idSegmentoSGV != null ? !idSegmentoSGV.equals(cliente.idSegmentoSGV) : cliente.idSegmentoSGV != null)
            return false;
        if (pontoReferencia != null ? !pontoReferencia.equals(cliente.pontoReferencia) : cliente.pontoReferencia != null)
            return false;
        if (contato != null ? !contato.equals(cliente.contato) : cliente.contato != null)
            return false;
        if (possuiRecadastro != null ? !possuiRecadastro.equals(cliente.possuiRecadastro) : cliente.possuiRecadastro != null)
            return false;
        return retornoRecadastro != null ? retornoRecadastro.equals(cliente.retornoRecadastro) : cliente.retornoRecadastro == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id.hashCode();
        result = 31 * result + (nomeFantasia != null ? nomeFantasia.hashCode() : 0);
        result = 31 * result + (razaoSocial != null ? razaoSocial.hashCode() : 0);
        result = 31 * result + (tipoLogradouro != null ? tipoLogradouro.hashCode() : 0);
        result = 31 * result + (nomeLogradouro != null ? nomeLogradouro.hashCode() : 0);
        result = 31 * result + (numeroLogradouro != null ? numeroLogradouro.hashCode() : 0);
        result = 31 * result + (complementoLogradouro != null ? complementoLogradouro.hashCode() : 0);
        result = 31 * result + (bairro != null ? bairro.hashCode() : 0);
        result = 31 * result + (cidade != null ? cidade.hashCode() : 0);
        result = 31 * result + (estado != null ? estado.hashCode() : 0);
        result = 31 * result + (cep != null ? cep.hashCode() : 0);
        result = 31 * result + (dddTelefone != null ? dddTelefone.hashCode() : 0);
        result = 31 * result + (telefone != null ? telefone.hashCode() : 0);
        result = 31 * result + (dddCelular != null ? dddCelular.hashCode() : 0);
        result = 31 * result + (cpf_cnpj != null ? cpf_cnpj.hashCode() : 0);
        result = 31 * result + (rg_ie != null ? rg_ie.hashCode() : 0);
        result = 31 * result + (celular != null ? celular.hashCode() : 0);
        result = 31 * result + (codigoSGV != null ? codigoSGV.hashCode() : 0);
        result = 31 * result + (ativo != null ? ativo.hashCode() : 0);
        result = 31 * result + (codigoIntraFlex != null ? codigoIntraFlex.hashCode() : 0);
        result = 31 * result + (codigoeFresh != null ? codigoeFresh.hashCode() : 0);
        result = 31 * result + (exibirCodigo != null ? exibirCodigo.hashCode() : 0);
        result = 31 * result + (codigoAplic != null ? codigoAplic.hashCode() : 0);
        result = 31 * result + (atualizaLocal != null ? atualizaLocal.hashCode() : 0);
        result = 31 * result + (senha != null ? senha.hashCode() : 0);
        result = 31 * result + (pedeSenha != null ? pedeSenha.hashCode() : 0);
        result = 31 * result + (auditagem != null ? auditagem.hashCode() : 0);
        result = 31 * result + (statuseFresh != null ? statuseFresh.hashCode() : 0);
        result = 31 * result + (statusSGV != null ? statusSGV.hashCode() : 0);
        result = 31 * result + (atualizaContato != null ? atualizaContato.hashCode() : 0);
        result = 31 * result + (fechamentoFatura != null ? fechamentoFatura.hashCode() : 0);
        result = 31 * result + (emailCliente != null ? emailCliente.hashCode() : 0);
        result = 31 * result + (incluir ? 1 : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + diasCortes;
        result = 31 * result + (curvaRecarga != null ? curvaRecarga.hashCode() : 0);
        result = 31 * result + (bloqueiaVendaVista ? 1 : 0);
        result = 31 * result + (atualizaBinario ? 1 : 0);
        result = 31 * result + (curvaChip != null ? curvaChip.hashCode() : 0);
        result = 31 * result + (adquirencia ? 1 : 0);
        result = 31 * result + (eletronico ? 1 : 0);
        result = 31 * result + (fisico ? 1 : 0);
        result = 31 * result + qtdBoletoPendente;
        temp = Double.doubleToLongBits(valorBoletoPendente);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + cerca;
        result = 31 * result + (bloqueiaAtendimento ? 1 : 0);
        result = 31 * result + qtdLocReprovada;
        result = 31 * result + (recadastro ? 1 : 0);
        result = 31 * result + (curvaAdquirencia != null ? curvaAdquirencia.hashCode() : 0);
        result = 31 * result + (clienteFisico ? 1 : 0);
        result = 31 * result + (clienteCorban ? 1 : 0);
        result = 31 * result + (clienteEletronico ? 1 : 0);
        result = 31 * result + (clienteAdquirencia ? 1 : 0);
        result = 31 * result + (clienteAppFlex ? 1 : 0);
        result = 31 * result + (merchandising != null ? merchandising.hashCode() : 0);
        result = 31 * result + (idSegmentoSGV != null ? idSegmentoSGV.hashCode() : 0);
        result = 31 * result + (pontoReferencia != null ? pontoReferencia.hashCode() : 0);
        result = 31 * result + (contato != null ? contato.hashCode() : 0);
        result = 31 * result + (possuiRecadastro != null ? possuiRecadastro.hashCode() : 0);
        result = 31 * result + (retornoRecadastro != null ? retornoRecadastro.hashCode() : 0);
        return result;
    }

    public EnumRegisterPersonType personType() {
        return getCpf_cnpj().length() == CPF_LENGTH ?
                EnumRegisterPersonType.PHYSICAL : EnumRegisterPersonType.JURIDICAL;
    }

    public @Nullable String selectClientAdquirenciaType() {
        switch (clienteTipoAdq) {
            case 1:
                return "ISO";
            case 2:
                return "SUB";
            default:
                return null;
        }
    }

    public String mountClientInfo() {
        return retornaCodigoExibicao() + " - " + getNomeFantasia();
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    @Override
    public Integer getIdValue() {
        return Integer.parseInt(getId());
    }

    @Override
    public String getDescriptionValue()
    {
        return getNomeFantasia() + " - " + codigoSGV;
    }
}
