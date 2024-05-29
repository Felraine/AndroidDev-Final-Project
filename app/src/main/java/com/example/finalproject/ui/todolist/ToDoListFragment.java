package com.example.finalproject.ui.todolist;

import android.content.Intent;
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

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        List<Notes> notesList = dbHelper.getAllNotes();
        myAdapter = new MyAdapter(requireContext(), notesList);
        recyclerView.setAdapter(myAdapter);

        binding.addnewnotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNoteActivity.class));
            }
        });

        final TextView textView = binding.notestitle;
        toDoListViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
