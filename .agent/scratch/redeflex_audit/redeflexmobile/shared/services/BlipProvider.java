package com.axys.redeflexmobile.shared.services;

import android.content.Context;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.util.DeviceUtils;

import net.take.blipchat.AuthType;
import net.take.blipchat.BlipClient;
import net.take.blipchat.models.Account;
import net.take.blipchat.models.AuthConfig;
import net.take.blipchat.models.BlipOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BlipProvider {

    private Context context;

    public BlipProvider(Context context) {
        this.context = context;
    }

    public void initializeService(int colaboradorId, String colaboradorNome) {
        try {
            AuthConfig authConfig = new AuthConfig(
                    AuthType.Dev,
                    String.valueOf(colaboradorId),
                    "pass" + colaboradorId
            );

            Map<String, String> extras = getBlibFcmToken();

            Account account = new Account();
            account.setFullName(colaboradorNome);
            account.setExtras(extras);

            BlipOptions blipOptions = new BlipOptions();
            blipOptions.setAuthConfig(authConfig);
            blipOptions.setAccount(account);

            BlipClient.openBlipThread(context, BuildConfig.CHATBOT_BLIP_KEY, blipOptions);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private Map<String, String> getBlibFcmToken() {
        Map<String, String> extras = new HashMap<>();
        String fcmUserToken = DeviceUtils.getStringInSharedPreferences(context, DeviceUtils.PREFS_FCM_TOKEN);
        extras.put("#inbox.forwardTo", String.format("%s@firebase.gw.msging.net", fcmUserToken));
        return extras;
    }

    public static void callChatBot(Context context) {
        Colaborador colaborador = new DBColaborador(context).get();
        new BlipProvider(context).initializeService(colaborador.getId(), colaborador.getNome());
    }
}
