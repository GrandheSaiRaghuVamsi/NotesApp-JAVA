package com.example.noteapp;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.util.Calendar;

public class PaintScreenActivity extends AppCompatActivity {

    private ImageView imageView;

    ProgressDialog pd;
    String title;
    private float floatStartX = -1, floatStartY = -1,
            floatEndX = -1, floatEndY = -1;

    private Bitmap bitmap;

    Button btn,btn1 ;
    private Canvas canvas;
    private Paint paint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_screen);

        ActivityCompat.requestPermissions(this
                ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        imageView = findViewById(R.id.imageView1);
        btn = findViewById(R.id.button);
        btn1 = findViewById(R.id.button1);
        title = getIntent().getStringExtra("ptitle");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd= new ProgressDialog(PaintScreenActivity.this);
                pd.setMessage("Loading..."); // Setting Message
                pd.setTitle("ProgressDialog"); // Setting Title
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                pd.show(); // Display Progress Dialog
                pd.setCancelable(false);
                new Thread(new Runnable() {
                    public void run() {
                        int i=0;
                        try {
                            Thread.sleep(1000);
                            i=1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pd.dismiss();
                        if(i==1)
                        {
                            Intent iu =new Intent(PaintScreenActivity.this,NoteDetailsActivity.class);
                            iu.putExtra("title",getIntent().getStringExtra("ptitle"));
                            iu.putExtra("content",getIntent().getStringExtra("pcontent"));
                            iu.putExtra("time",getIntent().getStringExtra("ptime"));
                            iu.putExtra("date",getIntent().getStringExtra("pdate"));
                            startActivity(iu);
                        }
                    }
                }).start();

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaintScreenActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
                Intent inte  = new Intent(PaintScreenActivity.this,PaintScreenActivity.class);
                startActivity(inte);
            }
        });

    }


    private void drawPaintSketchImage(){

        if (bitmap == null){
            bitmap = Bitmap.createBitmap(imageView.getWidth(),
                    imageView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            paint.setColor(Color.RED);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
        }
        canvas.drawLine(floatStartX,
                floatStartY-220,
                floatEndX,
                floatEndY-220,
                paint);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
            floatStartX = event.getX();
            floatStartY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
        }
        return super.onTouchEvent(event);
    }




    @Override
    public void onBackPressed(){
        Intent inte  = new Intent(PaintScreenActivity.this,NoteDetailsActivity.class);
        startActivity(inte);
    }

}