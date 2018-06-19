package example.starfox.sheduler.api;


import example.starfox.sheduler.MessagesModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MessagesApi {
    @GET("/get/msg.php")
    Call<MessagesModel> getData(@Query("session") String session);
}
