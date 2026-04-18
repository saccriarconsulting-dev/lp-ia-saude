package com.axys.redeflexmobile.ui.dialog;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Bruno Pimentel on 14/01/19.
 */
public class ImageChooserDialog extends BaseDialog {

    @BindView(R.id.image_chooser_ll_camera) LinearLayout llCamera;
    @BindView(R.id.image_chooser_ll_gallery) LinearLayout llGallery;
    @BindView(R.id.image_chooser_bt_cancel) Button btCancel;

    private EnumRegisterAttachmentType type;
    private OnClickListener takePhotoCallback;
    private OnClickListener addFromGalleryCallback;

    public static ImageChooserDialog newInstance(EnumRegisterAttachmentType type,
                                                 @NonNull OnClickListener takePhotoCallback,
                                                 @NonNull OnClickListener addFromGalleryCallback) {
        ImageChooserDialog imageChooserDialog = new ImageChooserDialog();
        imageChooserDialog.type = type;
        imageChooserDialog.takePhotoCallback = takePhotoCallback;
        imageChooserDialog.addFromGalleryCallback = addFromGalleryCallback;
        return imageChooserDialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_image_chooser;
    }

    @Override
    protected void initialize(View view) {
        if (EnumRegisterAttachmentType.FACADE.equals(type)) {
            llGallery.setVisibility(View.GONE);
        } else {
            llGallery.setOnClickListener(addFromGalleryCallback);
        }

        llCamera.setOnClickListener(takePhotoCallback);
        btCancel.setOnClickListener(v -> dismiss());
    }
}
