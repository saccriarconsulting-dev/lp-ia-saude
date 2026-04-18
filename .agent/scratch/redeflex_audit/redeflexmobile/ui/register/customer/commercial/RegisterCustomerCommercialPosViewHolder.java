package com.axys.redeflexmobile.ui.register.customer.commercial;

import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;
import static com.axys.redeflexmobile.shared.util.NumberUtils.SINGLE_INT;

import android.view.View;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.customerregister.MachineType;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Bruno Pimentel on 23/11/18.
 */
public class RegisterCustomerCommercialPosViewHolder extends BaseViewHolder<MachineType> {

    @BindView(R.id.fourth_register_pos_item_ll_container) LinearLayout llContainer;
    @BindView(R.id.fourth_register_pos_item_et_details) CustomEditText etDetails;
    @BindView(R.id.fourth_register_pos_item_et_unitary_value) CustomEditText etUnitaryValue;
    @BindView(R.id.fourth_register_pos_item_ll_remove) LinearLayout llRemove;

    private RegisterCustomerCommercialPosViewHolderListener callback;

    RegisterCustomerCommercialPosViewHolder(View itemView,
                                            RegisterCustomerCommercialPosViewHolderListener adapterListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        callback = adapterListener;
    }

    @Override
    public void bind(MachineType object, int position) {
        super.bind(object, position);
        etDetails.setLabel(object.getModel());
        etDetails.setText(object.getDescription());
        etUnitaryValue.setText(StringUtils.maskCurrencyDouble(object.getSelectedRentValue()));

        int realPosition = position - SINGLE_INT;
        if (callback != null && callback.getCompositeDisposable() != null) {
            callback.getCompositeDisposable()
                    .add(RxView.clicks(llContainer)
                            .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(o -> callback.onPosSelected(object, realPosition), throwable -> {
                            }));
            callback.getCompositeDisposable()
                    .add(RxView.clicks(llRemove)
                            .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(o -> callback.onPosRemove(realPosition), throwable -> {
                            }));
        }
    }

    public interface RegisterCustomerCommercialPosViewHolderListener {
        CompositeDisposable getCompositeDisposable();

        void onPosSelected(MachineType machineType, Integer position);

        void onPosRemove(int position);
    }
}
