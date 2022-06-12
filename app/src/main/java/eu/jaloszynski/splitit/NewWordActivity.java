package eu.jaloszynski.splitit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.jaloszynski.splitit.R;
import eu.jaloszynski.splitit.adapter.ExpenseFriendsListAdapter;
import eu.jaloszynski.splitit.adapter.ExpenseListAdapter;
import eu.jaloszynski.splitit.adapter.ExpenseListAdapter2;
import eu.jaloszynski.splitit.adapter.FriendsSpinnerAdapter;
import eu.jaloszynski.splitit.helpers.ExpenseExtra;
import eu.jaloszynski.splitit.helpers.OnItemClickListener;
import eu.jaloszynski.splitit.helpers.OnItemClickListenerFriends;
import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.persistence.Friends;
import eu.jaloszynski.splitit.repository.FriendsRepository;

public class NewWordActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY_FRIENDS_LIST = "FRIENDS_LIST_EXTRA";
    public static final String EXTRA_REPLY_EXPENSE = "EXPENSE_EXTRA";
    public static final String EXTRA_REPLY_TITLE = "TITLE_EXTRA";

    private TextView tvUsersList;
    private EditText etExpenseView;
    private EditText etValueView;
    private TextView tvSum;
    private Spinner spUsers;
    private double sum = 0;
    private int friendsCounter = 1;
    private boolean spinnerActive = false;
    private TextView btClearAll;
    private TextView tvExpensesList;
    private Button btSave;
    private Button btAdd;
    private List<Friends> addedFriendsList = new ArrayList<>();
    private Switch swAddMe;
    ArrayList<Friends> friends_tmp1;

    private RecyclerView rvFriendsExpenses;
    private RecyclerView.Adapter adapterFriendsExpenses;


    public NewWordActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        etExpenseView = findViewById(R.id.etExpense);
        etValueView = findViewById(R.id.etValue);
        spUsers = findViewById(R.id.users_dropdown);
        //tvUsersList = findViewById(R.id.usersList);
        tvSum = findViewById(R.id.tvSum);
        btClearAll = findViewById(R.id.button_clearAll);
        btSave = findViewById(R.id.button_save);
        btAdd = findViewById(R.id.button_add);
        swAddMe = findViewById(R.id.sw_addMe);

        loadSpinnerData();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSaveButton();
            }
        });
        btClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddButton();
            }
        });

        swAddMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swAddMe.isChecked())
                {
                    friendsCounter += 1;
                }
                else
                {
                    friendsCounter -= 1;
                }
                changeSum();
            }
        });

        etValueView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(etValueView.getText()))
                {
                    sum=0;
                }
                changeSum();
            }
        });

        this.rvFriendsExpenses = (RecyclerView) findViewById(R.id.rvFriendsExpenses);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.rvFriendsExpenses.setLayoutManager(mLayoutManager);

       // adapterFriendsExpenses = new ExpenseFriendsListAdapter(addedFriendsList);

        adapterFriendsExpenses = new ExpenseFriendsListAdapter(this, new OnItemClickListenerFriends() {

            @Override
            public void onItemClick(Friends item) {

                addedFriendsList.remove(item);

                changeSum();
                adapterFriendsExpenses.notifyDataSetChanged();
                loadSpinnerData();

            }

        },addedFriendsList);

        this.rvFriendsExpenses.setAdapter(adapterFriendsExpenses);

    }



    private void setAddButton() {
        if ( TextUtils.isEmpty(etValueView.getText()) && TextUtils.isEmpty(etExpenseView.getText() )) {
           // TODO toast z informacją że nie dodano
        } else
            tvExpensesList.append(etExpenseView.getText() + " " + etValueView.getText()  + "\n");
        changeSum();
    }

    private void clearAll() {
        tvSum.setText("Suma :");
        tvUsersList.setText("Lista wybranych znajomych");
        tvExpensesList.setText("Lista wydatków:");
    }

    private void setSaveButton() {
        Intent replyIntent = new Intent();

        if (TextUtils.isEmpty(etExpenseView.getText()) || TextUtils.isEmpty(etValueView.getText()) || friendsCounter < 1 ) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            replyIntent.putExtra(EXTRA_REPLY_FRIENDS_LIST, (Serializable) addedFriendsList);
            replyIntent.putExtra(EXTRA_REPLY_TITLE,  etExpenseView.getText().toString());
            replyIntent.putExtra(EXTRA_REPLY_EXPENSE,  sum/friendsCounter);
            setResult(RESULT_OK, replyIntent);
            finish();
        }

    }

    private void loadSpinnerData() {

        FriendsRepository friendsRepository = new FriendsRepository(getApplication());

        ArrayList<Friends> friendsList=new ArrayList<Friends>(2);
        friendsList.add(0,new Friends("Wybierz","znajomego"));


        final ArrayAdapter<Friends> dataAdapter = new FriendsSpinnerAdapter(this,
                android.R.layout.simple_spinner_item, friendsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spUsers.setAdapter(dataAdapter);
        spUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if(position==0)
                    return;
                Friends friends = dataAdapter.getItem(position);
                friendsCounter +=1;
                addedFriendsList.add(friends);
                changeSum();
                dataAdapter.remove(friends);
                adapterFriendsExpenses.notifyDataSetChanged();
                spUsers.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        friendsRepository.getAllFriendses().observe(this, new Observer<List<Friends>>() {

            @Override
            public void onChanged(@Nullable final List<Friends> items) {
                Collections.sort(items, new Comparator<Friends>() {
                    public int compare(Friends obj1, Friends obj2) {
                        return obj1.getSurname().compareToIgnoreCase(obj2.getSurname());
                    }
                });
                dataAdapter.addAll(items);
                dataAdapter.notifyDataSetChanged();
            }

        });

    }

    private void changeSum() {
        if (TextUtils.isEmpty(etValueView.getText().toString()))
        {
            sum = 0;
        }else {
            sum = Double.parseDouble(etValueView.getText().toString());
        }
        tvSum.setText("Suma wydatków: " + sum + " Na głowę wychodzi: " + sum / friendsCounter);
    }

    private float roulette(){

        return  2.2f;
    }
}
