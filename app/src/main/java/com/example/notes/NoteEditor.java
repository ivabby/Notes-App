package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditor extends AppCompatActivity {

    private static final String TAG = "NoteEditor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = findViewById(R.id.editText);

        Intent intent = getIntent();
        final int noteId = intent.getIntExtra("noteId" , -1);


        if(noteId != -1){
            editText.setText(MainActivity.notes.get(noteId));

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "onTextChanged: " + s.toString());

                    MainActivity.notes.set(noteId , String.valueOf(s.toString()));

                    MainActivity.arrayAdapter.notifyDataSetChanged();

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes" , Context.MODE_PRIVATE);

                    HashSet<String> set = new HashSet<>(MainActivity.notes);

                    sharedPreferences.edit().putStringSet("notes" , set).apply();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        else {
            Log.d(TAG, "onCreate: noteId cannot be -1.");
        }

    }
}
