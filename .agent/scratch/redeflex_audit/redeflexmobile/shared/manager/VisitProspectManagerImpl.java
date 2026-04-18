package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.bd.DBCatalogoAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBMotivoAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.bd.DBPerguntasQualidade;
import com.axys.redeflexmobile.shared.bd.DBProspect;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirenciaAgendada;
import com.axys.redeflexmobile.shared.bd.DBVisitaAdquirencia;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectAttachment;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCatalog;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQuality;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

/**
 * @author Rogério Massa on 03/01/19.
 */

public class VisitProspectManagerImpl implements VisitProspectManager {

    private DBColaborador dbColaborador;
    private DBCliente dbCliente;
    private DBMotivoAdquirencia dbMotivoAdquirencia;
    private DBPerguntasQualidade dbPerguntasQualidade;
    private DBVisitaAdquirencia dbVisitaAdquirencia;
    private DBCatalogoAdquirencia dbCatalogoAdquirencia;
    private DBProspect dbProspect;
    private DBRotaAdquirencia dbRotaAdquirencia;
    private DBRotaAdquirenciaAgendada dbRotaAdquirenciaAgendada;
    private DBOs dbOs;

    public VisitProspectManagerImpl(DBColaborador dbColaborador,
                                    DBCliente dbCliente,
                                    DBMotivoAdquirencia dbMotivoAdquirencia,
                                    DBPerguntasQualidade dbPerguntasQualidade,
                                    DBVisitaAdquirencia dbVisitaAdquirencia,
                                    DBCatalogoAdquirencia dbCatalogoAdquirencia,
                                    DBProspect dbProspect,
                                    DBRotaAdquirencia dbRotaAdquirencia,
                                    DBRotaAdquirenciaAgendada dbRotaAdquirenciaAgendada,
                                    DBOs dbOs) {
        this.dbColaborador = dbColaborador;
        this.dbCliente = dbCliente;
        this.dbMotivoAdquirencia = dbMotivoAdquirencia;
        this.dbPerguntasQualidade = dbPerguntasQualidade;
        this.dbVisitaAdquirencia = dbVisitaAdquirencia;
        this.dbCatalogoAdquirencia = dbCatalogoAdquirencia;
        this.dbProspect = dbProspect;
        this.dbRotaAdquirencia = dbRotaAdquirencia;
        this.dbRotaAdquirenciaAgendada = dbRotaAdquirenciaAgendada;
        this.dbOs = dbOs;
    }

    @Override
    public Single<RouteClientProspect> getClient(int clientId) {
        return Single.just(dbCliente.getById(String.valueOf(clientId)))
                .flatMap(response -> Single.just(new RouteClientProspect(response)));
    }

    @Override
    public Single<RouteClientProspect> getClientProspect(int clientId) {
        return Single.just(dbProspect.pegarPorId(clientId));
    }

    @Override
    public Single<Colaborador> getSalesman() {
        return Single.just(dbColaborador.get());
    }

    @Override
    public Single<List<VisitProspectCatalog>> getCatalogImages() {
        return Single.just(dbCatalogoAdquirencia.pegarTodos());
    }

    @Override
    public Single<List<VisitProspectCancelReason>> getReasons() {
        return Single.just(dbMotivoAdquirencia.pegarTodas());
    }

    @Override
    public Single<List<VisitProspectQualityQuestion>> getQuality() {
        return Single.just(dbPerguntasQualidade.pegarTodas());
    }

    @Override
    public Completable cancelVisit(VisitProspect visitProspect,
                                   VisitProspectAttachment visitProspectAttachment,
                                   VisitProspectCancelReason reason) {

        RoutesProspect routesProspect = dbRotaAdquirencia.pegarPorId(visitProspect.getIdRoute());
        if (reason.isSchedule()
                && routesProspect.getIdScheduled() == null
                && routesProspect.getVisitCount() == EMPTY_INT
                && routesProspect.getDayOfWeek() == DateUtils.getDayOfWeek()) {

            RoutesProspect routesProspectScheduled = new RoutesProspect(routesProspect);
            routesProspectScheduled.setIdScheduled(visitProspect.getIdRoute());

            return Completable.create(emitter -> {
                try {
                    long idRouteScheduled = dbRotaAdquirenciaAgendada.salvar(routesProspectScheduled);
                    visitProspect.setIdRouteScheduled((int) (long) idRouteScheduled);
                    long idVisit = dbVisitaAdquirencia.salvarVisita(visitProspect);
                    saveVisitAttachment(idVisit, visitProspectAttachment);
                    emitter.onComplete();
                } catch (Exception e) {
                    Timber.e(e);
                    emitter.onError(e);
                }
            });
        } else if (reason.isSchedule()) {
            RoutesProspect routesProspectScheduled = dbRotaAdquirenciaAgendada.pegarPorId(routesProspect.getIdScheduled());
            if (routesProspectScheduled != null) {
                visitProspect.setIdRouteScheduled(routesProspectScheduled.getId());
            }
        }

        return Completable.create(emitter -> {
            try {
                long idVisit = dbVisitaAdquirencia.salvarVisita(visitProspect);
                saveVisitAttachment(idVisit, visitProspectAttachment);
                emitter.onComplete();
            } catch (Exception e) {
                Timber.e(e);
                emitter.onError(e);
            }
        });
    }

    @Override
    public Completable saveVisit(VisitProspect visitProspect,
                                 VisitProspectAttachment visitProspectAttachment) {
        return Completable.create(emitter -> {
            try {
                long idVisit = dbVisitaAdquirencia.salvarVisita(visitProspect);
                saveVisitAttachment(idVisit, visitProspectAttachment);
                emitter.onComplete();
            } catch (Exception e) {
                Timber.e(e);
                emitter.onError(e);
            }
        });
    }

    @Override
    public Completable saveVisitQuality(VisitProspect visitProspect,
                                        List<VisitProspectQualityQuestion> answers,
                                        List<VisitProspectQualityQuestion> questions,
                                        VisitProspectAttachment visitProspectAttachment) {
        return Completable.create(emitter -> {
            try {
                long idVisit = dbVisitaAdquirencia.salvarVisita(visitProspect);
                saveVisitAttachment(idVisit, visitProspectAttachment);
                VisitProspectQuality visitProspectQuality = new VisitProspectQuality();
                Gson gson = Utilidades.getGson();
                visitProspectQuality.setIdVisit(idVisit);
                visitProspectQuality.setAnswers(gson.toJson(answers));
                visitProspectQuality.setQuestions(gson.toJson(questions));
                dbVisitaAdquirencia.salvarVisitaQualidade(visitProspectQuality);
                emitter.onComplete();
            } catch (Exception e) {
                Timber.e(e);
                emitter.onError(e);
            }
        });
    }

    private void saveVisitAttachment(long idVisit, VisitProspectAttachment visitProspectAttachment) {
        visitProspectAttachment.setIdVisit(idVisit);
        dbVisitaAdquirencia.salvarVisitaAnexo(visitProspectAttachment);
    }

    @Override
    public Boolean verifyOSWithoutScheduling() {
        return dbOs.existeOsPendenteAgendamento();
    }

    @Override
    public RouteClientProspect getCustomerUpdated(String customerId) {
        return new RouteClientProspect(dbCliente.getById(String.valueOf(customerId)));
    }
}
