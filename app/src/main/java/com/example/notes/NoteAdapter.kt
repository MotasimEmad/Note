package com.example.notes

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

import soup.neumorphism.NeumorphImageView

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.PostViewHolder>() {

    private var notesList: ArrayList<Note>? = ArrayList()
    private var context: Context? = null
    private var deleteItemClick: DeleteItemClick? = null
    private var timer: Timer? = null
    private var notesSourc: ArrayList<Note>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.note_container, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteAdapter.PostViewHolder, position: Int) {
        holder.date.text = notesList!![position].date
        holder.title.text = notesList!![position].title
        holder.body.text = notesList!![position].body

        val mGradientDrawable = holder.NoteColorLayout.background as GradientDrawable
        if (notesList!![position].color != null) {
            mGradientDrawable.setColor(Color.parseColor(notesList!![position].color))
        } else {
            mGradientDrawable.setColor(Color.parseColor("#7B7B7B"))
        }

        if (notesList!![position].image == null) {
            holder.NoteImage.visibility = View.GONE
        } else {
            holder.NoteImage.setImageBitmap(BitmapFactory.decodeFile(notesList!![position].image))
            holder.NoteImage.visibility = View.VISIBLE
        }

        holder.cardViewDelete.setOnClickListener {
            deleteItemClick!!.onItemDelete(
                position,
                notesList!![position].id
            )
        }
    }

    override fun getItemCount(): Int {
        return notesList!!.size
    }

    fun setList(postsList: ArrayList<Note>, context: Context, deleteItemClick: DeleteItemClick) {
        this.notesList = postsList
        this.context = context
        this.deleteItemClick = deleteItemClick
        this.notesSourc = postsList
        notifyDataSetChanged()
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView
        val title: TextView
        val body: TextView
        val NoteImage: ImageView
        val NoteColorLayout: RelativeLayout
        val cardViewDelete: NeumorphImageView

        init {

            date = itemView.findViewById(R.id.NoteDate)
            title = itemView.findViewById(R.id.NoteTitle)
            body = itemView.findViewById(R.id.NoteBody)
            NoteColorLayout = itemView.findViewById(R.id.RelativeLayoutNoteColor)
            NoteImage = itemView.findViewById(R.id.NoteImage)
            cardViewDelete = itemView.findViewById(R.id.imageRemoveNote)
        }
    }

    interface DeleteItemClick {
        fun onItemDelete(position: Int, id: Int)
    }
}