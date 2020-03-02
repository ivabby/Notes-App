package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView listView;

    public static ArrayList<String> notes = new ArrayList<>();

    public static ArrayAdapter arrayAdapter;

    TextView empty;

    //  Creating the options menu with add note
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_main , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.addNote:

                empty.setVisibility(View.GONE);
                notes.add(0 , "");

                Intent intent = new Intent(MainActivity.this , NoteEditor.class);
                intent.putExtra("noteId" , 0);
                startActivity(intent);


                return true;

            default:

                return false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        empty = findViewById(R.id.empty);

//        notes.add("Example note");


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes" , Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes" , null);

        if(set == null){
            empty.setVisibility(View.VISIBLE);
        } else{
            notes = new ArrayList<>(set);
        }

        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 , notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: clicked item is " + position);

                Intent intent = new Intent(MainActivity.this , NoteEditor.class);
                intent.putExtra("noteId" , position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete this note")
                        .setMessage("Are You Sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: deleting this item.");

                                notes.remove(position);

                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("om.example.notes" , Context.MODE_PRIVATE);

                                HashSet<String> set = new HashSet<>(MainActivity.notes);

                                sharedPreferences.edit().putStringSet("notes" , set).apply();
                            }
                        })
                        .setNegativeButton("No" , null)
                        .show();

                return true;
            }
        });
    }
}
