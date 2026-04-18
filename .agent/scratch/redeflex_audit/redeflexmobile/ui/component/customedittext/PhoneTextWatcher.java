package com.axys.redeflexmobile.ui.component.customedittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

/**
 * @author Bruno Pimentel on 18/01/19.
 */
public class PhoneTextWatcher implements TextWatcher {

    private final WeakReference<EditText> editTextWeakReference;
    private PhoneTextWatcherListener callback;

    PhoneTextWatcher(EditText textView, PhoneTextWatcherListener callback) {
        this.editTextWeakReference = new WeakReference<>(textView);
        this.callback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // unused
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // unused
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editable.toString().trim().equals("(")) {

            editText.removeTextChangedListener(this);
            callback.pauseListener();

            editText.setText(EMPTY_STRING);

            editText.addTextChangedListener(this);
            callback.unPauseListener();
        }
    }

    public interface PhoneTextWatcherListener {
        void pauseListener();

        void unPauseListener();
    }
}
