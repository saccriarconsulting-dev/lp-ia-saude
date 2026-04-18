package com.axys.redeflexmobile.shared.util;

import io.reactivex.Single;

public interface CheckForInternetProvider {
    Single<Boolean> checkForInternet();
}
