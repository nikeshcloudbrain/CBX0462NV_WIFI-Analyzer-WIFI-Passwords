package com.internet.speedtest.speedcheck.nvboost.ipget;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    public static String BASE_URL;
    private static RetroClient apiClient;
    private Retrofit retrofit;

    public RetroClient() {

        BASE_URL = Api.BASE_URL;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        chain -> {
                            Request original = chain.request();

                            Request.Builder requestBuilder = original.newBuilder()
                                    .method(original.method(), original.body());

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                ).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    public static synchronized RetroClient getInstance() {
        if (apiClient == null) {
            apiClient = new RetroClient();
        }
        return apiClient;
    }

    public Api getApi2() {
        return retrofit.create(Api.class);
    }

}
