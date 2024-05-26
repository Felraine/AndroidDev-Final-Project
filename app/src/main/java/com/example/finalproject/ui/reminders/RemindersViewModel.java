package com.example.finalproject.ui.reminders;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class RemindersViewModel extends ViewModel {
    private final MutableLiveData<List<Reminder>> reminders;

    public RemindersViewModel() {
        reminders = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Reminder>> getReminders() {
        return reminders;
    }

    public void addReminder(Reminder reminder) {
        List<Reminder> currentReminders = reminders.getValue();
        currentReminders.add(reminder);
        reminders.setValue(currentReminders);
    }

    public void removeReminder(Reminder reminder) {
        List<Reminder> currentReminders = reminders.getValue();
        currentReminders.remove(reminder);
        reminders.setValue(currentReminders);
    }

    public static class Reminder {
        public String text;
        public String time;
        public int day;
        public int month;
        public int year;
        public int hour;
        public int min;

        public Reminder(String text, String time, int day, int month, int year, int hour, int min) {
            this.text = text;
            this.time = time;
            this.day = day;
            this.month = month;
            this.year = year;
            this.hour = hour;
            this.min = min;
        }
    }
}
