package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;

import java.util.ArrayList;

public class BandeirasAdapter extends RecyclerView.Adapter<BandeirasAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<FlagsBank> mLista;

    public BandeirasAdapter(ArrayList<FlagsBank> pLista, Context pContext) {
        mContext = pContext;
        mLista = pLista;
    }

    @NonNull
    @Override
    public BandeirasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_bandeiras, parent, false);
        return new BandeirasAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BandeirasAdapter.ViewHolder holder, int position) {
        byte[] imageBytes = Base64.decode(mLista.get(position).getBase64Image(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.btnBandeira.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnBandeira;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnBandeira = itemView.findViewById(R.id.imgButton_bandeira);
        }
    }
}
