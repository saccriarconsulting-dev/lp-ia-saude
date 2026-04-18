package com.axys.redeflexmobile.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.component.ComponentProgressLoading;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

public abstract class BaseActivity extends DaggerAppCompatActivity implements BaseActivityView {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    private boolean isKeyboardListenerActivated;
    private boolean isBackButtonActivated;
    private ProgressDialog progressDialog;

    @LayoutRes
    protected abstract int getContentView();

    protected abstract void initialize();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(getContentView());
        ButterKnife.bind(this);

        isKeyboardListenerActivated = true;
        initializeKeyboardListener();

        Utilidades.getDataServidorESalvaBanco(this);
        initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("%s HAS BEING DESTROYED", this.getClass().getSimpleName());
    }

    @Override
    public String getStringByResId(int resourceId) {
        return getString(resourceId);
    }

    @Override
    public String getStringByResId(int stringResource, Object... args) {
        return getString(stringResource, args);
    }

    @Override
    public void showOneButtonDialog(@Nullable String title, @Nullable String message, @Nullable View.OnClickListener buttonCallback) {
        Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), message);
        alerta.show((dialog, which) -> {
            if (buttonCallback != null) buttonCallback.onClick(null);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isBackButtonActivated) return;
        int quantityFragment = getSupportFragmentManager().getBackStackEntryCount();
        if (quantityFragment <= 1) {
            this.finish();
        }
        if (quantityFragment > 1) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void openFragment(Fragment fragment) {
        Integer frameLayoutId = getFrameLayoutId();
        if (frameLayoutId == null) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(frameLayoutId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    @Override
    public void openRootFragment(Fragment fragment) {
        this.getSupportFragmentManager().popBackStack(null, POP_BACK_STACK_INCLUSIVE);
        this.openFragment(fragment);
    }

    @Override
    public void openNextFragmentWithAnimation(Fragment fragment) {
        Integer frameLayoutId = getFrameLayoutId();
        if (frameLayoutId == null) return;
        this.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.fadeout)
                .replace(frameLayoutId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void openPreviousFragmentWithAnimation(Fragment fragment) {
        Integer frameLayoutId = getFrameLayoutId();
        if (frameLayoutId == null) return;
        this.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_left_in, R.anim.fadeout)
                .replace(frameLayoutId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showLoading() {
        getWindow().setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE);
        ComponentProgressLoading loading = getComponentProgressLoading();
        if (loading == null) return;
        loading.showLoading();
    }

    @Override
    public void hideLoading() {
        getWindow().clearFlags(FLAG_NOT_TOUCHABLE);
        ComponentProgressLoading loading = getComponentProgressLoading();
        if (loading == null) return;
        loading.hideLoading();
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Carregando");
            progressDialog.setCancelable(false);
            progressDialog.show();

            return;
        }

        progressDialog.show();
    }

    @Override
    public void setKeyboardListenerActivated(boolean keyboardListenerActivated) {
        isKeyboardListenerActivated = keyboardListenerActivated;
    }

    public void setBackButtonActivated(boolean activated) {
        isBackButtonActivated = activated;
    }

    private void initializeKeyboardListener() {
        final int MIN_HEIGHT_VALUE = 150;
    }

    public void onKeyboardOpen() {
        // unused
    }

    public void onKeyboardClose() {
        // unused
    }

    protected ComponentProgressLoading getComponentProgressLoading() {
        return null;
    }

    protected Integer getFrameLayoutId() {
        return null;
    }

    public void showBackButtonToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void hideBackButtonToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
