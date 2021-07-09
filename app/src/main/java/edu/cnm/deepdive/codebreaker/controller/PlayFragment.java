package edu.cnm.deepdive.codebreaker.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
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
import edu.cnm.deepdive.codebreaker.viewmodel.GameViewModel;

public class PlayFragment extends Fragment implements TextWatcher {

  private FragmentPlayBinding binding;
  private GameViewModel viewModel;
  private int codeLength;

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
    binding.guess.addTextChangedListener(this);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    viewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
    viewModel.getGame().observe(getViewLifecycleOwner(), (game) -> {
      // TODO Make game display prettier.
      codeLength = game.getLength();
      if (game.isSolved()) {
        binding.guess.setEnabled(false);
        binding.submit.setEnabled(false);
      } else {
        binding.guess.setEnabled(true);
        enforceSubmitConditions();
      }
      SimpleGuessAdapter adapter = new SimpleGuessAdapter(getContext(), game.getGuesses());
      binding.guessList.setAdapter(adapter);
    });
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

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    enforceSubmitConditions();
  }

  private void enforceSubmitConditions() {
    binding.submit.setEnabled(binding.guess.getText().toString().trim().length() == codeLength);
  }

}