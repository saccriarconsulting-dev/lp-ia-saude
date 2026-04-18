package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.menu;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.menu.MenuBottomSheetAdapter.MenuBottomSheetViewHolder;

public class MenuBottomSheetAdapter extends RecyclerView.Adapter<MenuBottomSheetViewHolder> {

    private List<MenuBottomPos> menus;
    private PublishSubject<MenuBottomPos> menuSubject = PublishSubject.create();

    MenuBottomSheetAdapter(@NonNull List<MenuBottomPos> menus) {
        this.menus = menus;
    }

    @NonNull
    @Override
    public MenuBottomSheetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cliente_info_pos_menu, viewGroup, false);

        return new MenuBottomSheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuBottomSheetViewHolder viewHolder, int i) {
        MenuBottomPos menu = menus.get(viewHolder.getAdapterPosition());
        viewHolder.bind(menu, menuSubject);
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    Observable<MenuBottomPos> getAction() {
        return menuSubject;
    }

    static class MenuBottomSheetViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        @BindView(R.id.item_cliente_info_pos_menu_iv_icone) AppCompatImageView ivIcone;
        @BindView(R.id.item_cliente_info_pos_menu_tv_nome) AppCompatTextView tvNome;
        @BindView(R.id.item_cliente_info_pos_menu_ll_container) LinearLayout llContainer;

        MenuBottomSheetViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void bind(MenuBottomPos menu, PublishSubject<MenuBottomPos> menuSubject) {
            ivIcone.setImageResource(menu.getIcone());
            tvNome.setText(menu.getNome());
            llContainer.setOnClickListener(view -> menuSubject.onNext(menu));
        }
    }
}
