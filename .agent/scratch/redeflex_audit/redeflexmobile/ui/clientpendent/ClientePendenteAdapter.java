package com.axys.redeflexmobile.ui.clientpendent;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Pendencia;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * @author Lucas Marciano on 28/02/2020
 */
class ClientePendenteAdapter extends BaseExpandableListAdapter {
    private static final int BUTTON_WAIT_DURATION = 2200;
    private List<PendenciaCliente> pendenciaClientes = new ArrayList<>();
    private List<Cliente> clients = new ArrayList<>();
    private ClientePendenteActivity.ListenerAdapter listenerShowDialog;
    private ClientePendenteActivity.ListenerOpenNewActivity listenerOpenNewActivity;
    private CompositeDisposable compositeDisposable;

    @Override
    public int getGroupCount() {
        return clients.size();
    }

    @Override
    public int getChildrenCount(int indexChild) {
        return clients.get(indexChild).getPendencias().size() + 1;
    }

    @Override
    public Object getGroup(int indexGroup) {
        return clients.get(indexGroup);
    }

    @Override
    public Object getChild(int indexGroup, int indexChild) {
        return clients.get(indexChild).getPendencias();
    }

    @Override
    public long getGroupId(int indexGroup) {
        return indexGroup;
    }

    @Override
    public long getChildId(int indexGroup, int indexChild) {
        return indexChild;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        view = View.inflate(parent.getContext(), R.layout.item_cliente_pendente, null);

        Cliente cliente = this.clients.get(groupPosition);

        ((TextView) view.findViewById(R.id.tv_cliente_name)).setText(
                parent.getContext().getString(
                        R.string.label_name_client_pendent, cliente.retornaCodigoSvgIntraflex(),
                        cliente.getNomeFantasia())
        );

        String label = parent.getResources().getQuantityString(
                R.plurals.label_number_pendencies_client,
                cliente.getPendencias().size(),
                cliente.getPendencias().size());

        ((TextView) view.findViewById(R.id.tv_total_pendencias)).setText(label);

        compositeDisposable.add(RxView.clicks(view.findViewById(R.id.tv_cliente_detalhe_link))
                .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(v -> listenerOpenNewActivity.onOpenNewActivity(cliente), Timber::e)
        );

        ((TextView) view.findViewById(R.id.tv_cliente_situacao)).setText(
                parent.getContext().getString(
                        R.string.label_situation_client,
                        cliente.retornaSituacao()));

        if (isExpanded) {
            ((ImageView) view.findViewById(R.id.iv_indicator_group)).setImageResource(R.drawable.ic_arrow_up_red);
            view.findViewById(R.id.tv_cliente_detalhe_link).setVisibility(View.GONE);
            view.findViewById(R.id.group_hide_layout_expanded).setVisibility(View.GONE);
            view.findViewById(R.id.tv_label_title_pendencias).setVisibility(View.VISIBLE);
            view.findViewById(R.id.container_child).setBackground(ContextCompat.getDrawable(parent.getContext(),
                    R.drawable.bg_rounded_corners_top_withe));

        } else {
            ((ImageView) view.findViewById(R.id.iv_indicator_group)).setImageResource(R.drawable.ic_arrow_down_red);
            view.findViewById(R.id.tv_cliente_detalhe_link).setVisibility(View.VISIBLE);
            view.findViewById(R.id.group_hide_layout_expanded).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tv_label_title_pendencias).setVisibility(View.GONE);
            view.findViewById(R.id.container_child).setBackground(ContextCompat.getDrawable(parent.getContext(),
                    R.drawable.bg_rounded_corners_withe));
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        Cliente cliente = clients.get(groupPosition);
        if (cliente.getPendencias().size() >= (childPosition + 1)) {
            view = View.inflate(parent.getContext(), R.layout.item_pendencia_in_cliente_pendencia, null);
            Pendencia pendencia = cliente.getPendencias().get(childPosition);
            ((TextView) view.findViewById(R.id.tv_pendencia_descricao)).setText(pendencia.getDescricao());

            PendenciaCliente pendenciaCliente = obterPendenciaCliente(pendencia, cliente, childPosition);
            if (pendenciaCliente != null && pendenciaCliente.getPendenciaMotivoId() > 0) {
                PendenciaMotivo pendenciaMotivo = obterPendenciaMotivo(pendenciaCliente, pendencia);
                view.findViewById(R.id.tv_motivo_descricao).setVisibility(View.VISIBLE);
                if (pendenciaCliente.isEmptyExplication()) {
                    view.findViewById(R.id.tv_explication).setVisibility(View.VISIBLE);
                    ((TextView) view.findViewById(R.id.tv_explication)).setText(
                            parent.getContext().getString(
                                    R.string.label_explication_description,
                                    pendenciaCliente.getExplicacao())
                    );
                }

                ((TextView) view.findViewById(R.id.tv_motivo_descricao)).setText(
                        parent.getContext().getString(
                                R.string.label_motivo_description,
                                validaPendenciaMotivo(pendenciaMotivo, view))
                );
                ((ImageView) view.findViewById(R.id.iv_indicator_response)).setImageResource(R.drawable.ic_check_pendencia_responded);
            } else {
                view.findViewById(R.id.tv_motivo_descricao).setVisibility(View.GONE);
                view.findViewById(R.id.tv_explication).setVisibility(View.GONE);
                ((ImageView) view.findViewById(R.id.iv_indicator_response))
                        .setImageResource(R.drawable.ic_check_pendencia_not_responded);

                compositeDisposable.add(RxView.clicks(view)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> listenerShowDialog.onShowDialogWithResponses(
                                pendencia.getMotivos(),
                                pendenciaCliente
                        ), Timber::e)
                );
            }

            if (pendenciaCliente.getObservacao() != null) {
                view.findViewById(R.id.tv_pendencia_observacao).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.tv_pendencia_observacao)).setText(pendenciaCliente.getObservacao());
            } else {
                view.findViewById(R.id.tv_pendencia_observacao).setVisibility(View.GONE);
            }
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_pendencia_in_cliente_link, null);

            compositeDisposable.add(RxView.clicks(view.findViewById(R.id.tv_cliente_detalhe_link))
                    .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                    .subscribe(v -> listenerOpenNewActivity.onOpenNewActivity(cliente), Timber::e)
            );
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    void setListenerShowDialog(ClientePendenteActivity.ListenerAdapter listener) {
        this.listenerShowDialog = listener;
    }

    void setListenerOpenNewActivity(ClientePendenteActivity.ListenerOpenNewActivity listenerOpenNewActivity) {
        this.listenerOpenNewActivity = listenerOpenNewActivity;
    }

    void setClients(List<Cliente> clients) {
        this.clients = clients;
        notifyDataSetChanged();
    }

    public void setCompositeDisposable(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    void setPendenciaClientes(List<PendenciaCliente> pendenciaClientes) {
        this.pendenciaClientes = pendenciaClientes;
    }

    private String validaPendenciaMotivo(PendenciaMotivo pendenciaMotivo, View view) {
        if (pendenciaMotivo != null && pendenciaMotivo.getDescricao() != null) {
            return pendenciaMotivo.getDescricao();
        }

        return view.getContext().getString(R.string.cliente_pendente_mensagem_erro_relacionamento);
    }

    private PendenciaCliente obterPendenciaCliente(Pendencia pendencia, Cliente cliente, int childPosition) {
        List<PendenciaCliente> response = new ArrayList<>();
        for (PendenciaCliente pc : pendenciaClientes) {
            if (String.valueOf(pc.getClienteId()).equals(cliente.getId()) &&
                    String.valueOf(pc.getPendenciaId()).equals(pendencia.getPendenciaId())) {
                response.add(pc);
            }
        }
        return response.size() > childPosition ? response.get(childPosition) : response.get(response.size() - 1);
    }

    private PendenciaMotivo obterPendenciaMotivo(PendenciaCliente pendenciaCliente, Pendencia pendencia) {
        for (PendenciaMotivo pm : pendencia.getMotivos()) {
            if (pm.getPendenciaMotivoId() == pendenciaCliente.getPendenciaMotivoId()) {
                return pm;
            }
        }
        return null;
    }
}
