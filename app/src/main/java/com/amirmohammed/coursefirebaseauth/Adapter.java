package com.amirmohammed.coursefirebaseauth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    List<NoteModel> modelList;

    public Adapter(List<NoteModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NoteModel noteModel = modelList.get(position);

        holder.textViewNote.setText(noteModel.getNote());
        holder.textViewDescription.setText(noteModel.getDescription());
        holder.textViewFinishBy.setText(noteModel.getFinishBy());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewNote, textViewDescription, textViewFinishBy;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewNote = itemView.findViewById(R.id.note_item_note_title);
            textViewDescription = itemView.findViewById(R.id.note_item_description);
            textViewFinishBy = itemView.findViewById(R.id.note_item_finish_by);
        }
    }
}
