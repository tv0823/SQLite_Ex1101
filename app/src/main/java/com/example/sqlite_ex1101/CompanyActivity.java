package com.example.sqlite_ex1101;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CompanyActivity extends AppCompatActivity {
    EditText companyNum, companyName, mainPhone, secPhone;

    SQLiteDatabase db;
    HelperDB hlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        companyNum = findViewById(R.id.companyNumEt);
        companyName = findViewById(R.id.companyNameEt);
        mainPhone = findViewById(R.id.mainPhoneEt);
        secPhone = findViewById(R.id.secPhoneEt);

        hlp = new HelperDB(this);
    }

    public void createCompanyBtn(View view) {
        String companyNumString = companyNum.getText().toString();
        String companyNameString = companyName.getText().toString();
        String mainPhoneString = mainPhone.getText().toString();
        String secondPhoneString = secPhone.getText().toString();

        if (companyNumString.isEmpty() || companyNameString.isEmpty() || mainPhoneString.isEmpty() || secondPhoneString.isEmpty()) {
            Toast.makeText(this, "Input must have a value", Toast.LENGTH_SHORT).show();
        } else if (InDataBaseFunc.inDataBase(Company.TABLE_COMPANY, companyNumString, Company.COMPANY_NAME, hlp)) {
            Toast.makeText(this, "Company is in the db already!", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues cv = new ContentValues();

            cv.put(Company.COMPANY_ID, companyNumString);
            cv.put(Company.COMPANY_NAME, companyNameString);
            cv.put(Company.MAIN_PHONE, mainPhoneString);
            cv.put(Company.SECOND_PHONE, secondPhoneString);

            db = hlp.getWritableDatabase();
            db.insert(Company.TABLE_COMPANY, null, cv);
            db.close();

            Toast.makeText(this, "Company created successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}