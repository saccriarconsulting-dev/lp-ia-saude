package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.menu;

import com.axys.redeflexmobile.R;

import java.util.ArrayList;
import java.util.List;

public class MenuBottomPos {

    private int icone;
    private String nome;
    private EnumMenuBottomPos type;

    private MenuBottomPos(int icone, EnumMenuBottomPos enumMenuBottomPos) {
        this.icone = icone;
        this.nome = enumMenuBottomPos.getValue();
        this.type = enumMenuBottomPos;
    }

    static List<MenuBottomPos> getMenus(boolean carregarTodos) {
        List<MenuBottomPos> lista = new ArrayList<>();
        lista.add(new MenuBottomPos(R.drawable.ic_pos_detalhe_wrapped, EnumMenuBottomPos.DETALHES));
        if (carregarTodos) {
            lista.add(new MenuBottomPos(R.drawable.ic_pos_troca_wrapped, EnumMenuBottomPos.TROCAR));
            lista.add(new MenuBottomPos(R.drawable.ic_pos_remove_wrapped, EnumMenuBottomPos.REMOVER));
        }

        return lista;
    }

    int getIcone() {
        return icone;
    }

    public String getNome() {
        return nome;
    }

    public EnumMenuBottomPos getType() {
        return type;
    }

    public enum EnumMenuBottomPos {
        DETALHES("Detalhes"), TROCAR("Trocar"), REMOVER("Remover");

        public String value;

        EnumMenuBottomPos(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
