package com.example.myshopkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myshopkart.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private Toolbar home_toolbar;

    private FloatingActionButton floatingActionButton;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;

    private TextView totalsumResult;


    private String type;
    private int amount;
    private String note;
    private String post_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(home_toolbar);
        getSupportActionBar().setTitle("Daily Shopping List");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uid);

        mDatabase.keepSynced(true);


        recyclerView = findViewById(R.id.recycleView);
        totalsumResult = findViewById(R.id.amount);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int totalammount = 0;
                for (DataSnapshot snap:dataSnapshot.getChildren()){
                    Data data=snap.getValue(Data.class);
                    totalammount+=data.getmAmount();
                    String sttotal=String.valueOf(totalammount+".00");
                    totalsumResult.setText(sttotal);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        floatingActionButton = findViewById(R.id.fab);
        //database and auth
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();
            }
        });
    }

    private void customDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View myview = inflater.inflate(R.layout.input_data, null);
        final AlertDialog dialog = alertDialog.create();
        dialog.setView(myview);
        final EditText types = myview.findViewById(R.id.edit_input);
        final EditText note = myview.findViewById(R.id.edit_note);
        final EditText amount = myview.findViewById(R.id.edit_amt);

        Button save = myview.findViewById(R.id.btn_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mType = types.getText().toString().trim();
                String mAmount = amount.getText().toString().trim();
                String mNote = note.getText().toString().trim();

                int amounts = Integer.parseInt(mAmount);


                if(TextUtils.isEmpty(mType)){
                    types.setError("Required field..");
                    return;
                }


                if(TextUtils.isEmpty(mAmount)){
                    amount.setError("Required field..");
                    return;
                }

                String id = mDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(mType, amounts, mNote, date, id);
                mDatabase.child(id).setValue(data);
                Toast.makeText(HomeActivity.this, "Item Added to DB", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

//        dialog.setView(myview);


        dialog.show();


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>

                (
                        Data.class,
                        R.layout.item_data,
                        MyViewHolder.class,
                        mDatabase

                )
        {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, final Data data, final int position) {

                myViewHolder.setAmount(data.getmAmount());
                myViewHolder.setDate(data.getDate());
                myViewHolder.setNote(data.getmNote());
                myViewHolder.setType(data.getmType());

                myViewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key=getRef(position).getKey();
                        type=data.getmType();
                        note=data.getmNote();
                        amount=data.getmAmount();


                        updateData();
                    }
                });


            }
        };

            recyclerView.setAdapter(adapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView=itemView;
        }

        public void setType(String type){
            TextView mType = myView.findViewById(R.id.type);
            mType.setText(type);

        }

        public void setAmount(int amount){
            TextView mAmount = myView.findViewById(R.id.amount);
            String stam = String.valueOf(amount);
            mAmount.setText(stam);

        }
        public void setNote(String note){
            TextView mNote = myView.findViewById(R.id.note);
            mNote.setText(note);

        }

        public void setDate(String date){
            TextView mDate = myView.findViewById(R.id.date);
            mDate.setText(date);

        }
    }


    public void updateData(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);

        View mView=inflater.inflate(R.layout.update_input_field,null);

        final AlertDialog dialog=mydialog.create();

        dialog.setView(mView);

        final EditText edt_Type=mView.findViewById(R.id.edit_input_update);
        final EditText edt_Ammoun=mView.findViewById(R.id.edit_amt_update);
        final EditText edt_Note=mView.findViewById(R.id.edit_note_update);

        edt_Type.setText(type);
        edt_Type.setSelection(type.length());

        edt_Ammoun.setText(String.valueOf(amount));
        edt_Ammoun.setSelection(String.valueOf(amount).length());

        edt_Note.setText(note);
        edt_Note.setSelection(note.length());



        Button btnUpdate=mView.findViewById(R.id.btn_update);
        Button btnDelete=mView.findViewById(R.id.btn_delete);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type=edt_Type.getText().toString().trim();

                String mAmmount=String.valueOf(amount);

                mAmmount=edt_Ammoun.getText().toString().trim();

                note=edt_Note.getText().toString().trim();

                int intammount=Integer.parseInt(mAmmount);

                String date=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(type,intammount,note,date,post_key);

                mDatabase.child(post_key).setValue(data);


                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();

                dialog.dismiss();

            }
        });



        dialog.show();




    }


}