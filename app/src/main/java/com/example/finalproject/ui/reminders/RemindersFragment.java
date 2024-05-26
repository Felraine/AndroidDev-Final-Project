package com.example.finalproject.ui.reminders;
import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentRemindersBinding;
import com.example.finalproject.ui.reminders.RemindersViewModel.Reminder;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RemindersFragment extends Fragment {

    private FragmentRemindersBinding binding;
    private static final int REQUEST_CODE_NOTIFICATION_POLICY = 123;
    private static final String CHANNEL_ID = "REMINDER CHANNEL";
    // private NotificationCompat.Builder builder; !?!?
    Button add;
    AlertDialog dialog;
    LinearLayout layout;
    int hour, min, year, month, day;
    String reminderText, selectedTime;

    private RemindersViewModel remindersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        remindersViewModel = new ViewModelProvider(this).get(RemindersViewModel.class);

        binding = FragmentRemindersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        createNotificationChannel();
        add = binding.addButton;
        layout = binding.container;

        buildDialog();
        add.setOnClickListener(v -> dialog.show());


        remindersViewModel.getReminders().observe(getViewLifecycleOwner(), new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                layout.removeAllViews();
                for (Reminder reminder : reminders) {
                    addCard(reminder.text, reminder.time, reminder.day, reminder.month, reminder.year, reminder.hour, reminder.min);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_NOTIFICATION_POLICY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, re-trigger the notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
                //notificationManager.notify(1, builder.build()); helpppppp
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        //stuff inside dialog.xml
        EditText name = view.findViewById(R.id.name);
        Button timeButton = view.findViewById(R.id.timerButton);
        Button dateButton = view.findViewById(R.id.dateButton);

        timeButton.setOnClickListener(this::popTimePicker);
        dateButton.setOnClickListener(this::popDatePicker);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 30) {
                    s.delete(30, s.length());
                    Toast.makeText(requireContext(),
                            "Maximum 30 characters allowed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);
        builder.setTitle("Input Reminder")
                .setPositiveButton("Submit", (dialog, which) -> {
                    reminderText = name.getText().toString();
                    Reminder reminder = new Reminder(reminderText, selectedTime, day, month, year, hour, min);
                    remindersViewModel.addReminder(reminder);
                    setReminder(reminderText, year, month, day, hour, min);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                });

        dialog = builder.create();
    }

    // TIME PICKER
    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view1, hourOfDay, minute) -> {
            hour = hourOfDay;
            min = minute;
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, min);
            ((Button) view).setText(selectedTime);
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), onTimeSetListener, hour, min, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    // DATE PICKER
    public void popDatePicker(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = (view1, yearSelected, monthOfYear, dayOfMonth) -> {
            year = yearSelected;
            month = monthOfYear + 1;
            day = dayOfMonth;
            String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);
            ((Button) view).setText(date);
        };

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), onDateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void addCard(String name, String time, int day, int month, int year, int hour, int min) {
        View view = getLayoutInflater().inflate(R.layout.card, null);
        TextView nameView = view.findViewById(R.id.reminderTitle);
        TextView timeView = view.findViewById(R.id.timeView);
        TextView dateView = view.findViewById(R.id.dateView);
        ImageButton delete = view.findViewById(R.id.deleteButton);

        nameView.setText(name);
        timeView.setText(time);
        dateView.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year));

        //Reminder reminder = new Reminder(name, time, day, month, year, hour, min);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remindersViewModel.removeReminder(reminder); //shoot me bro
                layout.removeView(view);
            }
        });

        layout.addView(view);
    }

    private void setReminder(String text, int year, int month, int day, int hour, int min) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), ReminderBroadcastReceiver.class);
        intent.putExtra("reminderText", text);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder ";
            String description = "Channel for Reminder Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public class ReminderBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String reminderText = intent.getStringExtra("reminderText");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification) // NOTIFICATION ICON
                    .setContentTitle(reminderText)
                    .setContentText("Reminder!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, REQUEST_CODE_NOTIFICATION_POLICY);
                return;
            }
            notificationManager.notify(1, builder.build());
        }
    }
}

