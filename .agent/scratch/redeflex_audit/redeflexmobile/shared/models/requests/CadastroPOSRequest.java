package com.axys.redeflexmobile.shared.models.requests;

import java.util.List;

public class CadastroPOSRequest {
    private final String idVendedor;
    private final List<Integer> idsCadastros;

    public CadastroPOSRequest(String idVendedor, List<Integer> idsCadastros) {
        this.idVendedor    = idVendedor;
        this.idsCadastros  = idsCadastros;
    }

    public String getIdVendedor()         { return idVendedor; }
    public List<Integer> getIdsCadastros(){ return idsCadastros; }
}