package com.example.noteapp;

import androidx.annotation.NonNull;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;


public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;
    ImageView im;
    String docId,uuuu;
    int h=0;
    DocumentSnapshot snapshot;
    private List<ClipData.Item> items;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position , @NonNull Note note){
        holder.titleTextView.setText(note.Title);
        holder.contentTextView.setText(note.Content);
        holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));
        Intent in = new Intent(context,NoteDetailsActivity.class);
        if(note.likeval==1)
        {
            holder.like.setVisibility(View.VISIBLE);
            h=1;
            in.putExtra("like1", "true" );
        }
        else {

            in.putExtra("like1", "false" );
        }
        holder.itemView.setOnClickListener((v)->{
            in.putExtra("title",note.Title);
            in.putExtra("content",note.Content);
            docId = this.getSnapshots().getSnapshot(position).getId();
            in.putExtra("docId",docId);
            in.putExtra("date", note.dateday );
            in.putExtra("time", note.time );
            context.startActivity(in);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }
    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView,contentTextView,timestampTextView,date,time;
        ImageView image;
       ImageView like;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.detailtitle);
            contentTextView = itemView.findViewById(R.id.detailcontent);
            timestampTextView = itemView.findViewById(R.id.detaildate);
            like = itemView.findViewById(R.id.del_note);


        }


    }

}
