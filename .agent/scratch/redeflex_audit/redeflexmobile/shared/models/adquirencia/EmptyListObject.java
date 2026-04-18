package com.axys.redeflexmobile.shared.models.adquirencia;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import android.view.View;

/**
 * @author Vitor Otero on 18/10/17.
 */

public class EmptyListObject {

    public static final int ICON_RESOURCE_ZERO = 0;

    private int icon;
    private String title;
    private String message;
    private String buttonText;
    private View.OnClickListener buttonCallback;

    public EmptyListObject(String title) {
        this.title = title;
    }

    public EmptyListObject(@DrawableRes int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public EmptyListObject(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public EmptyListObject(@DrawableRes int icon, String title, String message) {
        this.icon = icon;
        this.title = title;
        this.message = message;
    }

    public EmptyListObject(String title,
                           String message,
                           @Nullable String buttonText,
                           @Nullable View.OnClickListener buttonCallback) {
        this.title = title;
        this.message = message;
        this.buttonText = buttonText;
        this.buttonCallback = buttonCallback;
    }

    public EmptyListObject(@DrawableRes int icon,
                           String title,
                           String message,
                           @Nullable String buttonText,
                           @Nullable View.OnClickListener buttonCallback) {
        this.icon = icon;
        this.title = title;
        this.message = message;
        this.buttonText = buttonText;
        this.buttonCallback = buttonCallback;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getButtonText() {
        return buttonText;
    }

    public View.OnClickListener getButtonCallback() {
        return buttonCallback;
    }
}
