package com.axys.redeflexmobile.ui.adquirencia.ranking.list;

import android.content.Context;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRankingStatus;
import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;

/**
 * @author Rogério Massa on 22/11/18.
 */

class RankingListViewHolder extends BaseViewHolder<Ranking> {

    @BindView(R.id.register_item_cv_container) CardView cvContainer;
    @BindView(R.id.ranking_item_tv_name) TextView tvName;
    @BindView(R.id.ranking_item_tv_status) TextView tvStatus;
    @BindView(R.id.ranking_item_iv_alert) ImageView ivAlert;
    @BindView(R.id.ranking_item_ll_body) LinearLayout llBody;
    @BindView(R.id.ranking_item_tv_revenues) TextView tvRevenues;
    @BindView(R.id.ranking_item_tv_billing) TextView tvBilling;

    private Context context;
    private IBaseViewHolderListener listener;
    private IBaseViewHolderExpandableListener adapterListener;
    private RankingListViewHolderListener viewListener;

    RankingListViewHolder(View itemView,
                          IBaseViewHolderListener listener,
                          IBaseViewHolderExpandableListener adapterListener,
                          RankingListViewHolderListener viewListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        this.adapterListener = adapterListener;
        this.viewListener = viewListener;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(Ranking ranking, int position) {
        super.bind(ranking, position);

        cvContainer.setLayoutParams(prepareMargins(cvContainer.getContext(), listener.isTheLast(position)));
        cvContainer.setOnClickListener(view -> adapterListener.openClose(position));

        boolean isOpened = adapterListener.isOpened(position);
        llBody.setVisibility(isOpened ? View.VISIBLE : View.GONE);
        ivAlert.setVisibility(isOpened ? View.VISIBLE : View.GONE);
        if (viewListener != null && viewListener.getCompositeDisposable() != null) {
            viewListener.getCompositeDisposable()
                    .add(RxView.clicks(ivAlert)
                            .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(o -> viewListener.onClickItem(ranking), throwable -> {
                            }));
        }


        tvName.setText(ranking.getFantasyName());
        EnumRankingStatus status = EnumRankingStatus.getEnumByValue(ranking.getStatus());
        tvStatus.setText(context.getString(R.string.frg_main_ranking_item_status, ranking.getStatus()));
        tvStatus.setBackgroundResource(status.getBackground());

        tvRevenues.setText(context.getString(R.string.frg_main_ranking_item_revenues,
                StringUtils.maskCurrencyDouble(ranking.getExpectedRevenue().toString())));
        tvBilling.setText(context.getString(R.string.frg_main_ranking_item_billing,
                StringUtils.maskCurrencyDouble(ranking.getRevenue().toString())));
    }

    public interface RankingListViewHolderListener {
        CompositeDisposable getCompositeDisposable();

        void onClickItem(Ranking ranking);
    }
}
