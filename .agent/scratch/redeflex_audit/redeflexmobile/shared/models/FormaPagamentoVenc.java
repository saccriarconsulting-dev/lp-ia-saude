package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 30/09/2016.
 */

public class FormaPagamentoVenc {
    private FormaPagamento formapgto;
    private Date datavencimento;

    public FormaPagamento getFormapgto() {
        return formapgto;
    }

    public void setFormapgto(FormaPagamento formapgto) {
        this.formapgto = formapgto;
    }

    public Date getDatavencimento() {
        return datavencimento;
    }

    public void setDatavencimento(Date datavencimento) {
        this.datavencimento = datavencimento;
    }
}