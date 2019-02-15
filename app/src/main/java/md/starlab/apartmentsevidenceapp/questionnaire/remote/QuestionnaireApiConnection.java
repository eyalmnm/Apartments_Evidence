package md.starlab.apartmentsevidenceapp.questionnaire.remote;

import java.util.List;

import md.starlab.apartmentsevidenceapp.questionnaire.model.QuestionModel;
import md.starlab.apartmentsevidenceapp.questionnaire.model.TransactionAnswerModel;
import md.starlab.apartmentsevidenceapp.questionnaire.model.TransactionAnswerModelRequest;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

// Ref: https://stackoverflow.com/questions/37049346/upload-file-in-retrofit-2

public interface QuestionnaireApiConnection {

    @GET("v1/transactions/{id}/questions")
    Call<List<QuestionModel>> doGetQuestionnaire(@Path("id") String transactionId,
                                                 @Header("Authorization") String token);


    @POST("v1/mobile/transactions")
    Call<TransactionAnswerModel> postAnswer(@Body TransactionAnswerModelRequest answerModel,
                                            @Header("Authorization") String token);

    @Multipart
    @POST("v1/mobile/transactions/{id}/attachments")
    Call<ResponseBody> postAttachment(
            @Path("id") String transactionId,
            @Header("Authorization") String token,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file);
}
