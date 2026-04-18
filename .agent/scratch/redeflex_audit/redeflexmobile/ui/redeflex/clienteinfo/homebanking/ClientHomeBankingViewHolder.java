package com.axys.redeflexmobile.ui.redeflex.clienteinfo.homebanking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.constraintlayout.widget.Group;
import androidx.cardview.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientHomeBanking;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lucasmarciano on 01/07/20
 */
@SuppressLint("NonConstantResourceId")
class ClientHomeBankingViewHolder extends BaseViewHolder<ClientHomeBanking> {

    @BindView(R.id.card_container) CardView cardContainer;
    @BindView(R.id.tv_name_bank) TextView tvBankName;
    @BindView(R.id.tv_count_type) TextView tvCountType;
    @BindView(R.id.tv_agency) TextView tvAgency;
    @BindView(R.id.tv_count_number) TextView tvCountNumber;

    @BindView(R.id.iv_indicator_group) ImageView ivIndicatorGroup;
    @BindView(R.id.group_hide_layout_expanded) Group groupLayoutFlags;
    @BindView(R.id.ll_flags) LinearLayout llFlags;
    @BindView(R.id.tv_label_flag) TextView tvFlags;

    private final Context context;

    ClientHomeBankingViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(ClientHomeBanking item, int position) {
        super.bind(item, position);
        setupInfoViews(item);
        setupVisibilityActions();
        setupListBanks(item.getFlags());
    }

    private void setupInfoViews(ClientHomeBanking item) {
        tvBankName.setText(item.getBankName());
        setLabel(tvCountType, context.getString(R.string.label_type_count), item.getCountType());
        setLabel(tvAgency, context.getString(R.string.label_agency), item.getAgencyInfo() );
        setLabel(tvCountNumber, context.getString(R.string.label_count_number), item.getCountInfo());
    }

    private void setupVisibilityActions() {
        cardContainer.setOnClickListener(v -> {
            int visibility = groupLayoutFlags.getVisibility();
            if (visibility == View.VISIBLE) {
                ivIndicatorGroup.setImageResource(R.drawable.ic_arrow_down_red);
                groupLayoutFlags.setVisibility(View.GONE);
            } else {
                ivIndicatorGroup.setImageResource(R.drawable.ic_arrow_up_red);
                groupLayoutFlags.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupListBanks(@Nullable List<FlagsBank> flags) {
        if (flags != null && flags.size() > 0) {
            for (FlagsBank flag : flags) {
                if (flag == null) {
                    tvFlags.setVisibility(View.GONE);
                    continue;
                }

                tvFlags.setVisibility(View.VISIBLE);
                LinearLayout llFlag = createLinearLayout();

                ImageView ivFlagImage = createImageView();

                byte[] imageByteArray = Base64.decode(flag.getImage(), Base64.DEFAULT);
                Glide.with(context)
                        .load(imageByteArray)
                        .apply(new RequestOptions().placeholder(R.drawable.attachment_item_add)
                                .error(R.drawable.bg_rav_message)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(ivFlagImage);

                llFlag.addView(ivFlagImage);

                TextView tvFlagName = createTextView();
                tvFlagName.setText(flag.getName());
                llFlag.addView(tvFlagName);

                llFlags.addView(llFlag);
            }
        }
    }

    private ImageView createImageView() {
        int DIMENSION = 28;
        int size = Math.round(DeviceUtils.convertDpToPixel(DIMENSION));
        ImageView ivFlagImage = new ImageView(context);
        ivFlagImage.setAdjustViewBounds(true);
        ivFlagImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ivFlagImage.setLayoutParams(new LinearLayout.LayoutParams(size, size));
        return ivFlagImage;
    }

    private LinearLayout createLinearLayout() {
        LinearLayout parent = new LinearLayout(context);
        parent.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        );
        parent.setVerticalGravity(Gravity.CENTER);
        parent.setOrientation(LinearLayout.HORIZONTAL);

        return parent;
    }

    private TextView createTextView() {
        int MARGIN_VERTICAL = 16;
        int MARGIN_HORIZONTAL = 4;

        TextView tvFlagName = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(MARGIN_VERTICAL, MARGIN_HORIZONTAL, MARGIN_VERTICAL, MARGIN_HORIZONTAL);
        tvFlagName.setLayoutParams(params);

        return tvFlagName;
    }

    private void setLabel(TextView field, String label, String value) {
        if (field == null || label == null) return;
        if (Util_IO.isNullOrEmpty(value)) {
            field.setText("");
            field.setVisibility(View.GONE);
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString span = new SpannableString(label);
        span.setSpan(new ForegroundColorSpan(Color.BLACK), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(span).append(" ").append(value);
        field.setText(builder);
    }
}
