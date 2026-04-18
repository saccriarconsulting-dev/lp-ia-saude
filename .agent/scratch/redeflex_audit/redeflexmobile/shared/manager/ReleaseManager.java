package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.adquirencia.Release;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Rogério Massa on 04/12/18.
 */

public interface ReleaseManager {

    Single<List<Release>> getReleases(int clientId);
}
