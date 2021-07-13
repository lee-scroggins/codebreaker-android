package edu.cnm.deepdive.codebreaker.service;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.codebreaker.model.dao.GameDao;
import edu.cnm.deepdive.codebreaker.model.dao.GuessDao;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.service.CodebreakerDatabase.Converters;
import java.util.Date;

@Database(/*needs items*/
    entities = {Game.class, Guess.class}, /*1. completed game entity*/
    version = 2, /*2. version of game. Update each change*/
    exportSchema = true /*3. creates file with JSON description.  Includes SQL code Room uses to generate.  Generate location in build.gradle.*/
    )

@TypeConverters({Converters.class})
public abstract class CodebreakerDatabase extends RoomDatabase {

  private static final String DB_NAME = "codebreaker-db";

  private static Application context;

  public static void setContext(Application context) {
    CodebreakerDatabase.context = context;
  }

  public static CodebreakerDatabase getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public abstract GameDao getGameDao();

  public abstract GuessDao getGuessDao();

  private static class InstanceHolder {

    private static final CodebreakerDatabase INSTANCE =
        Room.databaseBuilder(context, CodebreakerDatabase.class, DB_NAME) //first thing passed to builder is the context, next the class, followed by name of file
            .build();

  }
  /*Date Conversion Java to SQL*/
  public static class Converters {
    @TypeConverter
    public static Long dateToLong(Date value) {/*Date to Long*/
      return (value != null) ? value.getTime() : null;
    }

    @TypeConverter
    public static Date longToDate(Long value) {
      return (value != null) ? new Date(value) : null;
    }

  }

}
