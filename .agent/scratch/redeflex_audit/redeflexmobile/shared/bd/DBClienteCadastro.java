package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterSexo;
import com.axys.redeflexmobile.shared.models.AnexoRetorno;
import com.axys.redeflexmobile.shared.models.ClienteCadastroAnexo;
import com.axys.redeflexmobile.shared.models.ClienteCadastroPOS;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.RetCadCliente;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddress;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAttachment;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContato;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContatoPrincipal;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterHorarioFunc;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterPartners;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterTax;
import com.axys.redeflexmobile.shared.models.customerregister.MachineType;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

/**
 * Created by joao.viana on 01/09/2016.
 */

public class DBClienteCadastro {

    private Context mContext;
    private String mTabelaCadastro = "ClienteCadastro";
    private String mTabelaAnexo = "ClienteCadastroAnexo";
    private String mTabelaSocios = "ClienteCadastroSocios";

    public DBClienteCadastro(Context pContext) {
        this.mContext = pContext;
    }

    public void addCadastro(CustomerRegister customerRegister) {
        if (customerRegister == null) return;
        Colaborador colaborador = new DBColaborador(mContext).get();

        ContentValues values = new ContentValues();
        boolean recadastro = false;
        if (StringUtils.isNotEmpty(customerRegister.getId())) {
            values.put("id", customerRegister.getId());
            recadastro = true;
        }

        final String dataCadastro = StringUtils.isNotEmpty(customerRegister.getId())
                ? Util_IO.dateTimeToString(customerRegister.getDateRegister(), Config.FormatDateTimeStringBanco)
                : Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco);

        final String onlyNumbersRegisterDate = StringUtils.returnOnlyNumbers(dataCadastro);
        final String token = onlyNumbersRegisterDate.substring(onlyNumbersRegisterDate.length() - 5);
        values.put("datacadastro", dataCadastro);
        values.put("token", token);
        values.put("latitude", customerRegister.getLatitude());
        values.put("longitude", customerRegister.getLongitude());
        values.put("precisao", customerRegister.getPrecision());
        values.put("versaoApp", BuildConfig.VERSION_NAME);
        values.put("recadastro", recadastro);
        values.put("tipoCliente", customerRegister.getCustomerType().getDescriptionWhitOutAccents());
        values.put("tipo", customerRegister.getPersonType().getCharValue());
        values.put("mcc", customerRegister.getMcc());
        values.put("nome", Util_IO.trataString(customerRegister.getFullName()));
        values.put("fantasia", Util_IO.trataString(customerRegister.getFantasyName()));
        values.put("cpfcnpj", customerRegister.getCpfCnpj());
        values.put("rg_ie", customerRegister.getRgIe());
        values.put("datanascimento", Util_IO.dateTimeToString(customerRegister.getBirthdate(), Config.FormatDateTimeStringBanco));
        values.put("segmento", customerRegister.getSegment());
        values.put("codigoSgv", customerRegister.getSgvCode());
        values.put("nomeSocio", Util_IO.trataString(customerRegister.getPartnerName()));
        values.put("cpfSocio", customerRegister.getPartnerCpf());
        values.put("dtnascimentoSocio", Util_IO.dateTimeToString(customerRegister.getPartnerBirthday(), Config.FormatDateTimeStringBanco));

        if (customerRegister.getAccountType() != null) {
            values.put("tipoContaId", customerRegister.getAccountType());
        }

        values.put("banco", customerRegister.getBank());
        values.put("agencia", customerRegister.getBankAgency());
        values.put("digitoAgencia", Util_IO.trataString(customerRegister.getBankAgencyDigit()));
        values.put("conta", customerRegister.getBankAccount());
        values.put("digitoConta", Util_IO.trataString(customerRegister.getBankAccountDigit()));
        values.put("faturamentoMedioPrevisto", customerRegister.getForeseenRevenue());
        values.put("idPrazoNegociacao", customerRegister.getNegotiationTermId());
        values.put("antecipacao", customerRegister.getAnticipation());
        values.put("debitoAutomatico", customerRegister.getDebitAutomatic());
        values.put("aluguelMaqVenc", customerRegister.getRentalMachineDue());
        values.put("isencao", customerRegister.getExemption());
        values.put("obs", Util_IO.trataString(customerRegister.getObservation()));

        // Dados Pessoa Fisica
        values.put("idProfissao", customerRegister.getIdProfissao());
        values.put("idRenda", customerRegister.getIdRenda());
        values.put("idPatrimonio", customerRegister.getIdPatrimonio());

        if (customerRegister.getSexo() != null) {
            values.put("sexo", customerRegister.getSexo().getCharValue());
        }

        values.put("faturamentoBruto", customerRegister.getFaturamentoBruto());
        values.put("percVendaCartao", customerRegister.getPercVendaCartao());
        values.put("percVendaEcommerce", customerRegister.getPercVendaEcommerce());
        values.put("percFaturamento", customerRegister.getPercFaturamento());
        values.put("percEntregaImediata", customerRegister.getPercEntregaImediata());
        values.put("percEntregaPosterior", customerRegister.getPercEntregaPosterior());
        values.put("prazoEntrega", customerRegister.getPrazoEntrega());
        values.put("entregaPosCompra", customerRegister.getEntregaPosCompra());
        values.put("dataFundacaoPF", Util_IO.dateTimeToString(customerRegister.getDataFundacaoPF(), Config.FormatDateTimeStringBanco));
        values.put("IdSolPid_Server", customerRegister.getIdSolPid_Server());

