package com.axys.redeflexmobile.shared.models.requests;

/** Request body para envio de anexos */
public class AnexoRequestBody {
    public final String cpfcnpj;
    public final String imagemBase64;
    public final String tipo;
    public final String idVendedor;
    public final String longitude;
    public final String latitude;
    public final String precisao;

    public AnexoRequestBody(String cpfcnpj,
                            String imagemBase64,
                            String tipo,
                            String idVendedor,
                            String longitude,
                            String latitude,
                            String precisao) {
        this.cpfcnpj      = cpfcnpj;
        this.imagemBase64 = imagemBase64;
        this.tipo         = tipo;
        this.idVendedor   = idVendedor;
        this.longitude    = longitude;
        this.latitude     = latitude;
        this.precisao     = precisao;
    }
}
