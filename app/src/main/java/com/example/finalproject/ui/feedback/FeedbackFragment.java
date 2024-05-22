package com.example.finalproject.ui.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.databinding.FragmentFeedbackBinding;

public class FeedbackFragment extends Fragment {

    private FragmentFeedbackBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FeedbackViewModel feedbackViewModel =
                new ViewModelProvider(this).get(FeedbackViewModel.class);

        binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

      /*  final TextView textView = binding.textFeedback;
        feedbackViewModel.getText().observe(getViewLifecycleOwner(), textView::setText); */

        /*
        * binding: like a replacement for findViewById.
        * example: binding.button.setOnClickListener(new View.OnClickListener(){
        * viewModel.userClicked();
        * });
        * */
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}