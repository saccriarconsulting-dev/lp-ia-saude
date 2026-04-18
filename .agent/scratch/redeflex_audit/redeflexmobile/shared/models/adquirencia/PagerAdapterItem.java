package com.axys.redeflexmobile.shared.models.adquirencia;

import androidx.fragment.app.Fragment;

/**
 * @author Rogério Massa on 22/11/18.
 */

public class PagerAdapterItem {

    public String title;
    public Fragment fragment;

    public PagerAdapterItem(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }
}
