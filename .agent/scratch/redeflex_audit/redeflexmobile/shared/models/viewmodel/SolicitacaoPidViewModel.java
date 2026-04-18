package com.axys.redeflexmobile.shared.models.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axys.redeflexmobile.shared.models.SolicitacaoPid;

public class SolicitacaoPidViewModel extends ViewModel {
    private final MutableLiveData<SolicitacaoPid> solicitacaopidLiveData = new MutableLiveData<>();

    public LiveData<SolicitacaoPid> getSolicitacaoPid() {
        return solicitacaopidLiveData;
    }

    public void setSolicitacaoPid(SolicitacaoPid solicitacaoPid) {
        solicitacaopidLiveData.setValue(solicitacaoPid);
    }
}
