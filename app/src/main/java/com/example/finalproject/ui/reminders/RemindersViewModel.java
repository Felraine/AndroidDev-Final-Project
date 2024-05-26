package com.example.finalproject.ui.reminders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        currentReminders.removeIf(r -> r.getId().equals(reminder.getId()));
        reminders.setValue(currentReminders);
    }

    public static class Reminder {
        private String id;
        private String title;
        private String description;

        public Reminder(String title, String description) {
            this.id = UUID.randomUUID().toString();
            this.title = title;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
}
