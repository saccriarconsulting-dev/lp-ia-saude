package com.axys.redeflexmobile.ui.register.customer.attachment;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType.CPF;
import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentAdapter.RegisterCustomerAttachmentAdapterItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public class RegisterCustomerAttachmentViewHolder extends BaseViewHolder<RegisterCustomerAttachmentAdapterItem> {

    @BindView(R.id.fourth_register_attachment_item_ll_container) LinearLayout llContainer;
    @BindView(R.id.fourth_register_attachment_item_iv_thumbnail) ImageView ivThumbnail;
    @BindView(R.id.fourth_register_attachment_item_iv_remove) ImageView ivRemove;
    @BindView(R.id.fourth_register_attachment_item_tv_status) TextView tvStatusRefused;
    @BindView(R.id.fourth_register_attachment_item_tv_name) TextView tvName;
    @BindView(R.id.fourth_register_attachment_item_tv_add) TextView tvAdd;
    @BindView(R.id.fourth_register_attachment_item_tv_size) TextView tvSize;

    private RegisterCustomerAttachmentCallback callback;
    private IBaseViewHolderListener viewHolderListener;

    RegisterCustomerAttachmentViewHolder(View itemView,
                                         RegisterCustomerAttachmentCallback callback,
                                         IBaseViewHolderListener viewHolderListener) {
        super(itemView);
        this.callback = callback;
        this.viewHolderListener = viewHolderListener;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(RegisterCustomerAttachmentAdapterItem object, int position) {

        Glide.with(itemView)
                .load(object.image)
                .apply(new RequestOptions().placeholder(R.drawable.attachment_item_add)
                        .error(R.drawable.attachment_item_add)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(ivThumbnail);

        llContainer.setLayoutParams(prepareMargins(llContainer.getContext(),
                viewHolderListener.isTheLast(position)));

        boolean isAcquisition = (object.customerType == EnumRegisterCustomerType.ACQUISITION || object.customerType == EnumRegisterCustomerType.SUBADQUIRENCIA);
        boolean isPhysical = object.personType == EnumRegisterPersonType.PHYSICAL;
        tvName.setText(!isAcquisition && !isPhysical && object.type == CPF
                ? object.type.getIdentifier()
                : object.type.getDescriptionValue());

        boolean hasImage = StringUtils.isNotEmpty(object.image);
        tvAdd.setVisibility(hasImage ? View.GONE : View.VISIBLE);
        tvSize.setText(hasImage
                ? tvSize.getContext().getString(R.string.customer_register_attachment_item_image_size, object.imageSize)
                : tvSize.getContext().getString(R.string.customer_register_attachment_item_image_size_limit));

        ivRemove.setVisibility(hasImage ? View.VISIBLE : View.GONE);

        if (callback != null && callback.getCompositeDisposable() != null) {
            callback.getCompositeDisposable()
                    .add(RxView.clicks(ivRemove)
                            .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(o -> callback.onRemoveClick(object), throwable -> {
                            }));
            callback.getCompositeDisposable()
                    .add(RxView.clicks(ivThumbnail)
                            .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(o -> {
                                if (!hasImage) {
                                    callback.onThumbnailClick(object);
                                }
                            }, throwable -> {
                            }));
        }

        tvStatusRefused.setVisibility(View.GONE);
        if (object.situation != null && object.situation.equals(3)) {
            tvStatusRefused.setVisibility(View.VISIBLE);
            tvStatusRefused.setText(context.getString(R.string.customer_register_attachment_status,
                    StringUtils.isNotEmpty(object.returnValue)
                            ? object.returnValue
                            : context.getString(R.string.customer_register_attachment_status_refused)));
        }
    }

    public interface RegisterCustomerAttachmentCallback {
        CompositeDisposable getCompositeDisposable();

        void onThumbnailClick(RegisterCustomerAttachmentAdapterItem item);

        void onRemoveClick(RegisterCustomerAttachmentAdapterItem item);
    }
}
