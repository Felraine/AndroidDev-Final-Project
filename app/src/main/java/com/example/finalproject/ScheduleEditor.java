package com.example.finalproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class ScheduleEditor extends AppCompatActivity {

    private Sched s;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_editor);

        EditText sched = findViewById(R.id.sched);
        TextView from = findViewById(R.id.from);
        TextView to = findViewById(R.id.to);
        Spinner color = findViewById(R.id.color);
        Button submit = findViewById(R.id.submit);
        TextView delete = findViewById(R.id.delete);

        databaseFunctions database = new databaseFunctions(this);
        s = new Sched();
        String date = getIntent().getStringExtra("Date");
        s.setID(database.getNextID(date));

        String[] colors = {"Rose", "Blue", "Green", "Red", "Yellow", "Orange", "Purple", "Grey"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, colors);
        color.setAdapter(adapter);

        if (getIntent().hasExtra("Task")) {
            s.setID(getIntent().getIntExtra("ID", 0));
            s.setSched(getIntent().getStringExtra("Task"));
            s.setFrom(getIntent().getStringExtra("From"));
            s.setTo(getIntent().getStringExtra("To"));
            s.setColor(getIntent().getStringExtra("Color"));
            color.setSelection(adapter.getPosition(s.getColor()));
            GradientDrawable background = (GradientDrawable) color.getBackground();
            background.setColor(s.getColorID(ScheduleEditor.this));
            sched.setText(s.getSched());
            from.setText(s.getFromToString());
            to.setText(s.getToToString());

            submit.setOnClickListener(v-> {
                if (sched.getText().toString().equals("")) {
                    sched.setError("Schedule cannot be empty");
                    return;
                }
                if (from.getText().equals("Click Here")) {
                    Toast.makeText(this, "Select Time: From", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (to.getText().equals("Click Here")) {
                    Toast.makeText(this, "Select Time: To", Toast.LENGTH_SHORT).show();
                    return;
                }
                s.setSched(sched.getText().toString());
                database.updateSched(s, date);
                Toast.makeText(this, "Schedule updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

            delete.setOnClickListener(v-> {
                database.deleteSched(s.getID(), date);
                Toast.makeText(this, "Schedule deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

        } else {
            submit.setOnClickListener(v-> {
                if (sched.getText().toString().equals("")) {
                    sched.setError("Schedule cannot be empty");
                    return;
                }
                if (from.getText().equals("Click Here")) {
                    Toast.makeText(this, "Select Time: From", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (to.getText().equals("Click Here")) {
                    Toast.makeText(this, "Select Time: To", Toast.LENGTH_SHORT).show();
                    return;
                }
                s.setSched(sched.getText().toString());
                database.addSched(s, date);
                Toast.makeText(this, "Schedule added successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

            delete.setVisibility(View.GONE);

        }

        from.setOnClickListener(v-> {
            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hh, mm) -> {
                String ho = new DecimalFormat("00").format(hh);
                String min = new DecimalFormat("00").format(mm);
                from.setText(ho+":"+min);
                s.setFrom(ho+":"+min);
            }, s.getFrom().getHour(), s.getFrom().getMinute(), true);
            timePickerDialog.show();
        });

        to.setOnClickListener(v-> {
            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hh, mm) -> {
                String ho = new DecimalFormat("00").format(hh);
                String min = new DecimalFormat("00").format(mm);
                to.setText(ho+":"+min);
                s.setTo(ho+":"+min);
            }, s.getTo().getHour(), s.getTo().getMinute(), true);
            timePickerDialog.show();
        });

        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s.setColor(color.getSelectedItem().toString());
                GradientDrawable background = (GradientDrawable) color.getBackground();
                background.setColor(s.getColorID(ScheduleEditor.this));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

    }
}