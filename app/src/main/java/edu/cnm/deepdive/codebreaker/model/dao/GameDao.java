package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.pojo.GameWithGuesses;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface GameDao {

  @Insert
  Single<Long> insert(Game game);

  @Insert
  Single<List<Long>> insert(Game... games);

  @Insert
  Single<List<Long>> insert(Collection<? extends Game> games);

  @Update
  Single<Integer> update(Game game);

  @Update
  Single<Integer> update(Game... games);

  @Update
  Single<Integer> update(Collection<? extends Game> games);

  @Delete
  Single<Integer> delete(Game game);

  @Delete
  Single<Integer> delete(Game... games);

  @Delete
  Single<Integer> delete(Collection<? extends Game> games);

  @Transaction
  @Query("SELECT * FROM game WHERE game_id = :gameId")
  LiveData<GameWithGuesses> select(long gameId);

  @Transaction
  @Query("SELECT g.* FROM game AS g "
      + "INNER JOIN (SELECT game_id, COUNT(*) AS guess_count FROM guess GROUP BY game_id) AS s "
      + "ON g.game_id = s.game_id "
      + "WHERE g.length = :length AND g.pool_size = :poolSize and g.solved "
      + "ORDER BY s.guess_count ASC")
  LiveData<List<GameWithGuesses>> selectTopScores(int length, int poolSize);

}
