package geocaching.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArrayListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public ArrayListAdapter(Context context, String[] values) {
        super(context, R.layout.activity_pre_game, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_pre_game, parent, false);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        firstLine.setText(values[position]);

        String s = values[position];
        if (s.startsWith("Treasure 1") || s.startsWith("Treasure 2")) {
            imageView.setImageResource(R.drawable.sharp_done_outline_black_24);
            secondLine.setText(values[position] + " has been found!");
        } else {
            imageView.setImageResource(R.drawable.round_cancel_black_24);
            secondLine.setText(values[position] + " hasn't been found yet!");
        }

        return rowView;
    }
}