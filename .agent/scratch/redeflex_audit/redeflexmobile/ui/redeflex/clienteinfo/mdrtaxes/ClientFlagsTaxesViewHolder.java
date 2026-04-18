package com.axys.redeflexmobile.ui.redeflex.clienteinfo.mdrtaxes;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author lucasmarciano on 30/07/20
 */
@SuppressLint("NonConstantResourceId")
class ClientFlagsTaxesViewHolder extends RecyclerView.ViewHolder {

    private final ImageView ivFLag;
    private final LinearLayout llContainer;
    private final Context context;
    private ClickListener viewHolderClickListener;

    public ClientFlagsTaxesViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        ivFLag = itemView.findViewById(R.id.iv_flag);
        llContainer = itemView.findViewById(R.id.customer_register_commercial_tax_type_rl_container);
    }

    public void bind(FlagsBank flag, boolean checkItem) {
        if (flag.getImage() != null) {
            byte[] imageByteArray = Base64.decode(flag.getImage(), Base64.DEFAULT);
            Glide.with(context)
                    .load(imageByteArray)
                    .apply(new RequestOptions().placeholder(R.drawable.attachment_item_add)
                            .error(R.drawable.background_rounded_white)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(ivFLag);
        }

        llContainer.setBackgroundResource(checkItem ?
                R.drawable.background_rounded_white_border_red :
                R.drawable.background_rounded_white
        );

        itemView.setOnClickListener(v -> viewHolderClickListener.clickEvent(flag));
    }

    public void setViewHolderClickListener(ClickListener clickListener) {
        viewHolderClickListener = clickListener;
    }

    interface ClickListener {
        void clickEvent(FlagsBank flag);
    }
}
