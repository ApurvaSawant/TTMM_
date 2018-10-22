package i.ddsweb.ttmm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;

import android.content.Intent;

import android.graphics.Bitmap;

import android.net.Uri;

import android.provider.MediaStore;

import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ImageView;

import android.widget.TextView;

import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Signup extends AppCompatActivity {

    EditText name, phn, email, pass, dob;
    Button register;
    private FirebaseAuth mAuth;
    String mname, memail, mpass, mdob, mphn;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        setupUIViews();


        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    mAuth.createUserWithEmailAndPassword(memail, mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUserData();
                                mAuth.signOut();
                                Toast.makeText(Signup.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Signup.this, Login.class));
                            } else {
                                Toast.makeText(Signup.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
            }
        });


    }


    private void setupUIViews() {
        name = findViewById(R.id.idname);
        pass = findViewById(R.id.idpass);
        phn = findViewById(R.id.idphn);
        email = findViewById(R.id.idemail);
        register = findViewById(R.id.idreg);
        dob = findViewById(R.id.iddob);

    }


    private boolean validate()
    {
        Boolean a=false;

        mname = name.getText().toString();
        mpass = pass.getText().toString();
        memail = email.getText().toString();
        mdob = dob.getText().toString();
        mphn = phn.getText().toString();

        if(mname.isEmpty() || mpass.isEmpty() || memail.isEmpty() || mdob.isEmpty() || mphn.isEmpty()){

            Toast.makeText(Signup.this, "Please enter all the details", Toast.LENGTH_SHORT).show();

        }else{

            a = true;

        }



        return a;
    }


    private void sendUserData()
    {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mDatabase.getReference();

        Users userProfile = new Users(mname,memail,mphn,mdob,mpass);

        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(userProfile);

    }
    
    





}

