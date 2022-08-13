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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Password extends AppCompatActivity {
    Button btn_continue;
    EditText et_password, et_confPassword;
    String name, regNo, email, contact, nicNo, password, confPassword, userID, ownsFarmland, farmlandName, location, size, farmingType, issigationMethod, liveStock;
    ProgressBar loginProgressbar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_password);

        btn_continue = findViewById(R.id.btn_continue);
        et_password = findViewById(R.id.password);
        et_confPassword = findViewById(R.id.conf_password);

        loginProgressbar = findViewById(R.id.loginProgressbar);
        loginProgressbar.setVisibility(View.INVISIBLE);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Intent getRegData = getIntent();
        name = getRegData.getStringExtra("name");
        regNo = getRegData.getStringExtra("registerNo");
        email = getRegData.getStringExtra("email");
        contact = getRegData.getStringExtra("contactNo");
        nicNo = getRegData.getStringExtra("nicNo");
        ownsFarmland = getRegData.getStringExtra("ownsFarmland");
        farmlandName = getRegData.getStringExtra("farmlandName");
        location = getRegData.getStringExtra("location");
        size = getRegData.getStringExtra("size");
        farmingType = getRegData.getStringExtra("farmingType");
        issigationMethod = getRegData.getStringExtra("issigationMethod");
        liveStock = getRegData.getStringExtra("liveStock");

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                password = et_password.getText().toString().trim();
                confPassword = et_confPassword.getText().toString().trim();

                if (TextUtils.isEmpty(password)) {
                    et_password.setError("Password is required.");
                    return;
                } else {
                    if (password.length() < 6) {
                        et_password.setError("Password must be more than 6 characters.");
                    } else {
                        if (!confPassword.equals(password)) {
                            et_confPassword.setError("Password not match.");
                        } else {
                            RegisterUser();
                        }
                    }
                }

            }
        });
    }

    public void RegisterUser() {

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    loginProgressbar.setVisibility(View.VISIBLE);
                    if (task.isSuccessful()) {
                        Toast.makeText(Password.this, "User Created.", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name);
                        user.put("regNo", regNo);
                        user.put("email", email);
                        user.put("contact", contact);
                        user.put("nicNo", nicNo);
                        user.put("password", password);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("OnSuccess: user profile is created for" + userID);
                                AddUserDetails();
                            }
                        });
                    } else {
                        loginProgressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Password.this, "Error !!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });



    }

    public void AddUserDetails() {
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userID);
            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("regNo", regNo);
            user.put("email", email);
            user.put("contact", contact);
            user.put("nicNo", nicNo);
            user.put("password", password);
            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    System.out.println("OnSuccess: user profile details updated for " + userID);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
            });

    }

}