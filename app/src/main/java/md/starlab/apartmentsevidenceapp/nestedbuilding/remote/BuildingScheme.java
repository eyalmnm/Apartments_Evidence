package md.starlab.apartmentsevidenceapp.nestedbuilding.remote;

import md.starlab.apartmentsevidenceapp.nestedbuilding.model.MobileApartmentModel;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NestedBuilding;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewEntranceRequest;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewEntranceRequestNoOrder;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewEntranceResponse;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewFloorRequest;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewFloorResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BuildingScheme {

    @GET("v1/mobile/buildings/{id}")
    Call<NestedBuilding> getCitiesAndStreets(@Path("id") String id, @Header("Authorization") String token);

    @POST("v1/mobile/floors")
    Call<NewFloorResponse> addNewFloor(@Body NewFloorRequest request, @Header("Authorization") String token);

    @POST("v1/mobile/entrances")
    Call<NewEntranceResponse> addNewEntranceNoOrder(@Body NewEntranceRequestNoOrder request, @Header("Authorization") String token);

    @POST("v1/mobile/entrances")
    Call<NewEntranceResponse> addNewEntrance(@Body NewEntranceRequest request, @Header("Authorization") String token);

    @POST("v1/mobile/apartments")
    Call<MobileApartmentModel> addApartment(@Body MobileApartmentModel request, @Header("Authorization") String token);

    @PUT("v1/mobile/apartments/{id}")
    Call<MobileApartmentModel> moveApartment(@Path("id") String id, @Body MobileApartmentModel request, @Header("Authorization") String token);

    @DELETE("v1/mobile/apartments/{id}")
    Call<String> removeApartment(@Path("id") String id, @Header("Authorization") String token);

    @DELETE("v1/mobile/floors/{id}")
    Call<String> removeFloor(@Path("id") String id, @Header("Authorization") String token);

    @DELETE("v1/mobile/entrances/{id}")
    Call<String> removeEntrance(@Path("id") String id, @Header("Authorization") String token);
}