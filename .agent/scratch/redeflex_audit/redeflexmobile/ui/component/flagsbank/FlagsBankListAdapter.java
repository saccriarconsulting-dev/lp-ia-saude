package com.axys.redeflexmobile.ui.component.flagsbank;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;

import org.jetbrains.annotations.NotNull;

import java.util.List;

interface ItemFlagBankSelected {
    void onItemFlagBankSelected(FlagsBank flag);
}

public class FlagsBankListAdapter extends RecyclerView.Adapter<FlagsBankListAdapter.ViewHolder> {

    private List<FlagsBank> data;
    private final ItemFlagBankSelected listener;
    private Integer cardFlagIdSelected;

    Boolean selectItemEnabled = true;
    public void setSelectItemEnabled(Boolean enabled){
        this.selectItemEnabled = enabled;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            container = (LinearLayout) view.findViewById(R.id.item_card_container);
            image = (ImageView)view.findViewById(R.id.item_card_image);
        }
    }

    public FlagsBankListAdapter(List<FlagsBank> data, ItemFlagBankSelected listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setCardFlagIdSelected(Integer selectedId){
        this.cardFlagIdSelected = selectedId;
        this.notifyDataSetChanged();
    }

    public void updateData(List<FlagsBank> data){
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_flag, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,
                                 final int position)
    {
        FlagsBank item = data.get(position);
        holder.container.setBackgroundResource(item.getId().equals(cardFlagIdSelected)
                ? R.drawable.background_rounded_white_border_red
                : R.drawable.background_rounded_white);
        holder.image.setImageBitmap(decodeImageBitmap(item.getBase64Image()));
        holder.container.setOnClickListener(v -> onClick(item));
    }

    private void onClick(FlagsBank item) {
        if(selectItemEnabled){
            cardFlagIdSelected = item.getId();
            notifyDataSetChanged();
            listener.onItemFlagBankSelected(item);
        }
    }

    private Bitmap decodeImageBitmap(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    public int getItemCount() { return data.size(); }
}