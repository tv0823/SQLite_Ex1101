package com.example.sqlite_ex1101;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The main activity of the application.
 * Provides buttons to navigate to other activities for adding new employees,
 * adding new companies, creating new orders, and viewing leave requests.
 * Also includes an options menu for navigating to credits, showing all data,
 * sorting data, and viewing leave requests.
 *
 * @author Tal Weintraub <tv0823@bs.amalnet.k12.il>
 * @version 1
 * @since 6/4/2022
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Helper class for managing the SQLite database.
     */
    private HelperDB hlp;
    /**
     * Instance of the SQLite database.
     */
    private SQLiteDatabase db;

    /**
     * Called when the activity is first created. Initializes the layout,
     * creates an instance of the {@link HelperDB}, and opens and closes
     * a readable database connection to ensure database setup.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hlp = new HelperDB(this);
        db = hlp.getReadableDatabase();
        db.close();
    }

    /**
     * Called when the "Add New Employee" button is clicked.
     * Navigates the user to the {@link EmployeeActivity} to add a new employee.
     *
     * @param view The View that was clicked (the "Add New Employee" button).
     */
    public void addNewEmployee(View view) {
        Intent si = new Intent(this, EmployeeActivity.class);
        startActivity(si);
    }

    /**
     * Called when the "Add New Company" button is clicked.
     * Navigates the user to the {@link CompanyActivity} to add a new company.
     *
     * @param view The View that was clicked (the "Add New Company" button).
     */
    public void addNewCompany(View view) {
        Intent si = new Intent(this, CompanyActivity.class);
        startActivity(si);
    }

    /**
     * Called when the "Create Order" button is clicked.
     * Navigates the user to the {@link OrderActivity} to create a new order.
     *
     * @param view The View that was clicked (the "Create Order" button).
     */
    public void createOrder(View view) {
        Intent si = new Intent(this, OrderActivity.class);
        startActivity(si);
    }

    /**
     * Called when the "Leave" button is clicked.
     * Navigates the user to the {@link LeaveActivity} to view leave requests.
     *
     * @param view The View that was clicked (the "Leave" button).
     */
    public void leaveBtn(View view) {
        Intent si = new Intent(this, LeaveActivity.class);
        startActivity(si);
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

        if(id == R.id.menuCred) {
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