        long id;
        if (!recadastro) {
            try {
                GPSTracker gps = new GPSTracker(mContext);
                values.put("latitude", gps.getLatitude());
                values.put("longitude", gps.getLongitude());
                values.put("precisao", gps.getPrecisao());
            } catch (Exception ex) {
                values.put("latitude", 0.0);
                values.put("longitude", 0.0);
                values.put("precisao", 0.0);
                Timber.e(ex);
            }
            id = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaCadastro, null, values);
            if (id == -1) return;
        } else {
            id = Long.parseLong(customerRegister.getId());
            values.put("dataatualizacao", Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
            values.put("sync", 0);
            values.put("retorno", "");
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCadastro, values, "[id]=?", new String[]{customerRegister.getId()});
        }

        for (CustomerRegisterAttachment clienteCadastroAnexo : customerRegister.getAttachments()) {

            ContentValues valueAttachment = new ContentValues();
            valueAttachment.put("idCadastro", id);
            valueAttachment.put("anexo", clienteCadastroAnexo.getFile());
            valueAttachment.put("tipo", clienteCadastroAnexo.getType());
            valueAttachment.put("latitude", clienteCadastroAnexo.getLatitude());
            valueAttachment.put("longitude", clienteCadastroAnexo.getLongitude());
            valueAttachment.put("precisao", clienteCadastroAnexo.getPrecision());
            valueAttachment.put("tamanhoArquivo", clienteCadastroAnexo.getFileSize());
            valueAttachment.put("sync", 0);

            if (!recadastro || clienteCadastroAnexo.getId() == null) {
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaAnexo, null, valueAttachment);
                continue;
            }

            if (clienteCadastroAnexo.getId() != null && !clienteCadastroAnexo.isActivated()) {

                String query = "SELECT id, anexo FROM [ClienteCadastroAnexo] WHERE id=?";
                Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(query,
                        new String[]{String.valueOf(clienteCadastroAnexo.getId())});

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        if (cursor.getString(1).contains("/Files/Compressed")) {
                            Utilidades.deletaArquivo(cursor.getString(1));
                        }
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAnexo, "[id]=? ",
                                new String[]{String.valueOf(cursor.getString(0))});

                    } while (cursor.moveToNext());
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        addTaxes(id, customerRegister.getTaxList());

        new DBClienteCadastroPOS(mContext).salvarModelosPOS(id, StringUtils.isEmpty(customerRegister.getId()),
                Stream.ofNullable(customerRegister.getPosList())
                        .map(machineType -> {
                            ClienteCadastroPOS model = new ClienteCadastroPOS();
                            model.setIdAppMobile(machineType.getId());
                            model.setIdTipoMaquina(machineType.getIdMachineType());
                            model.setPosModelo(machineType.getModel());
                            model.setPosDescricao(machineType.getDescription());
                            model.setValorAluguel(machineType.getSelectedRentValue());
                            model.setCpfCnpjCliente(customerRegister.getCpfCnpj());
                            model.setTipoConexao(machineType.getIdConnectionType());
                            model.setMetragemCabo(machineType.getCableLength());
                            model.setIdOperadora(machineType.getCarrierId());
                            return model;
                        })
                        .toList());

        // Ao adicionar os Endereços ajusta os campos referente ao contato principal que fica na tabela de endereços
        addEnderecos(Stream.ofNullable(customerRegister.getAddresses())
                .map(customerRegisterAddress -> {
                    customerRegisterAddress.setIdCustomer((int) id);
                    customerRegisterAddress.setIdSalesman(colaborador.getId());
                    customerRegisterAddress.setCpfCnpj(customerRegister.getCpfCnpj());

                    // Em caso de Pessoa Fisica pega os dados preenchidos
                    if (customerRegister.getPersonType().equals(EnumRegisterPersonType.PHYSICAL)) {
                        if (customerRegister.getContatoPrincipal() != null) {
                            if (!Util_IO.isNullOrEmpty(customerRegister.getContatoPrincipal().getNome()))
                                customerRegisterAddress.setContactName(customerRegister.getContatoPrincipal().getNome());

                            if (!Util_IO.isNullOrEmpty(customerRegister.getContatoPrincipal().getCelular()))
                                customerRegisterAddress.setCellphone(customerRegister.getContatoPrincipal().getCelular());

                            if (!Util_IO.isNullOrEmpty(customerRegister.getContatoPrincipal().getTelefone()))
                                customerRegisterAddress.setPhoneNumber(customerRegister.getContatoPrincipal().getTelefone());

                            if (!Util_IO.isNullOrEmpty(customerRegister.getContatoPrincipal().getEmail()))
                                customerRegisterAddress.setEmail(customerRegister.getContatoPrincipal().getEmail());
                        }
                    }
                    else
                    {
                        if (customerRegister.getPartners() != null) {
                            if (!Util_IO.isNullOrEmpty(customerRegister.getPartners().getNome()))
                                customerRegisterAddress.setContactName(customerRegister.getPartners().getNome());

                            if (!Util_IO.isNullOrEmpty(customerRegister.getPartners().getCelular()))
                                customerRegisterAddress.setCellphone(customerRegister.getPartners().getCelular());

                            if (!Util_IO.isNullOrEmpty(customerRegister.getPartners().getTelefone()))
                                customerRegisterAddress.setPhoneNumber(customerRegister.getPartners().getTelefone());

                            if (!Util_IO.isNullOrEmpty(customerRegister.getPartners().getEmail()))
                                customerRegisterAddress.setEmail(customerRegister.getPartners().getEmail());
                        }

                    }
                    return customerRegisterAddress;
                })
                .toList());

        // Adiciona os dados dos Sócios
        addSocios((int) id, customerRegister.getPartners());

        // Adiciona HorarioFuncionamento
        addHorarioFunc((int) id, Stream.ofNullable(customerRegister.getHorarioFunc())
                .map(customerRegisterHorarioFunc -> {
                    customerRegisterHorarioFunc.setIdCadastro((int) id);
                    return customerRegisterHorarioFunc;
                })
                .toList());

        // Adiciona Contatos
        addContatos((int) id, Stream.ofNullable(customerRegister.getContatos())
                .map(customerRegisterContatos -> {
                    customerRegisterContatos.setIdCadastro((int) id);
                    return customerRegisterContatos;
                })
                .toList());

        // Adiciona os dados do Contato Principal
        addContatoPrincipal((int) id, customerRegister.getContatoPrincipal());
    }

    private void addContatoPrincipal(int id, CustomerRegisterContatoPrincipal contatoPrincipal) {
        if (contatoPrincipal == null)
            return;

        DBClienteCadastroContatoPrincipal dbClienteCadastroContatoPrincipal = new DBClienteCadastroContatoPrincipal(mContext);
        dbClienteCadastroContatoPrincipal.deleteByIdCadastro(id);
        contatoPrincipal.setIdCadastro(id);
        try {
            dbClienteCadastroContatoPrincipal.addDBClienteCadastroContatoPrincipal(contatoPrincipal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addContatos(int id, List<CustomerRegisterContato> contatoList) {
        if (contatoList == null || contatoList.isEmpty()) return;
        DBClienteCadastroContato dbClienteCadastroContato = new DBClienteCadastroContato(mContext);
        dbClienteCadastroContato.deleteByIdCadastro(id);
        Stream.of(contatoList).forEach(dbClienteCadastroContato::addDBClienteCadastroContato);
    }

    private void addHorarioFunc(int id, List<CustomerRegisterHorarioFunc> horarioFuncList) {
        if (horarioFuncList == null || horarioFuncList.isEmpty()) return;
        DBClienteCadastroHorarioFunc dbClienteCadastroHorarioFunc = new DBClienteCadastroHorarioFunc(mContext);
        dbClienteCadastroHorarioFunc.deleteByIdCadastro(id);
        Stream.of(horarioFuncList).forEach(dbClienteCadastroHorarioFunc::addClienteCadastroHoarioFunc);
    }

    private void addSocios(int id, CustomerRegisterPartners socios) {
        if (socios == null)
            return;

        DBClienteCadastroSocios dbClienteCadastroSocios = new DBClienteCadastroSocios(mContext);
        dbClienteCadastroSocios.deleteByIdCadastro(id);
        socios.setIdCadastro(id);
        try {
            dbClienteCadastroSocios.addClienteCadastroSocios(socios);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAnexo(AnexoRetorno pAnexoRetorno, String pLocalArquivo) {
        ContentValues values = new ContentValues();
        values.put("idCadastro", pAnexoRetorno.getIdCadastro());
        values.put("anexo", pLocalArquivo);
        values.put("tipo", pAnexoRetorno.getTipo());
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaAnexo, null, values);
    }

    private void addEnderecos(List<CustomerRegisterAddress> addressList) {
        if (addressList == null || addressList.isEmpty()) return;
        DBClienteCadastroEndereco dbClienteCadastroEndereco = new DBClienteCadastroEndereco(mContext);
        Stream.of(addressList).forEach(dbClienteCadastroEndereco::salvar);
    }

    private void addTaxes(long id, List<CustomerRegisterTax> taxList) {
        if (taxList == null || taxList.isEmpty()) return;
        DBClienteCadastroTaxa dbClienteCadastroTaxa = new DBClienteCadastroTaxa(mContext);
        dbClienteCadastroTaxa.deletaTudoPorId(id);
        Stream.of(taxList)
                .map(tax -> {
                    tax.setIdCustomerRegister((int) id);
                    return tax;
                })
                .forEach(dbClienteCadastroTaxa::salvar);
    }

    public void updateCadastro(CustomerRegister pClienteCadastro, String pSituacao) {
        ContentValues values = new ContentValues();
        values.put("dataatualizacao", Util_IO.dateTimeToString(pClienteCadastro.getDateUpdate(), Config.FormatDateTimeStringBanco));
        if (!pClienteCadastro.isConfirmed())
            values.put("sync", pSituacao);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCadastro, values, "[id]=?", new String[]{pClienteCadastro.getId()});
    }

    public String updateRetorno(RetCadCliente pRetCadCliente) {
        String cpfCnpj = null;

        ContentValues values = new ContentValues();
        values.put("idServer", pRetCadCliente.getId());
        values.put("sync", pRetCadCliente.getSituacao());
        values.put("retorno", pRetCadCliente.getRetorno());
        values.put("dataatualizacao", Util_IO.dateTimeToString(pRetCadCliente.getData(), Config.FormatDateTimeStringBanco));
        values.put("versaoApp", BuildConfig.VERSION_NAME);
        values.put("codigoSgv", pRetCadCliente.getCodigoSGV());

        try {
            SimpleDbHelper.INSTANCE.open(mContext)
                    .update(
                            mTabelaCadastro,
                            values,
                            "[id] = ? AND [cpfcnpj] = ?",
                            new String[]{pRetCadCliente.getIdAppMobile(), pRetCadCliente.getCpfCnpj()}
                    );

            String query = "SELECT id, nome, cpfcnpj FROM [ClienteCadastro] WHERE id = ? AND cpfcnpj = ?";
            Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext)
                    .rawQuery(query,
                            new String[]{pRetCadCliente.getIdAppMobile(), pRetCadCliente.getCpfCnpj()});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                cpfCnpj = cursor.getString(2);
                String nomeCliente = cursor.getString(1);
                if (Utilidades.verificarHorarioComercial(mContext, false)) {
                    Notificacoes.Cadastro("Alteração no Cadastro de Cliente!",
                            cursor.getInt(0), this.mContext, nomeCliente, false);
                }
            }

            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception ex) {
            Timber.e(ex);
        }

        return cpfCnpj;
    }

    public void updateSync(String pId, int pSituacao) {
        ContentValues values = new ContentValues();
        values.put("sync", pSituacao);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCadastro, values, "[id]=?", new String[]{pId});
    }

    public void updateTokenConfirmation(final int customerId) {
        final ContentValues values = new ContentValues();
        values.put("confirmado", Util_IO.booleanToNumber(true));
        values.put("sync", 0);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaCadastro, values, "[id] = ?", new String[]{String.valueOf(customerId)});
    }

    public void updateAnexo(String pId) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAnexo, values, "[id]=?", new String[]{pId});
    }

    public ArrayList<CustomerRegister> getClientes(String pFantasia) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        if (!Util_IO.isNullOrEmpty(pFantasia))
            sb.appendLine("AND t0.[fantasia] LIKE '%" + pFantasia + "%'");
        boolean apenasPosAtivas = true;
        return getClienteCadastro(sb.toString(), null, apenasPosAtivas);
    }

    public ArrayList<CustomerRegister> getClientesWithFilter(String pFantasia) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        if (!Util_IO.isNullOrEmpty(pFantasia)) {
            sb.appendLine("AND (t0.[cpfcnpj] LIKE '%" + pFantasia + "%'");
            sb.appendLine(" OR t0.[nome] LIKE '%" + pFantasia + "%'");
            sb.appendLine(" OR t0.[fantasia] LIKE '%" + pFantasia + "%')");
        }
        boolean apenasPosAtivas = false;
        return getClienteCadastro(sb.toString(), null, apenasPosAtivas);
    }

    public CustomerRegister getClientesById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[id] = ?");
        boolean apenasPosAtivas = true;
        return Utilidades.firstOrDefault(getClienteCadastro(sb.toString(), new String[]{pId}, apenasPosAtivas));
    }

    public ArrayList<ClienteCadastroAnexo> getAnexos(String pIdCadastro) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[tipo]");
        sb.appendLine(",[anexo] AS imagem");
        sb.appendLine(",IFNULL([situacao],0) AS situacao");
        sb.appendLine(",UPPER(IFNULL([retorno],'')) AS mensagem");
        sb.appendLine(",'N' AS editar");
        sb.appendLine(",[idCadastro]");
        sb.appendLine(",[latitude]");
        sb.appendLine(",[longitude]");
        sb.appendLine(",[precisao]");
        sb.appendLine(",[tamanhoArquivo]");
        sb.appendLine("FROM [ClienteCadastroAnexo]");
        sb.appendLine("WHERE 1=1");
        sb.appendLine("AND [idCadastro] = ?");

        ArrayList<ClienteCadastroAnexo> lista = new ArrayList<>();
        try {
            lista = Util_DB.RetornaLista(mContext, ClienteCadastroAnexo.class, sb.toString(), new String[]{pIdCadastro});
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return lista;
    }

    private ArrayList<CustomerRegisterAddress> getEnderecos(String pIdCadastro) {
        DBClienteCadastroEndereco dbClienteCadastroEndereco = new DBClienteCadastroEndereco(mContext);
        return dbClienteCadastroEndereco.pegarTodas(Integer.valueOf(pIdCadastro));
    }

    private ArrayList<CustomerRegisterTax> getTaxes(String pIdCadastro) {
        DBClienteCadastroTaxa dbClienteCadastroTaxa = new DBClienteCadastroTaxa(mContext);
        return dbClienteCadastroTaxa.pegarTodas(Integer.valueOf(pIdCadastro));
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCadastro, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAnexo, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaSocios, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete("ClienteCadastroContatos", null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete("ClienteCadastroHorarioFuncionamento", null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete("ClienteCadastroContatoPrincipal", null, null);
    }

    public void deleteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAnexo, "[idCadastro]=?", new String[]{pId});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCadastro, "[id]=?", new String[]{pId});
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaSocios, "[idCadastro]=?", new String[]{pId});
        SimpleDbHelper.INSTANCE.open(mContext).delete("ClienteCadastroContatos", "[idCadastro]=?", new String[]{pId});
        SimpleDbHelper.INSTANCE.open(mContext).delete("ClienteCadastroHorarioFuncionamento", "[idCadastro]=?", new String[]{pId});
        SimpleDbHelper.INSTANCE.open(mContext).delete("ClienteCadastroContatoPrincipal", "[idCadastro]=?", new String[]{pId});
    }

    public void deletarAnexoById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAnexo, "[id]=?", new String[]{pId});
    }

    public List<CustomerRegister> getClientesPendentes() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[sync] = 0");
        sb.appendLine("AND t0.[confirmado] = 0");

        boolean apenasPosAtivas = false;
        return Stream.ofNullable(getClienteCadastro(sb.toString(), null, apenasPosAtivas))
                .map(register -> {
                    if (StringUtils.isEmpty(register.getSgvCode())) {
                        register.setSgvCode(null);
                    }
                    return register;
                })
                .toList();
    }

    public List<CustomerRegister> getClientesPendentesSituacao99() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[sync] = 0");
        sb.appendLine("AND t0.[confirmado] = 1");
        boolean apenasPosAtivas = false;
        return Stream.ofNullable(getClienteCadastro(sb.toString(), null, apenasPosAtivas))
                .map(register -> {
                    if (StringUtils.isEmpty(register.getSgvCode())) {
                        register.setSgvCode(null);
                    }
                    return register;
                })
                .toList();
    }

    public List<CustomerRegister> getClientesRecadastroPendentes() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[sync] = 0");
        sb.appendLine("AND t0.[recadastro] = 1");

        boolean apenasPosAtivas = false;
        return Stream.ofNullable(getClienteCadastro(sb.toString(), null, apenasPosAtivas))
                .map(register -> {
                    if (StringUtils.isEmpty(register.getSgvCode())) {
                        register.setSgvCode(null);
                    }
                    return register;
                })
                .toList();
    }

    public ArrayList<ClienteCadastroAnexo> getAnexosPendentes() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t1.[id]");
        sb.appendLine(",t1.[tipo]");
        sb.appendLine(",t0.[cpfcnpj]");
        sb.appendLine(",t1.[anexo]");
        sb.appendLine(",(SELECT id FROM Colaborador) idVendedor");
        sb.appendLine(",t1.[idCadastro]");
        sb.appendLine(",t1.[latitude]");
        sb.appendLine(",t1.[longitude]");
        sb.appendLine(",t1.[precisao]");
        sb.appendLine("FROM [ClienteCadastro] t0");
        sb.appendLine("JOIN [ClienteCadastroAnexo] t1 ON (t0.[id] = t1.[idCadastro])");
        sb.appendLine("WHERE 1=1");
        sb.appendLine("AND t1.[sync] = 0");

        ArrayList<ClienteCadastroAnexo> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null)) {
            ClienteCadastroAnexo clienteCadastroAnexo;
            if (cursor.moveToFirst()) {
                do {
                    clienteCadastroAnexo = new ClienteCadastroAnexo();
                    clienteCadastroAnexo.setId(cursor.getString(0));
                    clienteCadastroAnexo.setTipo(cursor.getString(1));
                    clienteCadastroAnexo.setCpfcnpj(cursor.getString(2));
                    clienteCadastroAnexo.setImagem(cursor.getString(3));
                    clienteCadastroAnexo.setIdVendedor(cursor.getString(4));
                    clienteCadastroAnexo.setIdCadastro(cursor.getString(5));
                    clienteCadastroAnexo.setLatitude(cursor.getDouble(6));
                    clienteCadastroAnexo.setLongitude(cursor.getDouble(7));
                    clienteCadastroAnexo.setPrecisao(cursor.getDouble(8));
                    lista.add(clienteCadastroAnexo);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return lista;
    }

    public int retornaCodigoCadastro(String pCpfCnpj) {
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext)
                .query(
                        mTabelaCadastro,
                        new String[]{"id"},
                        "[cpfcnpj]=?",
                        new String[]{pCpfCnpj},
                        null,
                        null,
                        null,
                        null
                )
        ) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getInt(0);
            }
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return 0;
    }

    public boolean verificaAnexo(AnexoRetorno pAnexoRetorno) {
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext)
                .query(
                        mTabelaAnexo,
                        new String[]{"id"},
                        "tipo=? AND idCadastro=?",
                        new String[]{pAnexoRetorno.getTipo(), pAnexoRetorno.getIdCadastro()},
                        null,
                        null,
                        null,
                        null
                )
        ) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            Timber.e(ex);
            return false;
        }
    }

    public void deletaAnexoDuplicados() {
        Cursor cursor = null, cursor2 = null;
        try {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("SELECT [idCadastro], [tipo]");
            sb.appendLine("FROM [ClienteCadastroAnexo]");
            sb.appendLine("GROUP BY [idCadastro], [tipo]");
            sb.appendLine("HAVING COUNT(1) > 1");
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    sb = new Util_IO.StringBuilder();
                    sb.appendLine("SELECT MIN(id)");
                    sb.appendLine("FROM [ClienteCadastroAnexo]");
                    sb.appendLine("WHERE [idCadastro] = ?");
                    sb.appendLine("AND [tipo] = ?");
                    cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(cursor.getInt(0)), cursor.getString(1)});
                    if (cursor2 != null && cursor2.getCount() > 0) {
                        cursor2.moveToFirst();
                        String query = "SELECT [id], [anexo] FROM [ClienteCadastroAnexo] WHERE [id] = ?";
                        cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(query, new String[]{String.valueOf(cursor2.getInt(0))});
                        if (cursor2 != null && cursor2.getCount() > 0) {
                            if (cursor2.getString(1).contains("/Files/Compressed"))
                                Utilidades.deletaArquivo(cursor.getString(1));
                            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAnexo, "[id]=? ", new String[]{cursor.getString(0)});
                        }
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
        }
    }

    public void deleteOk() {
        Cursor cursor = null, cursor2 = null;
        try {
            String query = "SELECT [id] FROM [ClienteCadastro] WHERE dataatualizacao < datetime('now', '-90 day') AND sync IN (2,6,7,11,10)";
            Util_IO.StringBuilder sb;
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(query, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        sb = new Util_IO.StringBuilder();
                        sb.appendLine("SELECT id, anexo FROM [ClienteCadastroAnexo]");
                        sb.appendLine("WHERE [idCadastro] = ?");
                        cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{cursor.getString(0)});
                        if (cursor2 != null) {
                            if (cursor2.moveToFirst()) {
                                do {
                                    if (cursor2.getString(1).contains("/Files/Compressed"))
                                        Utilidades.deletaArquivo(cursor2.getString(1));
                                    SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAnexo, "[id]=? ", new String[]{cursor2.getString(0)});
                                } while (cursor2.moveToNext());
                            }
                            cursor2.close();
                        }
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaCadastro, "[id]=? ", new String[]{cursor.getString(0)});
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
        }
    }

    public void updateRetornoAnexo(AnexoRetorno pAnexoRetorno) {
        ContentValues values = new ContentValues();
        values.put("situacao", pAnexoRetorno.getSituacao());
        values.put("retorno", pAnexoRetorno.getRetorno());
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAnexo, values, "[tipo]=? AND [idCadastro]=?", new String[]{pAnexoRetorno.getTipo(), pAnexoRetorno.getIdCadastro()});
    }

    private ArrayList<CustomerRegister> getClienteCadastro(String pCondicao, String[] pCampos, boolean apenasAtivas) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.[id]");
        sb.appendLine(",t0.[idServer]");
        sb.appendLine(",t0.[datacadastro]");
        sb.appendLine(",t0.[dataatualizacao]");
        sb.appendLine(",(SELECT id FROM Colaborador) idVendedor");
        sb.appendLine(",IFNULL(t0.[latitude],0)");
        sb.appendLine(",IFNULL(t0.[longitude],0)");
        sb.appendLine(",IFNULL(t0.[precisao],0)");
        sb.appendLine(",IFNULL(t0.[versaoApp],'" + BuildConfig.VERSION_NAME + "')");
        sb.appendLine(",IFNULL(t0.[sync],0)");
        sb.appendLine(",IFNULL(t0.[recadastro],0) AS recadastro");
        sb.appendLine(",t0.[retorno]");
        sb.appendLine(",t0.[tipoCliente]");
        sb.appendLine(",t0.[tipo]");
        sb.appendLine(",IFNULL(t0.[mcc], '')");
        sb.appendLine(",IFNULL(t0.[nome], '')");
        sb.appendLine(",IFNULL(t0.[fantasia],'')");
        sb.appendLine(",t0.[cpfcnpj]");
        sb.appendLine(",t0.[rg_Ie]");
        sb.appendLine(",t0.[datanascimento]");
        sb.appendLine(",IFNULL(t0.[segmento],0)");
        sb.appendLine(",t0.[nomeSocio]");
        sb.appendLine(",t0.[cpfSocio]");
        sb.appendLine(",t0.[dtnascimentoSocio]");
        sb.appendLine(",IFNULL(t0.[codigoSgv],'')");
        sb.appendLine(",IFNULL(t0.[tipoContaId], '')");
        sb.appendLine(",IFNULL(t0.[banco],0)");
        sb.appendLine(",IFNULL(t0.[agencia],'')");
        sb.appendLine(",IFNULL(t0.[digitoAgencia], '')");
        sb.appendLine(",IFNULL(t0.[conta],'')");
        sb.appendLine(",IFNULL(t0.[digitoConta],'')");
        sb.appendLine(",IFNULL(t0.[faturamentoMedioPrevisto], 0)");
        sb.appendLine(",IFNULL(t0.[antecipacao],0)");
        sb.appendLine(",IFNULL(t0.[idPrazoNegociacao],0)");
        sb.appendLine(",IFNULL(t0.[debitoAutomatico], 0)");
        sb.appendLine(",IFNULL(t0.[aluguelMaqVenc],0)");
        sb.appendLine(",IFNULL(t0.[isencao], 0)");
        sb.appendLine(",IFNULL(t0.[obs],'')");
        sb.appendLine(",IFNULL(t0.[confirmado], 0)");
        sb.appendLine(",IFNULL(t0.[idProfissao],'')");
        sb.appendLine(",IFNULL(t0.[idRenda],'')");
        sb.appendLine(",IFNULL(t0.[idPatrimonio],'')");
        sb.appendLine(",IFNULL(t0.[sexo],'')");
        sb.appendLine(",IFNULL(t0.[faturamentoBruto], 0)");
        sb.appendLine(",IFNULL(t0.[percVendaCartao], 0)");
        sb.appendLine(",IFNULL(t0.[percVendaEcommerce], 0)");
        sb.appendLine(",IFNULL(t0.[percFaturamento], 0)");
        sb.appendLine(",IFNULL(t0.[percEntregaImediata], 0)");
        sb.appendLine(",IFNULL(t0.[percEntregaPosterior], 0)");
        sb.appendLine(",IFNULL(t0.[prazoEntrega], 0)");
        sb.appendLine(",IFNULL(t0.[entregaPosCompra],'')");
        sb.appendLine(",t0.[dataFundacaoPF]");
        sb.appendLine(",t0.[IdSolPid_Server]");
        sb.appendLine("FROM [ClienteCadastro] t0");
        sb.appendLine("WHERE 1=1");

        if (pCondicao != null) {
            sb.append(pCondicao);
        }

        ArrayList<CustomerRegister> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos)) {
            if (cursor.moveToFirst()) {
                do {
                    lista.add(converter(cursor, apenasAtivas));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return lista;
    }

    private CustomerRegister converter(Cursor cursor, boolean apenasAtivas) {
        DBClienteCadastroPOS dbClienteCadastroPOS = new DBClienteCadastroPOS(mContext);
        DBTipoConta dbTipoConta = new DBTipoConta(mContext);
        DBClienteCadastroHorarioFunc dbClienteCadastroHorarioFunc = new DBClienteCadastroHorarioFunc(mContext);
        DBClienteCadastroSocios dbClienteCadastroSocios = new DBClienteCadastroSocios(mContext);
        DBClienteCadastroContato dbClienteCadastroContato = new DBClienteCadastroContato(mContext);
        DBClienteCadastroContatoPrincipal dbClienteCadastroContatoPrincipal = new DBClienteCadastroContatoPrincipal(mContext);

        CustomerRegister customerRegister = new CustomerRegister();
        customerRegister.setId(cursor.getString(0));
        customerRegister.setIdServer(getInteger(cursor, 1));
        final String registerDate = cursor.getString(2);
        customerRegister.setDateRegister(Util_IO.stringToDate(registerDate, Config.FormatDateTimeStringBanco));
        customerRegister.setDateUpdate(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
        customerRegister.setIdSalesman(cursor.getString(4));
        customerRegister.setLatitude(cursor.getDouble(5));
        customerRegister.setLongitude(cursor.getDouble(6));
        customerRegister.setPrecision(cursor.getDouble(7));
        customerRegister.setApplicationVersion(cursor.getString(8));
        customerRegister.setSync(cursor.getInt(9));
        customerRegister.setReRegister(Util_IO.stringToBoolean(cursor.getString(10)));
        customerRegister.setReturnValue(cursor.getString(11));
        customerRegister.setCustomerType(EnumRegisterCustomerType.getEnumByDescription(cursor.getString(12)));
        customerRegister.setPersonType(EnumRegisterPersonType.getEnumByCharValue(cursor.getString(13)));
        customerRegister.setMcc(cursor.getInt(14));
        customerRegister.setFullName(cursor.getString(15));
        customerRegister.setFantasyName(cursor.getString(16));
        customerRegister.setCpfCnpj(cursor.getString(17));
        customerRegister.setRgIe(cursor.getString(18));
        customerRegister.setBirthdate(Util_IO.stringToDate(cursor.getString(19), Config.FormatDateStringBanco));
        customerRegister.setSegment(cursor.getInt(20));
        customerRegister.setPartnerName(cursor.getString(21));
        customerRegister.setPartnerCpf(cursor.getString(22));
        customerRegister.setPartnerBirthday(Util_IO.stringToDate(cursor.getString(23), Config.FormatDateStringBanco));
        customerRegister.setSgvCode(cursor.getString(24));
        customerRegister.setAccountType(cursor.getInt(25));
        customerRegister.setBank(cursor.getInt(26));
        customerRegister.setBankAgency(cursor.getString(27));
        customerRegister.setBankAgencyDigit(cursor.getString(28));
        customerRegister.setBankAccount(cursor.getString(29));
        customerRegister.setBankAccountDigit(cursor.getString(30));
        customerRegister.setForeseenRevenue(cursor.getDouble(31));
        customerRegister.setAnticipation(Util_IO.stringToBoolean(cursor.getString(32)));
        customerRegister.setNegotiationTermId(cursor.getInt(33));
        customerRegister.setDebitAutomatic(Util_IO.stringToBoolean(cursor.getString(34)));
        customerRegister.setRentalMachineDue(cursor.getString(35));
        customerRegister.setExemption(cursor.getInt(36));
        customerRegister.setObservation(cursor.getString(37));
        customerRegister.setConfirmed(Util_IO.numberToBoolean(cursor.getInt(38)));
        customerRegister.setIdProfissao(cursor.getInt(39));
        customerRegister.setIdRenda(cursor.getInt(40));
        customerRegister.setIdPatrimonio(cursor.getInt(41));

        if (!Util_IO.isNullOrEmpty(cursor.getString(42))) {
            customerRegister.setSexo(EnumRegisterSexo.getEnumByCharValue(cursor.getString(42)));
        }

        customerRegister.setFaturamentoBruto(cursor.getDouble(43));
        customerRegister.setPercVendaCartao(cursor.getInt(44));
        customerRegister.setPercVendaEcommerce(cursor.getInt(45));
        customerRegister.setPercFaturamento(cursor.getInt(46));
        customerRegister.setPercEntregaImediata(cursor.getInt(47));
        customerRegister.setPercEntregaPosterior(cursor.getInt(48));
        customerRegister.setPrazoEntrega(cursor.getInt(49));

        if (!Util_IO.isNullOrEmpty(cursor.getString(50)))
            customerRegister.setEntregaPosCompra(cursor.getString(50));

        customerRegister.setDataFundacaoPF(Util_IO.stringToDate(cursor.getString(51), Config.FormatDateStringBanco));
        customerRegister.setIdSolPid_Server(getInteger(cursor, 52));

        customerRegister.setAddresses(getEnderecos(customerRegister.getId()));
        customerRegister.setTaxList(getTaxes(customerRegister.getId()));
        customerRegister.setPosList(Stream.ofNullable(dbClienteCadastroPOS
                        .obterPOS(Integer.valueOf(customerRegister.getId()), apenasAtivas))
                .map(MachineType::new)
                .toList());

        customerRegister.setAttachments(Stream.ofNullable(
                        getAnexos(customerRegister.getId()))
                .map(CustomerRegisterAttachment::new)
                .toList());

        if (!registerDate.isEmpty()) {
            final String onlyNumbersRegisterDate = StringUtils.returnOnlyNumbers(registerDate);
            customerRegister.setToken(
                    onlyNumbersRegisterDate.substring(onlyNumbersRegisterDate.length() - 5)
            );
        }

        customerRegister.setTipoConta(dbTipoConta.getById(customerRegister.getAccountType()));

        // Carrega Dados de Horário Funcionamento
        customerRegister.setHorarioFunc((ArrayList<CustomerRegisterHorarioFunc>) Stream.ofNullable(dbClienteCadastroHorarioFunc
                        .getByIdCadastro(Integer.valueOf(customerRegister.getId())))
                .map(CustomerRegisterHorarioFunc::new)
                .toList());

        // Carrega Dados dos Contatos
        customerRegister.setContatos((ArrayList<CustomerRegisterContato>) Stream.ofNullable(dbClienteCadastroContato
                        .getByIdCadastro(Integer.valueOf(customerRegister.getId())))
                .map(CustomerRegisterContato::new)
                .toList());

        // Carrega Dados dos Sócios
        CustomerRegisterPartners customerRegisterPartners = new CustomerRegisterPartners();
        customerRegisterPartners = dbClienteCadastroSocios.getByIdCadastro(Integer.valueOf(customerRegister.getId()));
        customerRegister.setPartners(customerRegisterPartners);

        // Carrega Dados do Contato Principal
        CustomerRegisterContatoPrincipal customerRegisterContatoPrincipal = new CustomerRegisterContatoPrincipal();
        customerRegisterContatoPrincipal = dbClienteCadastroContatoPrincipal.getByIdCadastro(Integer.valueOf(customerRegister.getId()));
        customerRegister.setContatoPrincipal(customerRegisterContatoPrincipal);

        return customerRegister;
    }

    public boolean verificaCadastro(String pCpfCnpj, String tipoCliente) {
        String adquirencia = "IN ('Solver','Adquirencia')";
        String fisico = "IN ('Fisico', 'Eletronico','Solver','Adquirencia')";
        String eletronico = "IN ('Eletronico','Solver','Adquirencia')";
        String filtro = "";
        if (EnumRegisterCustomerType.ACQUISITION.getDescriptionValue().equalsIgnoreCase(tipoCliente)) {
            filtro = adquirencia;
        }
        if (EnumRegisterCustomerType.PHYSICAL.getDescriptionValue().equalsIgnoreCase(tipoCliente)) {
            filtro = fisico;
        }
        if (EnumRegisterCustomerType.ELECTRONIC.getDescriptionValue().equalsIgnoreCase(tipoCliente)) {
            filtro = eletronico;
        }

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext)
                .query(
                        mTabelaCadastro,
                        new String[]{"id"},
                        "[cpfcnpj]=? AND [tipoCliente] " + filtro,
                        new String[]{pCpfCnpj},
                        null,
                        null,
                        null,
                        null
                )
        ) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            Timber.e(ex);
            return false;
        }
    }

    private Integer getInteger(Cursor cursor, int coluna) {
        int integer = cursor.getInt(coluna);
        return integer == 0 ? null : integer;
    }

    public boolean existeCadastroPorTipoConta(Integer tipoConta) {
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext)
                .query(
                        mTabelaCadastro,
                        new String[]{"id"},
                        "[tipoContaId]=?",
                        new String[]{tipoConta.toString()},
                        null,
                        null,
                        null,
                        null
                )
        ) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            Timber.e(ex);
            return false;
        }
    }
}