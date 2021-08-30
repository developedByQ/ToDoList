package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    List<String> thingsToDo;
    Button add_btn;
    EditText add_tf;
    RecyclerView recycle_view;
    ToDoListAdapter toDoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_btn = findViewById(R.id.add_btn);
        add_tf = findViewById(R.id.add_tf);
        recycle_view = findViewById(R.id.recycle_view);


        loadItems();

        ToDoListAdapter.OnLongClickListener onLongClickListener = new ToDoListAdapter.OnLongClickListener() {
            @Override
            public void toDoLongClicked(int index) {
                thingsToDo.remove(index);
                toDoListAdapter.notifyItemRemoved(index);
                saveItems();
                Toast.makeText(getApplicationContext(), "Task Successfully Removed!", Toast.LENGTH_SHORT).show();
            }
        };

       toDoListAdapter = new ToDoListAdapter(thingsToDo, onLongClickListener);
        recycle_view.setAdapter(toDoListAdapter);
        recycle_view.setLayoutManager(new LinearLayoutManager(this));

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toDoItem = add_tf.getText().toString();
                thingsToDo.add(toDoItem);
                toDoListAdapter.notifyItemInserted(thingsToDo.size()-1);
                add_tf.setText("");
                saveItems();
                Toast.makeText(getApplicationContext(), "Task Successfully Added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    private void loadItems() {
        try {
            thingsToDo = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e) {
            Log.e("MainActivity", "Error loading items.");
            thingsToDo = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), thingsToDo);
        }catch (IOException e) {
            Log.e("MainActivity", "Error writing items.");
        }
    }
}