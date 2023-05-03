package com.example.lab8;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    // Declare variables for the UI elements
    private EditText nEmail, nPassword;
    private TextView  nSignUp,forgotPassword;
    private ProgressBar progressBar;
    private Button loginButton;
    public static final String EMAIL_REGEX = "^(.+)@(.+)$";

    // Declare a variable for the database helper
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiate the database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize the UI elements
        nEmail = findViewById(R.id.email_loginInput);
        nPassword = findViewById(R.id.password_loginInput);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progressBar);
        nSignUp = findViewById(R.id.create_account);
        forgotPassword = findViewById(R.id.cforgot_password);

        // Set up the login button to respond to clicks
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user input for email and password
                String email = nEmail.getText().toString();
                String password = nPassword.getText().toString();

                // Validate the user input
                validateInput(email,password);
                if (email.isEmpty() || !email.matches(EMAIL_REGEX)) {
                    nEmail.setError("Input valid email");
                    return;
                }

                if (password.isEmpty() ) {
                    nPassword.setError("Please input valid password");
                    return;
                }
                if (password.length() < 6  ) {
                    nPassword.setError("Please input password with at least 6 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                    if (performLogin(email, password)) {
                        progressBar.setVisibility(View.GONE);
                        // Login successful, start the main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // Login failed, show an error message
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();

                    }

            }
        });

        nSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });



    }

    // Validate the user input for email and password
    private boolean validateInput(String email, String password) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && !TextUtils.isEmpty(password);
    }

    // Attempt to log the user in using the provided email and password
    private boolean performLogin(String email, String password) {
        // Get a readable database instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns to retrieve
        String[] projection = {
                "name"
        };

        // Define the selection criteria
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = { email, password };

        // Execute the query and get the result as a cursor
        Cursor cursor = db.query(
                "users",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if the login was successful based on the number of rows returned
        boolean success = cursor.getCount() > 0;

        // Close the cursor and database
        cursor.close();
        db.close();

        // Return the login status
        return success;
    }
}



