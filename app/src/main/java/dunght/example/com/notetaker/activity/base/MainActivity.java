package dunght.example.com.notetaker.activity.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import dunght.example.com.notetaker.R;
import dunght.example.com.notetaker.config.Define;
import dunght.example.com.notetaker.custom.adapter.NoteAdapter;
import dunght.example.com.notetaker.db.db.table.NoteData;
import dunght.example.com.notetaker.model.Note;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private GridView gvNote;
    private NoteData noteData;
    private ArrayList<Note> listNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.tb_note);
        gvNote = (GridView)findViewById(R.id.gv_note);

        //aaaa

        noteData = new NoteData(this);
        listNote = noteData.getAllNote();

        NoteAdapter noteAdapter = new NoteAdapter(this, listNote);
        gvNote.setAdapter(noteAdapter);

        gvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra(Define.POSITION, i);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_add:
                startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
