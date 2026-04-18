package com.axys.redeflexmobile.ui.register.customer.attachment;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 14/01/19.
 */

@Module
public class RegisterCustomerAttachmentFragmentModule {

    @Provides
    RegisterCustomerAttachmentPresenter providePresenter(RegisterCustomerAttachmentFragment view,
                                                         SchedulerProvider schedulerProvider,
                                                         ExceptionUtils exceptionUtils) {
        return new RegisterCustomerAttachmentPresenterImpl(view, schedulerProvider, exceptionUtils);
    }

    @Provides
    RegisterCustomerAttachmentAdapter provideAdapter(RegisterCustomerAttachmentFragment callback) {
        return new RegisterCustomerAttachmentAdapter(callback);
    }
}
