package com.example.roomviewmodellivedatarecyclerviewmvvmpractice;

//Dao can be either interface or abstract class
//we just declare the method ,room will genrate code automatically


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    //here we can define data base operation in the form string

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY priority DESC")//compile time Room checks if this column Table will fit to Note.java class  if doesn't fit it will give the compile time error. Desc means decending order
    LiveData<List<Note>> getAllNotes();//it will work like observable if any change happens in the data base it will automatically shows on the view


}
