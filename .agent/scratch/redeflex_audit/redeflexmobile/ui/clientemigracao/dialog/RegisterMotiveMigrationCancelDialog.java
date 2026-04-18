package com.axys.redeflexmobile.ui.clientemigracao.dialog;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.base.BaseDialog;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_OBSERVATION;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

/**
 * @author Lucas Marciano on 15/04/2020
 */
public class RegisterMotiveMigrationCancelDialog extends BaseDialog {

    private static final int NO_SELECTED_ITEM = -1;
    private static final int VERTICAL_MARGIN = 22;
    private static final int HORIZONTAL_MARGIN = 0;

    @BindView(R.id.rg_motivos) RadioGroup rgMotives;
    @BindView(R.id.tv_cancel_migration) TextView tvCancel;
    @BindView(R.id.tv_confirm_migration) TextView tvConfirm;
    @BindView(R.id.cet_observation_migration) CustomEditText cetObservation;

    private RegisterMotiveMigrationCancelDialogListener callbackConfirm;
    private List<MotiveMigrationSub> motivesList = new ArrayList<>();

    public static RegisterMotiveMigrationCancelDialog newInstance(
            final RegisterMotiveMigrationCancelDialogListener callbackConfirm,
            final List<MotiveMigrationSub> motivesList) {

        final RegisterMotiveMigrationCancelDialog dialog = new RegisterMotiveMigrationCancelDialog();
        dialog.callbackConfirm = callbackConfirm;
        dialog.motivesList = motivesList;
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_save_migration_motive_cancel;
    }

    @Override
    protected void initialize(View view) {
        setCancelable(false);
        initializeRadioGroup();
        tvCancel.setOnClickListener(v -> dismiss());
        tvConfirm.setOnClickListener(v -> {
            if (!showErrorOrContinue()) {
                if (rgMotives.getCheckedRadioButtonId() != NO_SELECTED_ITEM) {
                    callbackConfirm.doOnConfirm(
                            rgMotives.getCheckedRadioButtonId(),
                            Util_IO.retiraAcento(cetObservation.getText())
                    );
                    dismiss();
                }
            } else {
                cetObservation.showError();
            }
        });
    }

    private void initializeRadioGroup() {
        if (getContext() != null) {
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_MARGIN);

            for (MotiveMigrationSub motive : motivesList) {
                RadioButton rb = new RadioButton(getContext());
                rb.setText(motive.getDescricao());
                rb.setId(motive.getId());
                rb.setLayoutParams(params);
                rb.setTextColor(getContext().getResources().getColor(R.color.textoCinzaEscuro));
                rgMotives.addView(rb);
            }
        }
    }

    private boolean showErrorOrContinue() {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if (cetObservation.getText().trim().length() <= EMPTY_INT) {
            errors.add(ET_OBSERVATION);
        }
        showErrors(errors);
        return errors.size() > EMPTY_INT;
    }

    private void showErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) {
            return;
        }
        if (errors.contains(ET_OBSERVATION)) {
            cetObservation.showError();
        }
    }

    public interface RegisterMotiveMigrationCancelDialogListener {
        void doOnConfirm(int itemSelected, String observation);
    }
}
