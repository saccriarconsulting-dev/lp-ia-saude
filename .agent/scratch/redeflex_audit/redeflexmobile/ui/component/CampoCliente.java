package com.axys.redeflexmobile.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

public class CampoCliente extends LinearLayout {

    private static final int EMPTY_VALUE = -1;
    private AppCompatEditText etField;
    private AppCompatTextView tvDescription;
    private ICampoClienteOnTextChanged onTextChanged;

    public CampoCliente(Context context) {
        this(context, null);
    }

    public CampoCliente(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CampoCliente(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    @Nullable
    public String getText() {
        return etField.getText().toString();
    }

    public void setText(String text) {
        etField.setText(text);
    }

    public void setLabel(String text) {
        tvDescription.setText(text);
    }

    public void setError(String message) {
        etField.requestFocus();
        etField.setError(message);
    }

    public void setClickableEventField(OnClickListener clickListener) {
        etField.setOnClickListener(clickListener);
    }

    public void addTextWatch(TextWatcher textWatcher) {
        etField.addTextChangedListener(textWatcher);
    }

    public void addTextFocus(MaskedTextChangedListener textWatcher) {
        etField.setOnFocusChangeListener(textWatcher);
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (onTextChanged != null) {
                    onTextChanged.onTextChanged(charSequence, i, i1, i2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    public AppCompatEditText getEditText() {
        return etField;
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        inflate(context, R.layout.campo_cliente_componente, this);

        tvDescription = (AppCompatTextView) findViewById(R.id.campo_cliente_tv_descricao);
        etField = (AppCompatEditText) findViewById(R.id.campo_cliente_et_valor);
        etField.addTextChangedListener(getTextWatcher());

        setAttributes(context, attrs);
    }

    private void setAttributes(Context context, @Nullable AttributeSet attributes) {
        if (attributes == null) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attributes, R.styleable.CampoCliente);
        populateDescription(typedArray);
        insertImage(context, typedArray);
        setPlaceholder(typedArray);
        setStates(typedArray);
        setInputType(typedArray);
        setMaxLength(typedArray);
        setImeOptions(typedArray);

        typedArray.recycle();
    }

    private void populateDescription(TypedArray typedArray) {
        CharSequence valor = typedArray.getText(R.styleable.CampoCliente_description);
        tvDescription.setText(valor);
    }

    private void insertImage(Context context, TypedArray typedArray) {
        int id = typedArray.getResourceId(R.styleable.CampoCliente_image_right, EMPTY_VALUE);
        Drawable drawable = id != EMPTY_VALUE
                ? AppCompatResources.getDrawable(context, id)
                : null;
        etField.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    private void setPlaceholder(TypedArray typedArray) {
        CharSequence placeholder = typedArray.getText(R.styleable.CampoCliente_placeholder);
        etField.setHint(placeholder);
    }

    private void setStates(TypedArray typedArray) {
        boolean focusable = typedArray.getBoolean(R.styleable.CampoCliente_focusable, true);
        boolean clickable = typedArray.getBoolean(R.styleable.CampoCliente_clickable, true);
        etField.setFocusable(focusable);
        etField.setClickable(clickable);
    }

    private void setInputType(TypedArray typedArray) {
        int inputType = typedArray.getInt(R.styleable.CampoCliente_android_inputType, InputType.TYPE_CLASS_TEXT);
        etField.setInputType(inputType);

        String digits = typedArray.getString(R.styleable.CampoCliente_android_digits);
        if (digits != null) {
            etField.setKeyListener(DigitsKeyListener.getInstance(digits));
        }
    }

    private void setMaxLength(TypedArray typedArray) {
        int maxLength = typedArray.getInteger(R.styleable.CampoCliente_android_maxLength, 0);

        if (maxLength == 0) {
            return;
        }

        InputFilter[] filtros = etField.getFilters();
        InputFilter[] novosFiltros = new InputFilter[filtros.length + 1];

        for (int i = 0; i < filtros.length; i++) {
            novosFiltros[i] = filtros[i];
        }

        novosFiltros[filtros.length] = new InputFilter.LengthFilter(maxLength);
        etField.setFilters(novosFiltros);
    }

    private void setImeOptions(TypedArray typedArray) {
        int imeOption = typedArray.getInteger(R.styleable.CampoCliente_android_imeOptions, 0);
        if (imeOption != 0) {
            etField.setImeOptions(imeOption);
        }
    }

    public ICampoClienteOnTextChanged getOnTextChanged() {
        return onTextChanged;
    }

    public void setOnTextChanged(ICampoClienteOnTextChanged onTextChanged) {
        this.onTextChanged = onTextChanged;
    }

    public interface ICampoClienteOnTextChanged {
        void onTextChanged(CharSequence charSequence, int i, int i1, int i2);
    }
}
