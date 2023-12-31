package com.example.fooddeliveryapp.Activities;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.fooddeliveryapp.Activities.LoginSignin.LoginActivity;
import com.example.fooddeliveryapp.Activities.LoginSignin.RegistrationActivity;
import com.example.fooddeliveryapp.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
    }

    public void Register(View view) {
        startActivities(new Intent[]{new Intent(WelcomeActivity.this, RegistrationActivity.class)});
    }

    public void Login(View view) {
        startActivities(new Intent[] {new Intent(WelcomeActivity.this, LoginActivity.class)});
    }

}