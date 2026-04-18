package com.axys.redeflexmobile.shared.util;

import android.widget.TextView;

import java.util.List;

public class AlterarCorTextView {

    public static void alterarCor(List<TextView> views, int cor) {
        for (TextView v : views) {
            v.setTextColor(cor);
        }
    }
}
