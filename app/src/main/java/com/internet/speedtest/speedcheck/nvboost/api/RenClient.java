package com.internet.speedtest.speedcheck.nvboost.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.internet.speedtest.speedcheck.nvboost.utils.Constant;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RenClient {

    public static String BASE_URL = Constant.getMainApi();

    private static RenClient retrofitCLient;

    private Retrofit retroMain;
    private Retrofit retroApps;

    public RenClient() {


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


        retroMain = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();


    }

    public static synchronized RenClient getInstance() {
        if (retrofitCLient == null || BASE_URL == null ) {
            retrofitCLient = new RenClient();
        }
        return retrofitCLient;
    }

    public ApiInter getApi() {
        return retroMain.create(ApiInter.class);
    }

}
