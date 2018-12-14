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
    /*
    private static final String TAG = "Beacon config";
    private ConfigurableDevice configurableDevice;
    private ConfigurableDevicesScanner devicesScanner;
    private DeviceConnection connection;
    private DeviceConnectionProvider connectionProvider;*/
    TextView textView1;
    private SharedPrefHelper sharedPrefHelper;

    //Map<String, String> beaconDataMap = new LinkedHashMap<>();
    //ArrayList<ConfigurableDevice> deviceArray = new ArrayList<>();

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

        //devicesScanner = new ConfigurableDevicesScanner(getContext());
        //deviceScan();

        Button button1 = view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // deviceScan();
                getGuestBookText();
            }
        });

        Button button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (connection.isConnected()){
                    beaconDataMap.put("BeaconMessage",editText1.getText().toString());
                    connection.settings.storage.writeStorage(beaconDataMap, new StorageManager.WriteCallback() {
                        @Override
                        public void onSuccess() {
                            //enableObjects();
                            textView1.setText(getResources().getString(R.string.guest_book_text, editText1.getText().toString()));
                        }

                        @Override
                        public void onFailure(DeviceConnectionException e) {

                        }
                    });
                }*/
                setGuestBookText(editText1.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (connection != null && connection.isConnected())
            connectToDevice();*/
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (connection != null && connection.isConnected())
            connection.close();*/
    }
/*
    private void deleteBeaconData(){
        Map<String, String> newBeaconData = new LinkedHashMap<>();
        beaconDataMap = newBeaconData;
        connection.settings.storage.writeStorage(beaconDataMap, new StorageManager.WriteCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Data write was a success");
                //setText(beaconData, beaconDataMap.toString());
            }

            @Override
            public void onFailure(DeviceConnectionException e) {

            }
        });
    }

    private void deviceScan() {
        devicesScanner.scanForDevices(new ConfigurableDevicesScanner.ScannerCallback() {
            @Override
            public void onDevicesFound(final List<ConfigurableDevicesScanner.ScanResultItem> list) {
                for (ConfigurableDevicesScanner.ScanResultItem item : list) {
                    Log.d(TAG, item.device.deviceId.toString());
                    connectionProvider = new DeviceConnectionProvider(getContext());
                    deviceArray.add(item.device);
                }
                if(deviceArray != null) {
                    configurableDevice = deviceArray.get(0);
                }
                if(configurableDevice != null) {
                    connectToDevice();
                }
                devicesScanner.stopScanning();
                //beaconIDData.setText(configurableDevice.deviceId.toString());
            }
        });
    }

    private void getData(){
        // Check if beacon game is enabled or disabled
        final String mapValueBeaconGame = beaconDataMap.values().toArray()[0].toString();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mapValueBeaconGame.equals("true")){
                   // gameToggle.setChecked(true);
                    //ameToggle.setChecked(gameToggle.isChecked());
                }
                else if (mapValueBeaconGame.equals("false")){
                    //gameToggle.setChecked(false);
                }
                //Log.d(TAG,gameToggle.getText().toString());
            }
        });

        // Check the beacon ID
        final String mapValueBeaconID = beaconDataMap.values().toArray()[1].toString();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // beaconIDEdit.setText(mapValueBeaconID);
            }
        });

        // Check the beacon data version
        final String mapValueBeaconVersion = beaconDataMap.values().toArray()[2].toString();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // beaconVersion.setText(mapValueBeaconVersion);
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
                                    if (beaconDataMap.containsKey("BeaconMessage")){
                                        setText(textView1, beaconDataMap.toString());
                                        //enableObjects();
                                        //getData();
                                    }
                                    else {
                                        Map<String, String> newBeaconData = new LinkedHashMap<>();
                                        newBeaconData.put("BeaconMessage","Hello world");
                                        newBeaconData.put("BeaconId","CHANGE THE ID");
                                        newBeaconData.put("BeaconVersion", "1");
                                        newBeaconData.put ("Beacon", "0-0-0");
                                        beaconDataMap = newBeaconData;
                                        connection.settings.storage.writeStorage(beaconDataMap, new StorageManager.WriteCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Log.d(TAG, "Data write was a success");
                                                setText(textView1, beaconDataMap.toString());
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }*/

    //For demo purposes.
    private void setGuestBookText(String guestBookText) {
        sharedPrefHelper.setGuestBookText(guestBookText);
        getGuestBookText();
    }

    private void getGuestBookText() {
        textView1.setText(getResources().getString(R.string.guest_book_text, sharedPrefHelper.getGuestBookText()));
    }
}
