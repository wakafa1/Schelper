package cn.wakafa.listview.Others;

/**
 * Created by wakafa on 2017/5/21.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.wakafa.listview.Others.Note;
import cn.wakafa.listview.R;

public class NoteAdapter extends ArrayAdapter<Note> {

    private int resourceId;

    public NoteAdapter(Context context, int textViewResourceId,
                       List<Note> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = getItem(position); // 获取当前项的 Note 实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.noteImage = (ImageView) view.findViewById (R.id.note_image);
            viewHolder.noteName = (TextView) view.findViewById (R.id.note_name);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.noteImage.setImageResource(note.getImageId());
        viewHolder.noteName.setText(note.getName());
        return view;
    }

    class ViewHolder {
        ImageView noteImage;
        TextView noteName;
    }

}
