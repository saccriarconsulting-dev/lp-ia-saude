package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectAttachment;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCatalog;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author Rogério Massa on 03/01/19.
 */

public interface VisitProspectManager {

    Single<RouteClientProspect> getClient(int clientId);

    Single<RouteClientProspect> getClientProspect(int clientId);

    Single<Colaborador> getSalesman();

    Single<List<VisitProspectCatalog>> getCatalogImages();

    Single<List<VisitProspectCancelReason>> getReasons();

    Single<List<VisitProspectQualityQuestion>> getQuality();

    Completable cancelVisit(VisitProspect visitProspect,
                            VisitProspectAttachment visitProspectAttachment,
                            VisitProspectCancelReason reason);

    Completable saveVisit(VisitProspect visitProspect, VisitProspectAttachment visitProspectAttachment);

    Completable saveVisitQuality(VisitProspect visitProspect,
                                 List<VisitProspectQualityQuestion> answers,
                                 List<VisitProspectQualityQuestion> questions,
                                 VisitProspectAttachment visitProspectAttachment);

    Boolean verifyOSWithoutScheduling();

    RouteClientProspect getCustomerUpdated(String customerId);
}
