package com.axys.redeflexmobile.shared.models.customerregister;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType.ACQUISITION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType.PHYSICAL;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterSexo;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidTaxaMDR;
import com.axys.redeflexmobile.shared.models.TipoConta;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType.EnumRegisterAddressType;
import com.axys.redeflexmobile.shared.services.network.util.JsonExclude;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rogério Massa on 14/11/18.
 */

public class CustomerRegister {

    public static String RETORNO_DESBLOQUEIO_ID_TERMINAL = "LIBERADO PARA INSTALAÇÃO";

    @SerializedName("IdAppMobile") private String id;
    @SerializedName(value = "Id", alternate = {"id"}) private Integer idServer;
    @SerializedName("DataCadastro") private Date dateRegister;
    @SerializedName("DataAtualizacao") private Date dateUpdate;
    @SerializedName("IdVendedor") private String idSalesman;
    @SerializedName("Latitude") private Double latitude;
    @SerializedName("Longitude") private Double longitude;
    @SerializedName("Precisao") private Double precision;
    @SerializedName("VersaoApp") private String applicationVersion;
    @JsonExclude private Integer sync;
    @SerializedName("Recadastro") private boolean reRegister;
    @JsonExclude private String returnValue;

    //region PASSO 1
    @SerializedName("TipoCliente") private EnumRegisterCustomerType customerType;
    @SerializedName("TipoPessoa") private EnumRegisterPersonType personType;
    @SerializedName("Mcc") private Integer mcc;
    @SerializedName("Nome") private String fullName;
    @SerializedName("Fantasia") private String fantasyName;
    @SerializedName("CpfCnpj") private String cpfCnpj;
    @SerializedName("Rg_IE") private String rgIe;
    @SerializedName("DataNascimento") private Date birthdate;
    @SerializedName("IdSegmentoSGV") private Integer segment;
    @SerializedName("NomeSocio") private String partnerName;
    @SerializedName("CpfSocio") private String partnerCpf;
    @SerializedName("DataNascSocio") private Date partnerBirthday;
    @SerializedName("IdProfissao") private Integer IdProfissao;
    @SerializedName("IdRenda") private Integer IdRenda;
    @SerializedName("IdPatrimonio") private Integer IdPatrimonio;
    @SerializedName("Sexo") private EnumRegisterSexo Sexo;

    @SerializedName("DataFundacaoPF") private Date DataFundacaoPF;

    //endregion

    //region PASSO 2
    @SerializedName("Enderecos") private List<CustomerRegisterAddress> addresses;
    //endregion

    //region PASSO 3
    @SerializedName("CodigoSGV") private String sgvCode;
    @SerializedName("tipoContaId") private Integer accountType;
    private TipoConta tipoConta;
    @SerializedName("IdBanco") private Integer bank;
    @SerializedName("Agencia") private String bankAgency;
    @SerializedName("DigitoAgencia") private String bankAgencyDigit;
    @SerializedName("Conta") private String bankAccount;
    @SerializedName("DigitoConta") private String bankAccountDigit;
    //endregion

    //region PASSO 4
    @SerializedName("FaturamentoMedioPrevisto") private Double foreseenRevenue;
    @SerializedName("Antecipacao") private Boolean anticipation;
    private Double anticipationValue;
    @SerializedName("IdPrazoNegociacao") private Integer negotiationTermId;
    @SerializedName("DebitoAutomatico") private Boolean debitAutomatic;
    @SerializedName("Pos") private List<MachineType> posList;
    @SerializedName("Vencimento") private String rentalMachineDue;
    @SerializedName("IdPrazoIsencao") private Integer exemption;
    @SerializedName("Obs") private String observation;
    @SerializedName("Taxas") private List<CustomerRegisterTax> taxList;

    private Double faturamentoBruto;
    private Integer percVendaCartao;
    private Integer percVendaEcommerce;
    private Integer percFaturamento;
    private Integer percEntregaImediata;
    private Integer percEntregaPosterior;
    private Integer prazoEntrega;
    private String entregaPosCompra;

    private Integer IdSolPid_Server;

    //endregion

    //region PASSO 5
    @JsonExclude private List<CustomerRegisterAttachment> attachments = new ArrayList<>();
    //endregion

    // Dados dos Socios
    @SerializedName("Socios") private CustomerRegisterPartners partners;

