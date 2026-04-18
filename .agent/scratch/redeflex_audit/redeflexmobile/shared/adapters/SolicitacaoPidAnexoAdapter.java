package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidAnexo;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_Imagem;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.dialog.ImageChooserDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SolicitacaoPidAnexoAdapter extends RecyclerView.Adapter<SolicitacaoPidAnexoAdapter.ViewSolicitacaoPidAnexoHolder> {

    private Context mContext;
    private ArrayList<SolicitacaoPidAnexo> mLista;
    private static final int REQUEST_IMAGE_CAPTURE = 99;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 100;
    private ImageChooserDialog dialog;
    private File tempFile;
    private int currentPosition;
    private FragmentManager mFragmentManager;


    public SolicitacaoPidAnexoAdapter(ArrayList<SolicitacaoPidAnexo> pLista, Context pContext, FragmentManager fragmentManager) {
        mContext = pContext;
        mLista = pLista;
        mFragmentManager = fragmentManager;
    }
    @NonNull
    @Override
    public SolicitacaoPidAnexoAdapter.ViewSolicitacaoPidAnexoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_solicitacaopid_anexos, parent, false);
        return new SolicitacaoPidAnexoAdapter.ViewSolicitacaoPidAnexoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitacaoPidAnexoAdapter.ViewSolicitacaoPidAnexoHolder holder, int position) {
        final SolicitacaoPidAnexo pidAnexo = mLista.get(position);

        Glide.with(holder.itemView)
                .load(pidAnexo.getAnexo())
                .apply(new RequestOptions().placeholder(R.drawable.attachment_item_add)
                        .error(R.drawable.attachment_item_add)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(holder.btnImagemAnexo);

        holder.tvNome.setText(pidAnexo.getTipo());
        holder.tvSituacao.setText(pidAnexo.getTipo());

        boolean hasImage = StringUtils.isNotEmpty(pidAnexo.getAnexo());
        holder.tvTamanho.setText(hasImage
                ? holder.tvTamanho.getContext().getString(R.string.customer_register_attachment_item_image_size, pidAnexo.getTamanhoarquivo())
                : holder.tvTamanho.getContext().getString(R.string.customer_register_attachment_item_image_size_limit));

        holder.tvAdicionar.setVisibility(hasImage ? View.GONE : View.VISIBLE);
        holder.btnDelete.setVisibility(hasImage ? View.VISIBLE : View.GONE);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remover a imagem
                pidAnexo.setAnexo(null);
                notifyItemChanged(position);
            }
        });

        holder.btnImagemAnexo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir a câmera
                currentPosition = position; // Atualiza a posição corrente
                tempFile = Utilidades.setImagem();
                Uri tempUri = FileProvider.getUriForFile(mContext, (mContext.getApplicationContext().getPackageName() + Util_Imagem.PROVIDER), tempFile);
                EnumRegisterAttachmentType attachmentType = getAttachmentType(pidAnexo.getTipo());
                dialog = ImageChooserDialog.newInstance(attachmentType,
                        t -> takePhoto(tempUri),
                        t -> selectImageFromGallery());

                dialog.show(mFragmentManager, ImageChooserDialog.class.getSimpleName());

            }
        });
    }

    private EnumRegisterAttachmentType getAttachmentType(String tipo) {
        switch (tipo){
            case "FotoFachadaPDV":
                return EnumRegisterAttachmentType.FACADE;
            case "Taxa":
                return EnumRegisterAttachmentType.TAXAS_CONCORRENTE;
            default:
                return EnumRegisterAttachmentType.OTHERS;
        }
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    public class ViewSolicitacaoPidAnexoHolder extends RecyclerView.ViewHolder {
        TextView tvSituacao, tvNome, tvAdicionar, tvTamanho;
        ImageView btnDelete, btnImagemAnexo;

        public ViewSolicitacaoPidAnexoHolder(@NonNull View itemView) {
            super(itemView);
            tvSituacao = itemView.findViewById(R.id.solPid_tv_situacao);
            tvNome = itemView.findViewById(R.id.solPid_tv_nome);
            tvAdicionar = itemView.findViewById(R.id.solPid_tv_adicionar);
            tvTamanho = itemView.findViewById(R.id.solPid_tv_tamanho);

            btnDelete = itemView.findViewById(R.id.solPid_iv_remove);
            btnImagemAnexo = itemView.findViewById(R.id.solPid_ImagemAnexo);
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public File getTempFile() {
        return tempFile;
    }

    public interface CameraCallback {
        void onImageCaptured(File imageFile, int position) throws IOException;
        void onImageCaptureFailed();
    }

    private void takePhoto(Uri tempUri) {
        dialog.dismiss();
        startCameraIntent(tempUri);
    }

    private void selectImageFromGallery() {
        dialog.dismiss();
        startGalleryIntent();
    }

    private void startCameraIntent(Uri tempUri) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ((Activity) mContext).startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void startGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ((Activity) mContext).startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY);
    }
}
