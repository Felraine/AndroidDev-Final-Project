package com.example.finalproject.ui.customizethemes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.databinding.FragmentCustomizethemeBinding;
public class CustomizeThemeFragment extends Fragment {

    private FragmentCustomizethemeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CustomizeThemeViewModel customizeThemeViewModel =
                new ViewModelProvider(this).get(CustomizeThemeViewModel.class);

        binding = FragmentCustomizethemeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textThemes;
        customizeThemeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}