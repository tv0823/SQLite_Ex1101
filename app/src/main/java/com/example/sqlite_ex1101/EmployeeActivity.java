package com.example.sqlite_ex1101;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for creating new employee records and adding them to the database.
 * Provides input fields for employee details such as card ID, first name, last name,
 * company, employee ID, and phone number. Includes validation for the employee ID
 * and checks for existing employee records before adding a new one.
 *
 * @author Tal Weintraub <tv0823@bs.amalnet.k12.il>
 * @version 1
 * @since 18/4/2022
 */
public class EmployeeActivity extends AppCompatActivity {
    /**
     * EditText field for entering the employee's card ID.
     */
    EditText cardId;
    /**
     * EditText field for entering the employee's first name.
     */
    EditText firstName;
    /**
     * EditText field for entering the employee's last name.
     */
    EditText lastName;
    /**
     * EditText field for entering the employee's company.
     */
    EditText company;
    /**
     * EditText field for entering the employee's unique ID.
     */
    EditText id;
    /**
     * EditText field for entering the employee's phone number.
     */
    EditText phoneNum;

    /**
     * Instance of the SQLiteDatabase for database operations.
     */
    SQLiteDatabase db;
    /**
     * Instance of the HelperDB class for managing the SQLite database.
     */
    HelperDB hlp;

    /**
     * Called when the activity is first created. Initializes the layout and
     * associates the EditText fields with their corresponding UI elements.
     * Also initializes the {@link HelperDB} and gets a readable database instance
     * which is immediately closed.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        cardId = findViewById(R.id.cardIdEt);
        firstName = findViewById(R.id.firstNameEt);
        lastName = findViewById(R.id.lastNameEt);
        company = findViewById(R.id.secPhoneEt);
        id = findViewById(R.id.idEt);
        phoneNum = findViewById(R.id.phoneNumEt);

        hlp = new HelperDB(this);
        db = hlp.getReadableDatabase();
        db.close();
    }

    /**
     * Handles the click event of the "Create Employee" button.
     * Retrieves the text from the input fields, validates the ID format,
     * checks if an employee with the same ID or phone number already exists
     * in the database, and if all checks pass, inserts a new employee record
     * into the {@link Employee#TABLE_EMPLOYEE} table.
     *
     * @param view The View that triggered the action (the "Create Employee" button).
     */
    public void createEmployeeBtn(View view) {
        String cardIdString = cardId.getText().toString();
        String firstNameString = firstName.getText().toString();
        String lastNameString = lastName.getText().toString();
        String companyString = company.getText().toString();
        String idString = id.getText().toString();
        String phoneString = phoneNum.getText().toString();

        if (cardIdString.isEmpty() || firstNameString.isEmpty() || lastNameString.isEmpty() || companyString.isEmpty() || idString.isEmpty() || phoneString.isEmpty()) {
            Toast.makeText(this, "Input must have a value", Toast.LENGTH_SHORT).show();
        } else if (!validId(idString)) {
            Toast.makeText(this, "Invalid ID", Toast.LENGTH_SHORT).show();
        } else if (InDataBaseFunc.inDataBase(Employee.TABLE_EMPLOYEE, phoneString, Employee.PHONE_NUM, hlp) || InDataBaseFunc.inDataBase(Employee.TABLE_EMPLOYEE, idString, Employee.ID, hlp)) {
            Toast.makeText(this, "Employee is in the db already!", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues cv = new ContentValues();

            cv.put(Employee.CARD_ID, cardIdString);
            cv.put(Employee.FIRST_NAME, firstNameString);
            cv.put(Employee.LAST_NAME, lastNameString);
            cv.put(Employee.COMPANY, companyString);
            cv.put(Employee.ID, idString);
            cv.put(Employee.PHONE_NUM, phoneString);

            db = hlp.getWritableDatabase();
            db.insert(Employee.TABLE_EMPLOYEE, null, cv);
            db.close();

            Toast.makeText(this, "Employee created successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Validates an Israeli ID number using a standard checksum algorithm.
     * The algorithm involves multiplying digits by 1 or 2 based on their position,
     * summing the results (handling double-digit results by subtracting 9), and
     * checking if the final sum is divisible by 10.
     *
     * @param id The ID string to validate.
     * @return {@code true} if the ID is a valid Israeli ID, {@code false} otherwise.
     */
    public boolean validId(String id) {
        int sumIdNumbers = 0;
        int currDigit = 0;

        if(id.equals("") || id.length() > 9) {
            return false;
        } else {
            char[] idChars = String.format("%09d", Integer.parseInt(id)).toCharArray();

            for (int i = 0; i < 9; i++) {
                currDigit = Character.getNumericValue(idChars[i]);
                currDigit *= (i % 2) + 1;

                if (currDigit > 9) {
                    currDigit -= 9;
                }

                sumIdNumbers += currDigit;
            }
            return sumIdNumbers % 10 == 0;
        }
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
