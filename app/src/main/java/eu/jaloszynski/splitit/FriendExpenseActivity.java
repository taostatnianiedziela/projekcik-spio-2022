package eu.jaloszynski.splitit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import eu.jaloszynski.splitit.adapter.ExpenseListAdapter2;
import eu.jaloszynski.splitit.helpers.OnItemClickListener;
import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.persistence.Friends;
import eu.jaloszynski.splitit.viewmodel.ExpenseViewModel;
import eu.jaloszynski.splitit.viewmodel.FriendsViewModel;

public class FriendExpenseActivity extends AppCompatActivity {
    private static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private static final int NEW_FRIEND_ACTIVITY_REQUEST_CODE = 2;
    private static String TAG = "FriendsExpenseActivity in SplitIt";


    private ExpenseViewModel expenseViewModel;
    private ExpenseListAdapter2 adapter_2;

    private AlertDialog.Builder builder;
    private TextView tv_info;
    double SumOfValue = 0;

    private int tmp_id = 0;

    private String name = "Adamie"; //TODO zrobić dynamiczną nazwę

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_expense);
        tv_info=findViewById(R.id.tv_info);
        builder = new AlertDialog.Builder(FriendExpenseActivity.this);

        Bundle b = getIntent().getExtras();
        if(b != null)
            tmp_id = b.getInt(MainActivity.FRIEND_EXPENSE_ACTIVITY_EXTRA_KEY);

        RecyclerView recyclerView = findViewById(R.id.recyclerview2);
        adapter_2 = new ExpenseListAdapter2(this, new OnItemClickListener() {
            @Override
            public void onItemClick(Expense item) {
                alertYesNoBuilder(item);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        recyclerView.setAdapter(adapter_2);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);

        // TODO dodac wyswietlanie tylko z danym ID
        expenseViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                // Update the cached copy of the words in the adapter.


                assert expenses != null;
                List<Expense> expenses2 =  expenses.stream()
                        .filter(p ->p.getExtern_key_Friends() == tmp_id)   // filtering price
                        .map(pm ->pm)          // fetching price
                        .collect(Collectors.toList());

//                Optional<Expense> expenses2 = expenses.stream().
//                        filter(p -> p.getExtern_key_Friends() == tmp_id).;


                adapter_2.setExpenses(expenses2);
                setSumView();
            }

        });


        ActivityCompat.requestPermissions(FriendExpenseActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

    }

    private void setSumView() {
        SumOfValue=countSumExpenses();
        tv_info.setText("Witaj "+name+"!\n"+"Pożyczyłeś łącznie " + SumOfValue);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSumView();
    }



    protected double countSumExpenses()
    {
        double tmp = 0;
        List<Expense> tmplist = expenseViewModel.getAllExpenses().getValue();
        if(tmplist!=null) {
            for (Expense temp1 : tmplist) {
                try {
                    tmp += parseDecimal(temp1.getValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return tmp;
    }

    public double parseDecimal(String input) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        ParsePosition parsePosition = new ParsePosition(0);
        Number number = numberFormat.parse(input, parsePosition);

        if(parsePosition.getIndex() != input.length()){
            throw new ParseException("Invalid input", parsePosition.getIndex());
        }

        return number.doubleValue();
    }

    public void RequestSmsCode(String message, String... number) {
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" ));
        intent.putExtra( "sms_body", message );
        startActivity(intent);
    }

    protected void alertYesNoBuilder(final Expense item)
    {

        builder.setTitle("AlertDialog with No Buttons");
        builder.setMessage("Czy chcesz usunąć dług " + item.getName() +" na kwotę " + item.getValue() + "za "+ item.getExpanse());

        //Yes Button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                expenseViewModel.delete(item);
                Toast.makeText(getApplicationContext(),"Yes button Clicked",Toast.LENGTH_LONG).show();
                Log.i("Code2care ", "Yes button Clicked!");
            }
        });

        //No Button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"No button Clicked",Toast.LENGTH_LONG).show();
                Log.i("Code2care ","No button Clicked!");
                dialog.dismiss();

            }
        });
        //Cancel Button
        builder.setNeutralButton("Wyślij SMS z przypomnienieniem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = "Cześć "+ item.getName() + " przypominam o długu w wysokości " + item.getValue() + " za " + item.getExpanse();
                RequestSmsCode(message);
                dialog.dismiss();
            }
        });

    }

}
