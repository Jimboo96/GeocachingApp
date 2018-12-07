package geocaching.app.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import geocaching.app.R;

public class ArrayListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    private SharedPrefHelper sharedPrefHelper;

    public ArrayListAdapter(Context context, String[] values) {
        super(context, R.layout.fragment_cache_list, values);
        this.context = context;
        this.values = values;
        sharedPrefHelper = new SharedPrefHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_cache_list, parent, false);
        final TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        final TextView secondLine = rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        ImageView selectionIconView = rowView.findViewById(R.id.selectionIcon);
        firstLine.setText(values[position]);

        String s = values[position];

        // Set a flag on selected cache on list.
        if(("Treasure " + sharedPrefHelper.getCacheSelection()).equals(s)) {
            selectionIconView.setImageResource(R.drawable.baseline_flag_black_24);
        } else {
            selectionIconView.setVisibility(View.GONE);
        }

        // Set checked state on a cache in list depending on if the cache has been found or not.
        if((sharedPrefHelper.getCache1State() && s.startsWith("Treasure 1")) ||
                (sharedPrefHelper.getCache2State() && s.startsWith("Treasure 2")) ||
                (sharedPrefHelper.getCache3State() && s.startsWith("Treasure 3")) ||
                (sharedPrefHelper.getCache4State() && s.startsWith("Treasure 4")) ) {
            firstLine.setText(context.getResources().getString(R.string.cache_found_text, values[position]));
            imageView.setImageResource(R.drawable.sharp_done_outline_black_24);
        }else {
            imageView.setImageResource(R.drawable.round_cancel_black_24);
            secondLine.setText(context.getResources().getString(R.string.cache_not_found_text, values[position]));
        }

        // Set scores on caches in list if it has been found.
        if((sharedPrefHelper.getCache1State() && s.startsWith("Treasure 1"))) {
            secondLine.setText(context.getResources().getString(R.string.list_score_text,sharedPrefHelper.getCache1Score()));
        }else if (sharedPrefHelper.getCache2State() && s.startsWith("Treasure 2")) {
            secondLine.setText(context.getResources().getString(R.string.list_score_text,sharedPrefHelper.getCache2Score()));
        }else if (sharedPrefHelper.getCache3State() && s.startsWith("Treasure 3")) {
            secondLine.setText(context.getResources().getString(R.string.list_score_text,sharedPrefHelper.getCache3Score()));
        }else if (sharedPrefHelper.getCache4State() && s.startsWith("Treasure 4")) {
            secondLine.setText(context.getResources().getString(R.string.list_score_text,sharedPrefHelper.getCache4Score()));
        }

        if((sharedPrefHelper.getCache1TimeSet() && s.startsWith("Treasure 1"))) {
            secondLine.setText(context.getResources().getString(R.string.list_score_timer_text,sharedPrefHelper.getCache1Score(),sharedPrefHelper.getCache1Minutes(),sharedPrefHelper.getCache1Seconds()));
        }else if (sharedPrefHelper.getCache2TimeSet()&& s.startsWith("Treasure 2")) {
            secondLine.setText(context.getResources().getString(R.string.list_score_timer_text,sharedPrefHelper.getCache2Score(),sharedPrefHelper.getCache2Minutes(),sharedPrefHelper.getCache2Seconds()));
        }else if (sharedPrefHelper.getCache3TimeSet() && s.startsWith("Treasure 3")) {
            secondLine.setText(context.getResources().getString(R.string.list_score_timer_text,sharedPrefHelper.getCache3Score(),sharedPrefHelper.getCache3Minutes(),sharedPrefHelper.getCache3Seconds()));
        }else if (sharedPrefHelper.getCache4TimeSet() && s.startsWith("Treasure 4")) {
            secondLine.setText(context.getResources().getString(R.string.list_score_timer_text,sharedPrefHelper.getCache4Score(),sharedPrefHelper.getCache4Minutes(),sharedPrefHelper.getCache4Seconds()));
        }

        return rowView;
    }
}