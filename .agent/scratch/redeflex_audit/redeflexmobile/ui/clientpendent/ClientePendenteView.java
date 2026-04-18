package com.axys.redeflexmobile.ui.clientpendent;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public interface ClientePendenteView extends BaseActivityView {
    /**
     * Populate the adapter with a list of the clients with pendencies.
     *
     * @param clientes [List<Cliente>]
     */
    void popularAdapter(List<Cliente> clientes);

    /**
     * Load all itens in the table PendenciasClientes.
     *
     * @param pendencias [List<PendenciaCliente>]
     */
    void carregarPendenciasClientes(List<PendenciaCliente> pendencias);

    void showModalTodosClientesRespondidos(boolean isEmpty);

    /**
     * Open the activity with respective client id.
     *
     * @param id        [String]
     * @param latidude  [String]
     * @param longitude [String]
     */
    void abrirClienteActivity(String id, String latidude, String longitude);

    /**
     * Show the modal with reasons.
     *
     * @param motivos          [List<PendenciaMotivo>]
     * @param pendenciaCliente [PendenciaCliente]
     */
    void showModalMotivos(List<PendenciaMotivo> motivos, PendenciaCliente pendenciaCliente);

    /**
     * Save the selected reason id selected in modal, in the data base.
     *
     * @param motivoId         [Integer]
     * @param pendenciaCliente [PendenciaCliente]
     */
    void salvarRespostaModal(int motivoId, PendenciaCliente pendenciaCliente);

    /**
     * Will return the id of the PendenciaCliente table that was updated
     *
     * @param numberRows [Integer]
     */
    void returnUpdatedNumberRows(int numberRows);

    /**
     * Show a loading screen when data are load.
     */
    void showMainLoading();

    /**
     * Hide a loading screen when data are load
     */
    void hideMainLoading();
}
