package com.axys.redeflexmobile.ui.component.customedittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.axys.redeflexmobile.R;

import java.lang.ref.WeakReference;

/**
 * @author Bruno Pimentel on 11/01/19.
 */
public class ActualLengthTextWatcher implements TextWatcher {

    private final WeakReference<TextView> textViewWeakReference;
    private int maxLength;

    ActualLengthTextWatcher(TextView textView, int maxLength) {
        this.textViewWeakReference = new WeakReference<>(textView);
        this.maxLength = maxLength;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //unused
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        TextView textView = textViewWeakReference.get();
        textView.setText(textView.getContext().getString(
                R.string.customer_register_commercial_actual_size,
                charSequence.length(),
                maxLength));
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //unused
    }
}
