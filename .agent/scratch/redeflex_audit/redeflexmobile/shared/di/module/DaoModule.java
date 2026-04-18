package com.axys.redeflexmobile.shared.di.module;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPid;
import com.axys.redeflexmobile.shared.bd.DBAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBAtualizarCliente;
import com.axys.redeflexmobile.shared.bd.DBBancos;
import com.axys.redeflexmobile.shared.bd.DBCallReason;
import com.axys.redeflexmobile.shared.bd.DBCartaoPonto;
import com.axys.redeflexmobile.shared.bd.DBCatalogoAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastro;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastroPOS;
import com.axys.redeflexmobile.shared.bd.DBCnae;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBComprovanteSkyTa;
import com.axys.redeflexmobile.shared.bd.DBCredencial;
import com.axys.redeflexmobile.shared.bd.DBEstado;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBFaixaDeFaturamentoMensal;
import com.axys.redeflexmobile.shared.bd.DBFaixaPatrimonial;
import com.axys.redeflexmobile.shared.bd.DBFaixaSalarial;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.bd.DBIsencao;
import com.axys.redeflexmobile.shared.bd.DBLocalizacaoCliente;
import com.axys.redeflexmobile.shared.bd.DBModeloPOS;
import com.axys.redeflexmobile.shared.bd.DBMotiveMigrationSub;
import com.axys.redeflexmobile.shared.bd.DBMotivoAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBOperadora;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.bd.DBPOS;
import com.axys.redeflexmobile.shared.bd.DBPerguntasQualidade;
import com.axys.redeflexmobile.shared.bd.DBPrazoNegociacao;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBProfissoes;
import com.axys.redeflexmobile.shared.bd.DBProspect;
import com.axys.redeflexmobile.shared.bd.DBRegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirenciaAgendada;
import com.axys.redeflexmobile.shared.bd.DBSegmento;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoTroca;
import com.axys.redeflexmobile.shared.bd.DBSugestaoVenda;
import com.axys.redeflexmobile.shared.bd.DBTaxaAdquirenciaPF;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.bd.DBTaxasAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBTipoConta;
import com.axys.redeflexmobile.shared.bd.DBTipoLogradouro;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.DBVisitaAdquirencia;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBClientHomeBanking;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBClientTaxMdr;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBFlagsBank;
import com.axys.redeflexmobile.shared.bd.registerrate.DBProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.bd.registerrate.DBRegistrationSimulationFee;
import com.axys.redeflexmobile.shared.dao.CallReasonDao;
import com.axys.redeflexmobile.shared.dao.CallReasonDaoImpl;
import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.dao.ClienteDaoImpl;
import com.axys.redeflexmobile.shared.dao.ColaboradorDao;
import com.axys.redeflexmobile.shared.dao.ColaboradorDaoImpl;
import com.axys.redeflexmobile.shared.dao.ConsignadoLimiteProdutoDao;
import com.axys.redeflexmobile.shared.dao.ConsignadoLimiteProdutoDaoImpl;
import com.axys.redeflexmobile.shared.dao.EstoqueDao;
import com.axys.redeflexmobile.shared.dao.EstoqueDaoImpl;
import com.axys.redeflexmobile.shared.dao.MotiveMigrationSubDao;
import com.axys.redeflexmobile.shared.dao.MotiveMigrationSubDaoImpl;
import com.axys.redeflexmobile.shared.dao.PendenciaClienteDao;
import com.axys.redeflexmobile.shared.dao.PendenciaClienteDaoImpl;
import com.axys.redeflexmobile.shared.dao.PendenciaDao;
import com.axys.redeflexmobile.shared.dao.PendenciaDaoImpl;
import com.axys.redeflexmobile.shared.dao.PendenciaMotivoDao;
import com.axys.redeflexmobile.shared.dao.PendenciaMotivoDaoImpl;
import com.axys.redeflexmobile.shared.dao.PosDao;
import com.axys.redeflexmobile.shared.dao.PosDaoImpl;
import com.axys.redeflexmobile.shared.dao.PrecoDao;
import com.axys.redeflexmobile.shared.dao.PrecoDaoImpl;
import com.axys.redeflexmobile.shared.dao.RegisterMigrationSubDao;
import com.axys.redeflexmobile.shared.dao.RegisterMigrationSubDaoImpl;
import com.axys.redeflexmobile.shared.dao.RegisterMigrationSubTaxDao;
import com.axys.redeflexmobile.shared.dao.RegisterMigrationSubTaxDaoImpl;
import com.axys.redeflexmobile.shared.dao.RotaDao;
import com.axys.redeflexmobile.shared.dao.RotaDaoImpl;
import com.axys.redeflexmobile.shared.dao.SegmentoDao;
import com.axys.redeflexmobile.shared.dao.SegmentoDaoImpl;
import com.axys.redeflexmobile.shared.dao.SugestaoVendaDao;
import com.axys.redeflexmobile.shared.dao.SugestaoVendaDaoImpl;
import com.axys.redeflexmobile.shared.dao.TipoContaDao;
import com.axys.redeflexmobile.shared.dao.TipoContaDaoImpl;
import com.axys.redeflexmobile.shared.dao.VendaDao;
import com.axys.redeflexmobile.shared.dao.VendaDaoImpl;
import com.axys.redeflexmobile.shared.dao.VisitaDao;
import com.axys.redeflexmobile.shared.dao.VisitaDaoImpl;
import com.axys.redeflexmobile.shared.dao.clientinfo.ClientHomeBankingDao;
import com.axys.redeflexmobile.shared.dao.clientinfo.ClientHomeBankingDaoImpl;
import com.axys.redeflexmobile.shared.dao.clientinfo.ClientTaxMdrDao;
import com.axys.redeflexmobile.shared.dao.clientinfo.ClientTaxMdrDaoImpl;
import com.axys.redeflexmobile.shared.dao.clientinfo.FlagsBankDao;
import com.axys.redeflexmobile.shared.dao.clientinfo.FlagsBankDaoImpl;
import com.axys.redeflexmobile.shared.dao.registerrate.ProspectingClientAcquisitionDao;
import com.axys.redeflexmobile.shared.dao.registerrate.ProspectingClientAcquisitionDaoImpl;
import com.axys.redeflexmobile.shared.dao.registerrate.RegistrationSimulationFeeDao;
import com.axys.redeflexmobile.shared.dao.registerrate.RegistrationSimulationFeeDaoImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Provides
    DBColaborador provideDbColaborador(Context context) {
        return new DBColaborador(context);
    }

    @Provides
    DBCartaoPonto provideDbCartaoPonto(Context context) {
        return new DBCartaoPonto(context);
    }

    @Provides
    DBClienteCadastro provideDbClienteCadastro(Context context) {
        return new DBClienteCadastro(context);
    }

    @Provides
    DBCliente provideDbCliente(Context context) {
        return new DBCliente(context);
    }

    @Provides
    DBAdquirencia provideDbAdquirencia(Context context) {
        return new DBAdquirencia(context);
    }

    @Provides
    DBTaxaAdquirenciaPF provideDBTaxaAdquirenciaPF(Context context) {
        return new DBTaxaAdquirenciaPF(context);
    }

    @Provides
    DBTaxasAdquirencia provideDBTaxasAdquirencia(Context context) {
        return new DBTaxasAdquirencia(context);
    }

    @Provides
    DBEstado provideDBEstado(Context context) {
        return new DBEstado(context);
    }

    @Provides
    DBModeloPOS provideDBModeloPOS(Context context) {
        return new DBModeloPOS(context);
    }

    @Provides
    DBPOS provideDBPOS(Context context) {
        return new DBPOS(context);
    }

    @Provides
    DBRotaAdquirencia provideDBRotaAdquirencia(Context context) {
        return new DBRotaAdquirencia(context);
    }

    @Provides
    DBRotaAdquirenciaAgendada provideDBRotaAdquirenciaAgendada(Context context) {
        return new DBRotaAdquirenciaAgendada(context);
    }

    @Provides
    DBMotivoAdquirencia provideDBMotivoAdquirencia(Context context) {
        return new DBMotivoAdquirencia(context);
    }

    @Provides
    DBPerguntasQualidade provideDBPerguntasQualidade(Context context) {
        return new DBPerguntasQualidade(context);
    }

    @Provides
    DBVisitaAdquirencia provideDBVisitaAdquirencia(Context context) {
        return new DBVisitaAdquirencia(context);
    }

    @Provides
    DBCatalogoAdquirencia provideDBCatalogoAdquirencia(Context context) {
        return new DBCatalogoAdquirencia(context);
    }

    @Provides
    DBProspect provideDBProspect(Context context) {
        return new DBProspect(context);
    }

    @Provides
    DBSegmento provideDBSegmento(Context context) {
        return new DBSegmento(context);
    }

    @Provides
    DBIsencao provideDBIsencao(Context context) {
        return new DBIsencao(context);
    }

    @Provides
    DBTaxaMdr provideDBTaxaMdr(Context context) {
        return new DBTaxaMdr(context);
    }

    @Provides
    DBOs provideDBOs(Context context) {
        return new DBOs(context);
    }

    @Provides
    DBComprovanteSkyTa provideDbComprovanteSkyTa(Context context) {
        return new DBComprovanteSkyTa(context);
    }

    @Provides
    DBSolicitacaoTroca provideDbSolicitacaoTroca(Context context) {
        return new DBSolicitacaoTroca(context);
    }

    @Provides
    DBPreco provideDbPreco(Context context) {
        return new DBPreco(context);
    }

    @Provides
    DBIccid provideDbIccid(Context context) {
        return new DBIccid(context);
    }

    @Provides
    DBVenda provideDbVenda(Context context) {
        return new DBVenda(context);
    }

    @Provides
    DBEstoque provideDbEstoque(Context context) {
        return new DBEstoque(context);
    }

    @Provides
    DBAtualizarCliente provideDBAtualizarCliente(Context context) {
        return new DBAtualizarCliente(context);
    }

    @Provides
    DBSugestaoVenda provideDBSugestaoVenda(Context context) {
        return new DBSugestaoVenda(context);
    }

    @Provides
    ClienteDao provideClienteDao(Context context) {
        return new ClienteDaoImpl(context);
    }

    @Provides
    EstoqueDao provideEstoqueDao(Context context) {
        return new EstoqueDaoImpl(context);
    }

    @Provides
    PrecoDao providePrecoDao(Context context) {
        return new PrecoDaoImpl(context);
    }

    @Provides
    RotaDao provideRotaDao(Context context) {
        return new RotaDaoImpl(context);
    }

    @Provides
    SugestaoVendaDao provideSugestaoVendaDao(Context context) {
        return new SugestaoVendaDaoImpl(context);
    }

    @Provides
    ConsignadoLimiteProdutoDao provideConsignadoLimiteProdutoDao(Context context) {
        return new ConsignadoLimiteProdutoDaoImpl(context);
    }

    @Provides
    VendaDao provideVendaDao(Context context) {
        return new VendaDaoImpl(context);
    }

    @Provides
    VisitaDao provideVisitaDao(Context context) {
        return new VisitaDaoImpl(context);
    }

    @Provides
    DBBancos provideDbBancos(Context context) {
        return new DBBancos(context);
    }

    @Provides
    SegmentoDao provideSegmentoDao(Context context) {
        return new SegmentoDaoImpl(context);
    }

    @Provides
    DBCnae provideDnCnae(Context context) {
        return new DBCnae(context);
    }

    @Provides
    DBOperadora provideDBOperadora(final Context context) {
        return new DBOperadora(context);
    }

    @Provides
    DBPrazoNegociacao provideDBPrazoNegociacao(Context context) {
        return new DBPrazoNegociacao(context);
    }

    @Provides
    DBClienteCadastroPOS provideDBClienteCadastroPOS(final Context context) {
        return new DBClienteCadastroPOS(context);
    }

    @Provides
    PosDao providePosDao(Context context) {
        return new PosDaoImpl(context);
    }

    @Provides
    ColaboradorDao provideColaboradorDao(Context context) {
        return new ColaboradorDaoImpl(context);
    }

    @Provides
    PendenciaDao providePendenciaDao(Context context) {
        return new PendenciaDaoImpl(context);
    }

    @Provides
    PendenciaMotivoDao providePendenciaMotivoDao(Context context) {
        return new PendenciaMotivoDaoImpl(context);
    }

    @Provides
    PendenciaClienteDao providePendenciaClienteDao(Context context) {
        return new PendenciaClienteDaoImpl(context);
    }

    @Provides
    MotiveMigrationSubDao provideMotiveMigrationSubDao(DBMotiveMigrationSub dbMotiveMigrationSub) {
        return new MotiveMigrationSubDaoImpl(dbMotiveMigrationSub);
    }

    @Provides
    RegisterMigrationSubDao provideRegisterMigrationSubDao(Context context) {
        return new RegisterMigrationSubDaoImpl(context);
    }

    @Provides
    DBRegisterMigrationSubTax provideDBRegisterMigrationSubTax(Context context) {
        return new DBRegisterMigrationSubTax(context);
    }

    @Provides
    RegisterMigrationSubTaxDao provideRegisterMigrationSubTaxDao(DBRegisterMigrationSubTax db, DBTaxaMdr dbTaxaMdr) {
        return new RegisterMigrationSubTaxDaoImpl(db, dbTaxaMdr);
    }

    @Provides
    DBMotiveMigrationSub provideDBMotiveMigrationSub(Context context) {
        return new DBMotiveMigrationSub(context);
    }

    @Provides
    DBProspectingClientAcquisition provideDBProspectingClientAcquisition(Context context) {
        return new DBProspectingClientAcquisition(context);
    }

    @Provides
    DBRegistrationSimulationFee provideDBRegistrationSimulationFee(Context context) {
        return new DBRegistrationSimulationFee(context);
    }

    @Provides
    RegistrationSimulationFeeDao provideRegistrationSimulationFeeDao(DBRegistrationSimulationFee db,
                                                                     DBTaxaMdr dbTaxaMdr) {
        return new RegistrationSimulationFeeDaoImpl(db, dbTaxaMdr);
    }

    @Provides
    ProspectingClientAcquisitionDao provideProspectingClientAcquisitionDao(DBProspectingClientAcquisition db,
                                                                           DBColaborador dbColaborador) {
        return new ProspectingClientAcquisitionDaoImpl(db, dbColaborador);
    }

    @Provides
    DBClientTaxMdr provideDBClientTaxMdr(Context context) {
        return new DBClientTaxMdr(context);
    }

    @Provides
    DBClientHomeBanking provideDBClientHomeBanking(Context context) {
        return new DBClientHomeBanking(context);
    }

    @Provides
    ClientTaxMdrDao provideClientTaxMdrDao(DBClientTaxMdr db) {
        return new ClientTaxMdrDaoImpl(db);
    }

    @Provides
    DBFlagsBank provideDBFlagsBank(Context context) {
        return new DBFlagsBank(context);
    }

    @Provides
    FlagsBankDao provideFlagsBankDao(DBFlagsBank db) {
        return new FlagsBankDaoImpl(db);
    }

    @Provides
    ClientHomeBankingDao provideClientHomeBankingDao(DBClientHomeBanking db) {
        return new ClientHomeBankingDaoImpl(db);
    }

    @Provides
    CallReasonDao provideCallReasonDao(DBCallReason db) {
        return new CallReasonDaoImpl(db);
    }

    @Provides
    DBTipoConta provideDbTipoConta(Context context) {
        return new DBTipoConta(context);
    }

    @Provides
    TipoContaDao provideTipoContaDao(DBTipoConta db) {
        return new TipoContaDaoImpl(db);
    }

    @Provides
    DBCredencial provideDBCredencial(Context context) {
        return new DBCredencial(context);
    }

    @Provides
    DBLocalizacaoCliente provideDbLocalizacaoCliente(Context context) {
        return new DBLocalizacaoCliente(context);
    }

    @Provides
    DBProfissoes provideDBProfissoes(Context context) {
        return new DBProfissoes(context);
    }

    @Provides
    DBFaixaSalarial provideDBFaixaSalarial(Context context) {
        return new DBFaixaSalarial(context);
    }

    @Provides
    DBFaixaPatrimonial provideDBFaixaPatrimonial(Context context) {
        return new DBFaixaPatrimonial(context);
    }
    @Provides
    DBFaixaDeFaturamentoMensal provideDBFaixaDeFaturamentoMensal(Context context) {
        return new DBFaixaDeFaturamentoMensal(context);
    }
    @Provides
    DBTipoLogradouro provideDBTipoLogradouro(Context context) {
        return new DBTipoLogradouro(context);
    }
    @Provides
    BDSolicitacaoPid provideBDSolicitacaoPid(Context context) {
        return new BDSolicitacaoPid(context);
    }
}
