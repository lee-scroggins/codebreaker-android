package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.SimpleGuessAdapter.Holder;
import edu.cnm.deepdive.codebreaker.databinding.ItemGuessBinding;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import java.util.List;

public class SimpleGuessAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final List<Guess> guesses;
  private final LayoutInflater inflater;
  private final String matchResultsFormat;
  @ColorInt private final int solution;

  public SimpleGuessAdapter(Context context, List<Guess> guesses) {
    this.context = context;
    this.guesses = guesses;
    inflater = LayoutInflater.from(context);
    matchResultsFormat = context.getString(R.string.match_results_format);
    solution = ContextCompat.getColor(context, R.color.solution);
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemGuessBinding binding = ItemGuessBinding.inflate(inflater, parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return guesses.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemGuessBinding binding;

    private Holder(@NonNull ItemGuessBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
      // TODO Perform any additional setup.
    }

    private void bind(int position) {
      Guess guess = guesses.get(position);
      binding.text.setText(guess.getText());
      binding.matchResults.setText(
          String.format(matchResultsFormat, guess.getExactMatches(), guess.getNearMatches()));
      binding.getRoot().setBackgroundColor(guess.isSolution() ? solution : Color.TRANSPARENT);
    }

  }

}
