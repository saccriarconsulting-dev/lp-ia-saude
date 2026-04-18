package com.axys.redeflexmobile.shared.models.requests;

public class AtendimentoRequest {
    private final String id;
    private final String dataAtendimento;
    private final String observacao;
    private final String versaoApp;

    public AtendimentoRequest(
            long id,
            String dataAtendimento,
            String observacao,
            String versaoApp
    ) {
        this.id               = String.valueOf(id);
        this.dataAtendimento  = dataAtendimento;
        this.observacao       = observacao;
        this.versaoApp        = versaoApp;
    }

    public String getId()                { return id; }
    public String getDataAtendimento()   { return dataAtendimento; }
    public String getObservacao()        { return observacao; }
    public String getVersaoApp()         { return versaoApp; }
}