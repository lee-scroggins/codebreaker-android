package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ItemScoreboardBinding;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.pojo.GameWithGuesses;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ScoreboardAdapter extends ArrayAdapter<GameWithGuesses> {

  private final DateFormat dateFormat;
  private final DateFormat timeFormat;
  private final NumberFormat numberFormat;
  private final String dateTimeCombinationFormat;

  public ScoreboardAdapter(@NotNull Context context, @NotNull List<GameWithGuesses> games) {
    super(context, R.layout.item_scoreboard, games);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    timeFormat = android.text.format.DateFormat.getTimeFormat(context);
    numberFormat = NumberFormat.getIntegerInstance();
    dateTimeCombinationFormat = context.getString(R.string.date_time_combination_format);


  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ItemScoreboardBinding binding = (convertView != null)
        ? ItemScoreboardBinding.bind(convertView)
        : ItemScoreboardBinding.inflate(LayoutInflater.from(getContext()), parent, false);
    GameWithGuesses game = getItem(position);
    List<Guess> guesses = game.getGuesses();
    int numGuesses = guesses.size();
    Date completed = guesses.get(numGuesses - 1).getCreated();
    binding.attempts.setText(numberFormat.format(numGuesses));
    binding.dateCompleted.setText(String.format(dateTimeCombinationFormat,
        dateFormat.format(completed), timeFormat.format(completed)));
    return binding.getRoot();
  }
}
