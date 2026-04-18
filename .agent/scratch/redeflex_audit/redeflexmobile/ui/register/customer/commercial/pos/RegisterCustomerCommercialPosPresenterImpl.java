package com.axys.redeflexmobile.ui.register.customer.commercial.pos;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.NEGATIVE_SINGLE_INT;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.Operadora;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.shared.models.ModeloPOSConexao;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.MachineType;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.NumberUtils;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * @author Rogério Massa on 18/02/19.
 */

public class RegisterCustomerCommercialPosPresenterImpl extends BasePresenterImpl<RegisterCustomerCommercialPosView>
        implements RegisterCustomerCommercialPosPresenter {

    private CustomerRegisterManager customerRegisterManager;
    private List<ModeloPOS> posList;
    private List<Operadora> carriers;
    private int positionInList;

    public RegisterCustomerCommercialPosPresenterImpl(final RegisterCustomerCommercialPosView view,
                                                      final SchedulerProvider schedulerProvider,
                                                      final ExceptionUtils exceptionUtils,
                                                      final CustomerRegisterManager customerRegisterManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.customerRegisterManager = customerRegisterManager;
    }

    @Override
    public void setPositionInList(Integer positionInList) {
        if (positionInList == null) positionInList = NEGATIVE_SINGLE_INT;
        this.positionInList = positionInList;
    }

    @Override
    public void getPosById(final int id) {
        final ModeloPOS pos = getPosByIdFromList(id);
        getView().hideCableLengthField();
        getView().hideCarriersField();
        getView().setPosFields(pos);
    }

    @Override
    public void getPosConnection(ModeloPOSConexao modeloPOSConexao) {
        if (areCarriesVisible(modeloPOSConexao)) {
            getView().showCarriersField(new ArrayList<>(carriers));
        } else {
            getView().hideCarriersField();
        }

        if (isCableLengthVisible(modeloPOSConexao)) {
            getView().showCableLengthField();
        } else {
            getView().hideCableLengthField();
        }
    }

    @Override
    public void initializeData() {
        compositeDisposable.add(Single.zip(
                customerRegisterManager.getPOSModels(),
                customerRegisterManager.getCarriers(),
                (posList, carriers) -> {
                    this.posList = posList;
                    this.carriers = carriers;
                    return posList;
                })
                .subscribeOn(schedulerProvider.ui())
                .observeOn(schedulerProvider.ui())
                .subscribe(posList -> getView().fillPosModel(new ArrayList<>(posList)),
                        this::showError));
    }

    @Override
    public void savePos(final MachineType machineType) {
        if (machineType.getModelId() == null
                || machineType.getIdConnectionType() == null
                || NumberUtils.isEmptyDouble(machineType.getSelectedRentValue())) {
            showError(getView().getStringByResId(R.string.customer_register_commercial_pos_error));
            return;
        }

        final ModeloPOS modelPOS = getPosByIdFromList(machineType.getModelId());
        final MachineType machineTypeToSave = new MachineType(modelPOS);
        final ModeloPOSConexao modeloPOSConexao = getModeloConexao(modelPOS, machineType.getIdConnectionType());
        machineTypeToSave.setIdConnectionType(machineType.getIdConnectionType());
        machineTypeToSave.setSelectedRentValue(machineType.getSelectedRentValue());

        if (areCarriesVisible(modeloPOSConexao)) {
            machineTypeToSave.setCarrierId(machineType.getCarrierId());
        } else {
            machineTypeToSave.setCarrierId(null);
        }

        final boolean isCableLengthValid = isCableLengthVisible(modeloPOSConexao)
                && machineType.getCableLength() != null
                && machineType.getCableLength() != EMPTY_INT;

        if (isCableLengthValid) {
            machineTypeToSave.setCableLength(machineType.getCableLength());
        } else {
            machineTypeToSave.setCableLength(null);
        }

        final RegisterCustomerView parentActivity = getView().getParentActivity();
        final CustomerRegister customerRegister = parentActivity.getCustomerRegisterCommercialCache();

        if (customerRegister.getPosList() == null) {
            customerRegister.setPosList(new ArrayList<>());
        }

        if (positionInList != NEGATIVE_SINGLE_INT) {
            customerRegister.getPosList().set(positionInList, machineTypeToSave);
        } else {
            customerRegister.getPosList().add(machineTypeToSave);
        }

        parentActivity.setCustomerRegisterCommercialCache(customerRegister);
        getView().onBackPressed();
    }

    private ModeloPOS getPosByIdFromList(final int id) {
        return Stream.ofNullable(posList)
                .filter(value -> value.getIdAppMobile().equals(id))
                .findFirst()
                .orElse(null);
    }

    private ModeloPOSConexao getModeloConexao(ModeloPOS modeloPOS, final int id) {
        return Stream.of(modeloPOS.getConnections())
                .filter(value -> value.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private boolean areCarriesVisible(final ModeloPOSConexao pos) {
        return pos.isCarrier();
    }

    private boolean isCableLengthVisible(final ModeloPOSConexao pos) {
        return pos.isCableLength();
    }
}
