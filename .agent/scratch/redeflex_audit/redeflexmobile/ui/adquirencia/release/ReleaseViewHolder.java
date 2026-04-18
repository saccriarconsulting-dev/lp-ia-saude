package com.axys.redeflexmobile.ui.adquirencia.release;

import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.Release;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;

/**
 * @author Rogério Massa on 04/12/18.
 */

public class ReleaseViewHolder extends BaseViewHolder<Release> {

    @BindView(R.id.release_item_cv_container) CardView cvContainer;
    @BindView(R.id.release_item_tv_machine) TextView tvMachine;

    private BaseViewHolder.IBaseViewHolderListener listener;
    private ReleaseViewHolderListener clickListener;

    ReleaseViewHolder(View itemView,
                      IBaseViewHolderListener listener,
                      ReleaseViewHolderListener clickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        this.clickListener = clickListener;
    }

    @Override
    public void bind(Release object, int position) {
        tvMachine.setText(object.getMachine());
        cvContainer.setLayoutParams(prepareMargins(cvContainer.getContext(),
                listener.isTheLast(position)));

        if (clickListener != null && clickListener.getCompositeDisposable() != null) {
            clickListener.getCompositeDisposable()
                    .add(RxView.clicks(cvContainer)
                            .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(o -> clickListener.onClickItem(object), throwable -> {
                            }));
        }
    }

    public interface ReleaseViewHolderListener {
        CompositeDisposable getCompositeDisposable();

        void onClickItem(Release release);
    }
}