    // Dados Horario Funcionamento
    @SerializedName("HorarioFuncionamento") private ArrayList<CustomerRegisterHorarioFunc> horarioFunc = new ArrayList<>();

    // Dados Horario Funcionamento
    @SerializedName("Contatos") private ArrayList<CustomerRegisterContato> contatos = new ArrayList<>();

    // Dados dos Socios
    @SerializedName("ContatoPrincipal") private CustomerRegisterContatoPrincipal contatoPrincipal = new CustomerRegisterContatoPrincipal();

    //region FIM CADASTRO
    @SerializedName("Token") private String token;
    @SerializedName("Confirmado") private boolean confirmed;
    //endregion

    public CustomerRegister() {
        this.customerType = ACQUISITION;
        this.personType = PHYSICAL;
        this.attachments = new ArrayList<>();
        this.posList = new ArrayList<>();
        this.addresses = new ArrayList<>();
        this.horarioFunc = new ArrayList<>();
    }

    public CustomerRegister(RouteClientProspect prospect) {
        this.customerType = EnumRegisterCustomerType.ACQUISITION;
        this.personType = StringUtils.isCnpj(prospect.getCpfCnpj())
                ? EnumRegisterPersonType.JURIDICAL
                : EnumRegisterPersonType.PHYSICAL;
        this.fullName = prospect.getNameFull();
        this.fantasyName = prospect.getNameFantasy();
        this.cpfCnpj = prospect.getCpfCnpj();
        this.latitude = prospect.getLatitude();
        this.longitude = prospect.getLongitude();

        CustomerRegisterAddress address = new CustomerRegisterAddress();
        address.setIdAddressType(EnumRegisterAddressType.MAIN.getValue());
        address.setPostalCode(prospect.getPostalCode());
        address.setAddressName(prospect.getAddressName());
        address.setAddressNumber(prospect.getAddressNumber());
        address.setAddressComplement(prospect.getAddressComplement());
        address.setAddressType(prospect.getAddressType());
        address.setCity(prospect.getCity());
        address.setFederalState(prospect.getFederalState());
        address.setPhoneNumber(prospect.getDddTelephone() + prospect.getTelephone());
        address.setCellphone(prospect.getDddCellphone() + prospect.getCellphone());
        address.setContactName(prospect.getContact());
        address.setEmail(prospect.getEmail());
        this.addresses = new ArrayList<>();
        this.addresses.add(address);
    }

    public CustomerRegister(CustomerRegister customerRegister) {
        this.id = customerRegister.getId();
        this.dateRegister = customerRegister.getDateRegister();
        this.dateUpdate = customerRegister.getDateUpdate();
        this.idSalesman = customerRegister.getIdSalesman();
        this.latitude = customerRegister.getLatitude();
        this.longitude = customerRegister.getLongitude();
        this.precision = customerRegister.getPrecision();
        this.applicationVersion = customerRegister.getApplicationVersion();
        this.sync = customerRegister.getSync();
        this.customerType = customerRegister.getCustomerType();
        this.personType = customerRegister.getPersonType();
        this.mcc = customerRegister.getMcc();
        this.fullName = customerRegister.getFullName();
        this.fantasyName = customerRegister.getFantasyName();
        this.cpfCnpj = customerRegister.getCpfCnpj();
        this.rgIe = customerRegister.getRgIe();
        this.birthdate = customerRegister.getBirthdate();
        this.segment = customerRegister.getSegment();
        this.addresses = customerRegister.getAddresses();
        this.sgvCode = customerRegister.getSgvCode();
        this.partnerCpf = customerRegister.getPartnerCpf();
        this.partnerName = customerRegister.getPartnerName();
        this.partnerBirthday = customerRegister.getPartnerBirthday();
        this.accountType = customerRegister.getAccountType();
        this.bank = customerRegister.getBank();
        this.bankAgency = customerRegister.getBankAgency();
        this.bankAgencyDigit = customerRegister.getBankAgencyDigit();
        this.bankAccount = customerRegister.getBankAccount();
        this.bankAccountDigit = customerRegister.getBankAccountDigit();
        this.foreseenRevenue = customerRegister.getForeseenRevenue();
        this.negotiationTermId = customerRegister.getNegotiationTermId();
        this.anticipation = customerRegister.getAnticipation();
        this.anticipationValue = customerRegister.getAnticipationValue();
        this.debitAutomatic = customerRegister.getDebitAutomatic();
        this.taxList = Stream.ofNullable(customerRegister.getTaxList())
                .map(CustomerRegisterTax::new)
                .toList();
        this.posList = customerRegister.getPosList();
        this.rentalMachineDue = customerRegister.getRentalMachineDue();
        this.exemption = customerRegister.getExemption();
        this.observation = customerRegister.getObservation();
        this.attachments = customerRegister.getAttachments();
        this.sync = customerRegister.getSync();
        this.IdProfissao = customerRegister.getIdProfissao();
        this.IdRenda = customerRegister.getIdRenda();
        this.IdPatrimonio = customerRegister.getIdPatrimonio();
        this.Sexo = customerRegister.getSexo();
        this.horarioFunc = customerRegister.getHorarioFunc();
        this.partners = customerRegister.getPartners();
        this.contatoPrincipal = customerRegister.getContatoPrincipal();
        this.faturamentoBruto = customerRegister.getFaturamentoBruto();
        this.percVendaCartao = customerRegister.getPercVendaCartao();
        this.percVendaEcommerce = customerRegister.getPercVendaEcommerce();
        this.percFaturamento = customerRegister.getPercFaturamento();
        this.percEntregaImediata = customerRegister.getPercEntregaImediata();
        this.percEntregaPosterior = customerRegister.getPercEntregaPosterior();
        this.prazoEntrega = customerRegister.getPrazoEntrega();
        this.entregaPosCompra = customerRegister.getEntregaPosCompra();
        this.DataFundacaoPF = customerRegister.getDataFundacaoPF();
    }

