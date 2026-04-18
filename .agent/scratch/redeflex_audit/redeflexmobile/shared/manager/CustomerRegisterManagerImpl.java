package com.axys.redeflexmobile.shared.manager;

import android.util.Log;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPid;
import com.axys.redeflexmobile.shared.bd.DBBancos;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastro;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBEstado;
import com.axys.redeflexmobile.shared.bd.DBFaixaDeFaturamentoMensal;
import com.axys.redeflexmobile.shared.bd.DBFaixaPatrimonial;
import com.axys.redeflexmobile.shared.bd.DBFaixaSalarial;
import com.axys.redeflexmobile.shared.bd.DBIsencao;
import com.axys.redeflexmobile.shared.bd.DBModeloPOS;
import com.axys.redeflexmobile.shared.bd.DBOperadora;
import com.axys.redeflexmobile.shared.bd.DBPrazoNegociacao;
import com.axys.redeflexmobile.shared.bd.DBProfissoes;
import com.axys.redeflexmobile.shared.bd.DBProspect;
import com.axys.redeflexmobile.shared.bd.DBSegmento;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.bd.DBTipoConta;
import com.axys.redeflexmobile.shared.bd.DBTipoLogradouro;
import com.axys.redeflexmobile.shared.bd.Operadora;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterDebitAutomatic;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterDueDate;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.Bancos;
import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ConsultaPessoaFisica;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.Estado;
import com.axys.redeflexmobile.shared.models.FaixaDeFaturamentoMensal;
import com.axys.redeflexmobile.shared.models.FaixaPatrimonial;
import com.axys.redeflexmobile.shared.models.FaixaSalarial;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.shared.models.PostConsultaCPF;
import com.axys.redeflexmobile.shared.models.Profissoes;
import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.TipoConta;
import com.axys.redeflexmobile.shared.models.TipoLogradouro;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterExemption;
import com.axys.redeflexmobile.shared.models.customerregister.PrazoNegociacao;
import com.axys.redeflexmobile.shared.models.customerregister.RegisterListItem;
import com.axys.redeflexmobile.shared.services.RegisterService;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

/**
 * @author Rogério Massa on 28/11/18.
 */

public class CustomerRegisterManagerImpl implements CustomerRegisterManager {

    private final DBCliente dbCliente;
    private final DBClienteCadastro dbClienteCadastro;
    private final DBTaxaMdr dbTaxaMdr;
    private final DBEstado dbEstado;
    private final DBBancos dbBancos;
    private final DBModeloPOS dbModeloPOS;
    private final DBProspect dbProspect;
    private final DBSegmento dbSegmento;
    private final DBIsencao dbIsencao;
    private final DBColaborador dbColaborador;
    private final DBOperadora dbOperadora;
    private final DBPrazoNegociacao dbPrazoNegociacao;
    private final RegisterService registerService;
    private final DBTipoConta dbTipoConta;
    private final DBProfissoes dbProfissoes;
    private final DBFaixaSalarial dbFaixaSalarial;
    private final DBFaixaPatrimonial dbFaixaPatrimonial;
    private final DBFaixaDeFaturamentoMensal dbFaixaDeFaturamentoMensal;
    private final DBTipoLogradouro dbTipoLogradouro;
    private final BDSolicitacaoPid dbSolicitacaoPid;

    public CustomerRegisterManagerImpl(DBCliente dbCliente,
                                       DBClienteCadastro dbClienteCadastro,
                                       DBTaxaMdr dbTaxaMdr,
                                       DBEstado dbEstado,
                                       DBBancos dbBancos,
                                       DBModeloPOS dbModeloPOS,
                                       DBProspect dbProspect,
                                       DBSegmento dbSegmento,
                                       DBIsencao dbIsencao,
                                       DBColaborador dbColaborador,
                                       DBOperadora dbOperadora,
                                       DBPrazoNegociacao dbPrazoNegociacao,
                                       RegisterService registerService,
                                       DBTipoConta dbTipoConta,
                                       DBProfissoes dbProfissoes,
                                       DBFaixaSalarial dbFaixaSalarial,
                                       DBFaixaPatrimonial dbFaixaPatrimonial,
                                       DBFaixaDeFaturamentoMensal dbFaixaDeFaturamentoMensal,
                                       DBTipoLogradouro dbTipoLogradouro,
                                       BDSolicitacaoPid dbSolicitacaoPid) {
        this.dbCliente = dbCliente;
        this.dbClienteCadastro = dbClienteCadastro;
        this.dbTaxaMdr = dbTaxaMdr;
        this.dbEstado = dbEstado;
        this.dbBancos = dbBancos;
        this.dbModeloPOS = dbModeloPOS;
        this.dbProspect = dbProspect;
        this.dbSegmento = dbSegmento;
        this.dbIsencao = dbIsencao;
        this.dbColaborador = dbColaborador;
        this.dbOperadora = dbOperadora;
        this.dbPrazoNegociacao = dbPrazoNegociacao;
        this.registerService = registerService;
        this.dbTipoConta = dbTipoConta;
        this.dbProfissoes = dbProfissoes;
        this.dbFaixaSalarial = dbFaixaSalarial;
        this.dbFaixaPatrimonial = dbFaixaPatrimonial;
        this.dbFaixaDeFaturamentoMensal = dbFaixaDeFaturamentoMensal;
        this.dbTipoLogradouro = dbTipoLogradouro;
        this.dbSolicitacaoPid = dbSolicitacaoPid;
    }

