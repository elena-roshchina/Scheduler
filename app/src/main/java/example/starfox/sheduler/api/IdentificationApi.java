package example.starfox.sheduler.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface IdentificationApi {
    @GET("/auth.php")
    Call<IdentificationModel> getData(@Query("login") String userLogin, @Query("pswd") String userPassword);
}
