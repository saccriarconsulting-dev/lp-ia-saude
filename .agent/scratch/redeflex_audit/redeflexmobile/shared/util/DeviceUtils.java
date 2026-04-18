package com.axys.redeflexmobile.shared.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.axys.redeflexmobile.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Vitor Otero on 13/10/17.
 */

public class DeviceUtils {

    public static final String PREFS_FCM_TOKEN = "prefsFcmToken";
    private static final int EMPTY_VIEW_LIST = 0;
    private static final int CLICK_DELAY = 200;
    private static final String PREFS_NAME = "BASEPROJECT_CAPPTAN_AND";
    private static final String CHAT_PERMISSION = "CHAT_PERMISSION";

    public static void saveStringInSharedPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringInSharedPreferences(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public static void changeStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        }
    }

    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {

            imm.hideSoftInputFromWindow(activity.findViewById(android.R.id.content)
                    .getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void closeKeyboardOnTouch(final Context context, final View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                InputMethodManager imm = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {

                    imm.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = EMPTY_VIEW_LIST; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                closeKeyboardOnTouch(context, innerView);
            }
        }
    }

    public static Float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static Point getScreenSizeInPixel(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm != null ? wm.getDefaultDisplay() : null;
        Point size = new Point();
        if (display != null) {
            display.getSize(size);
        }
        return size;
    }

    public static boolean isConnectionInternet(final Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void clickDelay(View view) {
        if (view == null) {
            return;
        }
        view.setEnabled(false);
        new Handler().postDelayed(
                () -> {
                    if (view != null) {
                        view.setEnabled(true);
                    }
                }
                , CLICK_DELAY);
    }

    public static void openKeyboard(Context context) {
        if (context == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, NumberUtils.EMPTY_INT);

    }

    public static void openNewActivity(Context context,
                                       Class<?> activityClass,
                                       Bundle parameters,
                                       boolean close) {
        Intent intent = new Intent(context, activityClass);
        if (parameters != null) {
            intent.putExtras(parameters);
        }

        context.startActivity(intent);

        if (close) {
            ((Activity) context).finish();
        }
    }

    public static void saveChatPreference(Context context, String permissao) {
        if (permissao != null)
            saveStringInSharedPreferences(context, CHAT_PERMISSION, permissao);
    }

    public static Boolean getChatPermission(Context context) {
        String permissao = getStringInSharedPreferences(context, CHAT_PERMISSION);
        return permissao != null && permissao.equals("S");
    }
}
