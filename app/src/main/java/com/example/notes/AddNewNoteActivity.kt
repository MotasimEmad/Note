package com.example.notes

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_new_note.*
import java.text.SimpleDateFormat
import java.util.*

class AddNewNoteActivity : AppCompatActivity() {

    var selectedNoteColor: String? = null
    lateinit var IndicatorView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_note)

        val mActionBar: ActionBar = supportActionBar!!
        mActionBar.hide()

        val mNotesDataBase: NotesDataBase = NotesDataBase.getInstance(this)

        NoteDate.text = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
            .format(Date())

        btnDone.setOnClickListener {
            mNotesDataBase.noteDao().insertNote(Note(NoteDate.text.toString(), EditNoteTitle.text.toString(), EditNoteBody.text.toString(), selectedNoteColor))
                .subscribeOn(Schedulers.computation())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onComplete() {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }

                    override fun onError(e: Throwable) {

                    }
                })
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        selectedNoteColor = "#5E5757"
        IndicatorView = findViewById(R.id.View)

        intiColorChange()

    }

    fun intiColorChange() {
        var ColorConstraintLayout: ConstraintLayout = findViewById(R.id.ConstraintLayout)

        var imageColor1: ImageView = ColorConstraintLayout.findViewById(R.id.imageColor1)
        var imageColor2: ImageView = ColorConstraintLayout.findViewById(R.id.imageColor2)
        var imageColor3: ImageView = ColorConstraintLayout.findViewById(R.id.imageColor3)
        var imageColor4: ImageView = ColorConstraintLayout.findViewById(R.id.imageColor4)

        ColorConstraintLayout.findViewById<View>(R.id.colorView1).setOnClickListener {
            selectedNoteColor = "#5E5757"
            imageColor1.setImageResource(R.drawable.ic_done)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)

            setIndicatorColor()

        }

        ColorConstraintLayout.findViewById<View>(R.id.colorView2).setOnClickListener {
            selectedNoteColor = "#FFC400"
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(R.drawable.ic_done)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)

            setIndicatorColor()

        }

        ColorConstraintLayout.findViewById<View>(R.id.colorView3).setOnClickListener {
            selectedNoteColor = "#F50057"
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(R.drawable.ic_done)
            imageColor4.setImageResource(0)

            setIndicatorColor()

        }

        ColorConstraintLayout.findViewById<View>(R.id.colorView4).setOnClickListener {
            selectedNoteColor = "#651FFF"
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(R.drawable.ic_done)

            setIndicatorColor()

        }
    }

    fun setIndicatorColor() {
        val mGradientDrawable: GradientDrawable = IndicatorView.background as GradientDrawable
        mGradientDrawable.setColor(Color.parseColor(selectedNoteColor))
    }
}
