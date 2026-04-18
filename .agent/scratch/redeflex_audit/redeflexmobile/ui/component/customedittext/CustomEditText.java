package com.axys.redeflexmobile.ui.component.customedittext;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.component.customedittext.PhoneTextWatcher.PhoneTextWatcherListener;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_FLOAT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.DECIMAL_3D;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.PERCENT;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.getEnum;

/**
 * @author Rogério Massa on 20/11/18.
 */

public class CustomEditText extends LinearLayout implements OnFocusChangeListener,
        OnEditorActionListener,
        PhoneTextWatcherListener {

    private static final InputFilter emojiFiltered = (source, start, end, dest, dstart, dend) -> {
        for (int index = start; index < end; index++) {
            int type = Character.getType(source.charAt(index));
            if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                return EMPTY_STRING;
            }
        }
        return null;
    };

    @BindView(R.id.cmp_edit_text_ll_field_cointainer) LinearLayout llFieldContainer;
    @BindView(R.id.cmp_edit_text_tv_label) TextView tvFieldLabel;
    @BindView(R.id.cmp_edit_text_tv_actual_length) TextView tvActualLength;
    @BindView(R.id.cmp_edit_text_tv_footer_hint) TextView tvFooterHint;
    @BindView(R.id.cmp_edit_text_error_ll_container) LinearLayout llErrorContainer;
    @BindView(R.id.cmp_edit_text_error_tv_label) TextView tvErrorLabel;

    private EditText etField;
    private String cmpHint, cmpPlaceholder, cmpFooterHint, cmpErrorLabel;
    private int cmpMaxLength;
    private float cmpMinHeight;
    private boolean cmpActualLength, cmpIsCurrency, cmpShowCurrencySymbol, cmpFocusable, cmpEnabled, isShowingError;
    private ComponentEditTextInputType cmpInputType;
    private ComponentEditTextMask cmpMask;
    private ComponentEditTextImeOptionType cmpImeOption;
    private MaskedTextChangedListener oldMaskedTextChangedListener;
    private PhoneMask oldPhoneChangedListener;
    private CustomEditAfterTextListener afterTextListener;

    public CustomEditText(Context context) {
        super(context);
        initialize(context);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CustomEditText, EMPTY_INT, EMPTY_INT);
        try {
            initializeArgs(attributes);
        } finally {
            attributes.recycle();
        }
        initialize(context);
    }

    public void setAfterTextListener(CustomEditAfterTextListener afterTextListener) {
        this.afterTextListener = afterTextListener;
    }

    @Override
    public void onFocusChange(View view, boolean focused) {
        if (focused) {
            etField.post(() -> etField.setSelection(etField.getText().length()));
        }

        if (isShowingError) {
            hideError();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            etField.postDelayed(() -> etField.clearFocus(), 100);
        }
        return false;
    }

    @Override
    public void pauseListener() {
        removeTextChangedListener(oldMaskedTextChangedListener);
    }

    @Override
    public void unPauseListener() {
        addTextChangedListener(oldMaskedTextChangedListener);
    }

    private void initializeArgs(TypedArray attributes) {
        try {
            cmpHint = attributes.getString(R.styleable.CustomEditText_cmpEditTextHint);
            cmpPlaceholder = attributes.getString(R.styleable.CustomEditText_cmpEditTextPlaceholder);
            cmpActualLength = attributes.getBoolean(R.styleable.CustomEditText_cmpEditTextShowActualLength, false);
            cmpFooterHint = attributes.getString(R.styleable.CustomEditText_cmpEditTextFooterHint);
            cmpErrorLabel = attributes.getString(R.styleable.CustomEditText_cmpEditTextErrorLabel);
            cmpMaxLength = attributes.getInteger(R.styleable.CustomEditText_cmpEditTextMaxLength, EMPTY_INT);
            cmpMinHeight = attributes.getDimension(R.styleable.CustomEditText_cmpEditTextMinHeight, EMPTY_FLOAT);
            cmpInputType = ComponentEditTextInputType.getEnum(attributes.getInteger(R.styleable.CustomEditText_cmpEditTextInputType, EMPTY_INT));
            cmpMask = getEnum(attributes.getInteger(R.styleable.CustomEditText_cmpEditTextMask, EMPTY_INT));
            cmpIsCurrency = attributes.getBoolean(R.styleable.CustomEditText_cmpEditTextIsCurrency, false);
            cmpShowCurrencySymbol = attributes.getBoolean(R.styleable.CustomEditText_cmpEditTextShowCurrencySymbol, false);
            cmpImeOption = ComponentEditTextImeOptionType.getEnum(attributes.getInteger(R.styleable.CustomEditText_cmpEditTextImeOption, EMPTY_INT));
            cmpFocusable = attributes.getBoolean(R.styleable.CustomEditText_cmpEditTextFocusable, true);
            cmpEnabled = attributes.getBoolean(R.styleable.CustomEditText_cmpEditTextEnabled, true);
        } catch (Exception ex) {
            Timber.e(ex);
        }
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View view = inflater.inflate(R.layout.custom_edit_text, this);
        ButterKnife.bind(this, view);
        customize();
    }

    private void customize() {
        boolean isPercent = false;
        boolean isDecimal_3d = false;
        try {
            isPercent = cmpMask.value == PERCENT.value;
            isDecimal_3d = cmpMask.value == DECIMAL_3D.value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cmpIsCurrency) {
            findViewById(R.id.cmp_edit_text_et_field).setVisibility(GONE);
            etField = findViewById(R.id.cmp_edit_text_et_field_currency);
            setInputType(InputType.TYPE_CLASS_NUMBER);
            addTextChangedListener(new MonetaryTextWatcher(etField, cmpShowCurrencySymbol, isPercent, isDecimal_3d));
        } else {
            findViewById(R.id.cmp_edit_text_et_field_currency).setVisibility(GONE);
            etField = findViewById(R.id.cmp_edit_text_et_field);
            setInputType(InputType.TYPE_CLASS_TEXT);
        }

        setCustomFilter(emojiFiltered);
        initializeTextWatcher();
        setLabel(cmpHint);
        setPlaceholder(cmpPlaceholder);
        setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etField.setOnFocusChangeListener(this);
        etField.setOnEditorActionListener(this);

        if (cmpActualLength) {
            addTextChangedListener(new ActualLengthTextWatcher(tvActualLength, cmpMaxLength));
            tvActualLength.setVisibility(VISIBLE);
            tvActualLength.setText(getContext().getString(
                    R.string.customer_register_commercial_actual_size, EMPTY_INT, cmpMaxLength));
        }
        if (cmpFooterHint != null) {
            tvFooterHint.setVisibility(VISIBLE);
            tvFooterHint.setText(cmpFooterHint);
        }
        if (cmpErrorLabel != null) {
            tvErrorLabel.setText(cmpErrorLabel);
        }
        if (cmpMask == null && cmpMaxLength > EMPTY_INT) {
            setCustomFilter(new InputFilter.LengthFilter(cmpMaxLength));
        }
        if (cmpMask == null && cmpInputType != null && !cmpIsCurrency) {
            setInputType(cmpInputType.typeFlag);
        }
        if (cmpMask != null) {
            setMask(cmpMask);
        }
        if (etField.getInputType() != InputType.TYPE_TEXT_FLAG_MULTI_LINE) {
            etField.setImeOptions(cmpImeOption.typeFlag);
        }
        if (cmpMinHeight != EMPTY_FLOAT) {
            etField.setHeight((int) cmpMinHeight);
        }
        if (cmpInputType == ComponentEditTextInputType.OBSERVATION) {
            etField.setMaxLines(Integer.MAX_VALUE);
        } else if (cmpInputType == ComponentEditTextInputType.NUMBER) {
            etField.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        }

        setFocusable(cmpFocusable);
        setEnabled(cmpEnabled);
    }

    private void initializeTextWatcher() {
        etField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //unused
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //unused
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (afterTextListener != null) {
                    afterTextListener.afterTextChanged(editable.toString());
                }
            }
        });
    }

    private void setCustomFilter(InputFilter filter) {
        InputFilter[] currentFilters = etField.getFilters() != null
                ? etField.getFilters()
                : new InputFilter[]{};
        InputFilter[] newFilters = new InputFilter[currentFilters.length + 1];
        int filterIndex = currentFilters.length;

        int index = 0;
        for (; index < currentFilters.length; index++) {
            newFilters[index] = currentFilters[index];
        }
        newFilters[filterIndex] = filter;
        etField.setFilters(newFilters);
    }

    private void applyMask(ComponentEditTextMask cmpMask) {
        MaskedTextChangedListener maskedTextChangedListener = new MaskedTextChangedListener(
                cmpMask.maskValue, etField, null, null);
        etField.setInputType(InputType.TYPE_CLASS_NUMBER);
        etField.setKeyListener(DigitsKeyListener.getInstance("0123456789 -.,()/"));
        removeTextChangedListener(oldPhoneChangedListener);
        removeTextChangedListener(oldMaskedTextChangedListener);
        addTextChangedListener(maskedTextChangedListener);
        oldMaskedTextChangedListener = maskedTextChangedListener;
    }

    private void applyPhoneMask() {
        PhoneMask phoneMask = new PhoneMask(new WeakReference<>(etField), "TELEPHONE");
        etField.setInputType(InputType.TYPE_CLASS_NUMBER);
        etField.setKeyListener(DigitsKeyListener.getInstance("0123456789 -.,()/"));
        removeTextChangedListener(oldPhoneChangedListener);
        removeTextChangedListener(oldMaskedTextChangedListener);
        addTextChangedListener(phoneMask);
        oldPhoneChangedListener = phoneMask;
    }

    private void applyCellPhoneMask() {
        PhoneMask phoneMask = new PhoneMask(new WeakReference<>(etField), "CELLPHONE");
        etField.setInputType(InputType.TYPE_CLASS_NUMBER);
        etField.setKeyListener(DigitsKeyListener.getInstance("0123456789 -.,()/"));
        removeTextChangedListener(oldPhoneChangedListener);
        removeTextChangedListener(oldMaskedTextChangedListener);
        addTextChangedListener(phoneMask);
        oldPhoneChangedListener = phoneMask;
    }

    public void addTextChangedListener(TextWatcher watcher) {
        etField.addTextChangedListener(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        etField.removeTextChangedListener(watcher);
    }

    public void setLabel(String hint) {
        tvFieldLabel.setText(hint);
    }

    public void setPlaceholder(String placeholder) {
        etField.setHint(placeholder);
    }

    public String getText() {
        if (!StringUtils.isEmpty(etField.getText().toString())) {
            return etField.getText().toString();
        }
        return EMPTY_STRING;
    }

    public void setText(String text) {
        if (cmpIsCurrency && StringUtils.isEmpty(StringUtils.returnOnlyNumbers(text))) {
            etField.setText(EMPTY_STRING);
            return;
        }
        etField.setText(text);
    }

    public void setInputType(int inputType) {
        etField.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT | inputType);
    }

    public void setImeOptions(int imeOption) {
        etField.setImeOptions(imeOption);
    }

    public void setMask(ComponentEditTextMask cmpMask) {
        this.cmpMask = cmpMask;
        if (cmpMask.equals(ComponentEditTextMask.TELEPHONE)) {
            applyPhoneMask();
        } else if (cmpMask.equals(ComponentEditTextMask.CELLPHONE)) {
            applyCellPhoneMask();
        } else {
            applyMask(cmpMask);
        }
    }

    public void setEnabled(boolean enabled) {
        etField.setEnabled(enabled);
        llFieldContainer.setBackgroundResource(enabled
                ? R.drawable.component_edit_text
                : R.drawable.component_edit_text_disabled);
        super.setEnabled(enabled);
    }

    public void setFocusable(boolean enabled) {
        llFieldContainer.setClickable(enabled);
        etField.setFocusable(enabled);
        etField.setClickable(enabled);
        etField.setLongClickable(enabled);
    }

    public void showError() {
        showError(getContext().getString(R.string.cpt_edit_text_error_fill_correctly));
    }

    public void showErrorPercentual() {
        showError("Percentual não pode ser maior que 100%.");
    }

    public void showError(String error) {
        if (error != null) tvErrorLabel.setText(error);
        llErrorContainer.setVisibility(VISIBLE);
        isShowingError = true;
    }

    public void hideError() {
        llErrorContainer.setVisibility(GONE);
        isShowingError = false;
    }

    public void clearValue() {
        etField.setText(EMPTY_STRING);
    }

    public void hideVisibility() {
        this.setVisibility(GONE);
    }

    public void showVisibility() {
        this.setVisibility(VISIBLE);
    }

    public Double getCurrencyDouble() {
        if (!cmpIsCurrency || StringUtils.isEmpty(getText())) return null;
        return Double.parseDouble(getText()
                .replaceAll("[^0-9,]", EMPTY_STRING)
                .replace(',', '.'));
    }

    public int getMaxLength() {
        return cmpMaxLength;
    }

    public boolean isNotFocused() {
        return !etField.isFocused();
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(etField.getText().toString());
    }

    public void requestFieldFocus() {
        etField.requestFocus();
        if (etField.getText() != null) {
            etField.setSelection(etField.getText().length());
        }
        DeviceUtils.openKeyboard(getContext());
    }

    public void removeFieldFocus() {
        etField.clearFocus();
    }

    private enum ComponentEditTextImeOptionType {

        ACTION_NEXT(0, EditorInfo.IME_ACTION_NEXT),
        NORMAL(1, EditorInfo.IME_ACTION_NONE),
        ACTION_GO(2, EditorInfo.IME_ACTION_GO),
        ACTION_DONE(3, EditorInfo.IME_ACTION_DONE);

        private int value;
        private int typeFlag;

        ComponentEditTextImeOptionType(int value, int typeFlag) {
            this.value = value;
            this.typeFlag = typeFlag;
        }

        public static ComponentEditTextImeOptionType getEnum(int value) {
            for (ComponentEditTextImeOptionType item : ComponentEditTextImeOptionType.values()) {
                if (item.value == value) {
                    return item;
                }
            }
            return null;
        }
    }

    public enum ComponentEditTextInputType {

        NUMBER(1, InputType.TYPE_CLASS_NUMBER),
        PASSWORD_NUMBER(2, InputType.TYPE_NUMBER_VARIATION_PASSWORD),
        PASSWORD_TEXT(3, InputType.TYPE_TEXT_VARIATION_PASSWORD),
        OBSERVATION(4, InputType.TYPE_TEXT_FLAG_MULTI_LINE),
        TEXT_VISIBLE_PASSWORD(5, InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD),
        TEXT_CAP_WORDS(6, InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        private int value;
        private int typeFlag;

        ComponentEditTextInputType(int value, int typeFlag) {
            this.value = value;
            this.typeFlag = typeFlag;
        }

        public static ComponentEditTextInputType getEnum(int value) {
            for (ComponentEditTextInputType item : ComponentEditTextInputType.values()) {
                if (item.value == value) {
                    return item;
                }
            }
            return null;
        }
    }

    public enum ComponentEditTextMask {

        CPF(1, "[000].[000].[000]-[00]"),
        DATE(2, "[00]/[00]/[0000]"),
        POSTAL_CODE(3, "[00000]-[000]"),
        TELEPHONE(4, "([00]) [0000]-[0000]"),
        CELLPHONE(5, "([00]) [00000]-[0000]"),
        CNPJ(6, "[00].[000].[000]/[0000]-[00]"),
        PERCENT(8, "[00],[00]"),
        DECIMAL_3D(9, "[0],[00]"),
        RG(10, "[00].[000].[000]-[0]");

        private int value;
        private String maskValue;

        ComponentEditTextMask(int value, String maskValue) {
            this.value = value;
            this.maskValue = maskValue;
        }

        public static ComponentEditTextMask getEnum(int value) {
            for (ComponentEditTextMask item : values()) {
                if (item.value == value) {
                    return item;
                }
            }
            return null;
        }
    }

    public interface CustomEditAfterTextListener {
        void afterTextChanged(String text);
    }
}
