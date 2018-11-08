package geocaching.app;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class PreGameActivity extends ListActivity {
    private ProgressDialog pd;

    private JSONObject jObject;
    protected int ID = 0;
    protected boolean completed = false;
    protected double longitude = 0;
    protected double latitude = 0;

    protected ArrayList<Integer> idArray;
    protected ArrayList<Boolean> completedArray;
    protected ArrayList<Double> longitudeArray;
    protected ArrayList<Double> latitudeArray;
    String[] values;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idArray = new ArrayList<>();
        completedArray = new ArrayList<>();
        longitudeArray = new ArrayList<>();
        latitudeArray = new ArrayList<>();

        final String URL = "http://www.students.oamk.fi/~t6bjji00/json_files/coordinates.json";
        new JsonTask().execute(URL);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //String item = (String) getListAdapter().getItem(position);
        //parseJSON(position);

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("id",idArray.get(position));
        intent.putExtra("longitude",longitudeArray.get(position));
        intent.putExtra("latitude",latitudeArray.get(position));

        //Toast.makeText(this, "id: " + ID + " completed: " + completed + " long: " + longitude + " lat: " + latitude, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    void parseJSON() {
        JSONObject treasures = null;
        JSONObject treasure = null;
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
            //treasure = treasures.getJSONObject("treasure" + position);
            //ID = treasure.getInt("id");
            //completed = treasure.getBoolean("completed");
            //longitude = treasure.getDouble("longitude");
            //latitude = treasure.getDouble("latitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean getCompleted(int position) {
        return completedArray.get(position);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(PreGameActivity.this);
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
                ArrayListAdapter adapter = new ArrayListAdapter(PreGameActivity.this, values);
                setListAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}