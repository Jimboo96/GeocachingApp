package geocaching.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class PreGameActivity extends ListActivity {
    private String cacheChoice;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[] { "Treasure 1", "Treasure 2", "Treasure 3", "Treasure 4" };
        ArrayListAdapter adapter = new ArrayListAdapter(this, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        cacheChoice = item;
        Toast.makeText(this, cacheChoice + " selected", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}