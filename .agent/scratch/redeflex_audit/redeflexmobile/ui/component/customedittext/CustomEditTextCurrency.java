package com.axys.redeflexmobile.ui.component.customedittext;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.util.AttributeSet;

/**
 * @author Rogério Massa on 21/02/19.
 */

public class CustomEditTextCurrency extends AppCompatEditText {

    public CustomEditTextCurrency(Context context) {
        super(context);
    }

    public CustomEditTextCurrency(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        this.post(() -> {
            Editable text = CustomEditTextCurrency.this.getText();
            if (text != null) {
                CustomEditTextCurrency.this.setSelection(text.length());
            }
        });
    }
}
