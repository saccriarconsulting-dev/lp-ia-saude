package com.axys.redeflexmobile.ui.clientemigracao.dialog;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 31/03/2020
 */
public class RegisterTokenMigrationAlertDialog extends BaseDialog {

    @BindView(R.id.dialog_alert_token_iv) ImageView imageView;
    @BindView(R.id.dialog_alert_token_tv_title) TextView tvTitle;
    @BindView(R.id.dialog_alert_token_tv_body) TextView tvBody;
    @BindView(R.id.dialog_alert_token_bt_ok) Button btOk;

    private Behavior behavior;
    private RegisterTokenAlertDialogListener callback;

    public static RegisterTokenMigrationAlertDialog newInstance(@NotNull final Behavior behavior) {
        final RegisterTokenMigrationAlertDialog fragment = new RegisterTokenMigrationAlertDialog();
        fragment.behavior = behavior;
        return fragment;
    }

    public void setNegativeCallback(final RegisterTokenAlertDialogListener callback) {
        this.callback = callback;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_alert_token;
    }

    @Override
    protected void initialize(View view) {
        imageView.setImageResource(behavior.iconRes);
        tvTitle.setText(behavior.titleRes);
        tvBody.setText(behavior.bodyRes);
        btOk.setOnClickListener(ignored -> {
            dismiss();
            if (behavior == Behavior.NEGATIVE) {
                callback.doAfterDismiss();
            }
        });
    }

    @Override
    public void showLoading() {
        // não utilizado
    }

    @Override
    public void hideLoading() {
        // não utilizado
    }

    public enum Behavior {
        NEGATIVE(R.drawable.ic_error_red_wraped, R.string.register_token_alert_dialog_negative_title, R.string.register_token_alert_dialog_negative_body),
        POSITIVE(R.drawable.ic_thumbs_up_red_wrapped, R.string.register_token_alert_dialog_positive_title, R.string.register_token_alert_dialog_positive_body);

        int iconRes;
        int titleRes;
        int bodyRes;

        Behavior(@DrawableRes int iconRes,
                 @StringRes int titleRes,
                 @StringRes int bodyRes) {
            this.iconRes = iconRes;
            this.titleRes = titleRes;
            this.bodyRes = bodyRes;
        }
    }

    public interface RegisterTokenAlertDialogListener {
        void doAfterDismiss();
    }
}
