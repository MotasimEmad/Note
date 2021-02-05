package com.example.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")

class Note(
    var date: String?,
    var title: String?,
    var body: String?,
    var color: String?,
    var image: String?
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
