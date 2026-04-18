package com.axys.redeflexmobile.ui.register.customer.commercial;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.customerregister.MachineType;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.axys.redeflexmobile.ui.register.customer.commercial.RegisterCustomerCommercialPosViewHolder.RegisterCustomerCommercialPosViewHolderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Pimentel on 23/11/18.
 */

public class RegisterCustomerCommercialPosAdapter extends Adapter<BaseViewHolder> {

    private static final int HEADER_INDEX = -1;
    private RegisterCustomerCommercialPosViewHolderListener callback;
    private List<MachineType> list;

    RegisterCustomerCommercialPosAdapter(RegisterCustomerCommercialPosViewHolderListener callback) {
        this.callback = callback;
        this.list = new ArrayList<>();
    }

    public List<MachineType> getList() {
        return list;
    }

    public void setList(List<MachineType> list) {
        if (list == null) list = new ArrayList<>();

        MachineType header = new MachineType();
        header.setId(HEADER_INDEX);
        this.list = new ArrayList<>();
        this.list.add(header);
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == HEADER_INDEX) return makeHeaderView(viewGroup.getContext());
        return new RegisterCustomerCommercialPosViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_customer_register_commercial_pos_item, viewGroup, false),
                callback);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (baseViewHolder instanceof RegisterCustomerCommercialPosViewHolder) {
            RegisterCustomerCommercialPosViewHolder holder = (RegisterCustomerCommercialPosViewHolder) baseViewHolder;
            holder.bind(list.get(i), i);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        MachineType machineType = list.get(position);
        if (machineType.getId() != null && machineType.getId() == HEADER_INDEX) {
            return HEADER_INDEX;
        }
        return super.getItemViewType(position);
    }

    private BaseViewHolder makeHeaderView(Context context) {
        TextView textView = new TextView(context);
        textView.setText(context.getString(R.string.customer_register_commercial_pos_list));
        textView.setTextSize(DeviceUtils.convertPixelsToDp(context.getResources().getDimension(R.dimen.text_medium)));
        textView.setTextColor(ContextCompat.getColor(context, R.color.black));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.setMargins(EMPTY_INT, (int) context.getResources().getDimension(R.dimen.spacing_normal), EMPTY_INT, EMPTY_INT);
        textView.setLayoutParams(layoutParams);

        return new BaseViewHolder(textView) {
        };
    }
}
