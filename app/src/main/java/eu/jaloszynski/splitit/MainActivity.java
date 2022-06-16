package eu.jaloszynski.splitit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

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
    public static final String FRIEND_EXPENSE_ACTIVITY_EXTRA_KEY = "ID_EXTERN_KEY_FRIENDS";
    private static String TAG = "MainActivity in SplitIt";


    private ExpenseViewModel expenseViewModel;
    private ExpenseListAdapter adapter;

    private FriendsViewModel friendsViewModel;
    private FriendsListAdapter adapterFriends;
    private AlertDialog.Builder builder;
    private TextView tv_info;
    double SumOfValue = 0;
    private String name = "Użytkowniku"; //TODO zrobić dynamiczną nazwę

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_info=findViewById(R.id.tv_info);

        FloatingActionButton fab_add_exoenses = findViewById(R.id.fab);
        fab_add_exoenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewExpensesActivity.class);
                //Intent intent = new Intent(MainActivity.this, HistoryExpenseActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        FloatingActionButton fab_history_exoenses = findViewById(R.id.fab_history);
        fab_history_exoenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                Intent intent = new Intent(MainActivity.this, HistoryExpenseActivity.class);
                startActivity(intent);
                //startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
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

        RecyclerView recyclerView = findViewById(R.id.rvFriendsExpenses);
        adapter = new ExpenseListAdapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(Expense item) {

                Intent intent = new Intent(MainActivity.this, FriendExpenseActivity.class);
                Bundle b = new Bundle();
                b.putInt(FRIEND_EXPENSE_ACTIVITY_EXTRA_KEY, item.getExtern_key_Friends()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);


        expenseViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                // Update the cached copy of the words in the adapter.

                expenses = sumOfExpensesFriend(expenses);
                setSumView(expenses);
                adapter.setExpenses(expenses);
            }
        });

        // TODO : dokonczyc wyswietlanie ??
        adapterFriends = new FriendsListAdapter(this);
        //recyclerView.setAdapter(adapter);

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendsViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        friendsViewModel.getAllFriendses().observe(this, new Observer<List<Friends>>() {
            @Override
            public void onChanged(@Nullable List<Friends> friendses) {
                // Update the cached copy of the words in the adapter.
                adapterFriends.setFriendses(friendses);
            }
        });

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
    }


    private List<Expense> sumOfExpensesFriend(List<Expense> tmp1) {
        boolean bezpiecznik = true;
        List<Expense> tmp2 = new ArrayList<Expense>();
        if(tmp1 != null && tmp2 !=null){
                for (int i=0 ; i<tmp1.size();i++) {
                    Expense temp1 = tmp1.get(i);
                    if(tmp2.isEmpty()) {
                        tmp2.add(temp1);
                    }
                    else {
                        for (int k=0 ; k<tmp2.size();k++) {
                            Expense temp2 = tmp2.get(k);
                            if (temp2.getName().equals(temp1.getName()) ) {
                                    double sum = temp2.getValueInDouble() + temp1.getValueInDouble();
                                    temp2.setValue(String.valueOf(sum));
                                    bezpiecznik = false;
                            }
                        }
                        if(bezpiecznik == true) {
                            tmp2.add(temp1);

                        }
                    }
                }
        }

        SumOfValue = 0;
        for (int k=0 ; k<tmp2.size();k++) {
            Expense temp2 = tmp2.get(k);
            SumOfValue += temp2.getValueInDouble();
            }

        return tmp2;

    }

    private void setSumView(List<Expense> expenses) {

         tv_info.setText("Witaj "+name+"!\n"+"Twoi znajomi są Ci dłużni \n" + SumOfValue + " zł");
        //tv_info.setText("Witaj "+name+"!\n"+"Sprawdź ile są Ci dłużni Twoi znajomi!");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Intent intent = getIntent();
                List<Expense>  tmpFriendsList = (List<Expense>) data.getSerializableExtra(NewExpensesActivity.EXTRA_REPLY_FRIENDS_LIST);
                double expense_1 = (double) data.getSerializableExtra(NewExpensesActivity.EXTRA_REPLY_EXPENSE);
                String title = (String) data.getSerializableExtra(NewExpensesActivity.EXTRA_REPLY_TITLE);

                if (tmpFriendsList != null && expense_1 != 0) {
                    for (Expense temp1 : tmpFriendsList) {

                        expenseViewModel.insert(temp1);
                    }
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
                    Friends tmp = new Friends(friend.getName(), friend.getSurname());
                    tmp.setImage(friend.getImage());
                    friendsViewModel.insert(tmp);
                }
                Snackbar.make(findViewById(R.id.fab), R.string.friend_saved, Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Snackbar.make(findViewById(R.id.fab), R.string.empty_not_saved, Snackbar.LENGTH_LONG)
                        .show();
            }
        }

    }

    public void RequestSmsCode(String message, String... number) {
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" ));
        intent.putExtra( "sms_body", message );
        startActivity(intent);
    }

}
