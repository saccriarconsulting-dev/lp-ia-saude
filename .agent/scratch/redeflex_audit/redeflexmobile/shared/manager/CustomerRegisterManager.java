package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.bd.Operadora;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterDebitAutomatic;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterDueDate;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.Bancos;
import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ConsultaPessoaFisica;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.Estado;
import com.axys.redeflexmobile.shared.models.FaixaDeFaturamentoMensal;
import com.axys.redeflexmobile.shared.models.FaixaPatrimonial;
import com.axys.redeflexmobile.shared.models.FaixaSalarial;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.shared.models.Profissoes;
import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.models.TipoConta;
import com.axys.redeflexmobile.shared.models.TipoLogradouro;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterExemption;
import com.axys.redeflexmobile.shared.models.customerregister.PrazoNegociacao;
import com.axys.redeflexmobile.shared.models.customerregister.RegisterListItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author Rogério Massa on 28/11/18.
 */

public interface CustomerRegisterManager {

    Single<List<RegisterListItem>> getRegisters(String filter);

    Single<CustomerRegister> getCustomerById(String idCustomer);

    Single<CustomerRegister> getCustomerRegisterById(int idCustomer);

    Single<CustomerRegister> getProspectById(int idProspect);

    Single<List<TaxaMdr>> getMccList(int personType);

    Single<List<TaxaMdr>> getMccListByPersonAndIdNegotiationPeriod(int personType, int idPeriodNegotiation);

    Single<List<Segmento>> getSegments();

    Single<Cep> getAddress(String postalCode);

    Single<ConsultaReceita> queryCnpj(String cnpj);

    Single<ConsultaPessoaFisica> getPessoaFisica(String cnpj, String datanascimento);

    Single<List<Estado>> getStates();

    Single<List<TipoConta>> getAccountTypes();

    Single<List<Bancos>> getBanks();

    Single<Bancos> getBankById(int id);

    Single<List<ModeloPOS>> getPOSModels();

    Single<List<PrazoNegociacao>> getNegotiationTerms();

    Single<List<EnumRegisterAnticipation>> getAnticipation(EnumRegisterPersonType type);

    Single<List<TaxaMdr>> getTaxList(CustomerRegister customerRegister);

    Single<List<EnumRegisterDebitAutomatic>> getDebitAutomatic();

    Single<List<Operadora>> getCarriers();

    Single<List<EnumRegisterDueDate>> getDueList();

    Single<List<CustomerRegisterExemption>> getExemptionList();

    Completable saveCustomerRegister(CustomerRegister clientRegister);

    Single<Boolean> verifyRegister(String text, String customerType);

    Colaborador getSalesmanDirect();

    Single<TaxaMdr> getAcquisitionTaxFromMcc(String mcc);

    Completable saveTokenConfirmation(int customerId);

    Single<TipoConta> getAccountType(int id);

    Single<List<Profissoes>> getProfissoes();

    Single<List<FaixaSalarial>> getRenda();

    Single<List<FaixaPatrimonial>> getPatrimonio();

    Single<List<FaixaDeFaturamentoMensal>> getFaixaDeFaturamentoMensal();
    Single<List<TipoLogradouro>> getTipoLogradouro();

    Single<CustomerRegister> getDadosCliente(String pCpfCnpj);

    Single<CustomerRegister> getSolicitacaoPid(int idSolPidServer);
}
