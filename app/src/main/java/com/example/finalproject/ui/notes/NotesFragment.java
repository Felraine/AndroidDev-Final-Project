package com.example.finalproject.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.databinding.FragmentBudgetBinding;
import com.example.finalproject.databinding.FragmentNotesBinding;

public class NotesFragment extends Fragment {
    public String title;
    private String content;
    private FragmentNotesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotesViewModel notesViewModel =
                new ViewModelProvider(this).get(NotesViewModel.class);

        binding = FragmentNotesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotes;
        notesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    /*public Note(){

    }*/

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title= title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /*public Note(String title, String content){
        this.title=title;
        this.content=content;
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}