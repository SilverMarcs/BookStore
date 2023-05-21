package com.example.lab02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button signUpButton, loginButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        signUpButton = findViewById(R.id.sign_up_button);
        loginButton = findViewById(R.id.login_button);
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.apply();

                    Toast.makeText(LoginPage.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginPage.this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                String storedUsername = sharedPreferences.getString("username", "");
                String storedPassword = sharedPreferences.getString("password", "");

                if (!username.isEmpty() && !password.isEmpty() && username.equals(storedUsername) && password.equals(storedPassword)) {
                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginPage.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}