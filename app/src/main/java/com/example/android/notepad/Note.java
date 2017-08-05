package com.example.android.notepad;

public class Note {

    private String title;
    private String content;

    public Note(String t, String c){
        title = t;
        content = c;
    }

    public void setTitle(String t){
        title = t;
    }

    public void setContent(String c){
        content = c;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

}
