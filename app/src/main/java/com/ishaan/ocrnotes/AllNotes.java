package com.ishaan.ocrnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllNotes extends AppCompatActivity {

    private FloatingActionButton logoutButton, addButton;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ArrayList<String> notes = new ArrayList<String>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private ImageView noItemImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        logoutButton = findViewById(R.id.logout);
        addButton = findViewById(R.id.add);
        recyclerView = findViewById(R.id.recyclerView);
        noItemImageView = findViewById(R.id.noItemImageView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("notes").child(UsernameActivity.userName);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                notes = new ArrayList<String>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (dataSnapshot1.getKey().equals("private"))
                    {
                        Log.i("INFO",dataSnapshot1.getKey());
                        for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                        {
                            if (dataSnapshot2.getKey().equals(MainActivity.userEmail.replace('.','_'))) {
                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                    if(!dataSnapshot3.getKey().equals("firstLogin")) {
                                        notes.add(dataSnapshot3.getValue().toString());
                                    }
                                }
                            }
                        }
                    }

                    else if(dataSnapshot1.getKey().equals("public"))
                    {
                      Log.i("INFO",dataSnapshot1.getKey());
                      for(DataSnapshot dataSnapshot2 :dataSnapshot1.getChildren())
                      {
                          notes.add(dataSnapshot2.getValue().toString());
                      }
                    }
                }
                if(notes.size() != 0)
                {
                    noItemImageView.setVisibility(View.GONE);
                }
                recyclerViewAdapter = new RecyclerViewAdapter(notes);
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(AllNotes.this)
                        .setIcon(R.drawable.logout_alert)
                        .setTitle("Proceed to Logout")
                        .setMessage(String.format("%s, are you sure you want to logout?",UsernameActivity.userName))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.logout = true;
                                Intent intent = new Intent(AllNotes.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllNotes.this, NewNote.class);
                startActivity(intent);
            }
        });
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
