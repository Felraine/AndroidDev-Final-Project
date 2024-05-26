package com.example.finalproject.ui.reminders;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentRemindersBinding;

public class RemindersFragment extends Fragment {

    private FragmentRemindersBinding binding;
    private static final String CHANNEL_ID = "REMINDER_CHANNEL";
    private Button add;
    private AlertDialog dialog;
    private LinearLayout layout;
    private String reminderText, reminderDesc;

    private RemindersViewModel remindersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        remindersViewModel = new ViewModelProvider(this).get(RemindersViewModel.class);

        binding = FragmentRemindersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        createNotificationChannel();

        add = binding.addButton; // add new reminder button
        layout = binding.container; // where the cards are being shown

        buildDialog();
        add.setOnClickListener(v -> dialog.show()); // opens dialog

        remindersViewModel.getReminders().observe(getViewLifecycleOwner(), reminders -> {
            layout.removeAllViews();
            for (RemindersViewModel.Reminder reminder : reminders) {
                addCard(reminder);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void buildDialog() {                //dialog.xml stuff here
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        EditText name = view.findViewById(R.id.name); // title of the reminder
        EditText desc = view.findViewById(R.id.description); // reminder description

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 30) {
                    s.delete(30, s.length());
                    Toast.makeText(requireContext(), "Maximum 30 characters allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 50) {
                    s.delete(50, s.length());
                    Toast.makeText(requireContext(), "Maximum 50 characters allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);
        builder.setTitle("Input Reminder")
                .setPositiveButton("Submit", (dialog, which) -> {
                    reminderText = name.getText().toString();
                    reminderDesc = desc.getText().toString();
                    RemindersViewModel.Reminder reminder = new RemindersViewModel.Reminder(reminderText, reminderDesc);
                    remindersViewModel.addReminder(reminder);
                    triggerNotification(reminderText, reminderDesc);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {});

        dialog = builder.create();
    }

    private void addCard(RemindersViewModel.Reminder reminder) {            //card.xml stuff here
        View view = getLayoutInflater().inflate(R.layout.card, null);
        TextView nameView = view.findViewById(R.id.reminderTitle);
        TextView desView = view.findViewById(R.id.descView);
        ImageButton delete = view.findViewById(R.id.deleteButton);

        nameView.setText(reminder.getTitle());
        desView.setText(reminder.getDescription());

        delete.setOnClickListener(v -> {
            layout.removeView(view);
            remindersViewModel.removeReminder(reminder);
        });

        layout.addView(view);
    }
                                                            //fault ahh notificaiton here
    private void triggerNotification(String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification) // Add your notification icon
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for Reminder Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static class ReminderBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String reminderText = intent.getStringExtra("reminderText");
            String reminderDesc = intent.getStringExtra("reminderDesc");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification) // Add your notification icon
                    .setContentTitle(reminderText)
                    .setContentText(reminderDesc)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        }
    }
}
