package com.axys.redeflexmobile.ui.register.customer.address;

import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Rogério Massa on 07/02/19.
 */

public class RegisterCustomerAddressListViewHolder extends BaseViewHolder<CustomerRegisterAddressType> {

    @BindView(R.id.customer_register_address_list_item_cv_container) CardView cvContainer;
    @BindView(R.id.customer_register_address_list_item_rl_container) RelativeLayout rlContainer;
    @BindView(R.id.customer_register_address_list_item_tv_description) TextView tvDescription;
    @BindView(R.id.customer_register_address_list_item_tv_flag) TextView tvFlag;
    @BindView(R.id.customer_register_address_list_item_iv_check) ImageView ivCheck;

    private RegisterCustomerAddressListViewHolderListener callback;

    RegisterCustomerAddressListViewHolder(View itemView,
                                          IBaseViewHolderListener baseViewHolderListener,
                                          RegisterCustomerAddressListViewHolderListener callback) {
        super(itemView, baseViewHolderListener);
        this.callback = callback;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(CustomerRegisterAddressType object, int position) {
        super.bind(object, position);

        tvDescription.setText(object.getType().getDescription());
        ivCheck.setVisibility(object.isChecked() ? View.VISIBLE : View.GONE);
        tvFlag.setText(object.isRequired()
                ? tvFlag.getContext().getString(R.string.customer_register_address_list_item_required)
                : tvFlag.getContext().getString(R.string.customer_register_address_list_item_optional));

        cvContainer.setLayoutParams(prepareMargins(rlContainer.getContext(),
                baseViewHolderListener.isTheLast(position)));

        if (callback != null && callback.getCompositeDisposable() != null) {
            callback.getCompositeDisposable()
                    .add(RxView.clicks(rlContainer)
                            .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(o -> callback.onClickItem(object), throwable -> {
                            }));
        }
    }

    public interface RegisterCustomerAddressListViewHolderListener {
        CompositeDisposable getCompositeDisposable();

        void onClickItem(CustomerRegisterAddressType addressType);
    }
}