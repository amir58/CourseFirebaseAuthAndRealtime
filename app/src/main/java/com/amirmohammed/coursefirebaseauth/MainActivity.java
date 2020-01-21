package com.amirmohammed.coursefirebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    EditText editTextNote;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNote = findViewById(R.id.main_et_note);
        buttonSave = findViewById(R.id.main_btn_save);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String note = editTextNote.getText().toString();

//                reference.child("notes").child("note4").setValue(note);
                reference.child("notes").push().setValue(note);

            }
        });

        buttonSave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                reference.child("notes").child("note1")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String note = dataSnapshot.getValue(String.class);
                                Toast.makeText(MainActivity.this, note, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                return false;
            }
        });


        getAllNotes();

    }

    List<String> notes = new ArrayList<>();
    private void getAllNotes() {

        reference.child("notes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Toast.makeText(MainActivity.this, "" + notes.size(), Toast.LENGTH_SHORT).show();


                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    String note = shot.getValue(String.class);
                    notes.add(note);
                }
                Toast.makeText(MainActivity.this, "" + notes.size(), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }

    private void userValidation() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void signOut() {
        auth.signOut();
        userValidation();
    }


}
