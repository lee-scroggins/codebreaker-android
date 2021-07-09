package edu.cnm.deepdive.codebreaker.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.SimpleGuessAdapter;
import edu.cnm.deepdive.codebreaker.databinding.FragmentPlayBinding;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.viewmodel.GameViewModel;

public class PlayFragment extends Fragment implements InputFilter {

  private static final String ILLEGAL_CHARACTERS_FORMAT = "[^%s]+";

  private FragmentPlayBinding binding;
  private GameViewModel viewModel;
  private int codeLength;
  private String pool;
  private String illegalCharacters;

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
      viewModel.submitGuess(binding.guess.getText().toString().trim().toUpperCase());
      binding.guess.setText("");
    });
    binding.guess.setFilters(new InputFilter[]{this});
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
    illegalCharacters = String.format(ILLEGAL_CHARACTERS_FORMAT, pool);
    if (game.isSolved()) {
      binding.guess.setEnabled(false);
      binding.submit.setEnabled(false);
    } else {
      binding.guess.setEnabled(true);
      enforceSubmitConditions(binding.guess.length());
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

  @Override
  public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd,
      Spanned dest, int destStart, int destEnd) {
    String modifiedSource = source
        .subSequence(sourceStart, sourceEnd)
        .toString()
        .toUpperCase()
        .replaceAll(illegalCharacters, "");
    StringBuilder builder = new StringBuilder(dest);
    builder.replace(destStart, destEnd, modifiedSource);
    if (builder.length() > codeLength) {
      modifiedSource =
          modifiedSource.substring(0, modifiedSource.length() - (builder.length() - codeLength));
    }
    int newLength = dest.length() - (destEnd - destStart) + modifiedSource.length();
    enforceSubmitConditions(newLength);
    return modifiedSource;
  }

}