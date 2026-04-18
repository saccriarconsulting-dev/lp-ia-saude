package com.axys.redeflexmobile.ui.register.customer.contato;

import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContato;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class RegisterCustomerDadosContatoViewHolder extends BaseViewHolder<CustomerRegisterContato> {
    @BindView(R.id.linha_DadoContato_txtNome) TextView tvNomeContato;
    @BindView(R.id.linha_DadoContato_txtCelular) TextView tvCelular;
    @BindView(R.id.linha_DadoContato_et_remove) ImageView llRemove;

    private RegisterCustomerDadosContatoViewHolderListener callback;

    RegisterCustomerDadosContatoViewHolder(View itemView,
                                           RegisterCustomerDadosContatoViewHolderListener adapterListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        callback = adapterListener;
    }

    @Override
    public void bind(CustomerRegisterContato object, int position) {
        super.bind(object, position);
        tvNomeContato.setText(object.getNome());
        tvCelular.setText(object.getCelular());

        int realPosition = position;
        if (callback != null && callback.getCompositeDisposable() != null) {
            callback.getCompositeDisposable()
                    .add(RxView.clicks(llRemove)
                            .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                            .subscribe(o -> callback.onPosRemove(realPosition), throwable -> {
                            }));
        }
    }

    public interface RegisterCustomerDadosContatoViewHolderListener {
        CompositeDisposable getCompositeDisposable();

        void onPosRemove(int position);
    }
}
