package com.example.noteapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class HomeFragment extends Fragment {

    private TextView ed;
    private String myStr;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    ImageButton del;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle data = getArguments();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button addNotebtn = (Button) getActivity().findViewById(R.id.add_note_btn);
        addNotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.homelinear);
      setupRecyclerView(recyclerView);
    }

  private void setupRecyclerView(RecyclerView recyclerView) {
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noteAdapter = new NoteAdapter(options,getContext());
        this.recyclerView.setAdapter(noteAdapter);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }


}