package com.example.sqlite_ex1101;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

/**
 * The type Sort activity.
 * This activity displays a list of employees based on different sorting and filtering options.
 * It provides options to show employee IDs, names, or a detailed list of employees sorted by ID.
 *
 * @author Tal Weintraub <tv0823@bs.amalnet.k12.il>
 * @version 1
 * @since 30/4/2022
 */
public class SortActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * The List view.
     */
    ListView lv;
    /**
     * The Options spinner.
     */
    Spinner options;
    /**
     * The Adp array adapter.
     */
    ArrayAdapter<String> adp;

    /**
     * The Database.
     */
    SQLiteDatabase db;
    /**
     * The Hlp HelperDB.
     */
    HelperDB hlp;
    /**
     * The Crsr cursor.
     */
    Cursor crsr;

    /**
     * The All options to show in the spinner.
     */
    String[] allOptions = {"show just ids", "show just names", "order Employees by id"};
    /**
     * The Ids array list.
     */
    ArrayList<String> idsArray;
    /**
     * The Names array list.
     */
    ArrayList<String> namesArray;
    /**
     * The Order employees array list.
     */
    ArrayList<String> orderEmployeesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        options = findViewById(R.id.options);
        lv = findViewById(R.id.lv);

        options.setOnItemSelectedListener(this);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, allOptions);
        options.setAdapter(adp);

        hlp = new HelperDB(this);

        idsArray = new ArrayList<>();
        namesArray = new ArrayList<>();
        orderEmployeesArray = new ArrayList<>();

        getIds();
        getNames();
        getOrderedEmployees();
    }

    /**
     * Get all id employees from the db.
     */
    private void getIds() {
        db = hlp.getReadableDatabase();
        crsr = db.query(Employee.TABLE_EMPLOYEE, new String[]{Employee.ID}, null, null, null, null, null, null);

        int col1 = crsr.getColumnIndex(Employee.ID);

        crsr.moveToFirst();
        idsArray.add("id");
        while (!crsr.isAfterLast()) {
            idsArray.add(crsr.getString(col1));
            crsr.moveToNext();
        }
        crsr.close();
    }

    /**
     * Get all names employees from db.
     */
    private void getNames() {
        db = hlp.getReadableDatabase();
        crsr = db.query(Employee.TABLE_EMPLOYEE, new String[]{Employee.FIRST_NAME, Employee.LAST_NAME}, null, null, null, null, null, null);

        int col1 = crsr.getColumnIndex(Employee.FIRST_NAME);
        int col2 = crsr.getColumnIndex(Employee.LAST_NAME);

        crsr.moveToFirst();
        namesArray.add("firstName lastName");
        while (!crsr.isAfterLast()) {
            namesArray.add(crsr.getString(col1) + " " + crsr.getString(col2));
            crsr.moveToNext();
        }
        crsr.close();
    }

    /**
     * Get all the id employees from the db sorted.
     */
    private void getOrderedEmployees() {
        db = hlp.getReadableDatabase();
        crsr = db.query(Employee.TABLE_EMPLOYEE, null, null, null, null, null, Employee.ID, null);

        int col1 = crsr.getColumnIndex(Employee.ID);
        int col2 = crsr.getColumnIndex(Employee.COMPANY);
        int col3 = crsr.getColumnIndex(Employee.FIRST_NAME);
        int col4 = crsr.getColumnIndex(Employee.LAST_NAME);
        int col5 = crsr.getColumnIndex(Employee.PHONE_NUM);
        int col6 = crsr.getColumnIndex(Employee.CARD_ID);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            String record =
                    "ID: " + crsr.getString(col1) + "\n" +
                            "Card ID: " + crsr.getString(col6) + "\n" +
                            "Name: " + crsr.getString(col3) + " " + crsr.getString(col4) + "\n" +
                            "Phone: " + crsr.getString(col5) + "\n" +
                            "Company: " + crsr.getString(col2);
            orderEmployeesArray.add(record);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
    }

    /**
     * Called when an item in the spinner is selected.
     * This method updates the ListView with the appropriate data based on the selected option.
     *
     * @param adapterView The AdapterView where the selection happened.
     * @param view        The view within the AdapterView that was clicked.
     * @param pos         The position of the view in the adapter.
     * @param l           The row ID of the item that was selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        switch (pos) {
            case 0:
                adp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, idsArray);
                break;
            case 1:
                adp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, namesArray);
                break;
            case 2:
                adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderEmployeesArray);
                break;
        }
        lv.setAdapter(adp);
    }

    /**
     * Called when nothing is selected in the spinner.
     *
     * @param adapterView The AdapterView where nothing is selected.
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * Create the options menu.
     *
     * @param menu The menu.
     * @return True if success.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Go where clicked.
     *
     * @param item The item in menu that was clicked.
     * @return True if success.
     */
    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuHome) {
            Intent si = new Intent(this, MainActivity.class);
            startActivity(si);
        } else if (id == R.id.menuCred) {
            Intent si = new Intent(this, CreditsActivity.class);
            startActivity(si);
        } else if (id == R.id.menuShow) {
            Intent si = new Intent(this, ShowAllDataActivity.class);
            startActivity(si);
        } else if (id == R.id.menuLeave) {
            Intent si = new Intent(this, LeaveActivity.class);
            startActivity(si);
        }

        return true;
    }
}