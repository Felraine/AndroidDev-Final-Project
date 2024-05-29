package com.example.finalproject.ui.todolist;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.AddNoteActivity;
import com.example.finalproject.DatabaseHelper;
import com.example.finalproject.MyAdapter;
import com.example.finalproject.Notes;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentToDoListBinding;

import java.util.List;

public class ToDoListFragment extends Fragment {

    private static final int ADD_NOTE_REQUEST = 1;

    private FragmentToDoListBinding binding;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ToDoListViewModel toDoListViewModel =
                new ViewModelProvider(this).get(ToDoListViewModel.class);

        binding = FragmentToDoListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        List<Notes> notesList = dbHelper.getAllNotes(username);
        myAdapter = new MyAdapter(requireContext(), notesList);
        recyclerView.setAdapter(myAdapter);

        binding.addnewnotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), AddNoteActivity.class), ADD_NOTE_REQUEST);
            }
        });

        final TextView textView = binding.notestitle;
        toDoListViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            updateAdapter();
        }
    }

    private void updateAdapter() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        List<Notes> notesList = dbHelper.getAllNotes(username);
        myAdapter = new MyAdapter(requireContext(), notesList);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
