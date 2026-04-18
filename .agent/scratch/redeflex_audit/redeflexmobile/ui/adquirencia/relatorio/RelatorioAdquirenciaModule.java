package com.axys.redeflexmobile.ui.adquirencia.relatorio;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * @author Vitor Herrmann on 04/01/19.
 */

@Module
public class RelatorioAdquirenciaModule {

    @Provides
    RelatorioAdquirenciaAdapter provideRelatorioAdquirenciaAdapter(Context context) {
        return new RelatorioAdquirenciaAdapter(context);
    }
}