    public CustomerRegister(Cliente cliente) {
        this.fullName = cliente.getRazaoSocial();
        this.fantasyName = cliente.getNomeFantasia();
        this.rgIe = cliente.getRg_ie();
        this.cpfCnpj = cliente.getCpf_cnpj();
        this.personType = StringUtils.isCpfValid(cliente.getCpf_cnpj())
                ? EnumRegisterPersonType.PHYSICAL
                : EnumRegisterPersonType.JURIDICAL;

        if (cliente.isAdquirencia()) {
            this.customerType = EnumRegisterCustomerType.ACQUISITION;
        } else if (cliente.isEletronico()) {
            this.customerType = EnumRegisterCustomerType.ELECTRONIC;
        } else if (cliente.isFisico()) {
            this.customerType = EnumRegisterCustomerType.PHYSICAL;
        }

        CustomerRegisterAddress address = new CustomerRegisterAddress();
        address.setIdAddressType(EnumRegisterAddressType.MAIN.getValue());
        address.setPostalCode(cliente.getCep());
        address.setAddressName(cliente.getNomeLogradouro());
        address.setAddressNumber(cliente.getNumeroLogradouro());
        address.setAddressComplement(cliente.getComplementoLogradouro());
        address.setAddressType(cliente.getTipoLogradouro());
        address.setCity(cliente.getCidade());
        address.setFederalState(cliente.getEstado());
        address.setPhoneNumber(cliente.getDddTelefone() + cliente.getTelefone());
        address.setCellphone(cliente.getDddCelular() + cliente.getCelular());
        address.setContactName("-");
        address.setEmail(cliente.getEmailCliente());
        this.addresses = new ArrayList<>();
        this.addresses.add(address);
    }

