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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Activity for viewing and deleting employee data from the database.
 * Displays a list of employees in a ListView, allowing the user to select
 * an employee and then delete their record along with any associated orders
 * and meals from the respective tables.
 *
 * @author Tal Weintraub <tv0823@bs.amalnet.k12.il>
 * @version 1
 * @since 30/4/2022
 */
public class LeaveActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    /**
     * ListView widget to display the list of employees.
     */
    ListView lv;
    /**
     * ArrayAdapter for populating the ListView with employee data.
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
     * Integer to store the index of the selected employee in the ListView.
     * Initialized to -1 indicating no selection.
     */
    int selectedIndex = -1;

    /**
     * ArrayList to store formatted strings of employee information for display.
     */
    ArrayList<String> employeesArray,
    /**
     * ArrayList to store the IDs of the employees, corresponding to the items in employeesArray.
     */
    employeeIds;

    /**
     * Called when the activity is first created. Initializes the UI components
     * (ListView), sets up its choice mode and item click listener, and retrieves
     * the list of employees from the database.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        lv = findViewById(R.id.lv);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);

        hlp = new HelperDB(this);

        employeesArray = new ArrayList<>();
        employeeIds = new ArrayList<>();

        getEmployees();
    }

    /**
     * Retrieves all employee records from the database and populates the
     * {@code employeesArray} with formatted employee information and the
     * {@code employeeIds} with the corresponding employee IDs.
     */
    private void getEmployees() {
        db = hlp.getReadableDatabase();
        crsr = db.query(Employee.TABLE_EMPLOYEE, null, null, null, null, null, null);

        employeesArray.clear();
        employeeIds.clear();

        int col1 = crsr.getColumnIndex(Employee.ID);
        int col2 = crsr.getColumnIndex(Employee.COMPANY);
        int col3 = crsr.getColumnIndex(Employee.FIRST_NAME);
        int col4 = crsr.getColumnIndex(Employee.LAST_NAME);
        int col5 = crsr.getColumnIndex(Employee.PHONE_NUM);
        int col6 = crsr.getColumnIndex(Employee.CARD_ID);

        crsr.moveToFirst();
        employeesArray.add("=== Employees ===");
        employeeIds.add("");

        while (!crsr.isAfterLast()) {
            String record =
                    "ID: " + crsr.getString(col1) + "\n" +
                            "Card ID: " + crsr.getString(col6) + "\n" +
                            "Name: " + crsr.getString(col3) + " " + crsr.getString(col4) + "\n" +
                            "Phone: " + crsr.getString(col5) + "\n" +
                            "Company: " + crsr.getString(col2);
            employeesArray.add(record);
            employeeIds.add(crsr.getString(col1));
            crsr.moveToNext();
        }
        crsr.close();
        db.close();

        adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employeesArray);
        lv.setAdapter(adp);

        adp.notifyDataSetChanged();
    }

    /**
     * Callback method to handle the click event on an item in the ListView.
     * Stores the position of the clicked item (employee) in the {@code selectedIndex}.
     * The first item (index 0), which is the header "=== Employees ===",
     * resets the {@code selectedIndex} to -1.
     *
     * @param adapterView The AdapterView where the click happened.
     * @param view        The view within the AdapterView that was clicked (the item view).
     * @param pos         The position of the view in the adapter.
     * @param rowId       The row ID of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long rowId) {
        if (pos == 0) {
            selectedIndex = -1;
        }
        selectedIndex = pos;
    }

    /**
     * Called when the "Delete" button is clicked. Deletes the selected employee's
     * record from the {@link Employee} table, along with any associated orders
     * from the {@link Order} table and their corresponding meals from the {@link Meal} table.
     * After deletion, it refreshes the list of employees displayed in the ListView
     * and resets the {@code selectedIndex}.
     *
     * @param view The View that was clicked (the "Delete" button).
     */
    public void deleteData(View view) {
        if (selectedIndex <= 0 || selectedIndex >= employeeIds.size()) return;

        String employeeId = employeeIds.get(selectedIndex);
        db = hlp.getWritableDatabase();

        crsr = db.query(Order.TABLE_ORDER, new String[]{Order.MEAL_ID}, Order.WORKER_ID + "=?", new String[]{employeeId}, null, null, null
        );

        ArrayList<String> mealIds = new ArrayList<>();
        int mealIdCol = crsr.getColumnIndex(Order.MEAL_ID);
        while (crsr.moveToNext()) {
            mealIds.add(crsr.getString(mealIdCol));
        }
        crsr.close();

        //delete all data that is connected to the Employee that was selected
        for (int i = 0; i < mealIds.size(); i++) {
            db.delete(Meal.TABLE_MEAL, Meal.MEAL_ID + "=?", new String[]{mealIds.get(i)});
        }
        db.delete(Order.TABLE_ORDER, Order.WORKER_ID + "=?", new String[]{employeeId});
        db.delete(Employee.TABLE_EMPLOYEE, Employee.ID + "=?", new String[]{employeeId});

        db.close();
        getEmployees();
        selectedIndex = -1;
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
        else if (id == R.id.menuShow) {
            Intent si = new Intent(this, ShowAllDataActivity.class);
            startActivity(si);
        }
        else if (id == R.id.menuSort) {
            Intent si = new Intent(this, SortActivity.class);
            startActivity(si);
        }

        return true;
    }
}
