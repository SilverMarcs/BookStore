package com.example.lab02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private EditText editTextBkTitle;
    private EditText editTextBkPrice;
    private EditText editTextBkAuthor;
    private EditText editTextBkDesc;
    private EditText editTextBkIsbn;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawerlayout;

    private ArrayList<Book> bookList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BookRecyclerAdapter bookRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        editTextBkTitle = findViewById(R.id.editTextBkTitle);
        editTextBkIsbn = findViewById(R.id.editTextBkIsbn);
        editTextBkAuthor = findViewById(R.id.editTextBkAuthor);
        editTextBkDesc = findViewById(R.id.editTextBkDesc);
        editTextBkPrice = findViewById(R.id.editTextBkPrice);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        drawerlayout = findViewById(R.id.drawer_layout);

        // Recycle Adapter
        recyclerView = findViewById(R.id.bookRecyclerView);

        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        recyclerView.setLayoutManager(layoutManager);   // Also StaggeredGridLayoutManager and GridLayoutManager or a custom Layout manager

        bookRecyclerAdapter = new BookRecyclerAdapter();
        bookRecyclerAdapter.setData(bookList);
        recyclerView.setAdapter(bookRecyclerAdapter);

        // Toolbar
        setSupportActionBar(toolbar);

        // Floating Action Button (FAB)
        fab.setOnClickListener(view -> addBook());

        // Drawer Layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation View Drawer
        NavigationView navigationView = findViewById(R.id.drawer_navigation_view);
        navigationView.setNavigationItemSelectedListener(new DrawerNavigationListener());

        // SMS Permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        // SMS Receiver
        IntentFilter smsReceivedFilter = new IntentFilter(SMSReceiver.SMS_FILTER);
        registerReceiver(smsReceiver, smsReceivedFilter);

        if (savedInstanceState == null) {
            this.loadBook();
        }
    }

    // drawer menu
    class DrawerNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.add_book_drawer) {
                addBook();
            } else if (id == R.id.remove_last_drawer) {
                bookList.remove(bookList.size() - 1);
                bookRecyclerAdapter.notifyDataSetChanged();
            } else if (id == R.id.remove_all_drawer) {
                bookList.clear();
                bookRecyclerAdapter.notifyDataSetChanged();
            } else if (id == R.id.close_app_drawer) {
                finish();
            }
            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    // options menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // options menu id listener
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_fields_option) {
            clearFields();
        } else if (id == R.id.load_data_option) {
            loadBook();
        } else if (id == R.id.total_books_option) {
            Toast.makeText(getApplicationContext(), "Number of  books in store: " + bookList.size(), Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // SMS Receiver
    BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            StringTokenizer sT = new StringTokenizer(message, "|");

            editTextBkTitle.setText(sT.nextToken());
            editTextBkIsbn.setText(sT.nextToken());
            editTextBkAuthor.setText(sT.nextToken());
            editTextBkDesc.setText(sT.nextToken());

            double price = Double.parseDouble(sT.nextToken());
            boolean priceBool = Boolean.parseBoolean(sT.nextToken());

            editTextBkPrice.setText(priceBool ? String.valueOf(price + 100) : String.valueOf(price + 5));

            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    };

    public void addBook() {

        if (editTextBkTitle.getText().toString().isEmpty() || editTextBkPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in title and price fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String bookTitle = editTextBkTitle.getText().toString();
        String bookIsbn = editTextBkIsbn.getText().toString();
        String bookAuthor = editTextBkAuthor.getText().toString();
        String bookDesc = editTextBkDesc.getText().toString();
        String bookPrice = editTextBkPrice.getText().toString();

        SharedPreferences myData = getPreferences(0);
        SharedPreferences.Editor myEditor = myData.edit();
        myEditor.putString("bkTitle", bookTitle);
        myEditor.putString("bkIsbn", bookIsbn);
        myEditor.putString("bkAuthor", bookAuthor);
        myEditor.putString("bkDesc", bookDesc);
        myEditor.putString("bkPrice", bookPrice);
        myEditor.apply();

        bookList.add(new Book(bookTitle, bookIsbn, bookAuthor, bookDesc, bookPrice));
        bookRecyclerAdapter.notifyDataSetChanged();

        String toastMsg = "Book (" + editTextBkTitle.getText().toString() + ") added. Price: " + editTextBkPrice.getText().toString();
        Toast addBookToast = Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT);
        addBookToast.show();
    }

    public void clearFields() {
        View[] allFields = new View[]{editTextBkTitle, editTextBkPrice, editTextBkAuthor, editTextBkDesc, editTextBkIsbn};

        for (View field : allFields) {
            ((EditText) field).setText("");
        }
    }

    private void loadBook() {
        SharedPreferences myData = getPreferences(0);
        editTextBkTitle.setText(myData.getString("bkTitle", ""));
        editTextBkIsbn.setText(myData.getString("bkIsbn", ""));
        editTextBkAuthor.setText(myData.getString("bkAuthor", ""));
        editTextBkDesc.setText(myData.getString("bkDesc", ""));
        editTextBkPrice.setText(myData.getString("bkPrice", ""));
    }
}