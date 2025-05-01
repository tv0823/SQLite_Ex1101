package com.example.sqlite_ex1101;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Activity for creating new orders in the database.
 * Allows the user to select a worker ID and company name from Spinners,
 * enter meal details in an AlertDialog, and then saves the order
 * along with the meal details to the respective tables in the database.
 *
 * @author Tal Weintraub <tv0823@bs.amalnet.k12.il>
 * @version 1
 * @since 18/4/2022
 */
public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * Spinner widget for selecting the worker's ID.
     */
    Spinner workerID,
    /**
     * Spinner widget for selecting the company name.
     */
    companiesNames;
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
     * AlertDialog builder for creating the dialog to input meal details.
     */
    AlertDialog.Builder adb;
    /**
     * String to store the selected company ID.
     */
    String companyID,
    /**
     * String to store the selected worker ID.
     */
    workerId;
    /**
     * ArrayList to store the worker IDs retrieved from the database for the workerID Spinner.
     */
    ArrayList<String> workersID = new ArrayList<>();
    /**
     * ArrayAdapter for populating the workerID Spinner with worker IDs.
     */
    ArrayAdapter<String> idsAdp;
    /**
     * HashMap to store company names as keys and their corresponding IDs as values
     * for the companiesNames Spinner.
     */
    HashMap<String, Integer> companies = new HashMap<>();
    /**
     * ArrayAdapter for populating the companiesNames Spinner with company names.
     */
    ArrayAdapter<String> companiesAdp;

    /**
     * Called when the activity is first created. Initializes the UI components
     * (Spinners), sets up their item selection listeners, and retrieves data
     * to populate the Spinners.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        workerID = findViewById(R.id.workerID);
        companiesNames = findViewById(R.id.companiesNames);

        workerID.setOnItemSelectedListener(this);
        companiesNames.setOnItemSelectedListener(this);

        hlp = new HelperDB(this);

        getSpinnersData();
    }

    /**
     * Retrieves worker IDs and company names from the database to populate
     * the respective Spinners. Worker IDs are fetched from the {@link Employee} table,
     * and company names along with their IDs are fetched from the {@link Company} table.
     */
    private void getSpinnersData() {
        db = hlp.getReadableDatabase();
        crsr = db.query(Employee.TABLE_EMPLOYEE, new String[]{Employee.ID}, null, null, null, null, Employee.ID + " DESC", null);

        int col1 = crsr.getColumnIndex(Employee.ID);

        crsr.moveToFirst();
        workersID.add("your id");
        while (!crsr.isAfterLast()) {
            workersID.add(crsr.getString(col1));
            crsr.moveToNext();
        }
        crsr.close();

        crsr = db.query(Company.TABLE_COMPANY, new String[]{Company.COMPANY_ID, Company.COMPANY_NAME}, null, null, null, null, Company.COMPANY_NAME + " DESC", null);
        col1 = crsr.getColumnIndex(Company.COMPANY_ID);
        int col2 = crsr.getColumnIndex(Company.COMPANY_NAME);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            companies.put(crsr.getString(col2), crsr.getInt(col1));
            crsr.moveToNext();
        }
        crsr.close();
        db.close();

        idsAdp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, workersID);
        workerID.setAdapter(idsAdp);

        ArrayList<String> companiesNamesList = new ArrayList<>(companies.keySet());
        companiesNamesList.add(0, "company name");
        companiesAdp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, companiesNamesList);
        this.companiesNames.setAdapter(companiesAdp);
    }

    /**
     * Called when the "Make Order" button is clicked. It checks if a worker and company
     * have been selected. If so, it displays an AlertDialog to get the meal details
     * from the user.
     *
     * @param view The View that was clicked (the "Make Order" button).
     */
    public void makeOrderBtn(View view) {
        if (workerId == null || companyID == null) {
            Toast.makeText(this, "There are no worker or company selected", Toast.LENGTH_SHORT).show();
        } else {
            adb = new AlertDialog.Builder(this);
            adb.setTitle("Meal info");

            final EditText firstMeal = new EditText(this);
            final EditText mainCourse = new EditText(this);
            final EditText extra = new EditText(this);
            final EditText dessert = new EditText(this);
            final EditText drink = new EditText(this);

            firstMeal.setHint("first meal");
            mainCourse.setHint("main course");
            extra.setHint("extra");
            dessert.setHint("dessert");
            drink.setHint("drink");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            layout.addView(firstMeal);
            layout.addView(mainCourse);
            layout.addView(extra);
            layout.addView(dessert);
            layout.addView(drink);

            adb.setView(layout);

            adb.setPositiveButton("buy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (TextUtils.isEmpty(firstMeal.getText().toString())
                            || TextUtils.isEmpty(mainCourse.getText().toString())
                            || TextUtils.isEmpty(extra.getText().toString())
                            || TextUtils.isEmpty(dessert.getText().toString())
                            || TextUtils.isEmpty(drink.getText().toString()))
                    {
                        Toast.makeText(OrderActivity.this, "Meal must have value", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        addOrderToDB(firstMeal.getText().toString(),
                                mainCourse.getText().toString(),
                                extra.getText().toString(),
                                dessert.getText().toString(),
                                drink.getText().toString());
                        Toast.makeText(OrderActivity.this, "Order completed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            adb.show();
        }
    }

    /**
     * Adds a new order to the database. First, it inserts the meal details into the
     * {@link Meal} table and retrieves the generated meal ID. Then, it inserts
     * the order details (date, time, worker ID, meal ID, company ID) into the
     * {@link Order} table. The date and time are recorded in the Israel time zone.
     *
     * @param firstMeal  The first meal selected by the user.
     * @param mainMeal   The main course selected by the user.
     * @param extra      Any extra items selected by the user.
     * @param dessert    The dessert selected by the user.
     * @param drink      The drink selected by the user.
     */
    private void addOrderToDB(String firstMeal, String mainMeal, String extra, String dessert, String drink)
    {
        // insert meal to db
        ContentValues cv = new ContentValues();

        cv.put(Meal.FIRST_MEAL, firstMeal);
        cv.put(Meal.MAIN_COURSE, mainMeal);
        cv.put(Meal.EXTRA, extra);
        cv.put(Meal.DESSERT, dessert);
        cv.put(Meal.DRINK, drink);

        db = hlp.getWritableDatabase();
        int mealId = (int) db.insert(Meal.TABLE_MEAL, null, cv);
        db.close();

        // insert order to db
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Use Israel's time zone to format the date in
        df.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        String[] timeDate = df.format(date).split(" ");

        cv = new ContentValues();

        cv.put(Order.DATE, timeDate[0]);
        cv.put(Order.TIME, timeDate[1]);
        cv.put(Order.WORKER_ID, workerId);
        cv.put(Order.MEAL_ID, mealId);
        cv.put(Order.COMPANY, companyID);

        db = hlp.getWritableDatabase();
        db.insert(Order.TABLE_ORDER, null, cv);
        db.close();
    }

    /**
     * Callback method to handle the selection of an item in either of the Spinners
     * (workerID or companiesNames). It updates the {@code workerId} or {@code companyID}
     * based on the selected item. The first item in each Spinner is treated as a
     * placeholder ("your id" for workerID and "company name" for companiesNames),
     * and selecting it sets the corresponding ID to null.
     *
     * @param adapterView The AdapterView where the selection happened.
     * @param view        The view within the AdapterView that was clicked.
     * @param pos         The position of the view in the adapter.
     * @param rowId       The row ID of the item that is selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long rowId) {
        if (adapterView.getAdapter().getItem(0).equals("your id"))
        {
            if (idsAdp.getItem(pos).equals("your id"))
                workerId = null;
            else
                workerId = idsAdp.getItem(pos);
        }
        else {
            if (companiesAdp.getItem(pos).equals("company name"))
                companyID = null;
            else
                companyID = String.valueOf(companies.get(companiesAdp.getItem(pos)));
        }
    }

    /**
     * Callback method to handle the event when no item is selected in either Spinner.
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
        else if (id == R.id.menuShow) {
            Intent si = new Intent(this, ShowAllDataActivity.class);
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
