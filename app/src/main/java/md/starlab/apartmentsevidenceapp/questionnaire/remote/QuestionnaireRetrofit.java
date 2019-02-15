package md.starlab.apartmentsevidenceapp.questionnaire.remote;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import md.starlab.apartmentsevidenceapp.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class QuestionnaireRetrofit {

    private static final String TAG = "QuestionnaireRetrofit";

    static HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.BODY;

    private static final String BASE_URL = BuildConfig.SERVER_URL;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(logLevel);
    static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Log.d(TAG, "BASE_URL: " + BASE_URL);
        Retrofit retrofit = builder.client(httpClient.build()).build();
        Log.d(TAG, "retrofit created: " + retrofit.toString());
        return retrofit.create(serviceClass);
    }
}
