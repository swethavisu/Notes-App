package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPass;
    private Button signupBtn;
    private TextView loginRedirectText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.createacc_email);
        signupPass = findViewById(R.id.createacc_pass);
        signupBtn = findViewById(R.id.signup_btn);
        loginRedirectText = findViewById(R.id.loginredirecttext);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String password = signupPass.getText().toString().trim();

                if (user.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                }
                if (password.isEmpty()) {
                    signupPass.setError("Password cannot be empty");
                } else {
                    auth.createUserWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(CreateAccount.this,"Account created successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CreateAccount.this, LoginActivity.class));
                                finish();
                            } else {
                                String errorMessage = "";
                                if (task.getException() != null && task.getException().getMessage() != null) {
                                    errorMessage = ": " + task.getException().getMessage();
                                } else {
                                    errorMessage = ": Unknown error";
                                }
                                Toast.makeText(CreateAccount.this, "Sign up failed" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                    loginRedirectText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(CreateAccount.this, LoginActivity.class));
                        }
                    });

                }
            }


        });
    }
}