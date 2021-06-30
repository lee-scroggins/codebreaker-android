package edu.cnm.deepdive.codebreaker.service;

import android.content.Context;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class GameRepository {

  private final CodebreakerServiceProxy proxy;
  private final Context context;

  public GameRepository(Context context) {
    this.context = context;
    proxy = CodebreakerServiceProxy.getInstance();
  }

  public Single<Game> create(String pool, int length){
    Game gameStub = new Game();
    gameStub.setPool(pool);
    gameStub.setLength(length);
    return proxy.startGame(gameStub)
        .subscribeOn(Schedulers.io());
  }

  public Single<Game> get(String id) {
    return proxy.getGame(id)
        .flatMap((game) -> proxy.getGuesses(game.getId())
            .map((guesses) -> {
              game.getGuesses().addAll(guesses);
              return game;
            }))
        .subscribeOn(Schedulers.io());
  }

  public Single<Guess> addGuess(Game game, String text) {
    Guess guess = new Guess();
    guess.setText(text);
    return proxy.submitGuess(game.getId(), guess)
        .subscribeOn(Schedulers.io());
  }

  public static class ValidationException extends IllegalArgumentException {

    private final Error error;

    public ValidationException(Error error) {
      this.error = error;
    }

    public Error getError() {
      return error;
    }

  }

}


