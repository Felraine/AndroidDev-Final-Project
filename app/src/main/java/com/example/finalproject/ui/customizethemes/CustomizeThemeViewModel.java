package com.example.finalproject.ui.customizethemes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomizeThemeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CustomizeThemeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Customize Theme");
    }

    public LiveData<String> getText() {
        return mText;
    }
}