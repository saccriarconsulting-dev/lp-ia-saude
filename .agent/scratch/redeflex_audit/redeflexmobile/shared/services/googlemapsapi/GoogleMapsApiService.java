package com.axys.redeflexmobile.shared.services.googlemapsapi;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Rogério Massa on 31/01/19.
 */

public interface GoogleMapsApiService {

    @GET("directions/json")
    Single<ResponseBody> getRoutes(@Query("origin") String origin,
                                   @Query("destination") String destination,
                                   @Query("mode") String mode,
                                   @Query("language") String language,
                                   @Query("alternatives") boolean alternatives,
                                   @Query("client") String client,
                                   @Query("signature") String signature);
}
