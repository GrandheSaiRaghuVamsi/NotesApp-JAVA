package com.example.noteapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.MalformedInputException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ImageUpload extends AppCompatActivity
{
    private StorageReference mStorageRef;

    Button btnPickImage, btnUpload,showAllBtn,retr;
    ImageView imgSource, imgDestination;
    LinearLayout lv;
    Uri selectedImage;
    ProgressBar pbbar;
    DatabaseReference databaseReference, childReference;
    private static final int REQUEST_TAKE_GALLERY_PHOTO = 2;
    StorageReference fireRef;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String strFileName ;
    int qwerty=0;
    String title,imageURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        showAllBtn = findViewById(R.id.next1);
        pbbar = findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        retr = findViewById(R.id.retrive);
        btnPickImage = findViewById(R.id.Select);
        btnUpload = findViewById(R.id.upload_btn);

        imgSource = findViewById(R.id.imagevi);
        imgDestination = findViewById(R.id.imagevi2);
        title = getIntent().getStringExtra("ptitle");
        strFileName = title+".jpg";
        fireRef = mStorageRef.child("images/" + currentUser.getUid().toString() + "/" + strFileName);
        retr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(imgDestination);
                    }
                });
            }
        });

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)
                        && !Environment.getExternalStorageState().equals(
                        Environment.MEDIA_CHECKING)) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_TAKE_GALLERY_PHOTO);

                } else
                    Toast.makeText(ImageUpload.this, "No gallery found.", Toast.LENGTH_SHORT).show();
            }
        });



        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImages();
            }
        });

        showAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iu =new Intent(ImageUpload.this,NoteDetailsActivity.class);
                iu.putExtra("title",title);
                iu.putExtra("content",getIntent().getStringExtra("pcontent"));
                iu.putExtra("time",getIntent().getStringExtra("ptime"));
                iu.putExtra("date",getIntent().getStringExtra("pdate"));
                startActivity(iu);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_PHOTO) {
                Bitmap originBitmap = null;
                selectedImage = data.getData();
                InputStream imageStream;
                try {
                    pbbar.setVisibility(View.VISIBLE);
                    imageStream = getContentResolver().openInputStream(
                            selectedImage);
                    originBitmap = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (originBitmap != null) {
                    {
                        this.imgSource.setImageBitmap(originBitmap);
                        pbbar.setVisibility(View.GONE);
                        imgSource.setVisibility(View.VISIBLE);
                    }
                } else
                    selectedImage = null;
            }

        }

    }

    public void UploadImages() {
        try {
            pbbar.setVisibility(View.VISIBLE);


            Uri file = selectedImage;

                UploadTask uploadTask = fireRef.putFile(file);
                Log.e("Fire Path", fireRef.toString());
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return fireRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.e("Image URL", downloadUri.toString());

                            pbbar.setVisibility(View.GONE);
                            selectedImage = null;
                            imageURL = downloadUri.toString();
                        } else {
                            Toast.makeText(ImageUpload.this, "Image upload unsuccessful. Please try again."
                                    , Toast.LENGTH_LONG).show();
                        }
                        pbbar.setVisibility(View.GONE);
                        imgDestination.setVisibility(View.VISIBLE);

                        DownloadImageFromURL downloadImageFromURL = new DownloadImageFromURL();
                        downloadImageFromURL.execute("");
                    }

                });

        }
        catch (Exception ex) {
            Toast.makeText(ImageUpload.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }



    private class DownloadImageFromURL extends AsyncTask<String, Void, String> {
        Bitmap bitmap = null;

        @Override
        protected void onPreExecute() {

        }

        protected String doInBackground(String... urls) {
            try {
                Log.e("imageURL is ", imageURL);
                InputStream in = new java.net.URL(imageURL).openStream();
                if (in != null) {
                    bitmap = BitmapFactory.decodeStream(in);
                } else
                    Log.e("Empty InputStream", "InputStream is empty.");
            } catch (MalformedInputException e) {
                Log.e("Error URL", e.getMessage().toString());
            } catch (Exception ex) {
                Log.e("Input stream error", "Input stream error");
            }
            return "";
        }

        protected void onPostExecute(String result) {
            if (bitmap != null) {
                imgDestination.setImageBitmap(bitmap);
            } else
                Log.e("Empty Bitmap", "Bitmap is empty.");
        }
    }

    @Override
    public void onBackPressed(){

    }

}

