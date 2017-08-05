package com.example.android.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Objects;


public class NoteSelect extends AppCompatActivity {

    public static int curNote = 0;
    ArrayList<Note> notesList = new ArrayList<>();
    Animation delAnim;
    Animation delClose;
    ImageView delIcon;
    NotesAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                File directory;
                directory = getFilesDir();
                File[] files = directory.listFiles();
                curNote = files.length;
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(main);
            }
        });

        delAnim = AnimationUtils.loadAnimation(this, R.anim.delete);
        delClose = AnimationUtils.loadAnimation(this, R.anim.delete_close);

        prepareNotes();
        adapter = new NotesAdapter(this, notesList);
        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curNote = position;
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(main);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                delButtonFirst(listView, view, position);
                return true;
            }
        });



        if (notesList.size() == 0){
            TextView desc = (TextView) findViewById(R.id.desc);
            desc.setText(R.string.description);
        }
    }

    public void delButtonFirst(ListView listView, View v, int position){
        curNote = position;
        delIcon = (ImageView) v.findViewById(R.id.del_icon);
        if (!Objects.equals(delIcon.getContentDescription().toString(), "open")) {
            delIcon.startAnimation(delAnim);
            delIcon.setClickable(true);
            delIcon.setContentDescription("open");
        }
//                OutsideTouch cls = new OutsideTouch();
//                cls.setOnTouchOutsideViewListener(delIcon, new OutsideTouch.OnTouchOutsideViewListener() {
//                    @Override
//                    public void onTouchOutside(View view, MotionEvent event) {
//                        view.startAnimation(delClose);
//                        view.setClickable(false);
//                    }
//                });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                delButton(delIcon, view, position);
                return true;
            }
        });

//        delIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delete("Note" + curNote + ".txt");
//                notesList.clear();
//                prepareNotes();
//                adapter.notifyDataSetChanged();
//                if (Objects.equals(v.getContentDescription().toString(), "open")) {
//                    v.startAnimation(delClose);
//                    v.setClickable(false);
//                    v.setContentDescription("close");
//                }
//            }
//        });



    }

    public void prepareNotes() {
        notesList.clear();
        File directory;
        directory = getFilesDir();
        File[] files = directory.listFiles();
        String theFile;
        for (int f = 0; f < files.length; f++) {
            theFile = "Note" + f + ".txt";
            Note note = new Note(openTitle(theFile), open(theFile));
            notesList.add(note);
        }

    }

    public void delete(String fileName){
        File directory;
        directory = getFilesDir();
        File[] files = directory.listFiles();
        int num = files.length;
        getApplicationContext().deleteFile(fileName);
        int f;
        String fName = "";
        for (f = curNote; f < num-1; f++){
            String s = "";
            fName = "Note" + (f+1) + ".txt";
            try {
                InputStream in = openFileInput(fName);
                if ( in != null) {
                    InputStreamReader tmp = new InputStreamReader( in );
                    BufferedReader reader = new BufferedReader(tmp);
                    String str;
                    StringBuilder buf = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        buf.append(str).append("\n");
                    } in.close();
                    s = buf.toString();
                }
            } catch (java.io.FileNotFoundException ignored) {} catch (Throwable t) {
                Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
            fName = "Note" + f + ".txt";
            try {
                OutputStreamWriter out = new OutputStreamWriter(openFileOutput(fName, 0));
                out.write(s);
                out.close();
            } catch (Throwable t) {
                Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        }
        getApplicationContext().deleteFile("Note" + f + ".txt");
    }

    public void delButton(View v, View view, int position){
        curNote = position;
        ImageView del = (ImageView) v;
        if (v != view.findViewById(R.id.del_icon)) {
            del.startAnimation(delClose);
            del.setClickable(false);
            del.setContentDescription("close");
        }
        delIcon = (ImageView) view.findViewById(R.id.del_icon);
        if (!Objects.equals(delIcon.getContentDescription().toString(), "open")) {
            delIcon.startAnimation(delAnim);
            delIcon.setClickable(true);
            delIcon.setContentDescription("open");
        }
//                OutsideTouch cls = new OutsideTouch();
//        cls.setOnTouchOutsideViewListener(delIcon, new OutsideTouch.OnTouchOutsideViewListener() {
//            @Override
//            public void onTouchOutside(View view, MotionEvent event) {
//                view.startAnimation(delClose);
//                view.setClickable(false);
//            }
//        });
//        delIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delete("Note" + curNote + ".txt");
//                notesList.clear();
//                prepareNotes();
//                adapter.notifyDataSetChanged();
//                if (Objects.equals(v.getContentDescription().toString(), "open")) {
//                    v.startAnimation(delClose);
//                    v.setClickable(false);
//                    v.setContentDescription("close");
//                }
//            }
//        });
    }

    public void d(View v) {
        delete("Note" + curNote + ".txt");
        prepareNotes();
        adapter.notifyDataSetChanged();
        if (Objects.equals(v.getContentDescription().toString(), "open")) {
            v.startAnimation(delClose);
            v.setClickable(false);
            v.setContentDescription("close");
        }

    }

    public String openTitle(String fileName){
        String title = "";
        try {
            InputStream in = openFileInput(fileName);
            if (in != null) {
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                String str;
                if ((str = reader.readLine()) != null) {
                    title = str;
                }
            }
        } catch (java.io.FileNotFoundException ignored) {} catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
        return title;
    }

    public String open(String fileName) {
        String content = "";
        try {
            InputStream in = openFileInput(fileName);
            if ( in != null) {
                InputStreamReader tmp = new InputStreamReader( in );
                BufferedReader reader = new BufferedReader(tmp);
                String str;
                StringBuilder buf = new StringBuilder();
                String temp = reader.readLine();
                while ((str = reader.readLine()) != null) {
                    buf.append(str).append("\n");
                } in.close();
                content = buf.toString();
            }
        } catch (java.io.FileNotFoundException ignored) {} catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
        return content;
    }

}
