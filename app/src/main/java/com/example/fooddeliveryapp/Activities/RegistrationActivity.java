package com.example.fooddeliveryapp.Activities;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.fooddeliveryapp.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void Login(View view) {
        startActivities(new Intent[] {new Intent(RegistrationActivity.this, LoginActivity.class)});
    }

    public void MainActivity(View view) {
        startActivities(new Intent[] {new Intent(RegistrationActivity.this, MainActivity.class)});
    }
}