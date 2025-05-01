package com.example.sqlite_ex1101;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Activity for displaying all data from selected tables in the database.
 * Allows the user to choose between viewing employees, companies, meals, or orders
 * using a Spinner. The selected data is then displayed in a ListView.
 *
 * @author Tal Weintraub <tv0823@bs.amalnet.k12.il>
 * @version 1
 * @since 30/4/2022
 */
public class ShowAllDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * Spinner widget for selecting the table to display.
     */
    Spinner options;
    /**
     * ListView widget for displaying the data from the selected table.
     */
    ListView lv;
    /**
     * ArrayAdapter for populating the ListView with String data.
     */
    ArrayAdapter<String> adp;
    /**
     * SQLiteDatabase instance for interacting with the database.
     */
    SQLiteDatabase db;
    /**
     * HelperDB instance for managing the SQLite database.
     */
    HelperDB hlp;
    /**
     * Cursor for iterating over the rows returned from database queries.
     */
    Cursor crsr;

    /**
     * Array of strings containing the names of the tables to display in the Spinner.
     */
    String[] allOptions = {"employees", "companies", "meals", "orders"};
    /**
     * ArrayList to store employee data retrieved from the database.
     */
    ArrayList<String> employeesArray,
    /**
     * ArrayList to store company data retrieved from the database.
     */
    companiesArray,
    /**
     * ArrayList to store meal data retrieved from the database.
     */
    mealsArray,
    /**
     * ArrayList to store order data retrieved from the database.
     */
    ordersArray;

    /**
     * Called when the activity is first created. Initializes the UI components,
     * sets up the Spinner with table options, and retrieves data from all tables.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_data);

        options = findViewById(R.id.options);
        lv = findViewById(R.id.lv);

        options.setOnItemSelectedListener(this);

        ArrayAdapter<String> adp = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, allOptions);
        options.setAdapter(adp);

        hlp = new HelperDB(this);

        employeesArray = new ArrayList<>();
        companiesArray = new ArrayList<>();
        mealsArray = new ArrayList<>();
        ordersArray = new ArrayList<>();

        getEmployees();
        getCompanies();
        getMeals();
        getOrders();
    }

    /**
     * Retrieves all employee records from the database and populates the {@code employeesArray}.
     * Each employee's information (ID, Card ID, Name, Phone, Company) is formatted
     * into a single string and added to the list.
     */
    private void getEmployees() {
        db = hlp.getReadableDatabase();
        crsr = db.query(Employee.TABLE_EMPLOYEE, null, null, null, null, null, null);

        int col1 = crsr.getColumnIndex(Employee.ID);
        int col2 = crsr.getColumnIndex(Employee.COMPANY);
        int col3 = crsr.getColumnIndex(Employee.FIRST_NAME);
        int col4 = crsr.getColumnIndex(Employee.LAST_NAME);
        int col5 = crsr.getColumnIndex(Employee.PHONE_NUM);
        int col6 = crsr.getColumnIndex(Employee.CARD_ID);

        crsr.moveToFirst();
        employeesArray.add("=== Employees ===");
        while (!crsr.isAfterLast()) {
            String record =
                    "ID: " + crsr.getString(col1) + "\n" +
                            "Card ID: " + crsr.getString(col6) + "\n" +
                            "Name: " + crsr.getString(col3) + " " + crsr.getString(col4) + "\n" +
                            "Phone: " + crsr.getString(col5) + "\n" +
                            "Company: " + crsr.getString(col2);
            employeesArray.add(record);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
    }

    /**
     * Retrieves all company records from the database and populates the {@code companiesArray}.
     * Each company's information (ID, Name, Phone 1, Phone 2) is formatted
     * into a single string and added to the list.
     */
    private void getCompanies() {
        db = hlp.getReadableDatabase();
        crsr = db.query(Company.TABLE_COMPANY, null, null, null, null, null, null);

        int col1 = crsr.getColumnIndex(Company.COMPANY_ID);
        int col2 = crsr.getColumnIndex(Company.COMPANY_NAME);
        int col3 = crsr.getColumnIndex(Company.MAIN_PHONE);
        int col4 = crsr.getColumnIndex(Company.SECOND_PHONE);

        crsr.moveToFirst();
        companiesArray.add("=== Companies ===");
        while (!crsr.isAfterLast()) {
            String record =
                    "ID: " + crsr.getString(col1) + "\n" +
                            "Name: " + crsr.getString(col2) + "\n" +
                            "Phone 1: " + crsr.getString(col3) + "\n" +
                            "Phone 2: " + crsr.getString(col4);
            companiesArray.add(record);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
    }

    /**
     * Retrieves all meal records from the database and populates the {@code mealsArray}.
     * Each meal's information (Meal ID, First Meal, Main Course, Extra, Dessert, Drink)
     * is formatted into a single string and added to the list.
     */
    private void getMeals() {
        db = hlp.getReadableDatabase();
        crsr = db.query(Meal.TABLE_MEAL, null, null, null, null, null, null);

        int col1 = crsr.getColumnIndex(Meal.MEAL_ID);
        int col2 = crsr.getColumnIndex(Meal.FIRST_MEAL);
        int col3 = crsr.getColumnIndex(Meal.MAIN_COURSE);
        int col4 = crsr.getColumnIndex(Meal.EXTRA);
        int col5 = crsr.getColumnIndex(Meal.DESSERT);
        int col6 = crsr.getColumnIndex(Meal.DRINK);

        crsr.moveToFirst();
        mealsArray.add("=== Meals ===");
        while (!crsr.isAfterLast()) {
            String record =
                    "Meal ID: " + crsr.getString(col1) + "\n" +
                            "First Meal: " + crsr.getString(col2) + "\n" +
                            "Main Course: " + crsr.getString(col3) + "\n" +
                            "Extra: " + crsr.getString(col4) + "\n" +
                            "Dessert: " + crsr.getString(col5) + "\n" +
                            "Drink: " + crsr.getString(col6);
            mealsArray.add(record);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
    }

    /**
     * Retrieves all order records from the database and populates the {@code ordersArray}.
     * Each order's information (Date, Time, Worker ID, Meal ID, Company) is formatted
     * into a single string and added to the list.
     */
    private void getOrders() {
        db = hlp.getReadableDatabase();
        crsr = db.query(Order.TABLE_ORDER, null, null, null, null, null, null);

        int col1 = crsr.getColumnIndex(Order.DATE);
        int col2 = crsr.getColumnIndex(Order.TIME);
        int col3 = crsr.getColumnIndex(Order.WORKER_ID);
        int col4 = crsr.getColumnIndex(Order.MEAL_ID);
        int col5 = crsr.getColumnIndex(Order.COMPANY);

        crsr.moveToFirst();
        ordersArray.add("=== Orders ===");
        while (!crsr.isAfterLast()) {
            String record =
                    "Date: " + crsr.getString(col1) + "\n" +
                            "Time: " + crsr.getString(col2) + "\n" +
                            "Worker ID: " + crsr.getString(col3) + "\n" +
                            "Meal ID: " + crsr.getString(col4) + "\n" +
                            "Company: " + crsr.getString(col5);
            ordersArray.add(record);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
    }

    /**
     * Callback method to handle the selection of an item in the Spinner.
     * Based on the selected position, it sets the ArrayAdapter for the ListView
     * to display the corresponding data array (employees, companies, meals, or orders).
     *
     * @param adapterView The AdapterView where the selection happened.
     * @param view        The view within the AdapterView that was clicked.
     * @param pos         The position of the view in the adapter.
     * @param l           The row ID of the item that is selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        switch (pos) {
            case 0:
                adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employeesArray);
                break;
            case 1:
                adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, companiesArray);
                break;
            case 2:
                adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mealsArray);
                break;
            case 3:
                adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ordersArray);
                break;
        }
        lv.setAdapter(adp);
    }

    /**
     * Callback method to handle the event when no item is selected in the Spinner.
     * Currently, it does nothing.
     *
     * @param adapterView The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }

    /**
     * Inflates the options menu. This adds items to the action bar if it is present.
     *
     * @param menu The menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handles item selections in the options menu.
     * Navigates to different activities based on the selected menu item.
     *
     * @param item The menu item that was selected.
     * @return Return false to allow normal menu processing to
     * proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuHome) {
            Intent si = new Intent(this, MainActivity.class);
            startActivity(si);
        }
        else if(id == R.id.menuCred) {
            Intent si = new Intent(this, CreditsActivity.class);
            startActivity(si);
        }
        else if (id == R.id.menuSort) {
            Intent si = new Intent(this, SortActivity.class);
            startActivity(si);
        }
        else if (id == R.id.menuLeave) {
            Intent si = new Intent(this, LeaveActivity.class);
            startActivity(si);
        }

        return true;
    }
}
