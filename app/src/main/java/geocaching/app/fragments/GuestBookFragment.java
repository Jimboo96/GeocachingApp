package geocaching.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.estimote.sdk.EstimoteSDK;

import geocaching.app.R;
import geocaching.app.helpers.SharedPrefHelper;

public class GuestBookFragment extends Fragment{
    private SharedPrefHelper sharedPrefHelper;
    TextView textView1;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guest_book, container, false);

        sharedPrefHelper = new SharedPrefHelper(getContext());

        EstimoteSDK.initialize(getContext().getApplicationContext(), "my-app-2vi", "8d4cf42cfb254f328f55eeaf051f8b90");
        EstimoteSDK.enableDebugLogging(false);

        textView1 = view.findViewById(R.id.message);
        getGuestBookText();

        final EditText editText1 = view.findViewById(R.id.editMessage);

        Button button1 = view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGuestBookText();
            }
        });

        Button button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGuestBookText(editText1.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //For demo purposes.
    private void setGuestBookText(String guestBookText) {
        sharedPrefHelper.setGuestBookText(guestBookText);
        getGuestBookText();
    }

    private void getGuestBookText() {
        textView1.setText(getResources().getString(R.string.guest_book_text, sharedPrefHelper.getGuestBookText()));
    }
}
