package edu.cnm.deepdive.codebreaker.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.codebreaker.model.dao.GameDao;
import edu.cnm.deepdive.codebreaker.model.dao.GuessDao;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.pojo.GameWithGuesses;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class GameRepository {

  private final CodebreakerServiceProxy proxy;
  private final Context context;
  private final GameDao gameDao;
  private final GuessDao guessDao;

  public GameRepository(Context context) {
    this.context = context;
    proxy = CodebreakerServiceProxy.getInstance();
    CodebreakerDatabase database = CodebreakerDatabase.getInstance();
    gameDao = database.getGameDao();
    guessDao = database.getGuessDao();
  }
 // Save Game
  public Single<Game> save(Game game) {
    return (
        (game.getId() == 0)   /*if game id is 0, must send to server to create database record*/
            ? proxy
            .startGame(game)  /*write game object to database*/
            .flatMap((receivedGame) -> {
              receivedGame.setPoolSize((int)receivedGame.getPool().codePoints().count());
              return gameDao
                  .insert(receivedGame)
                  .map((id) -> {
                    receivedGame.setId(id);
                    return receivedGame; /*returns single of long.  Replaces it with single game*/
                  });
            })
            : gameDao.update(
                game)    /* sends back integer.  need to map it to accept a long since gameId is a long*/
                .map(
                    (count) -> game) /*needs to be scheduled on a background thread or else the app will crash if in a foreground thread.*/
    )
        .subscribeOn(Schedulers
            .io()); /*sets scheduler, manages a pool of threads.  For io tasks, io spends most time waiting, scheduler can share threads for io tasks. */

  }

  public LiveData<GameWithGuesses> get(long id) {
    return gameDao.select(id);
  }

  // Saving a guess on an existing game.
  public Single<Game> save(Game game, Guess guess) { /*if guerss id is 0, needs to be stored in database.*/
    return (
        (guess.getId() == 0)
            ? proxy
            .submitGuess(game.getServiceKey(),
                guess) /* first action send to server.  output needs flatmap*/
            .map((recievedGuess) -> {    //received from server
              recievedGuess
                  .setGameId(game.getId());  //needs game id to save to proper database location
              return recievedGuess; //now guess has game id
            })
            .flatMap(guessDao::insert)  //returns updated game
            .map((id) -> game)
            : guessDao
                .update(guess)  //returns integer
                .map((count) -> game) //map back to game

    )
        .subscribeOn(Schedulers.io());
  }

  //Deletes game
  public Completable remove(
      Game game) {    /*if id is 0, not in database, no need to delete for nothing exists*/
    return (
        (game.getId() == 0)
            ? Completable.complete()
            : gameDao
                .delete(game)
                .ignoreElement()
    )
        .subscribeOn(Schedulers.io());

  }
 // Query generally live data
  public LiveData<List<GameWithGuesses>> getScoreboard(int codeLength, int poolSize) {
    return gameDao.selectTopScores(codeLength, poolSize);
  }

}
