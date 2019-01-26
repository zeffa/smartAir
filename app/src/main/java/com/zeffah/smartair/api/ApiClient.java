package com.zeffah.smartair.api;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.lufthansa.com/v1/";
//    private static final String BASE_URL = "http://192.168.43.14:5000/api/";
    private static Retrofit retrofit = null;
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK);
            request = builder.build();
            return chain.proceed(request);
        }
    };
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }
}
