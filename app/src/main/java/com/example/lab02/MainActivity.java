package com.example.lab02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText editTextBkTitle;
    private EditText editTextBkId;
    private EditText editTextBkPrice;
    private EditText editTextBkAuthor;
    private EditText editTextBkDesc;
    private EditText editTextBkIsbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextBkId = findViewById(R.id.editTextBkId);
        editTextBkTitle = findViewById(R.id.editTextBkTitle);
        editTextBkIsbn = findViewById(R.id.editTextBkIsbn);
        editTextBkAuthor = findViewById(R.id.editTextBkAuthor);
        editTextBkDesc = findViewById(R.id.editTextBkDesc);
        editTextBkPrice = findViewById(R.id.editTextBkPrice);

        if (savedInstanceState == null) {
            this.loadBookData();
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        IntentFilter smsReceivedFilter = new IntentFilter("SMS_RECEIVED_ACTION");
        SMSReceiver mySMS = new SMSReceiver();
        registerReceiver(smsReceiver, smsReceivedFilter);

    }

    BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String[] attributes = message.split("\\|");

            editTextBkId.setText(attributes[0]);
            editTextBkTitle.setText(attributes[1]);
            editTextBkIsbn.setText(attributes[2]);
            editTextBkAuthor.setText(attributes[3]);
            editTextBkDesc.setText(attributes[4]);
            editTextBkPrice.setText(attributes[5]);
        }
    };

    public void addBook(View view) {
        saveBookData();

        String toastMsg = "Book (" + editTextBkTitle.getText().toString() + ") added. Price: " + editTextBkPrice.getText().toString();
        Toast addBookToast = Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT);
        addBookToast.show();
    }

    public void clearFields(View view) {
        View[] allFields = new View[]{editTextBkId, editTextBkTitle, editTextBkPrice, editTextBkAuthor, editTextBkDesc, editTextBkIsbn};

        for (View field : allFields) {
            ((EditText) field).setText("");
        }
    }

    private void saveBookData() {
        SharedPreferences myData = getPreferences(0);
        SharedPreferences.Editor myEditor = myData.edit();
        myEditor.putString("bkTitle", editTextBkTitle.getText().toString());
        myEditor.putString("bkIsbn", editTextBkIsbn.getText().toString());
        myEditor.putString("bkAuthor", editTextBkAuthor.getText().toString());
        myEditor.putString("bkDesc", editTextBkDesc.getText().toString());
        myEditor.putString("bkPrice", editTextBkPrice.getText().toString());
        myEditor.putString("bkId", editTextBkId.getText().toString());
        myEditor.commit();
    }

    private void loadBookData() {
        SharedPreferences myData = getPreferences(0);
        editTextBkTitle.setText(myData.getString("bkTitle", ""));
        editTextBkIsbn.setText(myData.getString("bkIsbn", ""));
        editTextBkAuthor.setText(myData.getString("bkAuthor", ""));
        editTextBkDesc.setText(myData.getString("bkDesc", ""));
        editTextBkPrice.setText(myData.getString("bkPrice", ""));
        editTextBkId.setText(myData.getString("bkId", ""));
    }

    public void loadBookDataBtn(View view) {
        this.loadBookData();
    }
}