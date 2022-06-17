package eu.jaloszynski.splitit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Date;
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

public class NewExpensesActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY_FRIENDS_LIST = "FRIENDS_LIST_EXTRA";
    public static final String EXTRA_REPLY_EXPENSE = "EXPENSE_EXTRA";
    public static final String EXTRA_REPLY_TITLE = "TITLE_EXTRA";
    public static final int OPTION_SAVE = 1;
    public static final int OPTION_BACK = 2;

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
    private AlertDialog.Builder builder;


    Context context;
    int duration = Toast.LENGTH_SHORT;

    private RecyclerView rvFriendsExpenses;
    private RecyclerView.Adapter adapterFriendsExpenses;

    ArrayAdapter<Friends> dataAdapter;
    ArrayList<Friends> friendsList;
    FriendsRepository friendsRepository;

    public NewExpensesActivity() {
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
        builder = new AlertDialog.Builder(NewExpensesActivity.this);
        loadSpinnerData();
        context = getApplicationContext();






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

                addedExpensesList.remove(item);
                friendsCounter -=1;
                changeSum();
                adapterFriendsExpenses.notifyDataSetChanged();

                List<Friends> tmp = friendsRepository.getAllFriendses().getValue();
                for(Friends tmp2: tmp) {
                   if(tmp2.getId() == item.getExtern_key_Friends())
                   {
                       dataAdapter.add(tmp2);
                   }
                }

                //loadSpinnerData(); //TODO poprawić, odświerzanie aby nie ładował całego spinnera tylko dadany element.
            }

            @Override
            public void afterTextChanged(Expense item, String text) {
                if(!text.equals("") && !text.equals("podział"))
                {
                    int index = addedExpensesList.indexOf(item);
                    if(index>=0) {
                        item.setValue(text);
                        addedExpensesList.set(index, item);
                        countListPart();
                        changeSum();
                    }
                }

            }
        },addedExpensesList); //addedFriendsList

        this.rvFriendsExpenses.setAdapter(adapterFriendsExpenses);



    }


    @Override
    public void onBackPressed(){
        Intent replyIntent = new Intent();
        alertYesNoBuilder("Twoje zmiany nie zostaną zapisane. Czy napewno chcesz zamknąć ?",replyIntent, OPTION_SAVE);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    private void countListPart()
    {
        sumPart = addedExpensesList.stream().collect(Collectors.summingDouble(o->o.getValueInDouble()));
        sumPart= Math.round((sumPart)*100)/100.0;
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
        etExpenseView.setText("");
        etValueView.setText("");
        sum = 0;
        sumPart = 0;
        addedExpensesList.clear();
        adapterFriendsExpenses.notifyDataSetChanged();
        loadSpinnerData();

        changeSum();
    }

    private void setSaveButton() {
        Intent replyIntent = new Intent();

        if (TextUtils.isEmpty(etExpenseView.getText()) || TextUtils.isEmpty(etValueView.getText()) || friendsCounter < 1 ) {
            showToast("Popraw dane");
            //setResult(RESULT_CANCELED, replyIntent);
        } else if (sum-sumPart<0 && !swProportional.isChecked()) {
            showToast("Suma podziałów nie może być większa niż kwota wydatku");

        }
        else if(sum-sumPart>0 && !swProportional.isChecked())
        {
            alertYesNoBuilder("Do podziału zostało: " +(sum-sumPart) +"\nCzy napewno chcesz kontynuować?",replyIntent, OPTION_SAVE);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
         {
             saveAndExit(replyIntent);

         }

    }

    private void saveAndExit(Intent replyIntent) {
        for (Expense temp1 : addedExpensesList) {
            temp1.setExpanse(etExpenseView.getText().toString());
            if(swProportional.isChecked()) temp1.setValue((String.valueOf(Math.round((sum/friendsCounter)*100)/100.0)));
        }

        replyIntent.putExtra(EXTRA_REPLY_FRIENDS_LIST, (Serializable) addedExpensesList);
        replyIntent.putExtra(EXTRA_REPLY_TITLE,  etExpenseView.getText().toString());
        replyIntent.putExtra(EXTRA_REPLY_EXPENSE,  Math.round((sum/friendsCounter)*100)/100.0);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    private void loadSpinnerData() {

        friendsRepository = new FriendsRepository(getApplication());
        friendsList=new ArrayList<Friends>(2);
        friendsList.add(0,new Friends("Wybierz","znajomego"));

        dataAdapter = new FriendsSpinnerAdapter(this,
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

    private void showToast(String text)
    {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    protected void alertYesNoBuilder(String text, Intent intent, int option)
    {
        builder.setTitle("");
        builder.setMessage(text);

        //Yes Button
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(option==OPTION_SAVE) saveAndExit(intent);
                else if(option==OPTION_BACK) finish();
                //Toast.makeText(getApplicationContext(),"Dług usunięty!",Toast.LENGTH_LONG).show();
                //Log.i("Code2care ", "Dług usunięty!");
            }
        });

        //No Button
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Anulowano wybór",Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });

    }

}
