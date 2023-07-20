package com.example.fooddeliveryapp.Activities;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.fooddeliveryapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Register(View view) {
        startActivities(new Intent[] {new Intent(LoginActivity.this, RegistrationActivity.class)});

    }
}