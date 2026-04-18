package com.axys.redeflexmobile.shared.models.venda;

public class VerificaBotoesVenda {
    private boolean auditagemObrigatoria;
    private boolean merchandisingObrigatorio;
    private boolean jaFezAuditagem;
    private boolean jaFezMerchandising;

    public VerificaBotoesVenda() {
    }

    public VerificaBotoesVenda(boolean auditagemObrigatoria, boolean merchandisingObrigatorio) {
        this.auditagemObrigatoria = auditagemObrigatoria;
        this.merchandisingObrigatorio = merchandisingObrigatorio;
    }

    public boolean isAuditagemObrigatoria() {
        return auditagemObrigatoria;
    }

    public void setAuditagemObrigatoria(boolean auditagemObrigatoria) {
        this.auditagemObrigatoria = auditagemObrigatoria;
    }

    public boolean isMerchandisingObrigatorio() {
        return merchandisingObrigatorio;
    }

    public void setMerchandisingObrigatorio(boolean merchandisingObrigatorio) {
        this.merchandisingObrigatorio = merchandisingObrigatorio;
    }

    public boolean jaFezAuditagem() {
        return jaFezAuditagem;
    }

    public void setJaFezAuditagem(boolean jaFezAuditagem) {
        this.jaFezAuditagem = jaFezAuditagem;
    }

    public boolean jaFezMerchandising() {
        return jaFezMerchandising;
    }

    public void setJaFezMerchandising(boolean jaFezMerchandising) {
        this.jaFezMerchandising = jaFezMerchandising;
    }
}
