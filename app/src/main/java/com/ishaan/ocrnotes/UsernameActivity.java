package com.ishaan.ocrnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsernameActivity extends AppCompatActivity {

    private TextInputEditText userNameEditText;
    private MaterialButton continueButton;
    public static String userName;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        userNameEditText = findViewById(R.id.userNameEditText);
        continueButton = findViewById(R.id.addNoteButton);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        continueButton.setEnabled(false);

        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(null))
                {
                    continueButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = userNameEditText.getText().toString().trim();

                databaseReference.child("notes").child(userName).child("private").child(MainActivity.userEmail.replace('.','_')).child("firstLogin").setValue("false");
                databaseReference.child("account").child(MainActivity.userEmail.replace('.','_')).child("userName").setValue(userName);
                databaseReference.child("account").child(MainActivity.userEmail.replace('.','_')).child("firstLogIn").setValue(false);

                Intent intent = new Intent(UsernameActivity.this, AllNotes.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
