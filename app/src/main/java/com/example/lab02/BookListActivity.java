package com.example.lab02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BookListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        displayBookRecyclerFragment();
    }

    private void displayBookRecyclerFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new BookRecyclerFragment())
                .commit();
    }
}