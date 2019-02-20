package com.example.roomviewmodellivedatarecyclerviewmvvmpractice.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


//this is Rooom Datbase class

@Database(entities = {Note.class},version = 1)//we can add many table here
//whenever we  make change the database  we update the version is call migration
public abstract class NoteDatabase extends RoomDatabase {


    private static  NoteDatabase noteDatabase;//we will use singelton pattern

    public abstract NoteDao noteDao();//through this method we will access the data base ,room will care about implemenatation

    public static synchronized  NoteDatabase getInstance(Context context){

        if(noteDatabase== null){

            //we can't use new NoteDatabse() cause we are working throgh abstact class
            noteDatabase= Room.databaseBuilder(context.getApplicationContext(),NoteDatabase.class,"note_database")
                    .fallbackToDestructiveMigration()//when we want to made changes in database structure it will delete the old and make it new)
                    .addCallback(roomCallback)
                    .build();

        }
        return noteDatabase;
    }
    // populating the database through some value
    //using Callback

    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDbAsyncTask(noteDatabase).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
    //but we can't doon the main thread so we are working through async task in background
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            noteDao.insert(new Note("Title 3", "Description 3", 3));
            return null;
        }
    }

}
