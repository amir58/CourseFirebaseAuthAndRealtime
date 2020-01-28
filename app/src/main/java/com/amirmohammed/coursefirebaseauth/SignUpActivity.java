package com.amirmohammed.coursefirebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextRePassword;
    private Button buttonSignUp;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = findViewById(R.id.sign_up_et_email);
        editTextPassword = findViewById(R.id.sign_up_et_password);
        buttonSignUp = findViewById(R.id.sign_up_btn_sign_up);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String rePassword = editTextRePassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Please fill all data", Toast
                            .LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(rePassword)) {
                    Toast.makeText(SignUpActivity.this, "خليهم زى بعض", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }


                if (password.length() < 8) {
                    Toast.makeText(SignUpActivity.this, "Password must be 8 characters"
                            , Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Password must be 8 characters");
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                        (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "الايميل اتعمل يا عم الحج",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Failed", Toast
                                    .LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}
