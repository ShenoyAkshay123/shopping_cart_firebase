package com.example.myshopkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText email_login;
    private EditText password_login;
    private TextView sign_up;
    private Button logBtn;

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email_login = findViewById(R.id.email);
        password_login = findViewById(R.id.password);
        sign_up = findViewById(R.id.signup_txt);
        logBtn = findViewById(R.id.log_btn);
        mAuth = FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });


        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mEmail = email_login.getText().toString().trim();
                String mPass = password_login.getText().toString().trim();
                if(TextUtils.isEmpty(mEmail)){
                    email_login.setError("Required Field..");
                    return;
                }
                if(TextUtils.isEmpty(mPass)){
                    password_login.setError("Required Field..");
                    return;
                }

                mAuth.signInWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Not logged in", Toast.LENGTH_SHORT).show();


                        }
                    }
                });

                progressDialog.setMessage("Please wait...");
                progressDialog.show();


            }
        });
    }
}