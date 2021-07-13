package edu.cnm.deepdive.codebreaker.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.SimpleGuessAdapter;
import edu.cnm.deepdive.codebreaker.databinding.FragmentPlayBinding;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.viewmodel.GameViewModel;
import java.util.List;

public class PlayFragment extends Fragment {

  private FragmentPlayBinding binding;
  private GameViewModel viewModel;
  private int codeLength;
  private String pool;
  private Spinner[] spinners;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentPlayBinding.inflate(inflater, container, false);
    binding.submit.setOnClickListener((v) -> {
      // TODO Submit guess (from spinners) to viewmodel.
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < codeLength; i++) {
        String emoji = (String) spinners[i].getSelectedItem();
        builder.append(emoji);
      }
      viewModel.submitGuess(builder.toString());
    });
    spinners = setupSpinners(
        binding.guessContainer, getResources().getInteger(R.integer.code_length_pref_max));
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    viewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
    viewModel.getGame().observe(getViewLifecycleOwner(), this::update);
  }

  private void update(Game game) {
    // TODO Make game display prettier.
    codeLength = game.getLength();
    pool = game.getPool();
    // TODO Update visibility & contents of spinners.
    String[] emojis = getUnicodeArray(pool);
    List<Guess> guesses = game.getGuesses();
    Guess lastGuess = guesses.isEmpty() ? null : guesses.get(guesses.size() - 1);
    for (int spinnerIndex = codeLength; spinnerIndex < spinners.length; spinnerIndex++) {
      spinners[spinnerIndex].setVisibility(View.GONE);
    }
    for (int spinnerIndex = 0; spinnerIndex < codeLength; spinnerIndex++) {
      spinners[spinnerIndex].setVisibility(View.VISIBLE);
      ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
          R.layout.item_emoji, emojis);
      adapter.setDropDownViewResource(R.layout.item_emoji_dropdown);
      spinners[spinnerIndex].setAdapter(adapter);
      if (lastGuess != null) {
        String[] guessEmojis = getUnicodeArray(lastGuess.getText());
            String selection = guessEmojis[spinnerIndex];
        for (int emojiIndex = 0; emojiIndex < emojis.length; emojiIndex++) {
          if (emojis[emojiIndex].equals(selection)) {
            spinners[spinnerIndex].setSelection(emojiIndex);
          }
        }
      }
    }
    if (game.isSolved()) {
      binding.guessContainer.setVisibility(View.INVISIBLE);
      binding.submit.setVisibility(View.INVISIBLE);
    } else {
      binding.guessContainer.setVisibility(View.VISIBLE);
      binding.submit.setVisibility(View.VISIBLE);
    }
    SimpleGuessAdapter adapter = new SimpleGuessAdapter(getContext(), game.getGuesses());
    binding.guessList.setAdapter(adapter);
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.play_options, menu);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    //noinspection SwitchStatementWithTooFewBranches
    switch (item.getItemId()) {
      case R.id.new_game_option:
        viewModel.startGame();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  private void enforceSubmitConditions(int newLength) {
    binding.submit.setEnabled(newLength == codeLength);
  }

  private Spinner[] setupSpinners(ConstraintLayout layout, int numSpinners) {
    Spinner[] spinners = new Spinner[numSpinners];
    for (int i = 0; i < spinners.length; i++) {
      Spinner spinner =
          (Spinner) getLayoutInflater().inflate(R.layout.spinner_emoji, layout, false);
      layout.addView(spinner);
      spinner.setId(View.generateViewId());
      spinners[i] = spinner;
    }
    int layoutId = layout.getId();
    ConstraintSet constraints = new ConstraintSet();
    constraints.clone(layout);
    for (int i = 0; i < spinners.length; i++) {
      Spinner spinner = spinners[i];
      int spinnerId = spinner.getId();
      constraints.connect(
          spinnerId, ConstraintSet.START,
          (i > 0) ? spinners[i - 1].getId() : layoutId,
          (i > 0) ? ConstraintSet.END : ConstraintSet.START
      );
      constraints.connect(
          spinnerId, ConstraintSet.END,
          (i < spinners.length - 1) ? spinners[i + 1].getId() : layoutId,
          (i < spinners.length - 1) ? ConstraintSet.START : ConstraintSet.END
      );
      constraints.connect(spinnerId, ConstraintSet.TOP, layoutId, ConstraintSet.TOP);
      constraints.connect(spinnerId, ConstraintSet.BOTTOM, layoutId, ConstraintSet.BOTTOM);
    }
    constraints.applyTo(layout);
    return spinners;
  }

  private String[] getUnicodeArray(String source) {
    return source
        .codePoints()
        .mapToObj((codePoint) -> new String(new int[]{codePoint}, 0, 1))
        .toArray(String[]::new);
  }

}