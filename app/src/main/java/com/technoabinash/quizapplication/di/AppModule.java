package com.technoabinash.quizapplication.di;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.technoabinash.quizapplication.application.MainApplication;
import com.technoabinash.quizapplication.network.RetrofitService;
import com.technoabinash.quizapplication.repository.QuizRepo;
import com.technoabinash.quizapplication.room.dao.QuizDao;
import com.technoabinash.quizapplication.room.db.AppDatabase;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import timber.log.Timber;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    private final String HTTP_DIR_CACHE = "quiz_cache";
    private final Long CACHE_SIZE = 10 * 1024 * 1024L;

    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";
    int maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
//    private final String HEADER_TYPE = "Authorization";
    private final String API_BASE_URL = "https://opentdb.com/";


    @Singleton
    @Provides
    public RetrofitService provideRetrofitService(Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }

    @Singleton
    @Provides
    public QuizRepo provideRepo(RetrofitService service, @ApplicationContext Context context,QuizDao dao) {
        return new QuizRepo(service, context, dao);
    }
//    @Singleton
//    @Provides
//    public RoomRepository provideRoomRepo(QuizDao service) {
//        return new RoomRepository(service);
//    }


    @Singleton
    @Provides
    public Cache provideCache(@ApplicationContext Context context) {
        return new Cache(new File(context.getCacheDir(), HTTP_DIR_CACHE), CACHE_SIZE);
    }


    @Singleton
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Singleton
    @Provides
    public static Interceptor offlineInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.d("AppModule", "offline interceptor: called.");
                Request request = chain.request();

                // prevent caching when network is on. For that we use the "networkInterceptor"
                if (!MainApplication.hasNetwork()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .removeHeader(HEADER_PRAGMA)
                            .removeHeader(HEADER_CACHE_CONTROL)
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }



    @Singleton
    @Provides
    public static Interceptor networkInterceptor() {
        return chain -> {
            Log.d("AppModule", "network interceptor: called.");

            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(5, TimeUnit.SECONDS)
                    .build();

            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();
        };
    }


    @Singleton
    @Provides
    public OkHttpClient provideHttpClient(
            @ApplicationContext Context context, HttpLoggingInterceptor httpLoggingInterceptor, Cache cache) {

        return new OkHttpClient.Builder().addInterceptor(chain -> {
                    Request original = chain.request();

                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(30, TimeUnit.DAYS)
                            .build();


                    Request.Builder builder = original.newBuilder()
                            .method(original.method(), original.body());

                    Request request;

                    String isCacheAvailable = original.header("isCacheAvailable");
                    if (isCacheAvailable != null) {
                        if (isCacheAvailable.equals("yes".trim())) {
                            Timber.v("AppModule isCacheAvailable " + isCacheAvailable);
                            request = builder.cacheControl(cacheControl).build();
                        } else {
                            request = builder.build();
                        }
                    } else {
                        request = builder.build();
                    }

                    return chain.proceed(request);

                })
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor).readTimeout(120, TimeUnit.SECONDS)
                .addNetworkInterceptor(networkInterceptor())
                .addInterceptor(offlineInterceptor())
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();



    }


    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

    @Singleton
    @Provides
    public GsonConverterFactory provideGSonConverterFactory(Gson gSon) {
        return GsonConverterFactory.create(gSon);
    }


    @Singleton
    @Provides
    public QuizDao provideRoomDao(AppDatabase database) {
        return database.quizDao();
    }



    @Singleton
    @Provides
    public AppDatabase provideRoomDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                "QuizRepo"
        ).allowMainThreadQueries().build();
    }
    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient, GsonConverterFactory gSonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(gSonConverterFactory).client(okHttpClient).build();

    }


}
