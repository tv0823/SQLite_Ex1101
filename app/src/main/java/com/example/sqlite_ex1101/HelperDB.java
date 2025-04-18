package com.example.sqlite_ex1101;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.sqlite_ex1101.Company.COMPANY_ID;
import static com.example.sqlite_ex1101.Company.MAIN_PHONE;
import static com.example.sqlite_ex1101.Company.COMPANY_NAME;
import static com.example.sqlite_ex1101.Company.SECOND_PHONE;
import static com.example.sqlite_ex1101.Company.TABLE_COMPANY;

import static com.example.sqlite_ex1101.Employee.CARD_ID;
import static com.example.sqlite_ex1101.Meal.DESSERT;
import static com.example.sqlite_ex1101.Meal.FIRST_MEAL;
import static com.example.sqlite_ex1101.Employee.COMPANY;
import static com.example.sqlite_ex1101.Employee.ID;
import static com.example.sqlite_ex1101.Employee.FIRST_NAME;
import static com.example.sqlite_ex1101.Employee.LAST_NAME;
import static com.example.sqlite_ex1101.Employee.PHONE_NUM;
import static com.example.sqlite_ex1101.Employee.TABLE_EMPLOYEE;

import static com.example.sqlite_ex1101.Meal.DRINK;
import static com.example.sqlite_ex1101.Meal.EXTRAS;
import static com.example.sqlite_ex1101.Meal.MAIN_COURSE;
import static com.example.sqlite_ex1101.Meal.TABLE_MEAL;
import static com.example.sqlite_ex1101.Meal.MEAL_ID;

import static com.example.sqlite_ex1101.Order.DATE;
import static com.example.sqlite_ex1101.Order.TABLE_ORDER;
import static com.example.sqlite_ex1101.Order.HOUR;
import static com.example.sqlite_ex1101.Order.ORDER_ID;
import static com.example.sqlite_ex1101.Order.WORKER_ID;

public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbGYNbus.db";
    private static final int DATABASE_VERSION = 1;
    String strCreate, strDelete;

    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        strCreate = "CREATE TABLE " + TABLE_EMPLOYEE;
        strCreate += " ("+ID+" INTEGER PRIMARY KEY,";
        strCreate += " "+COMPANY+" TEXT,";
        strCreate += " "+FIRST_NAME+" TEXT,";
        strCreate += " "+LAST_NAME+" TEXT,";
        strCreate += " "+PHONE_NUM+" TEXT,";
        strCreate += " "+CARD_ID+" TEXT";
        strCreate += ");";
        db.execSQL(strCreate);

        strCreate = "CREATE TABLE " + TABLE_COMPANY;
        strCreate += " ("+COMPANY_ID+" INTEGER PRIMARY KEY,";
        strCreate += " "+COMPANY_NAME+" TEXT,";
        strCreate += " "+MAIN_PHONE+" TEXT,";
        strCreate += " "+SECOND_PHONE+" TEXT";
        strCreate += ");";
        db.execSQL(strCreate);

        strCreate = "CREATE TABLE " + TABLE_MEAL;
        strCreate += " ("+MEAL_ID+" INTEGER PRIMARY KEY,";
        strCreate += " "+FIRST_MEAL+" TEXT,";
        strCreate += " "+MAIN_COURSE+" TEXT,";
        strCreate += " "+EXTRAS+" TEXT,";
        strCreate += " "+DESSERT+" TEXT,";
        strCreate += " "+DRINK+" TEXT";
        strCreate += ");";
        db.execSQL(strCreate);

        strCreate = "CREATE TABLE " + TABLE_ORDER;
        strCreate += " ("+ORDER_ID+" INTEGER PRIMARY KEY,";
        strCreate += " "+DATE+" TEXT,";
        strCreate += " "+HOUR+" INTEGER,";
        strCreate += " "+WORKER_ID+" TEXT,";
        strCreate += " "+Order.MEAL_ID+" INTEGER";
        strCreate += " "+Order.COMPANY+" TEXT";
        strCreate += ");";
        db.execSQL(strCreate);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        strDelete = "DROP TABLE IF EXISTS "+ TABLE_EMPLOYEE;
        db.execSQL(strDelete);

        strDelete = "DROP TABLE IF EXISTS "+ TABLE_COMPANY;
        db.execSQL(strDelete);

        strDelete = "DROP TABLE IF EXISTS "+ TABLE_MEAL;
        db.execSQL(strDelete);
        
        strDelete = "DROP TABLE IF EXISTS "+ TABLE_ORDER;
        db.execSQL(strDelete);

        onCreate(db);
    }
}
