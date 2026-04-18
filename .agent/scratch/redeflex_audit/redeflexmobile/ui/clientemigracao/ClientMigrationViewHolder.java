package com.axys.redeflexmobile.ui.clientemigracao;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.cardview.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus;
import com.axys.redeflexmobile.shared.models.migracao.ClientMigrationResponse;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.core.content.ContextCompat.getColor;
import static androidx.core.content.ContextCompat.getDrawable;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType.MIGRATION;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.StringUtils.CPF_LENGTH;
import static com.axys.redeflexmobile.shared.util.StringUtils.SPACE_STRING;

/**
 * @author Lucas Marciano on 23/03/2020
 */
@SuppressLint("NonConstantResourceId")
public class ClientMigrationViewHolder extends BaseViewHolder<ClientMigrationResponse> {

    @BindView(R.id.register_item_cv_container) CardView cvContainer;
    @BindView(R.id.register_item_tv_status) TextView tvStatus;
    @BindView(R.id.register_item_tv_migracao) TextView tvTipoMigracao;
    @BindView(R.id.register_item_tv_name) TextView tvName;
    @BindView(R.id.register_item_tv_date_register) TextView tvDateRegister;
    @BindView(R.id.register_item_tv_date_change) TextView tvDateChange;
    @BindView(R.id.register_item_tv_cpf) TextView tvCpf;
    @BindView(R.id.register_item_tv_reason) TextView tvReason;
    @BindView(R.id.register_item_divider) View vLine;

    private final Context context;
    private final ClientMigrationViewHolderListener clickListener;

    ClientMigrationViewHolder(View itemView, ClientMigrationViewHolderListener clickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.clickListener = clickListener;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(ClientMigrationResponse client, int position) {
        super.bind(client, position);

        tvName.setText(client.getClient().getNomeFantasia());
        EnumRegisterStatus status;
        if (client.getRegisterMigrationSub() == null)
            status = EnumRegisterStatus.getEnumByValue(90, MIGRATION);
        else
            status = EnumRegisterStatus.getEnumByValue(client.getRegisterMigrationSub().getSituacao(), MIGRATION);

        tvStatus.setText(status.getTitle());
        tvStatus.setBackground(getDrawable(context, status.getBackground()));

        tvCpf.setText(StringUtils.returnOnlyNumbers(client.getClient().getCpf_cnpj()).length() == CPF_LENGTH
                ? context.getString(R.string.frg_main_register_item_cpf,
                StringUtils.maskCpfCnpj(client.getClient().getCpf_cnpj()))
                : context.getString(R.string.frg_main_register_item_cnpj,
                StringUtils.maskCpfCnpj(client.getClient().getCpf_cnpj())));

        if (client.getRegisterMigrationSub() == null) {
            if (client.getClient().isClienteMigracaoAdq())
                tvTipoMigracao.setText(setCustomLabel(R.string.frg_main_register_item_tipomigracao, "ADQ"));
            else if (client.getClient().isClienteMigracaoSub())
                tvTipoMigracao.setText(setCustomLabel(R.string.frg_main_register_item_tipomigracao, "SUB"));
            else
                tvTipoMigracao.setText(setCustomLabel(R.string.frg_main_register_item_tipomigracao, ""));

            tvDateRegister.setText(setCustomLabel(R.string.frg_main_register_item_date_register, "" ));
            tvDateChange.setText(setCustomLabel(R.string.frg_main_register_item_date_change,""));
            tvReason.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        }
        else {
            tvTipoMigracao.setText(setCustomLabel(R.string.frg_main_register_item_tipomigracao, client.getRegisterMigrationSub().getTipoMigracao()));
            tvDateRegister.setText(setCustomLabel(R.string.frg_main_register_item_date_register, Util_IO.stringDateTimeToString(client.getRegisterMigrationSub().getDataCadastro())));
            tvDateChange.setText(setCustomLabel(R.string.frg_main_register_item_date_change,Util_IO.stringDateTimeToString(client.getRegisterMigrationSub().getDataCadastro())));

            if (!Util_IO.isNullOrEmpty(client.getRegisterMigrationSub().getRetorno())) {
                tvReason.setVisibility(View.VISIBLE);
                vLine.setVisibility(View.VISIBLE);
                tvReason.setText(setCustomLabel(R.string.frg_main_register_item_tv_reason,client.getRegisterMigrationSub().getRetorno()));
            } else {
                tvReason.setVisibility(View.GONE);
                vLine.setVisibility(View.GONE);
            }
        }
        cvContainer.setOnClickListener(v -> clickListener.clickEvent(client));
    }

    private SpannableStringBuilder setCustomLabel(int stringRes, String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(context.getString(stringRes));
        spannableString.setSpan(new ForegroundColorSpan(getColor(context, android.R.color.black)),
                EMPTY_INT, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString).append(SPACE_STRING).append(value);
        return builder;
    }

    interface ClientMigrationViewHolderListener {
        void clickEvent(ClientMigrationResponse clientMigrationResponse);
    }

    interface ClientMigrationAdapterListener {
    }
}
