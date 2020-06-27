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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import org.w3c.dom.Text;

public class RegistrationActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private TextView sign_in;
    private Button sign_btn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        sign_btn = findViewById(R.id.signup_btn);
        sign_in = findViewById(R.id.signin_txt);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();



        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPass = password.getText().toString().trim();

                if(TextUtils.isEmpty(mEmail)){
                    email.setError("Required Field");
                    return;
                }
                if(TextUtils.isEmpty(mPass)){
                    password.setError("Required Field");
                    return;
                }



                mAuth.createUserWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }else{
                            try {
                                throw task.getException();

                            }
                            catch (FirebaseAuthInvalidCredentialsException e) {
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                            }
//                            catch (FirebaseAuthEmailException e){
//                                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
//                            }
                            catch (FirebaseAuthException e){
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e) {
                                progressDialog.dismiss();

                                e.printStackTrace();
                            }

//                            progressDialog.dismiss();
//                            Toast.makeText(RegistrationActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });




    }
}