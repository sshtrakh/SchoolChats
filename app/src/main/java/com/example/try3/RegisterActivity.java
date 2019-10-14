package com.example.try3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference reference;
  //  FirebaseAuth.AuthStateListener authStateListener;
    MaterialEditText emailEt, usernameEt, passwordEt;
    Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         //pointers----------------------------
        register_btn=findViewById(R.id.btn_register);
        usernameEt=findViewById(R.id.username);
        emailEt=findViewById(R.id.email);
        passwordEt=findViewById(R.id.password);
        coordinatorLayout=findViewById(R.id.coordinator);
        //------------------------------------

       register_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

             /*  String email = emailEt.getText().toString();
               //  fullName = fullnameEt.getText().toString();
               String password = passwordEt.getText().toString();

               //sign up the user
               firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {

                       if (task.isSuccessful())
                           Snackbar.make(coordinatorLayout, "Sign up successful", Snackbar.LENGTH_SHORT).show();
                       else
                           Snackbar.make(coordinatorLayout, "Sign up failed", Snackbar.LENGTH_SHORT).show();

                   }
               });*/
               String txt_username = usernameEt.getText().toString();
               String txt_email = emailEt.getText().toString();
               String txt_password = passwordEt.getText().toString();

               if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                   Toast.makeText(RegisterActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
               } else if (txt_password.length() < 6 ){
                   Toast.makeText(RegisterActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
               } else {
                   register(txt_username, txt_email, txt_password);
               }

           }
       });

    }


    private void register(final String username, String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}
