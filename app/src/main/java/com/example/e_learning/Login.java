package com.example.e_learning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email1, password1;
    Button login;
    TextView regbutton, resetbutton;
    ProgressBar progressbar1;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email1 = findViewById(R.id.email1);
        password1 = findViewById(R.id.password1);
        login = findViewById(R.id.login);
        regbutton = findViewById(R.id.regbutton);
        resetbutton = findViewById(R.id.resetbutton);
        progressbar1 = findViewById(R.id.progressbar1);
        fAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = email1.getText().toString().trim();
                String Password = password1.getText().toString().trim();


               /* if (Email == "chayma@gmail.com"){
                    startActivity(new Intent(getApplicationContext(),AdminPage.class));
                }*/

                if (Email.equalsIgnoreCase("chayma@gmail.com")){
                    startActivity(new Intent(getApplicationContext(),AdminPage.class));
                    return;
                }

                if (TextUtils.isEmpty(Email)) {
                    email1.setError(" Email required. ");
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    password1.setError(" Password required. ");
                    return;
                }
                if (Password.length() < 6) {
                    password1.setError(" Password must contain 6 characters at least. ");
                    return;
                }

                progressbar1.setVisibility(View.VISIBLE);

                //authenticate the user
                fAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "logged in successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Home.class));

                        } else {
                            Toast.makeText(Login.this, "failed to login!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressbar1.setVisibility(View.GONE);

                        }
                    }
                });


            }
            });
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));

            }
        });

        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordresetdialog = new AlertDialog.Builder(v.getContext());
                passwordresetdialog.setTitle("Reset Password ?");
                passwordresetdialog.setMessage("Enter your Email to receive the Reset Link");
                passwordresetdialog.setView(resetMail);
                //handle the user action on the button (if yes=reset)
                passwordresetdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract email and send reset link
                        String mail = resetMail.getText().toString().trim();
                        fAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){
                                   Toast.makeText(Login.this, "Reset Link sent to your E-mail", Toast.LENGTH_SHORT).show();
                               } else {
                                   Toast.makeText(Login.this, "ERROR! Reset Link is not sent!", Toast.LENGTH_SHORT).show();
                               }
                            }
                        });


                    }
                });
                //handle the user action on the button (if no=don't reset)
                passwordresetdialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //close the dialog, back to log in view
                    }
                });
                passwordresetdialog.create().show();


            }
        });

    }}

