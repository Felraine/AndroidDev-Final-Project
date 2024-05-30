package com.example.finalproject.ui.budget;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private double totalIncome = 0.0;
    private double currentBudget = 0.0;
    private double totalExpenses = 0.0;
    private TextView currentBudgetTextView;
    private TextView totalExpenseTextView;

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

        currentBudgetTextView = binding.walletView;
        totalExpenseTextView = binding.totalExpenseTextView;

        Button addExpenseButton = binding.addExpenseButton;
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExpenseDialog();
            }
        });

        Button addIncomeButton = binding.addIncomeButton;
        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddIncomeDialog();
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
                    updateBudget(-expenseAmount);
                    updateTotalExpenses(expenseAmount);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void showAddIncomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_dialog_add_income, null);
        builder.setView(dialogView);

        final EditText incomeAmountEditText = dialogView.findViewById(R.id.incomeAmountEditText);
        Button saveButton = dialogView.findViewById(R.id.saveButton);



        final AlertDialog dialog = builder.create();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String incomeName = ("Income");
                String incomeAmountStr = incomeAmountEditText.getText().toString();

                if (!TextUtils.isEmpty(incomeAmountStr)) {
                    double incomeAmount = Double.parseDouble(incomeAmountStr);
                    totalIncome += incomeAmount;
                    expenseList.add(new Expense(incomeName, incomeAmount));
                    updateBudget(incomeAmount);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void updateBudget(double amount) {
        currentBudget += amount;
        currentBudgetTextView.setText("Wallet: PHP " + String.format("%.2f", currentBudget));
    }

    private void updateTotalExpenses(double expenseAmount) {
        totalExpenses += expenseAmount;
        totalExpenseTextView.setText("Expenses: PHP " + String.format("%.2f", totalExpenses));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
