package com.example.android.notepad;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class NotesAdapter extends ArrayAdapter<Note> {

    NotesAdapter(Context context, ArrayList<Note> notesList){
        super(context, 0, notesList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Note currentNote = getItem(position);

        TextView titleText = (TextView) listItemView.findViewById(R.id.title);
        TextView contentText = (TextView) listItemView.findViewById(R.id.content);

        titleText.setText(currentNote.getTitle());
        contentText.setText(currentNote.getContent());

        return listItemView;
    }
}
