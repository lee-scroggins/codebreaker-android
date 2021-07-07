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
import androidx.preference.PreferenceManager;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.service.GameRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class GameViewModel extends AndroidViewModel implements LifecycleObserver {

  private final GameRepository repository;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final SharedPreferences preferences;

  public GameViewModel(@NonNull Application application) {
    super(application);
    repository = new GameRepository(application);
    game = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    preferences = PreferenceManager.getDefaultSharedPreferences(application);
    startGame();
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void startGame() {
    throwable.setValue(null);
    pending.add(
        repository
            .create("ABCDEF", getCodeLengthPref())
        .subscribe(
            game::postValue,
            this::handleThrowable
        )
    );
  }

  public void submitGuess(String text) {
    throwable.setValue(null);
    //noinspection ConstantConditions
    pending.add(
        repository
            .addGuess(game.getValue(), text)
            .subscribe(
                (game) -> this.game.postValue(game),
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

}
