package com.axys.redeflexmobile.ui.component.customedittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.axys.redeflexmobile.shared.util.StringUtils;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.Locale;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

/**
 * @author Bruno Pimentel on 11/01/2019.
 */
public class MonetaryTextWatcher implements TextWatcher {

    private Currency currencyInstance;
    private DecimalFormat numberFormat;
    private String symbol;

    private final WeakReference<EditText> editTextWeakReference;
    private boolean showSymbol;
    private boolean isPercent;
    private boolean isDecimal_3d;
    private String currentText = EMPTY_STRING;

    MonetaryTextWatcher(EditText editText, boolean showSymbol, boolean isPercent, boolean isDecimal_3d) {
        this.editTextWeakReference = new WeakReference<>(editText);
        this.showSymbol = showSymbol;
        this.isPercent = isPercent;
        this.isDecimal_3d = isDecimal_3d;

        Locale locale = Locale.getDefault();
        this.currencyInstance = Currency.getInstance(locale);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
        this.numberFormat = new DecimalFormat(decimalFormat.toPattern(), symbols);
        this.symbol = currencyInstance.getSymbol(locale);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //unused
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        String text = editText.getText().toString();
        if (StringUtils.isEmpty(text) || text.equals(currentText)) return;
        text = format(StringUtils.returnOnlyNumbers(text));
        currentText = text;
        editText.removeTextChangedListener(this);
        editText.setText(text);
        if(isPercent) {
            if (text.length() <= 4)
                editText.setSelection(text.length());
        }else if(isDecimal_3d){
            if (text.length() <= 3)
                editText.setSelection(text.length());
        }else {
            editText.setSelection(text.length());
        }
        editText.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //unused
    }

    private String format(String text) {
        try {
            final int numberTen = 10;
            Double parsedDouble = Double.parseDouble(text) /
                    Math.pow(numberTen, currencyInstance.getDefaultFractionDigits());

            if (showSymbol) {
                return numberFormat.format(parsedDouble)
                        .replaceAll("\\s+",EMPTY_STRING)
                        .replace(symbol, String.format("%s ", symbol));
            }
            return numberFormat.format(parsedDouble).replaceAll("[^0-9,.]", EMPTY_STRING);
        } catch (Exception e) {
            Timber.e(e);
            return EMPTY_STRING;
        }
    }
}
