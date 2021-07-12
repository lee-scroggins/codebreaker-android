package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.codebreaker.model.entity.CompletedGame;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface CompletedGameDao {
//C of "CRUD"
  @Insert
  Single<Long> insert(CompletedGame game); // Single game

  @Insert
  Single<List<Long>> insert(CompletedGame... games); // zero or more instances var arg or array

  @Insert
  Single<List<Long>> insert(Collection<CompletedGame> games); // zero or more in a collection.  list and sets are collections.

  //U of "CRUD"  (update)

  @Update
  Single<Integer> update(CompletedGame game);  // parrallel of C

  @Update
  Single<Integer> update(CompletedGame... games);  // an array of game objects or zero or more as multiple arguments.  Int return is number of records updated.

  @Update
  Single<Integer> update(Collection<CompletedGame> games); // parallel

  //Delete

  @Delete
  Single<Integer> delete (CompletedGame game);

  @Delete
  Single<Integer> delete (CompletedGame... games);

  @Delete
  Single<Integer> delete (Collection<CompletedGame> games);

  //create read update delete

  //Read , R in "CRUD"

  @Query("SELECT * FROM completed_game WHERE completed_game_id = :id")
  LiveData<CompletedGame> select(long id);   //single game based on id

  @Query("SELECT * FROM completed_game WHERE code_length = :codeLength AND pool_size = :poolSize ORDER BY attempts ASC")
  LiveData<List<CompletedGame>> selectByAttempt(int codeLength, int poolSize);

  @Query("SELECT * FROM completed_game WHERE code_length = :codeLength AND pool_size = :poolSize ORDER BY completed ASC")
  LiveData<List<CompletedGame>> selectByCompleted(int codeLength, int poolSize);


}
