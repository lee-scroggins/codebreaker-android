package edu.cnm.deepdive.codebreaker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import edu.cnm.deepdive.codebreaker.service.GameRepository;
import org.jetbrains.annotations.NotNull;

public class GameViewModel extends AndroidViewModel {

  private final GameRepository repository;

    public GameViewModel(@NonNull @NotNull Application application) {
    super(application);
    repository = new GameRepository(application);
  }
}
