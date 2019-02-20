package com.example.roomviewmodellivedatarecyclerviewmvvmpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    EditText editTextTitle, editTextDescription;
    NumberPicker numberPicker;
    public static final String EXTRA_ID="com.example.roomviewmodellivedatarecyclerviewmvvmpractice.EXTRA_ID";

    public static final String EXTRA_TITLE="com.example.roomviewmodellivedatarecyclerviewmvvmpractice.EXTRA_TILE";
    public static final String EXTRA_DESCRIPTION="com.example.roomviewmodellivedatarecyclerviewmvvmpractice.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY="com.example.roomviewmodellivedatarecyclerviewmvvmpractice.EXTRA_PRIORITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextDescription = findViewById(R.id.description);
        editTextTitle = findViewById(R.id.title);
        numberPicker = findViewById(R.id.number_picker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent=getIntent();
        if(intent.hasExtra(EXTRA_ID)){

            setTitle("EditNote");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));

            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));//we have to put default value
        }else{
            setTitle("Add Note");
        }
    }

    private void saveNote(){

        String title= editTextTitle.getText().toString().trim();
        String description=editTextDescription.getText().toString().trim();
        int priority=numberPicker.getValue();

        if(title.isEmpty()||description.isEmpty()){

            Toast.makeText(this, "Please insert the title and description", Toast.LENGTH_SHORT).show();
            return;

        }
        Intent intent=new Intent();
        intent.putExtra(EXTRA_TITLE,title);
        intent.putExtra(EXTRA_DESCRIPTION,description);
        intent.putExtra(EXTRA_PRIORITY,priority);

        int id=getIntent().getIntExtra(EXTRA_ID,-1);// if there is no update that means it will be -1

        if(id!=-1){

            intent.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK,intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save:
                saveNote();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }
}
