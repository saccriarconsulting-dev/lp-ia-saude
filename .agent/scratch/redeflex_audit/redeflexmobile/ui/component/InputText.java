package com.axys.redeflexmobile.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.EnumValidacao;
import com.axys.redeflexmobile.shared.util.Validacoes;

/**
 * Created by joao.viana on 24/01/2017.
 */

public class InputText extends RelativeLayout implements TextWatcher {

    private Context mContext;
    private RelativeLayout mLayout;
    private TextView mTextView;
    private EditText mEditText;
    private AttributeSet mAttributeSet;
    private TextWatcher lastMaskWatcher;
    private TextWatcher lastValidationWatcher;

    public InputText(Context context) {
        this(context, null);
    }

    public InputText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mAttributeSet = attrs;
        iniciar();
    }

    private void iniciar() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.input_text, this, true);

        mLayout = (RelativeLayout) getChildAt(0);

        mTextView = (TextView) mLayout.getChildAt(0);
        mEditText = (EditText) mLayout.getChildAt(1);
        mEditText.addTextChangedListener(this);
        String valor = "";
        TypedArray typedArray = mContext.obtainStyledAttributes(mAttributeSet, R.styleable.SearchableSpinner);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SearchableSpinner_hintText:
                    valor = typedArray.getString(attr);
                    mEditText.setHint(valor);
                    mTextView.setText(valor);
                    break;
                case R.styleable.SearchableSpinner_maxLength:
                    valor = typedArray.getString(attr);
                    mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(valor))});
                    break;
                case R.styleable.SearchableSpinner_android_inputType:
                    int inputType = typedArray.getInteger(attr, -1);
                    mEditText.setInputType(inputType);
                    break;
                case R.styleable.SearchableSpinner_android_enabled:
                    boolean habilta = typedArray.getBoolean(attr, true);
                    setEnabledText(habilta);
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().isEmpty())
            mTextView.setVisibility(View.GONE);
        else
            mTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getText() {
        return mEditText.getText().toString().trim();
    }

    public void setText(String valor) {
        mEditText.setText(valor);
    }

    public void setHint(String valor) {
        mEditText.setHint(valor);
        mTextView.setText(valor);
    }

    public void setEnabledText(Boolean valor) {
        mEditText.setEnabled(valor);
        if (!valor) {
            mLayout.setBackgroundColor(getResources().getColor(R.color.cinza_claro));
            mEditText.setHintTextColor(getResources().getColor(R.color.preto));
        } else {
            // TODO: Verificar cores habilitadas
        }
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mEditText.addTextChangedListener(watcher);
    }

    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "").replaceAll("[:]", "");
    }

    public void addMask(final String mask) {
        if (lastMaskWatcher != null) {
            mEditText.removeTextChangedListener(lastMaskWatcher);
        }
        lastMaskWatcher = new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                mEditText.setText(mascara);
                mEditText.setSelection(mascara.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mEditText.addTextChangedListener(lastMaskWatcher);
    }

    public void removeValidacao() {
        if (lastValidationWatcher != null) {
            mEditText.removeTextChangedListener(lastValidationWatcher);
        }
    }

    public void addValidacao(final EnumValidacao enumValidacao) {
        if (lastValidationWatcher != null) {
            mEditText.removeTextChangedListener(lastValidationWatcher);
        }
        lastValidationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    mLayout.setBackgroundResource(R.drawable.customborder);
                } else {
                    if (!validacao(s.toString(), enumValidacao))
                        mLayout.setBackgroundResource(R.drawable.custombordererror);
                    else
                        mLayout.setBackgroundResource(R.drawable.customborder);
                }
            }
        };
        mEditText.addTextChangedListener(lastValidationWatcher);
    }

    private boolean validacao(String valor, EnumValidacao enumValidacao) {
        switch (enumValidacao) {
            case CPF:
                return Validacoes.validaCPF(valor);
            case CNPJ:
                return Validacoes.validaCnpj(valor);
            case DATA:
                return Validacoes.validaData(valor);
            case CELULAR:
                return Validacoes.validaTelCelular(valor);
            case EMAIL:
                return Validacoes.validaEmail(valor);
            default:
                return true;
        }
    }

    public void removeTextChangedListener(TextWatcher textWatcher) {
        mEditText.removeTextChangedListener(textWatcher);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public void setSelection(int selection) {
        try {
            mEditText.setSelection(selection);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setHabilita(boolean bHabilita) {
        mEditText.setEnabled(bHabilita);
    }

    public void setError(String error) {
        mEditText.setError(error);
    }
}