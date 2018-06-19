package example.starfox.sheduler;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import example.starfox.sheduler.api.IdentificationApi;
import example.starfox.sheduler.api.LastUpdateIDApi;
import example.starfox.sheduler.api.MarksApi;
import example.starfox.sheduler.api.MessagesApi;
import example.starfox.sheduler.api.SheduleApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static IdentificationApi identificationApi;
    private static SheduleApi sheduleApi;
    private static LastUpdateIDApi lastUpdateIDApi;
    private static MarksApi marksApi;
    private static MessagesApi messagesApi;
    private static final String BASE_URL = "http://starfox.spb.ru";

    //
    private OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS);
    //



    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        identificationApi = retrofit.create(IdentificationApi.class);
        sheduleApi = retrofit.create(SheduleApi.class);
        lastUpdateIDApi = retrofit.create(LastUpdateIDApi.class);
        marksApi = retrofit.create(MarksApi.class);
        messagesApi = retrofit.create(MessagesApi.class);
    }

    public static IdentificationApi getAuthApi() {
        return identificationApi;
    }
    public static SheduleApi getScheduleApi() { return sheduleApi; }
    public static LastUpdateIDApi getLastUpdateIDApi() {return lastUpdateIDApi; }
    public static MarksApi getMarksApi() { return marksApi; }
    public static MessagesApi getMessagesApi() { return messagesApi; }
}
