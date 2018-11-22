package geocaching.app;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class CacheListActivity extends ListActivity {
    private ProgressDialog pd;
    private JSONObject jObject;

    protected ArrayList<Integer> idArray = new ArrayList<>();
    protected ArrayList<Boolean> completedArray = new ArrayList<>();
    protected ArrayList<Double> longitudeArray = new ArrayList<>();
    protected ArrayList<Double> latitudeArray = new ArrayList<>();
    protected String[] values;

    private SharedPrefHelper sharedPrefHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefHelper = new SharedPrefHelper(this);
        new JsonTask().execute("http://www.students.oamk.fi/~t6bjji00/json_files/coordinates.json");
    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position, final long id) {
        if(sharedPrefHelper.getCacheSelection() == 0) {
            sharedPrefHelper.setCacheSelection((int) id + 1);
            Toast.makeText(CacheListActivity.this, "Treasure " + (id+1) + " selected!", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(CacheListActivity.this, "Treasure " + (id+1) + " selected!", Toast.LENGTH_LONG).show();
                            startMapsIntent(position);
                            break;
                        }
                    }
                }};
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CacheListActivity.this);
                            builder.setMessage("Are you sure you want to start searching for a different cache?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                        }
                    }
            );
        } else {
            startMapsIntent(position);
        }
    }

    void startMapsIntent(final int position) {
        Intent intent = new Intent(CacheListActivity.this, MapsActivity.class);
        intent.putExtra("id",idArray.get(position));
        intent.putExtra("longitude",longitudeArray.get(position));
        intent.putExtra("latitude",latitudeArray.get(position));
        startActivity(intent);
    }

    void parseJSON() {
        JSONObject treasures, treasure;
        try {
            treasures = jObject.getJSONObject("treasures");
            values  = new String[treasures.length()];
            for (int i = 0;i<treasures.length();i++) {
                treasure = treasures.getJSONObject("treasure" + i);
                idArray.add(treasure.getInt("id"));
                completedArray.add(treasure.getBoolean("completed"));
                longitudeArray.add(treasure.getDouble("longitude"));
                latitudeArray.add(treasure.getDouble("latitude"));
                values[i] = "Treasure " + (i + 1);
            }
            ArrayListAdapter adapter = new ArrayListAdapter(CacheListActivity.this, values);
            setListAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(CacheListActivity.this);
            pd.setMessage("Loading cache locations...");
            pd.setCancelable(false);
            pd.show();
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
            if (pd.isShowing()){
                pd.dismiss();
            }

            try {
                jObject = new JSONObject(result);
                parseJSON();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}