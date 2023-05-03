package com.example.lab8;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

        private EditText NameSK, EmailSK, PasswordSK, confirmPasswordSK;
        private Button sSignupButton;
        private TextView login;
        private ProgressBar progressBar;
        public static final String EMAIL_REGEX = "^(.+)@(.+)$";

        // Add a reference to the MyDatabaseHelper class
        private DatabaseHelper mDbHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_account);

            // Initialize the MyDatabaseHelper instance
            mDbHelper = new DatabaseHelper(this);

            NameSK = findViewById(R.id.nameET);
            EmailSK = findViewById(R.id.emailET);
            PasswordSK = findViewById(R.id.passwordET);
            confirmPasswordSK = findViewById(R.id.confirmPassET);
            sSignupButton = findViewById(R.id.signUpBtn);
            progressBar = findViewById(R.id.progressBar);
            login = findViewById(R.id.loginTV);

            sSignupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = NameSK.getText().toString();
                    String email = EmailSK.getText().toString();
                    String password = PasswordSK.getText().toString();
                    String confirmPassword = confirmPasswordSK.getText().toString();

                    if (name.isEmpty() || name.equals(" ")) {
                        NameSK.setError("Please input valid name");
                        return;
                    }

                    if (email.isEmpty() || !email.matches(EMAIL_REGEX)) {
                        EmailSK.setError("Please input valid email");
                        return;
                    }

                    if (password.isEmpty() ) {
                        PasswordSK.setError("Please input valid password");
                        return;
                    }
                    if (password.length() < 6  ) {
                        PasswordSK.setError("Please input password with at least 6 characters");
                        return;
                    }

                    if (!password.equals(confirmPassword)) {
                        confirmPasswordSK.setError("Password not match");
                        return;
                    }


                    progressBar.setVisibility(View.VISIBLE);
                    // Validate user input
                        if (performSignup(name, email, password)) {
                            progressBar.setVisibility(View.GONE);
                            // Sign up successful, show a success message and return to the login screen
                            Toast.makeText(SignupActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            // Sign up failed, show an error message
                            Toast.makeText(SignupActivity.this, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                }
            });



            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        private boolean performSignup(String name, String email, String password) {
            // Get a writable database using the MyDatabaseHelper instance
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a ContentValues object to store the new user's information
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("email", email);
            values.put("password", password);



            // Insert the new user into the database and get the row ID of the newly inserted row
            long newRowId = db.insert("users", null, values);

            // Close the database to release resources
            db.close();

            // Check if the insert was successful (i.e. row ID is not -1)
            return newRowId != -1;
        }
    }

