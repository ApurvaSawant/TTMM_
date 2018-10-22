package i.ddsweb.ttmm;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class Create_Group extends AppCompatActivity
{
    private Button addPer,next;
    private final int Pick_contact = 1;
    String  grpname,sresult,t;
    int count=1,x=0;
    private TextView displaygrpname;
    String contactnm,percent,id,name;
    ArrayList<AmountInfo> ppllist=new ArrayList<>();

    ListView listView;

    private DatabaseReference myref;

    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        mAuth=FirebaseAuth.getInstance();
        myref=FirebaseDatabase.getInstance().getReference();

        setIdValues();

        Bundle bundle=getIntent().getExtras();
        grpname=bundle.getString("GroupName");
        if(bundle!=null)
        {
            displaygrpname.setText(grpname);
        }

        addPer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                count=count+1;
                callContact(v);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Create_Group.this,Create_group_split.class).putExtra("cnm",contactnm).putExtra("groupnamee",grpname).putExtra("Count",count));
            }
        });

    }



    private void setIdValues()
    {
        addPer = findViewById(R.id.idaddper);
        next=findViewById(R.id.idnext);
        displaygrpname=findViewById(R.id.iddisplaygrpname);
        listView=findViewById(R.id.iddispmem);
    }


    public void callContact(View v)
    {
        Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(in, Pick_contact);
    }


    @Override
    protected void onActivityResult(int reqCode, int ResultCode, Intent data)
    {

        super.onActivityResult(reqCode, ResultCode, data);
        t = null;
        if (reqCode == Pick_contact) {

            if (ResultCode == AppCompatActivity.RESULT_OK)
            {
                Uri contactData=data.getData();
                Cursor c=getContentResolver().query(contactData,null,null,null,null);



                if (c.moveToFirst()) { name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                    String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    String idnum = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));


                    if (hasPhone.equalsIgnoreCase("1")) {

                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,

                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + idnum, null, null);

                        phones.moveToFirst();

                        String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        t = cNumber;

                    }
                    setCon(name, t);
                    DisplayMembers();
                }
            }

        }


    }


    void setCon(String name,String t){

        String contactnm = name;
        String contactnum = t;
        sresult="0.00";
        myref = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpname).child("Members");
        id =  myref.push().getKey();
        AmountInfo amountinfo = new AmountInfo(id,contactnm,contactnum,sresult);
        myref.child(id).setValue(amountinfo);

    }



    private void DisplayMembers()
    {
        myref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpname).child("Members");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                ppllist.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren())
                {

                    AmountInfo amountInfo = ds.getValue(AmountInfo.class);

                    ppllist.add(amountInfo);

                    ListAdapter adapter = new ListAdapter(Create_Group.this, R.layout.people_listview, ppllist);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(Create_Group.this,R.style.AlertDialog);
        builder.setMessage("Are you sure you want to Delete this group? ").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpname);
                dref.removeValue();
                finish();
                startActivity(new Intent(Create_Group.this,Groups_List.class));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
