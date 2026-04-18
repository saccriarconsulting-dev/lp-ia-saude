package com.axys.redeflexmobile.shared.di.component;

import com.axys.redeflexmobile.shared.di.module.AppModule;
import com.axys.redeflexmobile.shared.di.module.BuildersModule;
import com.axys.redeflexmobile.shared.di.module.DaoModule;
import com.axys.redeflexmobile.shared.di.module.ManagerModule;
import com.axys.redeflexmobile.shared.di.module.RepositoryModule;
import com.axys.redeflexmobile.shared.di.module.ServiceModule;
import com.axys.redeflexmobile.shared.di.module.UtilModule;
import com.axys.redeflexmobile.ui.redeflex.RedeflexApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author diego on 04/10/17.
 */

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        DaoModule.class,
        ManagerModule.class,
        RepositoryModule.class,
        ServiceModule.class,
        UtilModule.class,
        BuildersModule.class
})
public interface AppComponent extends AndroidInjector<RedeflexApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(RedeflexApplication application);

        AppComponent build();
    }
}
