package com.example.finalproject.ui.feedback;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.finalproject.databaseFunctions;
import com.example.finalproject.databinding.FragmentFeedbackBinding;

public class FeedbackFragment extends Fragment {
    private FragmentFeedbackBinding binding;
    private databaseFunctions DB;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FeedbackViewModel feedbackViewModel =
                new ViewModelProvider(this).get(FeedbackViewModel.class);

        binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DB = new databaseFunctions(getContext());

        EditText feedbackEditText = binding.userfeedback;
        Button submitButton = binding.feedbackSubmitBtn;
        RatingBar rate = binding.ratingBar;

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = feedbackEditText.getText().toString().trim();
                String username = getLoggedInUsername();

                if (feedback.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter feedback", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = DB.insertFeedback(username, feedback);
                    if (isInserted) {
                        Toast.makeText(getContext(), "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                        rate.setRating(0);
                    } else {
                        Toast.makeText(getContext(), "Feedback submission failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return root;
    }

    private String getLoggedInUsername() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "default_username");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}