package com.example.sqlite_ex1101;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeActivity extends AppCompatActivity {
    EditText cardId, firstName, lastName, company, id, phoneNum;

    SQLiteDatabase db;
    HelperDB hlp;

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
    }

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
}