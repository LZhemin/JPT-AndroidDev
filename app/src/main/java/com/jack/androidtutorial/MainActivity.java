package com.jack.androidtutorial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.jack.message";

    private EditText textField;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField = (EditText) findViewById(R.id.message);
    }

    public void onScan(View pressed) {
        text = textField.getText().toString();
        Intent intent = new Intent(this, DisplayMessageActivity.class).putExtra(EXTRA_MESSAGE, text);
        Log.i("Message_Test", text);
        startActivity(intent);
    }
}
