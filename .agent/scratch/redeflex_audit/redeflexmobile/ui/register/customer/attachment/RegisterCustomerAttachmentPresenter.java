package com.axys.redeflexmobile.ui.register.customer.attachment;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAttachment;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;
import com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentAdapter.RegisterCustomerAttachmentAdapterItem;

/**
 * @author Rogério Massa on 14/01/19.
 */

public interface RegisterCustomerAttachmentPresenter extends BasePresenter<RegisterCustomerAttachmentView> {

    void initializeData();

    void saveAttachment(CustomerRegisterAttachment item);

    void deleteAttachment(RegisterCustomerAttachmentAdapterItem item);

    void prepareAttachmentList();

    void doSave(boolean isBack);
}
