package com.example.android.unipin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private Button cameraButton;
    private Button galleryButton;
    private String selection;
    private Button submitButton;
    private DatabaseReference databaseReference;
    private String identification;
    private Spinner identificationSpinner;
    private CircleImageView profileImage;
    private int REQUEST_IMAGE_CAPTURE = 1;
    private int REQUEST_IMAGE_GALLERY = 2;
    private FirebaseUser firebaseUser;
    private String uId;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);



        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        List<String> provider = firebaseUser.getProviders();
        if(firebaseUser!=null){
        uId = firebaseUser.getUid();}
        storageReference  = FirebaseStorage.getInstance().getReference(uId+".jpg");
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        cameraButton = findViewById(R.id.camera_button_info);
        galleryButton = findViewById(R.id.gallery_button_info);
        submitButton = findViewById(R.id.submit_button_info);
        profileImage = findViewById(R.id.profile_image_info);

        image();

        identificationSpinner = findViewById(R.id.identification_spinner_info);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.identify_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        identificationSpinner.setAdapter(adapter);

        identificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selection = (String) parent.getItemAtPosition(position);
                identification = selection;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                identification = "Adult with Autism or sensory processing disorder";
            }
        });


        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchCamera();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(InfoActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("These values cannot be edited later. Do you want to continue?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                User user  = new User(firebaseUser.getDisplayName(),uId
                                        ,firebaseUser.getEmail(),identification,0,
                                        0,1,"Newbie","bronze1");
                                databaseReference.child(uId).setValue(user);
                                Intent intent = new Intent(InfoActivity.this
                                        ,HomeActivity.class);
                                if(intent.resolveActivity(getPackageManager())!=null){
                                    startActivity(intent);}
                            }

                        })
                        .setNegativeButton("No",null)
                        .setIcon(R.drawable.ic_error_black_24dp)
                        .show();
            }
        });

    }


    private void dispatchCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InfoActivity.this,MainActivity.class);
        if(intent.resolveActivity(getPackageManager())!=null){
            auth.signOut();
            LoginManager.getInstance().logOut();
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        image();
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataArray = baos.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(dataArray);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    onStart();
                    Toast.makeText(InfoActivity.this, "Your picture has been uploaded"
                            , Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(InfoActivity.this, "Uh oh failure",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                Uri photoUri = data.getData();
                // Do something with the photo based on Uri
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver()
                            , photoUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] dataArray = baos.toByteArray();
                profileImage.setImageBitmap(selectedImage);
                UploadTask uploadTask = storageReference.putBytes(dataArray);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        onStart();
                        Toast.makeText(InfoActivity.this, "Your picture has been uploaded"
                                , Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InfoActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
    public void image(){
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profileImage);
    }

    private void openGallery(){
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
        }
    }
}
