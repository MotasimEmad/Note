package com.example.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    lateinit var mArrayListNote: ArrayList<Note>
    var notePosition: Int = -1
    lateinit var adapter2: NoteAdapter
    private lateinit var mNotesDataBase: NotesDataBase

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mActionBar: ActionBar = supportActionBar!!
        mActionBar.hide()

        mNotesDataBase = NotesDataBase.getInstance(this)
        mArrayListNote = ArrayList()

        adapter2 = NoteAdapter()
        PostsRecyclerView.adapter = adapter2
        PostsRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        btnCreateNewPost.setOnClickListener {
            startActivity(Intent(this, AddNewNoteActivity::class.java))
        }

//        EditSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//
//            }
//
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//                adapter2.cancleTimer()
//            }
//
//            override fun afterTextChanged(editable: Editable) {
//                if (mArrayListNote.size != 0) {
//                    adapter2.searchNote(editable.toString())
//                }
//            }
//        })
        getNote()
    }


    fun getNote() {
        mNotesDataBase.noteDao().notes
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<Note>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(notes: List<Note>) {
                    adapter2.setList(notes as java.util.ArrayList<Note>?, this@MainActivity, NoteAdapter.DeleteItemClick { position, id ->
                      mNotesDataBase.noteDao().deleteNote(id)
                          .subscribeOn(Schedulers.computation())
                          .subscribe(object : CompletableObserver {
                              override fun onSubscribe(d: Disposable) {

                              }

                              override fun onComplete() {

                              }

                              override fun onError(e: Throwable) {

                              }
                          })
                    })
                    adapter2.notifyDataSetChanged()
                }

                override fun onError(e: Throwable) {

                }
            })
    }

}
