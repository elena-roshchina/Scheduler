package example.starfox.sheduler.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import example.starfox.sheduler.IdentificationModel;


public interface IdentificationApi {
    @GET("/auth.php")
    Call<IdentificationModel> getData(@Query("login") String userLogin, @Query("pswd") String userPassword);
}
