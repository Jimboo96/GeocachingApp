package geocaching.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import geocaching.app.R;

public class GuestBookFragment extends Fragment{
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guest_book, container, false);

        TextView textView1 = view.findViewById(R.id.message);
        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);
        EditText editText1 = view.findViewById(R.id.editMessage);

        return view;
    }
}
