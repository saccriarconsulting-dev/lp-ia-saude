package com.axys.redeflexmobile.ui.clientpendent;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.List;

/**
 * @author Lucas Marciano on 28/02/2020
 */
interface ClientePendentePresenter extends BasePresenter<ClientePendenteView> {

    /**
     * Load the list of the clients with pendencies.
     */
    void carregarClientesPendentes();

    /**
     * Load the list of the clients with pendencies and without responsed.
     */
    void carregarClientesPendentesNaoRespondido();

    void carregarClientePendenteNaoRespondidoPorClienteId(String clienteId);
    /**
     * Load the pendencies by client.
     *
     * @param clientes [List<Cliente>]
     */
    void carregarPendencia(List<Cliente> clientes);

    /**
     * Load the pendencies by client and pendencies.
     *
     * @param clientes [List<Cliente>]
     */
    void carregarPendenciaMotivos(List<Cliente> clientes);

    /**
     * Load the list of the connection table PendenciaCliente.
     */
    void carregarPendenciasCliente();

    /**
     * Open the activity of the selected client.
     *
     * @param cliente [Cliente cliente]
     */
    void abrirClienteActivity(Cliente cliente);

    /**
     * Open modal with the reasons.
     *
     * @param motivos          [List<PendenciaMotivo>]
     * @param pendenciaCliente [PendenciaCliente]
     */
    void showModalMotivos(List<PendenciaMotivo> motivos, PendenciaCliente pendenciaCliente);

    /**
     * Save the selected response in local storage.
     *
     * @param pendenciaCliente [PendenciaCliente]
     */
    void salvarResposta(PendenciaCliente pendenciaCliente);
}
