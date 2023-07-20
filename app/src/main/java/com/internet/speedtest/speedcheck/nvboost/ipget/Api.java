package com.internet.speedtest.speedcheck.nvboost.ipget;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/* loaded from: classes2.dex */
public interface Api {
    public static final String BASE_URL = "http://ip-api.com/";

    @GET
    Call<Hero> getHeroes(@Url String str);

    @GET("json/{ip}")
    Call<IPINFO> getIPINFO(@Path("ip") String ip);
}
