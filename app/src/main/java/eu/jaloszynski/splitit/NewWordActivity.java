package eu.jaloszynski.splitit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import eu.jaloszynski.R;
import eu.jaloszynski.splitit.adapter.FriendsListAdapter;
import eu.jaloszynski.splitit.persistence.Friends;
import eu.jaloszynski.splitit.persistence.FriendsDao;
import eu.jaloszynski.splitit.persistence.FriendsRoomDatabase;
import eu.jaloszynski.splitit.viewmodel.FriendsViewModel;

public class NewWordActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "NO_TEXT";

    private EditText etExpenseView;
    private EditText etValueView;
    private TextView tvFriendsList;
    private Spinner spFriends;

    private FriendsViewModel friendsViewModel;
    private FriendsListAdapter adapterFriends;

    //private FriendsDao friendsDao;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        FriendsRoomDatabase db = Room.databaseBuilder(getApplicationContext(),
                FriendsRoomDatabase.class, "friends_database").build();
        FriendsDao friendsDao = db.friendsDao();
        LiveData<List<Friends>> tmpFriends = friendsDao.getAllFriends();

        etExpenseView = findViewById(R.id.etExpense);
        etValueView = findViewById(R.id.etValue);
        spFriends= (Spinner)findViewById(R.id.users_dropdown);

        ArrayAdapter myAdapter =
                ((ArrayAdapter) spFriends.getAdapter());


        adapterFriends = new FriendsListAdapter(this);
        spFriends.setAdapter(myAdapter);

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendsViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        friendsViewModel.getAllFriendses().observe(this, new Observer<List<Friends>>() {
            @Override
            public void onChanged(@Nullable List<Friends> friendses) {
                // Update the cached copy of the words in the adapter.
                adapterFriends.setFriendses(friendses);
            }
        });



//        myAdapter.notifyDataSetChanged();


        final Button saveBtn = findViewById(R.id.button_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          /*      Intent replyIntent = new Intent();

                if (TextUtils.isEmpty(etNameView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra(EXTRA_REPLY, etNameView.getText().toString());
                    setResult(RESULT_OK, replyIntent);
                }
                finish();*/
            }
        });
    }
}
