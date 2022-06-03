package eu.jaloszynski.splitit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import eu.jaloszynski.R;
import eu.jaloszynski.splitit.helpers.FriendsExtra;

public class AddNewFriendActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "FRIENDS";

    private EditText etName;
    private EditText etSurname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        etSurname = findViewById(R.id.etSurname);
        etName = findViewById(R.id.etName);

        final Button saveBtn = findViewById(R.id.btnAddFriend);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent replyIntent = new Intent();

                if (TextUtils.isEmpty(etName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                    // TODO warunek dla surname jeszcze
                } else {

                    FriendsExtra tmp1 = new FriendsExtra(
                            etName.getText().toString(),
                            etSurname.getText().toString());

                    setResult(RESULT_OK, replyIntent);

                    replyIntent.putExtra(EXTRA_REPLY, tmp1);


                }
                finish();
            }
        });
    }
}
