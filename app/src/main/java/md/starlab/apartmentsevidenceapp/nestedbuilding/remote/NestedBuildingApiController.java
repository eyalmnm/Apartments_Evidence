package md.starlab.apartmentsevidenceapp.nestedbuilding.remote;

import android.util.Log;

import md.starlab.apartmentsevidenceapp.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NestedBuildingApiController {

    private static final String TAG = "NestedBuildingApiCtrlr";

    private static final String BASE_URL = BuildConfig.SERVER_URL;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Log.d(TAG, "BASE_URL: " + BASE_URL);
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
