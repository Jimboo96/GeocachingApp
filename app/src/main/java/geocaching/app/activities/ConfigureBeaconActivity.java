package geocaching.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.connection.DeviceConnection;
import com.estimote.sdk.connection.DeviceConnectionCallback;
import com.estimote.sdk.connection.DeviceConnectionProvider;
import com.estimote.sdk.connection.exceptions.DeviceConnectionException;
import com.estimote.sdk.connection.scanner.ConfigurableDevice;
import com.estimote.sdk.connection.scanner.ConfigurableDevicesScanner;
import com.estimote.sdk.connection.settings.storage.StorageManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import geocaching.app.R;

public class ConfigureBeaconActivity extends AppCompatActivity {

    private static final String TAG = "Beacon config";

    private ConfigurableDevice configurableDevice;
    private DeviceConnection connection;
    private DeviceConnectionProvider connectionProvider;
    private TextView beaconIDData,beaconData,beaconVersion;
    private EditText beaconIDEdit;
    private Button deleteButton, resetButton, beaconIDEditSave;
    private ToggleButton gameToggle;
    String beaconVersionStr = "";
    String beaconIdStr = "";
    int beaconVersionInt = 0;
    Map<String, String> beaconDataMap = new LinkedHashMap<>();
    List<String> availableBeacons = new ArrayList<String>();
    private ConfigurableDevicesScanner devicesScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_beacon);

        EstimoteSDK.initialize(getApplicationContext(), "my-app-2vi", "8d4cf42cfb254f328f55eeaf051f8b90");
        EstimoteSDK.enableDebugLogging(false);

        beaconIDData = (TextView) findViewById(R.id.beaconIDData);
        beaconData = (TextView) findViewById(R.id.beaconData);
        beaconIDEdit = (EditText) findViewById(R.id.beaconIDEdit);
        beaconVersion = (TextView) findViewById(R.id.beaconVersion);

        // Button for resetting the beacon data
        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connection.isConnected()){
                    disableObjects();
                    resetBeaconData();
                }
            }
        });

        // Button for deleting the beacon data
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connection.isConnected()){
                    disableObjects();
                    deleteBeaconData();
                }
            }
        });

        gameToggle = (ToggleButton) findViewById(R.id.gameToggle);
        gameToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connection.isConnected()){
                    disableObjects();
                    Log.d(TAG,gameToggle.getText().toString());
                    if (gameToggle.getText().toString().equals("Enabled")){
                        beaconDataMap.put("BeaconGame","true");
                    }
                    else if (gameToggle.getText().toString().equals("Disabled")){
                        beaconDataMap.put("BeaconGame","false");
                    }
                    Log.d(TAG,beaconDataMap.toString());
                    connection.settings.storage.writeStorage(beaconDataMap, new StorageManager.WriteCallback() {
                        @Override
                        public void onSuccess() {
                            enableObjects();
                            setText(beaconData, beaconDataMap.toString());
                        }

                        @Override
                        public void onFailure(DeviceConnectionException e) {

                        }
                    });
                }
            }
        });

        beaconIDEditSave = (Button) findViewById(R.id.beaconIDEditSave);
        beaconIDEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connection.isConnected()){
                    disableObjects();
                    Log.d(TAG,beaconIDEdit.getText().toString());
                    beaconDataMap.put("BeaconId",beaconIDEdit.getText().toString());
                    beaconDataMap.put("BeaconVersion","0");
                    connection.settings.storage.writeStorage(beaconDataMap, new StorageManager.WriteCallback() {
                        @Override
                        public void onSuccess() {
                            resetBeaconData();
                        }

                        @Override
                        public void onFailure(DeviceConnectionException e) {

                        }
                    });
                }
            }
        });

        devicesScanner = new ConfigurableDevicesScanner(this);
        devicesScanner.scanForDevices(new ConfigurableDevicesScanner.ScannerCallback() {
            @Override
            public void onDevicesFound(final List<ConfigurableDevicesScanner.ScanResultItem> list) {
                for (ConfigurableDevicesScanner.ScanResultItem item : list) {
                    Log.d(TAG, item.device.deviceId.toString());
                    availableBeacons.add(item.device.deviceId.toString());
                    beaconIDData.setText(item.device.deviceId.toString());
                    configurableDevice = (ConfigurableDevice) item.device;
                    connectionProvider = new DeviceConnectionProvider(ConfigureBeaconActivity.this);
                    connectToDevice();
                }
                devicesScanner.stopScanning();
                //beaconIDData.setText(configurableDevice.deviceId.toString());
            }
        });
        /*
        Intent intent = getIntent();
        configurableDevice = (ConfigurableDevice) intent.getParcelableExtra(BeaconActivity.EXTRA_SCAN_RESULT_ITEM_DEVICE);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (connection != null && connection.isConnected())
            connectToDevice();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (connection != null && connection.isConnected())
            connection.close();
    }

    private void deleteBeaconData(){
        Map<String, String> newBeaconData = new LinkedHashMap<>();
        beaconDataMap = newBeaconData;
        connection.settings.storage.writeStorage(beaconDataMap, new StorageManager.WriteCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Data write was a success");
                setText(beaconData, beaconDataMap.toString());
            }

            @Override
            public void onFailure(DeviceConnectionException e) {

            }
        });
    }

    private void resetBeaconData(){
        beaconVersionStr = beaconDataMap.values().toArray()[2].toString();
        beaconVersionInt = Integer.parseInt(beaconVersionStr);
        beaconVersionInt++;
        beaconVersionStr = Integer.toString(beaconVersionInt);

        beaconIdStr = beaconDataMap.values().toArray()[1].toString();

        Map<String, String> newBeaconData = new LinkedHashMap<>();
        newBeaconData.put("BeaconGame","false");
        newBeaconData.put("BeaconId", beaconIdStr);
        newBeaconData.put("BeaconVersion", beaconVersionStr);
        newBeaconData.put ("Beacon", "0-0-0");
        beaconDataMap = newBeaconData;
        connection.settings.storage.writeStorage(beaconDataMap, new StorageManager.WriteCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Data write was a success");
                setText(beaconData, beaconDataMap.toString());
                getData();
                enableObjects();
            }

            @Override
            public void onFailure(DeviceConnectionException e) {
                Log.d(TAG,"Data write was a failure: " + e.getLocalizedMessage());
            }
        });
    }

    private void getData(){
        // Check if beacon game is enabled or disabled
        final String mapValueBeaconGame = beaconDataMap.values().toArray()[0].toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mapValueBeaconGame.equals("true")){
                    gameToggle.setChecked(true);
                    gameToggle.setChecked(gameToggle.isChecked());
                }
                else if (mapValueBeaconGame.equals("false")){
                    gameToggle.setChecked(false);
                }
                Log.d(TAG,gameToggle.getText().toString());
            }
        });

        // Check the beacon ID
        final String mapValueBeaconID = beaconDataMap.values().toArray()[1].toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                beaconIDEdit.setText(mapValueBeaconID);
            }
        });

        // Check the beacon data version
        final String mapValueBeaconVersion = beaconDataMap.values().toArray()[2].toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                beaconVersion.setText(mapValueBeaconVersion);
            }
        });

    }

    private void enableObjects(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deleteButton.setEnabled(true);
                resetButton.setEnabled(true);
                gameToggle.setEnabled(true);
                beaconIDEdit.setEnabled(true);
                beaconIDEditSave.setEnabled(true);
            }
        });
    }

    private void disableObjects(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deleteButton.setEnabled(false);
                resetButton.setEnabled(false);
                gameToggle.setEnabled(false);
                beaconIDEdit.setEnabled(false);
                beaconIDEditSave.setEnabled(false);
            }
        });
    }

    private void connectToDevice() {
        if (connection == null || !connection.isConnected()) {
            connectionProvider.connectToService(new DeviceConnectionProvider.ConnectionProviderCallback() {
                @Override
                public void onConnectedToService() {
                    connection = connectionProvider.getConnection(configurableDevice);
                    connection.connect(new DeviceConnectionCallback() {
                        @Override
                        public void onConnected() {
                            connection.settings.storage.readStorage(new StorageManager.ReadCallback() {
                                @Override
                                public void onSuccess(Map<String, String> map) {
                                    beaconDataMap = map;
                                    if (beaconDataMap.containsKey("BeaconGame")){
                                        setText(beaconData, beaconDataMap.toString());
                                        enableObjects();
                                        getData();
                                    }
                                    else {
                                        Map<String, String> newBeaconData = new LinkedHashMap<>();
                                        newBeaconData.put("BeaconGame","false");
                                        newBeaconData.put("BeaconId","CHANGE THE ID");
                                        newBeaconData.put("BeaconVersion", "1");
                                        newBeaconData.put ("Beacon", "0-0-0");
                                        beaconDataMap = newBeaconData;
                                        connection.settings.storage.writeStorage(beaconDataMap, new StorageManager.WriteCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Log.d(TAG, "Data write was a success");
                                                setText(beaconData, beaconDataMap.toString());
                                                enableObjects();
                                                getData();
                                            }

                                            @Override
                                            public void onFailure(DeviceConnectionException e) {
                                                Log.d(TAG,"Data write was a failure: " + e.getLocalizedMessage());
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(DeviceConnectionException e) {

                                }
                            });
                        }

                        @Override
                        public void onDisconnected() { }

                        @Override
                        public void onConnectionFailed(DeviceConnectionException e) {
                            Log.d(TAG, e.getMessage());
                        }
                    });
                }
            });
        }
    }

    private void setText(final TextView text, final String value) {
        Log.d(TAG, "running setText");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
}
