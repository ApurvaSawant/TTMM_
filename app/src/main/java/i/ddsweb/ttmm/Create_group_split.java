package i.ddsweb.ttmm;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Create_group_split extends AppCompatActivity {

    private EditText category,amount;
    String grpname,mcategory,mamount,sresult,percentd,conname,key;
    Float result;
    int count=1,x=0,res;

    String[] division={"Choose Category","EQUALLY","UNEQUALLY","BY PERCENT"};

    ArrayList<AmountInfo> ppllist=new ArrayList<>();



    private DatabaseReference myref;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_split);


        Spinner spin = findViewById(R.id.idspinner);
        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();

        setIdValues();

        Bundle bundle=getIntent().getExtras();
        conname=bundle.getString("cnm");
        grpname=bundle.getString("groupnamee");
        count=bundle.getInt("Count");




        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, division);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                switch (i) {
                    case 0:


                        break;
                    case 1:

                        mamount = amount.getText().toString();
                        mcategory=category.getText().toString();

                        setcategory();


                        Float num = Float.parseFloat(mamount);
                        result = num / count;
                        sresult = String.valueOf(result);


                        DisplayMembers();
                        finish();
                        startActivity(new Intent(Create_group_split.this, InGroup.class).putExtra("Groupname", grpname));

                        break;

                    case 2:

                        mamount = amount.getText().toString();
                        mcategory=category.getText().toString();

                        setcategory();

                        startActivity(new Intent(Create_group_split.this,Unequally.class).putExtra("GrpName",grpname));

                        break;

                    case 3:

                        mamount = amount.getText().toString();
                        mcategory=category.getText().toString();

                        setcategory();

                        AlertDialog.Builder builder = new AlertDialog.Builder(Create_group_split.this, R.style.AlertDialog);
                        builder.setTitle("Enter Percentage Value");

                        final EditText perField = new EditText(Create_group_split.this);
                        perField.setHint("E.g: 10");
                        builder.setView(perField);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                percentd = perField.getText().toString();

                                if (TextUtils.isEmpty(percentd)) {
                                    Toast.makeText(Create_group_split.this, "Please Enter Percent Value", Toast.LENGTH_SHORT).show();
                                } else {

                                    Float num = Float.parseFloat(mamount);
                                    Float per = Float.parseFloat(percentd);
                                    result = (num / count) * (per / 100);
                                    sresult = String.valueOf(result);
                                    DisplayMembers();
                                    finish();
                                    startActivity(new Intent(Create_group_split.this, InGroup.class).putExtra("Groupname", grpname));


                                }
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();


                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

    private void setIdValues()
    {
        category=findViewById(R.id.idcategory);
        amount=findViewById(R.id.idamount);

    }
    private void setcategory()
    {
        myref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpname);
        myref.child("Category").setValue(mcategory);

    }

    private void DisplayMembers()
    {
        myref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpname).child("Members");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                ppllist.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()) {

                    key=ds.getKey();
                    AmountInfo amountInfo = ds.getValue(AmountInfo.class);

                    update(key,sresult);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void update(String key,String sresult)
    {
        myref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpname).child("Members");
        myref.child(key).child("pricee").setValue(sresult);

    }
}


