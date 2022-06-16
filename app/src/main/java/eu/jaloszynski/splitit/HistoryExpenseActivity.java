package eu.jaloszynski.splitit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import eu.jaloszynski.splitit.adapter.ExpenseListAdapter;
import eu.jaloszynski.splitit.adapter.ExpenseListAdapter2;
import eu.jaloszynski.splitit.helpers.OnItemClickListener;
import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.viewmodel.ExpenseViewModel;

public class HistoryExpenseActivity extends AppCompatActivity {
    private static String TAG = "HistoryExpenseActivity in SplitIt";


    private ExpenseViewModel expenseViewModel;
    private ExpenseListAdapter adapter_3;
    private TextView tv_info;
    double SumOfValue = 0;
    private int tmp_id = 0;

    private String name = "Użytkowniku"; //TODO zrobić dynamiczną nazwę

    List<Expense> expenses2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tv_info=findViewById(R.id.tv_info);

        RecyclerView recyclerView = findViewById(R.id.rvHistoryExpenses);
        adapter_3 = new ExpenseListAdapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(Expense item) {
            }
        });
        recyclerView.setAdapter(adapter_3);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);

        // TODO dodac wyswietlanie tylko z danym ID
        expenseViewModel.getAllHistoryExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                // Update the cached copy of the words in the adapter.
                adapter_3.setExpenses(expenses);
            }

        });
        ActivityCompat.requestPermissions(HistoryExpenseActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
    }

    private void setSumView() {
        SumOfValue=countSumExpenses();
        tv_info.setText(name + " wisi Ci łącznie \n" + SumOfValue +" zł");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected double countSumExpenses()
    {
        double tmp = 0;
        List<Expense> tmplist = expenses2;
        if(tmplist!=null) {
            for (Expense temp1 : tmplist) {
                try {
                    tmp += Double.parseDouble(temp1.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return tmp;
    }




}
