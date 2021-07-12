package edu.cnm.deepdive.codebreaker;

import android.app.Application;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.codebreaker.service.CodebreakerDatabase;
import io.reactivex.schedulers.Schedulers;

public class CodebreakerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    // TODO Initialize database.
    CodebreakerDatabase.setContext(this);
    CodebreakerDatabase
        .getInstance()
        .getCompletedGameDao()
        .delete()
        .subscribeOn(Schedulers.io())
        .subscribe();
    // TODO Initialize other services, as necessary.
    Stetho.initializeWithDefaults(this);
  }

}
