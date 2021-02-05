package com.example.notes

import android.widget.EditText

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import io.reactivex.Completable
import io.reactivex.Single

@Dao
internal interface NoteDao {

    @get:Query("select * from notes_table ORDER BY id DESC")
    val notes: Single<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note): Completable

    @Query("delete from notes_table where id = :id")
    fun deleteNote(id: Int): Completable
}
