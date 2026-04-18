package com.axys.redeflexmobile.shared.di.module;

import android.content.Context;

import com.axys.redeflexmobile.shared.services.LoginService;
import com.axys.redeflexmobile.shared.services.PendenciaClienteService;
import com.axys.redeflexmobile.shared.services.ClienteService;
import com.axys.redeflexmobile.shared.services.PendenciaService;
import com.axys.redeflexmobile.shared.services.RegisterService;
import com.axys.redeflexmobile.shared.services.PendenciaMotivoService;
import com.axys.redeflexmobile.shared.services.SolicitacaoPDService;
import com.axys.redeflexmobile.shared.services.bus.ComprovanteSkyTaBus;
import com.axys.redeflexmobile.shared.services.bus.ComprovanteSkyTaBusImpl;
import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApiRetrofit;
import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApiService;
import com.axys.redeflexmobile.shared.services.migracao.MotiveMigrationSubService;
import com.axys.redeflexmobile.shared.services.migracao.RegisterMigrationSubService;
import com.axys.redeflexmobile.shared.services.network.PosNetwork;
import com.axys.redeflexmobile.shared.services.network.RankingService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ServiceModule {

    @Provides
    ComprovanteSkyTaBus provideComprovanteSkyTa(Context context) {
        return new ComprovanteSkyTaBusImpl(context);
    }

    @Provides
    ClienteService provideClienteService(Retrofit retrofit) {
        return retrofit.create(ClienteService.class);
    }

    @Provides
    RankingService provideRankingService(Retrofit retrofit) {
        return retrofit.create(RankingService.class);
    }

    @Provides
    RegisterService provideRegisterService(Retrofit retrofit) {
        return retrofit.create(RegisterService.class);
    }

    @Provides
    GoogleMapsApiService provideGoogleMapsApiService(GoogleMapsApiRetrofit googleMapsApiRetrofit) {
        return googleMapsApiRetrofit.getRetrofit().create(GoogleMapsApiService.class);
    }

    @Provides
    PosNetwork providePosNetwork(Retrofit retrofit) {
        return retrofit.create(PosNetwork.class);
    }

    @Provides
    PendenciaService providePendenciaService(Retrofit retrofit) {
        return retrofit.create(PendenciaService.class);
    }

    @Provides
    SolicitacaoPDService provideSolicitacaoPDService(Retrofit retrofit) {
        return retrofit.create(SolicitacaoPDService.class);
    }

    @Provides
    PendenciaMotivoService providePendenciaMotivoService(Retrofit retrofit) {
        return retrofit.create(PendenciaMotivoService.class);
    }

    @Provides
    PendenciaClienteService providePendenciaClienteService(Retrofit retrofit) {
        return retrofit.create(PendenciaClienteService.class);
    }

    @Provides
    RegisterMigrationSubService provideRegisterMigrationSubService(Retrofit retrofit) {
        return retrofit.create(RegisterMigrationSubService.class);
    }

    @Provides
    MotiveMigrationSubService provideMotiveMigrationSubService(Retrofit retrofit) {
        return retrofit.create(MotiveMigrationSubService.class);
    }

    @Provides
    LoginService provideLoginService(Retrofit retrofit) {
        return retrofit.create(LoginService.class);
    }
}
