package geocaching.app.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import geocaching.app.R;
import geocaching.app.activities.MainActivity;
import geocaching.app.activities.MapsActivity;
import geocaching.app.helpers.ArrayListAdapter;
import geocaching.app.helpers.SharedPrefHelper;
import geocaching.app.interfaces.FragmentLoader;
import geocaching.app.interfaces.KeyEventListener;

public class CacheListFragment extends ListFragment implements KeyEventListener, FragmentLoader {
    private JSONObject jObject;
    private ArrayList<Integer> idArray;
    private ArrayList<Double> longitudeArray;
    private ArrayList<Double> latitudeArray;
    private SharedPrefHelper sharedPrefHelper;

    public CacheListFragment(){
        idArray = new ArrayList<>();
        latitudeArray = new ArrayList<>();
        longitudeArray = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefHelper = new SharedPrefHelper(getContext());
        new JsonTask().execute("http://www.students.oamk.fi/~t6bjji00/json_files/coordinates.json");
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, final long id) {
        if(sharedPrefHelper.getCacheSelection() == 0) {
            sharedPrefHelper.setCacheSelection((int) id + 1);
            Toast.makeText(getContext(), "Treasure " + (id+1) + " selected!", Toast.LENGTH_LONG).show();
            startMapsIntent(position);
        }
        else if(sharedPrefHelper.getCacheSelection() != (int) (id+1)) {
            final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE: {
                            break;
                        }
                        case DialogInterface.BUTTON_NEGATIVE: {
                            sharedPrefHelper.setCacheSelection((int) id + 1);
                            Toast.makeText(getContext(), "Treasure " + (id+1) + " selected!", Toast.LENGTH_LONG).show();
                            startMapsIntent(position);
                            break;
                        }
                    }
                }};
            getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Are you sure you want to start searching for a different cache?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                        }
                    }
            );
        } else {
            startMapsIntent(position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            loadFragment(new MenuFragment());
            return false;
        }
        return onKeyDown(keyCode,event);
    }

    @Override
    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
        ((MainActivity)getActivity()).setCurrentFragment(fragment);
    }

    private void startMapsIntent(final int position) {
        sharedPrefHelper.setCoordinates(longitudeArray.get(position).floatValue(),latitudeArray.get(position).floatValue());
        Intent intent = new Intent(getContext(), MapsActivity.class);
        intent.putExtra("id",idArray.get(position));
        intent.putExtra("longitude",longitudeArray.get(position));
        intent.putExtra("latitude",latitudeArray.get(position));
        startActivity(intent);
    }

    private void parseJSON() {
        JSONObject treasures, treasure;
        String[] values;
        try {
            treasures = jObject.getJSONObject("treasures");
            values  = new String[treasures.length()];
            for (int i = 0;i<treasures.length();i++) {
                treasure = treasures.getJSONObject("treasure" + i);
                idArray.add(treasure.getInt("id"));
                longitudeArray.add(treasure.getDouble("longitude"));
                latitudeArray.add(treasure.getDouble("latitude"));
                values[i] = "Treasure " + (i + 1);
            }
            ArrayListAdapter adapter = new ArrayListAdapter(getContext(), values);
            setListAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                jObject = new JSONObject(result);
                parseJSON();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}