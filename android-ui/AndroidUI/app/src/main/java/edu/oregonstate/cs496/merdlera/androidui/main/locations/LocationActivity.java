package edu.oregonstate.cs496.merdlera.androidui.main.locations;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.oregonstate.cs496.merdlera.androidui.R;
import edu.oregonstate.cs496.merdlera.androidui.databinding.ActivityLocationsBinding;
import edu.oregonstate.cs496.merdlera.androidui.databinding.CheckinListItemViewBinding;

import java.util.List;

/**
 * Created by alec on 8/3/17.
 */

public class LocationActivity extends AppCompatActivity {

    private CheckIn newCheckIn;
    private CheckInDataSource dataSource;
    private ListView checkInListView;
    private final int requestLocation = 200;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        // Initialize new check-in form
        newCheckIn = new CheckIn();

        // Initialize data source
        dataSource = new CheckInDataSource(this);
        dataSource.open();

        // Initialize location access
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Set up data binding
        ActivityLocationsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_locations);
        binding.setNewCheckIn(newCheckIn);

        // Populate existing check-in list
        loadCheckIns();
    }

    public void onSubmit(View view) {
        if (newCheckIn.getComment().length() > 0) {
            // Check for location permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestLocation);
            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // Create check-in with current location
                newCheckIn.setLatitude(Double.toString(location.getLatitude()));
                newCheckIn.setLongitude(Double.toString(location.getLongitude()));
                createCheckIn(newCheckIn);
            }
        }
    }

    private void createCheckIn(CheckIn checkIn) {
        dataSource.createCheckIn(checkIn);
        newCheckIn.setComment("");
        loadCheckIns();
    }

    private void loadCheckIns() {
        List<CheckIn> allCheckIns = dataSource.getAllCheckIns();
        checkInListView = (ListView) findViewById(R.id.listview);
        checkInListView.setAdapter(new CheckInArrayAdapter(this, allCheckIns));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case requestLocation: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            newCheckIn.setLatitude(Double.toString(location.getLatitude()));
                            newCheckIn.setLongitude(Double.toString(location.getLongitude()));
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    });

                    // Create check-in with current location
                    if (newCheckIn.getLatitude().length() == 0 || newCheckIn.getLongitude().length() == 0) {
                        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        newCheckIn.setLatitude(Double.toString(currentLocation.getLatitude()));
                        newCheckIn.setLongitude(Double.toString(currentLocation.getLongitude()));
                        createCheckIn(newCheckIn);
                    }
                } else {
                    // Create check-in with default location
                    newCheckIn.setLatitude("44.5");
                    newCheckIn.setLongitude("-123.2");
                    createCheckIn(newCheckIn);
                }
            }
        }
    }

    private class CheckInArrayAdapter extends ArrayAdapter<CheckIn> {

        private final Context context;
        private final List<CheckIn> checkIns;

        public CheckInArrayAdapter(Context context, List<CheckIn> checkIns) {
            super(context, -1, checkIns);
            this.context = context;
            this.checkIns = checkIns;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CheckinListItemViewBinding binding = CheckinListItemViewBinding.inflate(inflater, parent, false);
            binding.setCheckIn(checkIns.get(position));

            return binding.getRoot();
        }
    }
}