    public CustomerRegister(SolicitacaoPid solicitacaoPid) {
        if (solicitacaoPid.getTipoCliente().equals("ISO"))
            this.customerType = EnumRegisterCustomerType.SUBADQUIRENCIA;
        else if (solicitacaoPid.getTipoCliente().equals("SUB"))
            this.customerType = EnumRegisterCustomerType.SUBADQUIRENCIA;
        else if (solicitacaoPid.getTipoCliente().equals("ADQ"))
            this.customerType = EnumRegisterCustomerType.ACQUISITION;

        this.personType = StringUtils.isCnpj(solicitacaoPid.getCpfCnpj())
                ? EnumRegisterPersonType.JURIDICAL
                : EnumRegisterPersonType.PHYSICAL;
        this.fullName = solicitacaoPid.getRazaoSocial();
        this.fantasyName = solicitacaoPid.getNomeFantasia();
        this.cpfCnpj = solicitacaoPid.getCpfCnpj();
        this.latitude = solicitacaoPid.getLatitude();
        this.longitude = solicitacaoPid.getLongitude();
        this.IdSolPid_Server = solicitacaoPid.getId_Server();
        this.mcc = solicitacaoPid.getMCCPrincipal();
        this.faturamentoBruto= solicitacaoPid.getFaturamentoPrevisto();
        this.foreseenRevenue = solicitacaoPid.getFaturamentoPrevisto();
        this.percFaturamento = 100;
        this.negotiationTermId = 2;

        // Carregar as Taxas Informadas na Solicitacao Pid
        ArrayList<CustomerRegisterTax> listTaxas = new ArrayList<>();
        for (SolicitacaoPidTaxaMDR item : solicitacaoPid.getListaTaxas()) {
            CustomerRegisterTax itemTaxa = new CustomerRegisterTax();

            if (item.getTaxaDebito().isPresent())
                itemTaxa.setDebit(item.getTaxaDebito().get());

            if (item.getTaxaCredito().isPresent())
                itemTaxa.setCredit(item.getTaxaCredito().get());

            if (item.getTaxaCredito6x().isPresent())
                itemTaxa.setUntilSix(item.getTaxaCredito6x().get());

            if (item.getTaxaCredito12x().isPresent())
                itemTaxa.setBiggerSix(item.getTaxaCredito12x().get());

            itemTaxa.setFlag(item.getBandeiraTipoId());

            if (item.getTaxaRavAutomatica().isPresent())
                itemTaxa.setAnticipation(item.getTaxaRavAutomatica().get());
            else if (item.getTaxaRavEventual().isPresent())
                itemTaxa.setAnticipation(item.getTaxaRavEventual().get());
            else
                itemTaxa.setAnticipation(0.00);
            listTaxas.add(itemTaxa);
        }
        this.taxList = Stream.ofNullable(listTaxas)
                .map(CustomerRegisterTax::new)
                .toList();

        if (listTaxas.size()>0)
        {
            if (listTaxas.get(0).getAnticipation() > 0) {
                this.anticipation = true;
                this.anticipationValue = listTaxas.get(0).getAnticipation();
            }
            else
            {
                this.anticipation = false;
                this.anticipationValue = 0.00;
            }
        }
    }

    //region CADASTRO
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdServer() {
        return idServer;
    }

