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

    private SharedPrefHelper sharedPrefHelper;

    public ArrayListAdapter(Context context, String[] values) {
        super(context, R.layout.activity_cache_list, values);
        this.context = context;
        this.values = values;
        sharedPrefHelper = new SharedPrefHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_cache_list, parent, false);
        final TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        final TextView secondLine = rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        ImageView selectionIconView = rowView.findViewById(R.id.selectionIcon);
        firstLine.setText(values[position]);

        String s = values[position];

        if(("Treasure " + sharedPrefHelper.getCacheSelection()).equals(s)) {
            selectionIconView.setImageResource(R.drawable.baseline_flag_black_24);
        } else {
            selectionIconView.setVisibility(View.GONE);
        }

        if((sharedPrefHelper.getCache1State() && s.startsWith("Treasure 1")) ||
                (sharedPrefHelper.getCache2State() && s.startsWith("Treasure 2")) ||
                (sharedPrefHelper.getCache3State() && s.startsWith("Treasure 3")) ||
                (sharedPrefHelper.getCache4State() && s.startsWith("Treasure 4")) ) {
            imageView.setImageResource(R.drawable.sharp_done_outline_black_24);
            secondLine.setText(values[position] + " has been found!");
        }else {
            imageView.setImageResource(R.drawable.round_cancel_black_24);
            secondLine.setText(values[position] + " hasn't been found yet!");
        }

        return rowView;
    }


    /* //Optimized for performance.
    private final Activity context;
    private final String[] names;

    static class ViewHolder {
        public TextView text;
        public TextView secondText;
        public ImageView image;
    }

    public ArrayListAdapter(Activity context, String[] names) {
        super(context, R.layout.activity_cache_list, names);
        this.context = context;
        this.names = names;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.activity_cache_list, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.firstLine);
            viewHolder.secondText = rowView.findViewById(R.id.secondLine);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.icon);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        String s = names[position];
        holder.text.setText(s);
        if (s.startsWith("Treasure 1") || s.startsWith("Treasure 2")) {
            holder.image.setImageResource(R.drawable.sharp_done_outline_black_24);
            holder.secondText.setText(s + " has been found!");
        } else {
            holder.image.setImageResource(R.drawable.round_cancel_black_24);
            holder.secondText.setText(s + " hasn't been found yet!");
        }

        return rowView;
    }
    */
}