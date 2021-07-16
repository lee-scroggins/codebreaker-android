package edu.cnm.deepdive.codebreaker.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.ScoreboardAdapter;
import edu.cnm.deepdive.codebreaker.databinding.FragmentScoreboardBinding;
import edu.cnm.deepdive.codebreaker.viewmodel.DashboardViewModel;
import edu.cnm.deepdive.codebreaker.viewmodel.GameViewModel;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

public class ScoreboardFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

  private GameViewModel viewModel;
  private FragmentScoreboardBinding binding;
  private BiConsumer<Integer, Boolean> codeLengthUpdater = (value, fromUser) -> {
    binding.codeLengthDisplay.setText(String.valueOf(value));
    if (fromUser) {
      viewModel.setCodeLength(value);
    }
  };
  private BiConsumer<Integer, Boolean> poolSizeUpdater = (value, fromUser) -> {
    binding.poolSizeDisplay.setText(String.valueOf(value));
    if (fromUser) {
      viewModel.setPoolSize(value);
    }
  };

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    binding = FragmentScoreboardBinding.inflate(inflater, container, false);
    binding.codeLength.setTag(codeLengthUpdater);
    binding.codeLength.setOnSeekBarChangeListener(this);
    binding.poolSize.setTag(poolSizeUpdater);
    binding.poolSize.setOnSeekBarChangeListener(this);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    viewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
    viewModel.getCodeLength().observe(getViewLifecycleOwner(), (codeLength) ->
        binding.codeLength.setProgress(codeLength));
    viewModel.getPoolSize().observe(getViewLifecycleOwner(), (poolSize) ->
        binding.poolSize.setProgress(poolSize));
    viewModel.getScoreboard().observe(getViewLifecycleOwner(), (games) -> {
      //noinspection ConstantConditions
      ScoreboardAdapter adapter = new ScoreboardAdapter(getContext(), games);
      binding.games.setAdapter(adapter);
    });
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    //noinspection unchecked
    ((BiConsumer<Integer, Boolean>) seekBar.getTag()).accept(progress, fromUser);
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
  }

}