package com.cseradam.szkt;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import android.app.ProgressDialog;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;

public class Register extends AppCompatActivity {


    FirebaseAuth auth;

    EditText emailET;
    EditText passwordET;
    EditText password2ET;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.editTextTextEmailAddress);
        passwordET = findViewById(R.id.editTextTextPassword);
        password2ET = findViewById(R.id.editTextTextPasswordAgain);
        progressDialog = new ProgressDialog(this);
    }

    public void callLogin(View view)
    {
        Intent login = new Intent(this, MainActivity.class);
        startActivity(login);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void register(View view)
    {
        String password = passwordET.getText().toString();
        String email = emailET.getText().toString();
        if (passwordET.getText().toString().equals(password2ET.getText().toString())) {
            password = passwordET.getText().toString();
        } else {
            passwordET.setError("A jelszó nem egyezik");
            password2ET.setError("A jelszó nem egyezik");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailET.setError("Az email üres");
            return;
        } else if (TextUtils.isEmpty(password)) {
            passwordET.setError("A jelszó üres");
            passwordET.setError("A jelszó üres");
            return;
        }

        progressDialog.setTitle("Percek kérdése");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    startActivity(new Intent(Register.this, MainPage.class));
                    Toast.makeText(Register.this, "Regisztráció sikeres", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Regisztráció nem sikerült", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}