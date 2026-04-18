package com.axys.redeflexmobile.ui.component.customspinner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.fragment.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinnerDialog.ICustomSpinnerDialogCallback;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;
import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

/**
 * @author Bruno Pimentel on 23/11/18.
 */
@SuppressLint({"NonConstantResourceId", "CheckResult"})
public class CustomSpinner extends RelativeLayout implements ICustomSpinnerDialogCallback {

    private static final int DEFAULT_EMPTY_INT = 0;

    @BindView(R.id.cmp_spinner_rl_container) RelativeLayout rlFieldContainer;
    @BindView(R.id.cmp_spinner_tv_label) TextView tvLabel;
    @BindView(R.id.cmp_spinner_tv_field) TextView tvField;
    @BindView(R.id.cmp_spinner_ll_container) LinearLayout llContainer;
    @BindView(R.id.cmp_spinner_error_tv_label) TextView tvErrorLabel;
    @BindView(R.id.cmp_spinner_error_ll_container) LinearLayout llErrorContainer;
    @BindView(R.id.cmp_spinner_iv_arrow) ImageView ivArrow;

    private String cmpHint;
    private String cmpPlaceholder;
    private String cmpErrorLabel;
    private boolean cmpEnabled = true;

    private List<ICustomSpinnerDialogModel> list;
    private ICustomSpinnerDialogModel selectedItem;
    private ICustomSpinnerCallback callback;

    private CompositeDisposable compositeDisposable;

    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ComponentSpinner, DEFAULT_EMPTY_INT, DEFAULT_EMPTY_INT);
        try {
            this.cmpHint = attributes.getString(R.styleable.ComponentSpinner_cmpSpinnerHint);
            this.cmpPlaceholder = attributes.getString(R.styleable.ComponentSpinner_cmpSpinnerPlaceholder);
            this.cmpErrorLabel = attributes.getString(R.styleable.ComponentSpinner_cmpSpinnerErrorLabel);
            this.cmpEnabled = attributes.getBoolean(R.styleable.ComponentSpinner_cmpSpinnerEnabled, true);
        } finally {
            attributes.recycle();
        }
        initialize(context);
    }

    public void setCallback(ICustomSpinnerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        ivArrow.setVisibility(enabled ? VISIBLE : GONE);
        rlFieldContainer.setBackgroundResource(enabled
                ? R.drawable.component_edit_text
                : R.drawable.component_edit_text_disabled);
    }

    @Override
    public void onItemSelected(ICustomSpinnerDialogModel item) {
        if (selectedItem != null && item.getDescriptionValue().equals(selectedItem.getDescriptionValue())) {
            return;
        }
        doSelect(item);
        if (callback != null) callback.onNext(item);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View view = inflater.inflate(R.layout.component_spinner, this);
        ButterKnife.bind(this, view);

        initializeCompositeDisposable();
        customize();
        setEnabled(cmpEnabled);
    }

    private void customize() {
        tvLabel.setText(cmpHint);
        tvField.setHint(cmpPlaceholder);

        if (cmpErrorLabel != null) {
            tvErrorLabel.setText(cmpErrorLabel);
        }
    }

    private void showDialog() {
        if (list == null) {
            return;
        }

        CustomSpinnerDialog dialog = CustomSpinnerDialog.newInstance(list, this);
        final FragmentActivity activity = (FragmentActivity) getContext();
        if (activity == null) {
            return;
        }
        dialog.show(activity.getSupportFragmentManager(), dialog.getClass().getSimpleName());
    }

    public void initializeCompositeDisposable() {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                RxView.clicks(this)
                        .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(o -> showDialog(), throwable -> {
                        })
        );
    }

    public List<ICustomSpinnerDialogModel> getList() {
        return list;
    }

    public void setList(List<ICustomSpinnerDialogModel> list) {
        this.list = new ArrayList<>();
        if (list != null) {
            this.list = Stream.of(list)
                    .filter(value -> value != null &&
                            StringUtils.isNotEmpty(value.getDescriptionValue()))
                    .toList();
        }
    }

    public void doSelect(ICustomSpinnerDialogModel selectedItem) {
        if (selectedItem == null) {
            removeSelection();
            return;
        }
        this.selectedItem = selectedItem;
        String text = selectedItem.getDescriptionValue();
        tvField.setText(text != null ? text : cmpPlaceholder);
        hideError();
    }

    public void doSelectWithCallback(Integer selectedItemId) {
        try {
            if (selectedItemId == null) {
                removeSelection();
                return;
            }
            for (ICustomSpinnerDialogModel object : getList()) {
                if (object.getIdValue().equals(selectedItemId)) {
                    onItemSelected(object);
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public void doSelectWithCallback(String selectedItemValue) {
        if (selectedItemValue == null) {
            removeSelection();
            return;
        }
        for (ICustomSpinnerDialogModel object : getList()) {
            if (object.getDescriptionValue().equals(selectedItemValue)) {
                onItemSelected(object);
            }
        }
    }

    public void removeSelection() {
        selectedItem = null;
        tvField.setText(EMPTY_STRING);
    }

    public ICustomSpinnerDialogModel getSelectedItem() {
        return selectedItem;
    }

    public Integer getSelectedItemId() {
        if (getSelectedItem() == null) {
            return null;
        }
        return getSelectedItem().getIdValue();
    }

    public String getSelectedItemDescription() {
        if (getSelectedItem() == null) {
            return EMPTY_STRING;
        }
        return getSelectedItem().getDescriptionValue();
    }

    public void showError() {
        showError(getContext().getString(R.string.cpt_edit_text_error_obligatory));
    }

    public void showError(String error) {
        if (error != null) tvErrorLabel.setText(error);
        llErrorContainer.setVisibility(VISIBLE);
    }

    public void showError(Integer error) {
        if (error != null) tvErrorLabel.setText(getContext().getString(error));
        llErrorContainer.setVisibility(VISIBLE);
    }

    public void hideError() {
        llErrorContainer.setVisibility(GONE);
    }

    public void hideVisibility() {
        this.setVisibility(GONE);
    }

    public void hideVisibilityAndClearValue() {
        this.setVisibility(GONE);
        removeSelection();
    }

    public void showVisibility() {
        this.setVisibility(VISIBLE);
    }

    public void dispose() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    public interface ICustomSpinnerCallback {
        void onNext(ICustomSpinnerDialogModel item);
    }
}
