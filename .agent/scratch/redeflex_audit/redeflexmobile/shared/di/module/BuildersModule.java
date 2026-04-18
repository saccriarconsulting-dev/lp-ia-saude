package com.axys.redeflexmobile.shared.di.module;

import com.axys.redeflexmobile.ui.adquirencia.map.MapProspectActivity;
import com.axys.redeflexmobile.ui.adquirencia.map.MapProspectActivityModule;
import com.axys.redeflexmobile.ui.adquirencia.prospectinfo.ProspectInfoActivity;
import com.axys.redeflexmobile.ui.adquirencia.prospectinfo.ProspectInfoActivityModule;
import com.axys.redeflexmobile.ui.adquirencia.ranking.RankingActivity;
import com.axys.redeflexmobile.ui.adquirencia.ranking.RankingModule;
import com.axys.redeflexmobile.ui.adquirencia.ranking.list.RankingListFragment;
import com.axys.redeflexmobile.ui.adquirencia.ranking.list.RankingListFragmentModule;
import com.axys.redeflexmobile.ui.adquirencia.relatorio.RelatorioAdquirenciaActivity;
import com.axys.redeflexmobile.ui.adquirencia.relatorio.RelatorioAdquirenciaModule;
import com.axys.redeflexmobile.ui.adquirencia.release.ReleaseActivity;
import com.axys.redeflexmobile.ui.adquirencia.release.ReleaseActivityModule;
import com.axys.redeflexmobile.ui.adquirencia.routes.RoutesProspectActivity;
import com.axys.redeflexmobile.ui.adquirencia.routes.RoutesProspectActivityModule;
import com.axys.redeflexmobile.ui.adquirencia.routes.list.RoutesProspectListFragment;
import com.axys.redeflexmobile.ui.adquirencia.routes.list.RoutesProspectListFragmentModule;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivity;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivityModule;
import com.axys.redeflexmobile.ui.adquirencia.visit.cancel.VisitProspectCancelFragment;
import com.axys.redeflexmobile.ui.adquirencia.visit.cancel.VisitProspectCancelFragmentModule;
import com.axys.redeflexmobile.ui.adquirencia.visit.catalog.VisitProspectCatalogFragment;
import com.axys.redeflexmobile.ui.adquirencia.visit.catalog.VisitProspectCatalogFragmentModule;
import com.axys.redeflexmobile.ui.adquirencia.visit.catalog.VisitProspectCatalogItemFragment;
import com.axys.redeflexmobile.ui.adquirencia.visit.catalog.VisitProspectCatalogItemFragmentModule;
import com.axys.redeflexmobile.ui.adquirencia.visit.quality.VisitProspectQualityFragment;
import com.axys.redeflexmobile.ui.adquirencia.visit.quality.VisitProspectQualityFragmentModule;
import com.axys.redeflexmobile.ui.adquirencia.visit.route.VisitProspectRouteFragment;
import com.axys.redeflexmobile.ui.adquirencia.visit.route.VisitProspectRouteFragmentModule;
import com.axys.redeflexmobile.ui.adquirencia.visit.visit.VisitProspectVisitFragment;
import com.axys.redeflexmobile.ui.adquirencia.visit.visit.VisitProspectVisitFragmentModule;
import com.axys.redeflexmobile.ui.cartaoponto.CartaoPontoActivity;
import com.axys.redeflexmobile.ui.cartaoponto.CartaoPontoModule;
import com.axys.redeflexmobile.ui.cliente.atualizar.AtualizarClienteActivity;
import com.axys.redeflexmobile.ui.cliente.atualizar.AtualizarClienteModule;
import com.axys.redeflexmobile.ui.cliente.lista.ClienteListaActivity;
import com.axys.redeflexmobile.ui.cliente.lista.ClienteListaModule;
import com.axys.redeflexmobile.ui.clientemigracao.ClientMigrationActivity;
import com.axys.redeflexmobile.ui.clientemigracao.ClientMigrationModule;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationActivity;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationModule;
import com.axys.redeflexmobile.ui.clientemigracao.register.personal.RegisterMigrationPersonalFragment;
import com.axys.redeflexmobile.ui.clientemigracao.register.personal.RegisterMigrationPersonalModule;
import com.axys.redeflexmobile.ui.clientemigracao.register.personal.horariofunc.RegisterMigrationHorarioFuncFragment;
import com.axys.redeflexmobile.ui.clientemigracao.register.personal.horariofunc.RegisterMigrationHorarioFuncModule;
import com.axys.redeflexmobile.ui.clientemigracao.register.proposal.RegisterMigrationProposalFragment;
import com.axys.redeflexmobile.ui.clientemigracao.register.proposal.RegisterMigrationProposalModule;
import com.axys.redeflexmobile.ui.clientemigracao.register.taxes.RegisterMigrationTaxesFragment;
import com.axys.redeflexmobile.ui.clientemigracao.register.taxes.RegisterMigrationTaxesModule;
import com.axys.redeflexmobile.ui.clientevalidacao.ClienteActivity;
import com.axys.redeflexmobile.ui.clientevalidacao.ClienteModule;
import com.axys.redeflexmobile.ui.clientpendent.ClientePendenteActivity;
import com.axys.redeflexmobile.ui.clientpendent.ClientePendenteModule;
import com.axys.redeflexmobile.ui.comprovante.container.ComprovanteSkyTaActivity;
import com.axys.redeflexmobile.ui.comprovante.container.ComprovanteSkyTaModule;
import com.axys.redeflexmobile.ui.dialog.cliente.ClienteInfoDialog;
import com.axys.redeflexmobile.ui.dialog.cliente.ClienteInfoModule;
import com.axys.redeflexmobile.ui.login.LoginActivity;
import com.axys.redeflexmobile.ui.login.LoginModule;
import com.axys.redeflexmobile.ui.pos.detalhes.VisualizarDetalhesPosActivity;
import com.axys.redeflexmobile.ui.pos.detalhes.VisualizarDetalhesPosModule;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoActivity;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.homebanking.ClientHomeBankingActivity;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.homebanking.ClientHomeBankingModule;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.mdrtaxes.ClientMdrTaxesActivity;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.mdrtaxes.ClientMdrTaxesModule;
import com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.ClienteInfoPosList;
import com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.ClienteInfoPosListModule;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivityModule;
import com.axys.redeflexmobile.ui.register.customer.address.RegisterCustomerAddressListFragment;
import com.axys.redeflexmobile.ui.register.customer.address.RegisterCustomerAddressListFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.address.addressregister.RegisterCustomerAddressFragment;
import com.axys.redeflexmobile.ui.register.customer.address.addressregister.RegisterCustomerAddressFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentFragment;
import com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.commercial.RegisterCustomerCommercialFragment;
import com.axys.redeflexmobile.ui.register.customer.commercial.RegisterCustomerCommercialFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.commercial.pos.RegisterCustomerCommercialPosFragment;
import com.axys.redeflexmobile.ui.register.customer.commercial.pos.RegisterCustomerCommercialPosFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.commercial.tax.RegisterCustomerCommercialTaxFragment;
import com.axys.redeflexmobile.ui.register.customer.commercial.tax.RegisterCustomerCommercialTaxFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.contato.RegisterCustomerDadosContatoFragment;
import com.axys.redeflexmobile.ui.register.customer.contato.RegisterCustomerDadosContatoFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.dadosec.RegisterCustomerDadosECFragment;
import com.axys.redeflexmobile.ui.register.customer.dadosec.RegisterCustomerDadosECFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.dadosec.horariofunc.RegisterCustomerHorarioFuncFragment;
import com.axys.redeflexmobile.ui.register.customer.dadosec.horariofunc.RegisterCustomerHorarioFuncFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.financial.RegisterCustomerFinancialFragment;
import com.axys.redeflexmobile.ui.register.customer.financial.RegisterCustomerFinancialFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.partners.RegisterCustomerPartnersFragment;
import com.axys.redeflexmobile.ui.register.customer.partners.RegisterCustomerPartnersFragmentModule;
import com.axys.redeflexmobile.ui.register.customer.personal.RegisterCustomerPersonalFragment;
import com.axys.redeflexmobile.ui.register.customer.personal.RegisterCustomerPersonalFragmentModule;
import com.axys.redeflexmobile.ui.register.list.RegisterListActivity;
import com.axys.redeflexmobile.ui.register.list.RegisterListActivityModule;
import com.axys.redeflexmobile.ui.simulationrate.registerlist.RegisterProspectListActivity;
import com.axys.redeflexmobile.ui.simulationrate.registerlist.RegisterProspectListModule;
import com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo.RegisterRateActivity;
import com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo.RegisterRateModule;
import com.axys.redeflexmobile.ui.simulationrate.registertaxes.RegisterTaxesActivity;
import com.axys.redeflexmobile.ui.simulationrate.registertaxes.RegisterTaxesModule;
import com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra.InformarCodigoBarraActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra.InformarCodigoBarraModule;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaModule;
import com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao.NovaSolicitacaoActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao.NovaSolicitacaoModule;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosModule;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemModule;
import com.axys.redeflexmobile.ui.venda.abertura.VendaAberturaActivity;
import com.axys.redeflexmobile.ui.venda.abertura.VendaAberturaActivityModule;
import com.axys.redeflexmobile.ui.venda.auditagem.AuditagemPdvActivity;
import com.axys.redeflexmobile.ui.venda.auditagem.AuditagemPdvActivityModule;
import com.axys.redeflexmobile.ui.venda.pedido.PedidoVendaActivity;
import com.axys.redeflexmobile.ui.venda.pedido.PedidoVendaActivityModule;
import com.axys.redeflexmobile.ui.venda.pedido.produto.PedidoVendaProdutoDialog;
import com.axys.redeflexmobile.ui.venda.pedido.produto.PedidoVendaProdutoModule;
import com.axys.redeflexmobile.ui.venda.pedido.quantidade.PedidoVendaQuantidadeDialog;
import com.axys.redeflexmobile.ui.venda.pedido.quantidade.PedidoVendaQuantidadeModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface BuildersModule {

    @ContributesAndroidInjector(modules = CartaoPontoModule.class)
    CartaoPontoActivity bindCartaoPontoActivity();

    @ContributesAndroidInjector(modules = RoutesProspectActivityModule.class)
    RoutesProspectActivity bindRotasProspectActivity();

    @ContributesAndroidInjector(modules = MapProspectActivityModule.class)
    MapProspectActivity bindMapProspectActivity();

    @ContributesAndroidInjector(modules = RankingModule.class)
    RankingActivity bindRankingActivity();

    @ContributesAndroidInjector(modules = VisitProspectActivityModule.class)
    VisitProspectActivity bindVisitaProspectActivity();

    @ContributesAndroidInjector(modules = RegisterListActivityModule.class)
    RegisterListActivity bindRegisterActivity();

    @ContributesAndroidInjector(modules = RegisterCustomerActivityModule.class)
    RegisterCustomerActivity bindCustomerRegisterActivity();

    @ContributesAndroidInjector(modules = ReleaseActivityModule.class)
    ReleaseActivity bindReleaseActivity();

    @ContributesAndroidInjector(modules = VisualizarDetalhesPosModule.class)
    VisualizarDetalhesPosActivity bindVisualizarDetalhesPosActivity();

    @ContributesAndroidInjector(modules = RelatorioAdquirenciaModule.class)
    RelatorioAdquirenciaActivity bindRelatorioAdquirenciaActivity();

    @ContributesAndroidInjector(modules = ComprovanteSkyTaModule.class)
    ComprovanteSkyTaActivity bindComprovanteSkyTaActivity();

    @ContributesAndroidInjector(modules = ListagemSolicitacaoTrocaModule.class)
    ListagemSolicitacaoTrocaActivity bindSolicitacaoTrocaActivity();

    @ContributesAndroidInjector(modules = SelecionarProdutosModule.class)
    SelecionarProdutosActivity bindSelecionarProdutosActivity();

    @ContributesAndroidInjector(modules = NovaSolicitacaoModule.class)
    NovaSolicitacaoActivity bindNovaSolicitacaoActivity();

    @ContributesAndroidInjector(modules = InformarCodigoBarraModule.class)
    InformarCodigoBarraActivity bindInformarCodigoBarraActivity();

    @ContributesAndroidInjector(modules = SelecionarProdutosBipagemModule.class)
    SelecionarProdutosBipagemActivity bindSelecionarProdutosBipagemActivity();

    @ContributesAndroidInjector(modules = VendaAberturaActivityModule.class)
    VendaAberturaActivity bindVendaAberturaActivity();

    @ContributesAndroidInjector(modules = ClienteInfoActivityModule.class)
    ClienteInfoActivity bindClienteInfoActivity();

    @ContributesAndroidInjector(modules = AtualizarClienteModule.class)
    AtualizarClienteActivity bindAtualizarClienteActivity();

    @ContributesAndroidInjector(modules = AuditagemPdvActivityModule.class)
    AuditagemPdvActivity bindAuditagemPdvActivity();

    @ContributesAndroidInjector(modules = PedidoVendaActivityModule.class)
    PedidoVendaActivity bindPedidoVendaActivity();

    @ContributesAndroidInjector(modules = RegisterRateModule.class)
    RegisterRateActivity bindRegisterRateActivity();

    @ContributesAndroidInjector(modules = RegisterTaxesModule.class)
    RegisterTaxesActivity bindRegisterTaxesActivity();

    @ContributesAndroidInjector(modules = ProspectInfoActivityModule.class)
    ProspectInfoActivity bindProspectInfoActivity();

    @ContributesAndroidInjector(modules = ClienteInfoPosListModule.class)
    ClienteInfoPosList buildClienteInfoPosListActivity();

    @ContributesAndroidInjector(modules = ClienteModule.class)
    ClienteActivity bindClienteActivity();

    @ContributesAndroidInjector(modules = ClienteListaModule.class)
    ClienteListaActivity bindClienteListaActivity();

    @ContributesAndroidInjector(modules = ClientePendenteModule.class)
    ClientePendenteActivity bindClientePendenteActivity();

    @ContributesAndroidInjector(modules = ClientMigrationModule.class)
    ClientMigrationActivity bindClienteMigracaoActivity();

    @ContributesAndroidInjector(modules = RegisterMigrationModule.class)
    RegisterMigrationActivity bindRegisterMigrationActivity();

    @ContributesAndroidInjector(modules = RegisterProspectListModule.class)
    RegisterProspectListActivity bindRegisterProspectListActivity();

    @ContributesAndroidInjector(modules = ClientMdrTaxesModule.class)
    ClientMdrTaxesActivity bindClientMdrTaxesActivity();

    @ContributesAndroidInjector(modules = ClientHomeBankingModule.class)
    ClientHomeBankingActivity bindClientHomeBankingActivity();

    @ContributesAndroidInjector(modules = LoginModule.class)
    LoginActivity bindLoginActivity();
    //endregion

    //region Fragments
    @ContributesAndroidInjector(modules = RoutesProspectListFragmentModule.class)
    RoutesProspectListFragment bindRotasProspectListFragment();

    @ContributesAndroidInjector(modules = RankingListFragmentModule.class)
    RankingListFragment bindRankingListFragment();

    @ContributesAndroidInjector(modules = VisitProspectRouteFragmentModule.class)
    VisitProspectRouteFragment bindVisitProspectRotaFragment();

    @ContributesAndroidInjector(modules = VisitProspectVisitFragmentModule.class)
    VisitProspectVisitFragment bindVisitProspectVisitFragment();

    @ContributesAndroidInjector(modules = VisitProspectCatalogFragmentModule.class)
    VisitProspectCatalogFragment bindVisitProspectCatalogFragment();

    @ContributesAndroidInjector(modules = VisitProspectQualityFragmentModule.class)
    VisitProspectQualityFragment bindVisitProspectQualityFragment();

    @ContributesAndroidInjector(modules = VisitProspectCatalogItemFragmentModule.class)
    VisitProspectCatalogItemFragment bindVisitProspectCatalogItemFragment();

    @ContributesAndroidInjector(modules = VisitProspectCancelFragmentModule.class)
    VisitProspectCancelFragment bindVisitProspectCancelFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerPersonalFragmentModule.class)
    RegisterCustomerPersonalFragment bindRegisterCustomerPersonalFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerCommercialFragmentModule.class)
    RegisterCustomerCommercialFragment bindRegisterCustomerComercialFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerFinancialFragmentModule.class)
    RegisterCustomerFinancialFragment bindRegisterCustomerFinancialFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerAddressListFragmentModule.class)
    RegisterCustomerAddressListFragment bindRegisterCustomerAddressListFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerAddressFragmentModule.class)
    RegisterCustomerAddressFragment bindAddressRegisterFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerAttachmentFragmentModule.class)
    RegisterCustomerAttachmentFragment bindRegisterCustomerAttachmentFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerCommercialTaxFragmentModule.class)
    RegisterCustomerCommercialTaxFragment bindRegisterCustomerCommercialTaxFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerCommercialPosFragmentModule.class)
    RegisterCustomerCommercialPosFragment bindRegisterCustomerCommercialPosFragment();

    @ContributesAndroidInjector(modules = RegisterMigrationPersonalModule.class)
    RegisterMigrationPersonalFragment bindRegisterMigrationPersonalFragment();

    @ContributesAndroidInjector(modules = RegisterMigrationHorarioFuncModule.class)
    RegisterMigrationHorarioFuncFragment bindRegisterMigrationHorarioFuncFragment();

    @ContributesAndroidInjector(modules = RegisterMigrationTaxesModule.class)
    RegisterMigrationTaxesFragment bindRegisterMigrationTaxesFragment();

    @ContributesAndroidInjector(modules = RegisterMigrationProposalModule.class)
    RegisterMigrationProposalFragment bindRegisterMigrationProposalFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerPartnersFragmentModule.class)
    RegisterCustomerPartnersFragment bindPartnersRegisterFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerDadosECFragmentModule.class)
    RegisterCustomerDadosECFragment bindDadosECRegisterFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerHorarioFuncFragmentModule.class)
    RegisterCustomerHorarioFuncFragment bindHorarioFuncRegisterFragment();

    @ContributesAndroidInjector(modules = RegisterCustomerDadosContatoFragmentModule.class)
    RegisterCustomerDadosContatoFragment bindDadosContatoFragment();

    //region Dialogs
    @ContributesAndroidInjector(modules = ClienteInfoModule.class)
    ClienteInfoDialog bindClienteInfoDialog();

    @ContributesAndroidInjector(modules = PedidoVendaProdutoModule.class)
    PedidoVendaProdutoDialog bindPedidoVendaProdutoDialog();

    @ContributesAndroidInjector(modules = PedidoVendaQuantidadeModule.class)
    PedidoVendaQuantidadeDialog bindPedidoVendaQuantidadeDialog();
    //endregion
}
