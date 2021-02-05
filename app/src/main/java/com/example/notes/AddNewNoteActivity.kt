package com.example.notes

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_new_note.*
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class AddNewNoteActivity : AppCompatActivity() {

    var selectedNoteColor: String? = null
    lateinit var IndicatorView: View
    var REQUEST_CODR_STORAGE_PERMISSION: Int = 1
    var REQUEST_CODR_SELECT_IMAGE: Int = 2
    var selectedImagePath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_note)

        val mActionBar: ActionBar = supportActionBar!!
        mActionBar.hide()

        val mNotesDataBase: NotesDataBase = NotesDataBase.getInstance(this)

        NoteDate.text = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
            .format(Date())

        btnDone.setOnClickListener {
            mNotesDataBase.noteDao().insertNote(Note(NoteDate.text.toString(), EditNoteTitle.text.toString(), EditNoteBody.text.toString(), selectedNoteColor, selectedImagePath))
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
        selectedImagePath = ""
        IndicatorView = findViewById(R.id.View)

        imageRemove.setOnClickListener {
            NoteImage.setImageBitmap(null)
            NoteImage.visibility = android.view.View.GONE

            findViewById<CardView>(R.id.imageRemove).visibility = android.view.View.GONE
            selectedImagePath = ""
        }

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

        ColorConstraintLayout.findViewById<LinearLayout>(R.id.layoutAddImage).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                   this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODR_STORAGE_PERMISSION)
            }else {
                selectImage()
            }
        }
    }

    fun setIndicatorColor() {
        val mGradientDrawable: GradientDrawable = IndicatorView.background as GradientDrawable
        mGradientDrawable.setColor(Color.parseColor(selectedNoteColor))
    }

    fun selectImage() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODR_SELECT_IMAGE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODR_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODR_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                var uri: Uri = data.data!!
                try {
                    var mInputStream: InputStream = contentResolver.openInputStream(uri)!!
                    var mBitmap: Bitmap = BitmapFactory.decodeStream(mInputStream)
                    NoteImage.setImageBitmap(mBitmap)
                    NoteImage.visibility = android.view.View.VISIBLE
                    findViewById<CardView>(R.id.imageRemove).visibility = android.view.View.VISIBLE

                    selectedImagePath = getPathFromUri(uri)
                } catch (exception: Exception) {
                    Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun getPathFromUri(uri: Uri): String {
        var filePath: String
        var cursor: Cursor? = contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )

        if (cursor == null) {
            filePath = uri.path!!
        } else {
            cursor.moveToFirst()
            var index: Int = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }
}
