package com.axys.redeflexmobile.ui.adquirencia.visit.quality;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup.LayoutParams;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectView;
import com.axys.redeflexmobile.ui.base.BaseFragment;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
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

public class VisitProspectQualityFragment extends BaseFragment<VisitProspectView> implements
        VisitProspectQualityView,
        CompoundButton.OnCheckedChangeListener {

    @Inject VisitProspectQualityPresenter presenter;

    @BindView(R.id.visit_prospect_quality_ll_options) LinearLayout llOptions;
    @BindView(R.id.visit_prospect_quality_bt_cancel) Button btCancel;
    @BindView(R.id.visit_prospect_quality_bt_confirm) Button btConfirm;

    private List<VisitProspectQualityQuestion> selectedAnswers;
    private CompositeDisposable compositeDisposable;

    public static VisitProspectQualityFragment newInstance() {
        return new VisitProspectQualityFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_visit_prospect_quality;
    }

    @Override
    public void initialize() {
        parentActivity.setToolbarTitle(getString(R.string.visit_prospect_quality_title));
        this.selectedAnswers = new ArrayList<>();

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RxView.clicks(btCancel)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> onCancelClick(), throwable -> {
                }));
        compositeDisposable.add(RxView.clicks(btConfirm)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> onConfirmClick(), throwable -> {
                }));

        presenter.getQuestions();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void fillReasons(List<VisitProspectQualityQuestion> reasons) {
        llOptions.removeAllViews();
        Stream.ofNullable(reasons).forEach(reason -> {
            CheckBox checkBoxButton = new CheckBox(requireContext());
            checkBoxButton.setOnCheckedChangeListener(this);
            checkBoxButton.setText(reason.getDescription());

            LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            final int MARGIN = (int) getResources().getDimension(R.dimen.spacing_normal);
            layoutParams.setMargins(EMPTY_INT, MARGIN, EMPTY_INT, EMPTY_INT);
            checkBoxButton.setLayoutParams(layoutParams);

            llOptions.addView(checkBoxButton);
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int index = llOptions.indexOfChild(buttonView);

        VisitProspectQualityQuestion question = presenter.getQuestionList().get(index);

        if (isChecked) {
            selectedAnswers.add(question);
        } else {
            selectedAnswers.remove(question);
        }
    }

    @Override
    public void onConfirmSuccess(List<VisitProspectQualityQuestion> answers,
                                 List<VisitProspectQualityQuestion> questions) {
        parentActivity.saveQualityVisit(answers, questions);
    }

    @Override
    public boolean validacaoDataAparelhoAdquirencia() {
        return Validacoes.validacaoDataAparelhoAdquirencia(getContext());
    }

    private void onCancelClick() {
        parentActivity.onBackPressed();
    }

    private void onConfirmClick() {
        List<VisitProspectQualityQuestion> questions = Stream.ofNullable(presenter.getQuestionList())
                .filter(question -> !selectedAnswers.contains(question))
                .toList();

        presenter.saveQualitySelected(selectedAnswers, questions);
    }
}
