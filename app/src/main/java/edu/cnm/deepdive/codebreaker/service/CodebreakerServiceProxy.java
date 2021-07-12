package edu.cnm.deepdive.codebreaker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.codebreaker.BuildConfig;
import edu.cnm.deepdive.codebreaker.model.dto.Game;
import edu.cnm.deepdive.codebreaker.model.dto.Guess;
import io.reactivex.Single;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CodebreakerServiceProxy {

  @POST("codes")
  Single<Game> startGame(@Body Game game);

  @GET("codes/{id}")
  Single<Game> getGame(@Path("id") String id);

  @POST("codes/{id}/guesses")
  Single<Guess> submitGuess(@Path("id") String id, @Body Guess guess);

  @GET("codes/{id}/guesses")
  Single<List<Guess>> getGuesses(@Path("id") String id);

  static CodebreakerServiceProxy getInstance() {
    return InstanceHolder.INSTANCE;
  }

  static Gson getGsonInstance() {
    return InstanceHolder.GSON;
  }

  class InstanceHolder {

    private static final Gson GSON;
    private static final CodebreakerServiceProxy INSTANCE;

    static {
      // Creation of instances of Gson, OkHttpClient, Retrofit all employ the "builder pattern", as
      // supported by those libraries.
      GSON = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl("https://ddc-java.services/codebreaker/")
          .addConverterFactory(GsonConverterFactory.create(GSON))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(client)
          .build();
      INSTANCE = retrofit.create(CodebreakerServiceProxy.class);
    }

  }

}
