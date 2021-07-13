package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface GuessDao {

  @Insert
  Single<Long> insert(Guess guess);

  @Insert
  Single<List<Long>> insert(Guess... guesses);

  @Insert
  Single<List<Long>> insert(Collection<? extends Guess> guesses);

  @Update
  Single<Integer> update(Guess guess);

  @Update
  Single<Integer> update(Guess... guesses);

  @Update
  Single<Integer> update(Collection<? extends Guess> guesses);

  @Delete
  Single<Integer> delete(Guess guess);

  @Delete
  Single<Integer> delete(Guess... guesses);

  @Delete
  Single<Integer> delete(Collection<? extends Guess> guesses);

  @Query("SELECT * FROM guess WHERE guess_id = :guessId")
  LiveData<Guess> select(long guessId);

  @Query("SELECT * FROM guess WHERE game_id = :gameId ORDER BY created ASC")
  LiveData<List<Guess>> selectForGame(long gameId);

}
