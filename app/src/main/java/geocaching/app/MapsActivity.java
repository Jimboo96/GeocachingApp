package geocaching.app;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int indexOfSelectedCache = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        indexOfSelectedCache = getIntent().getIntExtra("treasureNum", 0) + 1;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add markers and move the camera
        /*ArrayList<LatLng> coordinatesArray = new ArrayList<>();
        coordinatesArray.add(new LatLng(64.999970, 25.510664));
        coordinatesArray.add(new LatLng(64.996075, 25.501013));

        for(LatLng latLng : coordinatesArray) {
            index++;
            treasureMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Treasure " + index).snippet("Directional location of treasure cache " + indexOfSelectedCache + 1));
            treasureMarker.showInfoWindow();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinatesArray.get(indexOfSelectedCache)));*/

        LatLng cacheCoordinates = new LatLng(64.999970, 25.510664);
        Marker treasureMarker = mMap.addMarker(new MarkerOptions().position(cacheCoordinates).title("Treasure " + indexOfSelectedCache).snippet("Directional location of treasure cache " + indexOfSelectedCache));
        treasureMarker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cacheCoordinates));
    }
}
