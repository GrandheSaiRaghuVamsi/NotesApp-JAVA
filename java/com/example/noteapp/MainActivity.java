package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton bt1, bt2, bt3, bt4, bt5,bt6,bt7;
    LinearLayout layout;
    private DrawerLayout dl;
    EditText ed1;
    FirebaseAuth mAuth;

    String name,finalw;
    private static final String CHANNEL_ID = "channel_id01";
    public static final int NOTIFICATION_ID = 1;
    String message = "Hello this is your notification";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chnnelID = new NotificationChannel("Notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chnnelID);
        }

     //  addNoteBtn = findViewById(R.id.mainb);
       // addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this,NoteDetailsActivity.class) ));

        ed1 = findViewById(R.id.searched);
        bt7 = findViewById(R.id.searchbut);
        name=getIntent().getStringExtra("namehe");

        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dl = findViewById(R.id.drawer_layout);
        NavigationView nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,dl,toolbar,R.string.open_nav,R.string.close_nav);
        dl.addDrawerListener(toggle);
        toggle.syncState();

       if (savedInstanceState == null)
        {
           getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,new HomeFragment()).commit();
            nv.setCheckedItem(R.id.home);
        }

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.home:
                Toast.makeText(MainActivity.this,"Home",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this,MainActivity.class) );
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,new HomeFragment()).commit();
                break;

            case R.id.sett:
                Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,new SettingsFragment()).commit();
                break;

            case R.id.log:
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Logout");
                alertDialog.setMessage("Are You Sure ....?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotificationPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Notification")
                                .setSmallIcon(R.drawable.baseline_notes_24)
                                .setContentTitle(name)
                                .setContentText("Logged out successfully")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true);
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("Message", message);
                        PendingIntent p = PendingIntent.getActivity(MainActivity.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(p);
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(1, builder.build());
                        startActivity(i);
                        finish();

                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Sorry Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dailog = alertDialog.create();
                dailog.show();
                break;

        }
        dl.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START);
        }

    }

}