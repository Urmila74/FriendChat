package com.example.friendchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout usre_name, user_email, user_password, user_confirm_password;
    private Button register_button;

    //Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference mydatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing widgets:
        usre_name = (TextInputLayout)findViewById(R.id.username);
        user_email = (TextInputLayout)findViewById(R.id.email);
        user_password = (TextInputLayout)findViewById(R.id.password);
      //  user_confirm_password = (TextInputLayout)findViewById(R.id.confirm_password);
        register_button = (Button)findViewById(R.id.register);
        firebaseAuth = FirebaseAuth.getInstance();

        // Adding Event Listener to Button Register
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_text = usre_name.getEditText().getText().toString().trim();
                String  email_text = user_email.getEditText().getText().toString().trim();
                String password_text = user_password.getEditText().getText().toString().trim();
             //   String conf_pass_text = user_confirm_password.getEditText().toString();

                if(TextUtils.isEmpty(username_text) || TextUtils.isEmpty(email_text) || TextUtils.isEmpty(password_text)){
                    Toast.makeText(RegisterActivity.this, "Pleased Fill All Fields", Toast.LENGTH_SHORT).show();
                }else{
                    registerNow(username_text, email_text, password_text);
                }
            }
        });

    }

    private void registerNow(final String username, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            //private String path;

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    mydatabaseReference = FirebaseDatabase.getInstance().getReference( "MyUsers").child(userid);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");
                    hashMap.put("status", "Offline");

                    //Opening the Main Activity after Success Registration
                    mydatabaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                               // Toast.makeText(RegisterActivity.this, "successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "Invalid Email of Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

