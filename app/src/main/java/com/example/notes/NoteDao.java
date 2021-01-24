package com.example.notes;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
interface NoteDao {

    @Insert
    Completable insertNote(Note note);

    @Query("select * from notes_table ORDER BY id DESC")
    Single<List<Note>> getNotes();

}
