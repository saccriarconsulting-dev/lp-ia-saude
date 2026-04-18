package com.axys.redeflexmobile.ui.component;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.ImagemActivity;

/**
 * Created by joao.viana on 30/01/2017.
 */

public class ViewImage extends LinearLayout {
    private Context mContext;
    private LinearLayout mLayout;
    private AttributeSet mAttributeSet;
    private TextView mTitulo;
    private TextView mMensagem;
    private ImageView mImageView;

    public ViewImage(Context context) {
        this(context, null);
    }

    public ViewImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mAttributeSet = attrs;
        iniciar();
    }

    private void iniciar() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_image, this, true);

        mLayout = (LinearLayout) getChildAt(0);
        mTitulo = (TextView) mLayout.getChildAt(0);
        mMensagem = (TextView) mLayout.getChildAt(1);
        mImageView = (ImageView) mLayout.getChildAt(2);

        String valor = "";
        TypedArray typedArray = mContext.obtainStyledAttributes(mAttributeSet, R.styleable.SearchableSpinner);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SearchableSpinner_hintText:
                    valor = typedArray.getString(attr);
                    mTitulo.setText(valor);
                    break;
            }
        }
        typedArray.recycle();
    }

    public void mudarTitulo(String titulo) {
        mTitulo.setText(titulo);
        mTitulo.setHint(titulo);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public void setImageBitmap(Bitmap bitmap, final String localImagem) {
        mImageView.setImageBitmap(bitmap);
        mLayout.setOnClickListener((view) -> {
            try {
                Bundle bundle = new Bundle();
                bundle.putString(Config.LocalImagem, localImagem);
                bundle.putString(Config.NomeImagem, mTitulo.getText().toString());
                Utilidades.openNewActivity(mContext, ImagemActivity.class, bundle, false);
            } catch (ActivityNotFoundException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void setStatus(String status, String retorno) {
        try {
            if (!Util_IO.isNullOrEmpty(status)) {
                if (status.equals("3")) {
                    mLayout.setBackgroundColor(Color.parseColor("#EE0000"));
                    mTitulo.setTextColor(Color.parseColor("#FFFFFF"));
                    if (!Util_IO.isNullOrEmpty(retorno)) {
                        mMensagem.setText("Situação: " + retorno.trim());
                        mMensagem.setVisibility(View.VISIBLE);
                    } else {
                        mMensagem.setText("");
                        mMensagem.setVisibility(View.GONE);
                    }
                } else if (status.equals("0")) {
                    mLayout.setBackgroundColor(Color.parseColor("#EEEEE0"));
                    mTitulo.setTextColor(Color.parseColor("#A52A2A"));
                    mMensagem.setText("");
                    mMensagem.setVisibility(View.GONE);
                }
            } else {
                mLayout.setBackgroundColor(Color.parseColor("#EEEEE0"));
                mTitulo.setTextColor(Color.parseColor("#A52A2A"));
                mMensagem.setText("");
                mMensagem.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}