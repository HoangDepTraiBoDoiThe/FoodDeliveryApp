package com.example.fooddeliveryapp.Activities.LoginSignin;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddeliveryapp.Activities.HomePage.HomePage;
import com.example.fooddeliveryapp.Models.Cart.CartModel;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.ui.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword, edtAddress, edtPhoneNumb;
    Button btnReg;
    FirebaseAuth mAuthReg;
    TextView txvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuthReg = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edtEmailReg);
        edtPassword = findViewById(R.id.edtPasswordReg);
        edtAddress = findViewById(R.id.edtAddressReg);
        edtPhoneNumb = findViewById(R.id.edtPhoneReg);
        btnReg = findViewById(R.id.btnReg);
        txvLog = findViewById(R.id.txvLog);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("users");
        DatabaseReference cartsRef = firebaseDatabase.getReference("carts");

        txvLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, phoneN, address;
                email = String.valueOf(edtEmail.getText().toString());
                password = String.valueOf(edtPassword.getText().toString());
                phoneN = String.valueOf(edtAddress.getText().toString());
                address = String.valueOf(edtPhoneNumb.getText().toString());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegistrationActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistrationActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phoneN)) {
                    Toast.makeText(RegistrationActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(RegistrationActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuthReg.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override

                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();

                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    UserModel user = new UserModel(phoneN, address);
                                    CartModel cartModel = new CartModel(userId);
                                    usersRef.child(userId).setValue(user);
                                    cartsRef.child(userId).setValue(cartModel);
                                }
                                else {
                                    Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}