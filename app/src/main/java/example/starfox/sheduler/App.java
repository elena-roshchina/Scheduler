package example.starfox.sheduler;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import example.starfox.sheduler.api.IdentificationApi;
import example.starfox.sheduler.api.SheduleApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static IdentificationApi identificationApi;
    private static SheduleApi sheduleApi;
    private static final String BASE_URL = "http://starfox.spb.ru";

    //
    private OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS);
    //

    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        identificationApi = retrofit.create(IdentificationApi.class);
        sheduleApi = retrofit.create(SheduleApi.class);
    }

    public static IdentificationApi getAuthApi() {
        return identificationApi;
    }
    public static SheduleApi getScheduleApi() {return sheduleApi; }
}
