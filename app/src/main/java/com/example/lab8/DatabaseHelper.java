package com.example.lab8;
//ToDo Connect this database to the internet
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BasicLogin.db";

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_SECURITY_QUESTION = "security_question";
    private static final String COLUMN_SECURITY_ANSWER = "security_answer";

    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM_ID = "id";
    public static final String COLUMN_ITEM_NAME = "name";
    public static final String COLUMN_ITEM_DESCRIPTION = "description";
    public static final String COLUMN_ITEM_PRICE = "price";
    public static final String COLUMN_ITEM_DATE_POSTED = "date_posted";
    public static final String COLUMN_ITEM_SELLER_ID = "seller_id";

    public static final String TABLE_SELLERS = "sellers";
    public static final String COLUMN_SELLER_ID = "id";
    public static final String COLUMN_SELLER_NAME = "name";
    public static final String COLUMN_SELLER_EMAIL = "email";
    public static final String COLUMN_SELLER_PASSWORD = "password";

    public static final String TABLE_RATINGS = "ratings";
    public static final String COLUMN_RATING_ID = "id";
    public static final String COLUMN_RATING_USER_ID = "user_id";
    public static final String COLUMN_RATING_ITEM_ID = "item_id";
    public static final String COLUMN_RATING_VALUE = "value";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
            " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PASSWORD + "TEXT NOT NULL)";

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
            "(" +
            COLUMN_ITEM_ID + " INTEGER PRIMARY KEY," +
            COLUMN_ITEM_NAME + " TEXT," +
            COLUMN_ITEM_DESCRIPTION + " TEXT," +
            COLUMN_ITEM_PRICE + " INTEGER," +
            COLUMN_ITEM_DATE_POSTED + " TEXT," +
            COLUMN_ITEM_SELLER_ID + " INTEGER," +
            "FOREIGN KEY(" + COLUMN_ITEM_SELLER_ID + ") REFERENCES " + TABLE_SELLERS + "(" + COLUMN_SELLER_ID + ")" +
            ")";

    private static final String CREATE_SELLERS_TABLE = "CREATE TABLE " + TABLE_SELLERS +
            "(" +
            COLUMN_SELLER_ID + " INTEGER PRIMARY KEY," +
            COLUMN_SELLER_NAME + " TEXT," +
            COLUMN_SELLER_EMAIL + " TEXT," +
            COLUMN_SELLER_PASSWORD + " TEXT" +
            ")";

    private static final String CREATE_RATINGS_TABLE = "CREATE TABLE " + TABLE_RATINGS +
            "(" +
            COLUMN_RATING_ID + " INTEGER PRIMARY KEY," +
            COLUMN_RATING_USER_ID + " INTEGER," +
            COLUMN_RATING_ITEM_ID + " INTEGER," +
            COLUMN_RATING_VALUE + " INTEGER," +
            "FOREIGN KEY(" + COLUMN_RATING_USER_ID + ") REFERENCES " + TABLE_SELLERS + "(" + COLUMN_SELLER_ID + ")," +
            "FOREIGN KEY(" + COLUMN_RATING_ITEM_ID + ") REFERENCES " + TABLE_ITEMS + "(" + COLUMN_ITEM_ID + ")" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_RATINGS_TABLE);
        db.execSQL(CREATE_SELLERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
//ToDo Connect Me to the internet!!!
}
