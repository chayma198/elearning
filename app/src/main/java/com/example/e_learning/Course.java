package com.example.e_learning;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Course extends AppCompatActivity {

    //static final String key_desc = "description";
    // static  final String key_cont = "content";
    Button save;
    EditText editText, editText2;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        save = findViewById(R.id.save);

        fStore = FirebaseFirestore.getInstance();



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = editText.getText().toString();
                String content = editText2.getText().toString();


                DocumentReference docref = fStore.collection("courses").document();
                Map<String, Object> course = new HashMap<>();
                course.put("name", description);
                course.put("link", content);

                //fStore.collection("courses").document().set(course). addOnFailureListener();

                docref.set(course).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Toast.makeText(Course.this, "course saved", Toast.LENGTH_SHORT).show();

             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(Course.this, "failed to save" + e.getMessage(), Toast.LENGTH_SHORT).show();
                 e.printStackTrace();
             }
         });
    }
});
    }




}