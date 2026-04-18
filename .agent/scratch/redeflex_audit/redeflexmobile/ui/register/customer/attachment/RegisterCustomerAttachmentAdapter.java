package com.axys.redeflexmobile.ui.register.customer.attachment;

import static com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentViewHolder.RegisterCustomerAttachmentCallback;

import android.view.View;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.ui.base.BaseAdapter;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public class RegisterCustomerAttachmentAdapter extends
        BaseAdapter<RegisterCustomerAttachmentAdapter.RegisterCustomerAttachmentAdapterItem,
                RegisterCustomerAttachmentViewHolder> {

    private RegisterCustomerAttachmentCallback callback;

    RegisterCustomerAttachmentAdapter(RegisterCustomerAttachmentCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getLayoutItem() {
        return R.layout.fragment_customer_register_attachment_item;
    }

    @Override
    public BaseViewHolder holder(View view) {
        return new RegisterCustomerAttachmentViewHolder(view, callback, this);
    }

    public static class RegisterCustomerAttachmentAdapterItem {

        EnumRegisterAttachmentType type;
        EnumRegisterCustomerType customerType;
        EnumRegisterPersonType personType;
        String image;
        String imageSize;
        Integer situation;
        String returnValue;

        public RegisterCustomerAttachmentAdapterItem(EnumRegisterAttachmentType type,
                                                     EnumRegisterCustomerType customerType,
                                                     EnumRegisterPersonType personType) {
            this.type = type;
            this.customerType = customerType;
            this.personType = personType;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RegisterCustomerAttachmentAdapterItem that = (RegisterCustomerAttachmentAdapterItem) o;

            if (type != that.type) return false;
            if (customerType != that.customerType) return false;
            if (personType != that.personType) return false;
            if (image != null ? !image.equals(that.image) : that.image != null) return false;
            return imageSize != null ? imageSize.equals(that.imageSize) : that.imageSize == null;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (customerType != null ? customerType.hashCode() : 0);
            result = 31 * result + (personType != null ? personType.hashCode() : 0);
            result = 31 * result + (image != null ? image.hashCode() : 0);
            result = 31 * result + (imageSize != null ? imageSize.hashCode() : 0);
            return result;
        }
    }
}
