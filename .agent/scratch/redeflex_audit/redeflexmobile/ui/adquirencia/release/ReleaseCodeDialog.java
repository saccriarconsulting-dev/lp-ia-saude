package com.axys.redeflexmobile.ui.adquirencia.release;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.Release;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Rogério Massa on 04/12/18.
 */

public class ReleaseCodeDialog extends BaseDialog {

    public static final String TAG = ReleaseCodeDialog.class.getSimpleName();

    @BindView(R.id.dlg_release_code_tv_machine) TextView tvMachine;
    @BindView(R.id.dlg_release_ll_muxx) LinearLayout llMuxx;
    @BindView(R.id.dlg_release_tv_muxx) TextView tvMuxx;
    @BindView(R.id.dlg_release_ll_masters) LinearLayout llMasters;
    @BindView(R.id.dlg_release_tv_masters) TextView tvMasters;
    @BindView(R.id.dlg_release_code_bt_confirm) Button btConfirm;

    private Release release;

    public static ReleaseCodeDialog newInstance(Release release) {
        ReleaseCodeDialog dialog = new ReleaseCodeDialog();
        dialog.setRelease(release);
        return dialog;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_release_code;
    }

    @Override
    protected void initialize(View view) {
        setCancelable(false);
        tvMachine.setText(getString(R.string.dlg_release_code_machine, release.getMachine()));

        llMuxx.setVisibility(release.getMuxxCode() != null
                && StringUtils.isNotEmpty(release.getMuxxCode())
                ? View.VISIBLE : View.GONE);
        tvMuxx.setText(release.getMuxxCode());

        llMasters.setVisibility(release.getMasterCode() != null
                && StringUtils.isNotEmpty(release.getMasterCode())
                ? View.VISIBLE : View.GONE);

        tvMasters.setText(release.getMasterCode());
        btConfirm.setOnClickListener(v -> dismiss());
    }
}
