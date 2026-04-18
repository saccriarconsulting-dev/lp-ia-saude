package com.axys.redeflexmobile.ui.comprovante.container;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface ComprovanteSkyTaPresenter
        extends BasePresenter<ComprovanteSkyTaView> {

    void obterClientes();

    void setCliente(Cliente cliente);

    void setTipo(int tipo);

    void setImage(String image);

    void salvar();

    boolean hasSelectedImage();

    void setCampanha(int idCampanha);

    int getIdCampanha();

    void setTipoMaterial(int tipoMaterial);

    void setInterno(int interno);
}
