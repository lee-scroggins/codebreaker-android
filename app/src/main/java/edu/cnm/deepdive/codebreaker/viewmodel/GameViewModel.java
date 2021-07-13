package edu.cnm.deepdive.codebreaker.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.pojo.GameWithGuesses;
import edu.cnm.deepdive.codebreaker.service.GameRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.stream.Collectors;

public class GameViewModel extends AndroidViewModel implements LifecycleObserver {

  private final GameRepository repository;
  private final MutableLiveData<Long> gameId;
  private final LiveData<GameWithGuesses> game;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final SharedPreferences preferences;
  private final String basePool;

  public GameViewModel(@NonNull Application application) {
    super(application);
    repository = new GameRepository(application);
    gameId = new MutableLiveData<>();
    game = Transformations.switchMap(gameId, repository::get);     //witch map monitors this gameId
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    preferences = PreferenceManager.getDefaultSharedPreferences(application);
    String[] emojis = application.getResources().getStringArray(R.array.emojis);
    StringBuilder builder = new StringBuilder();
    for (String emoji : emojis) {
      builder.append(emoji);
    }
    basePool = builder.toString();
    startGame();
  }

  public LiveData<GameWithGuesses> getGame() {
    return game;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void startGame() {
    throwable.setValue(null);
    Game game = new Game();
    game.setPool(getPoolPref());
    game.setLength(getCodeLengthPref());
    pending.add(
        repository
            .save(game)
            .subscribe(
                (updatedGame) -> gameId.postValue(updatedGame.getId()),
                this::handleThrowable
            )
    );
  }

  public void submitGuess(Game game, String text) {
    throwable.setValue(null);
    Guess guess = new Guess();
    guess.setText(text);
    pending.add(
        repository
            .save(game, guess)
            .subscribe(
                (ignored) -> {
                },
                this::handleThrowable
            )
    );
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

  private void handleThrowable(Throwable throwable) {
    Log.e(getClass().getName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  private int getCodeLengthPref() {
    Context context = getApplication();
    Resources res = context.getResources();
    return preferences.getInt(res.getString(R.string.code_length_pref_key),
        res.getInteger(R.integer.code_length_pref_default));
  }

  private String getPoolPref() {
    Resources res = getApplication().getResources();
    int poolSizePref = preferences.getInt(res.getString(R.string.pool_size_pref_key),
        res.getInteger(R.integer.pool_size_pref_default));
    return basePool
        .codePoints()
        .limit(poolSizePref)
        .mapToObj((codePoint) -> new String(new int[]{codePoint}, 0, 1))
        .collect(Collectors.joining());
  }

}
