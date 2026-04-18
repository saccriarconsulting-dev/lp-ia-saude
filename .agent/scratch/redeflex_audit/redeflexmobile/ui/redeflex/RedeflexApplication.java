package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.multidex.MultiDex;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.component.DaggerAppComponent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;

public class RedeflexApplication extends DaggerApplication {

    // Tags para não reaplicar listeners a cada onStart
    private static final int TAG_BASE_PADDING = R.id.tag_base_padding_saved;
    private static final int TAG_INSETS_APPLIED = R.id.tag_insets_applied;

    // Configure aqui “folgas extras” (em dp) por Activity SEM editar as Activities
    // Use getName() (qualificado) ou getSimpleName(); abaixo uso qualificado:
    private static final Map<String, Integer> EXTRA_BOTTOM_BY_ACTIVITY_DP = new HashMap<>();
    static {
        // Exemplo: "SolicitacaoPrecoDifActivity" precisa de +24dp além dos insets
        EXTRA_BOTTOM_BY_ACTIVITY_DP.put("com.axys.redeflexmobile.ui.redeflex.SolicitacaoPrecoDifActivity", 24);
        // Adicione outras se necessário, ou deixe vazio
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);

        AndroidThreeTen.init(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        if (BuildConfig.DEBUG || BuildConfig.INNER_TEST) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.DebugTree()); // ajuste seu Tree de prod se tiver
        }

        enableGlobalEdgeToEdge();
    }

    private void enableGlobalEdgeToEdge() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

            @Override public void onActivityStarted(Activity activity) {
                // 1) Edge‑to‑edge
                WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);

                // 2) Pega o root "real" (primeiro filho do content) para aplicar padding
                View rootContainer = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                View content = (rootContainer instanceof ViewGroup && ((ViewGroup) rootContainer).getChildCount() > 0)
                        ? ((ViewGroup) rootContainer).getChildAt(0)
                        : rootContainer;

                // Evita reaplicar em reentranças do ciclo de vida
                if (content.getTag(TAG_INSETS_APPLIED) != null) return;
                content.setTag(TAG_INSETS_APPLIED, Boolean.TRUE);

                // 3) Salva padding base apenas uma vez
                if (content.getTag(TAG_BASE_PADDING) == null) {
                    int[] base = new int[] {
                            content.getPaddingLeft(),
                            content.getPaddingTop(),
                            content.getPaddingRight(),
                            content.getPaddingBottom()
                    };
                    content.setTag(TAG_BASE_PADDING, base);
                }

                // 4) Extra opcional por Activity (sem tocar na Activity)
                final int extraBottomPx = dp(activity, EXTRA_BOTTOM_BY_ACTIVITY_DP.getOrDefault(
                        activity.getClass().getName(), 0));

                int extraTopPx = 200;
                // 5) Listener de insets: soma systemBars + considera IME (teclado)
                ViewCompat.setOnApplyWindowInsetsListener(content, (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    Insets ime  = insets.getInsets(WindowInsetsCompat.Type.ime());
                    int bottomInset = Math.max(bars.bottom, ime.bottom);

                    int[] base = (int[]) v.getTag(TAG_BASE_PADDING);
                    v.setPadding(
                            base[0] + bars.left,
                            base[1] + bars.top,
                            base[2] + bars.right,
                            base[3] + bottomInset + extraBottomPx
                    );

                    if (BuildConfig.DEBUG) {
                        Log.d("Insets", activity.getClass().getSimpleName()
                                + " top=" + bars.top + " bottom=" + bottomInset
                                + " + extra=" + extraBottomPx);
                    }

                    // 6) Ajusta automaticamente qualquer FloatingActionButton no layout
                    adjustAllFabsBottomMargin(v, bottomInset + extraBottomPx);

                    return insets; // não consome
                });

                // 7) Garante aplicação; quando a view árvore muda, re‑ajustamos FABs
                ViewCompat.requestApplyInsets(content);
                content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        // Reajusta FABs caso entrem/saíam da hierarquia após o primeiro layout
                        WindowInsetsCompat current = ViewCompat.getRootWindowInsets(content);
                        if (current != null) {
                            Insets bars = current.getInsets(WindowInsetsCompat.Type.systemBars());
                            Insets ime  = current.getInsets(WindowInsetsCompat.Type.ime());
                            int bottomInset = Math.max(bars.bottom, ime.bottom);
                            adjustAllFabsBottomMargin(content, bottomInset + extraBottomPx);
                        }
                    }
                });

                // (Opcional) barras transparentes
                activity.getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
                activity.getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);
            }

            @Override public void onActivityResumed(Activity activity) {}
            @Override public void onActivityPaused(Activity activity) {}
            @Override public void onActivityStopped(Activity activity) {}
            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
            @Override public void onActivityDestroyed(Activity activity) {}
        });
    }

    /**
     * Percorre a árvore de views a partir de root e ajusta o bottomMargin de todo FloatingActionButton encontrado,
     * somando o inseto informado (para não “cortar” com a nav bar/gestos/IME).
     */
    private void adjustAllFabsBottomMargin(View root, int extraBottomPx) {
        Deque<View> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            View v = stack.pop();

            if (v instanceof FloatingActionButton) {
                ViewGroup.LayoutParams lp = v.getLayoutParams();
                if (lp instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
                    // Mantém a margem base e soma o extra (sem duplicar em chamadas subsequentes)
                    Object tag = v.getTag(TAG_BASE_PADDING);
                    int baseMargin = (tag instanceof Integer) ? (Integer) tag : mlp.bottomMargin;
                    if (!(tag instanceof Integer)) v.setTag(TAG_BASE_PADDING, baseMargin);

                    mlp.bottomMargin = baseMargin + extraBottomPx;
                    v.setLayoutParams(mlp);
                    v.requestLayout();
                }
            }

            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    stack.push(vg.getChildAt(i));
                }
            }
        }
    }

    private int dp(Context c, int value) {
        return Math.round(c.getResources().getDisplayMetrics().density * value);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder()
                .application(this)
                .build();
    }
}