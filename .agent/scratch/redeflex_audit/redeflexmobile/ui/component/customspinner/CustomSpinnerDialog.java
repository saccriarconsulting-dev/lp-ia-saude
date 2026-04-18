package com.axys.redeflexmobile.ui.component.customspinner;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.base.BaseAdapter.IBaseViewHolderClickListener;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import java.util.List;

import butterknife.BindView;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

/**
 * @author Bruno Pimentel on 23/11/18.
 */

public class CustomSpinnerDialog extends BaseDialog implements
        IBaseViewHolderClickListener<ICustomSpinnerDialogModel>,
        TextWatcher {

    @BindView(R.id.cpt_spinner_dialog_et) EditText etSpinnerDialog;
    @BindView(R.id.cpt_spinner_dialog_rv) RecyclerView rvSpinnerDialog;

    private List<ICustomSpinnerDialogModel> list;
    private ICustomSpinnerDialogCallback callback;
    private CustomSpinnerDialogAdapter adapter;
    private boolean isSelected;

    public static CustomSpinnerDialog newInstance(List<ICustomSpinnerDialogModel> list,
                                                  ICustomSpinnerDialogCallback callback) {
        CustomSpinnerDialog dialog = new CustomSpinnerDialog();
        dialog.setList(list);
        dialog.setCallback(callback);
        return dialog;
    }

    public void setList(List<ICustomSpinnerDialogModel> list) {
        this.list = list;
    }

    public void setCallback(ICustomSpinnerDialogCallback callback) {
        this.callback = callback;
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_spinner_dialog;
    }

    @Override
    protected void initialize(View view) {
        this.adapter = new CustomSpinnerDialogAdapter(this);
        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(), VERTICAL);
        this.rvSpinnerDialog.addItemDecoration(decoration);
        this.rvSpinnerDialog.setLayoutManager(new LinearLayoutManager(getContext()));
        this.rvSpinnerDialog.setAdapter(adapter);
        this.adapter.setList(list);

        etSpinnerDialog.addTextChangedListener(this);
    }

    @Override
    public void onClickListener(ICustomSpinnerDialogModel iCustomSpinnerDialogModel, View view) {
        if (!isSelected) {
            callback.onItemSelected(iCustomSpinnerDialogModel);
            isSelected = true;
        }
        dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //unused
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //unused
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == null) {
            return;
        }
        adapter.setList(Stream.ofNullable(this.list)
                .filter(value -> editable.toString().isEmpty()
                        || (value != null &&
                        value.getDescriptionValue() != null &&
                        Util_IO.trataString(value.getDescriptionValue().toLowerCase())
                                .contains(Util_IO.trataString(editable.toString().toLowerCase()))))
                .toList());
    }

    public interface ICustomSpinnerDialogCallback {
        void onItemSelected(ICustomSpinnerDialogModel item);
    }
}
