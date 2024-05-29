package com.example.finalproject.ui.todolist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ToDoListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ToDoListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Put the To Do List here");
    }

    public LiveData<String> getText() {
        return mText;
    }
}