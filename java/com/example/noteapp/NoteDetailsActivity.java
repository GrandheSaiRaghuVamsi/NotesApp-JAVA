package com.example.noteapp;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.graphics.Bitmap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.firestore.DocumentReference;
import java.io.IOException;
import java.util.Calendar;


public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;

    TextView Date,h,deletext;
    Bitmap img;
    CheckBox cb;
    AlertDialog dialog;
    LinearLayout layout,lay;
    ImageView  imagev1;
    MaterialCheckBox likeh;
    SeekBar sb;
    Button sbut;
    int s;
    TextView pagetitle;
    String title,content,docId,shu,time,dateday,datat,minhou;
    String likeq;
    boolean isEdit = false;
    ImageButton saveNoteBtn, image, pickDate, alarm, draw, todo,textsize;
    private String imageurlstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        todo = findViewById(R.id.todol);
        lay = findViewById(R.id.li2);
        deletext = findViewById(R.id.delete_node_text);
        layout = findViewById(R.id.linear);
        pagetitle = findViewById(R.id.page_title);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        image = findViewById(R.id.addpic);
        pickDate = findViewById(R.id.cal);
        Date = findViewById(R.id.datet);
        textsize = findViewById(R.id.textsize);
        alarm = findViewById(R.id.alarmon);
        sb = findViewById(R.id.seek);
        sbut = findViewById(R.id.seekb);
        h = findViewById(R.id.hour);
        //saveNoteBtn.setOnClickListener( (v)-> saveNote(Timestamp));
        draw = findViewById(R.id.draw);
        likeh = findViewById(R.id.favnote1);
        imageurlstr ="";
        //recivedata
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        imageurlstr = getIntent().getStringExtra("img");
        datat = getIntent().getStringExtra("date");
        minhou = getIntent().getStringExtra("time");
        likeq = getIntent().getStringExtra("like1");
        likeh.setChecked(Boolean.parseBoolean(likeq));

        if(docId!=null && !docId.isEmpty())
        {
            isEdit = true;
        }
        titleEditText.setText(title);
        contentEditText.setText(content);
        h.setText(getIntent().getStringExtra("time"));
        Date.setText(getIntent().getStringExtra("date"));
        shu = title;

        if(isEdit || title!=null)
        {
            pagetitle.setText("Edit Your Note");
            deletext.setVisibility(View.VISIBLE);

        }

        deletext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NoteDetailsActivity.this);
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Confirm it ...?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNodefromFirebase();
                        Toast.makeText(NoteDetailsActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }

                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(NoteDetailsActivity.this, "Not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dailog = alertDialog.create();
                dailog.show();
            }
        });




        textsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay.setVisibility(View.VISIBLE);
            }
        });

        sbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay.setVisibility(View.INVISIBLE);
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(NoteDetailsActivity.this, ImageUpload.class);
                o.putExtra("ptitle", titleEditText.getText().toString());
                o.putExtra("pcontent",contentEditText.getText().toString());
                o.putExtra("pdate", dateday);
                o.putExtra("ptime",time);
                startActivity(o);

            }
        });


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar , int progress, boolean fromUser)  {
                s=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                contentEditText.setText(contentEditText.getText().toString());
                contentEditText.setTextSize(s);

            }
        });


        buildDialog();

        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NoteDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateday = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chnnelID = new NotificationChannel("Notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chnnelID);
        }


       alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        int hour = 0,min = 0;
        TimePickerDialog timePickerDialog = new TimePickerDialog(NoteDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                time = hourOfDay+":"+minute;
                h.setText(hourOfDay+":"+minute+" ");

            }
        },hour,min,false);
        timePickerDialog.show();

            }
        });


       draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte  = new Intent(NoteDetailsActivity.this,PaintScreenActivity.class);
                inte.putExtra("ptitle", titleEditText.getText().toString());
                inte.putExtra("pcontent",contentEditText.getText().toString());
                inte.putExtra("pdate", dateday);
                inte.putExtra("ptime",time);
                startActivity(inte);
            }
       });


        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(NoteDetailsActivity.this,MainActivity.class);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NoteDetailsActivity.this);
                alertDialog.setTitle("confirm");
                alertDialog.setMessage("Save it ...?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNote();

                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(NoteDetailsActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dailog = alertDialog.create();
                dailog.show();
            }
        });


    }

    private void deleteNodefromFirebase() {

        DocumentReference docref;
        docref = Utility.getCollectionReferenceForNotes().document(docId);
        docref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note delete
                    Toast.makeText(NoteDetailsActivity.this, "Note deleted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    //note not added
                    Toast.makeText(NoteDetailsActivity.this, "Failed While Deleting", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dailog,null);

         EditText name = v.findViewById(R.id.nameet);
       builder.setView(v);
       builder.setTitle("Enter name")
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       addCard(name.getText().toString());
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });
       dialog = builder.create();
    }

    private void addCard(String name)
    {
        View view = getLayoutInflater().inflate(R.layout.card,null);

        CheckBox namecb = view.findViewById(R.id.namecb);
        Button delete = view.findViewById(R.id.delete12);

        namecb.setText(name);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });

        layout.addView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int i=2;
        int b = 0;
        

            if (requestCode == 100 && data!=null && data.getData()!=null)
            {

                    imagev1.setImageURI(data.getData());

                Uri uri = data.getData();

                imageurlstr = uri.toString();

                try {
                    img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NoteDetailsActivity.this);
        alertDialog.setTitle("confirm");
        alertDialog.setMessage("Save it ...?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveNote();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(NoteDetailsActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                Intent inte  = new Intent(NoteDetailsActivity.this,MainActivity.class);
                startActivity(inte);
            }
        });
        AlertDialog dailog = alertDialog.create();
        dailog.show();
    }


    void saveNote()
    {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if(noteTitle==null || noteTitle.isEmpty())
        {
            titleEditText.setError("Title is required");
            return;
        }
        if(noteContent==null || noteContent.isEmpty())
        {
            contentEditText.setError("Content is required");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(com.google.firebase.Timestamp.now());
        note.setDateday(dateday);
        note.setTime(time);
        note.setLikeval(likeh.getCheckedState());
        saveNoteToFirebase(note);
    }
    void saveNoteToFirebase(Note note){
        Intent i  = new Intent(NoteDetailsActivity.this,MainActivity.class);
        DocumentReference docref;
        if(isEdit){
            docref = Utility.getCollectionReferenceForNotes().document(docId);
        }
        else {
            docref = Utility.getCollectionReferenceForNotes().document();
        }
        docref.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful()){
                     //note added
                     Toast.makeText(NoteDetailsActivity.this, "Note Added Successfully", Toast.LENGTH_SHORT).show();
                     startActivity(i);
                     finish();
                 }else {
                     //note not added
                     Toast.makeText(NoteDetailsActivity.this, "Failed While Adding", Toast.LENGTH_SHORT).show();
                     finish();
                 }
            }
        });
    }

}
















