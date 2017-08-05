package com.example.android.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Objects;

import static com.example.android.notepad.NoteSelect.curNote;

public class MainActivity extends AppCompatActivity {

    EditText noteText;
    EditText titleText;
    Animation fabOpen;
    Animation fabRotate;
    Animation fabDerotate;
    Animation fabClose;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    FloatingActionButton fab;
    String file;
    Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabRotate = AnimationUtils.loadAnimation(this, R.anim.fab_rotate);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        fabDerotate = AnimationUtils.loadAnimation(this, R.anim.fab_derotate);

        file = "Note" + curNote + ".txt";
        note = open(file);
        titleText = (EditText) findViewById(R.id.title);
        noteText = (EditText) findViewById(R.id.edit_text);
        titleText.setText(note.getTitle());
        noteText.setText(note.getContent());

        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFab();
            }
        });

    }

    public void openFab(){
        fab.startAnimation(fabRotate);
        fab1.startAnimation(fabOpen);
        fab2.startAnimation(fabOpen);
        fab3.startAnimation(fabOpen);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFab();
            }
        });
        fab1.setClickable(true);
        fab2.setClickable(true);
        fab3.setClickable(true);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(file);
                closeFab();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFab();
                Intent noteSelect = new Intent(getApplicationContext(), NoteSelect.class);
                finish();
                startActivity(noteSelect);
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(file);
                Intent intent = new Intent(getApplicationContext(), NoteSelect.class);
                finish();
                startActivity(intent);
            }
        });
    }

    public void delete(String fileName){
        File directory;
        directory = getFilesDir();
        File[] files = directory.listFiles();
        int num = files.length;
        if (FileExists(file)) {
            getApplicationContext().deleteFile(file);
        }
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

    public void closeFab(){
        fab.startAnimation(fabDerotate);
        fab1.startAnimation(fabClose);
        fab2.startAnimation(fabClose);
        fab3.startAnimation(fabClose);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFab();
            }
        });
        fab1.setClickable(false);
        fab2.setClickable(false);
        fab3.setClickable(false);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NoteSelect.class);
        save(file);
        finish();
        startActivity(intent);
    }

    public void save(String fileName) {
        if (Objects.equals(titleText.getText().toString(), "") && Objects.equals(noteText.getText().toString(), "")){
            return;
        }
        try {
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(fileName, 0));
            out.write(titleText.getText().toString() + "\n");
            out.write(noteText.getText().toString());
            out.close();
            Snackbar.make(findViewById(R.id.fab), "Note Saved!", Snackbar.LENGTH_SHORT).show();
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean FileExists(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    public Note open(String fileName) {
        String title = "";
        String content = "";
        if (FileExists(fileName)) {
            try {
                InputStream in = openFileInput(fileName);
                if ( in != null) {
                    InputStreamReader tmp = new InputStreamReader( in );
                    BufferedReader reader = new BufferedReader(tmp);
                    String str;
                    StringBuilder buf = new StringBuilder();
                    if ((str = reader.readLine()) != null) {
                        title = str;
                    }
                    while ((str = reader.readLine()) != null) {
                        buf.append(str).append("\n");
                    } in.close();
                    content = buf.toString();
                }
            } catch (java.io.FileNotFoundException ignored) {} catch (Throwable t) {
                Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return new Note(title, content);
    }
}
