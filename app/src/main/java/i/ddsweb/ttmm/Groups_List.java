package i.ddsweb.ttmm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class Groups_List extends AppCompatActivity {

    Button creategrp;
    ListView list_view;
    String groupName;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> list_of_groups=new ArrayList<>();

    DatabaseReference groupref;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);

        mAuth=FirebaseAuth.getInstance();


        creategrp=findViewById(R.id.idcreategrp);
        groupref= FirebaseDatabase.getInstance().getReference();

        list_view=findViewById(R.id.idgrplistview);
        arrayAdapter=new ArrayAdapter<String>(Groups_List.this,android.R.layout.simple_list_item_1,list_of_groups);
        list_view.setAdapter(arrayAdapter);

        RetrieveandDisplayGroups();

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent=new Intent(Groups_List.this,InGroup.class);
                intent.putExtra("Groupname",list_view.getItemAtPosition(position).toString());
                startActivity(intent);

            }
        });


        creategrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Groups_List.this,R.style.AlertDialog);
                builder.setTitle("Enter Group Name");

                final EditText groupNameField=new EditText(Groups_List.this);
                groupNameField.setHint("E.g: Roomates");
                builder.setView(groupNameField);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        groupName=groupNameField.getText().toString();
                        groupName=groupName.toUpperCase();

                        if(TextUtils.isEmpty(groupName))
                        {
                            Toast.makeText(Groups_List.this, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Groups_List.this, groupName+" Created Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(Groups_List.this,Create_Group.class).putExtra("GroupName",groupName));
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


    private void RetrieveandDisplayGroups() {

        groupref.child("Users").child(mAuth.getCurrentUser().getUid()).child("Group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<>();
                Iterator iterator=dataSnapshot.getChildren().iterator();

                while (iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(Groups_List.this,options.class));
    }
}




