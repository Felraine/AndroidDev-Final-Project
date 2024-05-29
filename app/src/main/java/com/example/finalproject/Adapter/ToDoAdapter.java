package com.example.finalproject.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.AddNewTask;
import com.example.finalproject.Model.ToDoModel;
import com.example.finalproject.R;
import com.example.finalproject.Utils.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private Context context;
    private FragmentManager fragmentManager; // Add FragmentManager reference
    private DataBaseHelper myDB;
    private EditTaskListener editTaskListener;

    public ToDoAdapter(DataBaseHelper myDB, Context context, FragmentManager fragmentManager){
        this.context = context;
        this.fragmentManager = fragmentManager; // Assign fragmentManager
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myDB.updateStatus(item.getId(), 1);
                }else{
                    myDB.updateStatus(item.getId(), 0);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTaskListener.onEditTask(item.getId(), item.getTask());
            }
        });
    }

    public boolean toBoolean(int num){
        return num != 0;
    }

    public void setTasks(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public interface EditTaskListener {
        void onEditTask(int taskId, String taskName);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Context getContext() {
        return context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mCheckBox);
        }
    }

    public void editItem(int position){
        ToDoModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(fragmentManager, task.getTag());
    }
}
