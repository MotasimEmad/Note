package com.example.notes;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Note> notesList = new ArrayList<>();

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        holder.date.setText(notesList.get(position).getDate());
        holder.title.setText(notesList.get(position).getTitle());
        holder.body.setText(notesList.get(position).getBody());

        GradientDrawable mGradientDrawable = (GradientDrawable) holder.NoteConstraintLayout.getBackground();
        if (notesList.get(position).getColor() != null) {
            mGradientDrawable.setColor(Color.parseColor(notesList.get(position).getColor()));
        } else {
            mGradientDrawable.setColor(Color.parseColor("#7B7B7B"));
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void setList(ArrayList<Note> postsList) {
        this.notesList = postsList;
        notifyDataSetChanged();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView date, title, body;
        private ConstraintLayout NoteConstraintLayout;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.NoteDate);
            title = itemView.findViewById(R.id.NoteTitle);
            body = itemView.findViewById(R.id.NoteBody);
            NoteConstraintLayout = itemView.findViewById(R.id.NoteConstraintLayout);
        }
    }
}
