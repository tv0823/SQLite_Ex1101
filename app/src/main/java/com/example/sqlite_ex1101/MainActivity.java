package com.example.sqlite_ex1101;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addNewEmployee(View view) {
        Intent si = new Intent(this, EmployeeActivity.class);
        startActivity(si);
    }

    public void addNewCompany(View view) {
        Intent si = new Intent(this, CompanyActivity.class);
        startActivity(si);
    }

    public void createOrder(View view) {
    }
}