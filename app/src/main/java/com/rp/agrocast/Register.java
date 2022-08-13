package com.rp.agrocast;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText et_Name, et_RegNo, et_Email, et_Contact, et_NicNo;
    TextView nav_login;
    Button btn_continue;
    ProgressBar loginProgressbar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String name, registerNo, email, contactNo, nicNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        et_Name = findViewById(R.id.name);
        et_RegNo = findViewById(R.id.regNo);
        et_Email = findViewById(R.id.email);
        et_Contact = findViewById(R.id.contact);
        et_NicNo = findViewById(R.id.nicNo);
        nav_login = findViewById(R.id.navLogin);
        btn_continue = findViewById(R.id.btn_continue);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        loginProgressbar = findViewById(R.id.loginProgressbar);
        loginProgressbar.setVisibility(View.INVISIBLE);


        nav_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLogin = new Intent(Register.this, Login.class);
                startActivity(goToLogin);
            }
        });

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Register.class));
            finish();
        }

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_Name.getText().toString().trim();
                registerNo = et_RegNo.getText().toString().trim();
                email = et_Email.getText().toString().trim();
                contactNo = et_Contact.getText().toString().trim();
                nicNo = et_NicNo.getText().toString().trim();


                if (TextUtils.isEmpty(name)) {
                    et_Name.setError("Name is required.");
                    return;
                } else {
                    if (TextUtils.isEmpty(registerNo)) {
                        et_RegNo.setError("Registration No is required.");
                        return;
                    } else {
                        if (TextUtils.isEmpty(email)) {
                            et_Email.setError("Email No is required.");
                            return;
                        } else {
                            if (TextUtils.isEmpty(contactNo)) {
                                et_Contact.setError("Contact No is required.");
                                return;
                            } else {
                                if (TextUtils.isEmpty(nicNo)) {
                                    et_NicNo.setError("NIC No is required.");
                                    return;
                                } else {
                                    checkRole();
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void checkRole() {

        Intent goToPassword = new Intent(Register.this, Password.class);

        goToPassword.putExtra("name", name);
        goToPassword.putExtra("registerNo", registerNo);
        goToPassword.putExtra("email", email);
        goToPassword.putExtra("contactNo", contactNo);
        goToPassword.putExtra("nicNo", nicNo);


        startActivity(goToPassword);
    }

    }