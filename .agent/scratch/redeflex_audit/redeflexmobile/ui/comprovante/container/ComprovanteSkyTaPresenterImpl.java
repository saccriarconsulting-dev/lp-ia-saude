package com.axys.redeflexmobile.ui.comprovante.container;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.TypeVoucherEnum;
import com.axys.redeflexmobile.shared.manager.ComprovanteSkyTaManager;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ComprovanteSkyTa;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ComprovanteSkyTaPresenterImpl
        extends BasePresenterImpl<ComprovanteSkyTaView>
        implements ComprovanteSkyTaPresenter {

    private final ComprovanteSkyTaManager manager;
    private Cliente cliente;
    private int tipo = -1;
    private String image = null;
    private int idCampanha = -1;
    private int tipoMaterial = -1;
    private int interno = 0;

    public ComprovanteSkyTaPresenterImpl(ComprovanteSkyTaView view,
                                         SchedulerProvider schedulerProvider,
                                         ExceptionUtils exceptionUtils,
                                         ComprovanteSkyTaManager manager) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
    }

    @Override
    public void obterClientes() {
        Disposable disposable = manager.obterClientes()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(getView()::popularAdapter, Timber::e);

        compositeDisposable.add(disposable);
    }

    @Override
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public void salvar() {
        if (cliente == null) {
            getView().mostrarErroCliente();
            return;
        }

        if (tipo == -1) {
            getView().mostrarErroTipo();
            return;
        }
        if(tipo == TypeVoucherEnum.MERCHAN_CLARO.value){
            if(idCampanha == -1){
                getView().mostrarErroCampanha();
                return;
            }

            if (tipoMaterial == -1)
            {
                getView().mostrarErroMaterial();
                return;
            }
        }else{
            if (image == null) {
                getView().mostrarErroImagem();
                return;
            }
        }

        Colaborador colaborador = manager.obterColaborador();
        ComprovanteSkyTa comprovanteSkyTa = new ComprovanteSkyTa();
        comprovanteSkyTa.setIdCliente(Long.parseLong(cliente.getId()));
        comprovanteSkyTa.setAnexo(image);
        comprovanteSkyTa.setIdColaborador(colaborador.getId());
        comprovanteSkyTa.setTipo(tipo);
        comprovanteSkyTa.setSync(ComprovanteSkyTa.NAO_SINCRONIZADO);
        comprovanteSkyTa.setIdCampanha(idCampanha);
        comprovanteSkyTa.setIdMaterial(tipoMaterial);
        comprovanteSkyTa.setInterno(interno);

        manager.salvar(comprovanteSkyTa);

        manager.sincronizar(comprovanteSkyTa);
        getView().salvoSucesso();
    }

    @Override
    public boolean hasSelectedImage() {
        return this.image != null;
    }

    @Override
    public void setCampanha(int idCampanha) {
        this.idCampanha = idCampanha;
    }

    @Override
    public int getIdCampanha() {
        return this.idCampanha;
    }

    @Override
    public void setTipoMaterial(int tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    @Override
    public void setInterno(int interno) {
        this.interno = interno;
    }
}
