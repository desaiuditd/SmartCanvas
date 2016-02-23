package in.incognitech.smartcanvas.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import in.incognitech.smartcanvas.R;
import in.incognitech.smartcanvas.util.BitmapOptimizer;

/**
 * Created by udit on 06/02/16.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    private static List<Note> noteList;

    static class ViewHolder {
        ImageView imageView;
    }

    public NoteAdapter(Context context, int resource, List<Note> noteList) {
        super(context, resource, noteList);
        this.setNoteList(noteList);
    }

    public static List<Note> getNoteList() {
        return noteList;
    }

    public static void setNoteList(List<Note> noteList) {
        NoteAdapter.noteList = noteList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Note note = NoteAdapter.getNoteList().get(position);

        View row;
        ViewHolder holder;

        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.note_row, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.rowImage);
            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        // Set the image
        holder.imageView.setImageBitmap(BitmapOptimizer.decodeSampledBitmapFromFile(note.getPhotoPath(), 200, 200));

        return row;
    }
}
