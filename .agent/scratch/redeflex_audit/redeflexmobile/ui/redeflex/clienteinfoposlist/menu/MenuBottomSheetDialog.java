package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.menu;

import androidx.arch.core.util.Function;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MenuBottomSheetDialog extends BottomSheetDialogFragment {

    public static final int DELAY_BUTTON = 1800;
    private static final String TITULO = "titulo";
    private static final String MOSTRAR_TODAS_OPCOES = "mostrar_todas_opcoes";

    @BindView(R.id.cliente_info_pos_menu_tv_titulo) AppCompatTextView tvTitulo;
    @BindView(R.id.cliente_info_pos_menu_rv_menu) RecyclerView rvMenu;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Function<MenuBottomPos, Void> function;
    private boolean carregarTodos;

    public static MenuBottomSheetDialog newInstance(String titulo, boolean mostrarTodasOpcoes,
                                                    Function<MenuBottomPos, Void> function) {
        Bundle bundle = new Bundle();
        bundle.putString(TITULO, titulo);
        bundle.putBoolean(MOSTRAR_TODAS_OPCOES, mostrarTodasOpcoes);

        MenuBottomSheetDialog menuBottomSheetDialog = new MenuBottomSheetDialog();
        menuBottomSheetDialog.setArguments(bundle);
        menuBottomSheetDialog.setListener(function);
        return menuBottomSheetDialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        List<Fragment> frags = manager.getFragments();
        Fragment fragmento = Stream.of(frags)
                .filter(frag -> frag.getTag() != null && frag.getTag().equals(tag))
                .findFirst()
                .orElse(null);

        if (fragmento == null || !fragmento.isAdded()) {
            super.show(manager, tag);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cliente_info_pos_menu, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        informarTitulo();
        configurarRecycler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void informarTitulo() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }

        String titulo = bundle.getString(TITULO);
        tvTitulo.setText(titulo);
        carregarTodos = bundle.getBoolean(MOSTRAR_TODAS_OPCOES, false);
    }

    private void configurarRecycler() {
        if (getContext() == null) {
            return;
        }

        MenuBottomSheetAdapter adapter = new MenuBottomSheetAdapter(
                MenuBottomPos.getMenus(carregarTodos)
        );

        rvMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMenu.setAdapter(adapter);

        Disposable disposable = adapter.getAction()
                .throttleFirst(DELAY_BUTTON, TimeUnit.MILLISECONDS)
                .subscribe(opcao -> {
                    function.apply(opcao);
                    dismiss();
                });

        compositeDisposable.add(disposable);
    }

    private void setListener(Function<MenuBottomPos, Void> function) {
        this.function = function;
    }
}
