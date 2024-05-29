package com.example.finalproject.ui.todolist;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Adapter.ToDoAdapter;
import com.example.finalproject.AddNewTask;
import com.example.finalproject.Model.ToDoModel;
import com.example.finalproject.OnDialogCloseListener;
import com.example.finalproject.R;
import com.example.finalproject.RecyclerViewTouchHelper;
import com.example.finalproject.Utils.DataBaseHelper;
import com.example.finalproject.databinding.FragmentToDoListBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToDoListFragment extends Fragment implements OnDialogCloseListener, ToDoAdapter.EditTaskListener {

    private static final int ADD_NOTE_REQUEST = 1;

    private FragmentToDoListBinding binding;
    private RecyclerView recyclerView;
    private ToDoAdapter myAdapter;
    private DataBaseHelper myDB;
    private List<ToDoModel> mList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentToDoListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerview);

        myDB = new DataBaseHelper(requireContext());
        mList = new ArrayList<>();
        myAdapter = new ToDoAdapter(myDB, requireContext(), getChildFragmentManager());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(myAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(myAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        myAdapter.setTasks(mList);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask task = AddNewTask.newInstance();
                task.setOnDialogCloseListener(ToDoListFragment.this);
                task.show(getChildFragmentManager(), AddNewTask.TAG);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            mList = myDB.getAllTasks();
            Collections.reverse(mList);

            myAdapter.setTasks(mList);

            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        myAdapter.setTasks(mList);
        myAdapter.notifyDataSetChanged();
        Log.d(TAG, "onDialogClose: Refreshed dataset");

        Toast.makeText(requireContext(), "Dialog closed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditTask(int taskId, String taskName) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", taskId);
        bundle.putString("task", taskName);

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(requireActivity().getSupportFragmentManager(), task.getTag());
    }
}
