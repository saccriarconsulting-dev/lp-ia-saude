package com.axys.redeflexmobile.ui.register.customer.attachment;

import android.content.Context;

import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;
import com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentAdapter.RegisterCustomerAttachmentAdapterItem;

import java.util.List;

/**
 * @author Rogério Massa on 14/01/19.
 */

public interface RegisterCustomerAttachmentView extends RegisterCustomerCommon {

    Context getContext();

    void setHeader(boolean acquisition);

    void fillAttachments(List<RegisterCustomerAttachmentAdapterItem> items);

    void showError(String error);

    void onValidationSuccess();

    void onValidationSuccessBack();

    void setNumeroTitulo(String pNumero);
}
