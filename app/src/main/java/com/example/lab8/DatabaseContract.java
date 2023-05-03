package com.example.lab8;

import android.provider.BaseColumns;

/**
 * The purpose of the DatabaseContract class is to define the database schema for the SQLite database used in the application.
 * It defines the table name and column names for the "users" table.
 * By using this class to define the schema,
 * it allows for easier and more organized access to the table and column names throughout the application code,
 * avoiding hardcoded values that can be error-prone and difficult to maintain.
 * Additionally, it uses the BaseColumns interface to define the _ID column,
 * which is a standard column name used by Android for identifying rows in a table.
 */
public final class DatabaseContract {

    private DatabaseContract() {} // To prevent accidentally instantiating the contract class

    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_SECURITY_QUESTION = "security_question";
        public static final String COLUMN_NAME_SECURITY_ANSWER = "security_answer";
    }
}

