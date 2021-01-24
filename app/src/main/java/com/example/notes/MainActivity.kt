package com.example.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var mArrayListNote: ArrayList<Note>

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mActionBar: ActionBar = supportActionBar!!
        mActionBar.hide()

        val mNotesDataBase: NotesDataBase = NotesDataBase.getInstance(this)
        mArrayListNote = ArrayList()

        val adapter2 = PostAdapter()
        PostsRecyclerView.adapter = adapter2
        PostsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        btnCreateNewPost.setOnClickListener {
            startActivity(Intent(this, AddNewNoteActivity::class.java))
        }


        mNotesDataBase.noteDao().notes
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<Note>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(notes: List<Note>) {
                    adapter2.setList(notes as java.util.ArrayList<Note>?)
                    adapter2.notifyDataSetChanged()
                }

                override fun onError(e: Throwable) {

                }
            })
    }
}
