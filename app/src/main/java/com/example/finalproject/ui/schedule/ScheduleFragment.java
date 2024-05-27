package com.example.finalproject.ui.schedule;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.Sched;
import com.example.finalproject.ScheduleEditor;
import com.example.finalproject.databaseFunctions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ScheduleFragment extends Fragment {

    private LocalDate showedDate;
    private ArrayList<Sched> scheds;
    private final DateTimeFormatter mainDate = DateTimeFormatter.ofPattern("EEEE dd/MM");
    private databaseFunctions database;
    private TextView date;
    private ListAdapter listAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        date = rootView.findViewById(R.id.date);
        ImageView left = rootView.findViewById(R.id.left);
        ImageView right = rootView.findViewById(R.id.right);
        ListView listview = rootView.findViewById(R.id.listview);
        LinearLayout add = rootView.findViewById(R.id.add);

        scheds = new ArrayList<>();
        listAdapter = new ListAdapter();
        listview.setAdapter(listAdapter);
        database = new databaseFunctions(getContext());

        showedDate = LocalDate.now();
        RefreshData();

        date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
                showedDate = LocalDate.of(year, month + 1, day);
                RefreshData();
            }, showedDate.getYear(), showedDate.getMonthValue() - 1, showedDate.getDayOfMonth());
            datePickerDialog.show();
        });

        left.setOnClickListener(v -> {
            showedDate = showedDate.minusDays(1);
            RefreshData();
        });

        right.setOnClickListener(v -> {
            showedDate = showedDate.plusDays(1);
            RefreshData();
        });

        add.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScheduleEditor.class);
            intent.putExtra("Date", showedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            startActivity(intent);
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshData();
    }

    private void RefreshData() {
        date.setText(showedDate.format(mainDate));
        ArrayList<Sched> ts = database.getAllTasks(showedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Collections.sort(ts);
        scheds = ts;
        listAdapter.notifyDataSetChanged();
    }

    public class ListAdapter extends BaseAdapter {

        public ListAdapter() {

        }

        @Override
        public int getCount() {
            return scheds.size();
        }

        @Override
        public Sched getItem(int i) {
            return scheds.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"InflateParams", "ViewHolder"})
            View v = inflater.inflate(R.layout.schedule, null);

            TextView from = v.findViewById(R.id.from);
            TextView to = v.findViewById(R.id.to);
            TextView task = v.findViewById(R.id.sched);

            Sched s = scheds.get(i);

            from.setText(s.getFromToString());
            to.setText(s.getToToString());
            task.setText(s.getSched());

            GradientDrawable backDrawable = (GradientDrawable) task.getBackground();
            backDrawable.setColor(s.getColorID(getContext()));

            task.setOnLongClickListener(v2 -> {
                Intent intent = new Intent(getActivity(), ScheduleEditor.class);
                intent.putExtra("ID", s.getID());
                intent.putExtra("Task", s.getSched());
                intent.putExtra("From", s.getFromToString());
                intent.putExtra("To", s.getToToString());
                intent.putExtra("Color", s.getColor());
                intent.putExtra("Date", showedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                startActivity(intent);
                return true;
            });

            return v;
        }
    }
}
