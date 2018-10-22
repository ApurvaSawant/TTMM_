package i.ddsweb.ttmm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();

        final EditText lemail=findViewById(R.id.lidemail);
        final EditText lpassword=findViewById(R.id.lidpass);
        Button log=findViewById(R.id.idlogin);

        log.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       String lgemail = lemail.getText().toString();
                                       String lgpassword = lpassword.getText().toString();

                                       if(!lgemail.isEmpty() || !lgpassword.isEmpty()) {

                                           mAuth.signInWithEmailAndPassword(lgemail, lgpassword)
                                                   .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<AuthResult> task) {
                                                           //progress bar
                                                           if (task.isSuccessful()) {
                                                               Toast.makeText(Login.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                                               finish();
                                                               startActivity(new Intent(Login.this, options.class));

                                                           } else {
                                                               Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                                           }
                                                       }
                                                   });


                                       }
                                       else
                                       {
                                           Toast.makeText(Login.this, "Please Fill all the fields", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               }

        );

    }
    }

