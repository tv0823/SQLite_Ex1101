package com.example.sqlite_ex1101;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The type Helper db.
 * This class manages the creation and upgrading of the SQLite database used by the application.
 */
public class HelperDB extends SQLiteOpenHelper {
    /**
     * The name of the database file.
     */
    private static final String DATABASE_NAME = "dbGYNbus.db";
    /**
     * The current version of the database schema.
     */
    private static final int DATABASE_VERSION = 4;
    /**
     * String for creating tables.
     */
    String strCreate;
    /**
     * String for deleting tables.
     */
    String strDelete;

    /**
     * Instantiates a new Helper db.
     *
     * @param context The context of the application.
     */
    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * This method creates the necessary tables for the application's data.
     *
     * @param db The database instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        strCreate = "CREATE TABLE " + Employee.TABLE_EMPLOYEE;
        strCreate += " ("+Employee.ID+" INTEGER,";
        strCreate += " "+Employee.COMPANY+" TEXT,";
        strCreate += " "+Employee.FIRST_NAME+" TEXT,";
        strCreate += " "+Employee.LAST_NAME+" TEXT,";
        strCreate += " "+Employee.PHONE_NUM+" TEXT,";
        strCreate += " "+Employee.CARD_ID+" TEXT";
        strCreate += ");";
        db.execSQL(strCreate);

        strCreate = "CREATE TABLE " + Company.TABLE_COMPANY;
        strCreate += " ("+Company.COMPANY_ID+" INTEGER,";
        strCreate += " "+Company.COMPANY_NAME+" TEXT,";
        strCreate += " "+Company.MAIN_PHONE+" TEXT,";
        strCreate += " "+Company.SECOND_PHONE+" TEXT";
        strCreate += ");";
        db.execSQL(strCreate);

        strCreate = "CREATE TABLE " + Meal.TABLE_MEAL;
        strCreate += " ("+Meal.MEAL_ID+" INTEGER PRIMARY KEY,";
        strCreate += " "+Meal.FIRST_MEAL+" TEXT,";
        strCreate += " "+Meal.MAIN_COURSE+" TEXT,";
        strCreate += " "+Meal.EXTRA+" TEXT,";
        strCreate += " "+Meal.DESSERT+" TEXT,";
        strCreate += " "+Meal.DRINK+" TEXT";
        strCreate += ");";
        db.execSQL(strCreate);

        strCreate = "CREATE TABLE " + Order.TABLE_ORDER;
        strCreate += " ("+Order.DATE+" TEXT,";
        strCreate += " "+Order.TIME+" INTEGER,";
        strCreate += " "+Order.WORKER_ID+" TEXT,";
        strCreate += " "+Order.MEAL_ID+" INTEGER,";
        strCreate += " "+Order.COMPANY+" TEXT";
        strCreate += ");";
        db.execSQL(strCreate);
    }
    /**
     * Called when the database needs to be upgraded.
     * This method drops existing tables and recreates them with the new schema.
     *
     * @param db The database instance.
     * @param oldVer The old database version.
     * @param newVer The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        strDelete = "DROP TABLE IF EXISTS "+ Employee.TABLE_EMPLOYEE;
        db.execSQL(strDelete);

        strDelete = "DROP TABLE IF EXISTS "+ Company.TABLE_COMPANY;
        db.execSQL(strDelete);

        strDelete = "DROP TABLE IF EXISTS "+ Meal.TABLE_MEAL;
        db.execSQL(strDelete);

        strDelete = "DROP TABLE IF EXISTS "+ Order.TABLE_ORDER;
        db.execSQL(strDelete);

        onCreate(db);
    }
}