package com.axys.redeflexmobile.shared.services.bus.registerrate;

import android.content.Context;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.registerrate.DBProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.bd.registerrate.DBRegistrationSimulationFee;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.models.registerrate.RegistrationSimulationFee;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.bus.BaseBus;
import com.axys.redeflexmobile.shared.services.network.util.JsonUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.net.URL;
import java.util.List;

/**
 * @author Lucas Marciano on 24/04/2020
 */
public class ProspectingClientAcquisitionBus extends BaseBus {

    public static void send(Context context) {
        DBProspectingClientAcquisition db = new DBProspectingClientAcquisition(context);
        DBRegistrationSimulationFee dbFee = new DBRegistrationSimulationFee(context);
        try {
            List<ProspectingClientAcquisition> list = db.getNoSync();
            Stream.ofNullable(list).forEach(prospect -> {
                try {
                    if (prospect.getCompleteName().length() > 0 && prospect.getFantasyName().length() > 0) {
                        List<RegistrationSimulationFee> taxes = dbFee.getAllTaxesByProspectId(prospect.getId());
                        prospect.setTaxes(taxes);
                        String request = JsonUtils.getGsonInstance().toJson(removeSpecialCharacters(prospect));
                        String response = Utilidades.putRegistros(new URL(URLs.PROSPECT_CLIENT_ACQUISITION), request);
                        if (response != null && !response.equals("-1")) {
                            db.updateSync(prospect.getId());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ProspectingClientAcquisition removeSpecialCharacters(ProspectingClientAcquisition prospect) {
        prospect.setCompleteName(Util_IO.retiraAcento(prospect.getCompleteName()));
        prospect.setFantasyName(Util_IO.retiraAcento(prospect.getFantasyName()));
        prospect.setEmail(Util_IO.retiraAcento(prospect.getEmail()));
        return prospect;
    }
}
