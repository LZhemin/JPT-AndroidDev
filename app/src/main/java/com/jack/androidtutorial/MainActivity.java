package com.jack.androidtutorial;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.jack.message";

    private EditText textField;
    private String serverURL;
    private EditText resultField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField = (EditText) findViewById(R.id.message);
        resultField = (EditText) findViewById(R.id.result);
        serverURL = getResources().getString(R.string.serverURL);
    }

    public void onSubmit(View pressed) {
        String text = textField.getText().toString();
        Intent intent = new Intent(this, DisplayMessageActivity.class).putExtra(EXTRA_MESSAGE, text);
        Log.i("Message_Test", text);
        startActivity(intent);
    }

    public void onRequest(View pressed) {
        try {
            new RequestTask().execute(new URL(serverURL));
        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
        }
    }

    private class RequestTask extends AsyncTask<URL, Integer, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = "";
            try {
                HttpURLConnection httpUrlConnection;

                URL url = urls[0];
                httpUrlConnection = (HttpURLConnection) url.openConnection();

                httpUrlConnection.setRequestMethod("GET");
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");

                // checks server's status code first
                int status = httpUrlConnection.getResponseCode();
                if (status == HttpURLConnection.HTTP_OK) {
                    InputStream responseStream = new
                            BufferedInputStream(httpUrlConnection.getInputStream());

                    BufferedReader responseStreamReader =
                            new BufferedReader(new InputStreamReader(responseStream));

                    String line;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = responseStreamReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    responseStreamReader.close();
                    response = stringBuilder.toString();
                    httpUrlConnection.disconnect();
                } else {
                    httpUrlConnection.disconnect();
                    throw new IOException("Server returned non-OK status: " + status);
                }
            } catch (Exception e) {
                response = e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Requesting from server", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String results) {
            completeRequest(results);
        }
    }

    private void completeRequest(String results) {
        resultField.setText(results);
    }
}
