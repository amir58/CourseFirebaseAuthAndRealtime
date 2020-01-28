package com.amirmohammed.coursefirebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    EditText editTextNote, editTextDescription, editTextFinishBy;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userValidation();

        editTextNote = findViewById(R.id.main_et_note);
        editTextDescription = findViewById(R.id.main_et_desc);
        editTextFinishBy = findViewById(R.id.main_et_finish_by);
        buttonSave = findViewById(R.id.main_btn_save);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String note = editTextNote.getText().toString();
                String description = editTextDescription.getText().toString();
                String finshBy = editTextFinishBy.getText().toString();

                NoteModel noteModel = new NoteModel();
                noteModel.setNote(note);
                noteModel.setDescription(description);
                noteModel.setFinishBy(finshBy);

                String uid = auth.getCurrentUser().getUid();

                reference.child("UsersNotes").child(uid).push().setValue(noteModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

            }
        });


        getAllNotes();
    }


    List<NoteModel> notes = new ArrayList<>();

    private void getAllNotes() {
        String uid = auth.getCurrentUser().getUid();

        reference.child("UsersNotes").child(uid).addValueEventListener
                (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notes.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NoteModel noteModel = snapshot.getValue(NoteModel.class);
                    notes.add(noteModel);
                }

                Adapter adapter = new Adapter(notes);
                RecyclerView recyclerView = findViewById(R.id.rv);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
