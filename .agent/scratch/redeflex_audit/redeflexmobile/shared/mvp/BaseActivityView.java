package com.axys.redeflexmobile.shared.mvp;


import androidx.fragment.app.Fragment;

public interface BaseActivityView extends BaseView {

    void openFragment(Fragment fragment);

    void openRootFragment(Fragment fragment);

    void openNextFragmentWithAnimation(Fragment fragment);

    void openPreviousFragmentWithAnimation(Fragment fragment);

    void onBackPressed();

    default void showLoadingDialog() {
    }

    default void hideLoadingDialog() {
    }

    void closeKeyboard();

    void setKeyboardListenerActivated(boolean keyboardListenerActivated);
}
