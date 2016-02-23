package in.incognitech.smartcanvas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import in.incognitech.smartcanvas.db.NoteDbHelper;
import in.incognitech.smartcanvas.model.Note;
import in.incognitech.smartcanvas.model.NoteAdapter;

public class GridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Note> noteList = new ArrayList<Note>();
        SQLiteDatabase db = new NoteDbHelper(this).getWritableDatabase();
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {NoteDbHelper.ID_COLUMN, NoteDbHelper.PHOTO_PATH_COLUMN};
        Cursor cursor = db.query(NoteDbHelper.DATABASE_TABLE, resultColumns, where, whereArgs, groupBy, having, order);
        int count = 0;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String photo_path = cursor.getString(1);
            noteList.add(count++, new Note(id, photo_path));
        }

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new NoteAdapter(this, R.layout.note_row, noteList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), NoteActivity.class);
                i.putExtra("note_id", position);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean returnFlag = super.onOptionsItemSelected(item);
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                returnFlag = true;
                break;
        }
        return returnFlag;
    }
}
