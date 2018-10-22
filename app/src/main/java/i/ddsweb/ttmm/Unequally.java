package i.ddsweb.ttmm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class Unequally extends AppCompatActivity {

    ListView lv;
    Button done;
    String groupname,key;
    Float amountt,result,tot,payy;
    String  sresult,amt,price,name,pid,id,total,payable;

    DatabaseReference groupref;
    FirebaseAuth mAuth;
    ArrayList<AmountInfo> ppllist= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unequally);

        lv=findViewById(R.id.idlist);
        done= findViewById(R.id.iddone);

        Bundle bundle=getIntent().getExtras();
        groupname=bundle.getString("GrpName");


        mAuth=FirebaseAuth.getInstance();

        DisplayMembers();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                name=ppllist.get(position).getPersonname();
                pid=ppllist.get(position).getPersonid();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(Unequally.this);
                View mview = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final EditText mtot = mview.findViewById(R.id.idtotbill);
                final EditText mpay = mview.findViewById(R.id.idpaid);

                builder1.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        total =mtot.getText().toString();
                        payable=mpay.getText().toString();

                        if (!total.isEmpty() && !payable.isEmpty())
                        {
                            tot = Float.parseFloat(total);
                            payy=Float.parseFloat(payable);
                            result = tot - payy ;

                            sresult= String.valueOf(result);
                            update(pid,sresult);
                            DisplayMembers();

                        }
                        else
                        {
                            Toast.makeText(Unequally.this, "please fill all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder1.setView(mview);
                AlertDialog dialog=builder1.create();
                dialog.show();

            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Unequally.this,InGroup.class).putExtra("Groupname",groupname));
            }
        });


    }


    private void DisplayMembers()
    {
        groupref= FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(groupname).child("Members");
        groupref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                ppllist.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    AmountInfo amountInfo = ds.getValue(AmountInfo.class);

                    ppllist.add(amountInfo);

                    ListAdapter adapter = new ListAdapter(Unequally.this, R.layout.people_listview, ppllist);
                    lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void update(String key, String sresult)
    {
        groupref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(groupname).child("Members").child(key);
        groupref.child("pricee").setValue(sresult);

    }


}
