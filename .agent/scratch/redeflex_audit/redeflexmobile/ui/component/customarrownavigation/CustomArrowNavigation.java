package com.axys.redeflexmobile.ui.component.customarrownavigation;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;
import static com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigationItem.CustomArrowNavigationItemPosition.BEGIN;
import static com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigationItem.CustomArrowNavigationItemPosition.END;
import static com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigationItem.CustomArrowNavigationItemPosition.MIDDLE;
import static com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigationItem.getImageView;

/**
 * @author Bruno Pimentel on 21/11/18.
 */

public class CustomArrowNavigation extends LinearLayout {

    private static final int NUMBER_ZERO = 0;
    private static final int NUMBER_ONE = 1;

    private static final int PREVIOUS = 0;
    private static final int NEXT = 1;

    @BindView(R.id.cpt_arrow_navigation_tv_previous) TextView tvPrevious;
    @BindView(R.id.cpt_arrow_navigation_ll_controls) LinearLayout llControls;
    @BindView(R.id.cpt_arrow_navigation_tv_next) TextView tvNext;

    private Context context;
    private CustomArrowNavigationListener callback;
    private CustomArrowNavigationItem selectedItem;
    private List<CustomArrowNavigationItem> listItems;
    private CompositeDisposable compositeDisposable;

    private PublishSubject<Integer> nextPublisher = PublishSubject.create();
    private PublishSubject<Integer> previousPublisher = PublishSubject.create();

    public CustomArrowNavigation(Context context) {
        super(context);
        this.initialize(context);
    }

    public CustomArrowNavigation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context);
    }

    public void setListItems(List<CustomArrowNavigationItem> listItems) {
        this.listItems = listItems;
        prepareLayout();
    }

    public void setCallback(CustomArrowNavigationListener callback) {
        this.callback = callback;
    }

    public Fragment getTargetToPrevious() {
        selectedItem = listItems.get(selectedItem.getIndex() - NUMBER_ONE);
        prepareLayout();
        return selectedItem.getTarget();
    }

    public Fragment getTargetToNext() {
        selectedItem = listItems.get(selectedItem.getIndex() + NUMBER_ONE);
        prepareLayout();
        return selectedItem.getTarget();
    }

    public CustomArrowNavigationItem getSelectedItem() {
        return selectedItem;
    }

    private boolean isValidCount() {
        llControls.removeAllViews();
        if (listItems == null || listItems.isEmpty()) {
            tvPrevious.setVisibility(INVISIBLE);
            tvNext.setVisibility(INVISIBLE);
            return false;
        }
        return true;
    }

    private void makeNextButton() {
        tvNext.setText(context.getString(R.string.arrow_navigation_next_next));
        tvNext.setCompoundDrawablesWithIntrinsicBounds(NUMBER_ZERO, NUMBER_ZERO,
                R.drawable.ic_arrow_right_primary_dark_wrapped, NUMBER_ZERO);
    }

    private void makeConclusionButton() {
        tvNext.setText(context.getString(R.string.arrow_navigation_next_finish));
        tvNext.setCompoundDrawablesWithIntrinsicBounds(NUMBER_ZERO, NUMBER_ZERO,
                R.drawable.ic_arrow_right_white_wraped, NUMBER_ZERO);
    }

    private void initialize(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View view = inflater.inflate(R.layout.component_arrow_navigation, this);
        ButterKnife.bind(this, view);

        compositeDisposable = new CompositeDisposable();
        createEvents();

        Disposable disposable = Observable.merge(previousPublisher, nextPublisher)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(btn -> {
                    if (btn == NEXT) {
                        if (callback != null) {
                            callback.moveToNextFragment();
                        }
                    }

                    if (btn == PREVIOUS) {
                        if (callback != null) {
                            callback.moveToPreviousFragment();
                        }
                    }
                }, Timber::e);

        compositeDisposable.add(disposable);
    }

    private void createEvents() {
        tvNext.setOnClickListener(v -> nextPublisher.onNext(NEXT));
        tvPrevious.setOnClickListener(v -> previousPublisher.onNext(PREVIOUS));
    }

    private void prepareLayout() {
        if (!isValidCount()) return;

        for (int i = NUMBER_ZERO; i < listItems.size(); i++) {
            listItems.get(i).setIndex(i);
            if (selectedItem == null) selectedItem = listItems.get(i);
            llControls.addView(getImageView(context, selectedItem.getIndex() == i,
                    i == NUMBER_ZERO
                            ? BEGIN : i == listItems.size() - NUMBER_ONE
                            ? END : MIDDLE));
        }

        tvPrevious.setVisibility(selectedItem.getIndex() == NUMBER_ZERO ? INVISIBLE : VISIBLE);
        if (selectedItem.getIndex() == listItems.size() - NUMBER_ONE) makeConclusionButton();
        else makeNextButton();
    }

    public void initializeFirstFragment() {
        BaseActivityView activity = (BaseActivityView) context;
        if (activity == null) return;
        CustomArrowNavigationItem fragment = listItems.get(0);
        if (fragment == null) return;
        activity.openFragment(fragment.getTarget());
    }

    public void moveToPreviousFragment() {
        tvPrevious.performClick();
    }

    public void moveToNextFragment() {
        tvNext.performClick();
    }

    public void detach() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    public interface CustomArrowNavigationListener {
        void moveToPreviousFragment();

        void moveToNextFragment();

        void doComplete();
    }
}
