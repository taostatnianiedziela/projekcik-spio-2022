package eu.jaloszynski.splitit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import eu.jaloszynski.R;

public class NewWordActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "NO_TEXT";

    private EditText etNameView;
    private EditText etExpenseView;
    private EditText etValueView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        etNameView = findViewById(R.id.etName);
        etExpenseView = findViewById(R.id.etExpense);
        etValueView = findViewById(R.id.etValue);

        final Button saveBtn = findViewById(R.id.button_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent replyIntent = new Intent();

                if (TextUtils.isEmpty(etNameView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra(EXTRA_REPLY, etNameView.getText().toString());
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
