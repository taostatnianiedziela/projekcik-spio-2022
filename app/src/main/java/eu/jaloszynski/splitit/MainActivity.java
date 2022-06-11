package eu.jaloszynski.splitit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import eu.jaloszynski.splitit.adapter.ExpenseListAdapter;
import eu.jaloszynski.splitit.adapter.FriendsListAdapter;
import eu.jaloszynski.splitit.helpers.FriendsExtra;
import eu.jaloszynski.splitit.helpers.OnItemClickListener;
import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.persistence.Friends;
import eu.jaloszynski.splitit.viewmodel.ExpenseViewModel;
import eu.jaloszynski.splitit.viewmodel.FriendsViewModel;

public class MainActivity extends AppCompatActivity {
    private static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private static final int NEW_FRIEND_ACTIVITY_REQUEST_CODE = 2;
    private static String TAG = "MainActivity in SplitIt";


    private ExpenseViewModel expenseViewModel;
    private ExpenseListAdapter adapter;

    private FriendsViewModel friendsViewModel;
    private FriendsListAdapter adapterFriends;
    private List<Friends> tmpFriendsListExtra;
    private AlertDialog.Builder builder;
    private TextView tv_info;
    double SumOfValue = 0;
    private String name = "Adamie"; //TODO zrobić dynamiczną nazwę

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_info=findViewById(R.id.tv_info);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab_add_exoenses = findViewById(R.id.fab);
        fab_add_exoenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        FloatingActionButton fab_add_friends = findViewById(R.id.fab_add_friends);
        fab_add_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewFriendActivity.class);
                startActivityForResult(intent, NEW_FRIEND_ACTIVITY_REQUEST_CODE);

            }
        });

        builder = new AlertDialog.Builder(MainActivity.this);



        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new ExpenseListAdapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(Expense item) {
                alertYesNoBuilder(item);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                //Toast.makeText(getApplicationContext(), "Item Clicked" + item.getName(), Toast.LENGTH_LONG).show();


            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                // Update the cached copy of the words in the adapter.
                adapter.setExpenses(expenses);
                setSumView();
            }
        });

// TODO : dokonczyc wyswietlanie ??
        adapterFriends = new FriendsListAdapter(this);
        friendsViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        friendsViewModel.getAllFriendses().observe(this, new Observer<List<Friends>>() {
            @Override
            public void onChanged(@Nullable List<Friends> friends) {
                adapterFriends.setFriendses(friends);
            }
        });

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add_friend) {
            Intent intent = new Intent(MainActivity.this, AddNewFriendActivity.class);
            startActivityForResult(intent, NEW_FRIEND_ACTIVITY_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Intent intent = getIntent();
                List<Friends>  tmpFriendsList = (List<Friends>) data.getSerializableExtra(NewWordActivity.EXTRA_REPLY_FRIENDS_LIST);
                double expense_1 = (double) data.getSerializableExtra(NewWordActivity.EXTRA_REPLY_EXPENSE);
                String title = (String) data.getSerializableExtra(NewWordActivity.EXTRA_REPLY_TITLE);

                if (tmpFriendsList != null && expense_1 != 0) {
                    for (Friends temp1 : tmpFriendsList) {
                        System.out.println(temp1);
                        expenseViewModel.insert(new Expense(temp1.getName() + temp1.getSurname(),title,String.valueOf(expense_1),temp1.getId()));
                    }

                   // expenseViewModel.insert(new Expense(expense.getName(), expense.getExpense(), expense.getValue()));
                }
                Snackbar.make(findViewById(R.id.fab), R.string.word_saved, Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Snackbar.make(findViewById(R.id.fab), R.string.empty_not_saved, Snackbar.LENGTH_LONG)
                        .show();
            }
        }

        if (requestCode == NEW_FRIEND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Intent intent = getIntent();
                FriendsExtra friend = (FriendsExtra) data.getSerializableExtra(AddNewFriendActivity.EXTRA_REPLY);

                if (friend != null) {

                    friendsViewModel.insert(new Friends(friend.getName(), friend.getSurname()));
                }
                Snackbar.make(findViewById(R.id.fab), R.string.friend_saved, Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Snackbar.make(findViewById(R.id.fab), R.string.empty_not_saved, Snackbar.LENGTH_LONG)
                        .show();
            }
        }

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
