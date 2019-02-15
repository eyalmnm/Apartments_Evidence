package md.starlab.apartmentsevidenceapp.cities_streets.remote;

import java.util.List;

import md.starlab.apartmentsevidenceapp.cities_streets.model.CityData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CitiesAndStreets {

    /**
     * Used for retrieve a list of cities associated with streets.
     *
     * @param token Must be in form of "Bearer <token>"
     * @return a call object to perform the request
     */
    @GET("v1/mobile/cities")
    Call<List<CityData>> getCitiesAndStreets(@Header("Authorization") String token);
}
