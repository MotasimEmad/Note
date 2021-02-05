package com.example.notes;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertNote(Note note);

    @Query("select * from notes_table ORDER BY id DESC")
    Single<List<Note>> getNotes();

    @Query("delete from notes_table where id = :id")
    Completable deleteNote(int id);
}
