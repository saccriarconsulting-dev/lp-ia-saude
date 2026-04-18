package com.axys.redeflexmobile.ui.dialog;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaixaPosDialog extends BaseDialog {

    public static final int BUTTON_DELAY_DURATION = 1800;
    private static final String TITULO = "titulo";
    private static final String MENSAGEM = "mensagem";
    private static final String POSITIVE = "positive";
    private static final String NEGATIVE = "negative";

    @BindView(R.id.baixa_pos_tv_titulo) AppCompatTextView tvTitulo;
    @BindView(R.id.baixa_pos_tv_mensagem) AppCompatTextView tvMensagem;
    @BindView(R.id.baixa_pos_btn_ok) AppCompatButton btnPositive;
    @BindView(R.id.baixa_pos_btn_cancelar) AppCompatButton btnNegative;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Nullable private ButtonListener positiveListener;
    @Nullable private ButtonListener negativeListener;

    public static BaixaPosDialog newInstance(int titulo, int mensagem) {
        BaixaPosDialog baixaPosDialog = new BaixaPosDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(TITULO, titulo);
        bundle.putInt(MENSAGEM, mensagem);

        baixaPosDialog.setArguments(bundle);

        return baixaPosDialog;
    }

    public static BaixaPosDialog newInstance(int titulo, String mensagem, int positiveButton, int negativeButton) {
        BaixaPosDialog baixaPosDialog = new BaixaPosDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(TITULO, titulo);
        bundle.putString(MENSAGEM, mensagem);
        bundle.putInt(POSITIVE, positiveButton);
        bundle.putInt(NEGATIVE, negativeButton);

        baixaPosDialog.setArguments(bundle);

        return baixaPosDialog;
    }

    public static BaixaPosDialog newInstance(String titulo, String mensagem) {
        BaixaPosDialog baixaPosDialog = new BaixaPosDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TITULO, titulo);
        bundle.putString(MENSAGEM, mensagem);

        baixaPosDialog.setArguments(bundle);

        return baixaPosDialog;
    }

    public BaixaPosDialog setPositiveListener(@Nullable ButtonListener buttonListener) {
        this.positiveListener = buttonListener;

        return this;
    }

    public BaixaPosDialog setNegativeListener(@Nullable ButtonListener buttonListener) {
        this.negativeListener = buttonListener;

        return this;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        List<Fragment> frags = manager.getFragments();
        Optional<Fragment> fragmento = Stream.of(frags)
                .filter(frag -> frag.getTag() != null && frag.getTag().equals(tag))
                .findFirst();

        if (fragmento.isEmpty() || !fragmento.get().isAdded()) {
            super.show(manager, tag);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_baixa_pos;
    }

    @Override
    protected void initialize(View view) {
        setCancelable(false);
        configurarTitulo();
        criarEventos();
        ajustarBotoes();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void configurarTitulo() {
        final Bundle args = getArguments();
        if (args == null) {
            return;
        }

        final int resourceTitle = args.getInt(TITULO, 0);
        if (resourceTitle != 0) {
            tvTitulo.setText(args.getInt(TITULO));
        }

        final String title = args.getString(TITULO, null);
        if (title != null) {
            tvTitulo.setText(title);
        }

        final int resourceString = args.getInt(MENSAGEM, 0);
        if (resourceString != 0) {
            tvMensagem.setText(resourceString);
        }

        final String mensagem = args.getString(MENSAGEM, null);
        if (mensagem != null) {
            tvMensagem.setText(mensagem);
        }

    }

    private void criarEventos() {
        Disposable positiveDisposable = RxView.clicks(btnPositive)
                .throttleFirst(BUTTON_DELAY_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(view -> {
                    if (positiveListener != null) {
                        positiveListener.onTapButton();
                    }
                    dismissAllowingStateLoss();
                });

        Disposable negativeDisposable = RxView.clicks(btnNegative)
                .throttleFirst(BUTTON_DELAY_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(view -> {
                    if (negativeListener != null) {
                        negativeListener.onTapButton();
                    }
                    dismissAllowingStateLoss();
                });

        compositeDisposable.add(positiveDisposable);
        compositeDisposable.add(negativeDisposable);
    }

    private void ajustarBotoes() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }

        int negative = args.getInt(NEGATIVE, 0);
        if (negative != 0) {
            btnNegative.setText(args.getInt(NEGATIVE));
            btnNegative.setVisibility(View.VISIBLE);
        }

        int positive = args.getInt(POSITIVE, 0);
        if (positive != 0) {
            btnPositive.setText(args.getInt(POSITIVE));
        }
    }

    public interface ButtonListener {
        void onTapButton();
    }
}
