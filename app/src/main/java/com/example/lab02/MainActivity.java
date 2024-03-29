package com.example.lab02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.number.Scale;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab02.provider.Book;
import com.example.lab02.provider.BookViewModel;
import com.example.lab02.utils.RandomStringGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private BookViewModel viewModel;
    public DatabaseReference dbRef;
    private View frameLayout;
    private int initial_x, initial_y;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private int MAX_DISTANCE = 60;

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
        frameLayout = findViewById(R.id.frame_id);
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        scaleGestureDetector = new ScaleGestureDetector(this, new MyScaleGestureDetector());

        // ViewModel
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // Toolbar
        setSupportActionBar(toolbar);

        // Floating Action Button (FAB)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

        // Use below one
        // gesture on Frame layout. Deprecated. Use below one using gesture detector
//        frameLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                int action = motionEvent.getActionMasked();
//
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN: {
//                        initial_x = (int)motionEvent.getX();
//                        initial_y = (int)motionEvent.getY();
//                        return true;
//                    }
//                    case MotionEvent.ACTION_UP: {
//                        int final_x = (int)motionEvent.getX();
//                        int final_y = (int)motionEvent.getY();
//                        // horizontal
//
//                        // touch on top left corner
//                        if (initial_x < 50 && initial_y < 50) {
//                            editTextBkAuthor.setText(String.valueOf(editTextBkAuthor.getText()).toUpperCase());
//                            return true;
//                        }
//
//                        if (Math.abs(initial_y - final_y) < MAX_DISTANCE) {
//                            // right to left
//                            if (final_x < initial_x){
//                                addBook();
//                            }
//                        // vertical
//                        } else if (Math.abs(initial_x - final_x) < MAX_DISTANCE) {
//                            // bottom to top
//                            if (final_y < initial_y) {
//                                clearFields();
//                            }
//                            // bottom to top
//                            if (initial_y < final_y) {
//                                finish();
//                            }
//                        }
//                        return true;
//                    }
//                    case MotionEvent.ACTION_MOVE: {
//                        int final_x = (int)motionEvent.getX();
//                        int final_y = (int)motionEvent.getY();
//
//                        if (Math.abs(initial_y - final_y) < MAX_DISTANCE) {
//                            if (initial_x < final_x) {
//                                String valueStr = editTextBkPrice.getText().toString();
//                                float value = Float.parseFloat(valueStr);
//                                editTextBkPrice.setText(String.valueOf(value + 1));
//                            }
//                        }
//
//                        return true;
//                    }
//
//                    default:
//                        return false;
//                }
//
//            }
//        });


        // Drawer Layout

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return  true;
            }

        });

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

        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        dbRef = fbDB.getReference("Book/info");

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Toast.makeText(getApplicationContext() , "Book added to Firebase", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                Toast.makeText(getApplicationContext() , "Book deleted from Firebase", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        displayBookRecyclerFragment();
    }

    private void displayBookRecyclerFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new BookRecyclerFragment())
                .commit();
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
                viewModel.deleteLastBook();
            } else if (id == R.id.remove_all_drawer) {
                deleteAll();
                dbRef.removeValue();
            } else if (id == R.id.list_all_drawer) {
                Intent intent = new Intent(MainActivity.this, BookListActivity.class);
                startActivity(intent);
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
        }
        else if (id == R.id.total_books_option) {
            Toast.makeText(this, "Implement async operation", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.delete_expensive_option) {
            viewModel.deleteExpensiveBooks();
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

        String bookTitle = editTextBkTitle.getText().toString();
        String bookIsbn = editTextBkIsbn.getText().toString();
        String bookAuthor = editTextBkAuthor.getText().toString();
        String bookDesc = editTextBkDesc.getText().toString();
        String bookPrice = editTextBkPrice.getText().toString();

        Book book = new Book(bookTitle, bookAuthor, bookIsbn, bookDesc, Double.parseDouble(bookPrice));
        viewModel.insert(book);
        dbRef.push().setValue(book);

        String toastMsg = "Book (" + editTextBkTitle.getText().toString() + ") added. Price: " + editTextBkPrice.getText().toString();
        Toast addBookToast = Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT);
        addBookToast.show();
    }

    public void loadDefaultBook() {
        editTextBkTitle.setText("The Lord of the Rings");
        editTextBkIsbn.setText("9785546");
        editTextBkAuthor.setText("J.R.R. Tolkien");
        editTextBkDesc.setText("The Lord of the Rings is cool");
        editTextBkPrice.setText("100");
    }

    public void deleteAll() {
        viewModel.deleteAll();
    }

    public void clearFields() {
        View[] allFields = new View[]{editTextBkTitle, editTextBkPrice, editTextBkAuthor, editTextBkDesc, editTextBkIsbn};

        for (View field : allFields) {
            ((EditText) field).setText("");
        }
    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            clearFields();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            editTextBkIsbn.setText(RandomStringGenerator.generateNewRandomString(10));
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            loadDefaultBook();
            super.onLongPress(e);
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (velocityX > 1000 || velocityY > 1000) {
                moveTaskToBack(true);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY)) {                // horizontal scroll
                float currentPrice = Float.parseFloat(editTextBkPrice.getText().toString());
                float newPrice = currentPrice - distanceX;
                editTextBkPrice.setText(String.valueOf(newPrice));
            } else if (Math.abs(distanceY) > Math.abs(distanceX)) {         // vertical scroll
                editTextBkTitle.setText("untitled");
            }


            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private class MyScaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            editTextBkAuthor.setText(String.valueOf(editTextBkAuthor.getText()).toUpperCase());
            return super.onScale(detector);
        }
    }
}