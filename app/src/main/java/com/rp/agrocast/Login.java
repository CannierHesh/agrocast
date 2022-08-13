package com.rp.agrocast;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText et_email, et_password;
    TextView nav_signup;
    Button btn_login;
    ProgressBar loginProgressbar;
    FirebaseAuth fAuth;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        nav_signup = findViewById(R.id.nav_signup);
        btn_login = findViewById(R.id.btn_login);

        loginProgressbar = findViewById(R.id.loginProgressbar);
        loginProgressbar.setVisibility(View.INVISIBLE);

        fAuth = FirebaseAuth.getInstance();

        nav_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgressbar.setVisibility(View.VISIBLE);
                email = et_email.getText().toString().trim();
                password = et_password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    et_email.setError("Email is required.");
                    return;
                } else {
                    if (TextUtils.isEmpty(password)) {
                        et_password.setError("Email is required.");
                        return;
                    } else {
                        login();
                    }
                }
            }
        });
    }

    public void login() {
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    loginProgressbar.setVisibility(View.INVISIBLE);
                } else {
                    loginProgressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "Error !!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}