    public void setIdServer(Integer idServer) {
        this.idServer = idServer;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(String idSalesman) {
        this.idSalesman = idSalesman;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getPrecision() {
        return precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public Integer getSync() {
        return sync;
    }

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public boolean isReRegister() {
        return reRegister;
    }

    public void setReRegister(boolean reRegister) {
        this.reRegister = reRegister;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }
    //endregion

    //region GET/SET PASSO 1
    public EnumRegisterCustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(EnumRegisterCustomerType customerType) {
        this.customerType = customerType;
    }

    public EnumRegisterPersonType getPersonType() {
        return personType;
    }

    public void setPersonType(EnumRegisterPersonType personType) {
        this.personType = personType;
    }

    public Integer getMcc() {
        return mcc;
    }

    public void setMcc(Integer mcc) {
        this.mcc = mcc;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getRgIe() {
        return rgIe;
    }

    public void setRgIe(String rgIe) {
        this.rgIe = rgIe;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getSegment() {
        return segment;
    }

    public void setSegment(Integer segment) {
        this.segment = segment;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerCpf() {
        return partnerCpf;
    }

    public void setPartnerCpf(String partnerCpf) {
        this.partnerCpf = partnerCpf;
    }

    public Date getPartnerBirthday() {
        return partnerBirthday;
    }

    public void setPartnerBirthday(Date partnerBirthday) {
        this.partnerBirthday = partnerBirthday;
    }

    //endregion

    //region GET/SET PASSO 2
    public List<CustomerRegisterAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<CustomerRegisterAddress> addresses) {
        this.addresses = addresses;
    }
    //endregion

    //region GET/SET PASSO 3
    public String getSgvCode() {
        return sgvCode;
    }

    public void setSgvCode(String sgvCode) {
        this.sgvCode = sgvCode;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getBank() {
        return bank;
    }

    public void setBank(Integer bank) {
        this.bank = bank;
    }

    public String getBankAgency() {
        return bankAgency;
    }

    public void setBankAgency(String bankAgency) {
        this.bankAgency = bankAgency;
    }

    public String getBankAgencyDigit() {
        return bankAgencyDigit;
    }

    public void setBankAgencyDigit(String bankAgencyDigit) {
        this.bankAgencyDigit = bankAgencyDigit;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankAccountDigit() {
        return bankAccountDigit;
    }

    public void setBankAccountDigit(String bankAccountDigit) {
        this.bankAccountDigit = bankAccountDigit;
    }
    //endregion

    //region GET/SET PASSO 4
    public Double getForeseenRevenue() {
        return foreseenRevenue;
    }

    public void setForeseenRevenue(Double foreseenRevenue) {
        this.foreseenRevenue = foreseenRevenue;
    }

    public Integer getNegotiationTermId() {
        return negotiationTermId;
    }

    public void setNegotiationTermId(Integer negotiationTermId) {
        this.negotiationTermId = negotiationTermId;
    }

    public Boolean getAnticipation() {
        return anticipation;
    }

    public void setAnticipation(Boolean anticipation) {
        this.anticipation = anticipation;
    }

    public Double getAnticipationValue() {
        return anticipationValue;
    }

    public void setAnticipationValue(Double anticipationValue) {
        this.anticipationValue = anticipationValue;
    }

    public String getRentalMachineDue() {
        return rentalMachineDue;
    }

    public void setRentalMachineDue(String rentalMachineDue) {
        this.rentalMachineDue = rentalMachineDue;
    }

    public Boolean getDebitAutomatic() {
        return debitAutomatic;
    }

    public void setDebitAutomatic(Boolean debitAutomatic) {
        this.debitAutomatic = debitAutomatic;
    }

    public List<MachineType> getPosList() {
        return posList;
    }

    public void setPosList(List<MachineType> posList) {
        this.posList = posList;
    }

    public Integer getExemption() {
        return exemption;
    }

    public void setExemption(Integer exemption) {
        this.exemption = exemption;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public List<CustomerRegisterTax> getTaxList() {
        return taxList;
    }

    public void setTaxList(List<CustomerRegisterTax> taxList) {
        this.taxList = taxList;
    }

    //endregion

    //region GET/SET PASSO 5
    public List<CustomerRegisterAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<CustomerRegisterAttachment> attachments) {
        this.attachments = attachments;
    }
    //endregion

    //region GET/SET FIM CADASTRO
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public CustomerRegisterPartners getPartners() {
        return partners;
    }

    public void setPartners(CustomerRegisterPartners partners) {
        this.partners = partners;
    }

    public Integer getIdProfissao() {
        return IdProfissao;
    }

    public void setIdProfissao(Integer idProfissao) {
        this.IdProfissao = idProfissao;
    }

    public Integer getIdRenda() {
        return IdRenda;
    }

    public void setIdRenda(Integer idRenda) {
        this.IdRenda = idRenda;
    }

    public Integer getIdPatrimonio() {
        return IdPatrimonio;
    }

    public void setIdPatrimonio(Integer idPatrimonio) {
        this.IdPatrimonio = idPatrimonio;
    }

    public EnumRegisterSexo getSexo() {
        return Sexo;
    }

    public void setSexo(EnumRegisterSexo sexo) {
        this.Sexo = sexo;
    }
    //endregion

    public ArrayList<CustomerRegisterHorarioFunc> getHorarioFunc() {
        return horarioFunc;
    }

    public void setHorarioFunc(ArrayList<CustomerRegisterHorarioFunc> horarioFunc) {
        this.horarioFunc = horarioFunc;
    }

    public ArrayList<CustomerRegisterContato> getContatos() {
        return contatos;
    }

    public void setContatos(ArrayList<CustomerRegisterContato> contatos) {
        this.contatos = contatos;
    }

    public CustomerRegisterContatoPrincipal getContatoPrincipal() {
        return contatoPrincipal;
    }

    public void setContatoPrincipal(CustomerRegisterContatoPrincipal contatoPrincipal) {
        this.contatoPrincipal = contatoPrincipal;
    }

    public Double getFaturamentoBruto() {
        return faturamentoBruto;
    }

    public void setFaturamentoBruto(Double faturamentoBruto) {
        this.faturamentoBruto = faturamentoBruto;
    }

    public Integer getPercVendaCartao() {
        return percVendaCartao;
    }

    public void setPercVendaCartao(Integer percVendaCartao) {
        this.percVendaCartao = percVendaCartao;
    }

    public Integer getPercVendaEcommerce() {
        return percVendaEcommerce;
    }

    public void setPercVendaEcommerce(Integer percVendaEcommerce) {
        this.percVendaEcommerce = percVendaEcommerce;
    }

    public Integer getPercFaturamento() {
        return percFaturamento;
    }

    public void setPercFaturamento(Integer percFaturamento) {
        this.percFaturamento = percFaturamento;
    }

    public Integer getPercEntregaImediata() {
        return percEntregaImediata;
    }

    public void setPercEntregaImediata(Integer percEntregaImediata) {
        this.percEntregaImediata = percEntregaImediata;
    }

    public Integer getPercEntregaPosterior() {
        return percEntregaPosterior;
    }

    public void setPercEntregaPosterior(Integer percEntregaPosterior) {
        this.percEntregaPosterior = percEntregaPosterior;
    }

    public Integer getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(Integer prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    public String getEntregaPosCompra() {
        return entregaPosCompra;
    }

    public void setEntregaPosCompra(String entregaPosCompra) {
        this.entregaPosCompra = entregaPosCompra;
    }

    public Date getDataFundacaoPF() {
        return DataFundacaoPF;
    }

    public void setDataFundacaoPF(Date dataFundacaoPF) {
        this.DataFundacaoPF = dataFundacaoPF;
    }

    public Integer getIdSolPid_Server() {
        return IdSolPid_Server;
    }

    public void setIdSolPid_Server(Integer idSolPid_Server) {
        IdSolPid_Server = idSolPid_Server;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerRegister that = (CustomerRegister) o;

        if (reRegister != that.reRegister) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idServer != null ? !idServer.equals(that.idServer) : that.idServer != null)
            return false;
        if (dateRegister != null ? !dateRegister.equals(that.dateRegister) : that.dateRegister != null)
            return false;
        if (dateUpdate != null ? !dateUpdate.equals(that.dateUpdate) : that.dateUpdate != null)
            return false;
        if (idSalesman != null ? !idSalesman.equals(that.idSalesman) : that.idSalesman != null)
            return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null)
            return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null)
            return false;
        if (precision != null ? !precision.equals(that.precision) : that.precision != null)
            return false;
        if (applicationVersion != null ? !applicationVersion.equals(that.applicationVersion) : that.applicationVersion != null)
            return false;
        if (sync != null ? !sync.equals(that.sync) : that.sync != null) return false;
        if (returnValue != null ? !returnValue.equals(that.returnValue) : that.returnValue != null)
            return false;
        if (customerType != that.customerType) return false;
        if (personType != that.personType) return false;
        if (mcc != null ? !mcc.equals(that.mcc) : that.mcc != null) return false;
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null)
            return false;
        if (fantasyName != null ? !fantasyName.equals(that.fantasyName) : that.fantasyName != null)
            return false;
        if (cpfCnpj != null ? !cpfCnpj.equals(that.cpfCnpj) : that.cpfCnpj != null) return false;
        if (rgIe != null ? !rgIe.equals(that.rgIe) : that.rgIe != null) return false;
        if (birthdate != null ? !birthdate.equals(that.birthdate) : that.birthdate != null)
            return false;
        if (segment != null ? !segment.equals(that.segment) : that.segment != null) return false;
        if (partnerName != null ? !partnerName.equals(that.partnerName) : that.partnerName != null)
            return false;
        if (partnerCpf != null ? !partnerCpf.equals(that.partnerCpf) : that.partnerCpf != null)
            return false;
        if (partnerBirthday != null ? !partnerBirthday.equals(that.partnerBirthday) : that.partnerBirthday != null)
            return false;
        if (addresses != null ? !addresses.equals(that.addresses) : that.addresses != null)
            return false;
        if (sgvCode != null ? !sgvCode.equals(that.sgvCode) : that.sgvCode != null) return false;
        if (accountType != that.accountType) return false;
        if (bank != null ? !bank.equals(that.bank) : that.bank != null) return false;
        if (bankAgency != null ? !bankAgency.equals(that.bankAgency) : that.bankAgency != null)
            return false;
        if (bankAgencyDigit != null ? !bankAgencyDigit.equals(that.bankAgencyDigit) : that.bankAgencyDigit != null)
            return false;
        if (bankAccount != null ? !bankAccount.equals(that.bankAccount) : that.bankAccount != null)
            return false;
        if (bankAccountDigit != null ? !bankAccountDigit.equals(that.bankAccountDigit) : that.bankAccountDigit != null)
            return false;
        if (foreseenRevenue != null ? !foreseenRevenue.equals(that.foreseenRevenue) : that.foreseenRevenue != null)
            return false;
        if (anticipation != null ? !anticipation.equals(that.anticipation) : that.anticipation != null)
            return false;
        if (anticipationValue != null ? !anticipationValue.equals(that.anticipationValue) : that.anticipationValue != null)
            return false;
        if (negotiationTermId != null ? !negotiationTermId.equals(that.negotiationTermId) : that.negotiationTermId != null)
            return false;
        if (debitAutomatic != null ? !debitAutomatic.equals(that.debitAutomatic) : that.debitAutomatic != null)
            return false;
        if (posList != null ? !posList.equals(that.posList) : that.posList != null) return false;
        if (rentalMachineDue != null ? !rentalMachineDue.equals(that.rentalMachineDue) : that.rentalMachineDue != null)
            return false;
        if (exemption != null ? !exemption.equals(that.exemption) : that.exemption != null)
            return false;
        if (observation != null ? !observation.equals(that.observation) : that.observation != null)
            return false;
        if (taxList != null ? !taxList.equals(that.taxList) : that.taxList != null) return false;
        return attachments != null ? attachments.equals(that.attachments) : that.attachments == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idServer != null ? idServer.hashCode() : 0);
        result = 31 * result + (dateRegister != null ? dateRegister.hashCode() : 0);
        result = 31 * result + (dateUpdate != null ? dateUpdate.hashCode() : 0);
        result = 31 * result + (idSalesman != null ? idSalesman.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (precision != null ? precision.hashCode() : 0);
        result = 31 * result + (applicationVersion != null ? applicationVersion.hashCode() : 0);
        result = 31 * result + (sync != null ? sync.hashCode() : 0);
        result = 31 * result + (reRegister ? 1 : 0);
        result = 31 * result + (returnValue != null ? returnValue.hashCode() : 0);
        result = 31 * result + (customerType != null ? customerType.hashCode() : 0);
        result = 31 * result + (personType != null ? personType.hashCode() : 0);
        result = 31 * result + (mcc != null ? mcc.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (fantasyName != null ? fantasyName.hashCode() : 0);
        result = 31 * result + (cpfCnpj != null ? cpfCnpj.hashCode() : 0);
        result = 31 * result + (rgIe != null ? rgIe.hashCode() : 0);
        result = 31 * result + (birthdate != null ? birthdate.hashCode() : 0);
        result = 31 * result + (segment != null ? segment.hashCode() : 0);
        result = 31 * result + (partnerName != null ? partnerName.hashCode() : 0);
        result = 31 * result + (partnerCpf != null ? partnerCpf.hashCode() : 0);
        result = 31 * result + (partnerBirthday != null ? partnerBirthday.hashCode() : 0);
        result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
        result = 31 * result + (sgvCode != null ? sgvCode.hashCode() : 0);
        result = 31 * result + (accountType != null ? accountType.hashCode() : 0);
        result = 31 * result + (bank != null ? bank.hashCode() : 0);
        result = 31 * result + (bankAgency != null ? bankAgency.hashCode() : 0);
        result = 31 * result + (bankAgencyDigit != null ? bankAgencyDigit.hashCode() : 0);
        result = 31 * result + (bankAccount != null ? bankAccount.hashCode() : 0);
        result = 31 * result + (bankAccountDigit != null ? bankAccountDigit.hashCode() : 0);
        result = 31 * result + (foreseenRevenue != null ? foreseenRevenue.hashCode() : 0);
        result = 31 * result + (anticipation != null ? anticipation.hashCode() : 0);
        result = 31 * result + (anticipationValue != null ? anticipationValue.hashCode() : 0);
        result = 31 * result + (negotiationTermId != null ? negotiationTermId.hashCode() : 0);
        result = 31 * result + (debitAutomatic != null ? debitAutomatic.hashCode() : 0);
        result = 31 * result + (posList != null ? posList.hashCode() : 0);
        result = 31 * result + (rentalMachineDue != null ? rentalMachineDue.hashCode() : 0);
        result = 31 * result + (exemption != null ? exemption.hashCode() : 0);
        result = 31 * result + (observation != null ? observation.hashCode() : 0);
        result = 31 * result + (taxList != null ? taxList.hashCode() : 0);
        result = 31 * result + (attachments != null ? attachments.hashCode() : 0);
        return result;
    }
}
