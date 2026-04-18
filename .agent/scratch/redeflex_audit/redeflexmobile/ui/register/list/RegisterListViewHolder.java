package com.axys.redeflexmobile.ui.register.list;

import static androidx.core.content.ContextCompat.getColor;
import static androidx.core.content.ContextCompat.getDrawable;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;
import static com.axys.redeflexmobile.shared.util.StringUtils.CPF_LENGTH;
import static com.axys.redeflexmobile.shared.util.StringUtils.SPACE_STRING;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus;
import com.axys.redeflexmobile.shared.models.customerregister.RegisterListItem;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Rogério Massa on 21/11/18.
 */

class RegisterListViewHolder extends BaseViewHolder<RegisterListItem> {

    @BindView(R.id.register_item_cv_container) CardView cvContainer;
    @BindView(R.id.register_item_tv_status) TextView tvStatus;
    @BindView(R.id.register_item_tv_name) TextView tvName;
    @BindView(R.id.register_item_tv_date_register) TextView tvDateRegister;
    @BindView(R.id.register_item_tv_date_change) TextView tvDateChange;
    @BindView(R.id.register_item_tv_cpf) TextView tvCpf;
    @BindView(R.id.register_item_tv_reason) TextView tvReason;
    @BindView(R.id.register_item_divider) View viewDivider;
    @BindView(R.id.register_item_tv_see_more) TextView tvSeeMore;

    private Context context;
    private IBaseViewHolderListener listener;
    private RegisterListViewHolderListener clickListener;
    private RegisterListAdapterListener adapterListener;

    RegisterListViewHolder(View itemView,
                           IBaseViewHolderListener listener,
                           RegisterListViewHolderListener clickListener,
                           RegisterListAdapterListener adapterListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        this.clickListener = clickListener;
        this.adapterListener = adapterListener;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(RegisterListItem register, int position) {
        super.bind(register, position);

        EnumRegisterStatus status = EnumRegisterStatus
                .getEnumByValue(register.getStatus(), register.getCustomerType());
        tvStatus.setText(status.getTitle());
        tvStatus.setBackground(getDrawable(context, status.getBackground()));

        if (StringUtils.isNotEmpty(register.getSgvCode())) {
            tvName.setText(String.format("%s - %s", register.getSgvCode(), register.getClientName()));
        } else {
            tvName.setText(register.getClientName());
        }

        tvDateRegister.setText(null);
        if (StringUtils.isNotEmpty(register.getClientDateRegister())) {
            tvDateRegister.setText(setCustomLabel(R.string.frg_main_register_item_date_register,
                    register.getClientDateRegister()));
        }

        tvDateChange.setText(null);
        if (StringUtils.isNotEmpty(register.getClientDateChange())) {
            tvDateChange.setText(setCustomLabel(R.string.frg_main_register_item_date_change,
                    register.getClientDateChange()));
        }

        tvCpf.setText(null);
        if (StringUtils.isNotEmpty(register.getClientCpf())) {
            tvCpf.setText(StringUtils.returnOnlyNumbers(register.getClientCpf()).length() == CPF_LENGTH
                    ? context.getString(R.string.frg_main_register_item_cpf,
                    StringUtils.maskCpfCnpj(register.getClientCpf()))
                    : context.getString(R.string.frg_main_register_item_cnpj,
                    StringUtils.maskCpfCnpj(register.getClientCpf())));
        }

        prepareReasonLayout(register, position);

        if (clickListener != null && clickListener.getCompositeDisposable() != null) {
            clickListener.getCompositeDisposable()
                    .add(RxView.clicks(cvContainer)
                            .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(o -> clickListener.onClickItem(register), throwable -> {
                            }));
        }

        cvContainer.setLayoutParams(prepareMargins(cvContainer.getContext(),
                listener.isTheLast(position)));
    }

    private void prepareReasonLayout(RegisterListItem register, int position) {
        if (StringUtils.isEmpty(register.getReason())) {
            tvReason.setVisibility(View.GONE);
            viewDivider.setVisibility(View.GONE);
            tvSeeMore.setVisibility(View.GONE);
            return;
        }

        viewDivider.setVisibility(View.VISIBLE);
        tvReason.setVisibility(View.VISIBLE);
        tvReason.setText(setCustomLabel(R.string.frg_main_register_item_tv_reason, register.getReason()));
        tvReason.setMaxLines(Integer.MAX_VALUE);
        tvReason.setEllipsize(null);

        tvReason.post(() -> {

            if (tvReason.getLineCount() <= 2) {
                tvReason.setVisibility(View.VISIBLE);
                tvSeeMore.setVisibility(View.GONE);
                return;
            }

            if (adapterListener.isOpened(position)) {
                tvReason.setMaxLines(Integer.MAX_VALUE);
                tvReason.setEllipsize(null);
                tvSeeMore.setText(context.getString(R.string.frg_main_register_item_tv_see_less));
                tvSeeMore.setCompoundDrawablesWithIntrinsicBounds(EMPTY_INT, EMPTY_INT, R.drawable.ic_arrow_up_red_wraped, EMPTY_INT);
            } else {
                tvReason.setMaxLines(2);
                tvReason.setEllipsize(TextUtils.TruncateAt.END);
                tvSeeMore.setText(context.getString(R.string.frg_main_register_item_tv_see_more));
                tvSeeMore.setCompoundDrawablesWithIntrinsicBounds(EMPTY_INT, EMPTY_INT, R.drawable.ic_arrow_down_red_wraped, EMPTY_INT);
            }

            tvSeeMore.setVisibility(View.VISIBLE);
            tvSeeMore.setOnClickListener(v -> adapterListener.expandReason(position));
        });
    }

    private SpannableStringBuilder setCustomLabel(int stringRes, String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(context.getString(stringRes));
        spannableString.setSpan(new ForegroundColorSpan(getColor(context, android.R.color.black)),
                EMPTY_INT, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString).append(SPACE_STRING).append(value);
        return builder;
    }

    interface RegisterListViewHolderListener {
        CompositeDisposable getCompositeDisposable();

        void onClickItem(RegisterListItem register);
    }

    interface RegisterListAdapterListener {
        boolean isOpened(int position);

        void expandReason(int position);
    }
}
