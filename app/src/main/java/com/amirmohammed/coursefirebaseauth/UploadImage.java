package com.amirmohammed.coursefirebaseauth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UploadImage extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private Uri uriImageSelected = null;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        profileImage = findViewById(R.id.profile_image);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAndCropImage();
            }
        });

    }

    private void selectAndCropImage() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                uriImageSelected = result.getUri();
                profileImage.setImageURI(uriImageSelected);
                String uid = auth.getCurrentUser().getUid();
                uploadUserImage(uid);

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("pickPhoto", "Exception : " + error);
            }
        }

    }

    private void uploadUserImage(final String uid) {

        storageReference.child("profileImages").child(uid).putFile(uriImageSelected)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask
                            .TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            getImageUrl(uid);
                        }
                    }
                });
    }


    private void getImageUrl(final String uid) {
        storageReference.child("profileImages")
                .child(uid).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull
                                                   Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String url = task.getResult
                                    ().toString();
                            createUserData(uid, url);
                        }
                    }
                });

    }

    private void createUserData(String uid, String url) {

        reference.child("users").child(uid).child("profileImage")
                .setValue(url)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UploadImage.this,
                                    "User create Successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
