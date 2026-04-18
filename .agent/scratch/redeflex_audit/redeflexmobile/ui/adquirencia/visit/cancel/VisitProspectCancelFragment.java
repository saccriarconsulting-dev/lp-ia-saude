package com.axys.redeflexmobile.ui.adquirencia.visit.cancel;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectView;
import com.axys.redeflexmobile.ui.base.BaseFragment;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;

/**
 * @author Rogério Massa on 04/01/19.
 */

public class VisitProspectCancelFragment extends BaseFragment<VisitProspectView> implements
        VisitProspectCancelView, RadioGroup.OnCheckedChangeListener {

    @Inject VisitProspectCancelPresenter presenter;

    @BindView(R.id.visit_prospect_cancel_rg_options) RadioGroup rgOptions;
    @BindView(R.id.visit_prospect_cancel_cet_obs) CustomEditText cetCancelObs;
    @BindView(R.id.visit_prospect_cancel_bt_cancel) Button btCancel;
    @BindView(R.id.visit_prospect_cancel_bt_confirm) Button btConfirm;

    private List<VisitProspectCancelReason> reasons;
    private VisitProspectCancelReason selectedReason;
    private CompositeDisposable compositeDisposable;

    public static VisitProspectCancelFragment newInstance() {
        return new VisitProspectCancelFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_visit_prospect_cancel;
    }

    @Override
    public void initialize() {
        parentActivity.setToolbarTitle(getString(R.string.visit_prospect_cancel_title));

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RxView.clicks(btCancel)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> onCancelClick(), throwable -> {
                }));
        compositeDisposable.add(RxView.clicks(btConfirm)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> onConfirmClick(), throwable -> {
                }));

        rgOptions.setOnCheckedChangeListener(this);
        presenter.getReasons();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void fillReasons(List<VisitProspectCancelReason> reasons) {
        rgOptions.removeAllViews();
        this.reasons = reasons;
        Stream.ofNullable(reasons).forEach(reason -> {
            RadioButton radioButton = new RadioButton(requireContext());
            radioButton.setText(reason.getReason());
            radioButton.setTag(reason.getId());

            LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            final int MARGIN = (int) getResources().getDimension(R.dimen.spacing_normal);
            layoutParams.setMargins(EMPTY_INT, MARGIN, EMPTY_INT, EMPTY_INT);
            radioButton.setLayoutParams(layoutParams);

            rgOptions.addView(radioButton);
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        View radioButton = rgOptions.findViewById(checkedId);
        int index = rgOptions.indexOfChild(radioButton);
        selectedReason = reasons.get(index);
    }

    @Override
    public void onConfirmSuccess(VisitProspectCancelReason reason, String observation) {
        parentActivity.cancelVisit(reason, observation);
    }

    private void onCancelClick() {
        parentActivity.onBackPressed();
    }

    private void onConfirmClick() {
        presenter.saveReasonSelected(selectedReason, cetCancelObs.getText());
    }
}
