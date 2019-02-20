package com.example.roomviewmodellivedatarecyclerviewmvvmpractice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.roomviewmodellivedatarecyclerviewmvvmpractice.Database.Note;
import com.example.roomviewmodellivedatarecyclerviewmvvmpractice.ViewModel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        FloatingActionButton buttonAddNote = findViewById(R.id.add_notes);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

      new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
          @Override
          public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
              return false;//used for drag and drop and 0 is also for that
          }

          @Override
          public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

             noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
              Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
          }
      }).attachToRecyclerView(recyclerView);

      adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
          @Override
          public void onItemClick(Note note) {

              Intent intent=new Intent(MainActivity.this, AddEditNoteActivity.class);

              intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
              intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
              intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
              intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());

              startActivityForResult(intent,EDIT_NOTE_REQUEST);
          }
      });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.deleteallnotes:

                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted ", Toast.LENGTH_SHORT).show();
                return true;
             default:
                 return super.onOptionsItemSelected(item);


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }else if (requestCode == EDIT_NOTE_REQUEST&& resultCode == RESULT_OK){

            int id=data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);

            if(id==-1){

                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
            }else{

                String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

                Note note=new Note(title,description,priority);
                note.setId(id);

                noteViewModel.update(note);

                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
            }



        }
        else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}












/*
 <activity android:name=".AddEditNoteActivity"

            android:launchMode="singleTop"
            android:parentActivityName=".MainActivity"


            >
        </activity>
        that must be done in manifest

        this is responsible for back button process it will take to you to the main activity


          <activity android:name=".MainActivity"
            android:launchMode="singleTop">
            //because it will take to you previous one
            not to invoke on create method
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />

                //

        */