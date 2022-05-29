package eu.jaloszynski.splitit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import eu.jaloszynski.R;

public class NewWordActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "NO_TEXT";

    private EditText etNameView;
    private EditText etExpenseView;
    private EditText etValueView;
    private Spinner spUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        //etNameView = findViewById(R.id.et);
        etExpenseView = findViewById(R.id.etExpense);
        etValueView = findViewById(R.id.etValue);
        spUsers = findViewById(R.id.users_dropdown);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.friendsList,
                android.R.layout.simple_spinner_item);
        //link the adapter to the spinner
        spUsers = (Spinner) findViewById(R.id.users_dropdown);
        spUsers.setAdapter(adapter);



        final Button saveBtn = findViewById(R.id.button_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent replyIntent = new Intent();

                if (TextUtils.isEmpty(etExpenseView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {

                    ExpenseExtra tmp1 = new ExpenseExtra(
                            spUsers.getSelectedItem().toString(),
                            etValueView.getText().toString(),
                            etExpenseView.getText().toString());

                    setResult(RESULT_OK, replyIntent);

                    replyIntent.putExtra(EXTRA_REPLY, tmp1);


                }
                finish();
            }
        });
    }
}
