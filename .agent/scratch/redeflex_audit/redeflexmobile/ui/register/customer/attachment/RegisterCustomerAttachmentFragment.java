package com.axys.redeflexmobile.ui.register.customer.attachment;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAttachment;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.dialog.ImageChooserDialog;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;
import com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentAdapter.RegisterCustomerAttachmentAdapterItem;
import com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentViewHolder.RegisterCustomerAttachmentCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import id.zelory.compressor.Compressor;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Rogério Massa on 14/01/19.
 */

public class RegisterCustomerAttachmentFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerAttachmentView,
        RegisterCustomerAttachmentCallback {

    @Inject RegisterCustomerAttachmentPresenter presenter;
    @Inject RegisterCustomerAttachmentAdapter attachmentAdapter;

    @BindView(R.id.customer_register_attachment_tv_title_number) TextView tvTitleNumber;
    @BindView(R.id.customer_register_attachment_rv_list) RecyclerView rvList;

    private File tempFile;
    private ImageChooserDialog dialog;
    private RegisterCustomerAttachmentAdapterItem selectedAttachment;
    private CompositeDisposable compositeDisposable;

    public static RegisterCustomerAttachmentFragment newInstance() {
        return new RegisterCustomerAttachmentFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_attachment;
    }

    @Override
    public void initialize() {
        compositeDisposable = new CompositeDisposable();
        presenter.attachView(this);

        rvList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvList.setNestedScrollingEnabled(false);
        rvList.setAdapter(attachmentAdapter);
        presenter.initializeData();
        rvList.post(() -> rvList.scrollTo(EMPTY_INT, EMPTY_INT));
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
        presenter.clearDispose();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void setHeader(boolean acquisition) {
        tvTitleNumber.setText(acquisition
                ? R.string.customer_register_attachment_title_number_second_flow
                : R.string.customer_register_attachment_title_number_first_flow);
    }

    @Override
    public void fillAttachments(List<RegisterCustomerAttachmentAdapterItem> items) {
        attachmentAdapter.setList(items);
    }

    @Override
    public void showError(String error) {
        showOneButtonDialog(getString(R.string.app_name), error, null);
    }

    @Override
    public void onValidationSuccess() {
        parentActivity.doComplete();
    }

    @Override
    public void onValidationSuccessBack() {
        parentActivity.stepValidatedBack();
    }

    @Override
    public void setNumeroTitulo(String pNumero) {
        tvTitleNumber.setText(pNumero);
    }

    @Override
    public void persistData() {
        presenter.doSave(false);
    }

    @Override
    public void persistCloneData() {
        presenter.doSave(true);
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public void onThumbnailClick(RegisterCustomerAttachmentAdapterItem item) {
        if (EnumRegisterAttachmentType.FACADE.equals(item.type)
                && !Validacoes.validacaoDataAparelhoAdquirencia(getContext())) {
            return;
        }

        dialog = ImageChooserDialog.newInstance(item.type,
                v -> takePhoto(item),
                v -> selectImageFromGallery(item));
        dialog.show(requireFragmentManager(), ImageChooserDialog.class.getSimpleName());
    }

    @Override
    public void onRemoveClick(RegisterCustomerAttachmentAdapterItem item) {
        presenter.deleteAttachment(item);
        attachmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        attachmentAdapter.notifyDataSetChanged();
        try {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            String filePath = Utilidades.getFilePath(requireContext());
            if (requestCode == RequestCode.CaptureImagem) {
                prepareImage(filePath, tempFile);
            }

            if (requestCode == RequestCode.GaleriaImagem) {
                getImageFromGallery(data, filePath);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(requireContext(), ex.getMessage(), false);
        }
    }

    private void takePhoto(RegisterCustomerAttachmentAdapterItem item) {
        dialog.dismiss();
        selectedAttachment = item;
        tempFile = Utilidades.setImagem();
        startCameraIntent();
    }

    private void selectImageFromGallery(RegisterCustomerAttachmentAdapterItem item) {
        dialog.dismiss();
        selectedAttachment = item;
        startGalleryIntent();
    }

    private void startCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(requireContext(),
                BuildConfig.APPLICATION_ID + ".provider", tempFile));
        startActivityForResult(intent, RequestCode.CaptureImagem);
    }

    private void startGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RequestCode.GaleriaImagem);
    }

    private void getImageFromGallery(Intent data, String filePath) {
        try {
            File file = Utilidades.getImagemFromGallery(data, requireContext());
            prepareImage(filePath, file);
        } catch (Exception ex) {
            Utilidades.retornaMensagem(requireActivity(), "Erro: Ocorreu um erro ao carregar a imagem --> " + ex.getMessage(), true);
        }
    }

    private void prepareImage(String path, File file) throws IOException, NullPointerException {
        File finalPhoto = new Compressor(requireContext())
                .setDestinationDirectoryPath(path)
                .compressToFile(file);

        if (finalPhoto == null) throw new IOException("Imagem não selecionada");

        CustomerRegisterAttachment upload = new CustomerRegisterAttachment();
        upload.setSituation(0);
        upload.setType(selectedAttachment.type.getType());
        upload.setFile(finalPhoto.getAbsolutePath());
        upload.setFileSize(Formatter.formatShortFileSize(requireContext(), finalPhoto.length()));
        upload.setActivated(true);

        GPSTracker tracker = new GPSTracker(requireContext());
        upload.setLatitude(tracker.getLatitude());
        upload.setLongitude(tracker.getLongitude());
        upload.setPrecision(tracker.getPrecisao());

        presenter.saveAttachment(upload);
        tempFile = null;
    }
}
