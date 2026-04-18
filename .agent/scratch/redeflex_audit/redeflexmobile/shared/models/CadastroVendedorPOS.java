package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by diego.lobo on 28/02/2018.
 */

public class CadastroVendedorPOS {
    private Integer Id;
    private Integer TipoMaquinaId;
    private Integer IdVendedor;
    private Date DataSync;
    private Double ValorAluguel;
    private Integer IdLimite;
    private TipoMaquina TipoMaquina;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getTipoMaquinaId() {
        return TipoMaquinaId;
    }

    public void setTipoMaquinaId(Integer tipoMaquinaId) {
        TipoMaquinaId = tipoMaquinaId;
    }

    public Integer getIdVendedor() {
        return IdVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        IdVendedor = idVendedor;
    }

    public Date getDataSync() {
        return DataSync;
    }

    public void setDataSync(Date dataSync) {
        DataSync = dataSync;
    }

    public Double getValorAluguel() {
        return ValorAluguel;
    }

    public void setValorAluguel(Double valorAluguel) {
        ValorAluguel = valorAluguel;
    }

    public Integer getIdLimite() {
        return IdLimite;
    }

    public void setIdLimite(Integer idLimite) {
        IdLimite = idLimite;
    }

    public com.axys.redeflexmobile.shared.models.TipoMaquina getTipoMaquina() {
        return TipoMaquina;
    }

    public void setTipoMaquina(com.axys.redeflexmobile.shared.models.TipoMaquina tipoMaquina) {
        TipoMaquina = tipoMaquina;
    }
}
