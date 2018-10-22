package i.ddsweb.ttmm;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class options extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    DatabaseReference myref;
    ListView lview;
    Button addperson;
    private final int Pick_contact = 1;
    String  sresult="",amt,price,name,pid,id,t;
    Float amountt,result;
    ArrayList<AmountInfo> ppllist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mAuth= FirebaseAuth.getInstance();
        myref= FirebaseDatabase.getInstance().getReference();
        lview=findViewById(R.id.idppllist);
        addperson=findViewById(R.id.idaddfrnds);

        DisplayMembers();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder=new AlertDialog.Builder(options.this,R.style.AlertDialog);
                builder.setTitle("Settle up Amount");

                final EditText amountField=new EditText(options.this);
                amountField.setHint("E.g: Rs.125");
                builder.setView(amountField);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        amt=amountField.getText().toString();

                        if(amt.isEmpty())
                        {
                            Toast.makeText(options.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                        }
                        else {

                             name=ppllist.get(position).getPersonname();
                             price=ppllist.get(position).getPricee();
                             pid=ppllist.get(position).getPersonid();


                             amountt=Float.parseFloat(amt);
                             Float debt=Float.parseFloat(price);
                             result=debt-amountt;
                            if(result==0)
                            {
                                DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("People").child(pid);
                                dref.removeValue();

                                Toast.makeText(options.this, "You and " + name +" are all SETTLED UP ", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                sresult = String.valueOf(result);
                                update(pid,sresult);
                                DisplayMembers();
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

                builder.show();

            }
        });




        addperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(options.this,R.style.AlertDialog);
                builder.setTitle("Enter amount you have lent");

                final EditText amountField=new EditText(options.this);
                amountField.setHint("E.g: $125");
                builder.setView(amountField);

                builder.setPositiveButton("Choose Person", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        amt=amountField.getText().toString();

                        if(amt.isEmpty())
                        {
                            Toast.makeText(options.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            sresult=amt;
                            callContact(v);
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

                builder.show();
            }
        });


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home)
        {
            // Handle the camera action
        }

        else if (id == R.id.group)
        {
            startActivity(new Intent(options.this,Groups_List.class));

        }
        else if (id == R.id.rateus) {

            startActivity(new Intent(options.this,ratings.class));

        } else if (id == R.id.logout)
        {
            mAuth.signOut();
            finish();
            startActivity(new Intent(options.this,MainActivity.class));

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    void setCon(String name,String num)
    {
        myref = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("People");
        id =  myref.push().getKey();
        AmountInfo amountinfo = new AmountInfo(id,name,num,sresult);
        myref.child(id).setValue(amountinfo);

    }

    private void DisplayMembers()
    {
        myref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("People");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                ppllist.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()) {

                        AmountInfo amountInfo = ds.getValue(AmountInfo.class);

                        ppllist.add(amountInfo);

                    ListAdapter adapter = new ListAdapter(options.this, R.layout.people_listview, ppllist);
                    lview.setAdapter(adapter);
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
        myref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("People").child(key);
        myref.child("pricee").setValue(sresult);

    }


}
