package com.axys.redeflexmobile.shared.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;

import java.util.ArrayList;

public class ItemBandeiraAdapter extends RecyclerView.Adapter<ItemBandeiraAdapter.ViewItemBandeiraHolder>{
    private Context mContext;
    private ArrayList<FlagsBank> mLista;
    private ItemBandeiraAdapter.selecionarBandeira listener;

    public ItemBandeiraAdapter(ArrayList<FlagsBank> pLista, Context pContext, ItemBandeiraAdapter.selecionarBandeira listener) {
        mContext = pContext;
        mLista = pLista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewItemBandeiraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_card_flag, parent, false);
        return new ViewItemBandeiraHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemBandeiraAdapter.ViewItemBandeiraHolder holder, @SuppressLint("RecyclerView") int position) {
        FlagsBank item = mLista.get(position);

        if (item.isActive())
            holder.container.setBackgroundResource(R.drawable.background_rounded_white_border_red);
        else
            holder.container.setBackgroundResource(R.drawable.background_rounded_white);
        holder.image.setImageBitmap(decodeImageBitmap(item.getBase64Image()));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelecionarClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    protected class ViewItemBandeiraHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        ImageView image;

        public ViewItemBandeiraHolder(View view) {
            super(view);
            container = (LinearLayout) view.findViewById(R.id.item_card_container);
            image = (ImageView)view.findViewById(R.id.item_card_image);
        }
    }

    private Bitmap decodeImageBitmap(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public interface selecionarBandeira {
        void onSelecionarClick(int pos);
    }
}