    @Override
    public Single<List<RegisterListItem>> getRegisters(String filter) {
        return Single.just(Stream.ofNullable(dbClienteCadastro.getClientesWithFilter(filter))
                .map(RegisterListItem::new)
                .toList());
    }

    @Override
    public Single<CustomerRegister> getCustomerById(String idCustomer) {
        return Single.create(emitter -> {
            Cliente customer = dbCliente.getById(idCustomer);
            if (customer != null) {
                emitter.onSuccess(new CustomerRegister(customer));
            } else {
                emitter.onError(new IllegalArgumentException("Registros do cliente não encontrado"));
            }
        });
    }

    @Override
    public Single<CustomerRegister> getCustomerRegisterById(int idCustomer) {
        return Single.create(emitter -> {
            CustomerRegister customer = dbClienteCadastro.getClientesById(String.valueOf(idCustomer));
            if (customer != null) {
                emitter.onSuccess(customer);
            } else {
                emitter.onError(new IllegalArgumentException("Registros do cliente não encontrado"));
            }
        });
    }

    @Override
    public Single<CustomerRegister> getProspectById(int idProspect) {
        return Single.create(emitter -> {
            RouteClientProspect prospect = dbProspect.pegarPorId(idProspect);
            if (prospect != null) {
                emitter.onSuccess(new CustomerRegister(prospect));
            } else {
                emitter.onError(new IllegalArgumentException("Registros do prospect não encontrado"));
            }
        });
    }

