package md.starlab.apartmentsevidenceapp.history.apartment.remote;

import java.util.List;

import md.starlab.apartmentsevidenceapp.history.apartment.model.ApartmentTransactionAnswers;
import md.starlab.apartmentsevidenceapp.history.apartment.model.BuildingAssignedTransaction;
import md.starlab.apartmentsevidenceapp.history.apartment.model.TransactionAnswerModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApartmentHistory {

    @GET("v1/mobile/buildings/{id}/transactions")
    Call<List<BuildingAssignedTransaction>> getTransaction(@Path("id") String id, @Header("Authorization") String token);

    @GET("v1/apartments/{id}/history")
    Call<List<ApartmentTransactionAnswers>> getHistory(@Path("id") String id, @Header("Authorization") String token);

    @POST("v1/mobile/transactions")
    Call<TransactionAnswerModel> postAnswer(@Body TransactionAnswerModel answerModel, @Header("Authorization") String token);
}
