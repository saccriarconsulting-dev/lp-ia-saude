package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.enums.adquirencia.EnumMapPeriod;
import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Rogério Massa on 26/11/18.
 */

public interface MapItemManager {

    Single<List<Ranking>> getMapItems(EnumMapPeriod periodEnum);
}
