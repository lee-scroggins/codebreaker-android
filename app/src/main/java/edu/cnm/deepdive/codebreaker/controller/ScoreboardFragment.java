package edu.cnm.deepdive.codebreaker.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.FragmentScoreboardBinding;
import edu.cnm.deepdive.codebreaker.viewmodel.DashboardViewModel;
import edu.cnm.deepdive.codebreaker.viewmodel.GameViewModel;
import org.jetbrains.annotations.NotNull;

public class ScoreboardFragment extends Fragment {

  private GameViewModel viewModel;
  private FragmentScoreboardBinding binding;

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    binding = FragmentScoreboardBinding.inflate(inflater, container, false);
    // TODO Attach listeners sliders.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
    // TODO Add observers.
  }
}