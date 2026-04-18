package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.Iccid;

public interface IccidDao {

    Iccid getByCodigo(String pCodigo);
}
