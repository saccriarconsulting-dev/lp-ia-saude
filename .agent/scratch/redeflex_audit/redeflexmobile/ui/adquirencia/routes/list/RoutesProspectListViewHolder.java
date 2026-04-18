package com.axys.redeflexmobile.ui.adquirencia.routes.list;

import android.graphics.drawable.Drawable;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRoutesProspectStatus;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static androidx.core.content.ContextCompat.getDrawable;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRoutesProspectType.ROUTE;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRoutesProspectType.SCHEDULED;
import static com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect.TYPE_QUALITY;
import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;

/**
 * @author Rogério Massa on 29/10/18.
 */

class RoutesProspectListViewHolder extends BaseViewHolder<RoutesProspect> {

    @BindView(R.id.routes_list_item_cv_container) CardView cvContainer;
    @BindView(R.id.routes_list_item_tv_type) TextView tvType;
    @BindView(R.id.routes_list_item_tv_status) TextView tvStatus;
    @BindView(R.id.routes_list_item_tv_quality) TextView tvQuality;
    @BindView(R.id.routes_list_item_iv_check) ImageView ivCheck;
    @BindView(R.id.routes_list_item_tv_name) TextView tvClientName;
    @BindView(R.id.routes_list_item_tv_address) TextView tvClientAddress;

    private BaseViewHolder.IBaseViewHolderListener listener;
    private RoutesProspectListViewHolderListener clickListener;

    RoutesProspectListViewHolder(View itemView,
                                 IBaseViewHolderListener listener,
                                 RoutesProspectListViewHolderListener clickListener) {
        super(itemView);
        this.listener = listener;
        this.clickListener = clickListener;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(RoutesProspect route, int position) {
        super.bind(route, position);

        tvClientName.setText(route.getCustomerName());
        tvClientAddress.setText(route.getCustomerAddress());

        prepareType(route.getIdScheduled() != null);
        EnumRoutesProspectStatus status = EnumRoutesProspectStatus.getEnumByValue(route.getStatus());
        prepareStatus(status);

        tvQuality.setVisibility(route.getTypeAttendance() == TYPE_QUALITY
                ? View.VISIBLE : View.GONE);

        cvContainer.setLayoutParams(prepareMargins(cvContainer.getContext(),
                listener.isTheLast(position)));

        if (clickListener != null && clickListener.getCompositeDisposable() != null) {
            clickListener.getCompositeDisposable().add(RxView.clicks(cvContainer)
                    .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                    .subscribe(o -> clickListener.onClickItem(route), Timber::d));
        }
    }

    private void prepareType(boolean isScheduled) {
        String text;
        Drawable background;
        if (isScheduled) {
            text = SCHEDULED.getTitle();
            background = getDrawable(tvStatus.getContext(), SCHEDULED.getBackground());
        } else {
            text = ROUTE.getTitle();
            background = getDrawable(tvStatus.getContext(), ROUTE.getBackground());
        }
        tvType.setText(text);
        tvType.setBackground(background);
    }

    private void prepareStatus(EnumRoutesProspectStatus status) {
        ivCheck.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);

        if (status == null) return;
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(status.getTitle());
        tvStatus.setBackground(getDrawable(tvStatus.getContext(), status.getBackground()));

        if (status.equals(EnumRoutesProspectStatus.COMPLETED)) {
            ivCheck.setVisibility(View.VISIBLE);
            ivCheck.setImageResource(status.getFlag());
        }
    }

    public interface RoutesProspectListViewHolderListener {
        CompositeDisposable getCompositeDisposable();

        void onClickItem(RoutesProspect route);
    }
}
