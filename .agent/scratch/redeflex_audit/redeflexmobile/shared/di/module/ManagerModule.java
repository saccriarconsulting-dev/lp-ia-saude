package com.axys.redeflexmobile.shared.di.module;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPid;
import com.axys.redeflexmobile.shared.bd.DBBancos;
import com.axys.redeflexmobile.shared.bd.DBCatalogoAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastro;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastroPOS;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBComprovanteSkyTa;
import com.axys.redeflexmobile.shared.bd.DBEstado;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBFaixaDeFaturamentoMensal;
import com.axys.redeflexmobile.shared.bd.DBFaixaPatrimonial;
import com.axys.redeflexmobile.shared.bd.DBFaixaSalarial;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.bd.DBIsencao;
import com.axys.redeflexmobile.shared.bd.DBModeloPOS;
import com.axys.redeflexmobile.shared.bd.DBMotivoAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBOperadora;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.bd.DBPerguntasQualidade;
import com.axys.redeflexmobile.shared.bd.DBPrazoNegociacao;
import com.axys.redeflexmobile.shared.bd.DBProfissoes;
import com.axys.redeflexmobile.shared.bd.DBProspect;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirenciaAgendada;
import com.axys.redeflexmobile.shared.bd.DBSegmento;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoTroca;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.bd.DBTipoConta;
import com.axys.redeflexmobile.shared.bd.DBTipoLogradouro;
import com.axys.redeflexmobile.shared.bd.DBVisitaAdquirencia;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBFlagsBank;
import com.axys.redeflexmobile.shared.dao.CallReasonDao;
import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.dao.ColaboradorDao;
import com.axys.redeflexmobile.shared.dao.MotiveMigrationSubDao;
import com.axys.redeflexmobile.shared.dao.PendenciaClienteDao;
import com.axys.redeflexmobile.shared.dao.PendenciaDao;
import com.axys.redeflexmobile.shared.dao.PendenciaMotivoDao;
import com.axys.redeflexmobile.shared.dao.PosDao;
import com.axys.redeflexmobile.shared.dao.RegisterMigrationSubDao;
import com.axys.redeflexmobile.shared.dao.RegisterMigrationSubTaxDao;
import com.axys.redeflexmobile.shared.dao.SolicitacaoPDDao;
import com.axys.redeflexmobile.shared.dao.clientinfo.ClientHomeBankingDao;
import com.axys.redeflexmobile.shared.dao.clientinfo.ClientTaxMdrDao;
import com.axys.redeflexmobile.shared.dao.clientinfo.FlagsBankDao;
import com.axys.redeflexmobile.shared.dao.registerrate.ProspectingClientAcquisitionDao;
import com.axys.redeflexmobile.shared.dao.registerrate.RegistrationSimulationFeeDao;
import com.axys.redeflexmobile.shared.manager.CallReasonManager;
import com.axys.redeflexmobile.shared.manager.CallReasonManagerImpl;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.manager.ClienteManagerImpl;
import com.axys.redeflexmobile.shared.manager.ColaboradorManager;
import com.axys.redeflexmobile.shared.manager.ColaboradorManagerImpl;
import com.axys.redeflexmobile.shared.manager.ComprovanteSkyTaManager;
import com.axys.redeflexmobile.shared.manager.ComprovanteSkyTaManagerImpl;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManagerImpl;
import com.axys.redeflexmobile.shared.manager.LoginManager;
import com.axys.redeflexmobile.shared.manager.LoginManagerImpl;
import com.axys.redeflexmobile.shared.manager.MapItemManager;
import com.axys.redeflexmobile.shared.manager.MapItemManagerImpl;
import com.axys.redeflexmobile.shared.manager.MotiveMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.MotiveMigrationSubManagerImpl;
import com.axys.redeflexmobile.shared.manager.PendenciaClienteManager;
import com.axys.redeflexmobile.shared.manager.PendenciaClienteManagerImpl;
import com.axys.redeflexmobile.shared.manager.PendenciaMamagerImpl;
import com.axys.redeflexmobile.shared.manager.PendenciaManager;
import com.axys.redeflexmobile.shared.manager.PendenciaMotivoManager;
import com.axys.redeflexmobile.shared.manager.PendenciaMotivoManagerImpl;
import com.axys.redeflexmobile.shared.manager.PeriodNegotiationManager;
import com.axys.redeflexmobile.shared.manager.PeriodNegotiationManagerImpl;
import com.axys.redeflexmobile.shared.manager.PosManager;
import com.axys.redeflexmobile.shared.manager.PosManagerImpl;
import com.axys.redeflexmobile.shared.manager.ProspectInfoManager;
import com.axys.redeflexmobile.shared.manager.ProspectInfoManagerImpl;
import com.axys.redeflexmobile.shared.manager.RankingManager;
import com.axys.redeflexmobile.shared.manager.RankingManagerImpl;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManagerImpl;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubTaxManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubTaxManagerImpl;
import com.axys.redeflexmobile.shared.manager.ReleaseManager;
import com.axys.redeflexmobile.shared.manager.ReleaseManagerImpl;
import com.axys.redeflexmobile.shared.manager.RoutesProspectManager;
import com.axys.redeflexmobile.shared.manager.RoutesProspectManagerImpl;
import com.axys.redeflexmobile.shared.manager.SolicitacaoPDManager;
import com.axys.redeflexmobile.shared.manager.SolicitacaoPDManagerImpl;
import com.axys.redeflexmobile.shared.manager.TaxaMdrManager;
import com.axys.redeflexmobile.shared.manager.TaxaMdrManagerImpl;
import com.axys.redeflexmobile.shared.manager.VendaManager;
import com.axys.redeflexmobile.shared.manager.VendaManagerImpl;
import com.axys.redeflexmobile.shared.manager.VisitProspectManager;
import com.axys.redeflexmobile.shared.manager.VisitProspectManagerImpl;
import com.axys.redeflexmobile.shared.manager.clientinfo.ClientHomeBankingManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.ClientHomeBankingManagerImpl;
import com.axys.redeflexmobile.shared.manager.clientinfo.ClientTaxMdrManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.ClientTaxMdrManagerImpl;
import com.axys.redeflexmobile.shared.manager.clientinfo.FlagsBankManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.FlagsBankManagerImpl;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManager;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManagerImpl;
import com.axys.redeflexmobile.shared.manager.registerrate.RegistrationSimulationFeeManager;
import com.axys.redeflexmobile.shared.manager.registerrate.RegistrationSimulationFeeManagerImpl;
import com.axys.redeflexmobile.shared.repository.PedidoVendaRepository;
import com.axys.redeflexmobile.shared.services.ClienteService;
import com.axys.redeflexmobile.shared.services.LoginService;
import com.axys.redeflexmobile.shared.services.PendenciaClienteService;
import com.axys.redeflexmobile.shared.services.PendenciaMotivoService;
import com.axys.redeflexmobile.shared.services.PendenciaService;
import com.axys.redeflexmobile.shared.services.RegisterService;
import com.axys.redeflexmobile.shared.services.SolicitacaoPDService;
import com.axys.redeflexmobile.shared.services.bus.ComprovanteSkyTaBus;
import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApiManager;
import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApiManagerImpl;
import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApiService;
import com.axys.redeflexmobile.shared.services.migracao.MotiveMigrationSubService;
import com.axys.redeflexmobile.shared.services.network.PosNetwork;
import com.axys.redeflexmobile.shared.services.network.RankingService;
import com.axys.redeflexmobile.shared.util.SharedPreferenceEncrypted;
import com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra.InformarCodigoBarraManager;
import com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra.InformarCodigoBarraManagerImpl;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaManager;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaManagerImpl;
import com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao.NovaSolicitacaoManager;
import com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao.NovaSolicitacaoManagerImpl;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosManager;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosManagerImpl;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemManager;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemManagerImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ManagerModule {

    @Provides
    RoutesProspectManager provideRoutesListManager(DBRotaAdquirencia dbRotaAdquirencia,
                                                   DBColaborador dbColaborador) {
        return new RoutesProspectManagerImpl(dbRotaAdquirencia, dbColaborador);
    }

    @Provides
    ProspectInfoManager provideProspectInfoManager(DBProspect dbProspect) {
        return new ProspectInfoManagerImpl(dbProspect);
    }

    @Provides
    MapItemManager provideMapItemManager(RankingService service, DBColaborador dbColaborador) {
        return new MapItemManagerImpl(service, dbColaborador);
    }

    @Provides
    RankingManager provideRankingListManager(
            RankingService rankingService,
            DBColaborador dbColaborador) {
        return new RankingManagerImpl(rankingService, dbColaborador);
    }

    @Provides
    CustomerRegisterManager provideCustomerRegisterManager(DBCliente dbCliente,
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
        return new CustomerRegisterManagerImpl(dbCliente,
                dbClienteCadastro,
                dbTaxaMdr,
                dbEstado,
                dbBancos,
                dbModeloPOS,
                dbProspect,
                dbSegmento,
                dbIsencao,
                dbColaborador,
                dbOperadora,
                dbPrazoNegociacao,
                registerService,
                dbTipoConta,
                dbProfissoes,
                dbFaixaSalarial,
                dbFaixaPatrimonial,
                dbFaixaDeFaturamentoMensal,
                dbTipoLogradouro,
                dbSolicitacaoPid);
    }

    @Provides
    VisitProspectManager provideVisitProspectManager(DBColaborador dbColaborador,
                                                     DBCliente dbCliente,
                                                     DBMotivoAdquirencia dbMotivoAdquirencia,
                                                     DBPerguntasQualidade dbPerguntasQualidade,
                                                     DBVisitaAdquirencia dbVisitaAdquirencia,
                                                     DBCatalogoAdquirencia dbCatalogoAdquirencia,
                                                     DBProspect dbProspect,
                                                     DBRotaAdquirencia dbRotaAdquirencia,
                                                     DBRotaAdquirenciaAgendada dbRotaAdquirenciaAgendada,
                                                     DBOs dbOs) {
        return new VisitProspectManagerImpl(dbColaborador,
                dbCliente,
                dbMotivoAdquirencia,
                dbPerguntasQualidade,
                dbVisitaAdquirencia,
                dbCatalogoAdquirencia,
                dbProspect,
                dbRotaAdquirencia,
                dbRotaAdquirenciaAgendada,
                dbOs);
    }

    @Provides
    ReleaseManager providerReleaseManager(DBClienteCadastroPOS dbClienteCadastroPOS) {
        return new ReleaseManagerImpl(dbClienteCadastroPOS);
    }

    @Provides
    GoogleMapsApiManager providerGoogleMapsApiManagerImpl(GoogleMapsApiService googleMapsApiService) {
        return new GoogleMapsApiManagerImpl(googleMapsApiService);
    }

    @Provides
    ComprovanteSkyTaManager provideComprovanteSkyTaManager(
            DBComprovanteSkyTa dbComprovanteSkyTa,
            DBCliente dbCliente,
            DBColaborador dbColaborador,
            ComprovanteSkyTaBus comprovanteSkyTaBus) {
        return new ComprovanteSkyTaManagerImpl(
                dbComprovanteSkyTa,
                dbCliente,
                dbColaborador,
                comprovanteSkyTaBus
        );
    }

    @Provides
    ListagemSolicitacaoTrocaManager provideListagemSolicitacaoTrocaManager(
            DBEstoque dbEstoque,
            DBSolicitacaoTroca dbSolicitacaoTroca
    ) {
        return new ListagemSolicitacaoTrocaManagerImpl(dbEstoque, dbSolicitacaoTroca);
    }

    @Provides
    NovaSolicitacaoManager provideNovaSolicitacaoManager(DBCliente dbCliente,
                                                         DBColaborador dbColaborador,
                                                         DBSolicitacaoTroca dbSolicitacaoTroca) {
        return new NovaSolicitacaoManagerImpl(dbCliente, dbColaborador, dbSolicitacaoTroca);
    }

    @Provides
    SelecionarProdutosManager provideSelecionarProdutosManager(DBEstoque dbEstoque,
                                                               DBSolicitacaoTroca dbSolicitacaoTroca) {
        return new SelecionarProdutosManagerImpl(dbEstoque, dbSolicitacaoTroca);
    }

    @Provides
    SelecionarProdutosBipagemManager provideSelecionarProdutosBipagemManager(DBEstoque dbEstoque,
                                                                             DBIccid dbIccid) {
        return new SelecionarProdutosBipagemManagerImpl(dbEstoque, dbIccid);
    }

    @Provides
    InformarCodigoBarraManager provideInformarCodigoBarraManager(DBSolicitacaoTroca dbSolicitacaoTroca) {
        return new InformarCodigoBarraManagerImpl(dbSolicitacaoTroca);
    }

    @Provides
    ClienteManager provideClienteManager(ClienteService clienteService, ClienteDao clienteDao,
                                         ColaboradorDao colaboradorDao) {
        return new ClienteManagerImpl(clienteService, clienteDao, colaboradorDao);
    }

    @Provides
    VendaManager provideVendaManager(PedidoVendaRepository repository) {
        return new VendaManagerImpl(repository);
    }

    @Provides
    PosManager providePosManger(PosDao posDao, ClienteDao clienteDao, PosNetwork posNetwork,
                                ColaboradorDao colaboradorDao) {
        return new PosManagerImpl(posDao, clienteDao, posNetwork, colaboradorDao);
    }

    @Provides
    PendenciaManager providePendenciaManager(PendenciaService service, PendenciaDao dao) {
        return new PendenciaMamagerImpl(service, dao);
    }

    @Provides
    SolicitacaoPDManager provideSolicitacaoPDManager(SolicitacaoPDService service,
                                                     SolicitacaoPDDao dao) {
        return new SolicitacaoPDManagerImpl(service, dao);
    }

    @Provides
    PendenciaClienteManager providePendenciaClienteManager(PendenciaClienteService service,
                                                           PendenciaClienteDao dao) {
        return new PendenciaClienteManagerImpl(service, dao);
    }

    @Provides
    PendenciaMotivoManager providePendenciaMotivoManager(PendenciaMotivoService service,
                                                         PendenciaMotivoDao dao) {
        return new PendenciaMotivoManagerImpl(service, dao);
    }

    @Provides
    MotiveMigrationSubManager provideMotiveMigrationSubManager(MotiveMigrationSubService service,
                                                               MotiveMigrationSubDao dao) {
        return new MotiveMigrationSubManagerImpl(service, dao);
    }

    @Provides
    RegisterMigrationSubManager provideRegisterMigrationSubManager(RegisterMigrationSubDao dao) {
        return new RegisterMigrationSubManagerImpl(dao);
    }

    @Provides
    RegisterMigrationSubTaxManager provideRegisterMigrationSubTaxManager(RegisterMigrationSubTaxDao dao) {
        return new RegisterMigrationSubTaxManagerImpl(dao);
    }

    @Provides
    TaxaMdrManager provideTaxaMdrManager() {
        return new TaxaMdrManagerImpl();
    }

    @Provides
    PeriodNegotiationManager providePeriodNegotiationManager(DBPrazoNegociacao dbPrazoNegociacao) {
        return new PeriodNegotiationManagerImpl(dbPrazoNegociacao);
    }

    @Provides
    ProspectingClientAcquisitionManager provideProspectingClientAcquisitionManager(ProspectingClientAcquisitionDao dao) {
        return new ProspectingClientAcquisitionManagerImpl(dao);
    }

    @Provides
    RegistrationSimulationFeeManager provideRegistrationSimulationFeeManager(RegistrationSimulationFeeDao dao) {
        return new RegistrationSimulationFeeManagerImpl(dao);
    }

    @Provides
    ColaboradorManager provideColaboradorManager(ColaboradorDao dao) {
        return new ColaboradorManagerImpl(dao);
    }

    @Provides
    ClientTaxMdrManager provideClientTaxMdrManager(ClientTaxMdrDao dao) {
        return new ClientTaxMdrManagerImpl(dao);
    }

    @Provides
    ClientHomeBankingManager provideClientHomeBankingManager(ClientHomeBankingDao dao) {
        return new ClientHomeBankingManagerImpl(dao);
    }

    @Provides
    FlagsBankManager provideFlagsManager(FlagsBankDao dao) {
        return new FlagsBankManagerImpl(dao);
    }

    @Provides
    CallReasonManager provideCallReasonManager(CallReasonDao dao) {
        return new CallReasonManagerImpl(dao);
    }

    @Provides
    LoginManager provideLoginManager(LoginService loginService, Context context,
                                     SharedPreferenceEncrypted sharedPreferenceEncrypted,
                                     DBColaborador dbColaborador) {
        return new LoginManagerImpl(loginService, context, sharedPreferenceEncrypted, dbColaborador);
    }
}
