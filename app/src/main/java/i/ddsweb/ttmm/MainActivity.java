package i.ddsweb.ttmm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button signup = findViewById(R.id.idsignup);
        TextView login = findViewById(R.id.idlogin);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent=new Intent(MainActivity.this,Signup.class);
                startActivity(signupintent); //accepts obj of intent

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginintent=new Intent(MainActivity.this,Login.class);
                startActivity(loginintent); //accepts obj of intent

            }
        });





    }



}
