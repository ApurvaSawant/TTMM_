package i.ddsweb.ttmm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class InGroup extends AppCompatActivity {


    String groupname;
    Button ok;
    TextView ingroupname,catview,settledup;
    Float amountt,result;
    String  sresult,amt,price,name,pid,id;
    private ValueEventListener mListener;


    GifImageView gif;
    ListView lst_view;
    DatabaseReference groupref,myref;
    FirebaseAuth mAuth;
    ArrayList<AmountInfo> peoplelist= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_group);

        ingroupname=findViewById(R.id.idingrpname);
        lst_view=findViewById(R.id.idmemberlist);
        catview=findViewById(R.id.idcategoryview);
        ok=findViewById(R.id.idok);

        mAuth=FirebaseAuth.getInstance();


        Bundle bundle=getIntent().getExtras();
        groupname=bundle.getString("Groupname");
        if(bundle!=null)
        {
            ingroupname.setText(groupname);
        }

        //Displaying Category
        groupref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(groupname);
        groupref.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                catview.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                peoplelist.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    AmountInfo amountInfo = ds.getValue(AmountInfo.class);

                    peoplelist.add(amountInfo);

                    ListAdapter adapter = new ListAdapter(InGroup.this, R.layout.people_listview, peoplelist);
                    lst_view.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                if(peoplelist.size()==0)
                {
                    gif=findViewById(R.id.gif2);
                    settledup=findViewById(R.id.idsettledup);
                    gif.setVisibility(View.VISIBLE);
                    settledup.setVisibility(View.VISIBLE);
                    ok.setVisibility(View.VISIBLE);
                    TextView tv=findViewById(R.id.textView7);
                    tv.setVisibility(View.INVISIBLE);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(groupname);
                            myref.removeValue();
                            finish();
                            startActivity(new Intent(InGroup.this,Groups_List.class));

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(groupname).child("Members");
        myref.addValueEventListener(mListener);


        lst_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(InGroup.this,R.style.AlertDialog);
                builder.setTitle("Settle up Amount");

                final EditText amountField=new EditText(InGroup.this);
                amountField.setHint("E.g: Rs.125");
                builder.setView(amountField);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        amt=amountField.getText().toString();

                        if(amt.isEmpty())
                        {
                            Toast.makeText(InGroup.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            name=peoplelist.get(position).getPersonname();
                            price=peoplelist.get(position).getPricee();
                            pid=peoplelist.get(position).getPersonid();


                            amountt=Float.parseFloat(amt);
                            Float debt=Float.parseFloat(price);
                            result=debt-amountt;
                            if(result==0)
                            {
                                DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(groupname).child("Members").child(pid);
                                dref.removeValue();

                                Toast.makeText(InGroup.this, "You and " + name +" are all SETTLED UP ", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                sresult = String.valueOf(result);
                                update(pid,sresult);
                                //DisplayMembers();
                            }
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });

    }




    private void update(String key, String sresult)
    {
        DatabaseReference groupref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(groupname).child("Members").child(key).child("pricee");
        groupref.setValue(sresult);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myref.child("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(groupname).child("Members").removeEventListener(mListener);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InGroup.this,Groups_List.class));
    }
}
