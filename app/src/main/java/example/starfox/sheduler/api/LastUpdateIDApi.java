package example.starfox.sheduler.api;


import example.starfox.sheduler.LastUpdateIDModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface LastUpdateIDApi {
    @GET("/get/last.php")
    Call<LastUpdateIDModel> getData(@Query("session") String session);
}
