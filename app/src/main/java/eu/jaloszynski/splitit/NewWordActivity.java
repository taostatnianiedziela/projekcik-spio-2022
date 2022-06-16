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
import java.util.stream.Collectors;

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
    private double sumPart = 0;
    private int friendsCounter = 1;
    private boolean spinnerActive = false;
    private TextView btClearAll;
    private TextView tvExpensesList;
    private Button btSave;
    private Button btAdd;
    private List<Friends> addedFriendsList = new ArrayList<>();
    private List<Expense> addedExpensesList = new ArrayList<>();
    private Switch swAddMe;
    private Switch swProportional;
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
        swProportional  = findViewById(R.id.sw_proportional);

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

        swAddMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swichAction();
            }
        });

        swProportional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swichProportionalAction();
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
            public void onItemClick(Expense item) {
                //addedFriendsList.remove(item);
                addedExpensesList.remove(item);;
                friendsCounter -=1;
                changeSum();
                adapterFriendsExpenses.notifyDataSetChanged();
                loadSpinnerData(); //TODO poprawić, odświerzanie aby nie ładował całego spinnera tylko dadany element.
            }

            @Override
            public void afterTextChanged(Expense item, String text) {
                if(!text.equals("") && !text.equals("podział"))
                {
                    int index = addedExpensesList.indexOf(item);
                    item.setValue(text);
                    addedExpensesList.set(index,item);
                    countListPart();
                    changeSum();
                }

            }
        },addedExpensesList); //addedFriendsList

        this.rvFriendsExpenses.setAdapter(adapterFriendsExpenses);

    }

    private void countListPart()
    {
        sumPart = addedExpensesList.stream().collect(Collectors.summingDouble(o->o.getValueInDouble()));
    }

    private void swichAction() {
        if(swAddMe.isChecked()) friendsCounter += 1;
        else friendsCounter -= 1;
        changeSum();
    }

    private void swichProportionalAction() {

        if(swProportional.isChecked())
        {
            for (Expense temp1 : addedExpensesList) {
                temp1.setValue("0");
            }
        }

        changeSum();


    }

    //TODO czyszczenie wszystkiego zrobić
    private void clearAll() {
        tvSum.setText("Suma");
        tvUsersList.setText("Lista wybranych znajomych");
        tvExpensesList.setText("Lista wydatków:");
    }

    private void setSaveButton() {
        Intent replyIntent = new Intent();

        if (TextUtils.isEmpty(etExpenseView.getText()) || TextUtils.isEmpty(etValueView.getText()) || friendsCounter < 1 ) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {

            for (Expense temp1 : addedExpensesList) {
                temp1.setExpanse(etExpenseView.getText().toString());
                if(swProportional.isChecked())temp1.setValue((String.valueOf(Math.round((sum/friendsCounter)*100)/100.0)));
            }

            replyIntent.putExtra(EXTRA_REPLY_FRIENDS_LIST, (Serializable) addedExpensesList);
            replyIntent.putExtra(EXTRA_REPLY_TITLE,  etExpenseView.getText().toString());
            replyIntent.putExtra(EXTRA_REPLY_EXPENSE,  Math.round((sum/friendsCounter)*100)/100.0);
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
                Expense tmpEx = new Expense(friends.getName() + " " + friends.getSurname(), "0", "0", friends.getId());
                tmpEx.setImage(friends.getImage());
                addedExpensesList.add(tmpEx);
                //addedFriendsList.add(friends);

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

        if(swProportional.isChecked()) {
            tvSum.setText("Suma wydatków: " + Math.round(sum * 100.0) / 100.0 + " zł \n" + "Każda osoba powinna zapłacić: " + Math.round((sum / friendsCounter) * 100) / 100.0 + " zł");
        }
        else
        {
            tvSum.setText("Suma wydatków: " + Math.round(sum * 100.0) / 100.0 + " zł \n" + "Do podziału zostało " + (sum - sumPart));

        }
    }

}