    @Override
    public Single<List<TaxaMdr>> getMccList(int personType) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                emitter.onSuccess(dbTaxaMdr.getAllMccByPersonType(personType));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<TaxaMdr>> getMccListByPersonAndIdNegotiationPeriod(int personType, int idPeriodNegotiation) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                emitter.onSuccess(dbTaxaMdr.getMccListByPersonAndIdNegotiationPeriod(personType, idPeriodNegotiation));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<Segmento>> getSegments() {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            emitter.onSuccess(dbSegmento.getSegmento());
        });
    }

    @Override
    public Single<Boolean> verifyRegister(String text, String customerType) {
        return Single.just(dbClienteCadastro.verificaCadastro(text, customerType));
    }

    @Override
    public Colaborador getSalesmanDirect() {
        return dbColaborador.get();
    }

    @Override
    public Single<TaxaMdr> getAcquisitionTaxFromMcc(String mcc) {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            TaxaMdr taxa = dbTaxaMdr.pegarPorMcc(mcc);
            if (taxa == null) {
                emitter.onError(new IllegalArgumentException("Taxa não encontrada a partir do mcc selecionado"));
                return;
            }
            emitter.onSuccess(taxa);
        });
    }

    @Override
    public Single<ConsultaReceita> queryCnpj(String cnpj) {
        return registerService.queryCnpj(cnpj)
                .onErrorResumeNext(throwable -> Single.just(new ConsultaReceita()));
    }

    @Override
    public Single<ConsultaPessoaFisica> getPessoaFisica(String cnpj, String datanascimento) {
        try {
            PostConsultaCPF postConsultaCPF = new PostConsultaCPF();
            postConsultaCPF.setCpfCnpj(cnpj);
            postConsultaCPF.setBirth_date(datanascimento);

            return registerService.getPessoaFisica(postConsultaCPF)
                    .flatMap(pessoaFisica ->
                            Single.just(Stream.ofNullable(pessoaFisica)
                                    .findFirst()
                                    .orElse(new ConsultaPessoaFisica())));

        } catch (Exception ex) {
            Log.d("Roni", "getPessoaFisica: " + ex.getMessage());
            return null;
        }
    }

    @Override
    public Single<List<Estado>> getStates() {
        return Single.just(dbEstado.getEstado());
    }

    @Override
    public Single<Cep> getAddress(String postalCode) {
        return registerService.getAddress(postalCode)
                .flatMap(postalCodes ->
                        Single.just(Stream.ofNullable(postalCodes)
                                .findFirst()
                                .orElse(new Cep())));
    }

    @Override
    public Single<List<TipoConta>> getAccountTypes() {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            emitter.onSuccess(dbTipoConta.getAllActive());
        });
    }

    @Override
    public Single<List<Bancos>> getBanks() {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            emitter.onSuccess(dbBancos.getBancos());
        });
    }

    @Override
    public Single<Bancos> getBankById(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                Bancos bancos = dbBancos.getById(id);
                if (bancos == null) {
                    emitter.onError(new IllegalStateException("Banco não existe"));
                }
                emitter.onSuccess(dbBancos.getById(id));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<ModeloPOS>> getPOSModels() {
        return Single.just(dbModeloPOS.obterModelosPOS());
    }

    @Override
    public Single<List<PrazoNegociacao>> getNegotiationTerms() {
        return Single.create(emitter -> emitter.onSuccess(dbPrazoNegociacao.pegarTodas()));
    }

    @Override
    public Single<List<Operadora>> getCarriers() {
        return Single.just(dbOperadora.buscaOperadoras());
    }

    @Override
    public Single<List<EnumRegisterDueDate>> getDueList() {
        return Single.just(EnumRegisterDueDate.getDueDateList());
    }

    @Override
    public Single<List<EnumRegisterAnticipation>> getAnticipation(EnumRegisterPersonType type) {
        return Single.create(emitter -> emitter.onSuccess(EnumRegisterAnticipation.getEnumList()));
    }

    @Override
    public Single<List<TaxaMdr>> getTaxList(CustomerRegister customerRegister) {
        return Single.create(emitter -> {
            List<TaxaMdr> taxList = dbTaxaMdr.getAllTaxFlagTypes(
                    customerRegister.getPersonType().getIdValue(),
                    customerRegister.getForeseenRevenue(),
                    customerRegister.getNegotiationTermId(),
                    customerRegister.getMcc()
            );

            if (taxList.isEmpty() || taxList.size() < 3) {
                emitter.onError(new IllegalArgumentException("Não foram encontradas taxas para os parâmetros informados."));
            }
            emitter.onSuccess(taxList);
        });
    }

    @Override
    public Single<List<CustomerRegisterExemption>> getExemptionList() {
        return Single.just(dbIsencao.pegarTodas());
    }

    @Override
    public Single<List<EnumRegisterDebitAutomatic>> getDebitAutomatic() {
        return Single.just(EnumRegisterDebitAutomatic.getEnumList());
    }

    @Override
    public Completable saveCustomerRegister(CustomerRegister customerRegister) {
        return Completable.create(emitter -> {
            try {
                dbClienteCadastro.addCadastro(customerRegister);
                emitter.onComplete();
            } catch (Exception e) {
                Timber.e(e);
                emitter.onError(new IllegalStateException("Não foi possível salvar o cadastro", e));
            }
        });
    }

    @Override
    public Completable saveTokenConfirmation(final int customerId) {
        dbClienteCadastro.updateTokenConfirmation(customerId);
        return Completable.complete();
    }

    @Override
    public Single<TipoConta> getAccountType(int id) {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            emitter.onSuccess(dbTipoConta.getById(id));
        });
    }

    @Override
    public Single<List<Profissoes>> getProfissoes() {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            emitter.onSuccess(dbProfissoes.getProfissoes());
        });
    }

    @Override
    public Single<List<FaixaSalarial>> getRenda() {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            emitter.onSuccess(dbFaixaSalarial.getFaixaSalarial());
        });
    }

    @Override
    public Single<List<FaixaPatrimonial>> getPatrimonio() {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            emitter.onSuccess(dbFaixaPatrimonial.getFaixaPatrimonial());
        });
    }

    @Override
    public Single<List<FaixaDeFaturamentoMensal>> getFaixaDeFaturamentoMensal() {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            emitter.onSuccess(dbFaixaDeFaturamentoMensal.getFaixaDeFaturamentoMensal());
        });
    }

    @Override
    public Single<List<TipoLogradouro>> getTipoLogradouro() {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            emitter.onSuccess(dbTipoLogradouro.getTipoLogradouro());
        });
    }

    @Override
    public Single<CustomerRegister> getDadosCliente(String pCpfCnpj) {
        return registerService.getDadosCliente(pCpfCnpj)
                .flatMap(retorno ->
                            Single.just(Stream.ofNullable(retorno)
                                    .findFirst()
                                    .orElse(new CustomerRegister())));
    }

    @Override
    public Single<CustomerRegister> getSolicitacaoPid(int idSolPidServer) {
        return Single.create(emitter -> {
            SolicitacaoPid solicitacaoPid = dbSolicitacaoPid.getSolicitacaoPidIdServer(idSolPidServer);
            if (solicitacaoPid != null) {
                emitter.onSuccess(new CustomerRegister(solicitacaoPid));
            } else {
                emitter.onError(new IllegalArgumentException("Registros da Solicitacao Pid não encontrado"));
            }
        });
    }
}
