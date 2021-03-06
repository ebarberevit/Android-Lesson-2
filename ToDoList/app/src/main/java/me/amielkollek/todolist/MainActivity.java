package me.amielkollek.todolist;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ArrayAdapter<String> todoListAdapter;
    private ArrayList<String> todoListItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView todoList = (ListView) findViewById(R.id.todo_list);

        for (int i = 1; i<=50; i++){
            todoListItems.add("Item "+i); //dummy items for testing
        }

        todoListAdapter = new ArrayAdapter<String>(
                this, // the context
                R.layout.todo_item, // the list item layout
                todoListItems); // the list content

        todoList.setAdapter(todoListAdapter);

        Button addButton = (Button) findViewById(R.id.add_button);
        final Intent intent = new Intent(this, AddToDoItem.class);

        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(intent);
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
