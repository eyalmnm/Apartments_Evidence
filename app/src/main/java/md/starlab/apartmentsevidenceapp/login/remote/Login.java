package md.starlab.apartmentsevidenceapp.login.remote;

import md.starlab.apartmentsevidenceapp.login.model.LoginCredentials;
import md.starlab.apartmentsevidenceapp.login.model.LoginServerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Login {

    @POST("v1/users/login")
    Call<LoginServerResponse> loginWithCredentials(@Body LoginCredentials data);

    @GET("v1/users/logout")
    Call<Void> logout(@Header("Authorization") String token);
}
