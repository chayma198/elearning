package com.example.e_learning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.inputmethod.InputConnectionCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText fullname, password, phone, email;
    Button register;
    ProgressBar progressbar;
    TextView logbutton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = findViewById(R.id.fullname);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        register = findViewById(R.id.register);
        logbutton = findViewById(R.id.logbutton);

        fAuth = FirebaseAuth.getInstance();  //getting the current instance to the DB
        fStore = FirebaseFirestore.getInstance(); //getting the current instance to the cloud FireStore
        progressbar = findViewById(R.id.progressbar);

                 if(fAuth.getCurrentUser() != null ){
                     startActivity(new Intent(getApplicationContext(),MainActivity.class));
                     finish();
                 }


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                final String Fullname = fullname.getText().toString();
                final String Phone = phone.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                   email.setError(" Email required. ");
                   return;
                }

                if (TextUtils.isEmpty(Password)){
                    password.setError(" Password required. ");
                    return;
                }
                if (Password.length() < 6 ){
                    password.setError(" Password must contain 6 characters at least. ");
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);

                //register user in FireBase
                fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "user created successfully!", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid(); //save userID of the current user
                            DocumentReference documentReference = fStore.collection("users").document(userID);    //creating users collection 'users' and the document 'userID' in the firestore DB
                            Map<String, Object> user = new HashMap<>();  //store the data
                            user.put("fullname", Fullname);  //inert the data
                            user.put("email", Email);
                            user.put("phone", Phone); //"phone" : name of the attribute in the document in DB

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {                    //insert the data in the cloud
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","user profile is created for: "+ userID);        //success msg on the tag (event listener)
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),Home.class));
                        }else
                            Toast.makeText(Register.this, "user failed to register!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.GONE);

                    }
                });
                             }
        });
                 logbutton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         startActivity(new Intent(getApplicationContext(),Login.class));

                     }
                 });
    }
}
