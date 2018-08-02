package dunght.example.com.notetaker.custom.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dunght.example.com.notetaker.R;
import dunght.example.com.notetaker.model.Note;

public class NoteAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Note> listNote;

    public NoteAdapter(Activity activity, ArrayList<Note> listNote) {
        this.activity = activity;
        this.listNote = listNote;
    }

    @Override
    public int getCount() {
        return listNote.size();
    }

    @Override
    public Object getItem(int i) {
        return listNote.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        convertView = activity.getLayoutInflater().inflate(R.layout.item_note, null);

        TextView tvTitle = (TextView)convertView.findViewById(R.id.tv_title_custom);
        TextView tvContent = (TextView)convertView.findViewById(R.id.tv_content_custom);
        TextView tvDatatime = (TextView)convertView.findViewById(R.id.tv_datetime_custom);
        ImageView ivAlarm = (ImageView)convertView.findViewById(R.id.iv_alarm);
        View view = (View)convertView.findViewById(R.id.view_line);
        CardView cardView = (CardView)convertView.findViewById(R.id.card_view);

        tvTitle.setText(listNote.get(i).getTitle());
        tvContent.setText(listNote.get(i).getContent());
        tvDatatime.setText(listNote.get(i).getDatetimeCreate());
        ivAlarm.setImageResource(R.drawable.ic_alarm);

        if(listNote.get(i).getColor() != -1) {
            cardView.setCardBackgroundColor(listNote.get(i).getColor());
        }

        return convertView;
    }
}
