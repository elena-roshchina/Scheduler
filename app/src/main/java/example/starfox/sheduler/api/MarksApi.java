package example.starfox.sheduler.api;


import example.starfox.sheduler.MarksModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MarksApi {
    @GET("/get/marks.php")
    Call<MarksModel> getData(@Query("session") String session);
}
