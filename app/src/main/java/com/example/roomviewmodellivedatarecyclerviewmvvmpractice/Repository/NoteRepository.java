package com.example.roomviewmodellivedatarecyclerviewmvvmpractice.Repository;

// Creating a repository class ,it is simple java class it is not part of architecture
// it is best practice it provides abstraction b/w different data base sources and rest of database.  ViewModel will work independtly

import android.app.Application;
import android.os.AsyncTask;

import com.example.roomviewmodellivedatarecyclerviewmvvmpractice.Database.Note;
import com.example.roomviewmodellivedatarecyclerviewmvvmpractice.Database.NoteDao;
import com.example.roomviewmodellivedatarecyclerviewmvvmpractice.Database.NoteDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class NoteRepository {

    private NoteDao noteDao;


    private LiveData<List<Note>>allNotes;


    public NoteRepository(Application application){

        //Application is a sub class of Context we can use to create database through context
        NoteDatabase database=NoteDatabase.getInstance(application);

        noteDao=database.noteDao();
        allNotes=noteDao.getAllNotes();
    }

    public void insert(Note note){

        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void update(Note note){

        new UpdateNoteAsyncTask(noteDao).execute(note);


    }
    public void delete(Note note){

        new DeleteNoteAsyncTask(noteDao).execute(note);

    }
    public void deleteAllNodes(){

        new DeleteAllNoteAsyncTask(noteDao).execute();


    }
    public LiveData<List<Note>> getAllNotes(){

        //Room will automatically do this operation in background thread  bacuse of Live data.
        return allNotes;
    }
    //but for insert(),delete(),update() we can't do the data operation in main thread
    //if we do that our app will crash
    //so we will use asyncTask for this operation


    //we are using static cause it doesn't have refrence of repository so there will be no memory leak.

    private static class InsertNoteAsyncTask extends AsyncTask<Note , Void ,Void>{

        private NoteDao noteDao;

        public InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {

            noteDao.insert(notes[0]);

            return null;

        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;

        public UpdateNoteAsyncTask(NoteDao noteDao){

            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {

           noteDao.update(notes[0]);

            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note,Void,Void>{

        private NoteDao noteDao;
        public DeleteNoteAsyncTask(NoteDao noteDao) {

            this.noteDao = noteDao;

        }

        @Override
        protected Void doInBackground(Note... notes) {

            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void ,Void ,Void>{

        private NoteDao noteDao;

        public DeleteAllNoteAsyncTask(NoteDao noteDao){

            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

           noteDao.deleteAllNotes();
            return null;
        }
    }

}
