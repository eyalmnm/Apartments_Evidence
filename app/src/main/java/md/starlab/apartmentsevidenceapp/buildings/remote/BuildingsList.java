package md.starlab.apartmentsevidenceapp.buildings.remote;

import java.util.List;

import md.starlab.apartmentsevidenceapp.buildings.model.BuildingsData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface BuildingsList {

    @GET("v1/mobile/streets/{id}/buildings")
    Call<List<BuildingsData>> getBuildingByStreetID(@Path("id") String StreetId, @Header("Authorization") String token);
}
