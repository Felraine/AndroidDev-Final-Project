package com.example.finalproject.ui.budget;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.Expense;
import com.example.finalproject.ExpenseAdapter;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentBudgetBinding;
import java.util.ArrayList;
import java.util.List;

public class BudgetFragment extends Fragment {

    private FragmentBudgetBinding binding;
    private List<Expense> expenseList;
    private ExpenseAdapter expenseAdapter;
    private RecyclerView expenseRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BudgetViewModel budgetViewModel =
                new ViewModelProvider(this).get(BudgetViewModel.class);

        binding = FragmentBudgetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(expenseList);

        expenseRecyclerView = binding.expenseRecyclerView;
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseRecyclerView.setAdapter(expenseAdapter);

        Button addExpenseButton = binding.addExpenseButton;
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExpenseDialog();
            }
        });

        return root;
    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_dialog_add_expense, null);
        builder.setView(dialogView);

        final EditText expenseNameEditText = dialogView.findViewById(R.id.expenseNameEditText);
        final EditText expenseAmountEditText = dialogView.findViewById(R.id.expenseAmountEditText);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        final AlertDialog dialog = builder.create();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expenseName = expenseNameEditText.getText().toString();
                String expenseAmountStr = expenseAmountEditText.getText().toString();

                if (!TextUtils.isEmpty(expenseName) && !TextUtils.isEmpty(expenseAmountStr)) {
                    double expenseAmount = Double.parseDouble(expenseAmountStr);
                    expenseList.add(new Expense(expenseName, expenseAmount));
                    expenseAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
