package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Vendedor {

    @SerializedName("IdVendedor") public long idVendedor;
    @SerializedName("Vendedor") public String vendedor;
    public boolean selecionado;
    public List<RelatorioSupervisorProduto> produtos = new ArrayList<>();
    public boolean atualizado;
    @SerializedName("SemanaRota") public int semanaRota;

    public Vendedor() {
    }

    public Vendedor(long idVendedor, String vendedor, int semanaRota) {
        this.idVendedor = idVendedor;
        this.vendedor = vendedor;
        this.semanaRota = semanaRota;
    }

    public long getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(long idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public int getSemanaRota() {
        return semanaRota;
    }

    public void setSemanaRota(int semanaRota) {
        this.semanaRota = semanaRota;
    }
}
